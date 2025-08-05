package javax.xml.ws;

/* loaded from: rt.jar:javax/xml/ws/WebServiceException.class */
public class WebServiceException extends RuntimeException {
    public WebServiceException() {
    }

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }
}
