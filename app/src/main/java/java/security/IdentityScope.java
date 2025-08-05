package java.security;

import java.util.Enumeration;

@Deprecated
/* loaded from: rt.jar:java/security/IdentityScope.class */
public abstract class IdentityScope extends Identity {
    private static final long serialVersionUID = -2337346281189773310L;
    private static IdentityScope scope;

    public abstract int size();

    public abstract Identity getIdentity(String str);

    public abstract Identity getIdentity(PublicKey publicKey);

    public abstract void addIdentity(Identity identity) throws KeyManagementException;

    public abstract void removeIdentity(Identity identity) throws KeyManagementException;

    public abstract Enumeration<Identity> identities();

    private static void initializeSystemScope() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.IdentityScope.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("system.scope");
            }
        });
        if (str == null) {
            return;
        }
        try {
            Class.forName(str);
        } catch (ClassNotFoundException e2) {
            System.err.println("unable to establish a system scope from " + str);
            e2.printStackTrace();
        }
    }

    protected IdentityScope() {
        this("restoring...");
    }

    public IdentityScope(String str) {
        super(str);
    }

    public IdentityScope(String str, IdentityScope identityScope) throws KeyManagementException {
        super(str, identityScope);
    }

    public static IdentityScope getSystemScope() {
        if (scope == null) {
            initializeSystemScope();
        }
        return scope;
    }

    protected static void setSystemScope(IdentityScope identityScope) {
        check("setSystemScope");
        scope = identityScope;
    }

    public Identity getIdentity(Principal principal) {
        return getIdentity(principal.getName());
    }

    @Override // java.security.Identity, java.security.Principal
    public String toString() {
        return super.toString() + "[" + size() + "]";
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }
}
