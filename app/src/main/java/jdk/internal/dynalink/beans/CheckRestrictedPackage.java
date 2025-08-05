package jdk.internal.dynalink.beans;

import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CheckRestrictedPackage.class */
class CheckRestrictedPackage {
    private static final AccessControlContext NO_PERMISSIONS_CONTEXT = createNoPermissionsContext();

    CheckRestrictedPackage() {
    }

    static boolean isRestrictedClass(Class<?> clazz) {
        final String name;
        final int i2;
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return true;
        }
        final SecurityManager sm = System.getSecurityManager();
        if (sm == null || (i2 = (name = clazz.getName()).lastIndexOf(46)) == -1) {
            return false;
        }
        try {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.internal.dynalink.beans.CheckRestrictedPackage.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    sm.checkPackageAccess(name.substring(0, i2));
                    return null;
                }
            }, NO_PERMISSIONS_CONTEXT);
            return false;
        } catch (SecurityException e2) {
            return true;
        }
    }

    private static AccessControlContext createNoPermissionsContext() {
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, new Permissions())});
    }
}
