package java.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/net/CookieManager.class */
public class CookieManager extends CookieHandler {
    private CookiePolicy policyCallback;
    private CookieStore cookieJar;

    public CookieManager() {
        this(null, null);
    }

    public CookieManager(CookieStore cookieStore, CookiePolicy cookiePolicy) {
        this.cookieJar = null;
        this.policyCallback = cookiePolicy == null ? CookiePolicy.ACCEPT_ORIGINAL_SERVER : cookiePolicy;
        if (cookieStore == null) {
            this.cookieJar = new InMemoryCookieStore();
        } else {
            this.cookieJar = cookieStore;
        }
    }

    public void setCookiePolicy(CookiePolicy cookiePolicy) {
        if (cookiePolicy != null) {
            this.policyCallback = cookiePolicy;
        }
    }

    public CookieStore getCookieStore() {
        return this.cookieJar;
    }

    @Override // java.net.CookieHandler
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> map) throws IOException {
        if (uri == null || map == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        HashMap map2 = new HashMap();
        if (this.cookieJar == null) {
            return Collections.unmodifiableMap(map2);
        }
        boolean zEqualsIgnoreCase = "https".equalsIgnoreCase(uri.getScheme());
        ArrayList arrayList = new ArrayList();
        String path = uri.getPath();
        if (path == null || path.isEmpty()) {
            path = "/";
        }
        for (HttpCookie httpCookie : this.cookieJar.get(uri)) {
            if (pathMatches(path, httpCookie.getPath()) && (zEqualsIgnoreCase || !httpCookie.getSecure())) {
                if (httpCookie.isHttpOnly()) {
                    String scheme = uri.getScheme();
                    if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                    }
                }
                String portlist = httpCookie.getPortlist();
                if (portlist != null && !portlist.isEmpty()) {
                    int port = uri.getPort();
                    if (port == -1) {
                        port = "https".equals(uri.getScheme()) ? 443 : 80;
                    }
                    if (isInPortList(portlist, port)) {
                        arrayList.add(httpCookie);
                    }
                } else {
                    arrayList.add(httpCookie);
                }
            }
        }
        map2.put("Cookie", sortByPath(arrayList));
        return Collections.unmodifiableMap(map2);
    }

    @Override // java.net.CookieHandler
    public void put(URI uri, Map<String, List<String>> map) throws IOException {
        List<HttpCookie> listEmptyList;
        if (uri == null || map == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        if (this.cookieJar == null) {
            return;
        }
        PlatformLogger logger = PlatformLogger.getLogger("java.net.CookieManager");
        for (String str : map.keySet()) {
            if (str != null && (str.equalsIgnoreCase("Set-Cookie2") || str.equalsIgnoreCase("Set-Cookie"))) {
                for (String str2 : map.get(str)) {
                    try {
                        try {
                            listEmptyList = HttpCookie.parse(str2);
                        } catch (IllegalArgumentException e2) {
                            listEmptyList = Collections.emptyList();
                            if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                                logger.severe("Invalid cookie for " + ((Object) uri) + ": " + str2);
                            }
                        }
                        for (HttpCookie httpCookie : listEmptyList) {
                            if (httpCookie.getPath() == null) {
                                String path = uri.getPath();
                                if (!path.endsWith("/")) {
                                    int iLastIndexOf = path.lastIndexOf("/");
                                    if (iLastIndexOf > 0) {
                                        path = path.substring(0, iLastIndexOf + 1);
                                    } else {
                                        path = "/";
                                    }
                                }
                                httpCookie.setPath(path);
                            }
                            if (httpCookie.getDomain() == null) {
                                String host = uri.getHost();
                                if (host != null && !host.contains(".")) {
                                    host = host + ".local";
                                }
                                httpCookie.setDomain(host);
                            }
                            String portlist = httpCookie.getPortlist();
                            if (portlist != null) {
                                int port = uri.getPort();
                                if (port == -1) {
                                    port = "https".equals(uri.getScheme()) ? 443 : 80;
                                }
                                if (portlist.isEmpty()) {
                                    httpCookie.setPortlist("" + port);
                                    if (shouldAcceptInternal(uri, httpCookie)) {
                                        this.cookieJar.add(uri, httpCookie);
                                    }
                                } else if (isInPortList(portlist, port) && shouldAcceptInternal(uri, httpCookie)) {
                                    this.cookieJar.add(uri, httpCookie);
                                }
                            } else if (shouldAcceptInternal(uri, httpCookie)) {
                                this.cookieJar.add(uri, httpCookie);
                            }
                        }
                    } catch (IllegalArgumentException e3) {
                    }
                }
            }
        }
    }

    private boolean shouldAcceptInternal(URI uri, HttpCookie httpCookie) {
        try {
            return this.policyCallback.shouldAccept(uri, httpCookie);
        } catch (Exception e2) {
            return false;
        }
    }

    private static boolean isInPortList(String str, int i2) {
        int iIndexOf = str.indexOf(",");
        while (iIndexOf > 0) {
            if (Integer.parseInt(str.substring(0, iIndexOf)) == i2) {
                return true;
            }
            str = str.substring(iIndexOf + 1);
            iIndexOf = str.indexOf(",");
        }
        if (!str.isEmpty()) {
            try {
                if (Integer.parseInt(str) == i2) {
                    return true;
                }
                return false;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
        return false;
    }

    private boolean pathMatches(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str != null && str2 != null && str.startsWith(str2)) {
            return true;
        }
        return false;
    }

    private List<String> sortByPath(List<HttpCookie> list) {
        Collections.sort(list, new CookiePathComparator());
        ArrayList arrayList = new ArrayList();
        for (HttpCookie httpCookie : list) {
            if (list.indexOf(httpCookie) == 0 && httpCookie.getVersion() > 0) {
                arrayList.add("$Version=\"1\"");
            }
            arrayList.add(httpCookie.toString());
        }
        return arrayList;
    }

    /* loaded from: rt.jar:java/net/CookieManager$CookiePathComparator.class */
    static class CookiePathComparator implements Comparator<HttpCookie> {
        CookiePathComparator() {
        }

        @Override // java.util.Comparator
        public int compare(HttpCookie httpCookie, HttpCookie httpCookie2) {
            if (httpCookie == httpCookie2) {
                return 0;
            }
            if (httpCookie == null) {
                return -1;
            }
            if (httpCookie2 == null) {
                return 1;
            }
            if (!httpCookie.getName().equals(httpCookie2.getName())) {
                return 0;
            }
            if (httpCookie.getPath().startsWith(httpCookie2.getPath())) {
                return -1;
            }
            if (httpCookie2.getPath().startsWith(httpCookie.getPath())) {
                return 1;
            }
            return 0;
        }
    }
}
