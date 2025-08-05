package sun.net.www.protocol.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.AccessController;
import java.util.HashMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.www.HeaderParser;
import sun.net.www.protocol.http.AuthCacheValue;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:sun/net/www/protocol/http/AuthenticationInfo.class */
public abstract class AuthenticationInfo extends AuthCacheValue implements Cloneable {
    static final long serialVersionUID = -2588378268010453259L;
    public static final char SERVER_AUTHENTICATION = 's';
    public static final char PROXY_AUTHENTICATION = 'p';
    static final boolean serializeAuth;
    protected transient PasswordAuthentication pw;
    private static HashMap<String, Thread> requests;
    char type;
    AuthScheme authScheme;
    String protocol;
    String host;
    int port;
    String realm;
    String path;
    String s1;
    String s2;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract boolean supportsPreemptiveAuthorization();

    public abstract String getHeaderValue(URL url, String str);

    public abstract boolean setHeaders(HttpURLConnection httpURLConnection, HeaderParser headerParser, String str);

    public abstract boolean isAuthorizationStale(String str);

    static {
        $assertionsDisabled = !AuthenticationInfo.class.desiredAssertionStatus();
        serializeAuth = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("http.auth.serializeRequests"))).booleanValue();
        requests = new HashMap<>();
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public PasswordAuthentication credentials() {
        return this.pw;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public AuthCacheValue.Type getAuthType() {
        return this.type == 's' ? AuthCacheValue.Type.Server : AuthCacheValue.Type.Proxy;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    AuthScheme getAuthScheme() {
        return this.authScheme;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public String getHost() {
        return this.host;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public int getPort() {
        return this.port;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public String getRealm() {
        return this.realm;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public String getPath() {
        return this.path;
    }

    @Override // sun.net.www.protocol.http.AuthCacheValue
    public String getProtocolScheme() {
        return this.protocol;
    }

    protected boolean useAuthCache() {
        return true;
    }

    private static boolean requestIsInProgress(String str) {
        if (!serializeAuth) {
            return false;
        }
        synchronized (requests) {
            Thread threadCurrentThread = Thread.currentThread();
            Thread thread = requests.get(str);
            if (thread == null) {
                requests.put(str, threadCurrentThread);
                return false;
            }
            if (thread == threadCurrentThread) {
                return false;
            }
            while (requests.containsKey(str)) {
                try {
                    requests.wait();
                } catch (InterruptedException e2) {
                }
            }
            return true;
        }
    }

    private static void requestCompleted(String str) {
        synchronized (requests) {
            Thread thread = requests.get(str);
            if (thread != null && thread == Thread.currentThread()) {
                boolean z2 = requests.remove(str) != null;
                if (!$assertionsDisabled && !z2) {
                    throw new AssertionError();
                }
            }
            requests.notifyAll();
        }
    }

    public AuthenticationInfo(char c2, AuthScheme authScheme, String str, int i2, String str2) {
        this.type = c2;
        this.authScheme = authScheme;
        this.protocol = "";
        this.host = str.toLowerCase();
        this.port = i2;
        this.realm = str2;
        this.path = null;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public AuthenticationInfo(char c2, AuthScheme authScheme, URL url, String str) {
        this.type = c2;
        this.authScheme = authScheme;
        this.protocol = url.getProtocol().toLowerCase();
        this.host = url.getHost().toLowerCase();
        this.port = url.getPort();
        if (this.port == -1) {
            this.port = url.getDefaultPort();
        }
        this.realm = str;
        String path = url.getPath();
        if (path.length() == 0) {
            this.path = path;
        } else {
            this.path = reducePath(path);
        }
    }

    static String reducePath(String str) {
        int iLastIndexOf = str.lastIndexOf(47);
        int iLastIndexOf2 = str.lastIndexOf(46);
        if (iLastIndexOf != -1) {
            if (iLastIndexOf < iLastIndexOf2) {
                return str.substring(0, iLastIndexOf + 1);
            }
            return str;
        }
        return str;
    }

    static AuthenticationInfo getServerAuth(URL url) {
        int port = url.getPort();
        if (port == -1) {
            port = url.getDefaultPort();
        }
        return getAuth("s:" + url.getProtocol().toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + url.getHost().toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + port, url);
    }

    static String getServerAuthKey(URL url, String str, AuthScheme authScheme) {
        int port = url.getPort();
        if (port == -1) {
            port = url.getDefaultPort();
        }
        return "s:" + ((Object) authScheme) + CallSiteDescriptor.TOKEN_DELIMITER + url.getProtocol().toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + url.getHost().toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + port + CallSiteDescriptor.TOKEN_DELIMITER + str;
    }

    static AuthenticationInfo getServerAuth(String str) {
        AuthenticationInfo auth = getAuth(str, null);
        if (auth == null && requestIsInProgress(str)) {
            auth = getAuth(str, null);
        }
        return auth;
    }

    static AuthenticationInfo getAuth(String str, URL url) {
        if (url == null) {
            return (AuthenticationInfo) cache.get(str, null);
        }
        return (AuthenticationInfo) cache.get(str, url.getPath());
    }

    static AuthenticationInfo getProxyAuth(String str, int i2) {
        return (AuthenticationInfo) cache.get("p::" + str.toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + i2, null);
    }

    static String getProxyAuthKey(String str, int i2, String str2, AuthScheme authScheme) {
        return "p:" + ((Object) authScheme) + "::" + str.toLowerCase() + CallSiteDescriptor.TOKEN_DELIMITER + i2 + CallSiteDescriptor.TOKEN_DELIMITER + str2;
    }

    static AuthenticationInfo getProxyAuth(String str) {
        AuthenticationInfo authenticationInfo = (AuthenticationInfo) cache.get(str, null);
        if (authenticationInfo == null && requestIsInProgress(str)) {
            authenticationInfo = (AuthenticationInfo) cache.get(str, null);
        }
        return authenticationInfo;
    }

    void addToCache() {
        String strCacheKey = cacheKey(true);
        if (useAuthCache()) {
            cache.put(strCacheKey, this);
            if (supportsPreemptiveAuthorization()) {
                cache.put(cacheKey(false), this);
            }
        }
        endAuthRequest(strCacheKey);
    }

    static void endAuthRequest(String str) {
        if (!serializeAuth) {
            return;
        }
        synchronized (requests) {
            requestCompleted(str);
        }
    }

    void removeFromCache() {
        cache.remove(cacheKey(true), this);
        if (supportsPreemptiveAuthorization()) {
            cache.remove(cacheKey(false), this);
        }
    }

    public String getHeaderName() {
        if (this.type == 's') {
            return "Authorization";
        }
        return "Proxy-authorization";
    }

    String cacheKey(boolean z2) {
        if (z2) {
            return this.type + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) this.authScheme) + CallSiteDescriptor.TOKEN_DELIMITER + this.protocol + CallSiteDescriptor.TOKEN_DELIMITER + this.host + CallSiteDescriptor.TOKEN_DELIMITER + this.port + CallSiteDescriptor.TOKEN_DELIMITER + this.realm;
        }
        return this.type + CallSiteDescriptor.TOKEN_DELIMITER + this.protocol + CallSiteDescriptor.TOKEN_DELIMITER + this.host + CallSiteDescriptor.TOKEN_DELIMITER + this.port;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.pw = new PasswordAuthentication(this.s1, this.s2.toCharArray());
        this.s1 = null;
        this.s2 = null;
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.s1 = this.pw.getUserName();
        this.s2 = new String(this.pw.getPassword());
        objectOutputStream.defaultWriteObject();
    }
}
