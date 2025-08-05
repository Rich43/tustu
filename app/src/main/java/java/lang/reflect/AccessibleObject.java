package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.Permission;
import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;

/* loaded from: rt.jar:java/lang/reflect/AccessibleObject.class */
public class AccessibleObject implements AnnotatedElement {
    boolean override;
    volatile Object securityCheckCache;
    private static final Permission ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");
    static final ReflectionFactory reflectionFactory = (ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());

    public static void setAccessible(AccessibleObject[] accessibleObjectArr, boolean z2) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(ACCESS_PERMISSION);
        }
        for (AccessibleObject accessibleObject : accessibleObjectArr) {
            setAccessible0(accessibleObject, z2);
        }
    }

    public void setAccessible(boolean z2) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(ACCESS_PERMISSION);
        }
        setAccessible0(this, z2);
    }

    private static void setAccessible0(AccessibleObject accessibleObject, boolean z2) throws SecurityException {
        if ((accessibleObject instanceof Constructor) && z2 && ((Constructor) accessibleObject).getDeclaringClass() == Class.class) {
            throw new SecurityException("Cannot make a java.lang.Class constructor accessible");
        }
        accessibleObject.override = z2;
    }

    public boolean isAccessible() {
        return this.override;
    }

    protected AccessibleObject() {
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        throw new AssertionError((Object) "All subclasses should override this method");
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return super.isAnnotationPresent(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        throw new AssertionError((Object) "All subclasses should override this method");
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        return (T) getAnnotation(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        return (T[]) getAnnotationsByType(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        throw new AssertionError((Object) "All subclasses should override this method");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x004d A[PHI: r13
  0x004d: PHI (r13v1 java.lang.Class<?>) = (r13v0 java.lang.Class<?>), (r13v0 java.lang.Class<?>), (r13v3 java.lang.Class<?>) binds: [B:6:0x0010, B:8:0x0018, B:10:0x0023] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void checkAccess(java.lang.Class<?> r8, java.lang.Class<?> r9, java.lang.Object r10, int r11) throws java.lang.IllegalAccessException {
        /*
            r7 = this;
            r0 = r8
            r1 = r9
            if (r0 != r1) goto L6
            return
        L6:
            r0 = r7
            java.lang.Object r0 = r0.securityCheckCache
            r12 = r0
            r0 = r9
            r13 = r0
            r0 = r10
            if (r0 == 0) goto L4d
            r0 = r11
            boolean r0 = java.lang.reflect.Modifier.isProtected(r0)
            if (r0 == 0) goto L4d
            r0 = r10
            java.lang.Class r0 = r0.getClass()
            r1 = r0
            r13 = r1
            r1 = r9
            if (r0 == r1) goto L4d
            r0 = r12
            boolean r0 = r0 instanceof java.lang.Class[]
            if (r0 == 0) goto L54
            r0 = r12
            java.lang.Class[] r0 = (java.lang.Class[]) r0
            java.lang.Class[] r0 = (java.lang.Class[]) r0
            r14 = r0
            r0 = r14
            r1 = 1
            r0 = r0[r1]
            r1 = r13
            if (r0 != r1) goto L4a
            r0 = r14
            r1 = 0
            r0 = r0[r1]
            r1 = r8
            if (r0 != r1) goto L4a
            return
        L4a:
            goto L54
        L4d:
            r0 = r12
            r1 = r8
            if (r0 != r1) goto L54
            return
        L54:
            r0 = r7
            r1 = r8
            r2 = r9
            r3 = r10
            r4 = r11
            r5 = r13
            r0.slowCheckMemberAccess(r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.AccessibleObject.checkAccess(java.lang.Class, java.lang.Class, java.lang.Object, int):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Class[]] */
    void slowCheckMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i2, Class<?> cls3) throws IllegalAccessException {
        Reflection.ensureMemberAccess(cls, cls2, obj, i2);
        this.securityCheckCache = cls3 == cls2 ? cls : new Class[]{cls, cls3};
    }
}
