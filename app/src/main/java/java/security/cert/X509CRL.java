package java.security.cert;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.util.SignatureUtil;
import sun.security.x509.X509CRLImpl;

/* loaded from: rt.jar:java/security/cert/X509CRL.class */
public abstract class X509CRL extends CRL implements X509Extension {
    private transient X500Principal issuerPrincipal;

    public abstract byte[] getEncoded() throws CRLException;

    public abstract void verify(PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException;

    public abstract void verify(PublicKey publicKey, String str) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException, NoSuchProviderException;

    public abstract int getVersion();

    public abstract Principal getIssuerDN();

    public abstract Date getThisUpdate();

    public abstract Date getNextUpdate();

    public abstract X509CRLEntry getRevokedCertificate(BigInteger bigInteger);

    public abstract Set<? extends X509CRLEntry> getRevokedCertificates();

    public abstract byte[] getTBSCertList() throws CRLException;

    public abstract byte[] getSignature();

    public abstract String getSigAlgName();

    public abstract String getSigAlgOID();

    public abstract byte[] getSigAlgParams();

    protected X509CRL() {
        super(XMLX509Certificate.JCA_CERT_ID);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X509CRL)) {
            return false;
        }
        try {
            return Arrays.equals(X509CRLImpl.getEncodedInternal(this), X509CRLImpl.getEncodedInternal((X509CRL) obj));
        } catch (CRLException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 0;
        try {
            byte[] encodedInternal = X509CRLImpl.getEncodedInternal(this);
            for (int i3 = 1; i3 < encodedInternal.length; i3++) {
                i2 += encodedInternal[i3] * i3;
            }
            return i2;
        } catch (CRLException e2) {
            return i2;
        }
    }

    public void verify(PublicKey publicKey, Provider provider) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CRLException {
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
            byte[] tBSCertList = getTBSCertList();
            signature2.update(tBSCertList, 0, tBSCertList.length);
            if (!signature2.verify(getSignature())) {
                throw new SignatureException("Signature does not match.");
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new CRLException(e2);
        } catch (ProviderException e3) {
            throw new CRLException(e3.getMessage(), e3.getCause());
        }
    }

    public X500Principal getIssuerX500Principal() {
        if (this.issuerPrincipal == null) {
            this.issuerPrincipal = X509CRLImpl.getIssuerX500Principal(this);
        }
        return this.issuerPrincipal;
    }

    public X509CRLEntry getRevokedCertificate(X509Certificate x509Certificate) {
        if (!x509Certificate.getIssuerX500Principal().equals(getIssuerX500Principal())) {
            return null;
        }
        return getRevokedCertificate(x509Certificate.getSerialNumber());
    }
}
