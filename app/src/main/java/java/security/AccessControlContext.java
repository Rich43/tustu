package java.security;

import java.util.ArrayList;
import sun.security.util.Debug;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/security/AccessControlContext.class */
public final class AccessControlContext {
    private ProtectionDomain[] context;
    private boolean isPrivileged;
    private boolean isAuthorized;
    private AccessControlContext privilegedContext;
    private DomainCombiner combiner;
    private Permission[] permissions;
    private AccessControlContext parent;
    private boolean isWrapped;
    private boolean isLimited;
    private ProtectionDomain[] limitedContext;
    private static boolean debugInit = false;
    private static Debug debug = null;

    static Debug getDebug() {
        if (debugInit) {
            return debug;
        }
        if (Policy.isSet()) {
            debug = Debug.getInstance("access");
            debugInit = true;
        }
        return debug;
    }

    public AccessControlContext(ProtectionDomain[] protectionDomainArr) {
        this.isAuthorized = false;
        this.combiner = null;
        if (protectionDomainArr.length == 0) {
            this.context = null;
            return;
        }
        if (protectionDomainArr.length == 1) {
            if (protectionDomainArr[0] != null) {
                this.context = (ProtectionDomain[]) protectionDomainArr.clone();
                return;
            } else {
                this.context = null;
                return;
            }
        }
        ArrayList arrayList = new ArrayList(protectionDomainArr.length);
        for (int i2 = 0; i2 < protectionDomainArr.length; i2++) {
            if (protectionDomainArr[i2] != null && !arrayList.contains(protectionDomainArr[i2])) {
                arrayList.add(protectionDomainArr[i2]);
            }
        }
        if (!arrayList.isEmpty()) {
            this.context = new ProtectionDomain[arrayList.size()];
            this.context = (ProtectionDomain[]) arrayList.toArray(this.context);
        }
    }

    public AccessControlContext(AccessControlContext accessControlContext, DomainCombiner domainCombiner) {
        this(accessControlContext, domainCombiner, false);
    }

    AccessControlContext(AccessControlContext accessControlContext, DomainCombiner domainCombiner, boolean z2) {
        this.isAuthorized = false;
        this.combiner = null;
        if (!z2) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.CREATE_ACC_PERMISSION);
                this.isAuthorized = true;
            }
        } else {
            this.isAuthorized = true;
        }
        this.context = accessControlContext.context;
        this.combiner = domainCombiner;
    }

    AccessControlContext(ProtectionDomain protectionDomain, DomainCombiner domainCombiner, AccessControlContext accessControlContext, AccessControlContext accessControlContext2, Permission[] permissionArr) {
        this.isAuthorized = false;
        this.combiner = null;
        ProtectionDomain[] protectionDomainArr = protectionDomain != null ? new ProtectionDomain[]{protectionDomain} : null;
        if (accessControlContext2 != null) {
            if (domainCombiner != null) {
                this.context = domainCombiner.combine(protectionDomainArr, accessControlContext2.context);
            } else {
                this.context = combine(protectionDomainArr, accessControlContext2.context);
            }
        } else if (domainCombiner != null) {
            this.context = domainCombiner.combine(protectionDomainArr, null);
        } else {
            this.context = combine(protectionDomainArr, null);
        }
        this.combiner = domainCombiner;
        Permission[] permissionArr2 = null;
        if (permissionArr != null) {
            permissionArr2 = new Permission[permissionArr.length];
            for (int i2 = 0; i2 < permissionArr.length; i2++) {
                if (permissionArr[i2] == null) {
                    throw new NullPointerException("permission can't be null");
                }
                if (permissionArr[i2].getClass() == AllPermission.class) {
                    accessControlContext = null;
                }
                permissionArr2[i2] = permissionArr[i2];
            }
        }
        if (accessControlContext != null) {
            this.limitedContext = combine(accessControlContext.context, accessControlContext.limitedContext);
            this.isLimited = true;
            this.isWrapped = true;
            this.permissions = permissionArr2;
            this.parent = accessControlContext;
            this.privilegedContext = accessControlContext2;
        }
        this.isAuthorized = true;
    }

    AccessControlContext(ProtectionDomain[] protectionDomainArr, boolean z2) {
        this.isAuthorized = false;
        this.combiner = null;
        this.context = protectionDomainArr;
        this.isPrivileged = z2;
        this.isAuthorized = true;
    }

    AccessControlContext(ProtectionDomain[] protectionDomainArr, AccessControlContext accessControlContext) {
        this.isAuthorized = false;
        this.combiner = null;
        this.context = protectionDomainArr;
        this.privilegedContext = accessControlContext;
        this.isPrivileged = true;
    }

    ProtectionDomain[] getContext() {
        return this.context;
    }

    boolean isPrivileged() {
        return this.isPrivileged;
    }

    DomainCombiner getAssignedCombiner() {
        AccessControlContext inheritedAccessControlContext;
        if (this.isPrivileged) {
            inheritedAccessControlContext = this.privilegedContext;
        } else {
            inheritedAccessControlContext = AccessController.getInheritedAccessControlContext();
        }
        if (inheritedAccessControlContext != null) {
            return inheritedAccessControlContext.combiner;
        }
        return null;
    }

    public DomainCombiner getDomainCombiner() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_COMBINER_PERMISSION);
        }
        return getCombiner();
    }

    DomainCombiner getCombiner() {
        return this.combiner;
    }

    boolean isAuthorized() {
        return this.isAuthorized;
    }

    public void checkPermission(Permission permission) throws AccessControlException {
        boolean z2 = false;
        if (permission == null) {
            throw new NullPointerException("permission can't be null");
        }
        if (getDebug() != null) {
            boolean z3 = !Debug.isOn("codebase=");
            if (!z3) {
                int i2 = 0;
                while (true) {
                    if (this.context == null || i2 >= this.context.length) {
                        break;
                    }
                    if (this.context[i2].getCodeSource() == null || this.context[i2].getCodeSource().getLocation() == null || !Debug.isOn("codebase=" + this.context[i2].getCodeSource().getLocation().toString())) {
                        i2++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
            }
            z2 = z3 & (!Debug.isOn("permission=") || Debug.isOn(new StringBuilder().append("permission=").append(permission.getClass().getCanonicalName()).toString()));
            if (z2 && Debug.isOn("stack")) {
                Thread.dumpStack();
            }
            if (z2 && Debug.isOn("domain")) {
                if (this.context == null) {
                    debug.println("domain (context is null)");
                } else {
                    for (int i3 = 0; i3 < this.context.length; i3++) {
                        debug.println("domain " + i3 + " " + ((Object) this.context[i3]));
                    }
                }
            }
        }
        if (this.context == null) {
            checkPermission2(permission);
            return;
        }
        for (int i4 = 0; i4 < this.context.length; i4++) {
            if (this.context[i4] != null && !this.context[i4].implies(permission)) {
                if (z2) {
                    debug.println("access denied " + ((Object) permission));
                }
                if (Debug.isOn("failure") && debug != null) {
                    if (!z2) {
                        debug.println("access denied " + ((Object) permission));
                    }
                    Thread.dumpStack();
                    final ProtectionDomain protectionDomain = this.context[i4];
                    final Debug debug2 = debug;
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.security.AccessControlContext.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            debug2.println("domain that failed " + ((Object) protectionDomain));
                            return null;
                        }
                    });
                }
                throw new AccessControlException("access denied " + ((Object) permission), permission);
            }
        }
        if (z2) {
            debug.println("access allowed " + ((Object) permission));
        }
        checkPermission2(permission);
    }

    private void checkPermission2(Permission permission) throws AccessControlException {
        if (!this.isLimited) {
            return;
        }
        if (this.privilegedContext != null) {
            this.privilegedContext.checkPermission2(permission);
        }
        if (this.isWrapped) {
            return;
        }
        if (this.permissions != null) {
            Class<?> cls = permission.getClass();
            for (int i2 = 0; i2 < this.permissions.length; i2++) {
                Permission permission2 = this.permissions[i2];
                if (permission2.getClass().equals(cls) && permission2.implies(permission)) {
                    return;
                }
            }
        }
        if (this.parent != null) {
            if (this.permissions == null) {
                this.parent.checkPermission2(permission);
            } else {
                this.parent.checkPermission(permission);
            }
        }
    }

    AccessControlContext optimize() {
        AccessControlContext inheritedAccessControlContext;
        ProtectionDomain[] protectionDomainArrCombine;
        DomainCombiner domainCombiner = null;
        AccessControlContext accessControlContext = null;
        Permission[] permissionArr = null;
        if (this.isPrivileged) {
            inheritedAccessControlContext = this.privilegedContext;
            if (inheritedAccessControlContext != null && inheritedAccessControlContext.isWrapped) {
                permissionArr = inheritedAccessControlContext.permissions;
                accessControlContext = inheritedAccessControlContext.parent;
            }
        } else {
            inheritedAccessControlContext = AccessController.getInheritedAccessControlContext();
            if (inheritedAccessControlContext != null && inheritedAccessControlContext.isLimited) {
                accessControlContext = inheritedAccessControlContext;
            }
        }
        boolean z2 = this.context == null;
        boolean z3 = inheritedAccessControlContext == null || inheritedAccessControlContext.context == null;
        ProtectionDomain[] protectionDomainArr = z3 ? null : inheritedAccessControlContext.context;
        boolean z4 = (inheritedAccessControlContext == null || !inheritedAccessControlContext.isWrapped) && accessControlContext == null;
        if (inheritedAccessControlContext != null && inheritedAccessControlContext.combiner != null) {
            if (getDebug() != null) {
                debug.println("AccessControlContext invoking the Combiner");
            }
            domainCombiner = inheritedAccessControlContext.combiner;
            protectionDomainArrCombine = domainCombiner.combine(this.context, protectionDomainArr);
        } else {
            if (z2) {
                if (z3) {
                    calculateFields(inheritedAccessControlContext, accessControlContext, permissionArr);
                    return this;
                }
                if (z4) {
                    return inheritedAccessControlContext;
                }
            } else if (protectionDomainArr != null && z4 && this.context.length == 1 && this.context[0] == protectionDomainArr[0]) {
                return inheritedAccessControlContext;
            }
            protectionDomainArrCombine = combine(this.context, protectionDomainArr);
            if (z4 && !z3 && protectionDomainArrCombine == protectionDomainArr) {
                return inheritedAccessControlContext;
            }
            if (z3 && protectionDomainArrCombine == this.context) {
                calculateFields(inheritedAccessControlContext, accessControlContext, permissionArr);
                return this;
            }
        }
        this.context = protectionDomainArrCombine;
        this.combiner = domainCombiner;
        this.isPrivileged = false;
        calculateFields(inheritedAccessControlContext, accessControlContext, permissionArr);
        return this;
    }

    private static ProtectionDomain[] combine(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        boolean z2 = protectionDomainArr == null;
        boolean z3 = protectionDomainArr2 == null;
        int length = z2 ? 0 : protectionDomainArr.length;
        if (z3 && length <= 2) {
            return protectionDomainArr;
        }
        int length2 = z3 ? 0 : protectionDomainArr2.length;
        ProtectionDomain[] protectionDomainArr3 = new ProtectionDomain[length + length2];
        if (!z3) {
            System.arraycopy(protectionDomainArr2, 0, protectionDomainArr3, 0, length2);
        }
        for (int i2 = 0; i2 < length; i2++) {
            ProtectionDomain protectionDomain = protectionDomainArr[i2];
            if (protectionDomain != null) {
                int i3 = 0;
                while (true) {
                    if (i3 < length2) {
                        if (protectionDomain == protectionDomainArr3[i3]) {
                            break;
                        }
                        i3++;
                    } else {
                        int i4 = length2;
                        length2++;
                        protectionDomainArr3[i4] = protectionDomain;
                        break;
                    }
                }
            }
        }
        if (length2 != protectionDomainArr3.length) {
            if (!z3 && length2 == protectionDomainArr2.length) {
                return protectionDomainArr2;
            }
            if (z3 && length2 == length) {
                return protectionDomainArr;
            }
            ProtectionDomain[] protectionDomainArr4 = new ProtectionDomain[length2];
            System.arraycopy(protectionDomainArr3, 0, protectionDomainArr4, 0, length2);
            protectionDomainArr3 = protectionDomainArr4;
        }
        return protectionDomainArr3;
    }

    private void calculateFields(AccessControlContext accessControlContext, AccessControlContext accessControlContext2, Permission[] permissionArr) {
        ProtectionDomain[] protectionDomainArrCombine = combine(accessControlContext2 != null ? accessControlContext2.limitedContext : null, accessControlContext != null ? accessControlContext.limitedContext : null);
        if (protectionDomainArrCombine != null) {
            if (this.context == null || !containsAllPDs(protectionDomainArrCombine, this.context)) {
                this.limitedContext = protectionDomainArrCombine;
                this.permissions = permissionArr;
                this.parent = accessControlContext2;
                this.isLimited = true;
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AccessControlContext)) {
            return false;
        }
        AccessControlContext accessControlContext = (AccessControlContext) obj;
        if (!equalContext(accessControlContext) || !equalLimitedContext(accessControlContext)) {
            return false;
        }
        return true;
    }

    private boolean equalContext(AccessControlContext accessControlContext) {
        if (!equalPDs(this.context, accessControlContext.context)) {
            return false;
        }
        if (this.combiner == null && accessControlContext.combiner != null) {
            return false;
        }
        if (this.combiner != null && !this.combiner.equals(accessControlContext.combiner)) {
            return false;
        }
        return true;
    }

    private boolean equalPDs(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        if (protectionDomainArr == null) {
            return protectionDomainArr2 == null;
        }
        if (protectionDomainArr2 == null || !containsAllPDs(protectionDomainArr, protectionDomainArr2) || !containsAllPDs(protectionDomainArr2, protectionDomainArr)) {
            return false;
        }
        return true;
    }

    private boolean equalLimitedContext(AccessControlContext accessControlContext) {
        if (accessControlContext == null) {
            return false;
        }
        if (!this.isLimited && !accessControlContext.isLimited) {
            return true;
        }
        if (!this.isLimited || !accessControlContext.isLimited) {
            return false;
        }
        if (this.isWrapped && !accessControlContext.isWrapped) {
            return false;
        }
        if (!this.isWrapped && accessControlContext.isWrapped) {
            return false;
        }
        if (this.permissions == null && accessControlContext.permissions != null) {
            return false;
        }
        if ((this.permissions != null && accessControlContext.permissions == null) || !containsAllLimits(accessControlContext) || !accessControlContext.containsAllLimits(this)) {
            return false;
        }
        AccessControlContext nextPC = getNextPC(this);
        AccessControlContext nextPC2 = getNextPC(accessControlContext);
        if (nextPC == null && nextPC2 != null && nextPC2.isLimited) {
            return false;
        }
        if (nextPC != null && !nextPC.equalLimitedContext(nextPC2)) {
            return false;
        }
        if (this.parent == null && accessControlContext.parent != null) {
            return false;
        }
        if (this.parent != null && !this.parent.equals(accessControlContext.parent)) {
            return false;
        }
        return true;
    }

    private static AccessControlContext getNextPC(AccessControlContext accessControlContext) {
        while (accessControlContext != null && accessControlContext.privilegedContext != null) {
            accessControlContext = accessControlContext.privilegedContext;
            if (!accessControlContext.isWrapped) {
                return accessControlContext;
            }
        }
        return null;
    }

    private static boolean containsAllPDs(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        boolean z2 = false;
        for (ProtectionDomain protectionDomain : protectionDomainArr) {
            z2 = false;
            if (protectionDomain == null) {
                for (int i2 = 0; i2 < protectionDomainArr2.length && !z2; i2++) {
                    z2 = protectionDomainArr2[i2] == null;
                }
            } else {
                Class<?> cls = protectionDomain.getClass();
                for (int i3 = 0; i3 < protectionDomainArr2.length && !z2; i3++) {
                    ProtectionDomain protectionDomain2 = protectionDomainArr2[i3];
                    z2 = protectionDomain2 != null && cls == protectionDomain2.getClass() && protectionDomain.equals(protectionDomain2);
                }
            }
            if (!z2) {
                return false;
            }
        }
        return z2;
    }

    private boolean containsAllLimits(AccessControlContext accessControlContext) {
        boolean z2 = false;
        if (this.permissions == null && accessControlContext.permissions == null) {
            return true;
        }
        for (int i2 = 0; i2 < this.permissions.length; i2++) {
            Permission permission = this.permissions[i2];
            Class<?> cls = permission.getClass();
            z2 = false;
            for (int i3 = 0; i3 < accessControlContext.permissions.length && !z2; i3++) {
                Permission permission2 = accessControlContext.permissions[i3];
                z2 = cls.equals(permission2.getClass()) && permission.equals(permission2);
            }
            if (!z2) {
                return false;
            }
        }
        return z2;
    }

    public int hashCode() {
        int iHashCode = 0;
        if (this.context == null) {
            return 0;
        }
        for (int i2 = 0; i2 < this.context.length; i2++) {
            if (this.context[i2] != null) {
                iHashCode ^= this.context[i2].hashCode();
            }
        }
        return iHashCode;
    }
}
