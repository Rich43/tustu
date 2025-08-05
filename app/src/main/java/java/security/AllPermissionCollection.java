package java.security;

import java.io.Serializable;
import java.util.Enumeration;
import sun.security.util.SecurityConstants;

/* compiled from: AllPermission.java */
/* loaded from: rt.jar:java/security/AllPermissionCollection.class */
final class AllPermissionCollection extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = -4023755556366636806L;
    private boolean all_allowed = false;

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (!(permission instanceof AllPermission)) {
            throw new IllegalArgumentException("invalid permission: " + ((Object) permission));
        }
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        this.all_allowed = true;
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        return this.all_allowed;
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        return new Enumeration<Permission>() { // from class: java.security.AllPermissionCollection.1
            private boolean hasMore;

            {
                this.hasMore = AllPermissionCollection.this.all_allowed;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.hasMore;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public Permission nextElement() {
                this.hasMore = false;
                return SecurityConstants.ALL_PERMISSION;
            }
        };
    }
}
