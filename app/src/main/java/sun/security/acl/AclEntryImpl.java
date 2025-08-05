package sun.security.acl;

import java.security.Principal;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Vector;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/acl/AclEntryImpl.class */
public class AclEntryImpl implements AclEntry {
    private Principal user;
    private Vector<Permission> permissionSet;
    private boolean negative;

    public AclEntryImpl(Principal principal) {
        this.user = null;
        this.permissionSet = new Vector<>(10, 10);
        this.negative = false;
        this.user = principal;
    }

    public AclEntryImpl() {
        this.user = null;
        this.permissionSet = new Vector<>(10, 10);
        this.negative = false;
    }

    @Override // java.security.acl.AclEntry
    public boolean setPrincipal(Principal principal) {
        if (this.user != null) {
            return false;
        }
        this.user = principal;
        return true;
    }

    @Override // java.security.acl.AclEntry
    public void setNegativePermissions() {
        this.negative = true;
    }

    @Override // java.security.acl.AclEntry
    public boolean isNegative() {
        return this.negative;
    }

    @Override // java.security.acl.AclEntry
    public boolean addPermission(Permission permission) {
        if (this.permissionSet.contains(permission)) {
            return false;
        }
        this.permissionSet.addElement(permission);
        return true;
    }

    @Override // java.security.acl.AclEntry
    public boolean removePermission(Permission permission) {
        return this.permissionSet.removeElement(permission);
    }

    @Override // java.security.acl.AclEntry
    public boolean checkPermission(Permission permission) {
        return this.permissionSet.contains(permission);
    }

    @Override // java.security.acl.AclEntry
    public Enumeration<Permission> permissions() {
        return this.permissionSet.elements();
    }

    @Override // java.security.acl.AclEntry
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.negative) {
            stringBuffer.append(LanguageTag.SEP);
        } else {
            stringBuffer.append(Marker.ANY_NON_NULL_MARKER);
        }
        if (this.user instanceof Group) {
            stringBuffer.append("Group.");
        } else {
            stringBuffer.append("User.");
        }
        stringBuffer.append(((Object) this.user) + "=");
        Enumeration<Permission> enumerationPermissions = permissions();
        while (enumerationPermissions.hasMoreElements()) {
            stringBuffer.append((Object) enumerationPermissions.nextElement2());
            if (enumerationPermissions.hasMoreElements()) {
                stringBuffer.append(",");
            }
        }
        return new String(stringBuffer);
    }

    @Override // java.security.acl.AclEntry
    public synchronized Object clone() {
        AclEntryImpl aclEntryImpl = new AclEntryImpl(this.user);
        aclEntryImpl.permissionSet = (Vector) this.permissionSet.clone();
        aclEntryImpl.negative = this.negative;
        return aclEntryImpl;
    }

    @Override // java.security.acl.AclEntry
    public Principal getPrincipal() {
        return this.user;
    }
}
