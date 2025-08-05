package java.net;

import java.security.AccessControlContext;

/* compiled from: URLClassLoader.java */
/* loaded from: rt.jar:java/net/FactoryURLClassLoader.class */
final class FactoryURLClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    FactoryURLClassLoader(URL[] urlArr, ClassLoader classLoader, AccessControlContext accessControlContext) {
        super(urlArr, classLoader, accessControlContext);
    }

    FactoryURLClassLoader(URL[] urlArr, AccessControlContext accessControlContext) {
        super(urlArr, accessControlContext);
    }

    @Override // java.lang.ClassLoader
    public final Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
        int iLastIndexOf;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && (iLastIndexOf = str.lastIndexOf(46)) != -1) {
            securityManager.checkPackageAccess(str.substring(0, iLastIndexOf));
        }
        return super.loadClass(str, z2);
    }
}
