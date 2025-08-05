package java.lang;

import java.security.PrivilegedExceptionAction;

/* compiled from: ClassLoader.java */
/* loaded from: rt.jar:java/lang/SystemClassLoaderAction.class */
class SystemClassLoaderAction implements PrivilegedExceptionAction<ClassLoader> {
    private ClassLoader parent;

    SystemClassLoaderAction(ClassLoader classLoader) {
        this.parent = classLoader;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedExceptionAction
    public ClassLoader run() throws Exception {
        String property = System.getProperty("java.system.class.loader");
        if (property == null) {
            return this.parent;
        }
        ClassLoader classLoader = (ClassLoader) Class.forName(property, true, this.parent).getDeclaredConstructor(ClassLoader.class).newInstance(this.parent);
        Thread.currentThread().setContextClassLoader(classLoader);
        return classLoader;
    }
}
