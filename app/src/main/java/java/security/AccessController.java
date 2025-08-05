package java.security;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/AccessController.class */
public final class AccessController {
    @CallerSensitive
    public static native <T> T doPrivileged(PrivilegedAction<T> privilegedAction);

    @CallerSensitive
    public static native <T> T doPrivileged(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext);

    @CallerSensitive
    public static native <T> T doPrivileged(PrivilegedExceptionAction<T> privilegedExceptionAction) throws PrivilegedActionException;

    @CallerSensitive
    public static native <T> T doPrivileged(PrivilegedExceptionAction<T> privilegedExceptionAction, AccessControlContext accessControlContext) throws PrivilegedActionException;

    private static native AccessControlContext getStackAccessControlContext();

    static native AccessControlContext getInheritedAccessControlContext();

    private AccessController() {
    }

    @CallerSensitive
    public static <T> T doPrivilegedWithCombiner(PrivilegedAction<T> privilegedAction) {
        AccessControlContext stackAccessControlContext = getStackAccessControlContext();
        if (stackAccessControlContext == null) {
            return (T) doPrivileged(privilegedAction);
        }
        return (T) doPrivileged(privilegedAction, preserveCombiner(stackAccessControlContext.getAssignedCombiner(), Reflection.getCallerClass()));
    }

    @CallerSensitive
    public static <T> T doPrivileged(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext, Permission... permissionArr) {
        AccessControlContext context = getContext();
        if (permissionArr == null) {
            throw new NullPointerException("null permissions parameter");
        }
        return (T) doPrivileged(privilegedAction, createWrapper(accessControlContext == null ? null : accessControlContext.getCombiner(), Reflection.getCallerClass(), context, accessControlContext, permissionArr));
    }

    @CallerSensitive
    public static <T> T doPrivilegedWithCombiner(PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext, Permission... permissionArr) {
        AccessControlContext context = getContext();
        DomainCombiner combiner = context.getCombiner();
        if (combiner == null && accessControlContext != null) {
            combiner = accessControlContext.getCombiner();
        }
        if (permissionArr == null) {
            throw new NullPointerException("null permissions parameter");
        }
        return (T) doPrivileged(privilegedAction, createWrapper(combiner, Reflection.getCallerClass(), context, accessControlContext, permissionArr));
    }

    @CallerSensitive
    public static <T> T doPrivilegedWithCombiner(PrivilegedExceptionAction<T> privilegedExceptionAction) throws PrivilegedActionException {
        AccessControlContext stackAccessControlContext = getStackAccessControlContext();
        if (stackAccessControlContext == null) {
            return (T) doPrivileged(privilegedExceptionAction);
        }
        return (T) doPrivileged(privilegedExceptionAction, preserveCombiner(stackAccessControlContext.getAssignedCombiner(), Reflection.getCallerClass()));
    }

    private static AccessControlContext preserveCombiner(DomainCombiner domainCombiner, Class<?> cls) {
        return createWrapper(domainCombiner, cls, null, null, null);
    }

    private static AccessControlContext createWrapper(DomainCombiner domainCombiner, Class<?> cls, AccessControlContext accessControlContext, AccessControlContext accessControlContext2, Permission[] permissionArr) {
        ProtectionDomain callerPD = getCallerPD(cls);
        if (accessControlContext2 != null && !accessControlContext2.isAuthorized() && System.getSecurityManager() != null && !callerPD.impliesCreateAccessControlContext()) {
            return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)});
        }
        return new AccessControlContext(callerPD, domainCombiner, accessControlContext, accessControlContext2, permissionArr);
    }

    private static ProtectionDomain getCallerPD(final Class<?> cls) {
        return (ProtectionDomain) doPrivileged(new PrivilegedAction<ProtectionDomain>() { // from class: java.security.AccessController.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ProtectionDomain run() {
                return cls.getProtectionDomain();
            }
        });
    }

    @CallerSensitive
    public static <T> T doPrivileged(PrivilegedExceptionAction<T> privilegedExceptionAction, AccessControlContext accessControlContext, Permission... permissionArr) throws PrivilegedActionException {
        AccessControlContext context = getContext();
        if (permissionArr == null) {
            throw new NullPointerException("null permissions parameter");
        }
        return (T) doPrivileged(privilegedExceptionAction, createWrapper(accessControlContext == null ? null : accessControlContext.getCombiner(), Reflection.getCallerClass(), context, accessControlContext, permissionArr));
    }

    @CallerSensitive
    public static <T> T doPrivilegedWithCombiner(PrivilegedExceptionAction<T> privilegedExceptionAction, AccessControlContext accessControlContext, Permission... permissionArr) throws PrivilegedActionException {
        AccessControlContext context = getContext();
        DomainCombiner combiner = context.getCombiner();
        if (combiner == null && accessControlContext != null) {
            combiner = accessControlContext.getCombiner();
        }
        if (permissionArr == null) {
            throw new NullPointerException("null permissions parameter");
        }
        return (T) doPrivileged(privilegedExceptionAction, createWrapper(combiner, Reflection.getCallerClass(), context, accessControlContext, permissionArr));
    }

    public static AccessControlContext getContext() {
        AccessControlContext stackAccessControlContext = getStackAccessControlContext();
        if (stackAccessControlContext == null) {
            return new AccessControlContext((ProtectionDomain[]) null, true);
        }
        return stackAccessControlContext.optimize();
    }

    public static void checkPermission(Permission permission) throws AccessControlException {
        if (permission == null) {
            throw new NullPointerException("permission can't be null");
        }
        AccessControlContext stackAccessControlContext = getStackAccessControlContext();
        if (stackAccessControlContext == null) {
            Debug debug = AccessControlContext.getDebug();
            boolean z2 = false;
            if (debug != null) {
                z2 = (!Debug.isOn("codebase=")) & (!Debug.isOn("permission=") || Debug.isOn(new StringBuilder().append("permission=").append(permission.getClass().getCanonicalName()).toString()));
            }
            if (z2 && Debug.isOn("stack")) {
                Thread.dumpStack();
            }
            if (z2 && Debug.isOn("domain")) {
                debug.println("domain (context is null)");
            }
            if (z2) {
                debug.println("access allowed " + ((Object) permission));
                return;
            }
            return;
        }
        stackAccessControlContext.optimize().checkPermission(permission);
    }
}
