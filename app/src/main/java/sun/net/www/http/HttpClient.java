package sun.net.www.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.CacheRequest;
import java.net.CookieHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.NetworkClient;
import sun.net.ProgressSource;
import sun.net.www.HeaderParser;
import sun.net.www.MessageHeader;
import sun.net.www.MeteredStream;
import sun.net.www.ParseUtil;
import sun.net.www.URLConnection;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.security.action.GetPropertyAction;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/http/HttpClient.class */
public class HttpClient extends NetworkClient {
    protected boolean cachedHttpClient;
    protected boolean inCache;
    MessageHeader requests;
    PosterOutputStream poster;
    boolean streaming;
    boolean failedOnce;
    private boolean ignoreContinue;
    private static final int HTTP_CONTINUE = 100;
    static final int httpPortNumber = 80;
    protected boolean proxyDisabled;
    public boolean usingProxy;
    protected String host;
    protected int port;
    protected static KeepAliveCache kac;
    private static boolean keepAliveProp;
    private static boolean retryPostProp;
    private static final boolean cacheNTLMProp;
    private static final boolean cacheSPNEGOProp;
    volatile boolean keepingAlive;
    volatile boolean disableKeepAlive;
    int keepAliveConnections;
    int keepAliveTimeout;
    private CacheRequest cacheRequest;
    protected URL url;
    public boolean reuse;
    private HttpCapture capture;
    private static final PlatformLogger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HttpClient.class.desiredAssertionStatus();
        kac = new KeepAliveCache();
        keepAliveProp = true;
        retryPostProp = true;
        logger = HttpURLConnection.getHttpLogger();
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("http.keepAlive"));
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.net.http.retryPost"));
        String str3 = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.ntlm.cache"));
        String str4 = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.spnego.cache"));
        if (str != null) {
            keepAliveProp = Boolean.valueOf(str).booleanValue();
        } else {
            keepAliveProp = true;
        }
        if (str2 != null) {
            retryPostProp = Boolean.valueOf(str2).booleanValue();
        } else {
            retryPostProp = true;
        }
        if (str3 != null) {
            cacheNTLMProp = Boolean.parseBoolean(str3);
        } else {
            cacheNTLMProp = true;
        }
        if (str4 != null) {
            cacheSPNEGOProp = Boolean.parseBoolean(str4);
        } else {
            cacheSPNEGOProp = true;
        }
    }

    protected int getDefaultPort() {
        return 80;
    }

    private static int getDefaultPort(String str) {
        if ("http".equalsIgnoreCase(str)) {
            return 80;
        }
        if ("https".equalsIgnoreCase(str)) {
            return 443;
        }
        return -1;
    }

    private static void logFinest(String str) {
        if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
            logger.finest(str);
        }
    }

    @Deprecated
    public static synchronized void resetProperties() {
    }

    int getKeepAliveTimeout() {
        return this.keepAliveTimeout;
    }

    public boolean getHttpKeepAliveSet() {
        return keepAliveProp;
    }

    protected HttpClient() {
        this.cachedHttpClient = false;
        this.poster = null;
        this.failedOnce = false;
        this.ignoreContinue = true;
        this.usingProxy = false;
        this.keepingAlive = false;
        this.keepAliveConnections = -1;
        this.keepAliveTimeout = 0;
        this.cacheRequest = null;
        this.reuse = false;
        this.capture = null;
    }

    private HttpClient(URL url) throws IOException {
        this(url, (String) null, -1, false);
    }

    protected HttpClient(URL url, boolean z2) throws IOException {
        this(url, null, -1, z2);
    }

    public HttpClient(URL url, String str, int i2) throws IOException {
        this(url, str, i2, false);
    }

    protected HttpClient(URL url, Proxy proxy, int i2) throws IOException {
        this.cachedHttpClient = false;
        this.poster = null;
        this.failedOnce = false;
        this.ignoreContinue = true;
        this.usingProxy = false;
        this.keepingAlive = false;
        this.keepAliveConnections = -1;
        this.keepAliveTimeout = 0;
        this.cacheRequest = null;
        this.reuse = false;
        this.capture = null;
        this.proxy = proxy == null ? Proxy.NO_PROXY : proxy;
        this.host = url.getHost();
        this.url = url;
        this.port = url.getPort();
        if (this.port == -1) {
            this.port = getDefaultPort();
        }
        setConnectTimeout(i2);
        this.capture = HttpCapture.getCapture(url);
        openServer();
    }

    protected static Proxy newHttpProxy(String str, int i2, String str2) {
        if (str == null || str2 == null) {
            return Proxy.NO_PROXY;
        }
        return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(str, i2 < 0 ? getDefaultPort(str2) : i2));
    }

    private HttpClient(URL url, String str, int i2, boolean z2) throws IOException {
        this(url, z2 ? Proxy.NO_PROXY : newHttpProxy(str, i2, "http"), -1);
    }

    public HttpClient(URL url, String str, int i2, boolean z2, int i3) throws IOException {
        this(url, z2 ? Proxy.NO_PROXY : newHttpProxy(str, i2, "http"), i3);
    }

    public static HttpClient New(URL url) throws IOException {
        return New(url, Proxy.NO_PROXY, -1, true, null);
    }

    public static HttpClient New(URL url, boolean z2) throws IOException {
        return New(url, Proxy.NO_PROXY, -1, z2, null);
    }

    public static HttpClient New(URL url, Proxy proxy, int i2, boolean z2, HttpURLConnection httpURLConnection) throws IOException {
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        HttpClient httpClient = null;
        if (z2) {
            httpClient = kac.get(url, null);
            if (httpClient != null && httpURLConnection != null && httpURLConnection.streaming() && "POST".equals(httpURLConnection.getRequestMethod()) && !httpClient.available()) {
                httpClient.inCache = false;
                httpClient.closeServer();
                httpClient = null;
            }
            if (httpClient != null) {
                if ((httpClient.proxy != null && httpClient.proxy.equals(proxy)) || (httpClient.proxy == null && proxy == null)) {
                    synchronized (httpClient) {
                        httpClient.cachedHttpClient = true;
                        if (!$assertionsDisabled && !httpClient.inCache) {
                            throw new AssertionError();
                        }
                        httpClient.inCache = false;
                        if (httpURLConnection != null && httpClient.needsTunneling()) {
                            httpURLConnection.setTunnelState(HttpURLConnection.TunnelState.TUNNELING);
                        }
                        logFinest("KeepAlive stream retrieved from the cache, " + ((Object) httpClient));
                    }
                } else {
                    synchronized (httpClient) {
                        httpClient.inCache = false;
                        httpClient.closeServer();
                    }
                    httpClient = null;
                }
            }
        }
        if (httpClient == null) {
            httpClient = new HttpClient(url, proxy, i2);
        } else {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                if (httpClient.proxy == Proxy.NO_PROXY || httpClient.proxy == null) {
                    securityManager.checkConnect(InetAddress.getByName(url.getHost()).getHostAddress(), url.getPort());
                } else {
                    securityManager.checkConnect(url.getHost(), url.getPort());
                }
            }
            httpClient.url = url;
        }
        return httpClient;
    }

    public static HttpClient New(URL url, Proxy proxy, int i2, HttpURLConnection httpURLConnection) throws IOException {
        return New(url, proxy, i2, true, httpURLConnection);
    }

    public static HttpClient New(URL url, String str, int i2, boolean z2) throws IOException {
        return New(url, newHttpProxy(str, i2, "http"), -1, z2, null);
    }

    public static HttpClient New(URL url, String str, int i2, boolean z2, int i3, HttpURLConnection httpURLConnection) throws IOException {
        return New(url, newHttpProxy(str, i2, "http"), i3, z2, httpURLConnection);
    }

    public void finished() {
        if (this.reuse) {
            return;
        }
        this.keepAliveConnections--;
        this.poster = null;
        if (this.keepAliveConnections > 0 && isKeepingAlive() && !this.serverOutput.checkError()) {
            putInKeepAliveCache();
        } else {
            closeServer();
        }
    }

    /* JADX WARN: Finally extract failed */
    protected synchronized boolean available() {
        boolean z2 = true;
        int soTimeout = -1;
        try {
            try {
                try {
                    soTimeout = this.serverSocket.getSoTimeout();
                    this.serverSocket.setSoTimeout(1);
                    if (new BufferedInputStream(this.serverSocket.getInputStream()).read() == -1) {
                        logFinest("HttpClient.available(): read returned -1: not available");
                        z2 = false;
                    }
                    if (soTimeout != -1) {
                        this.serverSocket.setSoTimeout(soTimeout);
                    }
                } catch (Throwable th) {
                    if (soTimeout != -1) {
                        this.serverSocket.setSoTimeout(soTimeout);
                    }
                    throw th;
                }
            } catch (SocketTimeoutException e2) {
                logFinest("HttpClient.available(): SocketTimeout: its available");
                if (soTimeout != -1) {
                    this.serverSocket.setSoTimeout(soTimeout);
                }
            }
        } catch (IOException e3) {
            logFinest("HttpClient.available(): SocketException: not available");
            z2 = false;
        }
        return z2;
    }

    protected synchronized void putInKeepAliveCache() {
        if (this.inCache) {
            if (!$assertionsDisabled) {
                throw new AssertionError((Object) "Duplicate put to keep alive cache");
            }
        } else {
            this.inCache = true;
            kac.put(this.url, null, this);
        }
    }

    protected synchronized boolean isInKeepAliveCache() {
        return this.inCache;
    }

    public void closeIdleConnection() {
        HttpClient httpClient = kac.get(this.url, null);
        if (httpClient != null) {
            httpClient.closeServer();
        }
    }

    @Override // sun.net.NetworkClient
    public void openServer(String str, int i2) throws IOException {
        this.serverSocket = doConnect(str, i2);
        try {
            OutputStream outputStream = this.serverSocket.getOutputStream();
            if (this.capture != null) {
                outputStream = new HttpCaptureOutputStream(outputStream, this.capture);
            }
            this.serverOutput = new PrintStream((OutputStream) new BufferedOutputStream(outputStream), false, encoding);
            this.serverSocket.setTcpNoDelay(true);
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(encoding + " encoding not found", e2);
        }
    }

    public boolean needsTunneling() {
        return false;
    }

    public synchronized boolean isCachedConnection() {
        return this.cachedHttpClient;
    }

    public void afterConnect() throws IOException {
    }

    private synchronized void privilegedOpenServer(final InetSocketAddress inetSocketAddress) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.net.www.http.HttpClient.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws IOException {
                    HttpClient.this.openServer(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    private void superOpenServer(String str, int i2) throws IOException {
        super.openServer(str, i2);
    }

    protected synchronized void openServer() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(this.host, this.port);
        }
        if (this.keepingAlive) {
            return;
        }
        if (this.url.getProtocol().equals("http") || this.url.getProtocol().equals("https")) {
            if (this.proxy != null && this.proxy.type() == Proxy.Type.HTTP) {
                URLConnection.setProxiedHost(this.host);
                privilegedOpenServer((InetSocketAddress) this.proxy.address());
                this.usingProxy = true;
                return;
            } else {
                openServer(this.host, this.port);
                this.usingProxy = false;
                return;
            }
        }
        if (this.proxy != null && this.proxy.type() == Proxy.Type.HTTP) {
            URLConnection.setProxiedHost(this.host);
            privilegedOpenServer((InetSocketAddress) this.proxy.address());
            this.usingProxy = true;
        } else {
            super.openServer(this.host, this.port);
            this.usingProxy = false;
        }
    }

    public String getURLFile() throws IOException {
        String file;
        if (this.usingProxy && !this.proxyDisabled) {
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append(this.url.getProtocol());
            stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
            if (this.url.getAuthority() != null && this.url.getAuthority().length() > 0) {
                stringBuffer.append("//");
                stringBuffer.append(this.url.getAuthority());
            }
            if (this.url.getPath() != null) {
                stringBuffer.append(this.url.getPath());
            }
            if (this.url.getQuery() != null) {
                stringBuffer.append('?');
                stringBuffer.append(this.url.getQuery());
            }
            file = stringBuffer.toString();
        } else {
            file = this.url.getFile();
            if (file == null || file.length() == 0) {
                file = "/";
            } else if (file.charAt(0) == '?') {
                file = "/" + file;
            }
        }
        if (file.indexOf(10) == -1) {
            return file;
        }
        throw new MalformedURLException("Illegal character in URL");
    }

    @Deprecated
    public void writeRequests(MessageHeader messageHeader) {
        this.requests = messageHeader;
        this.requests.print(this.serverOutput);
        this.serverOutput.flush();
    }

    public void writeRequests(MessageHeader messageHeader, PosterOutputStream posterOutputStream) throws IOException {
        this.requests = messageHeader;
        this.requests.print(this.serverOutput);
        this.poster = posterOutputStream;
        if (this.poster != null) {
            this.poster.writeTo(this.serverOutput);
        }
        this.serverOutput.flush();
    }

    public void writeRequests(MessageHeader messageHeader, PosterOutputStream posterOutputStream, boolean z2) throws IOException {
        this.streaming = z2;
        writeRequests(messageHeader, posterOutputStream);
    }

    public boolean parseHTTP(MessageHeader messageHeader, ProgressSource progressSource, HttpURLConnection httpURLConnection) throws IOException {
        try {
            this.serverInput = this.serverSocket.getInputStream();
            if (this.capture != null) {
                this.serverInput = new HttpCaptureInputStream(this.serverInput, this.capture);
            }
            this.serverInput = new BufferedInputStream(this.serverInput);
            return parseHTTPHeader(messageHeader, progressSource, httpURLConnection);
        } catch (SocketTimeoutException e2) {
            if (this.ignoreContinue) {
                closeServer();
            }
            throw e2;
        } catch (IOException e3) {
            closeServer();
            this.cachedHttpClient = false;
            if (!this.failedOnce && this.requests != null) {
                this.failedOnce = true;
                if (!getRequestMethod().equals("CONNECT") && !this.streaming && (!httpURLConnection.getRequestMethod().equals("POST") || retryPostProp)) {
                    openServer();
                    checkTunneling(httpURLConnection);
                    afterConnect();
                    writeRequests(this.requests, this.poster);
                    return parseHTTP(messageHeader, progressSource, httpURLConnection);
                }
            }
            throw e3;
        }
    }

    private void checkTunneling(HttpURLConnection httpURLConnection) throws IOException {
        if (needsTunneling()) {
            MessageHeader messageHeader = this.requests;
            PosterOutputStream posterOutputStream = this.poster;
            httpURLConnection.doTunneling();
            this.requests = messageHeader;
            this.poster = posterOutputStream;
        }
    }

    private boolean parseHTTPHeader(MessageHeader messageHeader, ProgressSource progressSource, HttpURLConnection httpURLConnection) throws IOException {
        URI uri;
        int i2;
        this.keepAliveConnections = -1;
        this.keepAliveTimeout = 0;
        byte[] bArr = new byte[8];
        try {
            int i3 = 0;
            this.serverInput.mark(10);
            while (i3 < 8 && (i2 = this.serverInput.read(bArr, i3, 8 - i3)) >= 0) {
                i3 += i2;
            }
            String strFindValue = null;
            String strFindValue2 = null;
            boolean z2 = bArr[0] == 72 && bArr[1] == 84 && bArr[2] == 84 && bArr[3] == 80 && bArr[4] == 47 && bArr[5] == 49 && bArr[6] == 46;
            this.serverInput.reset();
            if (z2) {
                messageHeader.parseHeader(this.serverInput);
                CookieHandler cookieHandler = httpURLConnection.getCookieHandler();
                if (cookieHandler != null && (uri = ParseUtil.toURI(this.url)) != null) {
                    cookieHandler.put(uri, messageHeader.getHeaders());
                }
                if (this.usingProxy) {
                    strFindValue = messageHeader.findValue("Proxy-Connection");
                    strFindValue2 = messageHeader.findValue("Proxy-Authenticate");
                }
                if (strFindValue == null) {
                    strFindValue = messageHeader.findValue("Connection");
                    strFindValue2 = messageHeader.findValue("WWW-Authenticate");
                }
                boolean z3 = !this.disableKeepAlive;
                if (z3 && ((!cacheNTLMProp || !cacheSPNEGOProp) && strFindValue2 != null)) {
                    String lowerCase = strFindValue2.toLowerCase(Locale.US);
                    if (!cacheNTLMProp) {
                        z3 &= !lowerCase.startsWith("ntlm ");
                    }
                    if (!cacheSPNEGOProp) {
                        z3 = z3 & (!lowerCase.startsWith("negotiate ")) & (!lowerCase.startsWith("kerberos "));
                    }
                }
                this.disableKeepAlive |= !z3;
                if (strFindValue != null && strFindValue.toLowerCase(Locale.US).equals("keep-alive")) {
                    if (this.disableKeepAlive) {
                        this.keepAliveConnections = 1;
                    } else {
                        HeaderParser headerParser = new HeaderParser(messageHeader.findValue("Keep-Alive"));
                        this.keepAliveConnections = headerParser.findInt("max", this.usingProxy ? 50 : 5);
                        this.keepAliveTimeout = headerParser.findInt("timeout", this.usingProxy ? 60 : 5);
                    }
                } else if (bArr[7] != 48) {
                    if (strFindValue != null || this.disableKeepAlive) {
                        this.keepAliveConnections = 1;
                    } else {
                        this.keepAliveConnections = 5;
                    }
                }
            } else {
                if (i3 != 8) {
                    if (!this.failedOnce && this.requests != null) {
                        this.failedOnce = true;
                        if (!getRequestMethod().equals("CONNECT") && !this.streaming && (!httpURLConnection.getRequestMethod().equals("POST") || retryPostProp)) {
                            closeServer();
                            this.cachedHttpClient = false;
                            openServer();
                            checkTunneling(httpURLConnection);
                            afterConnect();
                            writeRequests(this.requests, this.poster);
                            return parseHTTP(messageHeader, progressSource, httpURLConnection);
                        }
                    }
                    throw new SocketException("Unexpected end of file from server");
                }
                messageHeader.set("Content-type", "unknown/unknown");
            }
            int i4 = -1;
            try {
                String value = messageHeader.getValue(0);
                int iIndexOf = value.indexOf(32);
                while (value.charAt(iIndexOf) == ' ') {
                    iIndexOf++;
                }
                i4 = Integer.parseInt(value.substring(iIndexOf, iIndexOf + 3));
            } catch (Exception e2) {
            }
            if (i4 == 100 && this.ignoreContinue) {
                messageHeader.reset();
                return parseHTTPHeader(messageHeader, progressSource, httpURLConnection);
            }
            long j2 = -1;
            String strFindValue3 = messageHeader.findValue("Transfer-Encoding");
            if (strFindValue3 != null && strFindValue3.equalsIgnoreCase("chunked")) {
                this.serverInput = new ChunkedInputStream(this.serverInput, this, messageHeader);
                if (this.keepAliveConnections <= 1) {
                    this.keepAliveConnections = 1;
                    this.keepingAlive = false;
                } else {
                    this.keepingAlive = !this.disableKeepAlive;
                }
                this.failedOnce = false;
            } else {
                String strFindValue4 = messageHeader.findValue("content-length");
                if (strFindValue4 != null) {
                    try {
                        j2 = Long.parseLong(strFindValue4);
                    } catch (NumberFormatException e3) {
                        j2 = -1;
                    }
                }
                String key = this.requests.getKey(0);
                if ((key != null && key.startsWith("HEAD")) || i4 == 304 || i4 == 204) {
                    j2 = 0;
                }
                if (this.keepAliveConnections > 1 && (j2 >= 0 || i4 == 304 || i4 == 204)) {
                    this.keepingAlive = !this.disableKeepAlive;
                    this.failedOnce = false;
                } else if (this.keepingAlive) {
                    this.keepingAlive = false;
                }
            }
            if (j2 > 0) {
                if (progressSource != null) {
                    progressSource.setContentType(messageHeader.findValue("content-type"));
                }
                if (isKeepingAlive() || this.disableKeepAlive) {
                    logFinest("KeepAlive stream used: " + ((Object) this.url));
                    this.serverInput = new KeepAliveStream(this.serverInput, progressSource, j2, this);
                    this.failedOnce = false;
                } else {
                    this.serverInput = new MeteredStream(this.serverInput, progressSource, j2);
                }
            } else if (j2 == -1) {
                if (progressSource != null) {
                    progressSource.setContentType(messageHeader.findValue("content-type"));
                    this.serverInput = new MeteredStream(this.serverInput, progressSource, j2);
                }
            } else if (progressSource != null) {
                progressSource.finishTracking();
            }
            return z2;
        } catch (IOException e4) {
            throw e4;
        }
    }

    public synchronized InputStream getInputStream() {
        return this.serverInput;
    }

    public OutputStream getOutputStream() {
        return this.serverOutput;
    }

    public String toString() {
        return getClass().getName() + "(" + ((Object) this.url) + ")";
    }

    public final boolean isKeepingAlive() {
        return getHttpKeepAliveSet() && this.keepingAlive;
    }

    public void setCacheRequest(CacheRequest cacheRequest) {
        this.cacheRequest = cacheRequest;
    }

    CacheRequest getCacheRequest() {
        return this.cacheRequest;
    }

    String getRequestMethod() {
        String key;
        if (this.requests != null && (key = this.requests.getKey(0)) != null) {
            return key.split("\\s+")[0];
        }
        return "";
    }

    protected void finalize() throws Throwable {
    }

    public void setDoNotRetry(boolean z2) {
        this.failedOnce = z2;
    }

    public void setIgnoreContinue(boolean z2) {
        this.ignoreContinue = z2;
    }

    @Override // sun.net.NetworkClient
    public void closeServer() {
        try {
            this.keepingAlive = false;
            this.serverSocket.close();
        } catch (Exception e2) {
        }
    }

    public String getProxyHostUsed() {
        if (!this.usingProxy) {
            return null;
        }
        return ((InetSocketAddress) this.proxy.address()).getHostString();
    }

    public int getProxyPortUsed() {
        if (this.usingProxy) {
            return ((InetSocketAddress) this.proxy.address()).getPort();
        }
        return -1;
    }
}
