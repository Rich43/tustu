package javax.net.ssl;

import java.security.Principal;
import java.security.cert.Certificate;
import java.util.EventObject;
import javax.security.auth.x500.X500Principal;
import javax.security.cert.X509Certificate;

/* loaded from: rt.jar:javax/net/ssl/HandshakeCompletedEvent.class */
public class HandshakeCompletedEvent extends EventObject {
    private static final long serialVersionUID = 7914963744257769778L;
    private transient SSLSession session;

    public HandshakeCompletedEvent(SSLSocket sSLSocket, SSLSession sSLSession) {
        super(sSLSocket);
        this.session = sSLSession;
    }

    public SSLSession getSession() {
        return this.session;
    }

    public String getCipherSuite() {
        return this.session.getCipherSuite();
    }

    public Certificate[] getLocalCertificates() {
        return this.session.getLocalCertificates();
    }

    public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        return this.session.getPeerCertificates();
    }

    public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        return this.session.getPeerCertificateChain();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.security.Principal] */
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        X500Principal subjectX500Principal;
        try {
            subjectX500Principal = this.session.getPeerPrincipal();
        } catch (AbstractMethodError e2) {
            subjectX500Principal = ((java.security.cert.X509Certificate) getPeerCertificates()[0]).getSubjectX500Principal();
        }
        return subjectX500Principal;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [java.security.Principal] */
    public Principal getLocalPrincipal() {
        X500Principal subjectX500Principal;
        try {
            subjectX500Principal = this.session.getLocalPrincipal();
        } catch (AbstractMethodError e2) {
            subjectX500Principal = null;
            Certificate[] localCertificates = getLocalCertificates();
            if (localCertificates != null) {
                subjectX500Principal = ((java.security.cert.X509Certificate) localCertificates[0]).getSubjectX500Principal();
            }
        }
        return subjectX500Principal;
    }

    public SSLSocket getSocket() {
        return (SSLSocket) getSource();
    }
}
