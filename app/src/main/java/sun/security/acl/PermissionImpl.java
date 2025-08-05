package sun.security.acl;

import java.security.acl.Permission;

/* loaded from: rt.jar:sun/security/acl/PermissionImpl.class */
public class PermissionImpl implements Permission {
    private String permission;

    public PermissionImpl(String str) {
        this.permission = str;
    }

    @Override // java.security.acl.Permission
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            return this.permission.equals(((Permission) obj).toString());
        }
        return false;
    }

    @Override // java.security.acl.Permission
    public String toString() {
        return this.permission;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
