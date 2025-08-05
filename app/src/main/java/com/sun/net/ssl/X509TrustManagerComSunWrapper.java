package com.sun.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/X509TrustManagerComSunWrapper.class */
final class X509TrustManagerComSunWrapper implements X509TrustManager {
    private javax.net.ssl.X509TrustManager theX509TrustManager;

    X509TrustManagerComSunWrapper(javax.net.ssl.X509TrustManager x509TrustManager) {
        this.theX509TrustManager = x509TrustManager;
    }

    @Override // com.sun.net.ssl.X509TrustManager
    public boolean isClientTrusted(X509Certificate[] x509CertificateArr) {
        try {
            this.theX509TrustManager.checkClientTrusted(x509CertificateArr, "UNKNOWN");
            return true;
        } catch (CertificateException e2) {
            return false;
        }
    }

    @Override // com.sun.net.ssl.X509TrustManager
    public boolean isServerTrusted(X509Certificate[] x509CertificateArr) {
        try {
            this.theX509TrustManager.checkServerTrusted(x509CertificateArr, "UNKNOWN");
            return true;
        } catch (CertificateException e2) {
            return false;
        }
    }

    @Override // com.sun.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.theX509TrustManager.getAcceptedIssuers();
    }
}
