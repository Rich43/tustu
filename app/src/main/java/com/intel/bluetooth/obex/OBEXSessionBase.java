package com.intel.bluetooth.obex;

import com.intel.bluetooth.BluetoothConnectionAccess;
import com.intel.bluetooth.BluetoothStack;
import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.obex.OBEXAuthentication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.StreamConnection;
import javax.obex.Authenticator;
import javax.obex.HeaderSet;
import javax.obex.ServerRequestHandler;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXSessionBase.class */
public abstract class OBEXSessionBase implements Connection, BluetoothConnectionAccess {
    protected boolean isConnected;
    private StreamConnection conn;
    private InputStream is;
    private OutputStream os;
    protected long connectionID;
    protected int mtu;
    protected Authenticator authenticator;
    protected final OBEXConnectionParams obexConnectionParams;
    protected int packetsCountWrite;
    protected int packetsCountRead;
    private Vector authChallengesSent;
    protected boolean requestSent;

    public OBEXSessionBase(StreamConnection conn, OBEXConnectionParams obexConnectionParams) throws IOException {
        this.mtu = 1024;
        if (obexConnectionParams == null) {
            throw new NullPointerException("obexConnectionParams is null");
        }
        this.isConnected = false;
        this.conn = conn;
        this.obexConnectionParams = obexConnectionParams;
        this.mtu = obexConnectionParams.mtu;
        this.connectionID = -1L;
        this.packetsCountWrite = 0;
        this.packetsCountRead = 0;
        boolean initOK = false;
        try {
            this.os = conn.openOutputStream();
            this.is = conn.openInputStream();
            initOK = true;
            if (1 == 0) {
                try {
                    close();
                } catch (IOException e2) {
                    DebugLog.error("close error", e2);
                }
            }
        } catch (Throwable th) {
            if (!initOK) {
                try {
                    close();
                } catch (IOException e3) {
                    DebugLog.error("close error", e3);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        StreamConnection c2 = this.conn;
        this.conn = null;
        try {
            try {
                if (this.is != null) {
                    this.is.close();
                    this.is = null;
                }
                if (this.os != null) {
                    this.os.close();
                    this.os = null;
                }
            } catch (Throwable th) {
                if (this.os != null) {
                    this.os.close();
                    this.os = null;
                }
                throw th;
            }
        } finally {
            if (c2 != null) {
                c2.close();
            }
        }
    }

    static OBEXHeaderSetImpl createOBEXHeaderSetImpl() {
        return new OBEXHeaderSetImpl();
    }

    public static HeaderSet createOBEXHeaderSet() {
        return createOBEXHeaderSetImpl();
    }

    static void validateCreatedHeaderSet(HeaderSet headers) {
        OBEXHeaderSetImpl.validateCreatedHeaderSet(headers);
    }

    protected void writePacket(int commId, OBEXHeaderSetImpl headers) throws IOException {
        writePacketWithFlags(commId, null, headers);
    }

    protected synchronized void writePacketWithFlags(int commId, byte[] headerFlagsData, OBEXHeaderSetImpl headers) throws IOException {
        if (this.requestSent) {
            throw new IOException("Write packet out of order");
        }
        this.requestSent = true;
        int len = 3;
        if (this.connectionID != -1) {
            len = 3 + 5;
        }
        if (headerFlagsData != null) {
            len += headerFlagsData.length;
        }
        byte[] data = null;
        if (headers != null) {
            data = OBEXHeaderSetImpl.toByteArray(headers);
            len += data.length;
        }
        if (len > this.mtu) {
            throw new IOException(new StringBuffer().append("Can't sent more data than in MTU, len=").append(len).append(", mtu=").append(this.mtu).toString());
        }
        this.packetsCountWrite++;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        OBEXHeaderSetImpl.writeObexLen(buf, commId, len);
        if (headerFlagsData != null) {
            buf.write(headerFlagsData);
        }
        if (this.connectionID != -1) {
            OBEXHeaderSetImpl.writeObexInt(buf, 203, this.connectionID);
        }
        if (data != null) {
            buf.write(data);
        }
        DebugLog.debug0x(new StringBuffer().append("obex send (").append(this.packetsCountWrite).append(")").toString(), OBEXUtils.toStringObexResponseCodes(commId), commId);
        this.os.write(buf.toByteArray());
        this.os.flush();
        DebugLog.debug(new StringBuffer().append("obex sent (").append(this.packetsCountWrite).append(") len").toString(), len);
        if (headers != null && headers.hasAuthenticationChallenge()) {
            if (this.authChallengesSent == null) {
                this.authChallengesSent = new Vector();
            }
            Enumeration iter = headers.getAuthenticationChallenges();
            while (iter.hasMoreElements()) {
                byte[] authChallenge = (byte[]) iter.nextElement2();
                OBEXAuthentication.Challenge challenge = new OBEXAuthentication.Challenge(authChallenge);
                this.authChallengesSent.addElement(challenge);
            }
        }
    }

    protected synchronized byte[] readPacket() throws IOException {
        if (!this.requestSent) {
            throw new IOException("Read packet out of order");
        }
        this.requestSent = false;
        byte[] header = new byte[3];
        OBEXUtils.readFully(this.is, this.obexConnectionParams, header);
        this.packetsCountRead++;
        DebugLog.debug0x(new StringBuffer().append("obex received (").append(this.packetsCountRead).append(")").toString(), OBEXUtils.toStringObexResponseCodes(header[0]), header[0] & 255);
        int lenght = OBEXUtils.bytesToShort(header[1], header[2]);
        if (lenght == 3) {
            return header;
        }
        if (lenght < 3 || lenght > 65535) {
            throw new IOException(new StringBuffer().append("Invalid packet length ").append(lenght).toString());
        }
        byte[] data = new byte[lenght];
        System.arraycopy(header, 0, data, 0, header.length);
        OBEXUtils.readFully(this.is, this.obexConnectionParams, data, header.length, lenght - header.length);
        if (this.is.available() > 0) {
            DebugLog.debug("has more data after read", this.is.available());
        }
        return data;
    }

    private void validateBluetoothConnection() {
        if (this.conn != null && !(this.conn instanceof BluetoothConnectionAccess)) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a Bluetooth connection ").append(this.conn.getClass().getName()).toString());
        }
    }

    void validateAuthenticationResponse(OBEXHeaderSetImpl requestHeaders, OBEXHeaderSetImpl incomingHeaders) throws IOException {
        if (requestHeaders != null && requestHeaders.hasAuthenticationChallenge() && !incomingHeaders.hasAuthenticationResponses()) {
            throw new IOException("Authentication response is missing");
        }
        handleAuthenticationResponse(incomingHeaders, null);
    }

    boolean handleAuthenticationResponse(OBEXHeaderSetImpl incomingHeaders, ServerRequestHandler serverHandler) throws IOException {
        if (incomingHeaders.hasAuthenticationResponses()) {
            if (this.authenticator == null) {
                throw new IOException("Authenticator required for authentication");
            }
            if (this.authChallengesSent == null && this.authChallengesSent.size() == 0) {
                throw new IOException("Authentication challenges had not been sent");
            }
            boolean authenticated = false;
            try {
                authenticated = OBEXAuthentication.handleAuthenticationResponse(incomingHeaders, this.authenticator, serverHandler, this.authChallengesSent);
                if (authenticated && this.authChallengesSent != null) {
                    this.authChallengesSent.removeAllElements();
                }
                return authenticated;
            } catch (Throwable th) {
                if (authenticated && this.authChallengesSent != null) {
                    this.authChallengesSent.removeAllElements();
                }
                throw th;
            }
        }
        if (this.authChallengesSent != null && this.authChallengesSent.size() > 0) {
            throw new IOException("Authentication response is missing");
        }
        return true;
    }

    void handleAuthenticationChallenge(OBEXHeaderSetImpl incomingHeaders, OBEXHeaderSetImpl replyHeaders) throws IOException {
        if (incomingHeaders.hasAuthenticationChallenge()) {
            if (this.authenticator == null) {
                throw new IOException("Authenticator required for authentication");
            }
            OBEXAuthentication.handleAuthenticationChallenge(incomingHeaders, replyHeaders, this.authenticator);
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public long getRemoteAddress() throws IOException {
        validateBluetoothConnection();
        if (this.conn == null) {
            throw new IOException("Connection closed");
        }
        return ((BluetoothConnectionAccess) this.conn).getRemoteAddress();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public RemoteDevice getRemoteDevice() {
        validateBluetoothConnection();
        if (this.conn == null) {
            return null;
        }
        return ((BluetoothConnectionAccess) this.conn).getRemoteDevice();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean isClosed() {
        if (this.conn == null) {
            return true;
        }
        if (this.conn instanceof BluetoothConnectionAccess) {
            return ((BluetoothConnectionAccess) this.conn).isClosed();
        }
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void shutdown() throws IOException {
        if (this.conn instanceof BluetoothConnectionAccess) {
            ((BluetoothConnectionAccess) this.conn).shutdown();
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void markAuthenticated() {
        validateBluetoothConnection();
        if (this.conn != null) {
            ((BluetoothConnectionAccess) this.conn).markAuthenticated();
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public int getSecurityOpt() {
        validateBluetoothConnection();
        if (this.conn == null) {
            return 0;
        }
        return ((BluetoothConnectionAccess) this.conn).getSecurityOpt();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean encrypt(long address, boolean on) throws IOException {
        validateBluetoothConnection();
        if (this.conn == null) {
            throw new IOException("Connection closed");
        }
        return ((BluetoothConnectionAccess) this.conn).encrypt(address, on);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void setRemoteDevice(RemoteDevice remoteDevice) {
        validateBluetoothConnection();
        if (this.conn != null) {
            ((BluetoothConnectionAccess) this.conn).setRemoteDevice(remoteDevice);
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public BluetoothStack getBluetoothStack() {
        validateBluetoothConnection();
        if (this.conn == null) {
            return null;
        }
        return ((BluetoothConnectionAccess) this.conn).getBluetoothStack();
    }

    int getPacketsCountWrite() {
        return this.packetsCountWrite;
    }

    int getPacketsCountRead() {
        return this.packetsCountRead;
    }

    int getPacketSize() {
        if (this.isConnected) {
            return this.mtu;
        }
        return this.obexConnectionParams.mtu;
    }

    void setPacketSize(int mtu) throws IOException {
        if (this.isConnected) {
            throw new IOException("Session already connected");
        }
        this.obexConnectionParams.mtu = mtu;
    }
}
