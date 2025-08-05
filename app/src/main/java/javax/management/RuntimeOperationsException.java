package javax.management;

/* loaded from: rt.jar:javax/management/RuntimeOperationsException.class */
public class RuntimeOperationsException extends JMRuntimeException {
    private static final long serialVersionUID = -8408923047489133588L;
    private RuntimeException runtimeException;

    public RuntimeOperationsException(RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public RuntimeOperationsException(RuntimeException runtimeException, String str) {
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
