package sun.security.provider.certpath.ssl;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Provider;
import java.security.cert.CRLSelector;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStoreSpi;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

/* loaded from: rt.jar:sun/security/provider/certpath/ssl/SSLServerCertStore.class */
public final class SSLServerCertStore extends CertStoreSpi {
    private final URI uri;
    private static final SSLSocketFactory socketFactory;
    private static final GetChainTrustManager trustManager = new GetChainTrustManager();
    private static final HostnameVerifier hostnameVerifier = new HostnameVerifier() { // from class: sun.security.provider.certpath.ssl.SSLServerCertStore.1
        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    };

    static {
        SSLSocketFactory socketFactory2;
        try {
            SSLContext sSLContext = SSLContext.getInstance("SSL");
            sSLContext.init(null, new TrustManager[]{trustManager}, null);
            socketFactory2 = sSLContext.getSocketFactory();
        } catch (GeneralSecurityException e2) {
            socketFactory2 = null;
        }
        socketFactory = socketFactory2;
    }

    SSLServerCertStore(URI uri) throws InvalidAlgorithmParameterException {
        super(null);
        this.uri = uri;
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<X509Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException {
        List<X509Certificate> matchingCerts;
        try {
            URLConnection uRLConnectionOpenConnection = this.uri.toURL().openConnection();
            if (uRLConnectionOpenConnection instanceof HttpsURLConnection) {
                if (socketFactory == null) {
                    throw new CertStoreException("No initialized SSLSocketFactory");
                }
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) uRLConnectionOpenConnection;
                httpsURLConnection.setSSLSocketFactory(socketFactory);
                httpsURLConnection.setHostnameVerifier(hostnameVerifier);
                synchronized (trustManager) {
                    try {
                        try {
                            httpsURLConnection.connect();
                            matchingCerts = getMatchingCerts(trustManager.serverChain, certSelector);
                            trustManager.cleanup();
                        } catch (Throwable th) {
                            trustManager.cleanup();
                            throw th;
                        }
                    } catch (IOException e2) {
                        if (trustManager.exchangedServerCerts) {
                            List<X509Certificate> matchingCerts2 = getMatchingCerts(trustManager.serverChain, certSelector);
                            trustManager.cleanup();
                            return matchingCerts2;
                        }
                        throw e2;
                    }
                }
                return matchingCerts;
            }
            return Collections.emptySet();
        } catch (IOException e3) {
            throw new CertStoreException(e3);
        }
    }

    private static List<X509Certificate> getMatchingCerts(List<X509Certificate> list, CertSelector certSelector) {
        if (certSelector == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (X509Certificate x509Certificate : list) {
            if (certSelector.match(x509Certificate)) {
                arrayList.add(x509Certificate);
            }
        }
        return arrayList;
    }

    @Override // java.security.cert.CertStoreSpi
    public Collection<X509CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException {
        throw new UnsupportedOperationException();
    }

    static CertStore getInstance(URI uri) throws InvalidAlgorithmParameterException {
        return new CS(new SSLServerCertStore(uri), null, "SSLServer", null);
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ssl/SSLServerCertStore$GetChainTrustManager.class */
    private static class GetChainTrustManager extends X509ExtendedTrustManager {
        private List<X509Certificate> serverChain;
        private boolean exchangedServerCerts;

        private GetChainTrustManager() {
            this.serverChain = Collections.emptyList();
            this.exchangedServerCerts = false;
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.X509ExtendedTrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.X509ExtendedTrustManager
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            List<X509Certificate> listAsList;
            this.exchangedServerCerts = true;
            if (x509CertificateArr == null) {
                listAsList = Collections.emptyList();
            } else {
                listAsList = Arrays.asList(x509CertificateArr);
            }
            this.serverChain = listAsList;
        }

        @Override // javax.net.ssl.X509ExtendedTrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
            checkServerTrusted(x509CertificateArr, str);
        }

        @Override // javax.net.ssl.X509ExtendedTrustManager
        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
            checkServerTrusted(x509CertificateArr, str);
        }

        void cleanup() {
            this.exchangedServerCerts = false;
            this.serverChain = Collections.emptyList();
        }
    }

    /* loaded from: rt.jar:sun/security/provider/certpath/ssl/SSLServerCertStore$CS.class */
    private static class CS extends CertStore {
        protected CS(CertStoreSpi certStoreSpi, Provider provider, String str, CertStoreParameters certStoreParameters) {
            super(certStoreSpi, provider, str, certStoreParameters);
        }
    }
}
