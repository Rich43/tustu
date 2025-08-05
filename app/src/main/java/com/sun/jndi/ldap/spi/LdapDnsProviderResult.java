package com.sun.jndi.ldap.spi;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/jndi/ldap/spi/LdapDnsProviderResult.class */
public final class LdapDnsProviderResult {
    private final String domainName;
    private final List<String> endpoints;

    public LdapDnsProviderResult(String str, List<String> list) {
        this.domainName = str == null ? "" : str;
        this.endpoints = new ArrayList(list);
    }

    public String getDomainName() {
        return this.domainName;
    }

    public List<String> getEndpoints() {
        return this.endpoints;
    }
}
