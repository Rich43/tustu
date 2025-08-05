package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/ConstraintsChecker.class */
class ConstraintsChecker extends PKIXCertPathChecker {
    private static final Debug debug = Debug.getInstance("certpath");
    private final int certPathLength;
    private int maxPathLength;

    /* renamed from: i, reason: collision with root package name */
    private int f13643i;
    private NameConstraintsExtension prevNC;
    private Set<String> supportedExts;

    ConstraintsChecker(int i2) {
        this.certPathLength = i2;
    }

    @Override // java.security.cert.PKIXCertPathChecker, java.security.cert.CertPathChecker
    public void init(boolean z2) throws CertPathValidatorException {
        if (!z2) {
            this.f13643i = 0;
            this.maxPathLength = this.certPathLength;
            this.prevNC = null;
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
            this.supportedExts = new HashSet(2);
            this.supportedExts.add(PKIXExtensions.BasicConstraints_Id.toString());
            this.supportedExts.add(PKIXExtensions.NameConstraints_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    @Override // java.security.cert.PKIXCertPathChecker
    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        this.f13643i++;
        checkBasicConstraints(x509Certificate);
        verifyNameConstraints(x509Certificate);
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.BasicConstraints_Id.toString());
            collection.remove(PKIXExtensions.NameConstraints_Id.toString());
        }
    }

    private void verifyNameConstraints(X509Certificate x509Certificate) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("---checking name constraints...");
        }
        if (this.prevNC != null && (this.f13643i == this.certPathLength || !X509CertImpl.isSelfIssued(x509Certificate))) {
            if (debug != null) {
                debug.println("prevNC = " + ((Object) this.prevNC) + ", currDN = " + ((Object) x509Certificate.getSubjectX500Principal()));
            }
            try {
                if (!this.prevNC.verify(x509Certificate)) {
                    throw new CertPathValidatorException("name constraints check failed", null, null, -1, PKIXReason.INVALID_NAME);
                }
            } catch (IOException e2) {
                throw new CertPathValidatorException(e2);
            }
        }
        this.prevNC = mergeNameConstraints(x509Certificate, this.prevNC);
        if (debug != null) {
            debug.println("name constraints verified.");
        }
    }

    static NameConstraintsExtension mergeNameConstraints(X509Certificate x509Certificate, NameConstraintsExtension nameConstraintsExtension) throws CertPathValidatorException {
        try {
            NameConstraintsExtension nameConstraintsExtension2 = X509CertImpl.toImpl(x509Certificate).getNameConstraintsExtension();
            if (debug != null) {
                debug.println("prevNC = " + ((Object) nameConstraintsExtension) + ", newNC = " + String.valueOf(nameConstraintsExtension2));
            }
            if (nameConstraintsExtension == null) {
                if (debug != null) {
                    debug.println("mergedNC = " + String.valueOf(nameConstraintsExtension2));
                }
                if (nameConstraintsExtension2 == null) {
                    return nameConstraintsExtension2;
                }
                return (NameConstraintsExtension) nameConstraintsExtension2.clone();
            }
            try {
                nameConstraintsExtension.merge(nameConstraintsExtension2);
                if (debug != null) {
                    debug.println("mergedNC = " + ((Object) nameConstraintsExtension));
                }
                return nameConstraintsExtension;
            } catch (IOException e2) {
                throw new CertPathValidatorException(e2);
            }
        } catch (CertificateException e3) {
            throw new CertPathValidatorException(e3);
        }
    }

    private void checkBasicConstraints(X509Certificate x509Certificate) throws CertPathValidatorException {
        if (debug != null) {
            debug.println("---checking basic constraints...");
            debug.println("i = " + this.f13643i + ", maxPathLength = " + this.maxPathLength);
        }
        if (this.f13643i < this.certPathLength) {
            int basicConstraints = -1;
            if (x509Certificate.getVersion() < 3) {
                if (this.f13643i == 1 && X509CertImpl.isSelfIssued(x509Certificate)) {
                    basicConstraints = Integer.MAX_VALUE;
                }
            } else {
                basicConstraints = x509Certificate.getBasicConstraints();
            }
            if (basicConstraints == -1) {
                throw new CertPathValidatorException("basic constraints check failed: this is not a CA certificate", null, null, -1, PKIXReason.NOT_CA_CERT);
            }
            if (!X509CertImpl.isSelfIssued(x509Certificate)) {
                if (this.maxPathLength <= 0) {
                    throw new CertPathValidatorException("basic constraints check failed: pathLenConstraint violated - this cert must be the last cert in the certification path", null, null, -1, PKIXReason.PATH_TOO_LONG);
                }
                this.maxPathLength--;
            }
            if (basicConstraints < this.maxPathLength) {
                this.maxPathLength = basicConstraints;
            }
        }
        if (debug != null) {
            debug.println("after processing, maxPathLength = " + this.maxPathLength);
            debug.println("basic constraints verified.");
        }
    }

    static int mergeBasicConstraints(X509Certificate x509Certificate, int i2) {
        int basicConstraints = x509Certificate.getBasicConstraints();
        if (!X509CertImpl.isSelfIssued(x509Certificate)) {
            i2--;
        }
        if (basicConstraints < i2) {
            i2 = basicConstraints;
        }
        return i2;
    }
}
