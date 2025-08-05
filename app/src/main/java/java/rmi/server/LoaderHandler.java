package java.rmi.server;

import java.net.MalformedURLException;
import java.net.URL;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/LoaderHandler.class */
public interface LoaderHandler {
    public static final String packagePrefix = "sun.rmi.server";

    @Deprecated
    Class<?> loadClass(String str) throws MalformedURLException, ClassNotFoundException;

    @Deprecated
    Class<?> loadClass(URL url, String str) throws MalformedURLException, ClassNotFoundException;

    @Deprecated
    Object getSecurityContext(ClassLoader classLoader);
}
