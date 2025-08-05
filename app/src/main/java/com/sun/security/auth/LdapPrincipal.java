package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/LdapPrincipal.class */
public final class LdapPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 6820120005580754861L;
    private final String nameString;
    private final LdapName name;

    public LdapPrincipal(String str) throws InvalidNameException {
        if (str == null) {
            throw new NullPointerException("null name is illegal");
        }
        this.name = getLdapName(str);
        this.nameString = str;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LdapPrincipal) {
            try {
                return this.name.equals(getLdapName(((LdapPrincipal) obj).getName()));
            } catch (InvalidNameException e2) {
                return false;
            }
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override // java.security.Principal
    public String getName() {
        return this.nameString;
    }

    @Override // java.security.Principal
    public String toString() {
        return this.name.toString();
    }

    private LdapName getLdapName(String str) throws InvalidNameException {
        return new LdapName(str);
    }
}
