package javax.management.remote;

import java.io.IOException;

/* loaded from: rt.jar:javax/management/remote/JMXProviderException.class */
public class JMXProviderException extends IOException {
    private static final long serialVersionUID = -3166703627550447198L;
    private Throwable cause;

    public JMXProviderException() {
        this.cause = null;
    }

    public JMXProviderException(String str) {
        super(str);
        this.cause = null;
    }

    public JMXProviderException(String str, Throwable th) {
        super(str);
        this.cause = null;
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
