package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPException.class */
public class SOAPException extends Exception {
    private Throwable cause;

    public SOAPException() {
        this.cause = null;
    }

    public SOAPException(String reason) {
        super(reason);
        this.cause = null;
    }

    public SOAPException(String reason, Throwable cause) {
        super(reason);
        initCause(cause);
    }

    public SOAPException(Throwable cause) {
        super(cause.toString());
        initCause(cause);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && this.cause != null) {
            return this.cause.getMessage();
        }
        return message;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }

    @Override // java.lang.Throwable
    public synchronized Throwable initCause(Throwable cause) {
        if (this.cause != null) {
            throw new IllegalStateException("Can't override cause");
        }
        if (cause == this) {
            throw new IllegalArgumentException("Self-causation not permitted");
        }
        this.cause = cause;
        return this;
    }
}
