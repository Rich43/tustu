package javax.crypto;

import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Vector;

/* compiled from: CryptoPermission.java */
/* loaded from: jce.jar:javax/crypto/CryptoPermissionCollection.class */
final class CryptoPermissionCollection extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = -511215555898802763L;
    private Vector<Permission> permissions = new Vector<>(3);

    CryptoPermissionCollection() {
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (isReadOnly()) {
            throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
        }
        if (!(permission instanceof CryptoPermission)) {
            return;
        }
        this.permissions.addElement(permission);
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof CryptoPermission)) {
            return false;
        }
        CryptoPermission cryptoPermission = (CryptoPermission) permission;
        Enumeration<Permission> enumerationElements = this.permissions.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((CryptoPermission) enumerationElements.nextElement2()).implies(cryptoPermission)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        return this.permissions.elements();
    }
}
