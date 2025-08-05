package com.sun.webkit.network;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.MalformedURLException;

/* loaded from: jfxrt.jar:com/sun/webkit/network/Util.class */
public final class Util {
    private Util() {
        throw new AssertionError();
    }

    public static String adjustUrlForWebKit(String url) throws MalformedURLException {
        int pos;
        if (URLs.newURL(url).getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE) && (pos = "file:".length()) < url.length() && url.charAt(pos) != '/') {
            url = url.substring(0, pos) + "///" + url.substring(pos);
        }
        return url;
    }

    static String formatHeaders(String headers) {
        return headers.trim().replaceAll("(?m)^", "    ");
    }
}
