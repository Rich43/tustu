package java.security.acl;

import java.security.Principal;
import java.util.Enumeration;

/* loaded from: rt.jar:java/security/acl/Group.class */
public interface Group extends Principal {
    boolean addMember(Principal principal);

    boolean removeMember(Principal principal);

    boolean isMember(Principal principal);

    Enumeration<? extends Principal> members();
}
