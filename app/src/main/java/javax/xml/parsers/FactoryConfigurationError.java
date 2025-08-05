package javax.xml.parsers;

/* loaded from: rt.jar:javax/xml/parsers/FactoryConfigurationError.class */
public class FactoryConfigurationError extends Error {
    private static final long serialVersionUID = -827108682472263355L;
    private Exception exception;

    public FactoryConfigurationError() {
        this.exception = null;
    }

    public FactoryConfigurationError(String msg) {
        super(msg);
        this.exception = null;
    }

    public FactoryConfigurationError(Exception e2) {
        super(e2.toString());
        this.exception = e2;
    }

    public FactoryConfigurationError(Exception e2, String msg) {
        super(msg);
        this.exception = e2;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && this.exception != null) {
            return this.exception.getMessage();
        }
        return message;
    }

    public Exception getException() {
        return this.exception;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.exception;
    }
}
