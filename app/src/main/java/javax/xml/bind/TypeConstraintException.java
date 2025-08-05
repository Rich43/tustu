package javax.xml.bind;

import java.io.PrintStream;

/* loaded from: rt.jar:javax/xml/bind/TypeConstraintException.class */
public class TypeConstraintException extends RuntimeException {
    private String errorCode;
    private volatile Throwable linkedException;
    static final long serialVersionUID = -3059799699420143848L;

    public TypeConstraintException(String message) {
        this(message, null, null);
    }

    public TypeConstraintException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public TypeConstraintException(Throwable exception) {
        this(null, null, exception);
    }

    public TypeConstraintException(String message, Throwable exception) {
        this(message, null, exception);
    }

    public TypeConstraintException(String message, String errorCode, Throwable exception) {
        super(message);
        this.errorCode = errorCode;
        this.linkedException = exception;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public Throwable getLinkedException() {
        return this.linkedException;
    }

    public void setLinkedException(Throwable exception) {
        this.linkedException = exception;
    }

    @Override // java.lang.Throwable
    public String toString() {
        if (this.linkedException == null) {
            return super.toString();
        }
        return super.toString() + "\n - with linked exception:\n[" + this.linkedException.toString() + "]";
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        if (this.linkedException != null) {
            this.linkedException.printStackTrace(s2);
            s2.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s2);
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }
}
