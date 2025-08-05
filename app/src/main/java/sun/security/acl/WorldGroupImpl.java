package sun.security.acl;

import java.security.Principal;

/* loaded from: rt.jar:sun/security/acl/WorldGroupImpl.class */
public class WorldGroupImpl extends GroupImpl {
    public WorldGroupImpl(String str) {
        super(str);
    }

    @Override // sun.security.acl.GroupImpl, java.security.acl.Group
    public boolean isMember(Principal principal) {
        return true;
    }
}
