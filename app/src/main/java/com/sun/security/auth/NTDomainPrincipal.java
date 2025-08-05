package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/NTDomainPrincipal.class */
public class NTDomainPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -4408637351440771220L;
    private String name;

    public NTDomainPrincipal(String str) {
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
        return new MessageFormat(ResourcesMgr.getString("NTDomainPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{this.name});
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof NTDomainPrincipal) && this.name.equals(((NTDomainPrincipal) obj).getName())) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return getName().hashCode();
    }
}
