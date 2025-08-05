package javax.xml.ws.spi;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:javax/xml/ws/spi/FactoryFinder.class */
class FactoryFinder {
    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "com.sun.org.glassfish.hk2.osgiresourcelocator.ServiceLoader";

    FactoryFinder() {
    }

    private static Object newInstance(String className, ClassLoader classLoader) {
        try {
            Class spiClass = safeLoadClass(className, classLoader);
            return spiClass.newInstance();
        } catch (ClassNotFoundException x2) {
            throw new WebServiceException("Provider " + className + " not found", x2);
        } catch (Exception x3) {
            throw new WebServiceException("Provider " + className + " could not be instantiated: " + ((Object) x3), x3);
        }
    }

    static Object find(String factoryId, String fallbackClassName) {
        File f2;
        if (isOsgi()) {
            return lookupUsingOSGiServiceLoader(factoryId);
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String serviceId = "META-INF/services/" + factoryId;
            BufferedReader rd = null;
            try {
                InputStream is = classLoader == null ? ClassLoader.getSystemResourceAsStream(serviceId) : classLoader.getResourceAsStream(serviceId);
                if (is != null) {
                    rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String factoryClassName = rd.readLine();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        Object objNewInstance = newInstance(factoryClassName, classLoader);
                        close(rd);
                        return objNewInstance;
                    }
                }
                close(rd);
            } catch (Exception e2) {
                close(rd);
            } catch (Throwable th) {
                close(rd);
                throw th;
            }
            FileInputStream inStream = null;
            try {
                String javah = System.getProperty("java.home");
                String configFile = javah + File.separator + "lib" + File.separator + "jaxws.properties";
                f2 = new File(configFile);
            } catch (Exception e3) {
                close(inStream);
            } catch (Throwable th2) {
                close(inStream);
                throw th2;
            }
            if (f2.exists()) {
                Properties props = new Properties();
                inStream = new FileInputStream(f2);
                props.load(inStream);
                Object objNewInstance2 = newInstance(props.getProperty(factoryId), classLoader);
                close(inStream);
                return objNewInstance2;
            }
            close(null);
            try {
                String systemProp = System.getProperty(factoryId);
                if (systemProp != null) {
                    return newInstance(systemProp, classLoader);
                }
            } catch (SecurityException e4) {
            }
            if (fallbackClassName == null) {
                throw new WebServiceException("Provider for " + factoryId + " cannot be found", null);
            }
            return newInstance(fallbackClassName, classLoader);
        } catch (Exception x2) {
            throw new WebServiceException(x2.toString(), x2);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e2) {
            }
        }
    }

    private static Class safeLoadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        int i2;
        try {
            SecurityManager s2 = System.getSecurityManager();
            if (s2 != null && (i2 = className.lastIndexOf(46)) != -1) {
                s2.checkPackageAccess(className.substring(0, i2));
            }
            if (classLoader == null) {
                return Class.forName(className);
            }
            return classLoader.loadClass(className);
        } catch (SecurityException se) {
            if ("com.sun.xml.internal.ws.spi.ProviderImpl".equals(className)) {
                return Class.forName(className);
            }
            throw se;
        }
    }

    private static boolean isOsgi() {
        try {
            Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException e2) {
            return false;
        }
    }

    private static Object lookupUsingOSGiServiceLoader(String factoryId) {
        try {
            Class serviceClass = Class.forName(factoryId);
            Class[] args = {serviceClass};
            Class target = Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME);
            Method m2 = target.getMethod("lookupProviderInstances", Class.class);
            Iterator iter = ((Iterable) m2.invoke(null, args)).iterator();
            if (iter.hasNext()) {
                return iter.next();
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
