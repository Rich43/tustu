package java.rmi.server;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/SocketSecurityException.class */
public class SocketSecurityException extends ExportException {
    private static final long serialVersionUID = -7622072999407781979L;

    public SocketSecurityException(String str) {
        super(str);
    }

    public SocketSecurityException(String str, Exception exc) {
        super(str, exc);
    }
}
