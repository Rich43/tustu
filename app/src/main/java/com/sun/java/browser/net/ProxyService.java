package com.sun.java.browser.net;

import java.io.IOException;
import java.net.URL;

/* loaded from: rt.jar:com/sun/java/browser/net/ProxyService.class */
public class ProxyService {
    private static ProxyServiceProvider provider = null;

    public static void setProvider(ProxyServiceProvider proxyServiceProvider) throws IOException {
        if (null == provider) {
            provider = proxyServiceProvider;
            return;
        }
        throw new IOException("Proxy service provider has already been set.");
    }

    public static ProxyInfo[] getProxyInfo(URL url) throws IOException {
        if (null == provider) {
            throw new IOException("Proxy service provider is not yet set");
        }
        return provider.getProxyInfo(url);
    }
}
