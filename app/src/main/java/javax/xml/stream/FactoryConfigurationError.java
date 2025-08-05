package javax.xml.stream;

/* loaded from: rt.jar:javax/xml/stream/FactoryConfigurationError.class */
public class FactoryConfigurationError extends Error {
    private static final long serialVersionUID = -2994412584589975744L;
    Exception nested;

    public FactoryConfigurationError() {
    }

    public FactoryConfigurationError(Exception e2) {
        this.nested = e2;
    }

    public FactoryConfigurationError(Exception e2, String msg) {
        super(msg);
        this.nested = e2;
    }

    public FactoryConfigurationError(String msg, Exception e2) {
        super(msg);
        this.nested = e2;
    }

    public FactoryConfigurationError(String msg) {
        super(msg);
    }

    public Exception getException() {
        return this.nested;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.nested;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) {
            return msg;
        }
        if (this.nested != null) {
            msg = this.nested.getMessage();
            if (msg == null) {
                msg = this.nested.getClass().toString();
            }
        }
        return msg;
    }
}
