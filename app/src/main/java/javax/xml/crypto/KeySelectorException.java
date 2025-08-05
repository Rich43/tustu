package javax.xml.crypto;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/crypto/KeySelectorException.class */
public class KeySelectorException extends Exception {
    private static final long serialVersionUID = -7480033639322531109L;
    private Throwable cause;

    public KeySelectorException() {
    }

    public KeySelectorException(String str) {
        super(str);
    }

    public KeySelectorException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public KeySelectorException(Throwable th) {
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
