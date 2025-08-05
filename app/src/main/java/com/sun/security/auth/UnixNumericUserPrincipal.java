package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/UnixNumericUserPrincipal.class */
public class UnixNumericUserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -4329764253802397821L;
    private String name;

    public UnixNumericUserPrincipal(String str) {
        if (str == null) {
            throw new NullPointerException(new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources")).format(new Object[]{"name"}));
        }
        this.name = str;
    }

    public UnixNumericUserPrincipal(long j2) {
        this.name = new Long(j2).toString();
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    public long longValue() {
        return new Long(this.name).longValue();
    }

    @Override // java.security.Principal
    public String toString() {
        return new MessageFormat(ResourcesMgr.getString("UnixNumericUserPrincipal.name", "sun.security.util.AuthResources")).format(new Object[]{this.name});
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if ((obj instanceof UnixNumericUserPrincipal) && getName().equals(((UnixNumericUserPrincipal) obj).getName())) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }
}
