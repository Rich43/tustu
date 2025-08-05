package java.security.cert;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import sun.security.util.SignatureUtil;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:java/security/cert/X509Certificate.class */
public abstract class X509Certificate extends Certificate implements X509Extension {
    private static final long serialVersionUID = -2491127588187038216L;
    private transient X500Principal subjectX500Principal;
    private transient X500Principal issuerX500Principal;

    public abstract void checkValidity() throws CertificateNotYetValidException, CertificateExpiredException;

    public abstract void checkValidity(Date date) throws CertificateNotYetValidException, CertificateExpiredException;

    public abstract int getVersion();

    public abstract BigInteger getSerialNumber();

    public abstract Principal getIssuerDN();

    public abstract Principal getSubjectDN();

    public abstract Date getNotBefore();

    public abstract Date getNotAfter();

    public abstract byte[] getTBSCertificate() throws CertificateEncodingException;

    public abstract byte[] getSignature();

    public abstract String getSigAlgName();

    public abstract String getSigAlgOID();

    public abstract byte[] getSigAlgParams();

    public abstract boolean[] getIssuerUniqueID();

    public abstract boolean[] getSubjectUniqueID();

    public abstract boolean[] getKeyUsage();

    public abstract int getBasicConstraints();

    protected X509Certificate() {
        super(XMLX509Certificate.JCA_CERT_ID);
    }

    public X500Principal getIssuerX500Principal() {
        if (this.issuerX500Principal == null) {
            this.issuerX500Principal = X509CertImpl.getIssuerX500Principal(this);
        }
        return this.issuerX500Principal;
    }

    public X500Principal getSubjectX500Principal() {
        if (this.subjectX500Principal == null) {
            this.subjectX500Principal = X509CertImpl.getSubjectX500Principal(this);
        }
        return this.subjectX500Principal;
    }

    public List<String> getExtendedKeyUsage() throws CertificateParsingException {
        return X509CertImpl.getExtendedKeyUsage(this);
    }

    public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
        return X509CertImpl.getSubjectAlternativeNames(this);
    }

    public Collection<List<?>> getIssuerAlternativeNames() throws CertificateParsingException {
        return X509CertImpl.getIssuerAlternativeNames(this);
    }

    @Override // java.security.cert.Certificate
    public void verify(PublicKey publicKey, Provider provider) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException {
        Signature signature;
        String sigAlgName = getSigAlgName();
        if (provider == null) {
            signature = Signature.getInstance(sigAlgName);
        } else {
            signature = Signature.getInstance(sigAlgName, provider);
        }
        Signature signature2 = signature;
        try {
            SignatureUtil.initVerifyWithParam(signature2, publicKey, SignatureUtil.getParamSpec(sigAlgName, getSigAlgParams()));
            byte[] tBSCertificate = getTBSCertificate();
            signature2.update(tBSCertificate, 0, tBSCertificate.length);
            if (!signature2.verify(getSignature())) {
                throw new SignatureException("Signature does not match.");
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new CertificateException(e2);
        } catch (ProviderException e3) {
            throw new CertificateException(e3.getMessage(), e3.getCause());
        }
    }
}
