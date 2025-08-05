package sun.net.www.protocol.https;

import java.io.IOException;
import java.net.Proxy;
import java.net.SecureCacheResponse;
import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import javax.security.cert.X509Certificate;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;

/* loaded from: rt.jar:sun/net/www/protocol/https/AbstractDelegateHttpsURLConnection.class */
public abstract class AbstractDelegateHttpsURLConnection extends HttpURLConnection {
    protected abstract SSLSocketFactory getSSLSocketFactory();

    protected abstract HostnameVerifier getHostnameVerifier();

    protected AbstractDelegateHttpsURLConnection(URL url, sun.net.www.protocol.http.Handler handler) throws IOException {
        this(url, null, handler);
    }

    protected AbstractDelegateHttpsURLConnection(URL url, Proxy proxy, sun.net.www.protocol.http.Handler handler) throws IOException {
        super(url, proxy, handler);
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    public void setNewClient(URL url) throws IOException, SecurityException {
        setNewClient(url, false);
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    public void setNewClient(URL url, boolean z2) throws IOException, SecurityException {
        int readTimeout = getReadTimeout();
        this.http = HttpsClient.New(getSSLSocketFactory(), url, getHostnameVerifier(), null, -1, z2, getConnectTimeout(), this);
        this.http.setReadTimeout(readTimeout);
        ((HttpsClient) this.http).afterConnect();
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    public void setProxiedClient(URL url, String str, int i2) throws IOException, SecurityException {
        setProxiedClient(url, str, i2, false);
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    public void setProxiedClient(URL url, String str, int i2, boolean z2) throws IOException, SecurityException {
        proxiedConnect(url, str, i2, z2);
        if (!this.http.isCachedConnection()) {
            doTunneling();
        }
        ((HttpsClient) this.http).afterConnect();
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    protected void proxiedConnect(URL url, String str, int i2, boolean z2) throws IOException {
        if (this.connected) {
            return;
        }
        int readTimeout = getReadTimeout();
        this.http = HttpsClient.New(getSSLSocketFactory(), url, getHostnameVerifier(), str, i2, z2, getConnectTimeout(), this);
        this.http.setReadTimeout(readTimeout);
        this.connected = true;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean z2) {
        this.connected = z2;
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection, java.net.URLConnection
    public void connect() throws IOException, SecurityException {
        if (this.connected) {
            return;
        }
        plainConnect();
        if (this.cachedResponse != null) {
            return;
        }
        if (!this.http.isCachedConnection() && this.http.needsTunneling()) {
            doTunneling();
        }
        ((HttpsClient) this.http).afterConnect();
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    protected HttpClient getNewHttpClient(URL url, Proxy proxy, int i2) throws IOException {
        return HttpsClient.New(getSSLSocketFactory(), url, getHostnameVerifier(), proxy, true, i2, (HttpURLConnection) this);
    }

    @Override // sun.net.www.protocol.http.HttpURLConnection
    protected HttpClient getNewHttpClient(URL url, Proxy proxy, int i2, boolean z2) throws IOException {
        return HttpsClient.New(getSSLSocketFactory(), url, getHostnameVerifier(), proxy, z2, i2, this);
    }

    public String getCipherSuite() {
        if (this.cachedResponse != null) {
            return ((SecureCacheResponse) this.cachedResponse).getCipherSuite();
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getCipherSuite();
    }

    public Certificate[] getLocalCertificates() {
        if (this.cachedResponse != null) {
            List<Certificate> localCertificateChain = ((SecureCacheResponse) this.cachedResponse).getLocalCertificateChain();
            if (localCertificateChain == null) {
                return null;
            }
            return (Certificate[]) localCertificateChain.toArray(new Certificate[0]);
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getLocalCertificates();
    }

    public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        if (this.cachedResponse != null) {
            List<Certificate> serverCertificateChain = ((SecureCacheResponse) this.cachedResponse).getServerCertificateChain();
            if (serverCertificateChain == null) {
                return null;
            }
            return (Certificate[]) serverCertificateChain.toArray(new Certificate[0]);
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getServerCertificates();
    }

    public X509Certificate[] getServerCertificateChain() throws SSLPeerUnverifiedException {
        if (this.cachedResponse != null) {
            throw new UnsupportedOperationException("this method is not supported when using cache");
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getServerCertificateChain();
    }

    Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        if (this.cachedResponse != null) {
            return ((SecureCacheResponse) this.cachedResponse).getPeerPrincipal();
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getPeerPrincipal();
    }

    Principal getLocalPrincipal() {
        if (this.cachedResponse != null) {
            return ((SecureCacheResponse) this.cachedResponse).getLocalPrincipal();
        }
        if (this.http == null) {
            throw new IllegalStateException("connection not yet open");
        }
        return ((HttpsClient) this.http).getLocalPrincipal();
    }
}
