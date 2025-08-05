package sun.net.www.protocol.https;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.security.AccessController;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.security.action.GetPropertyAction;
import sun.security.ssl.SSLSocketImpl;
import sun.security.util.HostnameChecker;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/net/www/protocol/https/HttpsClient.class */
final class HttpsClient extends HttpClient implements HandshakeCompletedListener {
    private static final int httpsPortNumber = 443;
    private static final String defaultHVCanonicalName = "javax.net.ssl.HttpsURLConnection.DefaultHostnameVerifier";
    private HostnameVerifier hv;
    private SSLSocketFactory sslSocketFactory;
    private SSLSession session;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HttpsClient.class.desiredAssertionStatus();
    }

    @Override // sun.net.www.http.HttpClient
    protected int getDefaultPort() {
        return httpsPortNumber;
    }

    private String[] getCipherSuites() {
        String[] strArr;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("https.cipherSuites"));
        if (str == null || "".equals(str)) {
            strArr = null;
        } else {
            Vector vector = new Vector();
            StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
            while (stringTokenizer.hasMoreTokens()) {
                vector.addElement(stringTokenizer.nextToken());
            }
            strArr = new String[vector.size()];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = (String) vector.elementAt(i2);
            }
        }
        return strArr;
    }

    private String[] getProtocols() {
        String[] strArr;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("https.protocols"));
        if (str == null || "".equals(str)) {
            strArr = null;
        } else {
            Vector vector = new Vector();
            StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
            while (stringTokenizer.hasMoreTokens()) {
                vector.addElement(stringTokenizer.nextToken());
            }
            strArr = new String[vector.size()];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = (String) vector.elementAt(i2);
            }
        }
        return strArr;
    }

    private String getUserAgent() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("https.agent"));
        if (str == null || str.length() == 0) {
            str = "JSSE";
        }
        return str;
    }

    private HttpsClient(SSLSocketFactory sSLSocketFactory, URL url) throws IOException {
        this(sSLSocketFactory, url, (String) null, -1);
    }

    HttpsClient(SSLSocketFactory sSLSocketFactory, URL url, String str, int i2) throws IOException {
        this(sSLSocketFactory, url, str, i2, -1);
    }

    HttpsClient(SSLSocketFactory sSLSocketFactory, URL url, String str, int i2, int i3) throws IOException {
        this(sSLSocketFactory, url, str == null ? null : HttpClient.newHttpProxy(str, i2, "https"), i3);
    }

    HttpsClient(SSLSocketFactory sSLSocketFactory, URL url, Proxy proxy, int i2) throws IOException {
        PlatformLogger httpLogger = HttpURLConnection.getHttpLogger();
        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
            httpLogger.finest("Creating new HttpsClient with url:" + ((Object) url) + " and proxy:" + ((Object) proxy) + " with connect timeout:" + i2);
        }
        this.proxy = proxy;
        setSSLSocketFactory(sSLSocketFactory);
        this.proxyDisabled = true;
        this.host = url.getHost();
        this.url = url;
        this.port = url.getPort();
        if (this.port == -1) {
            this.port = getDefaultPort();
        }
        setConnectTimeout(i2);
        openServer();
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, HttpURLConnection httpURLConnection) throws IOException {
        return New(sSLSocketFactory, url, hostnameVerifier, true, httpURLConnection);
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, boolean z2, HttpURLConnection httpURLConnection) throws IOException {
        return New(sSLSocketFactory, url, hostnameVerifier, (String) null, -1, z2, httpURLConnection);
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, String str, int i2, HttpURLConnection httpURLConnection) throws IOException {
        return New(sSLSocketFactory, url, hostnameVerifier, str, i2, true, httpURLConnection);
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, String str, int i2, boolean z2, HttpURLConnection httpURLConnection) throws IOException {
        return New(sSLSocketFactory, url, hostnameVerifier, str, i2, z2, -1, httpURLConnection);
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, String str, int i2, boolean z2, int i3, HttpURLConnection httpURLConnection) throws IOException {
        return New(sSLSocketFactory, url, hostnameVerifier, str == null ? null : HttpClient.newHttpProxy(str, i2, "https"), z2, i3, httpURLConnection);
    }

    static HttpClient New(SSLSocketFactory sSLSocketFactory, URL url, HostnameVerifier hostnameVerifier, Proxy proxy, boolean z2, int i2, HttpURLConnection httpURLConnection) throws IOException {
        if (proxy == null) {
            proxy = Proxy.NO_PROXY;
        }
        PlatformLogger httpLogger = HttpURLConnection.getHttpLogger();
        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
            httpLogger.finest("Looking for HttpClient for URL " + ((Object) url) + " and proxy value of " + ((Object) proxy));
        }
        HttpsClient httpsClient = null;
        if (z2) {
            httpsClient = (HttpsClient) kac.get(url, sSLSocketFactory);
            if (httpsClient != null && httpURLConnection != null && httpURLConnection.streaming() && "POST".equals(httpURLConnection.getRequestMethod()) && !httpsClient.available()) {
                httpsClient = null;
            }
            if (httpsClient != null) {
                if ((httpsClient.proxy != null && httpsClient.proxy.equals(proxy)) || (httpsClient.proxy == null && proxy == Proxy.NO_PROXY)) {
                    synchronized (httpsClient) {
                        httpsClient.cachedHttpClient = true;
                        if (!$assertionsDisabled && !httpsClient.inCache) {
                            throw new AssertionError();
                        }
                        httpsClient.inCache = false;
                        if (httpURLConnection != null && httpsClient.needsTunneling()) {
                            httpURLConnection.setTunnelState(HttpURLConnection.TunnelState.TUNNELING);
                        }
                        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
                            httpLogger.finest("KeepAlive stream retrieved from the cache, " + ((Object) httpsClient));
                        }
                    }
                } else {
                    synchronized (httpsClient) {
                        if (httpLogger.isLoggable(PlatformLogger.Level.FINEST)) {
                            httpLogger.finest("Not returning this connection to cache: " + ((Object) httpsClient));
                        }
                        httpsClient.inCache = false;
                        httpsClient.closeServer();
                    }
                    httpsClient = null;
                }
            }
        }
        if (httpsClient == null) {
            httpsClient = new HttpsClient(sSLSocketFactory, url, proxy, i2);
        } else {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                if (httpsClient.proxy == Proxy.NO_PROXY || httpsClient.proxy == null) {
                    securityManager.checkConnect(InetAddress.getByName(url.getHost()).getHostAddress(), url.getPort());
                } else {
                    securityManager.checkConnect(url.getHost(), url.getPort());
                }
            }
            httpsClient.url = url;
        }
        httpsClient.setHostnameVerifier(hostnameVerifier);
        return httpsClient;
    }

    void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hv = hostnameVerifier;
    }

    void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.sslSocketFactory = sSLSocketFactory;
    }

    SSLSocketFactory getSSLSocketFactory() {
        return this.sslSocketFactory;
    }

    @Override // sun.net.NetworkClient
    protected Socket createSocket() throws IOException {
        try {
            return this.sslSocketFactory.createSocket();
        } catch (SocketException e2) {
            Throwable cause = e2.getCause();
            if (cause != null && (cause instanceof UnsupportedOperationException)) {
                return super.createSocket();
            }
            throw e2;
        }
    }

    @Override // sun.net.www.http.HttpClient
    public boolean needsTunneling() {
        return (this.proxy == null || this.proxy.type() == Proxy.Type.DIRECT || this.proxy.type() == Proxy.Type.SOCKS) ? false : true;
    }

    @Override // sun.net.www.http.HttpClient
    public void afterConnect() throws IOException, SecurityException {
        SSLSocket sSLSocket;
        if (!isCachedConnection()) {
            SSLSocketFactory sSLSocketFactory = this.sslSocketFactory;
            try {
                if (!(this.serverSocket instanceof SSLSocket)) {
                    sSLSocket = (SSLSocket) sSLSocketFactory.createSocket(this.serverSocket, this.host, this.port, true);
                } else {
                    sSLSocket = (SSLSocket) this.serverSocket;
                    if (sSLSocket instanceof SSLSocketImpl) {
                        ((SSLSocketImpl) sSLSocket).setHost(this.host);
                    }
                }
            } catch (IOException e2) {
                try {
                    sSLSocket = (SSLSocket) sSLSocketFactory.createSocket(this.host, this.port);
                } catch (IOException e3) {
                    throw e2;
                }
            }
            String[] protocols = getProtocols();
            String[] cipherSuites = getCipherSuites();
            if (protocols != null) {
                sSLSocket.setEnabledProtocols(protocols);
            }
            if (cipherSuites != null) {
                sSLSocket.setEnabledCipherSuites(cipherSuites);
            }
            sSLSocket.addHandshakeCompletedListener(this);
            boolean z2 = true;
            String endpointIdentificationAlgorithm = sSLSocket.getSSLParameters().getEndpointIdentificationAlgorithm();
            if (endpointIdentificationAlgorithm != null && endpointIdentificationAlgorithm.length() != 0) {
                if (endpointIdentificationAlgorithm.equalsIgnoreCase("HTTPS")) {
                    z2 = false;
                }
            } else {
                boolean z3 = false;
                if (this.hv != null) {
                    String canonicalName = this.hv.getClass().getCanonicalName();
                    if (canonicalName != null && canonicalName.equalsIgnoreCase(defaultHVCanonicalName)) {
                        z3 = true;
                    }
                } else {
                    z3 = true;
                }
                if (z3) {
                    SSLParameters sSLParameters = sSLSocket.getSSLParameters();
                    sSLParameters.setEndpointIdentificationAlgorithm("HTTPS");
                    sSLSocket.setSSLParameters(sSLParameters);
                    z2 = false;
                }
            }
            sSLSocket.startHandshake();
            this.session = sSLSocket.getSession();
            this.serverSocket = sSLSocket;
            try {
                this.serverOutput = new PrintStream((OutputStream) new BufferedOutputStream(this.serverSocket.getOutputStream()), false, encoding);
                if (z2) {
                    checkURLSpoofing(this.hv);
                    return;
                }
                return;
            } catch (UnsupportedEncodingException e4) {
                throw new InternalError(encoding + " encoding not found");
            }
        }
        this.session = ((SSLSocket) this.serverSocket).getSession();
    }

    private void checkURLSpoofing(HostnameVerifier hostnameVerifier) throws IOException {
        String host = this.url.getHost();
        if (host != null && host.startsWith("[") && host.endsWith("]")) {
            host = host.substring(1, host.length() - 1);
        }
        String cipherSuite = this.session.getCipherSuite();
        try {
            HostnameChecker hostnameChecker = HostnameChecker.getInstance((byte) 1);
            if (cipherSuite.startsWith("TLS_KRB5")) {
                if (!HostnameChecker.match(host, getPeerPrincipal())) {
                    throw new SSLPeerUnverifiedException("Hostname checker failed for Kerberos");
                }
            } else {
                Certificate[] peerCertificates = this.session.getPeerCertificates();
                if (!(peerCertificates[0] instanceof X509Certificate)) {
                    throw new SSLPeerUnverifiedException("");
                }
                hostnameChecker.match(host, (X509Certificate) peerCertificates[0]);
            }
        } catch (CertificateException | SSLPeerUnverifiedException e2) {
            if (cipherSuite != null && cipherSuite.indexOf("_anon_") != -1) {
                return;
            }
            if (hostnameVerifier != null && hostnameVerifier.verify(host, this.session)) {
                return;
            }
            this.serverSocket.close();
            this.session.invalidate();
            throw new IOException("HTTPS hostname wrong:  should be <" + this.url.getHost() + ">");
        }
    }

    @Override // sun.net.www.http.HttpClient
    protected void putInKeepAliveCache() {
        if (this.inCache) {
            if (!$assertionsDisabled) {
                throw new AssertionError((Object) "Duplicate put to keep alive cache");
            }
        } else {
            this.inCache = true;
            kac.put(this.url, this.sslSocketFactory, this);
        }
    }

    @Override // sun.net.www.http.HttpClient
    public void closeIdleConnection() {
        HttpClient httpClient = kac.get(this.url, this.sslSocketFactory);
        if (httpClient != null) {
            httpClient.closeServer();
        }
    }

    String getCipherSuite() {
        return this.session.getCipherSuite();
    }

    public Certificate[] getLocalCertificates() {
        return this.session.getLocalCertificates();
    }

    Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        return this.session.getPeerCertificates();
    }

    javax.security.cert.X509Certificate[] getServerCertificateChain() throws SSLPeerUnverifiedException {
        return this.session.getPeerCertificateChain();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.security.Principal] */
    Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        X500Principal subjectX500Principal;
        try {
            subjectX500Principal = this.session.getPeerPrincipal();
        } catch (AbstractMethodError e2) {
            subjectX500Principal = ((X509Certificate) this.session.getPeerCertificates()[0]).getSubjectX500Principal();
        }
        return subjectX500Principal;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [java.security.Principal] */
    Principal getLocalPrincipal() {
        X500Principal subjectX500Principal;
        try {
            subjectX500Principal = this.session.getLocalPrincipal();
        } catch (AbstractMethodError e2) {
            subjectX500Principal = null;
            Certificate[] localCertificates = this.session.getLocalCertificates();
            if (localCertificates != null) {
                subjectX500Principal = ((X509Certificate) localCertificates[0]).getSubjectX500Principal();
            }
        }
        return subjectX500Principal;
    }

    @Override // javax.net.ssl.HandshakeCompletedListener
    public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
        this.session = handshakeCompletedEvent.getSession();
    }

    @Override // sun.net.www.http.HttpClient
    public String getProxyHostUsed() {
        if (!needsTunneling()) {
            return null;
        }
        return super.getProxyHostUsed();
    }

    @Override // sun.net.www.http.HttpClient
    public int getProxyPortUsed() {
        if (this.proxy == null || this.proxy.type() == Proxy.Type.DIRECT || this.proxy.type() == Proxy.Type.SOCKS) {
            return -1;
        }
        return ((InetSocketAddress) this.proxy.address()).getPort();
    }
}
