package java.rmi;

/* loaded from: rt.jar:java/rmi/UnmarshalException.class */
public class UnmarshalException extends RemoteException {
    private static final long serialVersionUID = 594380845140740218L;

    public UnmarshalException(String str) {
        super(str);
    }

    public UnmarshalException(String str, Exception exc) {
        super(str, exc);
    }
}
