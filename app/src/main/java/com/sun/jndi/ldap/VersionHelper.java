package com.sun.jndi.ldap;

import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: rt.jar:com/sun/jndi/ldap/VersionHelper.class */
abstract class VersionHelper {
    private static VersionHelper helper;

    abstract ClassLoader getURLClassLoader(String[] strArr) throws MalformedURLException;

    abstract Class<?> loadClass(String str) throws ClassNotFoundException;

    abstract Thread createThread(Runnable runnable);

    static {
        helper = null;
        try {
            Class.forName("java.net.URLClassLoader");
            Class.forName("java.security.PrivilegedAction");
            helper = (VersionHelper) Class.forName("com.sun.jndi.ldap.VersionHelper12").newInstance();
        } catch (Exception e2) {
        }
        if (helper == null) {
            try {
                helper = (VersionHelper) Class.forName("com.sun.jndi.ldap.VersionHelper11").newInstance();
            } catch (Exception e3) {
            }
        }
    }

    VersionHelper() {
    }

    static VersionHelper getVersionHelper() {
        return helper;
    }

    protected static URL[] getUrlArray(String[] strArr) throws MalformedURLException {
        URL[] urlArr = new URL[strArr.length];
        for (int i2 = 0; i2 < urlArr.length; i2++) {
            urlArr[i2] = new URL(strArr[i2]);
        }
        return urlArr;
    }
}
