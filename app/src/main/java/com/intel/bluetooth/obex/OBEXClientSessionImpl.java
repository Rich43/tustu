package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.Utils;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.StreamConnection;
import javax.obex.Authenticator;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXClientSessionImpl.class */
public class OBEXClientSessionImpl extends OBEXSessionBase implements ClientSession {
    protected OBEXClientOperation operation;
    private static final String FQCN;
    private static final Vector fqcnSet;
    static Class class$com$intel$bluetooth$obex$OBEXClientSessionImpl;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$obex$OBEXClientSessionImpl == null) {
            clsClass$ = class$("com.intel.bluetooth.obex.OBEXClientSessionImpl");
            class$com$intel$bluetooth$obex$OBEXClientSessionImpl = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$obex$OBEXClientSessionImpl;
        }
        FQCN = clsClass$.getName();
        fqcnSet = new Vector();
        fqcnSet.addElement(FQCN);
    }

    public OBEXClientSessionImpl(StreamConnection conn, OBEXConnectionParams obexConnectionParams) throws Error, IOException {
        super(conn, obexConnectionParams);
        Utils.isLegalAPICall(fqcnSet);
        this.requestSent = false;
        this.isConnected = false;
        this.operation = null;
    }

    @Override // javax.obex.ClientSession
    public HeaderSet createHeaderSet() {
        return OBEXSessionBase.createOBEXHeaderSet();
    }

    @Override // javax.obex.ClientSession
    public HeaderSet connect(HeaderSet headers) throws IOException {
        return connectImpl(headers, false);
    }

    private HeaderSet connectImpl(HeaderSet headers, boolean retry) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        if (this.isConnected) {
            throw new IOException("Session already connected");
        }
        byte[] connectRequest = {16, 0, OBEXUtils.hiByte(this.obexConnectionParams.mtu), OBEXUtils.loByte(this.obexConnectionParams.mtu)};
        writePacketWithFlags(128, connectRequest, (OBEXHeaderSetImpl) headers);
        byte[] b2 = readPacket();
        if (b2.length < 6) {
            if (b2.length == 3) {
                throw new IOException(new StringBuffer().append("Invalid response from OBEX server ").append(OBEXUtils.toStringObexResponseCodes(b2[0])).toString());
            }
            throw new IOException("Invalid response from OBEX server");
        }
        int serverMTU = OBEXUtils.bytesToShort(b2[5], b2[6]);
        if (serverMTU < 255) {
            throw new IOException(new StringBuffer().append("Invalid MTU ").append(serverMTU).toString());
        }
        if (serverMTU < this.mtu) {
            this.mtu = serverMTU;
        }
        DebugLog.debug("mtu selected", this.mtu);
        OBEXHeaderSetImpl responseHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 7);
        Object connID = responseHeaders.getHeader(203);
        if (connID != null) {
            this.connectionID = ((Long) connID).longValue();
        }
        validateAuthenticationResponse((OBEXHeaderSetImpl) headers, responseHeaders);
        if (!retry && responseHeaders.getResponseCode() == 193 && responseHeaders.hasAuthenticationChallenge()) {
            HeaderSet replyHeaders = OBEXHeaderSetImpl.cloneHeaders(headers);
            handleAuthenticationChallenge(responseHeaders, (OBEXHeaderSetImpl) replyHeaders);
            return connectImpl(replyHeaders, true);
        }
        if (responseHeaders.getResponseCode() == 160) {
            this.isConnected = true;
        }
        return responseHeaders;
    }

    @Override // javax.obex.ClientSession
    public HeaderSet disconnect(HeaderSet headers) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        canStartOperation();
        if (!this.isConnected) {
            throw new IOException("Session not connected");
        }
        writePacket(129, (OBEXHeaderSetImpl) headers);
        byte[] b2 = readPacket();
        this.isConnected = false;
        if (this.operation != null) {
            this.operation.close();
            this.operation = null;
        }
        return OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
    }

    @Override // javax.obex.ClientSession
    public void setConnectionID(long id) {
        if (id < 0 || id > 4294967295L) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid connectionID ").append(id).toString());
        }
        this.connectionID = id;
    }

    @Override // javax.obex.ClientSession
    public long getConnectionID() {
        return this.connectionID;
    }

    protected void canStartOperation() throws IOException {
        if (!this.isConnected) {
            throw new IOException("Session not connected");
        }
        if (this.operation != null) {
            if (!this.operation.isClosed()) {
                throw new IOException("Client is already in an operation");
            }
            this.operation = null;
        }
    }

    @Override // javax.obex.ClientSession
    public HeaderSet setPath(HeaderSet headers, boolean backup, boolean create) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        canStartOperation();
        return setPathImpl(headers, backup, create, false);
    }

    private HeaderSet setPathImpl(HeaderSet headers, boolean backup, boolean create, boolean authentRetry) throws IOException {
        byte[] request = new byte[2];
        request[0] = (byte) ((backup ? 1 : 0) | (create ? 0 : 2));
        request[1] = 0;
        writePacketWithFlags(133, request, (OBEXHeaderSetImpl) headers);
        byte[] b2 = readPacket();
        OBEXHeaderSetImpl responseHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
        validateAuthenticationResponse((OBEXHeaderSetImpl) headers, responseHeaders);
        if (!authentRetry && responseHeaders.getResponseCode() == 193 && responseHeaders.hasAuthenticationChallenge()) {
            OBEXHeaderSetImpl retryHeaders = OBEXHeaderSetImpl.cloneHeaders(headers);
            handleAuthenticationChallenge(responseHeaders, retryHeaders);
            return setPathImpl(retryHeaders, backup, create, true);
        }
        return responseHeaders;
    }

    @Override // javax.obex.ClientSession
    public Operation get(HeaderSet headers) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        canStartOperation();
        this.operation = new OBEXClientOperationGet(this, (OBEXHeaderSetImpl) headers);
        return this.operation;
    }

    @Override // javax.obex.ClientSession
    public Operation put(HeaderSet headers) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        canStartOperation();
        this.operation = new OBEXClientOperationPut(this, (OBEXHeaderSetImpl) headers);
        return this.operation;
    }

    @Override // javax.obex.ClientSession
    public HeaderSet delete(HeaderSet headers) throws IOException {
        OBEXSessionBase.validateCreatedHeaderSet(headers);
        canStartOperation();
        return deleteImp(headers, false);
    }

    HeaderSet deleteImp(HeaderSet headers, boolean authentRetry) throws IOException {
        writePacket(130, (OBEXHeaderSetImpl) headers);
        byte[] b2 = readPacket();
        OBEXHeaderSetImpl responseHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
        validateAuthenticationResponse((OBEXHeaderSetImpl) headers, responseHeaders);
        if (!authentRetry && responseHeaders.getResponseCode() == 193 && responseHeaders.hasAuthenticationChallenge()) {
            OBEXHeaderSetImpl retryHeaders = OBEXHeaderSetImpl.cloneHeaders(headers);
            handleAuthenticationChallenge(responseHeaders, retryHeaders);
            return deleteImp(retryHeaders, true);
        }
        return responseHeaders;
    }

    @Override // javax.obex.ClientSession
    public void setAuthenticator(Authenticator auth) {
        if (auth == null) {
            throw new NullPointerException("auth is null");
        }
        this.authenticator = auth;
    }

    @Override // com.intel.bluetooth.obex.OBEXSessionBase, javax.microedition.io.Connection
    public void close() throws IOException {
        try {
            if (this.operation != null) {
                this.operation.close();
                this.operation = null;
            }
        } finally {
            super.close();
        }
    }
}
