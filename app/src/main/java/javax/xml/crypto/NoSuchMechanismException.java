package javax.xml.crypto;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/crypto/NoSuchMechanismException.class */
public class NoSuchMechanismException extends RuntimeException {
    private static final long serialVersionUID = 4189669069570660166L;
    private Throwable cause;

    public NoSuchMechanismException() {
    }

    public NoSuchMechanismException(String str) {
        super(str);
    }

    public NoSuchMechanismException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public NoSuchMechanismException(Throwable th) {
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
