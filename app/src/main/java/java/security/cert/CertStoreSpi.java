package java.security.cert;

import java.security.InvalidAlgorithmParameterException;
import java.util.Collection;

/* loaded from: rt.jar:java/security/cert/CertStoreSpi.class */
public abstract class CertStoreSpi {
    public abstract Collection<? extends Certificate> engineGetCertificates(CertSelector certSelector) throws CertStoreException;

    public abstract Collection<? extends CRL> engineGetCRLs(CRLSelector cRLSelector) throws CertStoreException;

    public CertStoreSpi(CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
    }
}
