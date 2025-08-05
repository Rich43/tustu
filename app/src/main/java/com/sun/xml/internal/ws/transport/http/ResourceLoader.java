package com.sun.xml.internal.ws.transport.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/ResourceLoader.class */
public interface ResourceLoader {
    URL getResource(String str) throws MalformedURLException;

    URL getCatalogFile() throws MalformedURLException;

    Set<String> getResourcePaths(String str);
}
