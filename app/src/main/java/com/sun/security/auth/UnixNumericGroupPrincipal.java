package com.sun.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;
import jdk.Exported;
import sun.security.util.ResourcesMgr;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/UnixNumericGroupPrincipal.class */
public class UnixNumericGroupPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 3941535899328403223L;
    private String name;
    private boolean primaryGroup;

    public UnixNumericGroupPrincipal(String str, boolean z2) {
        if (str == null) {
            throw new NullPointerException(new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources")).format(new Object[]{"name"}));
        }
        this.name = str;
        this.primaryGroup = z2;
    }

    public UnixNumericGroupPrincipal(long j2, boolean z2) {
        this.name = new Long(j2).toString();
        this.primaryGroup = z2;
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    public long longValue() {
        return new Long(this.name).longValue();
    }

    public boolean isPrimaryGroup() {
        return this.primaryGroup;
    }

    @Override // java.security.Principal
    public String toString() {
        if (this.primaryGroup) {
            return new MessageFormat(ResourcesMgr.getString("UnixNumericGroupPrincipal.Primary.Group.name", "sun.security.util.AuthResources")).format(new Object[]{this.name});
        }
        return new MessageFormat(ResourcesMgr.getString("UnixNumericGroupPrincipal.Supplementary.Group.name", "sun.security.util.AuthResources")).format(new Object[]{this.name});
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnixNumericGroupPrincipal)) {
            return false;
        }
        UnixNumericGroupPrincipal unixNumericGroupPrincipal = (UnixNumericGroupPrincipal) obj;
        if (getName().equals(unixNumericGroupPrincipal.getName()) && isPrimaryGroup() == unixNumericGroupPrincipal.isPrimaryGroup()) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return toString().hashCode();
    }
}
