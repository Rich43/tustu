package javax.management.remote;

import java.io.IOException;

/* loaded from: rt.jar:javax/management/remote/JMXServerErrorException.class */
public class JMXServerErrorException extends IOException {
    private static final long serialVersionUID = 3996732239558744666L;
    private final Error cause;

    public JMXServerErrorException(String str, Error error) {
        super(str);
        this.cause = error;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
