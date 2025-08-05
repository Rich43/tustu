package java.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;
import sun.misc.JavaSecurityAccess;
import sun.misc.JavaSecurityProtectionDomainAccess;
import sun.misc.SharedSecrets;
import sun.security.util.Debug;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/security/ProtectionDomain.class */
public class ProtectionDomain {
    private CodeSource codesource;
    private ClassLoader classloader;
    private Principal[] principals;
    private PermissionCollection permissions;
    private boolean hasAllPerm;
    private boolean staticPermissions;
    final Key key = new Key();
    private static final Debug debug;

    /* loaded from: rt.jar:java/security/ProtectionDomain$JavaSecurityAccessImpl.class */
    private static class JavaSecurityAccessImpl implements JavaSecurityAccess {
        private JavaSecurityAccessImpl() {
        }

        @Override // sun.misc.JavaSecurityAccess
        public <T> T doIntersectionPrivilege(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext, AccessControlContext accessControlContext2) {
            if (privilegedAction == null) {
                throw new NullPointerException();
            }
            return (T) AccessController.doPrivileged(privilegedAction, getCombinedACC(accessControlContext2, accessControlContext));
        }

        @Override // sun.misc.JavaSecurityAccess
        public <T> T doIntersectionPrivilege(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext) {
            return (T) doIntersectionPrivilege(privilegedAction, AccessController.getContext(), accessControlContext);
        }

        private static AccessControlContext getCombinedACC(AccessControlContext accessControlContext, AccessControlContext accessControlContext2) {
            return new AccessControlContext(accessControlContext2.getContext(), new AccessControlContext(accessControlContext, accessControlContext2.getCombiner(), true)).optimize();
        }
    }

    static {
        SharedSecrets.setJavaSecurityAccess(new JavaSecurityAccessImpl());
        debug = Debug.getInstance("domain");
        SharedSecrets.setJavaSecurityProtectionDomainAccess(new JavaSecurityProtectionDomainAccess() { // from class: java.security.ProtectionDomain.2
            @Override // sun.misc.JavaSecurityProtectionDomainAccess
            public JavaSecurityProtectionDomainAccess.ProtectionDomainCache getProtectionDomainCache() {
                return new JavaSecurityProtectionDomainAccess.ProtectionDomainCache() { // from class: java.security.ProtectionDomain.2.1
                    private final Map<Key, PermissionCollection> map = Collections.synchronizedMap(new WeakHashMap());

                    @Override // sun.misc.JavaSecurityProtectionDomainAccess.ProtectionDomainCache
                    public void put(ProtectionDomain protectionDomain, PermissionCollection permissionCollection) {
                        this.map.put(protectionDomain == null ? null : protectionDomain.key, permissionCollection);
                    }

                    @Override // sun.misc.JavaSecurityProtectionDomainAccess.ProtectionDomainCache
                    public PermissionCollection get(ProtectionDomain protectionDomain) {
                        return protectionDomain == null ? this.map.get(null) : this.map.get(protectionDomain.key);
                    }
                };
            }

            @Override // sun.misc.JavaSecurityProtectionDomainAccess
            public boolean getStaticPermissionsField(ProtectionDomain protectionDomain) {
                return protectionDomain.staticPermissions;
            }
        });
    }

    public ProtectionDomain(CodeSource codeSource, PermissionCollection permissionCollection) {
        this.hasAllPerm = false;
        this.codesource = codeSource;
        if (permissionCollection != null) {
            this.permissions = permissionCollection;
            this.permissions.setReadOnly();
            if ((permissionCollection instanceof Permissions) && ((Permissions) permissionCollection).allPermission != null) {
                this.hasAllPerm = true;
            }
        }
        this.classloader = null;
        this.principals = new Principal[0];
        this.staticPermissions = true;
    }

    public ProtectionDomain(CodeSource codeSource, PermissionCollection permissionCollection, ClassLoader classLoader, Principal[] principalArr) {
        this.hasAllPerm = false;
        this.codesource = codeSource;
        if (permissionCollection != null) {
            this.permissions = permissionCollection;
            this.permissions.setReadOnly();
            if ((permissionCollection instanceof Permissions) && ((Permissions) permissionCollection).allPermission != null) {
                this.hasAllPerm = true;
            }
        }
        this.classloader = classLoader;
        this.principals = principalArr != null ? (Principal[]) principalArr.clone() : new Principal[0];
        this.staticPermissions = false;
    }

    public final CodeSource getCodeSource() {
        return this.codesource;
    }

    public final ClassLoader getClassLoader() {
        return this.classloader;
    }

    public final Principal[] getPrincipals() {
        return (Principal[]) this.principals.clone();
    }

    public final PermissionCollection getPermissions() {
        return this.permissions;
    }

    public boolean implies(Permission permission) {
        if (this.hasAllPerm) {
            return true;
        }
        if (!this.staticPermissions && Policy.getPolicyNoCheck().implies(this, permission)) {
            return true;
        }
        if (this.permissions != null) {
            return this.permissions.implies(permission);
        }
        return false;
    }

    boolean impliesCreateAccessControlContext() {
        return implies(SecurityConstants.CREATE_ACC_PERMISSION);
    }

    public String toString() {
        PermissionCollection permissions;
        String string = "<no principals>";
        if (this.principals != null && this.principals.length > 0) {
            StringBuilder sb = new StringBuilder("(principals ");
            for (int i2 = 0; i2 < this.principals.length; i2++) {
                sb.append(this.principals[i2].getClass().getName() + " \"" + this.principals[i2].getName() + PdfOps.DOUBLE_QUOTE__TOKEN);
                if (i2 < this.principals.length - 1) {
                    sb.append(",\n");
                } else {
                    sb.append(")\n");
                }
            }
            string = sb.toString();
        }
        if (Policy.isSet() && seeAllp()) {
            permissions = mergePermissions();
        } else {
            permissions = getPermissions();
        }
        return "ProtectionDomain  " + ((Object) this.codesource) + "\n " + ((Object) this.classloader) + "\n " + string + "\n " + ((Object) permissions) + "\n";
    }

    private static boolean seeAllp() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return true;
        }
        if (debug != null) {
            if (securityManager.getClass().getClassLoader() == null && Policy.getPolicyNoCheck().getClass().getClassLoader() == null) {
                return true;
            }
            return false;
        }
        try {
            securityManager.checkPermission(SecurityConstants.GET_POLICY_PERMISSION);
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private PermissionCollection mergePermissions() {
        if (this.staticPermissions) {
            return this.permissions;
        }
        PermissionCollection permissionCollection = (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction<PermissionCollection>() { // from class: java.security.ProtectionDomain.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public PermissionCollection run() {
                return Policy.getPolicyNoCheck().getPermissions(ProtectionDomain.this);
            }
        });
        Permissions permissions = new Permissions();
        int i2 = 8;
        ArrayList arrayList = new ArrayList(8);
        ArrayList arrayList2 = new ArrayList(32);
        if (this.permissions != null) {
            synchronized (this.permissions) {
                Enumeration<Permission> enumerationElements = this.permissions.elements();
                while (enumerationElements.hasMoreElements()) {
                    arrayList.add(enumerationElements.nextElement());
                }
            }
        }
        if (permissionCollection != null) {
            synchronized (permissionCollection) {
                Enumeration<Permission> enumerationElements2 = permissionCollection.elements();
                while (enumerationElements2.hasMoreElements()) {
                    arrayList2.add(enumerationElements2.nextElement());
                    i2++;
                }
            }
        }
        if (permissionCollection != null && this.permissions != null) {
            synchronized (this.permissions) {
                Enumeration<Permission> enumerationElements3 = this.permissions.elements();
                while (enumerationElements3.hasMoreElements()) {
                    Permission permissionNextElement = enumerationElements3.nextElement();
                    Class<?> cls = permissionNextElement.getClass();
                    String actions = permissionNextElement.getActions();
                    String name = permissionNextElement.getName();
                    int i3 = 0;
                    while (true) {
                        if (i3 < arrayList2.size()) {
                            Permission permission = (Permission) arrayList2.get(i3);
                            if (!cls.isInstance(permission) || !name.equals(permission.getName()) || !actions.equals(permission.getActions())) {
                                i3++;
                            } else {
                                arrayList2.remove(i3);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (permissionCollection != null) {
            for (int size = arrayList2.size() - 1; size >= 0; size--) {
                permissions.add((Permission) arrayList2.get(size));
            }
        }
        if (this.permissions != null) {
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                permissions.add((Permission) arrayList.get(size2));
            }
        }
        return permissions;
    }

    /* loaded from: rt.jar:java/security/ProtectionDomain$Key.class */
    final class Key {
        Key() {
        }
    }
}
