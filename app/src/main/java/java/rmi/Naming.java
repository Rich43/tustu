package java.rmi;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/rmi/Naming.class */
public final class Naming {
    private Naming() {
    }

    public static Remote lookup(String str) throws MalformedURLException, NotBoundException, RemoteException {
        ParsedNamingURL url = parseURL(str);
        Registry registry = getRegistry(url);
        if (url.name == null) {
            return registry;
        }
        return registry.lookup(url.name);
    }

    public static void bind(String str, Remote remote) throws MalformedURLException, AlreadyBoundException, RemoteException {
        ParsedNamingURL url = parseURL(str);
        Registry registry = getRegistry(url);
        if (remote == null) {
            throw new NullPointerException("cannot bind to null");
        }
        registry.bind(url.name, remote);
    }

    public static void unbind(String str) throws MalformedURLException, NotBoundException, RemoteException {
        ParsedNamingURL url = parseURL(str);
        getRegistry(url).unbind(url.name);
    }

    public static void rebind(String str, Remote remote) throws MalformedURLException, RemoteException {
        ParsedNamingURL url = parseURL(str);
        Registry registry = getRegistry(url);
        if (remote == null) {
            throw new NullPointerException("cannot bind to null");
        }
        registry.rebind(url.name, remote);
    }

    public static String[] list(String str) throws MalformedURLException, RemoteException {
        ParsedNamingURL url = parseURL(str);
        Registry registry = getRegistry(url);
        String str2 = "";
        if (url.port > 0 || !url.host.equals("")) {
            str2 = str2 + "//" + url.host;
        }
        if (url.port > 0) {
            str2 = str2 + CallSiteDescriptor.TOKEN_DELIMITER + url.port;
        }
        String str3 = str2 + "/";
        String[] list = registry.list();
        for (int i2 = 0; i2 < list.length; i2++) {
            list[i2] = str3 + list[i2];
        }
        return list;
    }

    private static Registry getRegistry(ParsedNamingURL parsedNamingURL) throws RemoteException {
        return LocateRegistry.getRegistry(parsedNamingURL.host, parsedNamingURL.port);
    }

    private static ParsedNamingURL parseURL(String str) throws MalformedURLException {
        try {
            return intParseURL(str);
        } catch (URISyntaxException e2) {
            MalformedURLException malformedURLException = new MalformedURLException("invalid URL String: " + str);
            malformedURLException.initCause(e2);
            int iIndexOf = str.indexOf(58);
            int iIndexOf2 = str.indexOf("//:");
            if (iIndexOf2 < 0) {
                throw malformedURLException;
            }
            if (iIndexOf2 == 0 || (iIndexOf > 0 && iIndexOf2 == iIndexOf + 1)) {
                int i2 = iIndexOf2 + 2;
                try {
                    return intParseURL(str.substring(0, i2) + "localhost" + str.substring(i2));
                } catch (MalformedURLException e3) {
                    throw e3;
                } catch (URISyntaxException e4) {
                    throw malformedURLException;
                }
            }
            throw malformedURLException;
        }
    }

    private static ParsedNamingURL intParseURL(String str) throws MalformedURLException, URISyntaxException {
        URI uri = new URI(str);
        if (uri.isOpaque()) {
            throw new MalformedURLException("not a hierarchical URL: " + str);
        }
        if (uri.getFragment() != null) {
            throw new MalformedURLException("invalid character, '#', in URL name: " + str);
        }
        if (uri.getQuery() != null) {
            throw new MalformedURLException("invalid character, '?', in URL name: " + str);
        }
        if (uri.getUserInfo() != null) {
            throw new MalformedURLException("invalid character, '@', in URL host: " + str);
        }
        String scheme = uri.getScheme();
        if (scheme != null && !scheme.equals("rmi")) {
            throw new MalformedURLException("invalid URL scheme: " + str);
        }
        String path = uri.getPath();
        if (path != null) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.length() == 0) {
                path = null;
            }
        }
        String host = uri.getHost();
        if (host == null) {
            host = "";
            try {
                uri.parseServerAuthority();
            } catch (URISyntaxException e2) {
                String authority = uri.getAuthority();
                if (authority != null && authority.startsWith(CallSiteDescriptor.TOKEN_DELIMITER)) {
                    try {
                        uri = new URI(null, "localhost" + authority, null, null, null);
                        uri.parseServerAuthority();
                    } catch (URISyntaxException e3) {
                        throw new MalformedURLException("invalid authority: " + str);
                    }
                } else {
                    throw new MalformedURLException("invalid authority: " + str);
                }
            }
        }
        int port = uri.getPort();
        if (port == -1) {
            port = 1099;
        }
        return new ParsedNamingURL(host, port, path);
    }

    /* loaded from: rt.jar:java/rmi/Naming$ParsedNamingURL.class */
    private static class ParsedNamingURL {
        String host;
        int port;
        String name;

        ParsedNamingURL(String str, int i2, String str2) {
            this.host = str;
            this.port = i2;
            this.name = str2;
        }
    }
}
