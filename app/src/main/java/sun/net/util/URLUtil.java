package sun.net.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLPermission;
import java.security.Permission;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:sun/net/util/URLUtil.class */
public class URLUtil {
    public static String urlNoFragString(URL url) {
        StringBuilder sb = new StringBuilder();
        String protocol = url.getProtocol();
        if (protocol != null) {
            sb.append(protocol.toLowerCase());
            sb.append("://");
        }
        String host = url.getHost();
        if (host != null) {
            sb.append(host.toLowerCase());
            int port = url.getPort();
            if (port == -1) {
                port = url.getDefaultPort();
            }
            if (port != -1) {
                sb.append(CallSiteDescriptor.TOKEN_DELIMITER).append(port);
            }
        }
        String file = url.getFile();
        if (file != null) {
            sb.append(file);
        }
        return sb.toString();
    }

    public static Permission getConnectPermission(URL url) throws IOException {
        String lowerCase = url.toString().toLowerCase();
        if (lowerCase.startsWith("http:") || lowerCase.startsWith("https:")) {
            return getURLConnectPermission(url);
        }
        if (lowerCase.startsWith("jar:http:") || lowerCase.startsWith("jar:https:")) {
            String string = url.toString();
            int iIndexOf = string.indexOf("!/");
            return getURLConnectPermission(new URL(string.substring(4, iIndexOf > -1 ? iIndexOf : string.length())));
        }
        return url.openConnection().getPermission();
    }

    private static Permission getURLConnectPermission(URL url) {
        return new URLPermission(url.getProtocol() + "://" + url.getAuthority() + url.getPath());
    }
}
