package sun.misc;

import java.security.PermissionCollection;
import java.security.ProtectionDomain;

/* loaded from: rt.jar:sun/misc/JavaSecurityProtectionDomainAccess.class */
public interface JavaSecurityProtectionDomainAccess {

    /* loaded from: rt.jar:sun/misc/JavaSecurityProtectionDomainAccess$ProtectionDomainCache.class */
    public interface ProtectionDomainCache {
        void put(ProtectionDomain protectionDomain, PermissionCollection permissionCollection);

        PermissionCollection get(ProtectionDomain protectionDomain);
    }

    ProtectionDomainCache getProtectionDomainCache();

    boolean getStaticPermissionsField(ProtectionDomain protectionDomain);
}
