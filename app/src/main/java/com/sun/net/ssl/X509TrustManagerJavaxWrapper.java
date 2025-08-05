package com.sun.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/X509TrustManagerJavaxWrapper.class */
final class X509TrustManagerJavaxWrapper implements javax.net.ssl.X509TrustManager {
    private X509TrustManager theX509TrustManager;

    X509TrustManagerJavaxWrapper(X509TrustManager x509TrustManager) {
        this.theX509TrustManager = x509TrustManager;
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        if (!this.theX509TrustManager.isClientTrusted(x509CertificateArr)) {
            throw new CertificateException("Untrusted Client Certificate Chain");
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        if (!this.theX509TrustManager.isServerTrusted(x509CertificateArr)) {
            throw new CertificateException("Untrusted Server Certificate Chain");
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.theX509TrustManager.getAcceptedIssuers();
    }
}
