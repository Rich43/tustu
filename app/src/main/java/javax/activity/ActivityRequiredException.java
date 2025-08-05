package javax.activity;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/activity/ActivityRequiredException.class */
public class ActivityRequiredException extends RemoteException {
    public ActivityRequiredException() {
    }

    public ActivityRequiredException(String str) {
        super(str);
    }

    public ActivityRequiredException(Throwable th) {
        this("", th);
    }

    public ActivityRequiredException(String str, Throwable th) {
        super(str, th);
    }
}
