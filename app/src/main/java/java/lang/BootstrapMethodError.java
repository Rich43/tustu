package java.lang;

/* loaded from: rt.jar:java/lang/BootstrapMethodError.class */
public class BootstrapMethodError extends LinkageError {
    private static final long serialVersionUID = 292;

    public BootstrapMethodError() {
    }

    public BootstrapMethodError(String str) {
        super(str);
    }

    public BootstrapMethodError(String str, Throwable th) {
        super(str, th);
    }

    public BootstrapMethodError(Throwable th) {
        super(th == null ? null : th.toString());
        initCause(th);
    }
}
