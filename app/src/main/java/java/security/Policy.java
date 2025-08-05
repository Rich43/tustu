package java.security;

import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;
import sun.security.jca.GetInstance;
import sun.security.provider.PolicyFile;
import sun.security.util.Debug;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/security/Policy.class */
public abstract class Policy {
    public static final PermissionCollection UNSUPPORTED_EMPTY_COLLECTION = new UnsupportedEmptyCollection();
    private static AtomicReference<PolicyInfo> policy = new AtomicReference<>(new PolicyInfo(null, false));
    private static final Debug debug = Debug.getInstance("policy");
    private WeakHashMap<ProtectionDomain.Key, PermissionCollection> pdMapping;

    /* loaded from: rt.jar:java/security/Policy$Parameters.class */
    public interface Parameters {
    }

    /* loaded from: rt.jar:java/security/Policy$PolicyInfo.class */
    private static class PolicyInfo {
        final Policy policy;
        final boolean initialized;

        PolicyInfo(Policy policy, boolean z2) {
            this.policy = policy;
            this.initialized = z2;
        }
    }

    static boolean isSet() {
        PolicyInfo policyInfo = policy.get();
        return policyInfo.policy != null && policyInfo.initialized;
    }

    private static void checkPermission(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("createPolicy." + str));
        }
    }

    public static Policy getPolicy() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_POLICY_PERMISSION);
        }
        return getPolicyNoCheck();
    }

    static Policy getPolicyNoCheck() {
        Policy policy2;
        PolicyInfo policyInfo = policy.get();
        if (!policyInfo.initialized || policyInfo.policy == null) {
            synchronized (Policy.class) {
                PolicyInfo policyInfo2 = policy.get();
                if (policyInfo2.policy == null) {
                    String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.Policy.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public String run() {
                            return Security.getProperty("policy.provider");
                        }
                    });
                    if (str == null) {
                        str = "sun.security.provider.PolicyFile";
                    }
                    try {
                        policyInfo2 = new PolicyInfo((Policy) Class.forName(str).newInstance(), true);
                    } catch (Exception e2) {
                        PolicyFile policyFile = new PolicyFile();
                        policy.set(new PolicyInfo(policyFile, false));
                        final String str2 = str;
                        Policy policy3 = (Policy) AccessController.doPrivileged(new PrivilegedAction<Policy>() { // from class: java.security.Policy.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            public Policy run() {
                                try {
                                    ClassLoader classLoader = null;
                                    for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
                                        classLoader = systemClassLoader;
                                    }
                                    if (classLoader != null) {
                                        return (Policy) Class.forName(str2, true, classLoader).newInstance();
                                    }
                                    return null;
                                } catch (Exception e3) {
                                    if (Policy.debug != null) {
                                        Policy.debug.println("policy provider " + str2 + " not available");
                                        e3.printStackTrace();
                                        return null;
                                    }
                                    return null;
                                }
                            }
                        });
                        if (policy3 != null) {
                            policyInfo2 = new PolicyInfo(policy3, true);
                        } else {
                            if (debug != null) {
                                debug.println("using sun.security.provider.PolicyFile");
                            }
                            policyInfo2 = new PolicyInfo(policyFile, true);
                        }
                    }
                    policy.set(policyInfo2);
                    policy2 = policyInfo2.policy;
                } else {
                    policy2 = policyInfo2.policy;
                }
            }
            return policy2;
        }
        return policyInfo.policy;
    }

    public static void setPolicy(Policy policy2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("setPolicy"));
        }
        if (policy2 != null) {
            initPolicy(policy2);
        }
        synchronized (Policy.class) {
            policy.set(new PolicyInfo(policy2, policy2 != null));
        }
    }

    private static void initPolicy(Policy policy2) {
        ProtectionDomain protectionDomain = (ProtectionDomain) AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>() { // from class: java.security.Policy.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ProtectionDomain run() {
                return Policy.this.getClass().getProtectionDomain();
            }
        });
        PermissionCollection permissions = null;
        synchronized (policy2) {
            if (policy2.pdMapping == null) {
                policy2.pdMapping = new WeakHashMap<>();
            }
        }
        if (protectionDomain.getCodeSource() != null) {
            Policy policy3 = policy.get().policy;
            if (policy3 != null) {
                permissions = policy3.getPermissions(protectionDomain);
            }
            if (permissions == null) {
                permissions = new Permissions();
                permissions.add(SecurityConstants.ALL_PERMISSION);
            }
            synchronized (policy2.pdMapping) {
                policy2.pdMapping.put(protectionDomain.key, permissions);
            }
        }
    }

    public static Policy getInstance(String str, Parameters parameters) throws NoSuchAlgorithmException {
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Policy", (Class<?>) PolicySpi.class, str, parameters);
            return new PolicyDelegate((PolicySpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public static Policy getInstance(String str, Parameters parameters, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Policy", (Class<?>) PolicySpi.class, str, parameters, str2);
            return new PolicyDelegate((PolicySpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public static Policy getInstance(String str, Parameters parameters, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        checkPermission(str);
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("Policy", (Class<?>) PolicySpi.class, str, parameters, provider);
            return new PolicyDelegate((PolicySpi) getInstance.impl, getInstance.provider, str, parameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    private static Policy handleException(NoSuchAlgorithmException noSuchAlgorithmException) throws NoSuchAlgorithmException {
        Throwable cause = noSuchAlgorithmException.getCause();
        if (cause instanceof IllegalArgumentException) {
            throw ((IllegalArgumentException) cause);
        }
        throw noSuchAlgorithmException;
    }

    public Provider getProvider() {
        return null;
    }

    public String getType() {
        return null;
    }

    public Parameters getParameters() {
        return null;
    }

    public PermissionCollection getPermissions(CodeSource codeSource) {
        return UNSUPPORTED_EMPTY_COLLECTION;
    }

    public PermissionCollection getPermissions(ProtectionDomain protectionDomain) {
        PermissionCollection permissionCollection;
        if (protectionDomain == null) {
            return new Permissions();
        }
        if (this.pdMapping == null) {
            initPolicy(this);
        }
        synchronized (this.pdMapping) {
            permissionCollection = this.pdMapping.get(protectionDomain.key);
        }
        if (permissionCollection != null) {
            Permissions permissions = new Permissions();
            synchronized (permissionCollection) {
                Enumeration<Permission> enumerationElements = permissionCollection.elements();
                while (enumerationElements.hasMoreElements()) {
                    permissions.add(enumerationElements.nextElement2());
                }
            }
            return permissions;
        }
        PermissionCollection permissions2 = getPermissions(protectionDomain.getCodeSource());
        if (permissions2 == null || permissions2 == UNSUPPORTED_EMPTY_COLLECTION) {
            permissions2 = new Permissions();
        }
        addStaticPerms(permissions2, protectionDomain.getPermissions());
        return permissions2;
    }

    private void addStaticPerms(PermissionCollection permissionCollection, PermissionCollection permissionCollection2) {
        if (permissionCollection2 != null) {
            synchronized (permissionCollection2) {
                Enumeration<Permission> enumerationElements = permissionCollection2.elements();
                while (enumerationElements.hasMoreElements()) {
                    permissionCollection.add(enumerationElements.nextElement2());
                }
            }
        }
    }

    public boolean implies(ProtectionDomain protectionDomain, Permission permission) {
        PermissionCollection permissionCollection;
        if (this.pdMapping == null) {
            initPolicy(this);
        }
        synchronized (this.pdMapping) {
            permissionCollection = this.pdMapping.get(protectionDomain.key);
        }
        if (permissionCollection != null) {
            return permissionCollection.implies(permission);
        }
        PermissionCollection permissions = getPermissions(protectionDomain);
        if (permissions == null) {
            return false;
        }
        synchronized (this.pdMapping) {
            this.pdMapping.put(protectionDomain.key, permissions);
        }
        return permissions.implies(permission);
    }

    public void refresh() {
    }

    /* loaded from: rt.jar:java/security/Policy$PolicyDelegate.class */
    private static class PolicyDelegate extends Policy {
        private PolicySpi spi;

        /* renamed from: p, reason: collision with root package name */
        private Provider f12469p;
        private String type;
        private Parameters params;

        private PolicyDelegate(PolicySpi policySpi, Provider provider, String str, Parameters parameters) {
            this.spi = policySpi;
            this.f12469p = provider;
            this.type = str;
            this.params = parameters;
        }

        @Override // java.security.Policy
        public String getType() {
            return this.type;
        }

        @Override // java.security.Policy
        public Parameters getParameters() {
            return this.params;
        }

        @Override // java.security.Policy
        public Provider getProvider() {
            return this.f12469p;
        }

        @Override // java.security.Policy
        public PermissionCollection getPermissions(CodeSource codeSource) {
            return this.spi.engineGetPermissions(codeSource);
        }

        @Override // java.security.Policy
        public PermissionCollection getPermissions(ProtectionDomain protectionDomain) {
            return this.spi.engineGetPermissions(protectionDomain);
        }

        @Override // java.security.Policy
        public boolean implies(ProtectionDomain protectionDomain, Permission permission) {
            return this.spi.engineImplies(protectionDomain, permission);
        }

        @Override // java.security.Policy
        public void refresh() {
            this.spi.engineRefresh();
        }
    }

    /* loaded from: rt.jar:java/security/Policy$UnsupportedEmptyCollection.class */
    private static class UnsupportedEmptyCollection extends PermissionCollection {
        private static final long serialVersionUID = -8492269157353014774L;
        private Permissions perms = new Permissions();

        public UnsupportedEmptyCollection() {
            this.perms.setReadOnly();
        }

        @Override // java.security.PermissionCollection
        public void add(Permission permission) {
            this.perms.add(permission);
        }

        @Override // java.security.PermissionCollection
        public boolean implies(Permission permission) {
            return this.perms.implies(permission);
        }

        @Override // java.security.PermissionCollection
        public Enumeration<Permission> elements() {
            return this.perms.elements();
        }
    }
}
