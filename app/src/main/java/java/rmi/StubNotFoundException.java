package java.rmi;

/* loaded from: rt.jar:java/rmi/StubNotFoundException.class */
public class StubNotFoundException extends RemoteException {
    private static final long serialVersionUID = -7088199405468872373L;

    public StubNotFoundException(String str) {
        super(str);
    }

    public StubNotFoundException(String str, Exception exc) {
        super(str, exc);
    }
}
