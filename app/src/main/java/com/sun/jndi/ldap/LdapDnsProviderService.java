package com.sun.jndi.ldap;

import com.sun.jndi.ldap.spi.LdapDnsProvider;
import com.sun.jndi.ldap.spi.LdapDnsProviderResult;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.naming.NamingException;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapDnsProviderService.class */
final class LdapDnsProviderService {
    private static volatile LdapDnsProviderService service;
    private static final Object LOCK = new int[0];
    private final ServiceLoader<LdapDnsProvider> providers;

    private LdapDnsProviderService() {
        if (System.getSecurityManager() == null) {
            this.providers = ServiceLoader.load(LdapDnsProvider.class, ClassLoader.getSystemClassLoader());
        } else {
            this.providers = (ServiceLoader) AccessController.doPrivileged(() -> {
                return ServiceLoader.load(LdapDnsProvider.class, ClassLoader.getSystemClassLoader());
            }, (AccessControlContext) null, new RuntimePermission("ldapDnsProvider"), SecurityConstants.GET_CLASSLOADER_PERMISSION);
        }
    }

    static LdapDnsProviderService getInstance() {
        if (service != null) {
            return service;
        }
        synchronized (LOCK) {
            if (service != null) {
                return service;
            }
            service = new LdapDnsProviderService();
            return service;
        }
    }

    LdapDnsProviderResult lookupEndpoints(String str, Hashtable<?, ?> hashtable) throws NamingException {
        LdapDnsProviderResult ldapDnsProviderResultOrElse = null;
        Hashtable hashtable2 = new Hashtable(hashtable);
        synchronized (LOCK) {
            Iterator<LdapDnsProvider> it = this.providers.iterator();
            while (ldapDnsProviderResultOrElse == null && it.hasNext()) {
                ldapDnsProviderResultOrElse = it.next().lookupEndpoints(str, hashtable2).filter(ldapDnsProviderResult -> {
                    return !ldapDnsProviderResult.getEndpoints().isEmpty();
                }).orElse(null);
            }
        }
        if (ldapDnsProviderResultOrElse == null) {
            return new DefaultLdapDnsProvider().lookupEndpoints(str, hashtable).orElse(new LdapDnsProviderResult("", Collections.emptyList()));
        }
        return ldapDnsProviderResultOrElse;
    }
}
