package jdk.internal.org.xml.sax;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/SAXException.class */
public class SAXException extends Exception {
    private Exception exception;
    static final long serialVersionUID = 583241635256073760L;

    public SAXException() {
        this.exception = null;
    }

    public SAXException(String str) {
        super(str);
        this.exception = null;
    }

    public SAXException(Exception exc) {
        this.exception = exc;
    }

    public SAXException(String str, Exception exc) {
        super(str);
        this.exception = exc;
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

    @Override // java.lang.Throwable
    public String toString() {
        if (this.exception != null) {
            return super.toString() + "\n" + this.exception.toString();
        }
        return super.toString();
    }
}
