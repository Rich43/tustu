package sun.security.acl;

import java.security.acl.Permission;

/* loaded from: rt.jar:sun/security/acl/AllPermissionsImpl.class */
public class AllPermissionsImpl extends PermissionImpl {
    public AllPermissionsImpl(String str) {
        super(str);
    }

    public boolean equals(Permission permission) {
        return true;
    }
}
