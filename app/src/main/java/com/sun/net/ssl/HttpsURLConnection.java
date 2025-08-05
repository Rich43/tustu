package com.sun.net.ssl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLSocketFactory;
import javax.security.cert.X509Certificate;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/HttpsURLConnection.class */
public abstract class HttpsURLConnection extends HttpURLConnection {
    protected HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;
    private static HostnameVerifier defaultHostnameVerifier = new HostnameVerifier() { // from class: com.sun.net.ssl.HttpsURLConnection.1
        @Override // com.sun.net.ssl.HostnameVerifier
        public boolean verify(String str, String str2) {
            return false;
        }
    };
    private static SSLSocketFactory defaultSSLSocketFactory = null;

    public abstract String getCipherSuite();

    public abstract X509Certificate[] getServerCertificateChain();

    public HttpsURLConnection(URL url) throws IOException {
        super(url);
        this.hostnameVerifier = defaultHostnameVerifier;
        this.sslSocketFactory = getDefaultSSLSocketFactory();
    }

    public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier) {
        if (hostnameVerifier == null) {
            throw new IllegalArgumentException("no default HostnameVerifier specified");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SSLPermission("setHostnameVerifier"));
        }
        defaultHostnameVerifier = hostnameVerifier;
    }

    public static HostnameVerifier getDefaultHostnameVerifier() {
        return defaultHostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        if (hostnameVerifier == null) {
            throw new IllegalArgumentException("no HostnameVerifier specified");
        }
        this.hostnameVerifier = hostnameVerifier;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public static void setDefaultSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        if (sSLSocketFactory == null) {
            throw new IllegalArgumentException("no default SSLSocketFactory specified");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        defaultSSLSocketFactory = sSLSocketFactory;
    }

    public static SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSSLSocketFactory == null) {
            defaultSSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        return defaultSSLSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        if (sSLSocketFactory == null) {
            throw new IllegalArgumentException("no SSLSocketFactory specified");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        this.sslSocketFactory = sSLSocketFactory;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.sslSocketFactory;
    }
}
