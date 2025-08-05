package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/UserPrincipal.class */
public final class UserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 892106070870210969L;
    private final String name;

    public UserPrincipal(String str) {
        if (str == null) {
            throw new NullPointerException("null name is illegal");
        }
        this.name = str;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UserPrincipal) {
            return this.name.equals(((UserPrincipal) obj).getName());
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    @Override // java.security.Principal
    public String toString() {
        return this.name;
    }
}
