package java.rmi;

/* loaded from: rt.jar:java/rmi/MarshalException.class */
public class MarshalException extends RemoteException {
    private static final long serialVersionUID = 6223554758134037936L;

    public MarshalException(String str) {
        super(str);
    }

    public MarshalException(String str, Exception exc) {
        super(str, exc);
    }
}
