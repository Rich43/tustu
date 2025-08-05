package sun.security.acl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:sun/security/acl/GroupImpl.class */
public class GroupImpl implements Group {
    private Vector<Principal> groupMembers = new Vector<>(50, 100);
    private String group;

    public GroupImpl(String str) {
        this.group = str;
    }

    @Override // java.security.acl.Group
    public boolean addMember(Principal principal) {
        if (this.groupMembers.contains(principal)) {
            return false;
        }
        if (this.group.equals(principal.toString())) {
            throw new IllegalArgumentException();
        }
        this.groupMembers.addElement(principal);
        return true;
    }

    @Override // java.security.acl.Group
    public boolean removeMember(Principal principal) {
        return this.groupMembers.removeElement(principal);
    }

    @Override // java.security.acl.Group
    public Enumeration<? extends Principal> members() {
        return this.groupMembers.elements();
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        return this.group.equals(((Group) obj).toString());
    }

    public boolean equals(Group group) {
        return equals((Object) group);
    }

    @Override // java.security.Principal
    public String toString() {
        return this.group;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.group.hashCode();
    }

    @Override // java.security.acl.Group
    public boolean isMember(Principal principal) {
        if (this.groupMembers.contains(principal)) {
            return true;
        }
        return isMemberRecurse(principal, new Vector<>(10));
    }

    @Override // java.security.Principal
    public String getName() {
        return this.group;
    }

    boolean isMemberRecurse(Principal principal, Vector<Group> vector) {
        Enumeration<? extends Principal> enumerationMembers = members();
        while (enumerationMembers.hasMoreElements()) {
            boolean zIsMember = false;
            Principal principalNextElement = enumerationMembers.nextElement();
            if (principalNextElement.equals(principal)) {
                return true;
            }
            if (principalNextElement instanceof GroupImpl) {
                GroupImpl groupImpl = (GroupImpl) principalNextElement;
                vector.addElement(this);
                if (!vector.contains(groupImpl)) {
                    zIsMember = groupImpl.isMemberRecurse(principal, vector);
                }
            } else if (principalNextElement instanceof Group) {
                Group group = (Group) principalNextElement;
                if (!vector.contains(group)) {
                    zIsMember = group.isMember(principal);
                }
            }
            if (zIsMember) {
                return zIsMember;
            }
        }
        return false;
    }
}
