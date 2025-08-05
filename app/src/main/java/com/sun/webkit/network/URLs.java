package com.sun.webkit.network;

import com.sun.webkit.network.about.Handler;
import java.net.MalformedURLException;
import java.net.NetPermission;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/webkit/network/URLs.class */
public final class URLs {
    private static final Map<String, URLStreamHandler> handlerMap;
    private static final Permission streamHandlerPermission;

    static {
        Map<String, URLStreamHandler> map = new HashMap<>(2);
        map.put("about", new Handler());
        map.put("data", new com.sun.webkit.network.data.Handler());
        handlerMap = Collections.unmodifiableMap(map);
        streamHandlerPermission = new NetPermission("specifyStreamHandler");
    }

    private URLs() {
        throw new AssertionError();
    }

    public static URL newURL(String spec) throws MalformedURLException {
        return newURL(null, spec);
    }

    public static URL newURL(URL context, String spec) throws MalformedURLException {
        try {
            return new URL(context, spec);
        } catch (MalformedURLException ex) {
            int colonPosition = spec.indexOf(58);
            URLStreamHandler handler = colonPosition != -1 ? handlerMap.get(spec.substring(0, colonPosition).toLowerCase()) : null;
            if (handler == null) {
                throw ex;
            }
            try {
                return (URL) AccessController.doPrivileged(() -> {
                    try {
                        return new URL(context, spec, handler);
                    } catch (MalformedURLException muex) {
                        throw new RuntimeException(muex);
                    }
                }, (AccessControlContext) null, streamHandlerPermission);
            } catch (RuntimeException re) {
                if (re.getCause() instanceof MalformedURLException) {
                    throw ((MalformedURLException) re.getCause());
                }
                throw re;
            }
        }
    }
}
