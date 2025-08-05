package com.sun.jndi.ldap;

import javax.naming.NamingEnumeration;

/* loaded from: rt.jar:com/sun/jndi/ldap/ReferralEnumeration.class */
interface ReferralEnumeration<T> extends NamingEnumeration<T> {
    void appendUnprocessedReferrals(LdapReferralException ldapReferralException);
}
