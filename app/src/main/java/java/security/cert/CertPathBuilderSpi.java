package java.security.cert;

import java.security.InvalidAlgorithmParameterException;

/* loaded from: rt.jar:java/security/cert/CertPathBuilderSpi.class */
public abstract class CertPathBuilderSpi {
    public abstract CertPathBuilderResult engineBuild(CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException;

    public CertPathChecker engineGetRevocationChecker() {
        throw new UnsupportedOperationException();
    }
}
