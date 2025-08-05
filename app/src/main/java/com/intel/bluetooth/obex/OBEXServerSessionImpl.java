package com.intel.bluetooth.obex;

import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.BluetoothServerConnection;
import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.UtilsJavaSE;
import java.io.EOFException;
import java.io.IOException;
import javax.microedition.io.StreamConnection;
import javax.obex.Authenticator;
import javax.obex.ServerRequestHandler;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXServerSessionImpl.class */
class OBEXServerSessionImpl extends OBEXSessionBase implements Runnable, BluetoothServerConnection {
    private ServerRequestHandler handler;
    private OBEXServerOperation operation;
    private boolean closeRequested;
    private volatile boolean delayClose;
    private Object canCloseEvent;
    private Object stackID;
    private Thread handlerThread;
    private static int threadNumber;
    static int errorCount = 0;
    static Class class$com$intel$bluetooth$obex$OBEXServerSessionImpl;

    private static synchronized int nextThreadNum() {
        int i2 = threadNumber;
        threadNumber = i2 + 1;
        return i2;
    }

    OBEXServerSessionImpl(StreamConnection connection, ServerRequestHandler handler, Authenticator authenticator, OBEXConnectionParams obexConnectionParams) throws IOException {
        super(connection, obexConnectionParams);
        this.closeRequested = false;
        this.delayClose = false;
        this.canCloseEvent = new Object();
        this.requestSent = true;
        this.handler = handler;
        this.authenticator = authenticator;
        this.stackID = BlueCoveImpl.getCurrentThreadBluetoothStackID();
        this.handlerThread = new Thread(this, new StringBuffer().append("OBEXServerSessionThread-").append(nextThreadNum()).toString());
        UtilsJavaSE.threadSetDaemon(this.handlerThread);
    }

    void startSessionHandlerThread() {
        this.handlerThread.start();
    }

    @Override // java.lang.Runnable
    public void run() {
        Class clsClass$;
        Thread.yield();
        try {
            try {
                if (this.stackID != null) {
                    BlueCoveImpl.setThreadBluetoothStackID(this.stackID);
                }
                while (!isClosed() && !this.closeRequested) {
                    if (!handleRequest()) {
                        DebugLog.debug("OBEXServerSession ends");
                        try {
                            super.close();
                            return;
                        } catch (IOException e2) {
                            DebugLog.debug("OBEXServerSession close error", (Throwable) e2);
                            return;
                        }
                    }
                }
                DebugLog.debug("OBEXServerSession ends");
                try {
                    super.close();
                } catch (IOException e3) {
                    DebugLog.debug("OBEXServerSession close error", (Throwable) e3);
                }
            } catch (Throwable e4) {
                if (class$com$intel$bluetooth$obex$OBEXServerSessionImpl == null) {
                    clsClass$ = class$("com.intel.bluetooth.obex.OBEXServerSessionImpl");
                    class$com$intel$bluetooth$obex$OBEXServerSessionImpl = clsClass$;
                } else {
                    clsClass$ = class$com$intel$bluetooth$obex$OBEXServerSessionImpl;
                }
                synchronized (clsClass$) {
                    errorCount++;
                    if (this.isConnected) {
                        DebugLog.error("OBEXServerSession error", e4);
                    } else {
                        DebugLog.debug("OBEXServerSession error", e4);
                    }
                    DebugLog.debug("OBEXServerSession ends");
                    try {
                        super.close();
                    } catch (IOException e5) {
                        DebugLog.debug("OBEXServerSession close error", (Throwable) e5);
                    }
                }
            }
        } catch (Throwable th) {
            DebugLog.debug("OBEXServerSession ends");
            try {
                super.close();
            } catch (IOException e6) {
                DebugLog.debug("OBEXServerSession close error", (Throwable) e6);
            }
            throw th;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // com.intel.bluetooth.obex.OBEXSessionBase, javax.microedition.io.Connection
    public void close() throws IOException {
        this.closeRequested = true;
        while (this.delayClose) {
            synchronized (this.canCloseEvent) {
                try {
                    if (this.delayClose) {
                        this.canCloseEvent.wait(700L);
                    }
                } catch (InterruptedException e2) {
                }
                this.delayClose = false;
            }
        }
        if (!isClosed()) {
            DebugLog.debug("OBEXServerSession close");
            if (this.operation != null) {
                this.operation.close();
                this.operation = null;
            }
        }
        super.close();
    }

    private boolean handleRequest() throws IOException {
        DebugLog.debug("OBEXServerSession handleRequest");
        this.delayClose = false;
        try {
            byte[] b2 = readPacket();
            this.delayClose = true;
            try {
                int opcode = b2[0] & 255;
                boolean finalPacket = (opcode & 128) != 0;
                if (finalPacket) {
                    DebugLog.debug("OBEXServerSession got operation finalPacket");
                }
                switch (opcode) {
                    case 2:
                    case 130:
                        processPut(b2, finalPacket);
                        break;
                    case 3:
                    case 131:
                        processGet(b2, finalPacket);
                        break;
                    case 5:
                    case 133:
                        processSetPath(b2, finalPacket);
                        break;
                    case 128:
                        processConnect(b2);
                        break;
                    case 129:
                        processDisconnect(b2);
                        break;
                    case 255:
                        processAbort();
                        break;
                    default:
                        writePacket(209, null);
                        break;
                }
                synchronized (this.canCloseEvent) {
                    this.canCloseEvent.notifyAll();
                }
                return true;
            } finally {
                this.delayClose = false;
            }
        } catch (EOFException e2) {
            if (this.isConnected) {
                throw e2;
            }
            DebugLog.debug("OBEXServerSession got EOF");
            close();
            return false;
        }
    }

    private void processConnect(byte[] b2) throws IOException {
        int rc;
        DebugLog.debug("Connect operation");
        if (b2[3] != 16) {
            throw new IOException(new StringBuffer().append("Unsupported client OBEX version ").append((int) b2[3]).toString());
        }
        if (b2.length < 7) {
            throw new IOException("Corrupted OBEX data");
        }
        int requestedMTU = OBEXUtils.bytesToShort(b2[5], b2[6]);
        if (requestedMTU < 255) {
            throw new IOException(new StringBuffer().append("Invalid MTU ").append(requestedMTU).toString());
        }
        this.mtu = requestedMTU;
        DebugLog.debug("mtu selected", this.mtu);
        OBEXHeaderSetImpl replyHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        OBEXHeaderSetImpl requestHeaders = OBEXHeaderSetImpl.readHeaders(b2, 7);
        if (!handleAuthenticationResponse(requestHeaders)) {
            rc = 193;
        } else {
            handleAuthenticationChallenge(requestHeaders, replyHeaders);
            rc = 208;
            try {
                rc = this.handler.onConnect(requestHeaders, replyHeaders);
            } catch (Throwable e2) {
                DebugLog.error("onConnect", e2);
            }
        }
        byte[] connectResponse = {16, 0, OBEXUtils.hiByte(this.obexConnectionParams.mtu), OBEXUtils.loByte(this.obexConnectionParams.mtu)};
        writePacketWithFlags(rc, connectResponse, replyHeaders);
        if (rc == 160) {
            this.isConnected = true;
        }
    }

    boolean handleAuthenticationResponse(OBEXHeaderSetImpl incomingHeaders) throws IOException {
        return handleAuthenticationResponse(incomingHeaders, this.handler);
    }

    private boolean validateConnection() throws IOException {
        if (this.isConnected) {
            return true;
        }
        writePacket(192, null);
        return false;
    }

    private void processDisconnect(byte[] b2) throws IOException {
        DebugLog.debug("Disconnect operation");
        if (!validateConnection()) {
            return;
        }
        OBEXHeaderSetImpl requestHeaders = OBEXHeaderSetImpl.readHeaders(b2, 3);
        OBEXHeaderSetImpl replyHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        int rc = 160;
        try {
            this.handler.onDisconnect(requestHeaders, replyHeaders);
        } catch (Throwable e2) {
            rc = 211;
            DebugLog.error("onDisconnect", e2);
        }
        this.isConnected = false;
        writePacket(rc, replyHeaders);
    }

    private void processDelete(OBEXHeaderSetImpl requestHeaders) throws IOException {
        int rc;
        DebugLog.debug("Delete operation");
        OBEXHeaderSetImpl replyHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        handleAuthenticationChallenge(requestHeaders, replyHeaders);
        try {
            rc = this.handler.onDelete(requestHeaders, replyHeaders);
        } catch (Throwable e2) {
            rc = 211;
            DebugLog.error("onDelete", e2);
        }
        writePacket(rc, replyHeaders);
    }

    private void processPut(byte[] b2, boolean finalPacket) throws IOException {
        int rc;
        DebugLog.debug("Put/Delete operation");
        if (!validateConnection()) {
            return;
        }
        OBEXHeaderSetImpl requestHeaders = OBEXHeaderSetImpl.readHeaders(b2, 3);
        if (!handleAuthenticationResponse(requestHeaders, this.handler)) {
            writePacket(193, null);
            return;
        }
        if (finalPacket && !requestHeaders.hasIncommingData()) {
            processDelete(requestHeaders);
            return;
        }
        DebugLog.debug("Put operation");
        this.operation = new OBEXServerOperationPut(this, requestHeaders, finalPacket);
        try {
            try {
                rc = this.handler.onPut(this.operation);
            } catch (Throwable e2) {
                rc = 211;
                DebugLog.error("onPut", e2);
            }
            if (!this.operation.isAborted) {
                this.operation.writeResponse(rc);
            }
        } finally {
            this.operation.close();
            this.operation = null;
        }
    }

    private void processGet(byte[] b2, boolean finalPacket) throws IOException {
        int rc;
        DebugLog.debug("Get operation");
        if (!validateConnection()) {
            return;
        }
        OBEXHeaderSetImpl requestHeaders = OBEXHeaderSetImpl.readHeaders(b2, 3);
        if (!handleAuthenticationResponse(requestHeaders, this.handler)) {
            writePacket(193, null);
            return;
        }
        this.operation = new OBEXServerOperationGet(this, requestHeaders, finalPacket);
        try {
            try {
                rc = this.handler.onGet(this.operation);
            } catch (Throwable e2) {
                rc = 211;
                DebugLog.error("onGet", e2);
            }
            if (!this.operation.isAborted) {
                this.operation.writeResponse(rc);
            }
        } finally {
            this.operation.close();
            this.operation = null;
        }
    }

    private void processAbort() throws IOException {
        DebugLog.debug("Abort operation");
        if (!validateConnection()) {
            return;
        }
        if (this.operation != null) {
            this.operation.isAborted = true;
            this.operation.close();
            this.operation = null;
            writePacket(160, null);
            return;
        }
        writePacket(192, null);
    }

    private void processSetPath(byte[] b2, boolean finalPacket) throws IOException {
        int rc;
        DebugLog.debug("SetPath operation");
        if (!validateConnection()) {
            return;
        }
        if (b2.length < 5) {
            throw new IOException("Corrupted OBEX data");
        }
        OBEXHeaderSetImpl requestHeaders = OBEXHeaderSetImpl.readHeaders(b2, 5);
        boolean backup = (b2[3] & 1) != 0;
        boolean create = (b2[3] & 2) == 0;
        DebugLog.debug("setPath backup", backup);
        DebugLog.debug("setPath create", create);
        if (!handleAuthenticationResponse(requestHeaders, this.handler)) {
            writePacket(193, null);
            return;
        }
        OBEXHeaderSetImpl replyHeaders = OBEXSessionBase.createOBEXHeaderSetImpl();
        handleAuthenticationChallenge(requestHeaders, replyHeaders);
        try {
            rc = this.handler.onSetPath(requestHeaders, replyHeaders, backup, create);
        } catch (Throwable e2) {
            rc = 211;
            DebugLog.error("onSetPath", e2);
        }
        writePacket(rc, replyHeaders);
    }
}
