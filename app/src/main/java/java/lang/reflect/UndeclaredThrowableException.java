package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/UndeclaredThrowableException.class */
public class UndeclaredThrowableException extends RuntimeException {
    static final long serialVersionUID = 330127114055056639L;
    private Throwable undeclaredThrowable;

    public UndeclaredThrowableException(Throwable th) {
        super((Throwable) null);
        this.undeclaredThrowable = th;
    }

    public UndeclaredThrowableException(Throwable th, String str) {
        super(str, null);
        this.undeclaredThrowable = th;
    }

    public Throwable getUndeclaredThrowable() {
        return this.undeclaredThrowable;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.undeclaredThrowable;
    }
}
