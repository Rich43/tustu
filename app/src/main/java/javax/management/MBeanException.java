package javax.management;

/* loaded from: rt.jar:javax/management/MBeanException.class */
public class MBeanException extends JMException {
    private static final long serialVersionUID = 4066342430588744142L;
    private Exception exception;

    public MBeanException(Exception exc) {
        this.exception = exc;
    }

    public MBeanException(Exception exc, String str) {
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
