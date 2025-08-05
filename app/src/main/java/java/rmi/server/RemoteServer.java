package java.rmi.server;

import java.io.OutputStream;
import java.io.PrintStream;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.transport.tcp.TCPTransport;

/* loaded from: rt.jar:java/rmi/server/RemoteServer.class */
public abstract class RemoteServer extends RemoteObject {
    private static final long serialVersionUID = -4100238210092549637L;
    private static boolean logNull;

    protected RemoteServer() {
    }

    protected RemoteServer(RemoteRef remoteRef) {
        super(remoteRef);
    }

    public static String getClientHost() throws ServerNotActiveException {
        return TCPTransport.getClientHost();
    }

    public static void setLog(OutputStream outputStream) {
        logNull = outputStream == null;
        UnicastServerRef.callLog.setOutputStream(outputStream);
    }

    public static PrintStream getLog() {
        if (logNull) {
            return null;
        }
        return UnicastServerRef.callLog.getPrintStream();
    }

    static {
        logNull = !UnicastServerRef.logCalls;
    }
}
