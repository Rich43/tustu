package sun.nio.fs;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:sun/nio/fs/WindowsUriSupport.class */
class WindowsUriSupport {
    private static final String IPV6_LITERAL_SUFFIX = ".ipv6-literal.net";

    private WindowsUriSupport() {
    }

    private static URI toUri(String str, boolean z2, boolean z3) {
        String strSubstring;
        String strReplace;
        if (z2) {
            int iIndexOf = str.indexOf(92, 2);
            strSubstring = str.substring(2, iIndexOf);
            strReplace = str.substring(iIndexOf).replace('\\', '/');
            if (strSubstring.endsWith(IPV6_LITERAL_SUFFIX)) {
                strSubstring = strSubstring.substring(0, strSubstring.length() - IPV6_LITERAL_SUFFIX.length()).replace('-', ':').replace('s', '%');
            }
        } else {
            strSubstring = "";
            strReplace = "/" + str.replace('\\', '/');
        }
        if (z3) {
            strReplace = strReplace + "/";
        }
        try {
            return new URI(DeploymentDescriptorParser.ATTR_FILE, strSubstring, strReplace, null);
        } catch (URISyntaxException e2) {
            if (!z2) {
                throw new AssertionError(e2);
            }
            String str2 = "//" + str.replace('\\', '/');
            if (z3) {
                str2 = str2 + "/";
            }
            try {
                return new URI(DeploymentDescriptorParser.ATTR_FILE, null, str2, null);
            } catch (URISyntaxException e3) {
                throw new AssertionError(e3);
            }
        }
    }

    static URI toUri(WindowsPath windowsPath) {
        WindowsPath absolutePath = windowsPath.toAbsolutePath();
        String string = absolutePath.toString();
        boolean zIsDirectory = false;
        if (!string.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
            try {
                absolutePath.checkRead();
                zIsDirectory = WindowsFileAttributes.get(absolutePath, true).isDirectory();
            } catch (SecurityException | WindowsException e2) {
            }
        }
        return toUri(string, absolutePath.isUnc(), zIsDirectory);
    }

    static WindowsPath fromUri(WindowsFileSystem windowsFileSystem, URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute");
        }
        if (uri.isOpaque()) {
            throw new IllegalArgumentException("URI is not hierarchical");
        }
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
            throw new IllegalArgumentException("URI scheme is not \"file\"");
        }
        if (uri.getFragment() != null) {
            throw new IllegalArgumentException("URI has a fragment component");
        }
        if (uri.getQuery() != null) {
            throw new IllegalArgumentException("URI has a query component");
        }
        String path = uri.getPath();
        if (path.equals("")) {
            throw new IllegalArgumentException("URI path component is empty");
        }
        String authority = uri.getAuthority();
        if (authority != null && !authority.equals("")) {
            String host = uri.getHost();
            if (host == null) {
                throw new IllegalArgumentException("URI authority component has undefined host");
            }
            if (uri.getUserInfo() != null) {
                throw new IllegalArgumentException("URI authority component has user-info");
            }
            if (uri.getPort() != -1) {
                throw new IllegalArgumentException("URI authority component has port number");
            }
            if (host.startsWith("[")) {
                host = host.substring(1, host.length() - 1).replace(':', '-').replace('%', 's') + IPV6_LITERAL_SUFFIX;
            }
            path = "\\\\" + host + path;
        } else if (path.length() > 2 && path.charAt(2) == ':') {
            path = path.substring(1);
        }
        return WindowsPath.parse(windowsFileSystem, path);
    }
}
