package java.rmi;

/* loaded from: rt.jar:java/rmi/ConnectException.class */
public class ConnectException extends RemoteException {
    private static final long serialVersionUID = 4863550261346652506L;

    public ConnectException(String str) {
        super(str);
    }

    public ConnectException(String str, Exception exc) {
        super(str, exc);
    }
}
