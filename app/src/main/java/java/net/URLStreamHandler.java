package java.net;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.util.IPAddressUtil;

/* loaded from: rt.jar:java/net/URLStreamHandler.class */
public abstract class URLStreamHandler {
    protected abstract URLConnection openConnection(URL url) throws IOException;

    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    protected void parseURL(URL url, String str, int i2, int i3) {
        int iLastIndexOf;
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        String userInfo = url.getUserInfo();
        String host = url.getHost();
        int port = url.getPort();
        String path = url.getPath();
        String query = url.getQuery();
        String ref = url.getRef();
        boolean z2 = false;
        boolean z3 = false;
        if (i2 < i3) {
            int iIndexOf = str.indexOf(63);
            z3 = iIndexOf == i2;
            if (iIndexOf != -1 && iIndexOf < i3) {
                query = str.substring(iIndexOf + 1, i3);
                if (i3 > iIndexOf) {
                    i3 = iIndexOf;
                }
                str = str.substring(0, iIndexOf);
            }
        }
        if (!(i2 <= i3 - 4 && str.charAt(i2) == '/' && str.charAt(i2 + 1) == '/' && str.charAt(i2 + 2) == '/' && str.charAt(i2 + 3) == '/') && i2 <= i3 - 2 && str.charAt(i2) == '/' && str.charAt(i2 + 1) == '/') {
            int i4 = i2 + 2;
            int iIndexOf2 = str.indexOf(47, i4);
            if (iIndexOf2 < 0 || iIndexOf2 > i3) {
                iIndexOf2 = str.indexOf(63, i4);
                if (iIndexOf2 < 0 || iIndexOf2 > i3) {
                    iIndexOf2 = i3;
                }
            }
            String strSubstring = str.substring(i4, iIndexOf2);
            authority = strSubstring;
            host = strSubstring;
            int iIndexOf3 = authority.indexOf(64);
            if (iIndexOf3 != -1) {
                if (iIndexOf3 != authority.lastIndexOf(64)) {
                    userInfo = null;
                    host = null;
                } else {
                    userInfo = authority.substring(0, iIndexOf3);
                    host = authority.substring(iIndexOf3 + 1);
                }
            } else {
                userInfo = null;
            }
            if (host != null) {
                if (host.length() > 0 && host.charAt(0) == '[') {
                    int iIndexOf4 = host.indexOf(93);
                    if (iIndexOf4 > 2) {
                        String str2 = host;
                        host = str2.substring(0, iIndexOf4 + 1);
                        if (!IPAddressUtil.isIPv6LiteralAddress(host.substring(1, iIndexOf4))) {
                            throw new IllegalArgumentException("Invalid host: " + host);
                        }
                        port = -1;
                        if (str2.length() > iIndexOf4 + 1) {
                            if (str2.charAt(iIndexOf4 + 1) == ':') {
                                int i5 = iIndexOf4 + 1;
                                if (str2.length() > i5 + 1) {
                                    port = Integer.parseInt(str2.substring(i5 + 1));
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid authority field: " + authority);
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid authority field: " + authority);
                    }
                } else {
                    int iIndexOf5 = host.indexOf(58);
                    port = -1;
                    if (iIndexOf5 >= 0) {
                        if (host.length() > iIndexOf5 + 1) {
                            port = Integer.parseInt(host.substring(iIndexOf5 + 1));
                        }
                        host = host.substring(0, iIndexOf5);
                    }
                }
            } else {
                host = "";
            }
            if (port < -1) {
                throw new IllegalArgumentException("Invalid port number :" + port);
            }
            i2 = iIndexOf2;
            if (authority != null && authority.length() > 0) {
                path = "";
            }
        }
        if (host == null) {
            host = "";
        }
        if (i2 < i3) {
            if (str.charAt(i2) == '/') {
                path = str.substring(i2, i3);
            } else if (path != null && path.length() > 0) {
                z2 = true;
                int iLastIndexOf2 = path.lastIndexOf(47);
                String str3 = "";
                if (iLastIndexOf2 == -1 && authority != null) {
                    str3 = "/";
                }
                path = path.substring(0, iLastIndexOf2 + 1) + str3 + str.substring(i2, i3);
            } else {
                path = (authority != null ? "/" : "") + str.substring(i2, i3);
            }
        } else if (z3 && path != null) {
            int iLastIndexOf3 = path.lastIndexOf(47);
            if (iLastIndexOf3 < 0) {
                iLastIndexOf3 = 0;
            }
            path = path.substring(0, iLastIndexOf3) + "/";
        }
        if (path == null) {
            path = "";
        }
        if (z2) {
            while (true) {
                int iIndexOf6 = path.indexOf("/./");
                if (iIndexOf6 < 0) {
                    break;
                } else {
                    path = path.substring(0, iIndexOf6) + path.substring(iIndexOf6 + 2);
                }
            }
            int i6 = 0;
            while (true) {
                int iIndexOf7 = path.indexOf("/../", i6);
                if (iIndexOf7 < 0) {
                    break;
                }
                if (iIndexOf7 > 0 && (iLastIndexOf = path.lastIndexOf(47, iIndexOf7 - 1)) >= 0 && path.indexOf("/../", iLastIndexOf) != 0) {
                    path = path.substring(0, iLastIndexOf) + path.substring(iIndexOf7 + 3);
                    i6 = 0;
                } else {
                    i6 = iIndexOf7 + 3;
                }
            }
            while (path.endsWith("/..")) {
                int iLastIndexOf4 = path.lastIndexOf(47, path.indexOf("/..") - 1);
                if (iLastIndexOf4 < 0) {
                    break;
                } else {
                    path = path.substring(0, iLastIndexOf4 + 1);
                }
            }
            if (path.startsWith("./") && path.length() > 2) {
                path = path.substring(2);
            }
            if (path.endsWith("/.")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        setURL(url, protocol, host, port, authority, userInfo, path, query, ref);
    }

    protected int getDefaultPort() {
        return -1;
    }

    protected boolean equals(URL url, URL url2) {
        String ref = url.getRef();
        String ref2 = url2.getRef();
        return (ref == ref2 || (ref != null && ref.equals(ref2))) && sameFile(url, url2);
    }

    protected int hashCode(URL url) {
        int port;
        int iHashCode = 0;
        String protocol = url.getProtocol();
        if (protocol != null) {
            iHashCode = 0 + protocol.hashCode();
        }
        InetAddress hostAddress = getHostAddress(url);
        if (hostAddress != null) {
            iHashCode += hostAddress.hashCode();
        } else {
            String host = url.getHost();
            if (host != null) {
                iHashCode += host.toLowerCase().hashCode();
            }
        }
        String file = url.getFile();
        if (file != null) {
            iHashCode += file.hashCode();
        }
        if (url.getPort() == -1) {
            port = iHashCode + getDefaultPort();
        } else {
            port = iHashCode + url.getPort();
        }
        String ref = url.getRef();
        if (ref != null) {
            port += ref.hashCode();
        }
        return port;
    }

    protected boolean sameFile(URL url, URL url2) {
        if (url.getProtocol() != url2.getProtocol() && (url.getProtocol() == null || !url.getProtocol().equalsIgnoreCase(url2.getProtocol()))) {
            return false;
        }
        if (url.getFile() != url2.getFile() && (url.getFile() == null || !url.getFile().equals(url2.getFile()))) {
            return false;
        }
        if ((url.getPort() != -1 ? url.getPort() : url.handler.getDefaultPort()) != (url2.getPort() != -1 ? url2.getPort() : url2.handler.getDefaultPort()) || !hostsEqual(url, url2)) {
            return false;
        }
        return true;
    }

    protected InetAddress getHostAddress(URL url) {
        return url.getHostAddress();
    }

    protected boolean hostsEqual(URL url, URL url2) {
        InetAddress hostAddress = getHostAddress(url);
        InetAddress hostAddress2 = getHostAddress(url2);
        if (hostAddress != null && hostAddress2 != null) {
            return hostAddress.equals(hostAddress2);
        }
        if (url.getHost() == null || url2.getHost() == null) {
            return url.getHost() == null && url2.getHost() == null;
        }
        return url.getHost().equalsIgnoreCase(url2.getHost());
    }

    protected String toExternalForm(URL url) {
        int length = url.getProtocol().length() + 1;
        if (url.getAuthority() != null && url.getAuthority().length() > 0) {
            length += 2 + url.getAuthority().length();
        }
        if (url.getPath() != null) {
            length += url.getPath().length();
        }
        if (url.getQuery() != null) {
            length += 1 + url.getQuery().length();
        }
        if (url.getRef() != null) {
            length += 1 + url.getRef().length();
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        stringBuffer.append(url.getProtocol());
        stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
        if (url.getAuthority() != null && url.getAuthority().length() > 0) {
            stringBuffer.append("//");
            stringBuffer.append(url.getAuthority());
        }
        if (url.getPath() != null) {
            stringBuffer.append(url.getPath());
        }
        if (url.getQuery() != null) {
            stringBuffer.append('?');
            stringBuffer.append(url.getQuery());
        }
        if (url.getRef() != null) {
            stringBuffer.append(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            stringBuffer.append(url.getRef());
        }
        return stringBuffer.toString();
    }

    protected void setURL(URL url, String str, String str2, int i2, String str3, String str4, String str5, String str6, String str7) {
        String strCheckHostString;
        if (this != url.handler) {
            throw new SecurityException("handler for url different from this handler");
        }
        if (str2 != null && url.isBuiltinStreamHandler(this) && (strCheckHostString = IPAddressUtil.checkHostString(str2)) != null) {
            throw new IllegalArgumentException(strCheckHostString);
        }
        url.set(url.getProtocol(), str2, i2, str3, str4, str5, str6, str7);
    }

    @Deprecated
    protected void setURL(URL url, String str, String str2, int i2, String str3, String str4) {
        String str5 = null;
        String strSubstring = null;
        if (str2 != null && str2.length() != 0) {
            str5 = i2 == -1 ? str2 : str2 + CallSiteDescriptor.TOKEN_DELIMITER + i2;
            int iLastIndexOf = str2.lastIndexOf(64);
            if (iLastIndexOf != -1) {
                strSubstring = str2.substring(0, iLastIndexOf);
                str2 = str2.substring(iLastIndexOf + 1);
            }
        }
        String strSubstring2 = null;
        String strSubstring3 = null;
        if (str3 != null) {
            int iLastIndexOf2 = str3.lastIndexOf(63);
            if (iLastIndexOf2 != -1) {
                strSubstring3 = str3.substring(iLastIndexOf2 + 1);
                strSubstring2 = str3.substring(0, iLastIndexOf2);
            } else {
                strSubstring2 = str3;
            }
        }
        setURL(url, str, str2, i2, str5, strSubstring, strSubstring2, strSubstring3, str4);
    }
}
