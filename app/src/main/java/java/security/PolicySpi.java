package java.security;

/* loaded from: rt.jar:java/security/PolicySpi.class */
public abstract class PolicySpi {
    protected abstract boolean engineImplies(ProtectionDomain protectionDomain, Permission permission);

    protected void engineRefresh() {
    }

    protected PermissionCollection engineGetPermissions(CodeSource codeSource) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }

    protected PermissionCollection engineGetPermissions(ProtectionDomain protectionDomain) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }
}
