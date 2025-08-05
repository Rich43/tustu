package java.rmi;

/* loaded from: rt.jar:java/rmi/UnexpectedException.class */
public class UnexpectedException extends RemoteException {
    private static final long serialVersionUID = 1800467484195073863L;

    public UnexpectedException(String str) {
        super(str);
    }

    public UnexpectedException(String str, Exception exc) {
        super(str, exc);
    }
}
