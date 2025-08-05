package javax.xml.crypto;

import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/crypto/URIReferenceException.class */
public class URIReferenceException extends Exception {
    private static final long serialVersionUID = 7173469703932561419L;
    private Throwable cause;
    private URIReference uriReference;

    public URIReferenceException() {
    }

    public URIReferenceException(String str) {
        super(str);
    }

    public URIReferenceException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public URIReferenceException(String str, Throwable th, URIReference uRIReference) {
        this(str, th);
        if (uRIReference == null) {
            throw new NullPointerException("uriReference cannot be null");
        }
        this.uriReference = uRIReference;
    }

    public URIReferenceException(Throwable th) {
        super(th == null ? null : th.toString());
        this.cause = th;
    }

    public URIReference getURIReference() {
        return this.uriReference;
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
