package com.sun.corba.se.impl.util;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import java.net.MalformedURLException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/JDKBridge.class */
public class JDKBridge {
    private static final String LOCAL_CODEBASE_KEY = "java.rmi.server.codebase";
    private static final String USE_CODEBASE_ONLY_KEY = "java.rmi.server.useCodebaseOnly";
    private static String localCodebase = null;
    private static boolean useCodebaseOnly;

    public static String getLocalCodebase() {
        return localCodebase;
    }

    public static boolean useCodebaseOnly() {
        return useCodebaseOnly;
    }

    public static Class loadClass(String str, String str2, ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader == null) {
            return loadClassM(str, str2, useCodebaseOnly);
        }
        try {
            return loadClassM(str, str2, useCodebaseOnly);
        } catch (ClassNotFoundException e2) {
            return classLoader.loadClass(str);
        }
    }

    public static Class loadClass(String str, String str2) throws ClassNotFoundException {
        return loadClass(str, str2, null);
    }

    public static Class loadClass(String str) throws ClassNotFoundException {
        return loadClass(str, null, null);
    }

    static {
        setCodebaseProperties();
    }

    public static final void main(String[] strArr) {
        System.out.println("1.2 VM");
    }

    public static synchronized void setCodebaseProperties() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(LOCAL_CODEBASE_KEY));
        if (str != null && str.trim().length() > 0) {
            localCodebase = str;
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction(USE_CODEBASE_ONLY_KEY));
        if (str2 != null && str2.trim().length() > 0) {
            useCodebaseOnly = Boolean.valueOf(str2).booleanValue();
        }
    }

    public static synchronized void setLocalCodebase(String str) {
        localCodebase = str;
    }

    private static Class loadClassM(String str, String str2, boolean z2) throws ClassNotFoundException {
        try {
            return JDKClassLoader.loadClass(null, str);
        } catch (ClassNotFoundException e2) {
            try {
                if (!z2 && str2 != null) {
                    return RMIClassLoader.loadClass(str2, str);
                }
                return RMIClassLoader.loadClass(str);
            } catch (MalformedURLException e3) {
                throw new ClassNotFoundException(str + ": " + e3.toString());
            }
        }
    }
}
