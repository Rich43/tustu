package com.sun.net.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.Iterator;
import sun.security.jca.Providers;

/* loaded from: rt.jar:com/sun/net/ssl/SSLSecurity.class */
final class SSLSecurity {
    private SSLSecurity() {
    }

    private static Provider.Service getService(String str, String str2) {
        Iterator<Provider> it = Providers.getProviderList().providers().iterator();
        while (it.hasNext()) {
            Provider.Service service = it.next().getService(str, str2);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    private static Object[] getImpl1(String str, String str2, Provider.Service service) throws NoSuchAlgorithmException {
        Class<?> clsLoadClass;
        Provider provider = service.getProvider();
        String className = service.getClassName();
        try {
            ClassLoader classLoader = provider.getClass().getClassLoader();
            if (classLoader == null) {
                clsLoadClass = Class.forName(className);
            } else {
                clsLoadClass = classLoader.loadClass(className);
            }
            try {
                Object objNewInstance = null;
                Class<?> cls = Class.forName("javax.net.ssl." + str2 + "Spi");
                if (cls != null && checkSuperclass(clsLoadClass, cls)) {
                    if (str2.equals("SSLContext")) {
                        objNewInstance = new SSLContextSpiWrapper(str, provider);
                    } else if (str2.equals("TrustManagerFactory")) {
                        objNewInstance = new TrustManagerFactorySpiWrapper(str, provider);
                    } else if (str2.equals("KeyManagerFactory")) {
                        objNewInstance = new KeyManagerFactorySpiWrapper(str, provider);
                    } else {
                        throw new IllegalStateException("Class " + clsLoadClass.getName() + " unknown engineType wrapper:" + str2);
                    }
                } else {
                    Class<?> cls2 = Class.forName("com.sun.net.ssl." + str2 + "Spi");
                    if (cls2 != null && checkSuperclass(clsLoadClass, cls2)) {
                        objNewInstance = service.newInstance(null);
                    }
                }
                if (objNewInstance != null) {
                    return new Object[]{objNewInstance, provider};
                }
                throw new NoSuchAlgorithmException("Couldn't locate correct object or wrapper: " + str2 + " " + str);
            } catch (ClassNotFoundException e2) {
                IllegalStateException illegalStateException = new IllegalStateException("Engine Class Not Found for " + str2);
                illegalStateException.initCause(e2);
                throw illegalStateException;
            }
        } catch (ClassNotFoundException e3) {
            throw new NoSuchAlgorithmException("Class " + className + " configured for " + str2 + " not found: " + e3.getMessage());
        } catch (SecurityException e4) {
            throw new NoSuchAlgorithmException("Class " + className + " configured for " + str2 + " cannot be accessed: " + e4.getMessage());
        }
    }

    static Object[] getImpl(String str, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        Provider.Service service;
        if (str3 != null) {
            Provider provider = Providers.getProviderList().getProvider(str3);
            if (provider == null) {
                throw new NoSuchProviderException("No such provider: " + str3);
            }
            service = provider.getService(str2, str);
        } else {
            service = getService(str2, str);
        }
        if (service == null) {
            throw new NoSuchAlgorithmException("Algorithm " + str + " not available");
        }
        return getImpl1(str, str2, service);
    }

    static Object[] getImpl(String str, String str2, Provider provider) throws NoSuchAlgorithmException {
        Provider.Service service = provider.getService(str2, str);
        if (service == null) {
            throw new NoSuchAlgorithmException("No such algorithm: " + str);
        }
        return getImpl1(str, str2, service);
    }

    private static boolean checkSuperclass(Class<?> cls, Class<?> cls2) {
        if (cls == null || cls2 == null) {
            return false;
        }
        while (!cls.equals(cls2)) {
            cls = cls.getSuperclass();
            if (cls == null) {
                return false;
            }
        }
        return true;
    }

    static Object[] truncateArray(Object[] objArr, Object[] objArr2) {
        for (int i2 = 0; i2 < objArr2.length; i2++) {
            objArr2[i2] = objArr[i2];
        }
        return objArr2;
    }
}
