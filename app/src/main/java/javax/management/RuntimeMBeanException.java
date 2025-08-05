package javax.management;

/* loaded from: rt.jar:javax/management/RuntimeMBeanException.class */
public class RuntimeMBeanException extends JMRuntimeException {
    private static final long serialVersionUID = 5274912751982730171L;
    private RuntimeException runtimeException;

    public RuntimeMBeanException(RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public RuntimeMBeanException(RuntimeException runtimeException, String str) {
        super(str);
        this.runtimeException = runtimeException;
    }

    public RuntimeException getTargetException() {
        return this.runtimeException;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.runtimeException;
    }
}
