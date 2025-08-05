package javax.management;

/* loaded from: rt.jar:javax/management/ReflectionException.class */
public class ReflectionException extends JMException {
    private static final long serialVersionUID = 9170809325636915553L;
    private Exception exception;

    public ReflectionException(Exception exc) {
        this.exception = exc;
    }

    public ReflectionException(Exception exc, String str) {
        super(str);
        this.exception = exc;
    }

    public Exception getTargetException() {
        return this.exception;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.exception;
    }
}
