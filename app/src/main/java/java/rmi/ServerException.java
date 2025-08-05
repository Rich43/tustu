package java.rmi;

/* loaded from: rt.jar:java/rmi/ServerException.class */
public class ServerException extends RemoteException {
    private static final long serialVersionUID = -4775845313121906682L;

    public ServerException(String str) {
        super(str);
    }

    public ServerException(String str, Exception exc) {
        super(str, exc);
    }
}
