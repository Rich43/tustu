package java.security.cert;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:java/security/cert/CertificateFactory.class */
public class CertificateFactory {
    private String type;
    private Provider provider;
    private CertificateFactorySpi certFacSpi;

    protected CertificateFactory(CertificateFactorySpi certificateFactorySpi, Provider provider, String str) {
        this.certFacSpi = certificateFactorySpi;
        this.provider = provider;
        this.type = str;
    }

    public static final CertificateFactory getInstance(String str) throws CertificateException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertificateFactory", (Class<?>) CertificateFactorySpi.class, str);
            return new CertificateFactory((CertificateFactorySpi) getInstance.impl, getInstance.provider, str);
        } catch (NoSuchAlgorithmException e2) {
            throw new CertificateException(str + " not found", e2);
        }
    }

    public static final CertificateFactory getInstance(String str, String str2) throws CertificateException, NoSuchProviderException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertificateFactory", (Class<?>) CertificateFactorySpi.class, str, str2);
            return new CertificateFactory((CertificateFactorySpi) getInstance.impl, getInstance.provider, str);
        } catch (NoSuchAlgorithmException e2) {
            throw new CertificateException(str + " not found", e2);
        }
    }

    public static final CertificateFactory getInstance(String str, Provider provider) throws CertificateException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertificateFactory", (Class<?>) CertificateFactorySpi.class, str, provider);
            return new CertificateFactory((CertificateFactorySpi) getInstance.impl, getInstance.provider, str);
        } catch (NoSuchAlgorithmException e2) {
            throw new CertificateException(str + " not found", e2);
        }
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final String getType() {
        return this.type;
    }

    public final Certificate generateCertificate(InputStream inputStream) throws CertificateException {
        return this.certFacSpi.engineGenerateCertificate(inputStream);
    }

    public final Iterator<String> getCertPathEncodings() {
        return this.certFacSpi.engineGetCertPathEncodings();
    }

    public final CertPath generateCertPath(InputStream inputStream) throws CertificateException {
        return this.certFacSpi.engineGenerateCertPath(inputStream);
    }

    public final CertPath generateCertPath(InputStream inputStream, String str) throws CertificateException {
        return this.certFacSpi.engineGenerateCertPath(inputStream, str);
    }

    public final CertPath generateCertPath(List<? extends Certificate> list) throws CertificateException {
        return this.certFacSpi.engineGenerateCertPath(list);
    }

    public final Collection<? extends Certificate> generateCertificates(InputStream inputStream) throws CertificateException {
        return this.certFacSpi.engineGenerateCertificates(inputStream);
    }

    public final CRL generateCRL(InputStream inputStream) throws CRLException {
        return this.certFacSpi.engineGenerateCRL(inputStream);
    }

    public final Collection<? extends CRL> generateCRLs(InputStream inputStream) throws CRLException {
        return this.certFacSpi.engineGenerateCRLs(inputStream);
    }
}
