package javax.net.ssl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/* loaded from: rt.jar:javax/net/ssl/HttpsURLConnection.class */
public abstract class HttpsURLConnection extends HttpURLConnection {
    protected HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;
    private static HostnameVerifier defaultHostnameVerifier = new DefaultHostnameVerifier();
    private static SSLSocketFactory defaultSSLSocketFactory = null;

    public abstract String getCipherSuite();

    public abstract Certificate[] getLocalCertificates();

    public abstract Certificate[] getServerCertificates() throws SSLPeerUnverifiedException;

    protected HttpsURLConnection(URL url) {
        super(url);
        this.hostnameVerifier = defaultHostnameVerifier;
        this.sslSocketFactory = getDefaultSSLSocketFactory();
    }

    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return ((X509Certificate) getServerCertificates()[0]).getSubjectX500Principal();
    }

    public Principal getLocalPrincipal() {
        Certificate[] localCertificates = getLocalCertificates();
        if (localCertificates != null) {
            return ((X509Certificate) localCertificates[0]).getSubjectX500Principal();
        }
        return null;
    }

    /* loaded from: rt.jar:javax/net/ssl/HttpsURLConnection$DefaultHostnameVerifier.class */
    private static class DefaultHostnameVerifier implements HostnameVerifier {
        private DefaultHostnameVerifier() {
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            return false;
        }
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
