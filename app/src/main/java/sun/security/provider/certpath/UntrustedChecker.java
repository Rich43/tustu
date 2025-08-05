package sun.security.provider.certpath;

import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.util.UntrustedCertificates;

/* loaded from: rt.jar:sun/security/provider/certpath/UntrustedChecker.class */
public final class UntrustedChecker extends PKIXCertPathChecker {
    private static final Debug debug = Debug.getInstance("certpath");

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public void init(boolean z2) throws CertPathValidatorException {
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public boolean isForwardCheckingSupported() {
        return true;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public Set<String> getSupportedExtensions() {
        return null;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        if (UntrustedCertificates.isUntrusted(x509Certificate)) {
            if (debug != null) {
                debug.println("UntrustedChecker: untrusted certificate " + ((Object) x509Certificate.getSubjectX500Principal()));
            }
            throw new CertPathValidatorException("Untrusted certificate: " + ((Object) x509Certificate.getSubjectX500Principal()));
        }
    }
}
