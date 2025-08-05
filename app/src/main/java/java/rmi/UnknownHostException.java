package java.rmi;

/* loaded from: rt.jar:java/rmi/UnknownHostException.class */
public class UnknownHostException extends RemoteException {
    private static final long serialVersionUID = -8152710247442114228L;

    public UnknownHostException(String str) {
        super(str);
    }

    public UnknownHostException(String str, Exception exc) {
        super(str, exc);
    }
}
