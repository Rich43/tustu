package javax.security.auth;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.Security;
import java.util.Objects;
import sun.security.util.Debug;
import sun.security.util.ResourcesMgr;

@Deprecated
/* loaded from: rt.jar:javax/security/auth/Policy.class */
public abstract class Policy {
    private static Policy policy;
    private static final String AUTH_POLICY = "sun.security.provider.AuthPolicyFile";
    private final AccessControlContext acc = AccessController.getContext();
    private static boolean isCustomPolicy;

    public abstract PermissionCollection getPermissions(Subject subject, CodeSource codeSource);

    public abstract void refresh();

    protected Policy() {
    }

    public static Policy getPolicy() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("getPolicy"));
        }
        return getPolicyNoCheck();
    }

    static Policy getPolicyNoCheck() {
        if (policy == null) {
            synchronized (Policy.class) {
                if (policy == null) {
                    String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.auth.Policy.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public String run2() {
                            return Security.getProperty("auth.policy.provider");
                        }
                    });
                    if (str == null) {
                        str = AUTH_POLICY;
                    }
                    try {
                        final String str2 = str;
                        Policy policy2 = (Policy) AccessController.doPrivileged(new PrivilegedExceptionAction<Policy>() { // from class: javax.security.auth.Policy.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Policy run() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
                                return (Policy) Class.forName(str2, false, Thread.currentThread().getContextClassLoader()).asSubclass(Policy.class).newInstance();
                            }
                        });
                        AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: javax.security.auth.Policy.3
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Void run() {
                                Policy.setPolicy(Policy.this);
                                boolean unused = Policy.isCustomPolicy = !str2.equals(Policy.AUTH_POLICY);
                                return null;
                            }
                        }, (AccessControlContext) Objects.requireNonNull(policy2.acc));
                    } catch (Exception e2) {
                        throw new SecurityException(ResourcesMgr.getString("unable.to.instantiate.Subject.based.policy"));
                    }
                }
            }
        }
        return policy;
    }

    public static void setPolicy(Policy policy2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("setPolicy"));
        }
        policy = policy2;
        isCustomPolicy = policy2 != null;
    }

    static boolean isCustomPolicySet(Debug debug) {
        if (policy != null) {
            if (debug != null && isCustomPolicy) {
                debug.println("Providing backwards compatibility for javax.security.auth.policy implementation: " + policy.toString());
            }
            return isCustomPolicy;
        }
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.auth.Policy.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("auth.policy.provider");
            }
        });
        if (str != null && !str.equals(AUTH_POLICY)) {
            if (debug != null) {
                debug.println("Providing backwards compatibility for javax.security.auth.policy implementation: " + str);
                return true;
            }
            return true;
        }
        return false;
    }
}
