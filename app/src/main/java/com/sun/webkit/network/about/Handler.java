package com.sun.webkit.network.about;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* loaded from: jfxrt.jar:com/sun/webkit/network/about/Handler.class */
public final class Handler extends URLStreamHandler {
    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) {
        return new AboutURLConnection(url);
    }
}
