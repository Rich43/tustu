package javax.management;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/* compiled from: MBeanServerPermission.java */
/* loaded from: rt.jar:javax/management/MBeanServerPermissionCollection.class */
class MBeanServerPermissionCollection extends PermissionCollection {
    private MBeanServerPermission collectionPermission;
    private static final long serialVersionUID = -5661980843569388590L;

    MBeanServerPermissionCollection() {
    }

    @Override // java.security.PermissionCollection
    public synchronized void add(Permission permission) {
        if (!(permission instanceof MBeanServerPermission)) {
            throw new IllegalArgumentException("Permission not an MBeanServerPermission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("Read-only permission collection");
        }
        MBeanServerPermission mBeanServerPermission = (MBeanServerPermission) permission;
        if (this.collectionPermission == null) {
            this.collectionPermission = mBeanServerPermission;
        } else if (!this.collectionPermission.implies(permission)) {
            this.collectionPermission = new MBeanServerPermission(this.collectionPermission.mask | mBeanServerPermission.mask);
        }
    }

    @Override // java.security.PermissionCollection
    public synchronized boolean implies(Permission permission) {
        return this.collectionPermission != null && this.collectionPermission.implies(permission);
    }

    @Override // java.security.PermissionCollection
    public synchronized Enumeration<Permission> elements() {
        Set setSingleton;
        if (this.collectionPermission == null) {
            setSingleton = Collections.emptySet();
        } else {
            setSingleton = Collections.singleton(this.collectionPermission);
        }
        return Collections.enumeration(setSingleton);
    }
}
