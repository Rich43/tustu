package com.sun.net.ssl;

import java.security.cert.X509Certificate;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/X509TrustManager.class */
public interface X509TrustManager extends TrustManager {
    boolean isClientTrusted(X509Certificate[] x509CertificateArr);

    boolean isServerTrusted(X509Certificate[] x509CertificateArr);

    X509Certificate[] getAcceptedIssuers();
}
