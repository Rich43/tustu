package javax.activity;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/activity/ActivityCompletedException.class */
public class ActivityCompletedException extends RemoteException {
    public ActivityCompletedException() {
    }

    public ActivityCompletedException(String str) {
        super(str);
    }

    public ActivityCompletedException(Throwable th) {
        this("", th);
    }

    public ActivityCompletedException(String str, Throwable th) {
        super(str, th);
    }
}
