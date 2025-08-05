package java.security.cert;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/* loaded from: rt.jar:java/security/cert/PKIXCertPathChecker.class */
public abstract class PKIXCertPathChecker implements CertPathChecker, Cloneable {
    @Override // java.security.cert.CertPathChecker
    public abstract void init(boolean z2) throws CertPathValidatorException;

    @Override // java.security.cert.CertPathChecker
    public abstract boolean isForwardCheckingSupported();

    public abstract Set<String> getSupportedExtensions();

    public abstract void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException;

    protected PKIXCertPathChecker() {
    }

    @Override // java.security.cert.CertPathChecker
    public void check(Certificate certificate) throws CertPathValidatorException {
        check(certificate, Collections.emptySet());
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }
}
