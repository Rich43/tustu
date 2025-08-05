package javax.xml.transform;

/* loaded from: rt.jar:javax/xml/transform/TransformerConfigurationException.class */
public class TransformerConfigurationException extends TransformerException {
    private static final long serialVersionUID = 1285547467942875745L;

    public TransformerConfigurationException() {
        super("Configuration Error");
    }

    public TransformerConfigurationException(String msg) {
        super(msg);
    }

    public TransformerConfigurationException(Throwable e2) {
        super(e2);
    }

    public TransformerConfigurationException(String msg, Throwable e2) {
        super(msg, e2);
    }

    public TransformerConfigurationException(String message, SourceLocator locator) {
        super(message, locator);
    }

    public TransformerConfigurationException(String message, SourceLocator locator, Throwable e2) {
        super(message, locator, e2);
    }
}
