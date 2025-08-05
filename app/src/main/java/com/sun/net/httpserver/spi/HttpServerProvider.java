package com.sun.net.httpserver.spi;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import jdk.Exported;
import sun.net.httpserver.DefaultHttpServerProvider;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/spi/HttpServerProvider.class */
public abstract class HttpServerProvider {
    private static final Object lock = new Object();
    private static HttpServerProvider provider = null;

    public abstract HttpServer createHttpServer(InetSocketAddress inetSocketAddress, int i2) throws IOException;

    public abstract HttpsServer createHttpsServer(InetSocketAddress inetSocketAddress, int i2) throws IOException;

    protected HttpServerProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("httpServerProvider"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderFromProperty() {
        String property = System.getProperty("com.sun.net.httpserver.HttpServerProvider");
        if (property == null) {
            return false;
        }
        try {
            provider = (HttpServerProvider) Class.forName(property, true, ClassLoader.getSystemClassLoader()).newInstance();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SecurityException e2) {
            throw new ServiceConfigurationError(null, e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean loadProviderAsService() {
        Iterator it = ServiceLoader.load(HttpServerProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            try {
                provider = (HttpServerProvider) it.next();
                return true;
            } catch (ServiceConfigurationError e2) {
                if (!(e2.getCause() instanceof SecurityException)) {
                    throw e2;
                }
            }
        }
        return false;
    }

    public static HttpServerProvider provider() {
        synchronized (lock) {
            if (provider != null) {
                return provider;
            }
            return (HttpServerProvider) AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.sun.net.httpserver.spi.HttpServerProvider.1
                @Override // java.security.PrivilegedAction
                public Object run() {
                    if (!HttpServerProvider.loadProviderFromProperty() && !HttpServerProvider.loadProviderAsService()) {
                        HttpServerProvider unused = HttpServerProvider.provider = new DefaultHttpServerProvider();
                        return HttpServerProvider.provider;
                    }
                    return HttpServerProvider.provider;
                }
            });
        }
    }
}
