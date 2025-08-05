package sun.security.acl;

import java.security.Principal;
import java.security.acl.Group;
import java.security.acl.LastOwnerException;
import java.security.acl.NotOwnerException;
import java.security.acl.Owner;
import java.util.Enumeration;

/* loaded from: rt.jar:sun/security/acl/OwnerImpl.class */
public class OwnerImpl implements Owner {
    private Group ownerGroup = new GroupImpl("AclOwners");

    public OwnerImpl(Principal principal) {
        this.ownerGroup.addMember(principal);
    }

    @Override // java.security.acl.Owner
    public synchronized boolean addOwner(Principal principal, Principal principal2) throws NotOwnerException {
        if (!isOwner(principal)) {
            throw new NotOwnerException();
        }
        this.ownerGroup.addMember(principal2);
        return false;
    }

    @Override // java.security.acl.Owner
    public synchronized boolean deleteOwner(Principal principal, Principal principal2) throws NotOwnerException, LastOwnerException {
        if (!isOwner(principal)) {
            throw new NotOwnerException();
        }
        Enumeration<? extends Principal> enumerationMembers = this.ownerGroup.members();
        enumerationMembers.nextElement();
        if (enumerationMembers.hasMoreElements()) {
            return this.ownerGroup.removeMember(principal2);
        }
        throw new LastOwnerException();
    }

    @Override // java.security.acl.Owner
    public synchronized boolean isOwner(Principal principal) {
        return this.ownerGroup.isMember(principal);
    }
}
