package com.sun.jndi.dns;

import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsNameParser.class */
class DnsNameParser implements NameParser {
    DnsNameParser() {
    }

    @Override // javax.naming.NameParser
    public Name parse(String str) throws NamingException {
        return new DnsName(str);
    }

    public boolean equals(Object obj) {
        return obj instanceof DnsNameParser;
    }

    public int hashCode() {
        return DnsNameParser.class.hashCode() + 1;
    }
}
