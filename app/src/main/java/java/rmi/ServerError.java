package java.rmi;

/* loaded from: rt.jar:java/rmi/ServerError.class */
public class ServerError extends RemoteException {
    private static final long serialVersionUID = 8455284893909696482L;

    public ServerError(String str, Error error) {
        super(str, error);
    }
}
