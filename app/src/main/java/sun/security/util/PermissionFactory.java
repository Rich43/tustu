package sun.security.util;

import java.security.Permission;

/* loaded from: rt.jar:sun/security/util/PermissionFactory.class */
public interface PermissionFactory<T extends Permission> {
    T newPermission(String str);
}
