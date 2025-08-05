package javax.xml.parsers;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/* loaded from: rt.jar:javax/xml/parsers/FactoryFinder.class */
class FactoryFinder {
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";
    private static boolean debug;
    private static final Properties cacheProps;
    static volatile boolean firstTime;
    private static final SecuritySupport ss;
    static final /* synthetic */ boolean $assertionsDisabled;

    FactoryFinder() {
    }

    static {
        $assertionsDisabled = !FactoryFinder.class.desiredAssertionStatus();
        debug = false;
        cacheProps = new Properties();
        firstTime = true;
        ss = new SecuritySupport();
        try {
            String val = ss.getSystemProperty("jaxp.debug");
            debug = (val == null || "false".equals(val)) ? false : true;
        } catch (SecurityException e2) {
            debug = false;
        }
    }

    private static void dPrint(String msg) {
        if (debug) {
            System.err.println("JAXP: " + msg);
        }
    }

    private static Class<?> getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader) throws SecurityException, ClassNotFoundException {
        try {
            if (cl == null) {
                if (useBSClsLoader) {
                    return Class.forName(className, false, FactoryFinder.class.getClassLoader());
                }
                ClassLoader cl2 = ss.getContextClassLoader();
                if (cl2 == null) {
                    throw new ClassNotFoundException();
                }
                return Class.forName(className, false, cl2);
            }
            return Class.forName(className, false, cl);
        } catch (ClassNotFoundException e1) {
            if (doFallback) {
                return Class.forName(className, false, FactoryFinder.class.getClassLoader());
            }
            throw e1;
        }
    }

    static <T> T newInstance(Class<T> cls, String str, ClassLoader classLoader, boolean z2) throws FactoryConfigurationError {
        return (T) newInstance(cls, str, classLoader, z2, false);
    }

    static <T> T newInstance(Class<T> type, String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader) throws FactoryConfigurationError {
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        if (System.getSecurityManager() != null && className != null && className.startsWith(DEFAULT_PACKAGE)) {
            cl = null;
            useBSClsLoader = true;
        }
        try {
            Class<?> providerClass = getProviderClass(className, cl, doFallback, useBSClsLoader);
            if (!type.isAssignableFrom(providerClass)) {
                throw new ClassCastException(className + " cannot be cast to " + type.getName());
            }
            Object instance = providerClass.newInstance();
            if (debug) {
                dPrint("created new instance of " + ((Object) providerClass) + " using ClassLoader: " + ((Object) cl));
            }
            return type.cast(instance);
        } catch (ClassNotFoundException x2) {
            throw new FactoryConfigurationError(x2, "Provider " + className + " not found");
        } catch (Exception x3) {
            throw new FactoryConfigurationError(x3, "Provider " + className + " could not be instantiated: " + ((Object) x3));
        }
    }

    static <T> T find(Class<T> cls, String str) throws FactoryConfigurationError {
        String name = cls.getName();
        dPrint("find factoryId =" + name);
        try {
            String systemProperty = ss.getSystemProperty(name);
            if (systemProperty != null) {
                dPrint("found system property, value=" + systemProperty);
                return (T) newInstance(cls, systemProperty, null, true);
            }
        } catch (SecurityException e2) {
            if (debug) {
                e2.printStackTrace();
            }
        }
        try {
            if (firstTime) {
                synchronized (cacheProps) {
                    if (firstTime) {
                        File file = new File(ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties");
                        firstTime = false;
                        if (ss.doesFileExist(file)) {
                            dPrint("Read properties file " + ((Object) file));
                            cacheProps.load(ss.getFileInputStream(file));
                        }
                    }
                }
            }
            String property = cacheProps.getProperty(name);
            if (property != null) {
                dPrint("found in $java.home/jaxp.properties, value=" + property);
                return (T) newInstance(cls, property, null, true);
            }
        } catch (Exception e3) {
            if (debug) {
                e3.printStackTrace();
            }
        }
        T t2 = (T) findServiceProvider(cls);
        if (t2 != null) {
            return t2;
        }
        if (str == null) {
            throw new FactoryConfigurationError("Provider for " + name + " cannot be found");
        }
        dPrint("loaded from fallback value: " + str);
        return (T) newInstance(cls, str, null, true);
    }

    private static <T> T findServiceProvider(final Class<T> cls) {
        try {
            return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: javax.xml.parsers.FactoryFinder.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public T run2() {
                    ServiceLoader<T> serviceLoader = ServiceLoader.load(cls);
                    Iterator<T> iterator = serviceLoader.iterator();
                    if (iterator.hasNext()) {
                        return iterator.next();
                    }
                    return null;
                }
            });
        } catch (ServiceConfigurationError e2) {
            RuntimeException runtimeException = new RuntimeException("Provider for " + ((Object) cls) + " cannot be created", e2);
            throw new FactoryConfigurationError(runtimeException, runtimeException.getMessage());
        }
    }
}
