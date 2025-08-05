package javax.management;

/* loaded from: rt.jar:javax/management/MBeanRegistrationException.class */
public class MBeanRegistrationException extends MBeanException {
    private static final long serialVersionUID = 4482382455277067805L;

    public MBeanRegistrationException(Exception exc) {
        super(exc);
    }

    public MBeanRegistrationException(Exception exc, String str) {
        super(exc, str);
    }
}
