package com.sun.net.ssl.internal.www.protocol.https;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import sun.security.util.DerValue;
import sun.security.util.HostnameChecker;
import sun.security.x509.X500Name;

/* compiled from: DelegateHttpsURLConnection.java */
/* loaded from: rt.jar:com/sun/net/ssl/internal/www/protocol/https/VerifierWrapper.class */
class VerifierWrapper implements HostnameVerifier {
    private com.sun.net.ssl.HostnameVerifier verifier;

    VerifierWrapper(com.sun.net.ssl.HostnameVerifier hostnameVerifier) {
        this.verifier = hostnameVerifier;
    }

    @Override // javax.net.ssl.HostnameVerifier
    public boolean verify(String str, SSLSession sSLSession) {
        String servername;
        try {
            if (sSLSession.getCipherSuite().startsWith("TLS_KRB5")) {
                servername = HostnameChecker.getServerName(getPeerPrincipal(sSLSession));
            } else {
                Certificate[] peerCertificates = sSLSession.getPeerCertificates();
                if (peerCertificates == null || peerCertificates.length == 0 || !(peerCertificates[0] instanceof X509Certificate)) {
                    return false;
                }
                servername = getServername((X509Certificate) peerCertificates[0]);
            }
            if (servername == null) {
                return false;
            }
            return this.verifier.verify(str, servername);
        } catch (SSLPeerUnverifiedException e2) {
            return false;
        }
    }

    private Principal getPeerPrincipal(SSLSession sSLSession) throws SSLPeerUnverifiedException {
        Principal peerPrincipal;
        try {
            peerPrincipal = sSLSession.getPeerPrincipal();
        } catch (AbstractMethodError e2) {
            peerPrincipal = null;
        }
        return peerPrincipal;
    }

    private static String getServername(X509Certificate x509Certificate) {
        try {
            Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames != null) {
                for (List<?> list : subjectAlternativeNames) {
                    if (((Integer) list.get(0)).intValue() == 2) {
                        return (String) list.get(1);
                    }
                }
            }
            DerValue derValueFindMostSpecificAttribute = HostnameChecker.getSubjectX500Name(x509Certificate).findMostSpecificAttribute(X500Name.commonName_oid);
            if (derValueFindMostSpecificAttribute != null) {
                try {
                    return derValueFindMostSpecificAttribute.getAsString();
                } catch (IOException e2) {
                }
            }
            return null;
        } catch (CertificateException e3) {
            return null;
        }
    }
}
