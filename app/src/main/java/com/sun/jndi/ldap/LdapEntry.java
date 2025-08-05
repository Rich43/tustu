package com.sun.jndi.ldap;

import java.util.Vector;
import javax.naming.directory.Attributes;
import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapEntry.class */
final class LdapEntry {
    String DN;
    Attributes attributes;
    Vector<Control> respCtls;

    LdapEntry(String str, Attributes attributes) {
        this.respCtls = null;
        this.DN = str;
        this.attributes = attributes;
    }

    LdapEntry(String str, Attributes attributes, Vector<Control> vector) {
        this.respCtls = null;
        this.DN = str;
        this.attributes = attributes;
        this.respCtls = vector;
    }
}
