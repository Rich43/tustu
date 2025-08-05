package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.obex.HeaderSet;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXServerOperationGet.class */
class OBEXServerOperationGet extends OBEXServerOperation implements OBEXOperationDelivery, OBEXOperationReceive {
    protected OBEXServerOperationGet(OBEXServerSessionImpl session, OBEXHeaderSetImpl receivedHeaders, boolean finalPacket) throws IOException {
        super(session, receivedHeaders);
        if (finalPacket) {
            this.requestEnded = true;
            this.finalPacketReceived = true;
        }
        this.inputStream = new OBEXOperationInputStream(this);
        processIncommingData(receivedHeaders, finalPacket);
    }

    @Override // com.intel.bluetooth.obex.OBEXServerOperation, javax.microedition.io.InputConnection
    public InputStream openInputStream() throws IOException {
        if (this.isClosed) {
            throw new IOException("operation closed");
        }
        if (this.inputStreamOpened) {
            throw new IOException("input stream already open");
        }
        this.inputStreamOpened = true;
        return this.inputStream;
    }

    @Override // com.intel.bluetooth.obex.OBEXServerOperation, javax.microedition.io.OutputConnection
    public OutputStream openOutputStream() throws IOException {
        if (this.isClosed) {
            throw new IOException("operation closed");
        }
        if (this.outputStream != null) {
            throw new IOException("output stream already open");
        }
        this.requestEnded = true;
        this.outputStream = new OBEXOperationOutputStream(this.session.mtu, this);
        this.session.writePacket(144, this.sendHeaders);
        this.sendHeaders = null;
        return this.outputStream;
    }

    @Override // com.intel.bluetooth.obex.OBEXServerOperation, javax.microedition.io.Connection
    public void close() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
            this.outputStream = null;
        }
        this.inputStream.close();
        super.close();
    }

    @Override // com.intel.bluetooth.obex.OBEXServerOperation
    protected boolean readRequestPacket() throws IOException {
        byte[] b2 = this.session.readPacket();
        int opcode = b2[0] & 255;
        boolean finalPacket = (opcode & 128) != 0;
        if (finalPacket) {
            DebugLog.debug("server operation got final packet");
            this.finalPacketReceived = true;
        }
        switch (opcode) {
            case 3:
            case 131:
                if (finalPacket) {
                    this.requestEnded = true;
                }
                HeaderSet requestHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
                OBEXHeaderSetImpl.appendHeaders(this.receivedHeaders, requestHeaders);
                processIncommingData(requestHeaders, finalPacket);
                break;
            case 255:
                processAbort();
                break;
            default:
                this.errorReceived = true;
                DebugLog.debug0x("server operation invalid request", OBEXUtils.toStringObexResponseCodes(opcode), opcode);
                this.session.writePacket(192, null);
                break;
        }
        return finalPacket;
    }

    @Override // com.intel.bluetooth.obex.OBEXOperationReceive
    public void receiveData(OBEXOperationInputStream is) throws IOException {
        if (this.requestEnded || this.errorReceived) {
            this.inputStream.appendData(null, true);
            return;
        }
        DebugLog.debug("server operation reply continue");
        this.session.writePacket(144, this.sendHeaders);
        this.sendHeaders = null;
        readRequestPacket();
    }

    @Override // com.intel.bluetooth.obex.OBEXOperationDelivery
    public void deliverPacket(boolean finalPacket, byte[] buffer) throws IOException, IllegalArgumentException {
        if (this.session.requestSent) {
            readRequestPacket();
            if (this.session.requestSent) {
                throw new IOException("Client not requesting data");
            }
        }
        OBEXHeaderSetImpl dataHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        int dataHeaderID = 72;
        if (finalPacket) {
            dataHeaderID = 73;
        }
        dataHeaders.setHeader(dataHeaderID, buffer);
        if (this.sendHeaders != null) {
            OBEXHeaderSetImpl.appendHeaders(dataHeaders, this.sendHeaders);
            this.sendHeaders = null;
        }
        this.session.writePacket(144, dataHeaders);
        readRequestPacket();
    }

    private void processAbort() throws IOException {
        this.finalPacketReceived = true;
        this.requestEnded = true;
        this.isAborted = true;
        this.session.writePacket(160, null);
        throw new IOException("Operation aborted");
    }
}
