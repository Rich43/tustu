package java.lang;

/* loaded from: rt.jar:java/lang/ExceptionInInitializerError.class */
public class ExceptionInInitializerError extends LinkageError {
    private static final long serialVersionUID = 1521711792217232256L;
    private Throwable exception;

    public ExceptionInInitializerError() {
        initCause(null);
    }

    public ExceptionInInitializerError(Throwable th) {
        initCause(null);
        this.exception = th;
    }

    public ExceptionInInitializerError(String str) {
        super(str);
        initCause(null);
    }

    public Throwable getException() {
        return this.exception;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.exception;
    }
}
