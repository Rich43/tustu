package java.rmi.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;

/* loaded from: rt.jar:java/rmi/server/RMIClassLoader.class */
public class RMIClassLoader {
    private static final RMIClassLoaderSpi defaultProvider = newDefaultProviderInstance();
    private static final RMIClassLoaderSpi provider = (RMIClassLoaderSpi) AccessController.doPrivileged(new PrivilegedAction<RMIClassLoaderSpi>() { // from class: java.rmi.server.RMIClassLoader.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public RMIClassLoaderSpi run2() {
            return RMIClassLoader.initializeProvider();
        }
    });

    private RMIClassLoader() {
    }

    @Deprecated
    public static Class<?> loadClass(String str) throws MalformedURLException, ClassNotFoundException {
        return loadClass((String) null, str);
    }

    public static Class<?> loadClass(URL url, String str) throws MalformedURLException, ClassNotFoundException {
        return provider.loadClass(url != null ? url.toString() : null, str, null);
    }

    public static Class<?> loadClass(String str, String str2) throws MalformedURLException, ClassNotFoundException {
        return provider.loadClass(str, str2, null);
    }

    public static Class<?> loadClass(String str, String str2, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
        return provider.loadClass(str, str2, classLoader);
    }

    public static Class<?> loadProxyClass(String str, String[] strArr, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
        return provider.loadProxyClass(str, strArr, classLoader);
    }

    public static ClassLoader getClassLoader(String str) throws MalformedURLException, SecurityException {
        return provider.getClassLoader(str);
    }

    public static String getClassAnnotation(Class<?> cls) {
        return provider.getClassAnnotation(cls);
    }

    public static RMIClassLoaderSpi getDefaultProviderInstance() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("setFactory"));
        }
        return defaultProvider;
    }

    @Deprecated
    public static Object getSecurityContext(ClassLoader classLoader) {
        return sun.rmi.server.LoaderHandler.getSecurityContext(classLoader);
    }

    private static RMIClassLoaderSpi newDefaultProviderInstance() {
        return new RMIClassLoaderSpi() { // from class: java.rmi.server.RMIClassLoader.2
            @Override // java.rmi.server.RMIClassLoaderSpi
            public Class<?> loadClass(String str, String str2, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
                return sun.rmi.server.LoaderHandler.loadClass(str, str2, classLoader);
            }

            @Override // java.rmi.server.RMIClassLoaderSpi
            public Class<?> loadProxyClass(String str, String[] strArr, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
                return sun.rmi.server.LoaderHandler.loadProxyClass(str, strArr, classLoader);
            }

            @Override // java.rmi.server.RMIClassLoaderSpi
            public ClassLoader getClassLoader(String str) throws MalformedURLException {
                return sun.rmi.server.LoaderHandler.getClassLoader(str);
            }

            @Override // java.rmi.server.RMIClassLoaderSpi
            public String getClassAnnotation(Class<?> cls) {
                return sun.rmi.server.LoaderHandler.getClassAnnotation(cls);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RMIClassLoaderSpi initializeProvider() {
        String property = System.getProperty("java.rmi.server.RMIClassLoaderSpi");
        if (property != null) {
            if (property.equals("default")) {
                return defaultProvider;
            }
            try {
                return (RMIClassLoaderSpi) Class.forName(property, false, ClassLoader.getSystemClassLoader()).asSubclass(RMIClassLoaderSpi.class).newInstance();
            } catch (ClassCastException e2) {
                LinkageError linkageError = new LinkageError("provider class not assignable to RMIClassLoaderSpi");
                linkageError.initCause(e2);
                throw linkageError;
            } catch (ClassNotFoundException e3) {
                throw new NoClassDefFoundError(e3.getMessage());
            } catch (IllegalAccessException e4) {
                throw new IllegalAccessError(e4.getMessage());
            } catch (InstantiationException e5) {
                throw new InstantiationError(e5.getMessage());
            }
        }
        Iterator it = ServiceLoader.load(RMIClassLoaderSpi.class, ClassLoader.getSystemClassLoader()).iterator();
        if (it.hasNext()) {
            try {
                return (RMIClassLoaderSpi) it.next();
            } catch (ClassCastException e6) {
                LinkageError linkageError2 = new LinkageError("provider class not assignable to RMIClassLoaderSpi");
                linkageError2.initCause(e6);
                throw linkageError2;
            }
        }
        return defaultProvider;
    }
}
