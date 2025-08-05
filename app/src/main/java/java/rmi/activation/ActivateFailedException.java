package java.rmi.activation;

import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/activation/ActivateFailedException.class */
public class ActivateFailedException extends RemoteException {
    private static final long serialVersionUID = 4863550261346652506L;

    public ActivateFailedException(String str) {
        super(str);
    }

    public ActivateFailedException(String str, Exception exc) {
        super(str, exc);
    }
}
