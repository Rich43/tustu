package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/UnixPrincipal.class */
public class UnixPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -2951667807323493631L;
    private String name;

    public UnixPrincipal(String str) {
        if (str == null) {
            throw new NullPointerException(new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources")).format(new Object[]{"name"}));
        }
        this.name = str;
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    @Override // java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("UnixPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{this.name});
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof UnixPrincipal) && getName().equals(((UnixPrincipal) obj).getName())) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }
}
