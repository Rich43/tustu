package javax.activity;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/activity/InvalidActivityException.class */
public class InvalidActivityException extends RemoteException {
    public InvalidActivityException() {
    }

    public InvalidActivityException(String str) {
        super(str);
    }

    public InvalidActivityException(Throwable th) {
        this("", th);
    }

    public InvalidActivityException(String str, Throwable th) {
        super(str, th);
    }
}
