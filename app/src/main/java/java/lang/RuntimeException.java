package java.lang;

/* loaded from: rt.jar:java/lang/RuntimeException.class */
public class RuntimeException extends Exception {
    static final long serialVersionUID = -7034897190745766939L;

    public RuntimeException() {
    }

    public RuntimeException(String str) {
        super(str);
    }

    public RuntimeException(String str, Throwable th) {
        super(str, th);
    }

    public RuntimeException(Throwable th) {
        super(th);
    }

    protected RuntimeException(String str, Throwable th, boolean z2, boolean z3) {
        super(str, th, z2, z3);
    }
}
