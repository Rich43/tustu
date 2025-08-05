package java.lang;

/* loaded from: rt.jar:java/lang/Error.class */
public class Error extends Throwable {
    static final long serialVersionUID = 4980196508277280342L;

    public Error() {
    }

    public Error(String str) {
        super(str);
    }

    public Error(String str, Throwable th) {
        super(str, th);
    }

    public Error(Throwable th) {
        super(th);
    }

    protected Error(String str, Throwable th, boolean z2, boolean z3) {
        super(str, th, z2, z3);
    }
}
