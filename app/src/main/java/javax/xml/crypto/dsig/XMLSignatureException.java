package javax.xml.crypto.dsig;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/crypto/dsig/XMLSignatureException.class */
public class XMLSignatureException extends Exception {
    private static final long serialVersionUID = -3438102491013869995L;
    private Throwable cause;

    public XMLSignatureException() {
    }

    public XMLSignatureException(String str) {
        super(str);
    }

    public XMLSignatureException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public XMLSignatureException(Throwable th) {
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
        if (this.cause != null) {
            this.cause.printStackTrace();
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (this.cause != null) {
            this.cause.printStackTrace(printStream);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (this.cause != null) {
            this.cause.printStackTrace(printWriter);
        }
    }
}
