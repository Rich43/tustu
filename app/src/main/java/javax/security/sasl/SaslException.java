package javax.security.sasl;

import java.io.IOException;

/* loaded from: rt.jar:javax/security/sasl/SaslException.class */
public class SaslException extends IOException {
    private Throwable _exception;
    private static final long serialVersionUID = 4579784287983423626L;

    public SaslException() {
    }

    public SaslException(String str) {
        super(str);
    }

    public SaslException(String str, Throwable th) {
        super(str);
        if (th != null) {
            initCause(th);
        }
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this._exception;
    }

    @Override // java.lang.Throwable
    public Throwable initCause(Throwable th) {
        super.initCause(th);
        this._exception = th;
        return this;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String string = super.toString();
        if (this._exception != null && this._exception != this) {
            string = string + " [Caused by " + this._exception.toString() + "]";
        }
        return string;
    }
}
