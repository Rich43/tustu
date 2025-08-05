package javax.naming;

import java.util.Hashtable;

/* loaded from: rt.jar:javax/naming/ReferralException.class */
public abstract class ReferralException extends NamingException {
    private static final long serialVersionUID = -2881363844695698876L;

    public abstract Object getReferralInfo();

    public abstract Context getReferralContext() throws NamingException;

    public abstract Context getReferralContext(Hashtable<?, ?> hashtable) throws NamingException;

    public abstract boolean skipReferral();

    public abstract void retryReferral();

    protected ReferralException(String str) {
        super(str);
    }

    protected ReferralException() {
    }
}
