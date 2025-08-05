package javax.naming.ldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ReferralException;

/* loaded from: rt.jar:javax/naming/ldap/LdapReferralException.class */
public abstract class LdapReferralException extends ReferralException {
    private static final long serialVersionUID = -1668992791764950804L;

    @Override // javax.naming.ReferralException
    public abstract Context getReferralContext() throws NamingException;

    @Override // javax.naming.ReferralException
    public abstract Context getReferralContext(Hashtable<?, ?> hashtable) throws NamingException;

    public abstract Context getReferralContext(Hashtable<?, ?> hashtable, Control[] controlArr) throws NamingException;

    protected LdapReferralException(String str) {
        super(str);
    }

    protected LdapReferralException() {
    }
}
