package sun.security.acl;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.NotOwnerException;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: rt.jar:sun/security/acl/AclImpl.class */
public class AclImpl extends OwnerImpl implements Acl {
    private Hashtable<Principal, AclEntry> allowedUsersTable;
    private Hashtable<Principal, AclEntry> allowedGroupsTable;
    private Hashtable<Principal, AclEntry> deniedUsersTable;
    private Hashtable<Principal, AclEntry> deniedGroupsTable;
    private String aclName;
    private Vector<Permission> zeroSet;

    public AclImpl(Principal principal, String str) {
        super(principal);
        this.allowedUsersTable = new Hashtable<>(23);
        this.allowedGroupsTable = new Hashtable<>(23);
        this.deniedUsersTable = new Hashtable<>(23);
        this.deniedGroupsTable = new Hashtable<>(23);
        this.aclName = null;
        this.zeroSet = new Vector<>(1, 1);
        try {
            setName(principal, str);
        } catch (Exception e2) {
        }
    }

    @Override // java.security.acl.Acl
    public void setName(Principal principal, String str) throws NotOwnerException {
        if (!isOwner(principal)) {
            throw new NotOwnerException();
        }
        this.aclName = str;
    }

    @Override // java.security.acl.Acl
    public String getName() {
        return this.aclName;
    }

    @Override // java.security.acl.Acl
    public synchronized boolean addEntry(Principal principal, AclEntry aclEntry) throws NotOwnerException {
        if (!isOwner(principal)) {
            throw new NotOwnerException();
        }
        Hashtable<Principal, AclEntry> hashtableFindTable = findTable(aclEntry);
        Principal principal2 = aclEntry.getPrincipal();
        if (hashtableFindTable.get(principal2) != null) {
            return false;
        }
        hashtableFindTable.put(principal2, aclEntry);
        return true;
    }

    @Override // java.security.acl.Acl
    public synchronized boolean removeEntry(Principal principal, AclEntry aclEntry) throws NotOwnerException {
        if (isOwner(principal)) {
            return findTable(aclEntry).remove(aclEntry.getPrincipal()) != null;
        }
        throw new NotOwnerException();
    }

    @Override // java.security.acl.Acl
    public synchronized Enumeration<Permission> getPermissions(Principal principal) {
        Enumeration<Permission> enumerationSubtract = subtract(getGroupPositive(principal), getGroupNegative(principal));
        Enumeration<Permission> enumerationSubtract2 = subtract(getGroupNegative(principal), getGroupPositive(principal));
        return subtract(union(subtract(getIndividualPositive(principal), getIndividualNegative(principal)), subtract(enumerationSubtract, subtract(getIndividualNegative(principal), getIndividualPositive(principal)))), union(subtract(getIndividualNegative(principal), getIndividualPositive(principal)), subtract(enumerationSubtract2, subtract(getIndividualPositive(principal), getIndividualNegative(principal)))));
    }

    @Override // java.security.acl.Acl
    public boolean checkPermission(Principal principal, Permission permission) {
        Enumeration<Permission> permissions = getPermissions(principal);
        while (permissions.hasMoreElements()) {
            if (permissions.nextElement2().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.security.acl.Acl
    public synchronized Enumeration<AclEntry> entries() {
        return new AclEnumerator(this, this.allowedUsersTable, this.allowedGroupsTable, this.deniedUsersTable, this.deniedGroupsTable);
    }

    @Override // java.security.acl.Acl
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration<AclEntry> enumerationEntries = entries();
        while (enumerationEntries.hasMoreElements()) {
            stringBuffer.append(enumerationEntries.nextElement2().toString().trim());
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    private Hashtable<Principal, AclEntry> findTable(AclEntry aclEntry) {
        Hashtable<Principal, AclEntry> hashtable;
        if (aclEntry.getPrincipal() instanceof Group) {
            if (aclEntry.isNegative()) {
                hashtable = this.deniedGroupsTable;
            } else {
                hashtable = this.allowedGroupsTable;
            }
        } else if (aclEntry.isNegative()) {
            hashtable = this.deniedUsersTable;
        } else {
            hashtable = this.allowedUsersTable;
        }
        return hashtable;
    }

    private static Enumeration<Permission> union(Enumeration<Permission> enumeration, Enumeration<Permission> enumeration2) {
        Vector vector = new Vector(20, 20);
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement2());
        }
        while (enumeration2.hasMoreElements()) {
            Permission permissionNextElement2 = enumeration2.nextElement2();
            if (!vector.contains(permissionNextElement2)) {
                vector.addElement(permissionNextElement2);
            }
        }
        return vector.elements();
    }

    private Enumeration<Permission> subtract(Enumeration<Permission> enumeration, Enumeration<Permission> enumeration2) {
        Vector vector = new Vector(20, 20);
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement2());
        }
        while (enumeration2.hasMoreElements()) {
            Permission permissionNextElement2 = enumeration2.nextElement2();
            if (vector.contains(permissionNextElement2)) {
                vector.removeElement(permissionNextElement2);
            }
        }
        return vector.elements();
    }

    private Enumeration<Permission> getGroupPositive(Principal principal) {
        Enumeration<Permission> enumerationElements = this.zeroSet.elements();
        Enumeration<Principal> enumerationKeys = this.allowedGroupsTable.keys();
        while (enumerationKeys.hasMoreElements()) {
            Group group = (Group) enumerationKeys.nextElement2();
            if (group.isMember(principal)) {
                enumerationElements = union(this.allowedGroupsTable.get(group).permissions(), enumerationElements);
            }
        }
        return enumerationElements;
    }

    private Enumeration<Permission> getGroupNegative(Principal principal) {
        Enumeration<Permission> enumerationElements = this.zeroSet.elements();
        Enumeration<Principal> enumerationKeys = this.deniedGroupsTable.keys();
        while (enumerationKeys.hasMoreElements()) {
            Group group = (Group) enumerationKeys.nextElement2();
            if (group.isMember(principal)) {
                enumerationElements = union(this.deniedGroupsTable.get(group).permissions(), enumerationElements);
            }
        }
        return enumerationElements;
    }

    private Enumeration<Permission> getIndividualPositive(Principal principal) {
        Enumeration<Permission> enumerationElements = this.zeroSet.elements();
        AclEntry aclEntry = this.allowedUsersTable.get(principal);
        if (aclEntry != null) {
            enumerationElements = aclEntry.permissions();
        }
        return enumerationElements;
    }

    private Enumeration<Permission> getIndividualNegative(Principal principal) {
        Enumeration<Permission> enumerationElements = this.zeroSet.elements();
        AclEntry aclEntry = this.deniedUsersTable.get(principal);
        if (aclEntry != null) {
            enumerationElements = aclEntry.permissions();
        }
        return enumerationElements;
    }
}
