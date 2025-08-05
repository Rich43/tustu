package com.sun.jndi.ldap;

import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapNameParser.class */
class LdapNameParser implements NameParser {
    @Override // javax.naming.NameParser
    public Name parse(String str) throws NamingException {
        return new javax.naming.ldap.LdapName(str);
    }
}
