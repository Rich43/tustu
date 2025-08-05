package sun.security.provider.certpath;

import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.PKIXExtensions;

/* loaded from: rt.jar:sun/security/provider/certpath/KeyChecker.class */
class KeyChecker extends PKIXCertPathChecker {
    private static final Debug debug = Debug.getInstance("certpath");
    private final int certPathLen;
    private final CertSelector targetConstraints;
    private int remainingCerts;
    private Set<String> supportedExts;
    private static final int KEY_CERT_SIGN = 5;

    KeyChecker(int i2, CertSelector certSelector) {
        this.certPathLen = i2;
        this.targetConstraints = certSelector;
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public void init(boolean z2) throws CertPathValidatorException {
        if (!z2) {
            this.remainingCerts = this.certPathLen;
            return;
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public boolean isForwardCheckingSupported() {
        return false;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public Set<String> getSupportedExtensions() {
        if (this.supportedExts == null) {
            this.supportedExts = new HashSet(3);
            this.supportedExts.add(PKIXExtensions.KeyUsage_Id.toString());
            this.supportedExts.add(PKIXExtensions.ExtendedKeyUsage_Id.toString());
            this.supportedExts.add(PKIXExtensions.SubjectAlternativeName_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        this.remainingCerts--;
        if (this.remainingCerts == 0) {
            if (this.targetConstraints != null && !this.targetConstraints.match(x509Certificate)) {
                throw new CertPathValidatorException("target certificate constraints check failed");
            }
        } else {
            verifyCAKeyUsage(x509Certificate);
        }
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.KeyUsage_Id.toString());
            collection.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
            collection.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
        }
    }

    static void verifyCAKeyUsage(X509Certificate x509Certificate) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("KeyChecker.verifyCAKeyUsage() ---checking CA key usage...");
        }
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage == null) {
            return;
        }
        if (!keyUsage[5]) {
            throw new CertPathValidatorException("CA key usage check failed: keyCertSign bit is not set", null, null, -1, PKIXReason.INVALID_KEY_USAGE);
        }
        if (debug != null) {
            debug.println("KeyChecker.verifyCAKeyUsage() CA key usage verified.");
        }
    }
}
