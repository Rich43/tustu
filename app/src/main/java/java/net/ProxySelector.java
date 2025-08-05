package java.net;

import java.io.IOException;
import java.util.List;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/ProxySelector.class */
public abstract class ProxySelector {
    private static ProxySelector theProxySelector;

    public abstract List<Proxy> select(URI uri);

    public abstract void connectFailed(URI uri, SocketAddress socketAddress, IOException iOException);

    static {
        try {
            Class<?> cls = Class.forName("sun.net.spi.DefaultProxySelector");
            if (cls != null && ProxySelector.class.isAssignableFrom(cls)) {
                theProxySelector = (ProxySelector) cls.newInstance();
            }
        } catch (Exception e2) {
            theProxySelector = null;
        }
    }

    public static ProxySelector getDefault() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_PROXYSELECTOR_PERMISSION);
        }
        return theProxySelector;
    }

    public static void setDefault(ProxySelector proxySelector) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.SET_PROXYSELECTOR_PERMISSION);
        }
        theProxySelector = proxySelector;
    }
}
