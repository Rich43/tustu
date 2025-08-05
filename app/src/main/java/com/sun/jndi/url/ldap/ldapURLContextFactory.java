package com.sun.jndi.url.ldap;

import com.sun.jndi.ldap.LdapCtx;
import com.sun.jndi.ldap.LdapCtxFactory;
import com.sun.jndi.ldap.LdapURL;
import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.ResolveResult;

/* loaded from: rt.jar:com/sun/jndi/url/ldap/ldapURLContextFactory.class */
public class ldapURLContextFactory implements ObjectFactory {
    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
        if (obj == null) {
            return new ldapURLContext(hashtable);
        }
        return LdapCtxFactory.getLdapCtxInstance(obj, hashtable);
    }

    static ResolveResult getUsingURLIgnoreRootDN(String str, Hashtable<?, ?> hashtable) throws NamingException {
        LdapURL ldapURL = new LdapURL(str);
        LdapCtx ldapCtx = new LdapCtx("", ldapURL.getHost(), ldapURL.getPort(), hashtable, ldapURL.useSsl());
        String dn = ldapURL.getDN() != null ? ldapURL.getDN() : "";
        CompositeName compositeName = new CompositeName();
        if (!"".equals(dn)) {
            compositeName.add(dn);
        }
        return new ResolveResult(ldapCtx, compositeName);
    }
}
