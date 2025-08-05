package java.security.cert;

/* loaded from: rt.jar:java/security/cert/CertPathChecker.class */
public interface CertPathChecker {
    void init(boolean z2) throws CertPathValidatorException;

    boolean isForwardCheckingSupported();

    void check(Certificate certificate) throws CertPathValidatorException;
}
