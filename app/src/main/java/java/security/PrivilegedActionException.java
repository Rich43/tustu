package java.security;

/* loaded from: rt.jar:java/security/PrivilegedActionException.class */
public class PrivilegedActionException extends Exception {
    private static final long serialVersionUID = 4724086851538908602L;
    private Exception exception;

    public PrivilegedActionException(Exception exc) {
        super((Throwable) null);
        this.exception = exc;
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
        String name = getClass().getName();
        return this.exception != null ? name + ": " + this.exception.toString() : name;
    }
}
