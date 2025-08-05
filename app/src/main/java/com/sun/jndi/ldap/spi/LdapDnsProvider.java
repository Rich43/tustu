package com.sun.jndi.ldap.spi;

import java.util.Map;
import java.util.Optional;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/ldap/spi/LdapDnsProvider.class */
public abstract class LdapDnsProvider {
    private static final RuntimePermission DNSPROVIDER_PERMISSION = new RuntimePermission("ldapDnsProvider");

    public abstract Optional<LdapDnsProviderResult> lookupEndpoints(String str, Map<?, ?> map) throws NamingException;

    protected LdapDnsProvider() {
        this(checkPermission());
    }

    private LdapDnsProvider(Void r3) {
    }

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(DNSPROVIDER_PERMISSION);
            return null;
        }
        return null;
    }
}
