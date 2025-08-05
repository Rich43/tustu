package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.obex.HeaderSet;
import javax.obex.Operation;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXServerOperation.class */
abstract class OBEXServerOperation implements Operation, OBEXOperation {
    protected OBEXServerSessionImpl session;
    protected HeaderSet receivedHeaders;
    protected OBEXHeaderSetImpl sendHeaders;
    protected OBEXOperationOutputStream outputStream;
    protected OBEXOperationInputStream inputStream;
    protected boolean isClosed = false;
    protected boolean isAborted = false;
    protected boolean finalPacketReceived = false;
    protected boolean requestEnded = false;
    protected boolean errorReceived = false;
    protected boolean incommingDataReceived = false;
    protected boolean outputStreamOpened = false;
    protected boolean inputStreamOpened = false;

    protected abstract boolean readRequestPacket() throws IOException;

    @Override // javax.microedition.io.InputConnection
    public abstract InputStream openInputStream() throws IOException;

    @Override // javax.microedition.io.OutputConnection
    public abstract OutputStream openOutputStream() throws IOException;

    protected OBEXServerOperation(OBEXServerSessionImpl session, OBEXHeaderSetImpl receivedHeaders) throws IOException {
        this.session = session;
        this.receivedHeaders = receivedHeaders;
        if (receivedHeaders.hasAuthenticationChallenge()) {
            this.sendHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
            this.session.handleAuthenticationChallenge(receivedHeaders, this.sendHeaders);
        }
    }

    boolean exchangeRequestPhasePackets() throws IOException {
        this.session.writePacket(144, null);
        return readRequestPacket();
    }

    void writeResponse(int responseCode) throws IOException {
        DebugLog.debug0x("server operation reply final", responseCode);
        this.session.writePacket(responseCode, this.sendHeaders);
        this.sendHeaders = null;
        if (responseCode == 160) {
            while (!this.finalPacketReceived && !this.session.isClosed()) {
                DebugLog.debug("server waits to receive final packet");
                readRequestPacket();
                if (!this.errorReceived) {
                    this.session.writePacket(responseCode, null);
                }
            }
            return;
        }
        DebugLog.debug("sent final reply");
    }

    protected void processIncommingData(HeaderSet dataHeaders, boolean eof) throws IOException {
        if (this.inputStream == null) {
            return;
        }
        byte[] data = (byte[]) dataHeaders.getHeader(72);
        if (data == null) {
            data = (byte[]) dataHeaders.getHeader(73);
            if (data != null) {
                eof = true;
            }
        }
        if (data != null) {
            this.incommingDataReceived = true;
            DebugLog.debug(new StringBuffer().append("server received Data eof: ").append(eof).append(" len:").toString(), data.length);
            this.inputStream.appendData(data, eof);
        } else if (eof) {
            this.inputStream.appendData(null, eof);
        }
    }

    @Override // javax.obex.Operation
    public void abort() throws IOException {
        throw new IOException("Can't abort server operation");
    }

    @Override // javax.obex.Operation
    public HeaderSet getReceivedHeaders() throws IOException {
        return OBEXHeaderSetImpl.cloneHeaders(this.receivedHeaders);
    }

    @Override // javax.obex.Operation
    public int getResponseCode() throws IOException {
        throw new IOException("Operation object was created by an OBEX server");
    }

    @Override // javax.obex.Operation
    public void sendHeaders(HeaderSet headers) throws IOException {
        if (headers == null) {
            throw new NullPointerException("headers are null");
        }
        OBEXHeaderSetImpl.validateCreatedHeaderSet(headers);
        if (this.isClosed) {
            throw new IOException("operation closed");
        }
        if (this.sendHeaders != null) {
            OBEXHeaderSetImpl.appendHeaders(this.sendHeaders, headers);
        } else {
            this.sendHeaders = (OBEXHeaderSetImpl) headers;
        }
    }

    @Override // javax.microedition.io.ContentConnection
    public String getEncoding() {
        return null;
    }

    @Override // javax.microedition.io.ContentConnection
    public long getLength() {
        try {
            Long len = (Long) this.receivedHeaders.getHeader(195);
            if (len == null) {
                return -1L;
            }
            return len.longValue();
        } catch (IOException e2) {
            return -1L;
        }
    }

    @Override // javax.microedition.io.ContentConnection
    public String getType() {
        try {
            return (String) this.receivedHeaders.getHeader(66);
        } catch (IOException e2) {
            return null;
        }
    }

    @Override // javax.microedition.io.InputConnection
    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    @Override // javax.microedition.io.OutputConnection
    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.isClosed = true;
    }

    @Override // com.intel.bluetooth.obex.OBEXOperation
    public boolean isClosed() {
        return this.isClosed;
    }

    public boolean isIncommingDataReceived() {
        return this.incommingDataReceived;
    }

    public boolean isErrorReceived() {
        return this.errorReceived;
    }
}
