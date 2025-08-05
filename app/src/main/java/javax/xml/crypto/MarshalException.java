package javax.xml.crypto;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/crypto/MarshalException.class */
public class MarshalException extends Exception {
    private static final long serialVersionUID = -863185580332643547L;
    private Throwable cause;

    public MarshalException() {
    }

    public MarshalException(String str) {
        super(str);
    }

    public MarshalException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public MarshalException(Throwable th) {
        super(th == null ? null : th.toString());
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
    }
}
