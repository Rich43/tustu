package javax.xml.stream;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/xml/stream/FactoryFinder.class */
class FactoryFinder {
    private static final String DEFAULT_PACKAGE = "com.sun.xml.internal.";
    private static boolean debug;
    private static final Properties cacheProps;
    private static volatile boolean firstTime;
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

    private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader) throws SecurityException, ClassNotFoundException {
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
            throw new FactoryConfigurationError("Provider " + className + " not found", x2);
        } catch (Exception x3) {
            throw new FactoryConfigurationError("Provider " + className + " could not be instantiated: " + ((Object) x3), x3);
        }
    }

    static <T> T find(Class<T> cls, String str) throws FactoryConfigurationError {
        return (T) find(cls, cls.getName(), null, str);
    }

    static <T> T find(Class<T> cls, String str, ClassLoader classLoader, String str2) throws FactoryConfigurationError {
        String property;
        dPrint("find factoryId =" + str);
        try {
            if (cls.getName().equals(str)) {
                property = ss.getSystemProperty(str);
            } else {
                property = System.getProperty(str);
            }
            if (property != null) {
                dPrint("found system property, value=" + property);
                return (T) newInstance(cls, property, classLoader, true);
            }
            String str3 = null;
            try {
                if (firstTime) {
                    synchronized (cacheProps) {
                        if (firstTime) {
                            str3 = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "stax.properties";
                            File file = new File(str3);
                            firstTime = false;
                            if (ss.doesFileExist(file)) {
                                dPrint("Read properties file " + ((Object) file));
                                cacheProps.load(ss.getFileInputStream(file));
                            } else {
                                str3 = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
                                File file2 = new File(str3);
                                if (ss.doesFileExist(file2)) {
                                    dPrint("Read properties file " + ((Object) file2));
                                    cacheProps.load(ss.getFileInputStream(file2));
                                }
                            }
                        }
                    }
                }
                String property2 = cacheProps.getProperty(str);
                if (property2 != null) {
                    dPrint("found in " + str3 + " value=" + property2);
                    return (T) newInstance(cls, property2, classLoader, true);
                }
            } catch (Exception e2) {
                if (debug) {
                    e2.printStackTrace();
                }
            }
            if (cls.getName().equals(str)) {
                T t2 = (T) findServiceProvider(cls, classLoader);
                if (t2 != null) {
                    return t2;
                }
            } else if (!$assertionsDisabled && str2 != null) {
                throw new AssertionError();
            }
            if (str2 == null) {
                throw new FactoryConfigurationError("Provider for " + str + " cannot be found", (Exception) null);
            }
            dPrint("loaded from fallback value: " + str2);
            return (T) newInstance(cls, str2, classLoader, true);
        } catch (SecurityException e3) {
            throw new FactoryConfigurationError("Failed to read factoryId '" + str + PdfOps.SINGLE_QUOTE_TOKEN, e3);
        }
    }

    private static <T> T findServiceProvider(final Class<T> cls, final ClassLoader classLoader) {
        try {
            return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: javax.xml.stream.FactoryFinder.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public T run2() {
                    ServiceLoader<T> serviceLoader;
                    if (classLoader == null) {
                        serviceLoader = ServiceLoader.load(cls);
                    } else {
                        serviceLoader = ServiceLoader.load(cls, classLoader);
                    }
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
