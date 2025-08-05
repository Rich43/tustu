package java.lang;

/* loaded from: rt.jar:java/lang/AssertionError.class */
public class AssertionError extends Error {
    private static final long serialVersionUID = -5013299493970297370L;

    public AssertionError() {
    }

    private AssertionError(String str) {
        super(str);
    }

    public AssertionError(Object obj) {
        this(String.valueOf(obj));
        if (obj instanceof Throwable) {
            initCause((Throwable) obj);
        }
    }

    public AssertionError(boolean z2) {
        this(String.valueOf(z2));
    }

    public AssertionError(char c2) {
        this(String.valueOf(c2));
    }

    public AssertionError(int i2) {
        this(String.valueOf(i2));
    }

    public AssertionError(long j2) {
        this(String.valueOf(j2));
    }

    public AssertionError(float f2) {
        this(String.valueOf(f2));
    }

    public AssertionError(double d2) {
        this(String.valueOf(d2));
    }

    public AssertionError(String str, Throwable th) {
        super(str, th);
    }
}
