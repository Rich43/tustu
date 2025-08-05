package com.sun.org.apache.xerces.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/utils/ObjectFactory.class */
public final class ObjectFactory {
    private static final String JAXP_INTERNAL = "com.sun.org.apache";
    private static final String STAX_INTERNAL = "com.sun.xml.internal";
    private static final boolean DEBUG = isDebugEnabled();

    private static boolean isDebugEnabled() {
        try {
            String val = SecuritySupport.getSystemProperty("xerces.debug");
            if (val != null) {
                if (!"false".equals(val)) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private static void debugPrintln(String msg) {
        if (DEBUG) {
            System.err.println("XERCES: " + msg);
        }
    }

    public static ClassLoader findClassLoader() throws ConfigurationError {
        if (System.getSecurityManager() != null) {
            return null;
        }
        ClassLoader context = SecuritySupport.getContextClassLoader();
        ClassLoader system = SecuritySupport.getSystemClassLoader();
        ClassLoader parentClassLoader = system;
        while (true) {
            ClassLoader chain = parentClassLoader;
            if (context == chain) {
                ClassLoader current = ObjectFactory.class.getClassLoader();
                ClassLoader parentClassLoader2 = system;
                while (true) {
                    ClassLoader chain2 = parentClassLoader2;
                    if (current == chain2) {
                        return system;
                    }
                    if (chain2 != null) {
                        parentClassLoader2 = SecuritySupport.getParentClassLoader(chain2);
                    } else {
                        return current;
                    }
                }
            } else if (chain != null) {
                parentClassLoader = SecuritySupport.getParentClassLoader(chain);
            } else {
                return context;
            }
        }
    }

    public static Object newInstance(String className, boolean doFallback) throws ConfigurationError {
        if (System.getSecurityManager() != null) {
            return newInstance(className, null, doFallback);
        }
        return newInstance(className, findClassLoader(), doFallback);
    }

    public static Object newInstance(String className, ClassLoader cl, boolean doFallback) throws ConfigurationError {
        try {
            Class providerClass = findProviderClass(className, cl, doFallback);
            Object instance = providerClass.newInstance();
            if (DEBUG) {
                debugPrintln("created new instance of " + ((Object) providerClass) + " using ClassLoader: " + ((Object) cl));
            }
            return instance;
        } catch (ClassNotFoundException x2) {
            throw new ConfigurationError("Provider " + className + " not found", x2);
        } catch (Exception x3) {
            throw new ConfigurationError("Provider " + className + " could not be instantiated: " + ((Object) x3), x3);
        }
    }

    public static Class findProviderClass(String className, boolean doFallback) throws ConfigurationError, ClassNotFoundException {
        return findProviderClass(className, findClassLoader(), doFallback);
    }

    public static Class findProviderClass(String className, ClassLoader cl, boolean doFallback) throws ConfigurationError, ClassNotFoundException {
        Class providerClass;
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            if (className.startsWith(JAXP_INTERNAL) || className.startsWith(STAX_INTERNAL)) {
                cl = null;
            } else {
                int lastDot = className.lastIndexOf(".");
                String packageName = className;
                if (lastDot != -1) {
                    packageName = className.substring(0, lastDot);
                }
                security.checkPackageAccess(packageName);
            }
        }
        if (cl == null) {
            providerClass = Class.forName(className, false, ObjectFactory.class.getClassLoader());
        } else {
            try {
                providerClass = cl.loadClass(className);
            } catch (ClassNotFoundException x2) {
                if (doFallback) {
                    ClassLoader current = ObjectFactory.class.getClassLoader();
                    if (current == null) {
                        providerClass = Class.forName(className);
                    } else if (cl != current) {
                        providerClass = current.loadClass(className);
                    } else {
                        throw x2;
                    }
                } else {
                    throw x2;
                }
            }
        }
        return providerClass;
    }
}
