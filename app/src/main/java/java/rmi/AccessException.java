package java.rmi;

/* loaded from: rt.jar:java/rmi/AccessException.class */
public class AccessException extends RemoteException {
    private static final long serialVersionUID = 6314925228044966088L;

    public AccessException(String str) {
        super(str);
    }

    public AccessException(String str, Exception exc) {
        super(str, exc);
    }
}
