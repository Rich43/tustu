package javax.management;

/* loaded from: rt.jar:javax/management/RuntimeErrorException.class */
public class RuntimeErrorException extends JMRuntimeException {
    private static final long serialVersionUID = 704338937753949796L;
    private Error error;

    public RuntimeErrorException(Error error) {
        this.error = error;
    }

    public RuntimeErrorException(Error error, String str) {
        super(str);
        this.error = error;
    }

    public Error getTargetError() {
        return this.error;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.error;
    }
}
