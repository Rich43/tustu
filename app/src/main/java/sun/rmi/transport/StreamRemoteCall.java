package sun.rmi.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteCall;
import java.security.AccessController;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.misc.ObjectInputFilter;
import sun.rmi.runtime.Log;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.tcp.TCPEndpoint;

/* loaded from: rt.jar:sun/rmi/transport/StreamRemoteCall.class */
public class StreamRemoteCall implements RemoteCall {
    private Connection conn;
    private ConnectionInputStream in = null;
    private ConnectionOutputStream out = null;
    private ObjectInputFilter filter = null;
    private boolean resultStarted = false;
    private Exception serverException = null;

    public StreamRemoteCall(Connection connection) {
        this.conn = connection;
    }

    public StreamRemoteCall(Connection connection, ObjID objID, int i2, long j2) throws RemoteException {
        try {
            this.conn = connection;
            Transport.transportLog.log(Log.VERBOSE, "write remote call header...");
            this.conn.getOutputStream().write(80);
            getOutputStream();
            objID.write(this.out);
            this.out.writeInt(i2);
            this.out.writeLong(j2);
        } catch (IOException e2) {
            throw new MarshalException("Error marshaling call header", e2);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    @Override // java.rmi.server.RemoteCall
    public ObjectOutput getOutputStream() throws IOException {
        return getOutputStream(false);
    }

    private ObjectOutput getOutputStream(boolean z2) throws IOException {
        if (this.out == null) {
            Transport.transportLog.log(Log.VERBOSE, "getting output stream");
            this.out = new ConnectionOutputStream(this.conn, z2);
        }
        return this.out;
    }

    @Override // java.rmi.server.RemoteCall
    public void releaseOutputStream() throws IOException {
        try {
            if (this.out != null) {
                try {
                    this.out.flush();
                    this.out.done();
                } catch (Throwable th) {
                    this.out.done();
                    throw th;
                }
            }
            this.conn.releaseOutputStream();
        } finally {
            this.out = null;
        }
    }

    public void setObjectInputFilter(ObjectInputFilter objectInputFilter) {
        if (this.in != null) {
            throw new IllegalStateException("set filter must occur before calling getInputStream");
        }
        this.filter = objectInputFilter;
    }

    @Override // java.rmi.server.RemoteCall
    public ObjectInput getInputStream() throws IOException {
        if (this.in == null) {
            Transport.transportLog.log(Log.VERBOSE, "getting input stream");
            this.in = new ConnectionInputStream(this.conn.getInputStream());
            if (this.filter != null) {
                AccessController.doPrivileged(() -> {
                    ObjectInputFilter.Config.setObjectInputFilter(this.in, this.filter);
                    return null;
                });
            }
        }
        return this.in;
    }

    @Override // java.rmi.server.RemoteCall
    public void releaseInputStream() throws IOException {
        try {
            if (this.in != null) {
                try {
                    this.in.done();
                } catch (RuntimeException e2) {
                }
                this.in.registerRefs();
                this.in.done(this.conn);
            }
            this.conn.releaseInputStream();
        } finally {
            this.in = null;
        }
    }

    public void discardPendingRefs() {
        this.in.discardRefs();
    }

    @Override // java.rmi.server.RemoteCall
    public ObjectOutput getResultStream(boolean z2) throws IOException {
        if (this.resultStarted) {
            throw new StreamCorruptedException("result already in progress");
        }
        this.resultStarted = true;
        new DataOutputStream(this.conn.getOutputStream()).writeByte(81);
        getOutputStream(true);
        if (z2) {
            this.out.writeByte(1);
        } else {
            this.out.writeByte(2);
        }
        this.out.writeID();
        return this.out;
    }

    @Override // java.rmi.server.RemoteCall
    public void executeCall() throws Exception {
        DGCAckHandler dGCAckHandler = null;
        try {
            try {
                if (this.out != null) {
                    dGCAckHandler = this.out.getDGCAckHandler();
                }
                releaseOutputStream();
                byte b2 = new DataInputStream(this.conn.getInputStream()).readByte();
                if (b2 != 81) {
                    if (Transport.transportLog.isLoggable(Log.BRIEF)) {
                        Transport.transportLog.log(Log.BRIEF, "transport return code invalid: " + ((int) b2));
                    }
                    throw new UnmarshalException("Transport return code invalid");
                }
                getInputStream();
                byte b3 = this.in.readByte();
                this.in.readID();
                if (dGCAckHandler != null) {
                    dGCAckHandler.release();
                }
                switch (b3) {
                    case 1:
                        return;
                    case 2:
                        try {
                            Object object = this.in.readObject();
                            if (object instanceof Exception) {
                                exceptionReceivedFromServer((Exception) object);
                                break;
                            } else {
                                discardPendingRefs();
                                throw new UnmarshalException("Return type not Exception");
                            }
                        } catch (Exception e2) {
                            discardPendingRefs();
                            throw new UnmarshalException("Error unmarshaling return", e2);
                        }
                }
                if (Transport.transportLog.isLoggable(Log.BRIEF)) {
                    Transport.transportLog.log(Log.BRIEF, "return code invalid: " + ((int) b3));
                }
                throw new UnmarshalException("Return code invalid");
            } catch (UnmarshalException e3) {
                throw e3;
            } catch (IOException e4) {
                throw new UnmarshalException("Error unmarshaling return header", e4);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                dGCAckHandler.release();
            }
            throw th;
        }
    }

    protected void exceptionReceivedFromServer(Exception exc) throws Exception {
        this.serverException = exc;
        StackTraceElement[] stackTrace = exc.getStackTrace();
        StackTraceElement[] stackTrace2 = new Throwable().getStackTrace();
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[stackTrace.length + stackTrace2.length];
        System.arraycopy(stackTrace, 0, stackTraceElementArr, 0, stackTrace.length);
        System.arraycopy(stackTrace2, 0, stackTraceElementArr, stackTrace.length, stackTrace2.length);
        exc.setStackTrace(stackTraceElementArr);
        if (UnicastRef.clientCallLog.isLoggable(Log.BRIEF)) {
            TCPEndpoint tCPEndpoint = (TCPEndpoint) this.conn.getChannel().getEndpoint();
            UnicastRef.clientCallLog.log(Log.BRIEF, "outbound call received exception: [" + tCPEndpoint.getHost() + CallSiteDescriptor.TOKEN_DELIMITER + tCPEndpoint.getPort() + "] exception: ", exc);
        }
        throw exc;
    }

    public Exception getServerException() {
        return this.serverException;
    }

    @Override // java.rmi.server.RemoteCall
    public void done() throws IOException {
        releaseInputStream();
    }
}
