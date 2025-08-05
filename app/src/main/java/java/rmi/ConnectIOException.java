package java.rmi;

/* loaded from: rt.jar:java/rmi/ConnectIOException.class */
public class ConnectIOException extends RemoteException {
    private static final long serialVersionUID = -8087809532704668744L;

    public ConnectIOException(String str) {
        super(str);
    }

    public ConnectIOException(String str, Exception exc) {
        super(str, exc);
    }
}
