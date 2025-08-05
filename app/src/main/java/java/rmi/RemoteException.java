package java.rmi;

import java.io.IOException;

/* loaded from: rt.jar:java/rmi/RemoteException.class */
public class RemoteException extends IOException {
    private static final long serialVersionUID = -5148567311918794206L;
    public Throwable detail;

    public RemoteException() {
        initCause(null);
    }

    public RemoteException(String str) {
        super(str);
        initCause(null);
    }

    public RemoteException(String str, Throwable th) {
        super(str);
        initCause(null);
        this.detail = th;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return super.getMessage() + "; nested exception is: \n\t" + this.detail.toString();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.detail;
    }
}
