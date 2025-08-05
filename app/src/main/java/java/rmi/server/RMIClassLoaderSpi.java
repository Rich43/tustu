package java.rmi.server;

import java.net.MalformedURLException;

/* loaded from: rt.jar:java/rmi/server/RMIClassLoaderSpi.class */
public abstract class RMIClassLoaderSpi {
    public abstract Class<?> loadClass(String str, String str2, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException;

    public abstract Class<?> loadProxyClass(String str, String[] strArr, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException;

    public abstract ClassLoader getClassLoader(String str) throws MalformedURLException;

    public abstract String getClassAnnotation(Class<?> cls);
}
