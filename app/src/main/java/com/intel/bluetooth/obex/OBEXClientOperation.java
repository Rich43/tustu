package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.obex.HeaderSet;
import javax.obex.Operation;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXClientOperation.class */
abstract class OBEXClientOperation implements Operation, OBEXOperation, OBEXOperationReceive, OBEXOperationDelivery {
    static final boolean SHORT_REQUEST_PHASE = true;
    protected OBEXClientSessionImpl session;
    protected char operationId;
    protected HeaderSet replyHeaders;
    protected boolean operationInContinue;
    protected OBEXOperationOutputStream outputStream;
    protected boolean outputStreamOpened = false;
    protected boolean inputStreamOpened = false;
    protected boolean errorReceived = false;
    protected boolean requestEnded = false;
    protected boolean finalBodyReceived = false;
    protected OBEXHeaderSetImpl startOperationHeaders = null;
    private boolean authenticationResponseCreated = false;
    protected boolean isClosed = false;
    protected boolean operationInProgress = false;
    protected Object lock = new Object();
    protected OBEXOperationInputStream inputStream = new OBEXOperationInputStream(this);

    @Override // javax.microedition.io.InputConnection
    public abstract InputStream openInputStream() throws IOException;

    @Override // javax.microedition.io.OutputConnection
    public abstract OutputStream openOutputStream() throws IOException;

    OBEXClientOperation(OBEXClientSessionImpl session, char operationId, OBEXHeaderSetImpl sendHeaders) throws IOException {
        this.session = session;
        this.operationId = operationId;
        startOperation(sendHeaders);
    }

    static boolean isShortRequestPhase() {
        return true;
    }

    protected void startOperation(OBEXHeaderSetImpl sendHeaders) throws IOException {
        this.startOperationHeaders = sendHeaders;
    }

    @Override // com.intel.bluetooth.obex.OBEXOperationReceive
    public void receiveData(OBEXOperationInputStream is) throws IOException {
        exchangePacket(this.startOperationHeaders);
        this.startOperationHeaders = null;
    }

    @Override // com.intel.bluetooth.obex.OBEXOperationDelivery
    public void deliverPacket(boolean finalPacket, byte[] buffer) throws IOException, IllegalArgumentException {
        if (this.requestEnded) {
            return;
        }
        if (this.startOperationHeaders != null) {
            exchangePacket(this.startOperationHeaders);
            this.startOperationHeaders = null;
        }
        int dataHeaderID = 72;
        if (finalPacket) {
            this.operationId = (char) (this.operationId | 128);
            dataHeaderID = 73;
            DebugLog.debug("client Request Phase ended");
            this.requestEnded = true;
        }
        OBEXHeaderSetImpl dataHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        dataHeaders.setHeader(dataHeaderID, buffer);
        exchangePacket(dataHeaders);
    }

    protected void endRequestPhase() throws IOException {
        if (this.requestEnded) {
            return;
        }
        DebugLog.debug("client ends Request Phase");
        this.operationInProgress = false;
        this.requestEnded = true;
        this.operationId = (char) (this.operationId | 128);
        exchangePacket(this.startOperationHeaders);
        this.startOperationHeaders = null;
    }

    private void exchangePacket(OBEXHeaderSetImpl headers) throws IOException {
        boolean success = false;
        try {
            this.session.writePacket(this.operationId, headers);
            byte[] b2 = this.session.readPacket();
            OBEXHeaderSetImpl dataHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
            this.session.handleAuthenticationResponse(dataHeaders, null);
            int responseCode = dataHeaders.getResponseCode();
            DebugLog.debug0x("client operation got reply", OBEXUtils.toStringObexResponseCodes(responseCode), responseCode);
            switch (responseCode) {
                case 144:
                    processIncommingHeaders(dataHeaders);
                    processIncommingData(dataHeaders, false);
                    this.operationInContinue = true;
                    break;
                case 160:
                    processIncommingHeaders(dataHeaders);
                    processIncommingData(dataHeaders, true);
                    this.operationInProgress = false;
                    this.operationInContinue = false;
                    break;
                case 193:
                    if (!this.authenticationResponseCreated && dataHeaders.hasAuthenticationChallenge()) {
                        DebugLog.debug("client resend request with auth response");
                        OBEXHeaderSetImpl retryHeaders = OBEXHeaderSetImpl.cloneHeaders(headers);
                        this.session.handleAuthenticationChallenge(dataHeaders, retryHeaders);
                        this.authenticationResponseCreated = true;
                        exchangePacket(retryHeaders);
                        break;
                    } else {
                        this.errorReceived = true;
                        this.operationInContinue = false;
                        processIncommingHeaders(dataHeaders);
                        throw new IOException("Authentication Failure");
                    }
                    break;
                default:
                    this.errorReceived = true;
                    this.operationInContinue = false;
                    processIncommingHeaders(dataHeaders);
                    processIncommingData(dataHeaders, true);
                    break;
            }
            success = true;
        } finally {
            if (!success) {
                this.errorReceived = true;
            }
        }
    }

    protected void processIncommingHeaders(HeaderSet dataHeaders) throws IOException {
        if (this.replyHeaders != null) {
            OBEXHeaderSetImpl.appendHeaders(dataHeaders, this.replyHeaders);
        }
        this.replyHeaders = dataHeaders;
    }

    protected void processIncommingData(HeaderSet dataHeaders, boolean eof) throws IOException {
        byte[] data = (byte[]) dataHeaders.getHeader(72);
        if (data == null) {
            data = (byte[]) dataHeaders.getHeader(73);
            if (data != null) {
                this.finalBodyReceived = true;
                eof = true;
            }
        }
        if (data != null) {
            DebugLog.debug(new StringBuffer().append("client received Data eof: ").append(eof).append(" len: ").toString(), data.length);
            this.inputStream.appendData(data, eof);
        } else if (eof) {
            this.inputStream.appendData(null, eof);
        }
    }

    @Override // javax.obex.Operation
    public void abort() throws IOException {
        validateOperationIsOpen();
        if (!this.operationInProgress && !this.operationInContinue) {
            throw new IOException("the transaction has already ended");
        }
        synchronized (this.lock) {
            if (this.outputStream != null) {
                this.outputStream.abort();
            }
            this.inputStream.close();
        }
        writeAbort();
    }

    private void writeAbort() throws IOException {
        try {
            this.session.writePacket(255, null);
            this.requestEnded = true;
            byte[] b2 = this.session.readPacket();
            HeaderSet dataHeaders = OBEXHeaderSetImpl.readHeaders(b2[0], b2, 3);
            if (dataHeaders.getResponseCode() != 160) {
                throw new IOException(new StringBuffer().append("Fails to abort operation, received ").append(OBEXUtils.toStringObexResponseCodes(dataHeaders.getResponseCode())).toString());
            }
        } finally {
            this.isClosed = true;
            closeStream();
        }
    }

    private void closeStream() throws IOException {
        try {
            receiveOperationEnd();
            this.operationInProgress = false;
            this.inputStream.close();
            closeOutputStream();
        } catch (Throwable th) {
            this.operationInProgress = false;
            this.inputStream.close();
            closeOutputStream();
            throw th;
        }
    }

    private void receiveOperationEnd() throws IOException {
        while (!isClosed() && this.operationInContinue) {
            DebugLog.debug("operation expects operation end");
            receiveData(this.inputStream);
        }
    }

    private void closeOutputStream() throws IOException {
        if (this.outputStream != null) {
            synchronized (this.lock) {
                if (this.outputStream != null) {
                    this.outputStream.close();
                }
                this.outputStream = null;
            }
        }
    }

    protected void validateOperationIsOpen() throws IOException {
        if (this.isClosed) {
            throw new IOException("operation closed");
        }
    }

    @Override // javax.obex.Operation
    public HeaderSet getReceivedHeaders() throws IOException {
        validateOperationIsOpen();
        endRequestPhase();
        return OBEXHeaderSetImpl.cloneHeaders(this.replyHeaders);
    }

    @Override // javax.obex.Operation
    public int getResponseCode() throws IOException {
        validateOperationIsOpen();
        endRequestPhase();
        closeOutputStream();
        receiveOperationEnd();
        return this.replyHeaders.getResponseCode();
    }

    @Override // javax.obex.Operation
    public void sendHeaders(HeaderSet headers) throws IOException {
        if (headers == null) {
            throw new NullPointerException("headers are null");
        }
        OBEXHeaderSetImpl.validateCreatedHeaderSet(headers);
        validateOperationIsOpen();
        if (this.requestEnded) {
            throw new IOException("the request phase has already ended");
        }
        if (this.startOperationHeaders != null) {
            exchangePacket(this.startOperationHeaders);
            this.startOperationHeaders = null;
        }
        exchangePacket((OBEXHeaderSetImpl) headers);
    }

    @Override // javax.microedition.io.ContentConnection
    public String getEncoding() {
        return null;
    }

    @Override // javax.microedition.io.ContentConnection
    public long getLength() {
        try {
            Long len = (Long) this.replyHeaders.getHeader(195);
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
            return (String) this.replyHeaders.getHeader(66);
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
        try {
            endRequestPhase();
            closeStream();
            if (!this.isClosed) {
                this.isClosed = true;
                DebugLog.debug("client operation closed");
            }
        } catch (Throwable th) {
            closeStream();
            if (!this.isClosed) {
                this.isClosed = true;
                DebugLog.debug("client operation closed");
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.obex.OBEXOperation
    public boolean isClosed() {
        return this.isClosed || this.errorReceived;
    }
}
