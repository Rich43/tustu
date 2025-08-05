package javax.crypto;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/* compiled from: CryptoPermissions.java */
/* loaded from: jce.jar:javax/crypto/PermissionsEnumerator.class */
final class PermissionsEnumerator implements Enumeration<Permission> {
    private Enumeration<PermissionCollection> perms;
    private Enumeration<Permission> permset = getNextEnumWithMore();

    PermissionsEnumerator(Enumeration<PermissionCollection> enumeration) {
        this.perms = enumeration;
    }

    @Override // java.util.Enumeration
    public synchronized boolean hasMoreElements() {
        if (this.permset == null) {
            return false;
        }
        if (this.permset.hasMoreElements()) {
            return true;
        }
        this.permset = getNextEnumWithMore();
        return this.permset != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Enumeration
    public synchronized Permission nextElement() {
        if (hasMoreElements()) {
            return this.permset.nextElement();
        }
        throw new NoSuchElementException("PermissionsEnumerator");
    }

    private Enumeration<Permission> getNextEnumWithMore() {
        while (this.perms.hasMoreElements()) {
            Enumeration<Permission> enumerationElements = this.perms.nextElement().elements();
            if (enumerationElements.hasMoreElements()) {
                return enumerationElements;
            }
        }
        return null;
    }
}
