package com.sun.webkit.network.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* loaded from: jfxrt.jar:com/sun/webkit/network/data/Handler.class */
public final class Handler extends URLStreamHandler {
    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return new DataURLConnection(url);
    }
}
