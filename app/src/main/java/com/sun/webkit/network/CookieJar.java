package com.sun.webkit.network;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: jfxrt.jar:com/sun/webkit/network/CookieJar.class */
final class CookieJar {
    private CookieJar() {
    }

    private static void fwkPut(String url, String cookie) {
        CookieHandler handler = CookieHandler.getDefault();
        if (handler != null) {
            try {
                URI uri = new URI(url);
                URI uri2 = rewriteToFilterOutHttpOnlyCookies(uri);
                Map<String, List<String>> headers = new HashMap<>();
                List<String> val = new ArrayList<>();
                val.add(cookie);
                headers.put("Set-Cookie", val);
                try {
                    handler.put(uri2, headers);
                } catch (IOException e2) {
                }
            } catch (URISyntaxException e3) {
            }
        }
    }

    private static String fwkGet(String url, boolean includeHttpOnlyCookies) {
        CookieHandler handler = CookieHandler.getDefault();
        if (handler != null) {
            try {
                URI uri = new URI(url);
                if (!includeHttpOnlyCookies) {
                    uri = rewriteToFilterOutHttpOnlyCookies(uri);
                }
                Map<String, List<String>> headers = new HashMap<>();
                try {
                    Map<String, List<String>> val = handler.get(uri, headers);
                    if (val != null) {
                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<String, List<String>> entry : val.entrySet()) {
                            String key = entry.getKey();
                            if ("Cookie".equalsIgnoreCase(key)) {
                                for (String s2 : entry.getValue()) {
                                    if (sb.length() > 0) {
                                        sb.append(VectorFormat.DEFAULT_SEPARATOR);
                                    }
                                    sb.append(s2);
                                }
                            }
                        }
                        return sb.toString();
                    }
                    return null;
                } catch (IOException e2) {
                    return null;
                }
            } catch (URISyntaxException e3) {
                return null;
            }
        }
        return null;
    }

    private static URI rewriteToFilterOutHttpOnlyCookies(URI uri) throws URISyntaxException {
        return new URI(uri.getScheme().equalsIgnoreCase("https") ? "javascripts" : "javascript", uri.getRawSchemeSpecificPart(), uri.getRawFragment());
    }
}
