package sun.net.www.protocol.http;

import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Authenticator;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.HttpRetryException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.ResponseCache;
import java.net.SecureCacheResponse;
import java.net.SocketPermission;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLPermission;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.net.tftp.TFTP;
import sun.misc.JavaNetHttpCookieAccess;
import sun.misc.SharedSecrets;
import sun.net.ApplicationProxy;
import sun.net.NetProperties;
import sun.net.ProgressSource;
import sun.net.util.IPAddressUtil;
import sun.net.www.HeaderParser;
import sun.net.www.MessageHeader;
import sun.net.www.MeteredStream;
import sun.net.www.ParseUtil;
import sun.net.www.http.ChunkedInputStream;
import sun.net.www.http.ChunkedOutputStream;
import sun.net.www.http.HttpClient;
import sun.net.www.http.PosterOutputStream;
import sun.net.www.protocol.http.DigestAuthentication;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetIntegerAction;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.internal.PAForUserEnc;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/protocol/http/HttpURLConnection.class */
public class HttpURLConnection extends java.net.HttpURLConnection {
    static String HTTP_CONNECT;
    static final String version;
    public static final String userAgent;
    static final int defaultmaxRedirects = 20;
    static final int maxRedirects;
    static final boolean validateProxy;
    static final boolean validateServer;
    static final Set<String> disabledProxyingSchemes;
    static final Set<String> disabledTunnelingSchemes;
    private StreamingOutputStream strOutputStream;
    private static final String RETRY_MSG1 = "cannot retry due to proxy authentication, in streaming mode";
    private static final String RETRY_MSG2 = "cannot retry due to server authentication, in streaming mode";
    private static final String RETRY_MSG3 = "cannot retry due to redirection, in streaming mode";
    private static boolean enableESBuffer;
    private static int timeout4ESBuffer;
    private static int bufSize4ES;
    private static final boolean allowRestrictedHeaders;
    private static final Set<String> restrictedHeaderSet;
    private static final String[] restrictedHeaders;
    static final String httpVersion = "HTTP/1.1";
    static final String acceptString = "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
    private static final String[] EXCLUDE_HEADERS;
    private static final String[] EXCLUDE_HEADERS2;
    protected HttpClient http;
    protected Handler handler;
    protected Proxy instProxy;
    private CookieHandler cookieHandler;
    private final ResponseCache cacheHandler;
    protected CacheResponse cachedResponse;
    private MessageHeader cachedHeaders;
    private InputStream cachedInputStream;
    protected PrintStream ps;
    private InputStream errorStream;
    private boolean setUserCookies;
    private String userCookies;
    private String userCookies2;

    @Deprecated
    private static HttpAuthenticator defaultAuth;
    private MessageHeader requests;
    private MessageHeader userHeaders;
    private boolean connecting;
    String domain;
    DigestAuthentication.Parameters digestparams;
    AuthenticationInfo currentProxyCredentials;
    AuthenticationInfo currentServerCredentials;
    boolean needToCheck;
    private boolean doingNTLM2ndStage;
    private boolean doingNTLMp2ndStage;
    private boolean tryTransparentNTLMServer;
    private boolean tryTransparentNTLMProxy;
    private boolean useProxyResponseCode;
    private Object authObj;
    boolean isUserServerAuth;
    boolean isUserProxyAuth;
    String serverAuthKey;
    String proxyAuthKey;
    protected ProgressSource pi;
    private MessageHeader responses;
    private InputStream inputStream;
    private PosterOutputStream poster;
    private boolean setRequests;
    private boolean failedOnce;
    private Exception rememberedException;
    private HttpClient reuseClient;
    private TunnelState tunnelState;
    private int connectTimeout;
    private int readTimeout;
    private SocketPermission socketPermission;
    private static final PlatformLogger logger;
    String requestURI;
    byte[] cdata;
    private static final String SET_COOKIE = "set-cookie";
    private static final String SET_COOKIE2 = "set-cookie2";
    private Map<String, List<String>> filteredHeaders;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/net/www/protocol/http/HttpURLConnection$TunnelState.class */
    public enum TunnelState {
        NONE,
        SETUP,
        TUNNELING
    }

    static {
        String str;
        $assertionsDisabled = !HttpURLConnection.class.desiredAssertionStatus();
        HTTP_CONNECT = "CONNECT";
        enableESBuffer = false;
        timeout4ESBuffer = 0;
        bufSize4ES = 0;
        restrictedHeaders = new String[]{"Access-Control-Request-Headers", "Access-Control-Request-Method", "Connection", "Content-Length", "Content-Transfer-Encoding", "Host", "Keep-Alive", "Origin", "Trailer", "Transfer-Encoding", "Upgrade", "Via"};
        maxRedirects = ((Integer) AccessController.doPrivileged(new GetIntegerAction("http.maxRedirects", 20))).intValue();
        version = (String) AccessController.doPrivileged(new GetPropertyAction("java.version"));
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("http.agent"));
        if (str2 == null) {
            str = "Java/" + version;
        } else {
            str = str2 + " Java/" + version;
        }
        userAgent = str;
        disabledTunnelingSchemes = schemesListToSet(getNetProperty("jdk.http.auth.tunneling.disabledSchemes"));
        disabledProxyingSchemes = schemesListToSet(getNetProperty("jdk.http.auth.proxying.disabledSchemes"));
        validateProxy = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("http.auth.digest.validateProxy"))).booleanValue();
        validateServer = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("http.auth.digest.validateServer"))).booleanValue();
        enableESBuffer = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.net.http.errorstream.enableBuffering"))).booleanValue();
        timeout4ESBuffer = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.net.http.errorstream.timeout", 300))).intValue();
        if (timeout4ESBuffer <= 0) {
            timeout4ESBuffer = 300;
        }
        bufSize4ES = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.net.http.errorstream.bufferSize", 4096))).intValue();
        if (bufSize4ES <= 0) {
            bufSize4ES = 4096;
        }
        allowRestrictedHeaders = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.net.http.allowRestrictedHeaders"))).booleanValue();
        if (!allowRestrictedHeaders) {
            restrictedHeaderSet = new HashSet(restrictedHeaders.length);
            for (int i2 = 0; i2 < restrictedHeaders.length; i2++) {
                restrictedHeaderSet.add(restrictedHeaders[i2].toLowerCase());
            }
        } else {
            restrictedHeaderSet = null;
        }
        EXCLUDE_HEADERS = new String[]{"Proxy-Authorization", "Authorization"};
        EXCLUDE_HEADERS2 = new String[]{"Proxy-Authorization", "Authorization", "Cookie", "Cookie2"};
        logger = PlatformLogger.getLogger("sun.net.www.protocol.http.HttpURLConnection");
    }

    private static String getNetProperty(String str) {
        return (String) AccessController.doPrivileged(() -> {
            return NetProperties.get(str);
        });
    }

    private static Set<String> schemesListToSet(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        for (String str2 : str.split("\\s*,\\s*")) {
            hashSet.add(str2.toLowerCase(Locale.ROOT));
        }
        return hashSet;
    }

    private static PasswordAuthentication privilegedRequestPasswordAuthentication(final String str, final InetAddress inetAddress, final int i2, final String str2, final String str3, final String str4, final URL url, final Authenticator.RequestorType requestorType) {
        return (PasswordAuthentication) AccessController.doPrivileged(new PrivilegedAction<PasswordAuthentication>() { // from class: sun.net.www.protocol.http.HttpURLConnection.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PasswordAuthentication run2() {
                if (HttpURLConnection.logger.isLoggable(PlatformLogger.Level.FINEST)) {
                    HttpURLConnection.logger.finest("Requesting Authentication: host =" + str + " url = " + ((Object) url));
                }
                PasswordAuthentication passwordAuthenticationRequestPasswordAuthentication = Authenticator.requestPasswordAuthentication(str, inetAddress, i2, str2, str3, str4, url, requestorType);
                if (HttpURLConnection.logger.isLoggable(PlatformLogger.Level.FINEST)) {
                    HttpURLConnection.logger.finest("Authentication returned: " + (passwordAuthenticationRequestPasswordAuthentication != null ? passwordAuthenticationRequestPasswordAuthentication.toString() : FXMLLoader.NULL_KEYWORD));
                }
                return passwordAuthenticationRequestPasswordAuthentication;
            }
        });
    }

    private boolean isRestrictedHeader(String str, String str2) {
        if (allowRestrictedHeaders) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        if (restrictedHeaderSet.contains(lowerCase)) {
            if (lowerCase.equals("connection") && str2.equalsIgnoreCase("close")) {
                return false;
            }
            return true;
        }
        if (lowerCase.startsWith("sec-")) {
            return true;
        }
        return false;
    }

    private boolean isExternalMessageHeaderAllowed(String str, String str2) {
        checkMessageHeader(str, str2);
        if (!isRestrictedHeader(str, str2)) {
            return true;
        }
        return false;
    }

    public static PlatformLogger getHttpLogger() {
        return logger;
    }

    public Object authObj() {
        return this.authObj;
    }

    public void authObj(Object obj) {
        this.authObj = obj;
    }

    private void checkMessageHeader(String str, String str2) {
        char cCharAt;
        int iIndexOf = str.indexOf(10);
        int iIndexOf2 = str.indexOf(58);
        if (iIndexOf != -1 || iIndexOf2 != -1) {
            throw new IllegalArgumentException("Illegal character(s) in message header field: " + str);
        }
        if (str2 == null) {
            return;
        }
        int iIndexOf3 = str2.indexOf(10);
        while (true) {
            int i2 = iIndexOf3;
            if (i2 != -1) {
                int i3 = i2 + 1;
                if (i3 >= str2.length() || !((cCharAt = str2.charAt(i3)) == ' ' || cCharAt == '\t')) {
                    break;
                } else {
                    iIndexOf3 = str2.indexOf(10, i3);
                }
            } else {
                return;
            }
        }
        throw new IllegalArgumentException("Illegal character(s) in message header value: " + str2);
    }

    @Override // java.net.HttpURLConnection
    public synchronized void setRequestMethod(String str) throws ProtocolException {
        if (this.connecting) {
            throw new IllegalStateException("connect in progress");
        }
        super.setRequestMethod(str);
    }

    private void writeRequests() throws IOException {
        if (this.http.usingProxy && tunnelState() != TunnelState.TUNNELING) {
            setPreemptiveProxyAuthentication(this.requests);
        }
        if (!this.setRequests) {
            if (!this.failedOnce) {
                checkURLFile();
                this.requests.prepend(this.method + " " + getRequestURI() + " " + httpVersion, null);
            }
            if (!getUseCaches()) {
                this.requests.setIfNotSet("Cache-Control", "no-cache");
                this.requests.setIfNotSet("Pragma", "no-cache");
            }
            this.requests.setIfNotSet("User-Agent", userAgent);
            int port = this.url.getPort();
            String host = this.url.getHost();
            if (port != -1 && port != this.url.getDefaultPort()) {
                host = host + CallSiteDescriptor.TOKEN_DELIMITER + String.valueOf(port);
            }
            String strFindValue = this.requests.findValue("Host");
            if (strFindValue == null || (!strFindValue.equalsIgnoreCase(host) && !checkSetHost())) {
                this.requests.set("Host", host);
            }
            this.requests.setIfNotSet(XIncludeHandler.HTTP_ACCEPT, acceptString);
            if (!this.failedOnce && this.http.getHttpKeepAliveSet()) {
                if (this.http.usingProxy && tunnelState() != TunnelState.TUNNELING) {
                    this.requests.setIfNotSet("Proxy-Connection", "keep-alive");
                } else {
                    this.requests.setIfNotSet("Connection", "keep-alive");
                }
            } else {
                this.requests.setIfNotSet("Connection", "close");
            }
            long ifModifiedSince = getIfModifiedSince();
            if (ifModifiedSince != 0) {
                Date date = new Date(ifModifiedSince);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                this.requests.setIfNotSet("If-Modified-Since", simpleDateFormat.format(date));
            }
            AuthenticationInfo serverAuth = AuthenticationInfo.getServerAuth(this.url);
            if (serverAuth != null && serverAuth.supportsPreemptiveAuthorization()) {
                this.requests.setIfNotSet(serverAuth.getHeaderName(), serverAuth.getHeaderValue(this.url, this.method));
                this.currentServerCredentials = serverAuth;
            }
            if (!this.method.equals("PUT") && (this.poster != null || streaming())) {
                this.requests.setIfNotSet("Content-type", "application/x-www-form-urlencoded");
            }
            boolean z2 = false;
            if (streaming()) {
                if (this.chunkLength != -1) {
                    this.requests.set("Transfer-Encoding", "chunked");
                    z2 = true;
                } else if (this.fixedContentLengthLong != -1) {
                    this.requests.set("Content-Length", String.valueOf(this.fixedContentLengthLong));
                } else if (this.fixedContentLength != -1) {
                    this.requests.set("Content-Length", String.valueOf(this.fixedContentLength));
                }
            } else if (this.poster != null) {
                synchronized (this.poster) {
                    this.poster.close();
                    this.requests.set("Content-Length", String.valueOf(this.poster.size()));
                }
            }
            if (!z2 && this.requests.findValue("Transfer-Encoding") != null) {
                this.requests.remove("Transfer-Encoding");
                if (logger.isLoggable(PlatformLogger.Level.WARNING)) {
                    logger.warning("use streaming mode for chunked encoding");
                }
            }
            setCookieHeader();
            this.setRequests = true;
        }
        if (logger.isLoggable(PlatformLogger.Level.FINE)) {
            logger.fine(this.requests.toString());
        }
        this.http.writeRequests(this.requests, this.poster, streaming());
        if (this.ps.checkError()) {
            String proxyHostUsed = this.http.getProxyHostUsed();
            int proxyPortUsed = this.http.getProxyPortUsed();
            disconnectInternal();
            if (this.failedOnce) {
                throw new IOException("Error writing to server");
            }
            this.failedOnce = true;
            if (proxyHostUsed != null) {
                setProxiedClient(this.url, proxyHostUsed, proxyPortUsed);
            } else {
                setNewClient(this.url);
            }
            this.ps = (PrintStream) this.http.getOutputStream();
            this.connected = true;
            this.responses = new MessageHeader();
            this.setRequests = false;
            writeRequests();
        }
    }

    private boolean checkSetHost() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String name = securityManager.getClass().getName();
            if (name.equals("sun.plugin2.applet.AWTAppletSecurityManager") || name.equals("sun.plugin2.applet.FXAppletSecurityManager") || name.equals("com.sun.javaws.security.JavaWebStartSecurity") || name.equals("sun.plugin.security.ActivatorSecurityManager")) {
                try {
                    securityManager.checkConnect(this.url.toExternalForm(), -2);
                    return true;
                } catch (SecurityException e2) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private void checkURLFile() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String name = securityManager.getClass().getName();
            if (name.equals("sun.plugin2.applet.AWTAppletSecurityManager") || name.equals("sun.plugin2.applet.FXAppletSecurityManager") || name.equals("com.sun.javaws.security.JavaWebStartSecurity") || name.equals("sun.plugin.security.ActivatorSecurityManager")) {
                try {
                    securityManager.checkConnect(this.url.toExternalForm(), -3);
                } catch (SecurityException e2) {
                    throw new SecurityException("denied access outside a permitted URL subpath", e2);
                }
            }
        }
    }

    protected void setNewClient(URL url) throws IOException {
        setNewClient(url, false);
    }

    protected void setNewClient(URL url, boolean z2) throws IOException {
        this.http = HttpClient.New(url, null, -1, z2, this.connectTimeout, this);
        this.http.setReadTimeout(this.readTimeout);
    }

    protected void setProxiedClient(URL url, String str, int i2) throws IOException {
        setProxiedClient(url, str, i2, false);
    }

    protected void setProxiedClient(URL url, String str, int i2, boolean z2) throws IOException {
        proxiedConnect(url, str, i2, z2);
    }

    protected void proxiedConnect(URL url, String str, int i2, boolean z2) throws IOException {
        this.http = HttpClient.New(url, str, i2, z2, this.connectTimeout, this);
        this.http.setReadTimeout(this.readTimeout);
    }

    protected HttpURLConnection(URL url, Handler handler) throws IOException {
        this(url, (Proxy) null, handler);
    }

    private static String checkHost(String str) throws IOException {
        if (str != null && str.indexOf(10) > -1) {
            throw new MalformedURLException("Illegal character in host");
        }
        return str;
    }

    public HttpURLConnection(URL url, String str, int i2) throws IOException {
        this(url, new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(checkHost(str), i2)));
    }

    public HttpURLConnection(URL url, Proxy proxy) throws IOException {
        this(url, proxy, new Handler());
    }

    private static URL checkURL(URL url) throws IOException {
        if (url != null && url.toExternalForm().indexOf(10) > -1) {
            throw new MalformedURLException("Illegal character in URL");
        }
        String strCheckAuthority = IPAddressUtil.checkAuthority(url);
        if (strCheckAuthority != null) {
            throw new MalformedURLException(strCheckAuthority);
        }
        return url;
    }

    protected HttpURLConnection(URL url, Proxy proxy, Handler handler) throws IOException {
        super(checkURL(url));
        this.ps = null;
        this.errorStream = null;
        this.setUserCookies = true;
        this.userCookies = null;
        this.userCookies2 = null;
        this.connecting = false;
        this.currentProxyCredentials = null;
        this.currentServerCredentials = null;
        this.needToCheck = true;
        this.doingNTLM2ndStage = false;
        this.doingNTLMp2ndStage = false;
        this.tryTransparentNTLMServer = true;
        this.tryTransparentNTLMProxy = true;
        this.useProxyResponseCode = false;
        this.inputStream = null;
        this.poster = null;
        this.setRequests = false;
        this.failedOnce = false;
        this.rememberedException = null;
        this.reuseClient = null;
        this.tunnelState = TunnelState.NONE;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        this.requestURI = null;
        this.cdata = new byte[128];
        this.requests = new MessageHeader();
        this.responses = new MessageHeader();
        this.userHeaders = new MessageHeader();
        this.handler = handler;
        this.instProxy = proxy;
        if (this.instProxy instanceof ApplicationProxy) {
            try {
                this.cookieHandler = CookieHandler.getDefault();
            } catch (SecurityException e2) {
            }
        } else {
            this.cookieHandler = (CookieHandler) AccessController.doPrivileged(new PrivilegedAction<CookieHandler>() { // from class: sun.net.www.protocol.http.HttpURLConnection.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public CookieHandler run2() {
                    return CookieHandler.getDefault();
                }
            });
        }
        this.cacheHandler = (ResponseCache) AccessController.doPrivileged(new PrivilegedAction<ResponseCache>() { // from class: sun.net.www.protocol.http.HttpURLConnection.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ResponseCache run2() {
                return ResponseCache.getDefault();
            }
        });
    }

    @Deprecated
    public static void setDefaultAuthenticator(HttpAuthenticator httpAuthenticator) {
        defaultAuth = httpAuthenticator;
    }

    public static InputStream openConnectionCheckRedirects(URLConnection uRLConnection) throws IOException {
        InputStream inputStream;
        boolean z2;
        HttpURLConnection httpURLConnection;
        int responseCode;
        int i2 = 0;
        do {
            if (uRLConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) uRLConnection).setInstanceFollowRedirects(false);
            }
            inputStream = uRLConnection.getInputStream();
            z2 = false;
            if ((uRLConnection instanceof HttpURLConnection) && (responseCode = (httpURLConnection = (HttpURLConnection) uRLConnection).getResponseCode()) >= 300 && responseCode <= 307 && responseCode != 306 && responseCode != 304) {
                URL url = httpURLConnection.getURL();
                String headerField = httpURLConnection.getHeaderField("Location");
                URL url2 = null;
                if (headerField != null) {
                    url2 = new URL(url, headerField);
                }
                httpURLConnection.disconnect();
                if (url2 == null || !url.getProtocol().equals(url2.getProtocol()) || url.getPort() != url2.getPort() || !hostsEqual(url, url2) || i2 >= 5) {
                    throw new SecurityException("illegal URL redirect");
                }
                z2 = true;
                uRLConnection = url2.openConnection();
                i2++;
            }
        } while (z2);
        return inputStream;
    }

    private static boolean hostsEqual(URL url, URL url2) {
        final String host = url.getHost();
        final String host2 = url2.getHost();
        if (host == null) {
            return host2 == null;
        }
        if (host2 == null) {
            return false;
        }
        if (host.equalsIgnoreCase(host2)) {
            return true;
        }
        final boolean[] zArr = {false};
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.www.protocol.http.HttpURLConnection.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                try {
                    zArr[0] = InetAddress.getByName(host).equals(InetAddress.getByName(host2));
                    return null;
                } catch (SecurityException | UnknownHostException e2) {
                    return null;
                }
            }
        });
        return zArr[0];
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        synchronized (this) {
            this.connecting = true;
        }
        plainConnect();
    }

    private boolean checkReuseConnection() {
        if (this.connected) {
            return true;
        }
        if (this.reuseClient != null) {
            this.http = this.reuseClient;
            this.http.setReadTimeout(getReadTimeout());
            this.http.reuse = false;
            this.reuseClient = null;
            this.connected = true;
            return true;
        }
        return false;
    }

    private String getHostAndPort(URL url) {
        final String host = url.getHost();
        try {
            host = (String) AccessController.doPrivileged(new PrivilegedExceptionAction<String>() { // from class: sun.net.www.protocol.http.HttpURLConnection.5
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public String run() throws IOException {
                    return InetAddress.getByName(host).getHostAddress();
                }
            });
        } catch (PrivilegedActionException e2) {
        }
        int port = url.getPort();
        if (port == -1) {
            if ("http".equals(url.getProtocol())) {
                return host + ":80";
            }
            return host + ":443";
        }
        return host + CallSiteDescriptor.TOKEN_DELIMITER + Integer.toString(port);
    }

    protected void plainConnect() throws IOException {
        synchronized (this) {
            if (this.connected) {
                return;
            }
            SocketPermission socketPermissionURLtoSocketPermission = URLtoSocketPermission(this.url);
            if (socketPermissionURLtoSocketPermission != null) {
                try {
                    AccessController.doPrivilegedWithCombiner(new PrivilegedExceptionAction<Void>() { // from class: sun.net.www.protocol.http.HttpURLConnection.6
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() throws IOException {
                            HttpURLConnection.this.plainConnect0();
                            return null;
                        }
                    }, (AccessControlContext) null, socketPermissionURLtoSocketPermission);
                    return;
                } catch (PrivilegedActionException e2) {
                    throw ((IOException) e2.getException());
                }
            }
            plainConnect0();
        }
    }

    SocketPermission URLtoSocketPermission(URL url) throws IOException {
        if (this.socketPermission != null) {
            return this.socketPermission;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        SocketPermission socketPermission = new SocketPermission(getHostAndPort(url), SecurityConstants.SOCKET_CONNECT_ACTION);
        try {
            securityManager.checkPermission(new URLPermission(url.getProtocol() + "://" + url.getAuthority() + url.getPath(), getRequestMethod() + CallSiteDescriptor.TOKEN_DELIMITER + getUserSetHeaders().getHeaderNamesInList()));
            this.socketPermission = socketPermission;
            return this.socketPermission;
        } catch (SecurityException e2) {
            return null;
        }
    }

    protected void plainConnect0() throws IOException {
        if (this.cacheHandler != null && getUseCaches()) {
            try {
                URI uri = ParseUtil.toURI(this.url);
                if (uri != null) {
                    this.cachedResponse = this.cacheHandler.get(uri, getRequestMethod(), getUserSetHeaders().getHeaders());
                    if ("https".equalsIgnoreCase(uri.getScheme()) && !(this.cachedResponse instanceof SecureCacheResponse)) {
                        this.cachedResponse = null;
                    }
                    if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                        logger.finest("Cache Request for " + ((Object) uri) + " / " + getRequestMethod());
                        logger.finest("From cache: " + (this.cachedResponse != null ? this.cachedResponse.toString() : FXMLLoader.NULL_KEYWORD));
                    }
                    if (this.cachedResponse != null) {
                        this.cachedHeaders = mapToMessageHeader(this.cachedResponse.getHeaders());
                        this.cachedInputStream = this.cachedResponse.getBody();
                    }
                }
            } catch (IOException e2) {
            }
            if (this.cachedHeaders != null && this.cachedInputStream != null) {
                this.connected = true;
                return;
            }
            this.cachedResponse = null;
        }
        try {
            if (this.instProxy == null) {
                ProxySelector proxySelector = (ProxySelector) AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() { // from class: sun.net.www.protocol.http.HttpURLConnection.7
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public ProxySelector run2() {
                        return ProxySelector.getDefault();
                    }
                });
                if (proxySelector != null) {
                    URI uri2 = ParseUtil.toURI(this.url);
                    if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                        logger.finest("ProxySelector Request for " + ((Object) uri2));
                    }
                    Iterator<Proxy> it = proxySelector.select(uri2).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Proxy next = it.next();
                        try {
                            if (!this.failedOnce) {
                                this.http = getNewHttpClient(this.url, next, this.connectTimeout);
                                this.http.setReadTimeout(this.readTimeout);
                            } else {
                                this.http = getNewHttpClient(this.url, next, this.connectTimeout, false);
                                this.http.setReadTimeout(this.readTimeout);
                            }
                            if (logger.isLoggable(PlatformLogger.Level.FINEST) && next != null) {
                                logger.finest("Proxy used: " + next.toString());
                                break;
                            }
                            break;
                        } catch (IOException e3) {
                            if (next != Proxy.NO_PROXY) {
                                proxySelector.connectFailed(uri2, next.address(), e3);
                                if (!it.hasNext()) {
                                    if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                                        logger.finest("Retrying with proxy: " + next.toString());
                                    }
                                    this.http = getNewHttpClient(this.url, next, this.connectTimeout, false);
                                    this.http.setReadTimeout(this.readTimeout);
                                }
                            } else {
                                throw e3;
                            }
                        }
                    }
                } else if (!this.failedOnce) {
                    this.http = getNewHttpClient(this.url, null, this.connectTimeout);
                    this.http.setReadTimeout(this.readTimeout);
                } else {
                    this.http = getNewHttpClient(this.url, null, this.connectTimeout, false);
                    this.http.setReadTimeout(this.readTimeout);
                }
            } else if (!this.failedOnce) {
                this.http = getNewHttpClient(this.url, this.instProxy, this.connectTimeout);
                this.http.setReadTimeout(this.readTimeout);
            } else {
                this.http = getNewHttpClient(this.url, this.instProxy, this.connectTimeout, false);
                this.http.setReadTimeout(this.readTimeout);
            }
            this.ps = (PrintStream) this.http.getOutputStream();
            this.connected = true;
        } catch (IOException e4) {
            throw e4;
        }
    }

    protected HttpClient getNewHttpClient(URL url, Proxy proxy, int i2) throws IOException {
        return HttpClient.New(url, proxy, i2, this);
    }

    protected HttpClient getNewHttpClient(URL url, Proxy proxy, int i2, boolean z2) throws IOException {
        return HttpClient.New(url, proxy, i2, z2, this);
    }

    private void expect100Continue() throws IOException {
        int readTimeout = this.http.getReadTimeout();
        boolean z2 = false;
        boolean z3 = false;
        if (readTimeout <= 0) {
            this.http.setReadTimeout(TFTP.DEFAULT_TIMEOUT);
            z2 = true;
        }
        try {
            this.http.parseHTTP(this.responses, this.pi, this);
        } catch (SocketTimeoutException e2) {
            if (!z2) {
                throw e2;
            }
            z3 = true;
            this.http.setIgnoreContinue(true);
        }
        if (!z3) {
            String value = this.responses.getValue(0);
            if (value != null && value.startsWith("HTTP/")) {
                String[] strArrSplit = value.split("\\s+");
                this.responseCode = -1;
                try {
                    if (strArrSplit.length > 1) {
                        this.responseCode = Integer.parseInt(strArrSplit[1]);
                    }
                } catch (NumberFormatException e3) {
                }
            }
            if (this.responseCode != 100) {
                throw new ProtocolException("Server rejected operation");
            }
        }
        this.http.setReadTimeout(readTimeout);
        this.responseCode = -1;
        this.responses.reset();
    }

    @Override // java.net.URLConnection
    public synchronized OutputStream getOutputStream() throws IOException {
        this.connecting = true;
        SocketPermission socketPermissionURLtoSocketPermission = URLtoSocketPermission(this.url);
        if (socketPermissionURLtoSocketPermission != null) {
            try {
                return (OutputStream) AccessController.doPrivilegedWithCombiner(new PrivilegedExceptionAction<OutputStream>() { // from class: sun.net.www.protocol.http.HttpURLConnection.8
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public OutputStream run() throws IOException {
                        return HttpURLConnection.this.getOutputStream0();
                    }
                }, (AccessControlContext) null, socketPermissionURLtoSocketPermission);
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }
        return getOutputStream0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized OutputStream getOutputStream0() throws IOException {
        try {
            if (!this.doOutput) {
                throw new ProtocolException("cannot write to a URLConnection if doOutput=false - call setDoOutput(true)");
            }
            if (this.method.equals("GET")) {
                this.method = "POST";
            }
            if ("TRACE".equals(this.method) && "http".equals(this.url.getProtocol())) {
                throw new ProtocolException("HTTP method TRACE doesn't support output");
            }
            if (this.inputStream != null) {
                throw new ProtocolException("Cannot write output after reading input.");
            }
            if (!checkReuseConnection()) {
                connect();
            }
            boolean z2 = false;
            if ("100-Continue".equalsIgnoreCase(this.requests.findValue("Expect")) && streaming()) {
                this.http.setIgnoreContinue(false);
                z2 = true;
            }
            if (streaming() && this.strOutputStream == null) {
                writeRequests();
            }
            if (z2) {
                expect100Continue();
            }
            this.ps = (PrintStream) this.http.getOutputStream();
            if (streaming()) {
                if (this.strOutputStream == null) {
                    if (this.chunkLength != -1) {
                        this.strOutputStream = new StreamingOutputStream(new ChunkedOutputStream(this.ps, this.chunkLength), -1L);
                    } else {
                        long j2 = 0;
                        if (this.fixedContentLengthLong != -1) {
                            j2 = this.fixedContentLengthLong;
                        } else if (this.fixedContentLength != -1) {
                            j2 = this.fixedContentLength;
                        }
                        this.strOutputStream = new StreamingOutputStream(this.ps, j2);
                    }
                }
                return this.strOutputStream;
            }
            if (this.poster == null) {
                this.poster = new PosterOutputStream();
            }
            return this.poster;
        } catch (ProtocolException e2) {
            int i2 = this.responseCode;
            disconnectInternal();
            this.responseCode = i2;
            throw e2;
        } catch (IOException e3) {
            disconnectInternal();
            throw e3;
        } catch (RuntimeException e4) {
            disconnectInternal();
            throw e4;
        }
    }

    public boolean streaming() {
        return (this.fixedContentLength == -1 && this.fixedContentLengthLong == -1 && this.chunkLength == -1) ? false : true;
    }

    private void setCookieHeader() throws IOException {
        if (this.cookieHandler != null) {
            synchronized (this) {
                if (this.setUserCookies) {
                    int key = this.requests.getKey("Cookie");
                    if (key != -1) {
                        this.userCookies = this.requests.getValue(key);
                    }
                    int key2 = this.requests.getKey("Cookie2");
                    if (key2 != -1) {
                        this.userCookies2 = this.requests.getValue(key2);
                    }
                    this.setUserCookies = false;
                }
            }
            this.requests.remove("Cookie");
            this.requests.remove("Cookie2");
            URI uri = ParseUtil.toURI(this.url);
            if (uri != null) {
                if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                    logger.finest("CookieHandler request for " + ((Object) uri));
                }
                Map<String, List<String>> map = this.cookieHandler.get(uri, this.requests.getHeaders(EXCLUDE_HEADERS));
                if (!map.isEmpty()) {
                    if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                        logger.finest("Cookies retrieved: " + map.toString());
                    }
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        String key3 = entry.getKey();
                        if ("Cookie".equalsIgnoreCase(key3) || "Cookie2".equalsIgnoreCase(key3)) {
                            List<String> value = entry.getValue();
                            if (value != null && !value.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                Iterator<String> it = value.iterator();
                                while (it.hasNext()) {
                                    sb.append(it.next()).append(VectorFormat.DEFAULT_SEPARATOR);
                                }
                                try {
                                    this.requests.add(key3, sb.substring(0, sb.length() - 2));
                                } catch (StringIndexOutOfBoundsException e2) {
                                }
                            }
                        }
                    }
                }
            }
            if (this.userCookies != null) {
                int key4 = this.requests.getKey("Cookie");
                if (key4 != -1) {
                    this.requests.set("Cookie", this.requests.getValue(key4) + ";" + this.userCookies);
                } else {
                    this.requests.set("Cookie", this.userCookies);
                }
            }
            if (this.userCookies2 != null) {
                int key5 = this.requests.getKey("Cookie2");
                if (key5 != -1) {
                    this.requests.set("Cookie2", this.requests.getValue(key5) + ";" + this.userCookies2);
                } else {
                    this.requests.set("Cookie2", this.userCookies2);
                }
            }
        }
    }

    @Override // java.net.URLConnection
    public synchronized InputStream getInputStream() throws IOException {
        this.connecting = true;
        SocketPermission socketPermissionURLtoSocketPermission = URLtoSocketPermission(this.url);
        if (socketPermissionURLtoSocketPermission != null) {
            try {
                return (InputStream) AccessController.doPrivilegedWithCombiner(new PrivilegedExceptionAction<InputStream>() { // from class: sun.net.www.protocol.http.HttpURLConnection.9
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public InputStream run() throws IOException {
                        return HttpURLConnection.this.getInputStream0();
                    }
                }, (AccessControlContext) null, socketPermissionURLtoSocketPermission);
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }
        return getInputStream0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:133:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0423 A[Catch: RuntimeException -> 0x0765, IOException -> 0x0774, all -> 0x07c6, TryCatch #6 {IOException -> 0x0774, RuntimeException -> 0x0765, blocks: (B:36:0x00bb, B:38:0x00c2, B:39:0x00c6, B:41:0x00cd, B:50:0x00f2, B:52:0x0107, B:53:0x0121, B:55:0x0136, B:56:0x013a, B:58:0x0157, B:59:0x0164, B:66:0x01a8, B:68:0x01bd, B:69:0x01cb, B:72:0x01d3, B:74:0x01da, B:75:0x01eb, B:76:0x01ec, B:77:0x01fb, B:79:0x0205, B:81:0x021f, B:86:0x0235, B:88:0x0246, B:90:0x027b, B:92:0x028a, B:247:0x073c, B:249:0x0743, B:250:0x0764, B:112:0x031a, B:115:0x0326, B:117:0x032d, B:118:0x033e, B:119:0x033f, B:120:0x034e, B:122:0x0358, B:124:0x0372, B:129:0x0388, B:131:0x0399, B:135:0x03c9, B:137:0x03d4, B:139:0x03de, B:140:0x040b, B:141:0x0410, B:143:0x0423, B:144:0x0431, B:146:0x0441, B:147:0x044f, B:148:0x0450, B:151:0x0466, B:153:0x046e, B:164:0x04d2, B:165:0x04ff, B:167:0x0507, B:168:0x050e, B:155:0x0475, B:157:0x047d, B:159:0x049a, B:162:0x04b0, B:161:0x04a7, B:163:0x04ca, B:171:0x0541, B:173:0x0558, B:174:0x0562, B:176:0x0569, B:179:0x057a, B:181:0x0587, B:183:0x0593, B:184:0x059d, B:186:0x05b0, B:225:0x06a3, B:227:0x06ad, B:236:0x06e1, B:237:0x070c, B:234:0x06d2, B:235:0x06e0, B:238:0x070d, B:210:0x062a, B:212:0x0631, B:214:0x0638, B:216:0x0646, B:218:0x0657, B:220:0x066f, B:222:0x0681, B:224:0x0688, B:194:0x05d1, B:196:0x05d8, B:197:0x05e4, B:180:0x0582, B:93:0x0294, B:95:0x02b4, B:96:0x02c2, B:101:0x02cd, B:103:0x02dd, B:104:0x02eb, B:105:0x02ec, B:107:0x02fc, B:109:0x030b, B:63:0x0186, B:65:0x0192), top: B:286:0x00bb, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0431 A[Catch: RuntimeException -> 0x0765, IOException -> 0x0774, all -> 0x07c6, TryCatch #6 {IOException -> 0x0774, RuntimeException -> 0x0765, blocks: (B:36:0x00bb, B:38:0x00c2, B:39:0x00c6, B:41:0x00cd, B:50:0x00f2, B:52:0x0107, B:53:0x0121, B:55:0x0136, B:56:0x013a, B:58:0x0157, B:59:0x0164, B:66:0x01a8, B:68:0x01bd, B:69:0x01cb, B:72:0x01d3, B:74:0x01da, B:75:0x01eb, B:76:0x01ec, B:77:0x01fb, B:79:0x0205, B:81:0x021f, B:86:0x0235, B:88:0x0246, B:90:0x027b, B:92:0x028a, B:247:0x073c, B:249:0x0743, B:250:0x0764, B:112:0x031a, B:115:0x0326, B:117:0x032d, B:118:0x033e, B:119:0x033f, B:120:0x034e, B:122:0x0358, B:124:0x0372, B:129:0x0388, B:131:0x0399, B:135:0x03c9, B:137:0x03d4, B:139:0x03de, B:140:0x040b, B:141:0x0410, B:143:0x0423, B:144:0x0431, B:146:0x0441, B:147:0x044f, B:148:0x0450, B:151:0x0466, B:153:0x046e, B:164:0x04d2, B:165:0x04ff, B:167:0x0507, B:168:0x050e, B:155:0x0475, B:157:0x047d, B:159:0x049a, B:162:0x04b0, B:161:0x04a7, B:163:0x04ca, B:171:0x0541, B:173:0x0558, B:174:0x0562, B:176:0x0569, B:179:0x057a, B:181:0x0587, B:183:0x0593, B:184:0x059d, B:186:0x05b0, B:225:0x06a3, B:227:0x06ad, B:236:0x06e1, B:237:0x070c, B:234:0x06d2, B:235:0x06e0, B:238:0x070d, B:210:0x062a, B:212:0x0631, B:214:0x0638, B:216:0x0646, B:218:0x0657, B:220:0x066f, B:222:0x0681, B:224:0x0688, B:194:0x05d1, B:196:0x05d8, B:197:0x05e4, B:180:0x0582, B:93:0x0294, B:95:0x02b4, B:96:0x02c2, B:101:0x02cd, B:103:0x02dd, B:104:0x02eb, B:105:0x02ec, B:107:0x02fc, B:109:0x030b, B:63:0x0186, B:65:0x0192), top: B:286:0x00bb, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0461 A[PHI: r15 r17
  0x0461: PHI (r15v2 sun.net.www.protocol.http.AuthenticationInfo) = (r15v1 sun.net.www.protocol.http.AuthenticationInfo), (r15v7 sun.net.www.protocol.http.AuthenticationInfo) binds: [B:114:0x0323, B:142:0x0420] A[DONT_GENERATE, DONT_INLINE]
  0x0461: PHI (r17v2 sun.net.www.protocol.http.AuthenticationHeader) = (r17v1 sun.net.www.protocol.http.AuthenticationHeader), (r17v3 sun.net.www.protocol.http.AuthenticationHeader) binds: [B:114:0x0323, B:142:0x0420] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x027b A[Catch: RuntimeException -> 0x0765, IOException -> 0x0774, all -> 0x07c6, TryCatch #6 {IOException -> 0x0774, RuntimeException -> 0x0765, blocks: (B:36:0x00bb, B:38:0x00c2, B:39:0x00c6, B:41:0x00cd, B:50:0x00f2, B:52:0x0107, B:53:0x0121, B:55:0x0136, B:56:0x013a, B:58:0x0157, B:59:0x0164, B:66:0x01a8, B:68:0x01bd, B:69:0x01cb, B:72:0x01d3, B:74:0x01da, B:75:0x01eb, B:76:0x01ec, B:77:0x01fb, B:79:0x0205, B:81:0x021f, B:86:0x0235, B:88:0x0246, B:90:0x027b, B:92:0x028a, B:247:0x073c, B:249:0x0743, B:250:0x0764, B:112:0x031a, B:115:0x0326, B:117:0x032d, B:118:0x033e, B:119:0x033f, B:120:0x034e, B:122:0x0358, B:124:0x0372, B:129:0x0388, B:131:0x0399, B:135:0x03c9, B:137:0x03d4, B:139:0x03de, B:140:0x040b, B:141:0x0410, B:143:0x0423, B:144:0x0431, B:146:0x0441, B:147:0x044f, B:148:0x0450, B:151:0x0466, B:153:0x046e, B:164:0x04d2, B:165:0x04ff, B:167:0x0507, B:168:0x050e, B:155:0x0475, B:157:0x047d, B:159:0x049a, B:162:0x04b0, B:161:0x04a7, B:163:0x04ca, B:171:0x0541, B:173:0x0558, B:174:0x0562, B:176:0x0569, B:179:0x057a, B:181:0x0587, B:183:0x0593, B:184:0x059d, B:186:0x05b0, B:225:0x06a3, B:227:0x06ad, B:236:0x06e1, B:237:0x070c, B:234:0x06d2, B:235:0x06e0, B:238:0x070d, B:210:0x062a, B:212:0x0631, B:214:0x0638, B:216:0x0646, B:218:0x0657, B:220:0x066f, B:222:0x0681, B:224:0x0688, B:194:0x05d1, B:196:0x05d8, B:197:0x05e4, B:180:0x0582, B:93:0x0294, B:95:0x02b4, B:96:0x02c2, B:101:0x02cd, B:103:0x02dd, B:104:0x02eb, B:105:0x02ec, B:107:0x02fc, B:109:0x030b, B:63:0x0186, B:65:0x0192), top: B:286:0x00bb, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0294 A[Catch: RuntimeException -> 0x0765, IOException -> 0x0774, all -> 0x07c6, TryCatch #6 {IOException -> 0x0774, RuntimeException -> 0x0765, blocks: (B:36:0x00bb, B:38:0x00c2, B:39:0x00c6, B:41:0x00cd, B:50:0x00f2, B:52:0x0107, B:53:0x0121, B:55:0x0136, B:56:0x013a, B:58:0x0157, B:59:0x0164, B:66:0x01a8, B:68:0x01bd, B:69:0x01cb, B:72:0x01d3, B:74:0x01da, B:75:0x01eb, B:76:0x01ec, B:77:0x01fb, B:79:0x0205, B:81:0x021f, B:86:0x0235, B:88:0x0246, B:90:0x027b, B:92:0x028a, B:247:0x073c, B:249:0x0743, B:250:0x0764, B:112:0x031a, B:115:0x0326, B:117:0x032d, B:118:0x033e, B:119:0x033f, B:120:0x034e, B:122:0x0358, B:124:0x0372, B:129:0x0388, B:131:0x0399, B:135:0x03c9, B:137:0x03d4, B:139:0x03de, B:140:0x040b, B:141:0x0410, B:143:0x0423, B:144:0x0431, B:146:0x0441, B:147:0x044f, B:148:0x0450, B:151:0x0466, B:153:0x046e, B:164:0x04d2, B:165:0x04ff, B:167:0x0507, B:168:0x050e, B:155:0x0475, B:157:0x047d, B:159:0x049a, B:162:0x04b0, B:161:0x04a7, B:163:0x04ca, B:171:0x0541, B:173:0x0558, B:174:0x0562, B:176:0x0569, B:179:0x057a, B:181:0x0587, B:183:0x0593, B:184:0x059d, B:186:0x05b0, B:225:0x06a3, B:227:0x06ad, B:236:0x06e1, B:237:0x070c, B:234:0x06d2, B:235:0x06e0, B:238:0x070d, B:210:0x062a, B:212:0x0631, B:214:0x0638, B:216:0x0646, B:218:0x0657, B:220:0x066f, B:222:0x0681, B:224:0x0688, B:194:0x05d1, B:196:0x05d8, B:197:0x05e4, B:180:0x0582, B:93:0x0294, B:95:0x02b4, B:96:0x02c2, B:101:0x02cd, B:103:0x02dd, B:104:0x02eb, B:105:0x02ec, B:107:0x02fc, B:109:0x030b, B:63:0x0186, B:65:0x0192), top: B:286:0x00bb, outer: #4 }] */
    /* JADX WARN: Type inference failed for: r0v142, types: [java.net.URLConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.io.InputStream getInputStream0() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 2023
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.protocol.http.HttpURLConnection.getInputStream0():java.io.InputStream");
    }

    private IOException getChainedException(final IOException iOException) {
        try {
            final Object[] objArr = {iOException.getMessage()};
            IOException iOException2 = (IOException) AccessController.doPrivileged(new PrivilegedExceptionAction<IOException>() { // from class: sun.net.www.protocol.http.HttpURLConnection.10
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public IOException run() throws Exception {
                    return (IOException) iOException.getClass().getConstructor(String.class).newInstance(objArr);
                }
            });
            iOException2.initCause(iOException);
            return iOException2;
        } catch (Exception e2) {
            return iOException;
        }
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        if (this.connected && this.responseCode >= 400) {
            if (this.errorStream != null) {
                return this.errorStream;
            }
            if (this.inputStream != null) {
                return this.inputStream;
            }
            return null;
        }
        return null;
    }

    private AuthenticationInfo resetProxyAuthentication(AuthenticationInfo authenticationInfo, AuthenticationHeader authenticationHeader) throws IOException {
        String headerValue;
        if (authenticationInfo != null && authenticationInfo.getAuthScheme() != AuthScheme.NTLM) {
            if (authenticationInfo.isAuthorizationStale(authenticationHeader.raw())) {
                if (authenticationInfo instanceof DigestAuthentication) {
                    DigestAuthentication digestAuthentication = (DigestAuthentication) authenticationInfo;
                    if (tunnelState() == TunnelState.SETUP) {
                        headerValue = digestAuthentication.getHeaderValue(connectRequestURI(this.url), HTTP_CONNECT);
                    } else {
                        headerValue = digestAuthentication.getHeaderValue(getRequestURI(), this.method);
                    }
                } else {
                    headerValue = authenticationInfo.getHeaderValue(this.url, this.method);
                }
                this.requests.set(authenticationInfo.getHeaderName(), headerValue);
                this.currentProxyCredentials = authenticationInfo;
                return authenticationInfo;
            }
            authenticationInfo.removeFromCache();
        }
        AuthenticationInfo httpProxyAuthentication = getHttpProxyAuthentication(authenticationHeader);
        this.currentProxyCredentials = httpProxyAuthentication;
        return httpProxyAuthentication;
    }

    TunnelState tunnelState() {
        return this.tunnelState;
    }

    public void setTunnelState(TunnelState tunnelState) {
        this.tunnelState = tunnelState;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x015c A[Catch: all -> 0x0242, TryCatch #0 {all -> 0x0242, blocks: (B:3:0x0025, B:4:0x002c, B:6:0x0033, B:7:0x0040, B:9:0x0065, B:10:0x0072, B:12:0x007f, B:14:0x008b, B:15:0x00a1, B:17:0x00cd, B:18:0x00dc, B:20:0x00e6, B:22:0x0100, B:27:0x0116, B:29:0x0127, B:31:0x015c, B:33:0x016b, B:46:0x01ec, B:48:0x01f3, B:54:0x0208, B:55:0x020f, B:56:0x0230, B:34:0x0187, B:36:0x01a7, B:37:0x01b5, B:38:0x01b6, B:41:0x01c8, B:44:0x01d4, B:45:0x01de), top: B:70:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0187 A[Catch: all -> 0x0242, TryCatch #0 {all -> 0x0242, blocks: (B:3:0x0025, B:4:0x002c, B:6:0x0033, B:7:0x0040, B:9:0x0065, B:10:0x0072, B:12:0x007f, B:14:0x008b, B:15:0x00a1, B:17:0x00cd, B:18:0x00dc, B:20:0x00e6, B:22:0x0100, B:27:0x0116, B:29:0x0127, B:31:0x015c, B:33:0x016b, B:46:0x01ec, B:48:0x01f3, B:54:0x0208, B:55:0x020f, B:56:0x0230, B:34:0x0187, B:36:0x01a7, B:37:0x01b5, B:38:0x01b6, B:41:0x01c8, B:44:0x01d4, B:45:0x01de), top: B:70:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0208 A[Catch: all -> 0x0242, TryCatch #0 {all -> 0x0242, blocks: (B:3:0x0025, B:4:0x002c, B:6:0x0033, B:7:0x0040, B:9:0x0065, B:10:0x0072, B:12:0x007f, B:14:0x008b, B:15:0x00a1, B:17:0x00cd, B:18:0x00dc, B:20:0x00e6, B:22:0x0100, B:27:0x0116, B:29:0x0127, B:31:0x015c, B:33:0x016b, B:46:0x01ec, B:48:0x01f3, B:54:0x0208, B:55:0x020f, B:56:0x0230, B:34:0x0187, B:36:0x01a7, B:37:0x01b5, B:38:0x01b6, B:41:0x01c8, B:44:0x01d4, B:45:0x01de), top: B:70:0x0025 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void doTunneling() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 611
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.protocol.http.HttpURLConnection.doTunneling():void");
    }

    static String connectRequestURI(URL url) {
        String host = url.getHost();
        int port = url.getPort();
        return host + CallSiteDescriptor.TOKEN_DELIMITER + (port != -1 ? port : url.getDefaultPort());
    }

    private void sendCONNECTRequest() throws IOException {
        int port = this.url.getPort();
        this.requests.set(0, HTTP_CONNECT + " " + connectRequestURI(this.url) + " " + httpVersion, null);
        this.requests.setIfNotSet("User-Agent", userAgent);
        String host = this.url.getHost();
        if (port != -1 && port != this.url.getDefaultPort()) {
            host = host + CallSiteDescriptor.TOKEN_DELIMITER + String.valueOf(port);
        }
        this.requests.setIfNotSet("Host", host);
        this.requests.setIfNotSet(XIncludeHandler.HTTP_ACCEPT, acceptString);
        if (this.http.getHttpKeepAliveSet()) {
            this.requests.setIfNotSet("Proxy-Connection", "keep-alive");
        }
        setPreemptiveProxyAuthentication(this.requests);
        if (logger.isLoggable(PlatformLogger.Level.FINE)) {
            logger.fine(this.requests.toString());
        }
        this.http.writeRequests(this.requests, null);
    }

    private void setPreemptiveProxyAuthentication(MessageHeader messageHeader) throws IOException {
        String headerValue;
        AuthenticationInfo proxyAuth = AuthenticationInfo.getProxyAuth(this.http.getProxyHostUsed(), this.http.getProxyPortUsed());
        if (proxyAuth != null && proxyAuth.supportsPreemptiveAuthorization()) {
            if (proxyAuth instanceof DigestAuthentication) {
                DigestAuthentication digestAuthentication = (DigestAuthentication) proxyAuth;
                if (tunnelState() == TunnelState.SETUP) {
                    headerValue = digestAuthentication.getHeaderValue(connectRequestURI(this.url), HTTP_CONNECT);
                } else {
                    headerValue = digestAuthentication.getHeaderValue(getRequestURI(), this.method);
                }
            } else {
                headerValue = proxyAuth.getHeaderValue(this.url, this.method);
            }
            messageHeader.set(proxyAuth.getHeaderName(), headerValue);
            this.currentProxyCredentials = proxyAuth;
        }
    }

    private AuthenticationInfo getHttpProxyAuthentication(AuthenticationHeader authenticationHeader) throws IOException {
        AuthenticationInfo proxyAuth = null;
        String strRaw = authenticationHeader.raw();
        final String proxyHostUsed = this.http.getProxyHostUsed();
        int proxyPortUsed = this.http.getProxyPortUsed();
        if (proxyHostUsed != null && authenticationHeader.isPresent()) {
            HeaderParser headerParser = authenticationHeader.headerParser();
            String strFindValue = headerParser.findValue("realm");
            String strScheme = authenticationHeader.scheme();
            AuthScheme authScheme = AuthScheme.UNKNOWN;
            if ("basic".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.BASIC;
            } else if ("digest".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.DIGEST;
            } else if ("ntlm".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.NTLM;
                this.doingNTLMp2ndStage = true;
            } else if (PAForUserEnc.AUTH_PACKAGE.equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.KERBEROS;
                this.doingNTLMp2ndStage = true;
            } else if ("Negotiate".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.NEGOTIATE;
                this.doingNTLMp2ndStage = true;
            }
            if (strFindValue == null) {
                strFindValue = "";
            }
            this.proxyAuthKey = AuthenticationInfo.getProxyAuthKey(proxyHostUsed, proxyPortUsed, strFindValue, authScheme);
            proxyAuth = AuthenticationInfo.getProxyAuth(this.proxyAuthKey);
            if (proxyAuth == null) {
                switch (authScheme) {
                    case BASIC:
                        InetAddress inetAddress = null;
                        try {
                            inetAddress = (InetAddress) AccessController.doPrivileged(new PrivilegedExceptionAction<InetAddress>() { // from class: sun.net.www.protocol.http.HttpURLConnection.11
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public InetAddress run() throws UnknownHostException {
                                    return InetAddress.getByName(proxyHostUsed);
                                }
                            });
                        } catch (PrivilegedActionException e2) {
                        }
                        PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication = privilegedRequestPasswordAuthentication(proxyHostUsed, inetAddress, proxyPortUsed, "http", strFindValue, strScheme, this.url, Authenticator.RequestorType.PROXY);
                        if (passwordAuthenticationPrivilegedRequestPasswordAuthentication != null) {
                            proxyAuth = new BasicAuthentication(true, proxyHostUsed, proxyPortUsed, strFindValue, passwordAuthenticationPrivilegedRequestPasswordAuthentication);
                            break;
                        }
                        break;
                    case DIGEST:
                        PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication2 = privilegedRequestPasswordAuthentication(proxyHostUsed, null, proxyPortUsed, this.url.getProtocol(), strFindValue, strScheme, this.url, Authenticator.RequestorType.PROXY);
                        if (passwordAuthenticationPrivilegedRequestPasswordAuthentication2 != null) {
                            proxyAuth = new DigestAuthentication(true, proxyHostUsed, proxyPortUsed, strFindValue, strScheme, passwordAuthenticationPrivilegedRequestPasswordAuthentication2, new DigestAuthentication.Parameters());
                            break;
                        }
                        break;
                    case NTLM:
                        if (NTLMAuthenticationProxy.supported) {
                            if (this.tryTransparentNTLMProxy) {
                                this.tryTransparentNTLMProxy = NTLMAuthenticationProxy.supportsTransparentAuth;
                                if (this.tryTransparentNTLMProxy && this.useProxyResponseCode) {
                                    this.tryTransparentNTLMProxy = false;
                                }
                            }
                            PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication3 = null;
                            if (this.tryTransparentNTLMProxy) {
                                logger.finest("Trying Transparent NTLM authentication");
                            } else {
                                passwordAuthenticationPrivilegedRequestPasswordAuthentication3 = privilegedRequestPasswordAuthentication(proxyHostUsed, null, proxyPortUsed, this.url.getProtocol(), "", strScheme, this.url, Authenticator.RequestorType.PROXY);
                                validateNTLMCredentials(passwordAuthenticationPrivilegedRequestPasswordAuthentication3);
                            }
                            if (this.tryTransparentNTLMProxy || (!this.tryTransparentNTLMProxy && passwordAuthenticationPrivilegedRequestPasswordAuthentication3 != null)) {
                                proxyAuth = NTLMAuthenticationProxy.proxy.create(true, proxyHostUsed, proxyPortUsed, passwordAuthenticationPrivilegedRequestPasswordAuthentication3);
                            }
                            this.tryTransparentNTLMProxy = false;
                            break;
                        }
                        break;
                    case NEGOTIATE:
                        proxyAuth = new NegotiateAuthentication(new HttpCallerInfo(authenticationHeader.getHttpCallerInfo(), "Negotiate"));
                        break;
                    case KERBEROS:
                        proxyAuth = new NegotiateAuthentication(new HttpCallerInfo(authenticationHeader.getHttpCallerInfo(), PAForUserEnc.AUTH_PACKAGE));
                        break;
                    case UNKNOWN:
                        if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                            logger.finest("Unknown/Unsupported authentication scheme: " + strScheme);
                        }
                    default:
                        throw new AssertionError((Object) "should not reach here");
                }
            }
            if (proxyAuth == null && defaultAuth != null && defaultAuth.schemeSupported(strScheme)) {
                try {
                    String strAuthString = defaultAuth.authString(new URL("http", proxyHostUsed, proxyPortUsed, "/"), strScheme, strFindValue);
                    if (strAuthString != null) {
                        proxyAuth = new BasicAuthentication(true, proxyHostUsed, proxyPortUsed, strFindValue, strAuthString);
                    }
                } catch (MalformedURLException e3) {
                }
            }
            if (proxyAuth != null && !proxyAuth.setHeaders(this, headerParser, strRaw)) {
                proxyAuth = null;
            }
        }
        if (logger.isLoggable(PlatformLogger.Level.FINER)) {
            logger.finer("Proxy Authentication for " + authenticationHeader.toString() + " returned " + (proxyAuth != null ? proxyAuth.toString() : FXMLLoader.NULL_KEYWORD));
        }
        return proxyAuth;
    }

    private AuthenticationInfo getServerAuthentication(AuthenticationHeader authenticationHeader) throws IOException {
        String strAuthString;
        URL url;
        AuthenticationInfo serverAuth = null;
        String strRaw = authenticationHeader.raw();
        if (authenticationHeader.isPresent()) {
            HeaderParser headerParser = authenticationHeader.headerParser();
            String strFindValue = headerParser.findValue("realm");
            String strScheme = authenticationHeader.scheme();
            AuthScheme authScheme = AuthScheme.UNKNOWN;
            if ("basic".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.BASIC;
            } else if ("digest".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.DIGEST;
            } else if ("ntlm".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.NTLM;
                this.doingNTLM2ndStage = true;
            } else if (PAForUserEnc.AUTH_PACKAGE.equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.KERBEROS;
                this.doingNTLM2ndStage = true;
            } else if ("Negotiate".equalsIgnoreCase(strScheme)) {
                authScheme = AuthScheme.NEGOTIATE;
                this.doingNTLM2ndStage = true;
            }
            this.domain = headerParser.findValue("domain");
            if (strFindValue == null) {
                strFindValue = "";
            }
            this.serverAuthKey = AuthenticationInfo.getServerAuthKey(this.url, strFindValue, authScheme);
            serverAuth = AuthenticationInfo.getServerAuth(this.serverAuthKey);
            InetAddress byName = null;
            if (serverAuth == null) {
                try {
                    byName = InetAddress.getByName(this.url.getHost());
                } catch (UnknownHostException e2) {
                }
            }
            int port = this.url.getPort();
            if (port == -1) {
                port = this.url.getDefaultPort();
            }
            if (serverAuth == null) {
                switch (authScheme) {
                    case BASIC:
                        PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication = privilegedRequestPasswordAuthentication(this.url.getHost(), byName, port, this.url.getProtocol(), strFindValue, strScheme, this.url, Authenticator.RequestorType.SERVER);
                        if (passwordAuthenticationPrivilegedRequestPasswordAuthentication != null) {
                            serverAuth = new BasicAuthentication(false, this.url, strFindValue, passwordAuthenticationPrivilegedRequestPasswordAuthentication);
                            break;
                        }
                        break;
                    case DIGEST:
                        PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication2 = privilegedRequestPasswordAuthentication(this.url.getHost(), byName, port, this.url.getProtocol(), strFindValue, strScheme, this.url, Authenticator.RequestorType.SERVER);
                        if (passwordAuthenticationPrivilegedRequestPasswordAuthentication2 != null) {
                            this.digestparams = new DigestAuthentication.Parameters();
                            serverAuth = new DigestAuthentication(false, this.url, strFindValue, strScheme, passwordAuthenticationPrivilegedRequestPasswordAuthentication2, this.digestparams);
                            break;
                        }
                        break;
                    case NTLM:
                        if (NTLMAuthenticationProxy.supported) {
                            try {
                                url = new URL(this.url, "/");
                            } catch (Exception e3) {
                                url = this.url;
                            }
                            if (this.tryTransparentNTLMServer) {
                                this.tryTransparentNTLMServer = NTLMAuthenticationProxy.supportsTransparentAuth;
                                if (this.tryTransparentNTLMServer) {
                                    this.tryTransparentNTLMServer = NTLMAuthenticationProxy.isTrustedSite(this.url);
                                }
                            }
                            PasswordAuthentication passwordAuthenticationPrivilegedRequestPasswordAuthentication3 = null;
                            if (this.tryTransparentNTLMServer) {
                                logger.finest("Trying Transparent NTLM authentication");
                            } else {
                                passwordAuthenticationPrivilegedRequestPasswordAuthentication3 = privilegedRequestPasswordAuthentication(this.url.getHost(), byName, port, this.url.getProtocol(), "", strScheme, this.url, Authenticator.RequestorType.SERVER);
                                validateNTLMCredentials(passwordAuthenticationPrivilegedRequestPasswordAuthentication3);
                            }
                            if (this.tryTransparentNTLMServer || (!this.tryTransparentNTLMServer && passwordAuthenticationPrivilegedRequestPasswordAuthentication3 != null)) {
                                serverAuth = NTLMAuthenticationProxy.proxy.create(false, url, passwordAuthenticationPrivilegedRequestPasswordAuthentication3);
                            }
                            this.tryTransparentNTLMServer = false;
                            break;
                        }
                        break;
                    case NEGOTIATE:
                        serverAuth = new NegotiateAuthentication(new HttpCallerInfo(authenticationHeader.getHttpCallerInfo(), "Negotiate"));
                        break;
                    case KERBEROS:
                        serverAuth = new NegotiateAuthentication(new HttpCallerInfo(authenticationHeader.getHttpCallerInfo(), PAForUserEnc.AUTH_PACKAGE));
                        break;
                    case UNKNOWN:
                        if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
                            logger.finest("Unknown/Unsupported authentication scheme: " + strScheme);
                        }
                    default:
                        throw new AssertionError((Object) "should not reach here");
                }
            }
            if (serverAuth == null && defaultAuth != null && defaultAuth.schemeSupported(strScheme) && (strAuthString = defaultAuth.authString(this.url, strScheme, strFindValue)) != null) {
                serverAuth = new BasicAuthentication(false, this.url, strFindValue, strAuthString);
            }
            if (serverAuth != null && !serverAuth.setHeaders(this, headerParser, strRaw)) {
                serverAuth = null;
            }
        }
        if (logger.isLoggable(PlatformLogger.Level.FINER)) {
            logger.finer("Server Authentication for " + authenticationHeader.toString() + " returned " + (serverAuth != null ? serverAuth.toString() : FXMLLoader.NULL_KEYWORD));
        }
        return serverAuth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkResponseCredentials(boolean z2) throws IOException {
        try {
            if (!this.needToCheck) {
                return;
            }
            if (validateProxy && this.currentProxyCredentials != null && (this.currentProxyCredentials instanceof DigestAuthentication)) {
                String strFindValue = this.responses.findValue("Proxy-Authentication-Info");
                if (z2 || strFindValue != null) {
                    ((DigestAuthentication) this.currentProxyCredentials).checkResponse(strFindValue, this.method, getRequestURI());
                    this.currentProxyCredentials = null;
                }
            }
            if (validateServer && this.currentServerCredentials != null && (this.currentServerCredentials instanceof DigestAuthentication)) {
                String strFindValue2 = this.responses.findValue("Authentication-Info");
                if (z2 || strFindValue2 != null) {
                    ((DigestAuthentication) this.currentServerCredentials).checkResponse(strFindValue2, this.method, this.url);
                    this.currentServerCredentials = null;
                }
            }
            if (this.currentServerCredentials == null && this.currentProxyCredentials == null) {
                this.needToCheck = false;
            }
        } catch (IOException e2) {
            disconnectInternal();
            this.connected = false;
            throw e2;
        }
    }

    String getRequestURI() throws IOException {
        if (this.requestURI == null) {
            this.requestURI = this.http.getURLFile();
        }
        return this.requestURI;
    }

    private boolean followRedirect() throws IOException {
        final int responseCode;
        final String headerField;
        URL url;
        if (!getInstanceFollowRedirects() || (responseCode = getResponseCode()) < 300 || responseCode > 307 || responseCode == 306 || responseCode == 304 || (headerField = getHeaderField("Location")) == null) {
            return false;
        }
        try {
            url = new URL(headerField);
            if (!this.url.getProtocol().equalsIgnoreCase(url.getProtocol())) {
                return false;
            }
        } catch (MalformedURLException e2) {
            url = new URL(this.url, headerField);
        }
        final URL url2 = url;
        this.socketPermission = null;
        SocketPermission socketPermissionURLtoSocketPermission = URLtoSocketPermission(url);
        if (socketPermissionURLtoSocketPermission != null) {
            try {
                return ((Boolean) AccessController.doPrivilegedWithCombiner(new PrivilegedExceptionAction<Boolean>() { // from class: sun.net.www.protocol.http.HttpURLConnection.12
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Boolean run() throws IOException {
                        return Boolean.valueOf(HttpURLConnection.this.followRedirect0(headerField, responseCode, url2));
                    }
                }, (AccessControlContext) null, socketPermissionURLtoSocketPermission)).booleanValue();
            } catch (PrivilegedActionException e3) {
                throw ((IOException) e3.getException());
            }
        }
        return followRedirect0(headerField, responseCode, url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean followRedirect0(String str, int i2, URL url) throws IOException {
        disconnectInternal();
        if (streaming()) {
            throw new HttpRetryException(RETRY_MSG3, i2, str);
        }
        if (logger.isLoggable(PlatformLogger.Level.FINE)) {
            logger.fine("Redirected from " + ((Object) this.url) + " to " + ((Object) url));
        }
        this.responses = new MessageHeader();
        if (i2 == 305) {
            String host = url.getHost();
            int port = url.getPort();
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(host, port);
            }
            setProxiedClient(this.url, host, port);
            this.requests.set(0, this.method + " " + getRequestURI() + " " + httpVersion, null);
            this.connected = true;
            this.useProxyResponseCode = true;
            return true;
        }
        URL url2 = this.url;
        this.url = url;
        this.requestURI = null;
        if (this.method.equals("POST") && !Boolean.getBoolean("http.strictPostRedirect") && i2 != 307) {
            this.requests = new MessageHeader();
            this.setRequests = false;
            super.setRequestMethod("GET");
            this.poster = null;
            if (!checkReuseConnection()) {
                connect();
            }
            if (!sameDestination(url2, this.url)) {
                this.userCookies = null;
                this.userCookies2 = null;
                return true;
            }
            return true;
        }
        if (!checkReuseConnection()) {
            connect();
        }
        if (this.http != null) {
            this.requests.set(0, this.method + " " + getRequestURI() + " " + httpVersion, null);
            int port2 = this.url.getPort();
            String host2 = this.url.getHost();
            if (port2 != -1 && port2 != this.url.getDefaultPort()) {
                host2 = host2 + CallSiteDescriptor.TOKEN_DELIMITER + String.valueOf(port2);
            }
            this.requests.set("Host", host2);
        }
        if (!sameDestination(url2, this.url)) {
            this.userCookies = null;
            this.userCookies2 = null;
            this.requests.remove("Cookie");
            this.requests.remove("Cookie2");
            this.requests.remove("Authorization");
            AuthenticationInfo serverAuth = AuthenticationInfo.getServerAuth(this.url);
            if (serverAuth != null && serverAuth.supportsPreemptiveAuthorization()) {
                this.requests.setIfNotSet(serverAuth.getHeaderName(), serverAuth.getHeaderValue(this.url, this.method));
                this.currentServerCredentials = serverAuth;
                return true;
            }
            return true;
        }
        return true;
    }

    private static boolean sameDestination(URL url, URL url2) {
        if (!$assertionsDisabled && !url.getProtocol().equalsIgnoreCase(url2.getProtocol())) {
            throw new AssertionError((Object) ("protocols not equal: " + ((Object) url) + " - " + ((Object) url2)));
        }
        if (!url.getHost().equalsIgnoreCase(url2.getHost())) {
            return false;
        }
        int port = url.getPort();
        if (port == -1) {
            port = url.getDefaultPort();
        }
        int port2 = url2.getPort();
        if (port2 == -1) {
            port2 = url2.getDefaultPort();
        }
        if (port != port2) {
            return false;
        }
        return true;
    }

    private void reset() throws IOException {
        int i2;
        this.http.reuse = true;
        this.reuseClient = this.http;
        InputStream inputStream = this.http.getInputStream();
        if (!this.method.equals("HEAD") || this.tunnelState == TunnelState.SETUP) {
            try {
                if ((inputStream instanceof ChunkedInputStream) || (inputStream instanceof MeteredStream)) {
                    while (inputStream.read(this.cdata) > 0) {
                    }
                } else {
                    long j2 = 0;
                    String strFindValue = this.responses.findValue("Content-Length");
                    if (strFindValue != null) {
                        try {
                            j2 = Long.parseLong(strFindValue);
                        } catch (NumberFormatException e2) {
                            j2 = 0;
                        }
                    }
                    long j3 = 0;
                    while (j3 < j2 && (i2 = inputStream.read(this.cdata)) != -1) {
                        j3 += i2;
                    }
                }
                try {
                    if (inputStream instanceof MeteredStream) {
                        inputStream.close();
                    }
                } catch (IOException e3) {
                }
            } catch (IOException e4) {
                this.http.reuse = false;
                this.reuseClient = null;
                disconnectInternal();
                return;
            }
        }
        this.responseCode = -1;
        this.responses = new MessageHeader();
        this.connected = false;
    }

    private void disconnectWeb() throws IOException {
        if (usingProxy() && this.http.isKeepingAlive()) {
            this.responseCode = -1;
            reset();
        } else {
            disconnectInternal();
        }
    }

    private void disconnectInternal() {
        this.responseCode = -1;
        this.inputStream = null;
        if (this.pi != null) {
            this.pi.finishTracking();
            this.pi = null;
        }
        if (this.http != null) {
            this.http.closeServer();
            this.http = null;
            this.connected = false;
        }
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        this.responseCode = -1;
        if (this.pi != null) {
            this.pi.finishTracking();
            this.pi = null;
        }
        if (this.http != null) {
            if (this.inputStream != null) {
                HttpClient httpClient = this.http;
                boolean zIsKeepingAlive = httpClient.isKeepingAlive();
                try {
                    this.inputStream.close();
                } catch (IOException e2) {
                }
                if (zIsKeepingAlive) {
                    httpClient.closeIdleConnection();
                }
            } else {
                this.http.setDoNotRetry(true);
                this.http.closeServer();
            }
            this.http = null;
            this.connected = false;
        }
        this.cachedInputStream = null;
        if (this.cachedHeaders != null) {
            this.cachedHeaders.reset();
        }
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        return (this.http == null || this.http.getProxyHostUsed() == null) ? false : true;
    }

    private String filterHeaderField(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        if (SET_COOKIE.equalsIgnoreCase(str) || SET_COOKIE2.equalsIgnoreCase(str)) {
            if (this.cookieHandler == null || str2.length() == 0) {
                return str2;
            }
            JavaNetHttpCookieAccess javaNetHttpCookieAccess = SharedSecrets.getJavaNetHttpCookieAccess();
            StringBuilder sb = new StringBuilder();
            boolean z2 = false;
            for (HttpCookie httpCookie : javaNetHttpCookieAccess.parse(str2)) {
                if (!httpCookie.isHttpOnly()) {
                    if (z2) {
                        sb.append(',');
                    }
                    sb.append(javaNetHttpCookieAccess.header(httpCookie));
                    z2 = true;
                }
            }
            return sb.length() == 0 ? "" : sb.toString();
        }
        return str2;
    }

    private Map<String, List<String>> getFilteredHeaderFields() {
        Map<String, List<String>> headers;
        if (this.filteredHeaders != null) {
            return this.filteredHeaders;
        }
        HashMap map = new HashMap();
        if (this.cachedHeaders != null) {
            headers = this.cachedHeaders.getHeaders();
        } else {
            headers = this.responses.getHeaders();
        }
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = value.iterator();
            while (it.hasNext()) {
                String strFilterHeaderField = filterHeaderField(key, it.next());
                if (strFilterHeaderField != null) {
                    arrayList.add(strFilterHeaderField);
                }
            }
            if (!arrayList.isEmpty()) {
                map.put(key, Collections.unmodifiableList(arrayList));
            }
        }
        Map<String, List<String>> mapUnmodifiableMap = Collections.unmodifiableMap(map);
        this.filteredHeaders = mapUnmodifiableMap;
        return mapUnmodifiableMap;
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String str) {
        try {
            getInputStream();
        } catch (IOException e2) {
        }
        if (this.cachedHeaders != null) {
            return filterHeaderField(str, this.cachedHeaders.findValue(str));
        }
        return filterHeaderField(str, this.responses.findValue(str));
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        try {
            getInputStream();
        } catch (IOException e2) {
        }
        return getFilteredHeaderFields();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int i2) {
        try {
            getInputStream();
        } catch (IOException e2) {
        }
        if (this.cachedHeaders != null) {
            return filterHeaderField(this.cachedHeaders.getKey(i2), this.cachedHeaders.getValue(i2));
        }
        return filterHeaderField(this.responses.getKey(i2), this.responses.getValue(i2));
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int i2) {
        try {
            getInputStream();
        } catch (IOException e2) {
        }
        if (this.cachedHeaders != null) {
            return this.cachedHeaders.getKey(i2);
        }
        return this.responses.getKey(i2);
    }

    @Override // java.net.URLConnection
    public synchronized void setRequestProperty(String str, String str2) {
        if (this.connected || this.connecting) {
            throw new IllegalStateException("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key is null");
        }
        if (isExternalMessageHeaderAllowed(str, str2)) {
            this.requests.set(str, str2);
            if (!str.equalsIgnoreCase("Content-Type")) {
                this.userHeaders.set(str, str2);
            }
        }
    }

    MessageHeader getUserSetHeaders() {
        return this.userHeaders;
    }

    @Override // java.net.URLConnection
    public synchronized void addRequestProperty(String str, String str2) {
        if (this.connected || this.connecting) {
            throw new IllegalStateException("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key is null");
        }
        if (isExternalMessageHeaderAllowed(str, str2)) {
            this.requests.add(str, str2);
            if (!str.equalsIgnoreCase("Content-Type")) {
                this.userHeaders.add(str, str2);
            }
        }
    }

    public void setAuthenticationProperty(String str, String str2) {
        checkMessageHeader(str, str2);
        this.requests.set(str, str2);
    }

    @Override // java.net.URLConnection
    public synchronized String getRequestProperty(String str) {
        if (str == null) {
            return null;
        }
        for (int i2 = 0; i2 < EXCLUDE_HEADERS.length; i2++) {
            if (str.equalsIgnoreCase(EXCLUDE_HEADERS[i2])) {
                return null;
            }
        }
        if (!this.setUserCookies) {
            if (str.equalsIgnoreCase("Cookie")) {
                return this.userCookies;
            }
            if (str.equalsIgnoreCase("Cookie2")) {
                return this.userCookies2;
            }
        }
        return this.requests.findValue(str);
    }

    @Override // java.net.URLConnection
    public synchronized Map<String, List<String>> getRequestProperties() {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (this.setUserCookies) {
            return this.requests.getHeaders(EXCLUDE_HEADERS);
        }
        HashMap map = null;
        if (this.userCookies != null || this.userCookies2 != null) {
            map = new HashMap();
            if (this.userCookies != null) {
                map.put("Cookie", Arrays.asList(this.userCookies));
            }
            if (this.userCookies2 != null) {
                map.put("Cookie2", Arrays.asList(this.userCookies2));
            }
        }
        return this.requests.filterAndAddHeaders(EXCLUDE_HEADERS2, map);
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.connectTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        if (this.connectTimeout < 0) {
            return 0;
        }
        return this.connectTimeout;
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.readTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        if (this.readTimeout < 0) {
            return 0;
        }
        return this.readTimeout;
    }

    public CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }

    String getMethod() {
        return this.method;
    }

    private MessageHeader mapToMessageHeader(Map<String, List<String>> map) {
        MessageHeader messageHeader = new MessageHeader();
        if (map == null || map.isEmpty()) {
            return messageHeader;
        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            for (String str : entry.getValue()) {
                if (key == null) {
                    messageHeader.prepend(key, str);
                } else {
                    messageHeader.add(key, str);
                }
            }
        }
        return messageHeader;
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/HttpURLConnection$HttpInputStream.class */
    class HttpInputStream extends FilterInputStream {
        private CacheRequest cacheRequest;
        private OutputStream outputStream;
        private boolean marked;
        private int inCache;
        private int markCount;
        private boolean closed;
        private byte[] skipBuffer;
        private static final int SKIP_BUFFER_SIZE = 8096;

        public HttpInputStream(InputStream inputStream) {
            super(inputStream);
            this.marked = false;
            this.inCache = 0;
            this.markCount = 0;
            this.cacheRequest = null;
            this.outputStream = null;
        }

        public HttpInputStream(InputStream inputStream, CacheRequest cacheRequest) {
            super(inputStream);
            this.marked = false;
            this.inCache = 0;
            this.markCount = 0;
            this.cacheRequest = cacheRequest;
            try {
                this.outputStream = cacheRequest.getBody();
            } catch (IOException e2) {
                this.cacheRequest.abort();
                this.cacheRequest = null;
                this.outputStream = null;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void mark(int i2) {
            super.mark(i2);
            if (this.cacheRequest != null) {
                this.marked = true;
                this.markCount = 0;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public synchronized void reset() throws IOException {
            super.reset();
            if (this.cacheRequest != null) {
                this.marked = false;
                this.inCache += this.markCount;
            }
        }

        private void ensureOpen() throws IOException {
            if (this.closed) {
                throw new IOException("stream is closed");
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            ensureOpen();
            try {
                byte[] bArr = new byte[1];
                int i2 = read(bArr);
                return i2 == -1 ? i2 : bArr[0] & 255;
            } catch (IOException e2) {
                if (this.cacheRequest != null) {
                    this.cacheRequest.abort();
                }
                throw e2;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4;
            ensureOpen();
            try {
                int i5 = super.read(bArr, i2, i3);
                if (this.inCache > 0) {
                    if (this.inCache >= i5) {
                        this.inCache -= i5;
                        i4 = 0;
                    } else {
                        i4 = i5 - this.inCache;
                        this.inCache = 0;
                    }
                } else {
                    i4 = i5;
                }
                if (i4 > 0 && this.outputStream != null) {
                    this.outputStream.write(bArr, i2 + (i5 - i4), i4);
                }
                if (this.marked) {
                    this.markCount += i5;
                }
                return i5;
            } catch (IOException e2) {
                if (this.cacheRequest != null) {
                    this.cacheRequest.abort();
                }
                throw e2;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long j2) throws IOException {
            int i2;
            ensureOpen();
            long j3 = j2;
            if (this.skipBuffer == null) {
                this.skipBuffer = new byte[SKIP_BUFFER_SIZE];
            }
            byte[] bArr = this.skipBuffer;
            if (j2 <= 0) {
                return 0L;
            }
            while (j3 > 0 && (i2 = read(bArr, 0, (int) Math.min(8096L, j3))) >= 0) {
                j3 -= i2;
            }
            return j2 - j3;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                if (this.closed) {
                    return;
                }
                try {
                    if (this.outputStream != null) {
                        if (read() != -1) {
                            this.cacheRequest.abort();
                        } else {
                            this.outputStream.close();
                        }
                    }
                    super.close();
                    this.closed = true;
                    HttpURLConnection.this.http = null;
                    HttpURLConnection.this.checkResponseCredentials(true);
                } catch (IOException e2) {
                    if (this.cacheRequest != null) {
                        this.cacheRequest.abort();
                    }
                    throw e2;
                }
            } catch (Throwable th) {
                this.closed = true;
                HttpURLConnection.this.http = null;
                HttpURLConnection.this.checkResponseCredentials(true);
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/HttpURLConnection$StreamingOutputStream.class */
    class StreamingOutputStream extends FilterOutputStream {
        long expected;
        long written;
        boolean closed;
        boolean error;
        IOException errorExcp;

        StreamingOutputStream(OutputStream outputStream, long j2) {
            super(outputStream);
            this.expected = j2;
            this.written = 0L;
            this.closed = false;
            this.error = false;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i2) throws IOException {
            checkError();
            this.written++;
            if (this.expected != -1 && this.written > this.expected) {
                throw new IOException("too many bytes written");
            }
            this.out.write(i2);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            checkError();
            this.written += i3;
            if (this.expected != -1 && this.written > this.expected) {
                this.out.close();
                throw new IOException("too many bytes written");
            }
            this.out.write(bArr, i2, i3);
        }

        void checkError() throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            }
            if (this.error) {
                throw this.errorExcp;
            }
            if (((PrintStream) this.out).checkError()) {
                throw new IOException("Error writing request body to server");
            }
        }

        boolean writtenOK() {
            return this.closed && !this.error;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.expected == -1) {
                super.close();
                OutputStream outputStream = HttpURLConnection.this.http.getOutputStream();
                outputStream.write(13);
                outputStream.write(10);
                outputStream.flush();
                return;
            }
            if (this.written != this.expected) {
                this.error = true;
                this.errorExcp = new IOException("insufficient data written");
                this.out.close();
                throw this.errorExcp;
            }
            super.flush();
        }
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/HttpURLConnection$ErrorStream.class */
    static class ErrorStream extends InputStream {
        ByteBuffer buffer;
        InputStream is;

        private ErrorStream(ByteBuffer byteBuffer) {
            this.buffer = byteBuffer;
            this.is = null;
        }

        private ErrorStream(ByteBuffer byteBuffer, InputStream inputStream) {
            this.buffer = byteBuffer;
            this.is = inputStream;
        }

        public static InputStream getErrorStream(InputStream inputStream, long j2, HttpClient httpClient) throws IOException {
            long j3;
            if (j2 == 0) {
                return null;
            }
            try {
                int readTimeout = httpClient.getReadTimeout();
                httpClient.setReadTimeout(HttpURLConnection.timeout4ESBuffer / 5);
                boolean z2 = false;
                if (j2 < 0) {
                    j3 = HttpURLConnection.bufSize4ES;
                    z2 = true;
                } else {
                    j3 = j2;
                }
                if (j3 <= HttpURLConnection.bufSize4ES) {
                    int i2 = (int) j3;
                    byte[] bArr = new byte[i2];
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    do {
                        try {
                            i5 = inputStream.read(bArr, i3, bArr.length - i3);
                        } catch (SocketTimeoutException e2) {
                            i4 += HttpURLConnection.timeout4ESBuffer / 5;
                        }
                        if (i5 < 0) {
                            if (z2) {
                                break;
                            }
                            throw new IOException("the server closes before sending " + j2 + " bytes of data");
                        }
                        i3 += i5;
                        if (i3 >= i2) {
                            break;
                        }
                    } while (i4 < HttpURLConnection.timeout4ESBuffer);
                    httpClient.setReadTimeout(readTimeout);
                    if (i3 == 0) {
                        return null;
                    }
                    if ((i3 == j3 && !z2) || (z2 && i5 < 0)) {
                        inputStream.close();
                        return new ErrorStream(ByteBuffer.wrap(bArr, 0, i3));
                    }
                    return new ErrorStream(ByteBuffer.wrap(bArr, 0, i3), inputStream);
                }
                return null;
            } catch (IOException e3) {
                return null;
            }
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.is == null) {
                return this.buffer.remaining();
            }
            return this.buffer.remaining() + this.is.available();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            int i2 = read(bArr);
            return i2 == -1 ? i2 : bArr[0] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int iRemaining = this.buffer.remaining();
            if (iRemaining > 0) {
                int i4 = iRemaining < i3 ? iRemaining : i3;
                this.buffer.get(bArr, i2, i4);
                return i4;
            }
            if (this.is == null) {
                return -1;
            }
            return this.is.read(bArr, i2, i3);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.buffer = null;
            if (this.is != null) {
                this.is.close();
            }
        }
    }

    private static void validateNTLMCredentials(PasswordAuthentication passwordAuthentication) throws IOException {
        if (passwordAuthentication == null) {
            return;
        }
        char[] password = passwordAuthentication.getPassword();
        if (password != null) {
            for (char c2 : password) {
                if (c2 == 0) {
                    throw new IOException("NUL character not allowed in NTLM password");
                }
            }
        }
        String userName = passwordAuthentication.getUserName();
        if (userName != null && userName.indexOf(0) != -1) {
            throw new IOException("NUL character not allowed in NTLM username or domain");
        }
    }
}
