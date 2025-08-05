package java.lang;

/* loaded from: rt.jar:java/lang/Exception.class */
public class Exception extends Throwable {
    static final long serialVersionUID = -3387516993124229948L;

    public Exception() {
    }

    public Exception(String str) {
        super(str);
    }

    public Exception(String str, Throwable th) {
        super(str, th);
    }

    public Exception(Throwable th) {
        super(th);
    }

    protected Exception(String str, Throwable th, boolean z2, boolean z3) {
        super(str, th, z2, z3);
    }
}
