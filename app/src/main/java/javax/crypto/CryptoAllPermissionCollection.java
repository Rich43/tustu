package javax.crypto;

import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Vector;

/* compiled from: CryptoAllPermission.java */
/* loaded from: jce.jar:javax/crypto/CryptoAllPermissionCollection.class */
final class CryptoAllPermissionCollection extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = 7450076868380144072L;
    private boolean all_allowed = false;

    CryptoAllPermissionCollection() {
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        if (permission != CryptoAllPermission.INSTANCE) {
            return;
        }
        this.all_allowed = true;
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof CryptoPermission)) {
            return false;
        }
        return this.all_allowed;
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        Vector vector = new Vector(1);
        if (this.all_allowed) {
            vector.add(CryptoAllPermission.INSTANCE);
        }
        return vector.elements();
    }
}
