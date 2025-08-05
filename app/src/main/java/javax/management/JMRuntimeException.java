package javax.management;

/* loaded from: rt.jar:javax/management/JMRuntimeException.class */
public class JMRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6573344628407841861L;

    public JMRuntimeException() {
    }

    public JMRuntimeException(String str) {
        super(str);
    }

    JMRuntimeException(String str, Throwable th) {
        super(str, th);
    }
}
