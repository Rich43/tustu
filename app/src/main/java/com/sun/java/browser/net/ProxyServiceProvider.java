package com.sun.java.browser.net;

import java.net.URL;

/* loaded from: rt.jar:com/sun/java/browser/net/ProxyServiceProvider.class */
public interface ProxyServiceProvider {
    ProxyInfo[] getProxyInfo(URL url);
}
