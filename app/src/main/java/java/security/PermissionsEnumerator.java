package java.security;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* compiled from: Permissions.java */
/* loaded from: rt.jar:java/security/PermissionsEnumerator.class */
final class PermissionsEnumerator implements Enumeration<Permission> {
    private Iterator<PermissionCollection> perms;
    private Enumeration<Permission> permset = getNextEnumWithMore();

    PermissionsEnumerator(Iterator<PermissionCollection> it) {
        this.perms = it;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
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
    public Permission nextElement() {
        if (hasMoreElements()) {
            return this.permset.nextElement();
        }
        throw new NoSuchElementException("PermissionsEnumerator");
    }

    private Enumeration<Permission> getNextEnumWithMore() {
        while (this.perms.hasNext()) {
            Enumeration<Permission> enumerationElements = this.perms.next().elements();
            if (enumerationElements.hasMoreElements()) {
                return enumerationElements;
            }
        }
        return null;
    }
}
