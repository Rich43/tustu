package javax.xml.transform;

/* loaded from: rt.jar:javax/xml/transform/TransformerFactoryConfigurationError.class */
public class TransformerFactoryConfigurationError extends Error {
    private static final long serialVersionUID = -6527718720676281516L;
    private Exception exception;

    public TransformerFactoryConfigurationError() {
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(String msg) {
        super(msg);
        this.exception = null;
    }

    public TransformerFactoryConfigurationError(Exception e2) {
        super(e2.toString());
        this.exception = e2;
    }

    public TransformerFactoryConfigurationError(Exception e2, String msg) {
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
