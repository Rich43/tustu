package com.sun.security.auth;

import java.io.Serializable;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;
import jdk.Exported;

@Exported(false)
@Deprecated
/* loaded from: rt.jar:com/sun/security/auth/SolarisNumericGroupPrincipal.class */
public class SolarisNumericGroupPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 2345199581042573224L;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.security.auth.SolarisNumericGroupPrincipal.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private String name;
    private boolean primaryGroup;

    public SolarisNumericGroupPrincipal(String str, boolean z2) {
        if (str == null) {
            throw new NullPointerException(rb.getString("provided.null.name"));
        }
        this.name = str;
        this.primaryGroup = z2;
    }

    public SolarisNumericGroupPrincipal(long j2, boolean z2) {
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
            return rb.getString("SolarisNumericGroupPrincipal.Primary.Group.") + this.name;
        }
        return rb.getString("SolarisNumericGroupPrincipal.Supplementary.Group.") + this.name;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SolarisNumericGroupPrincipal)) {
            return false;
        }
        SolarisNumericGroupPrincipal solarisNumericGroupPrincipal = (SolarisNumericGroupPrincipal) obj;
        if (getName().equals(solarisNumericGroupPrincipal.getName()) && isPrimaryGroup() == solarisNumericGroupPrincipal.isPrimaryGroup()) {
            return true;
        }
        return false;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return toString().hashCode();
    }
}
