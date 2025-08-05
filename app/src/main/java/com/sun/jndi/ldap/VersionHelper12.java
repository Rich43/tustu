package com.sun.jndi.ldap;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:com/sun/jndi/ldap/VersionHelper12.class */
final class VersionHelper12 extends VersionHelper {
    private static final String TRUST_URL_CODEBASE_PROPERTY = "com.sun.jndi.ldap.object.trustURLCodebase";
    private static final boolean trustURLCodebase = "true".equalsIgnoreCase(getPrivilegedProperty(TRUST_URL_CODEBASE_PROPERTY, "false"));
    private static final String TRUST_SERIAL_DATA_PROPERTY = "com.sun.jndi.ldap.object.trustSerialData";
    private static final boolean trustSerialData = "true".equalsIgnoreCase(getPrivilegedProperty(TRUST_SERIAL_DATA_PROPERTY, "true"));

    private static String getPrivilegedProperty(String str, String str2) {
        PrivilegedAction privilegedAction = () -> {
            return System.getProperty(str, str2);
        };
        if (System.getSecurityManager() == null) {
            return (String) privilegedAction.run2();
        }
        return (String) AccessController.doPrivileged(privilegedAction);
    }

    VersionHelper12() {
    }

    public static boolean isSerialDataAllowed() {
        return trustSerialData;
    }

    @Override // com.sun.jndi.ldap.VersionHelper
    ClassLoader getURLClassLoader(String[] strArr) throws MalformedURLException {
        ClassLoader contextClassLoader = getContextClassLoader();
        if (strArr != null && trustURLCodebase) {
            return URLClassLoader.newInstance(getUrlArray(strArr), contextClassLoader);
        }
        return contextClassLoader;
    }

    @Override // com.sun.jndi.ldap.VersionHelper
    Class<?> loadClass(String str) throws ClassNotFoundException {
        return Class.forName(str, true, getContextClassLoader());
    }

    private ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.jndi.ldap.VersionHelper12.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    @Override // com.sun.jndi.ldap.VersionHelper
    Thread createThread(final Runnable runnable) {
        final AccessControlContext context = AccessController.getContext();
        return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() { // from class: com.sun.jndi.ldap.VersionHelper12.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Thread run2() {
                return SharedSecrets.getJavaLangAccess().newThreadWithAcc(runnable, context);
            }
        });
    }
}
