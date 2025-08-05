package javax.xml.soap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/* loaded from: rt.jar:javax/xml/soap/FactoryFinder.class */
class FactoryFinder {
    FactoryFinder() {
    }

    private static Object newInstance(String className, ClassLoader classLoader) throws SOAPException {
        try {
            Class spiClass = safeLoadClass(className, classLoader);
            return spiClass.newInstance();
        } catch (ClassNotFoundException x2) {
            throw new SOAPException("Provider " + className + " not found", x2);
        } catch (Exception x3) {
            throw new SOAPException("Provider " + className + " could not be instantiated: " + ((Object) x3), x3);
        }
    }

    static Object find(String factoryId) throws SOAPException {
        return find(factoryId, null, false);
    }

    static Object find(String factoryId, String fallbackClassName) throws SOAPException {
        return find(factoryId, fallbackClassName, true);
    }

    static Object find(String factoryId, String defaultClassName, boolean tryFallback) throws SOAPException {
        InputStream is;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try {
                String systemProp = System.getProperty(factoryId);
                if (systemProp != null) {
                    return newInstance(systemProp, classLoader);
                }
            } catch (SecurityException e2) {
            }
            try {
                String javah = System.getProperty("java.home");
                String configFile = javah + File.separator + "lib" + File.separator + "jaxm.properties";
                File f2 = new File(configFile);
                if (f2.exists()) {
                    Properties props = new Properties();
                    props.load(new FileInputStream(f2));
                    return newInstance(props.getProperty(factoryId), classLoader);
                }
            } catch (Exception e3) {
            }
            String serviceId = "META-INF/services/" + factoryId;
            try {
                if (classLoader == null) {
                    is = ClassLoader.getSystemResourceAsStream(serviceId);
                } else {
                    is = classLoader.getResourceAsStream(serviceId);
                }
                if (is != null) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String factoryClassName = rd.readLine();
                    rd.close();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        return newInstance(factoryClassName, classLoader);
                    }
                }
            } catch (Exception e4) {
            }
            if (!tryFallback) {
                return null;
            }
            if (defaultClassName == null) {
                throw new SOAPException("Provider for " + factoryId + " cannot be found", null);
            }
            return newInstance(defaultClassName, classLoader);
        } catch (Exception x2) {
            throw new SOAPException(x2.toString(), x2);
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
            if (isDefaultImplementation(className)) {
                return Class.forName(className);
            }
            throw se;
        }
    }

    private static boolean isDefaultImplementation(String className) {
        return "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl".equals(className) || "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl".equals(className) || "com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory".equals(className) || "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl".equals(className);
    }
}
