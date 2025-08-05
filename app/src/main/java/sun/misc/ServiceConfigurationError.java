package sun.misc;

/* loaded from: rt.jar:sun/misc/ServiceConfigurationError.class */
public class ServiceConfigurationError extends Error {
    static final long serialVersionUID = 8769866263384244465L;

    public ServiceConfigurationError(String str) {
        super(str);
    }

    public ServiceConfigurationError(Throwable th) {
        super(th);
    }
}
