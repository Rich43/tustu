package sun.security.acl;

import java.security.Principal;

/* loaded from: rt.jar:sun/security/acl/PrincipalImpl.class */
public class PrincipalImpl implements Principal {
    private String user;

    public PrincipalImpl(String str) {
        this.user = str;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj instanceof PrincipalImpl) {
            return this.user.equals(((PrincipalImpl) obj).toString());
        }
        return false;
    }

    @Override // java.security.Principal
    public String toString() {
        return this.user;
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.user.hashCode();
    }

    @Override // java.security.Principal
    public String getName() {
        return this.user;
    }
}
