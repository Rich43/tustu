package java.rmi.activation;

/* loaded from: rt.jar:java/rmi/activation/ActivationException.class */
public class ActivationException extends Exception {
    public Throwable detail;
    private static final long serialVersionUID = -4320118837291406071L;

    public ActivationException() {
        initCause(null);
    }

    public ActivationException(String str) {
        super(str);
        initCause(null);
    }

    public ActivationException(String str, Throwable th) {
        super(str);
        initCause(null);
        this.detail = th;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return super.getMessage() + "; nested exception is: \n\t" + this.detail.toString();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.detail;
    }
}
