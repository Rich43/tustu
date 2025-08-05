package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XNIException.class */
public class XNIException extends RuntimeException {
    static final long serialVersionUID = 9019819772686063775L;
    private Exception fException;

    public XNIException(String message) {
        super(message);
    }

    public XNIException(Exception exception) {
        super(exception.getMessage());
        this.fException = exception;
    }

    public XNIException(String message, Exception exception) {
        super(message);
        this.fException = exception;
    }

    public Exception getException() {
        return this.fException;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.fException;
    }
}
