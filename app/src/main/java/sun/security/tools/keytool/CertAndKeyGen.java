package sun.security.tools.keytool;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PSSParameterSpec;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;
import sun.security.pkcs10.PKCS10;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:sun/security/tools/keytool/CertAndKeyGen.class */
public final class CertAndKeyGen {
    private SecureRandom prng;
    private String sigAlg;
    private KeyPairGenerator keyGen;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public CertAndKeyGen(String str, String str2) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance(str);
        this.sigAlg = str2;
    }

    public CertAndKeyGen(String str, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str3 == null) {
            this.keyGen = KeyPairGenerator.getInstance(str);
        } else {
            try {
                this.keyGen = KeyPairGenerator.getInstance(str, str3);
            } catch (Exception e2) {
                this.keyGen = KeyPairGenerator.getInstance(str);
            }
        }
        this.sigAlg = str2;
    }

    public void setRandom(SecureRandom secureRandom) {
        this.prng = secureRandom;
    }

    public void generate(int i2) throws InvalidKeyException {
        try {
            if (this.prng == null) {
                this.prng = new SecureRandom();
            }
            this.keyGen.initialize(i2, this.prng);
            KeyPair keyPairGenerateKeyPair = this.keyGen.generateKeyPair();
            this.publicKey = keyPairGenerateKeyPair.getPublic();
            this.privateKey = keyPairGenerateKeyPair.getPrivate();
            if (!XMLX509Certificate.JCA_CERT_ID.equalsIgnoreCase(this.publicKey.getFormat())) {
                throw new IllegalArgumentException("publicKey's is not X.509, but " + this.publicKey.getFormat());
            }
        } catch (Exception e2) {
            throw new IllegalArgumentException(e2.getMessage());
        }
    }

    public X509Key getPublicKey() {
        if (!(this.publicKey instanceof X509Key)) {
            return null;
        }
        return (X509Key) this.publicKey;
    }

    public PublicKey getPublicKeyAnyway() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public X509Certificate getSelfCertificate(X500Name x500Name, Date date, long j2) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, NoSuchProviderException {
        return getSelfCertificate(x500Name, date, j2, null);
    }

    public X509Certificate getSelfCertificate(X500Name x500Name, Date date, long j2, CertificateExtensions certificateExtensions) throws NoSuchAlgorithmException, SignatureException, ProviderException, InvalidKeyException, CertificateException, NoSuchProviderException {
        try {
            Date date2 = new Date();
            date2.setTime(date.getTime() + (j2 * 1000));
            GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            gregorianCalendar.setTime(date2);
            if (gregorianCalendar.get(1) > 9999) {
                throw new CertificateException("Validity period ends at calendar year " + gregorianCalendar.get(1) + " which is greater than 9999");
            }
            CertificateValidity certificateValidity = new CertificateValidity(date, date2);
            X509CertInfo x509CertInfo = new X509CertInfo();
            PSSParameterSpec defaultAlgorithmParameterSpec = AlgorithmId.getDefaultAlgorithmParameterSpec(this.sigAlg, this.privateKey);
            x509CertInfo.set("version", new CertificateVersion(2));
            x509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & Integer.MAX_VALUE));
            x509CertInfo.set("algorithmID", new CertificateAlgorithmId(AlgorithmId.getWithParameterSpec(this.sigAlg, defaultAlgorithmParameterSpec)));
            x509CertInfo.set("subject", x500Name);
            x509CertInfo.set("key", new CertificateX509Key(this.publicKey));
            x509CertInfo.set("validity", certificateValidity);
            x509CertInfo.set("issuer", x500Name);
            if (certificateExtensions != null) {
                x509CertInfo.set("extensions", certificateExtensions);
            }
            X509CertImpl x509CertImpl = new X509CertImpl(x509CertInfo);
            x509CertImpl.sign(this.privateKey, defaultAlgorithmParameterSpec, this.sigAlg, null);
            return x509CertImpl;
        } catch (IOException e2) {
            throw new CertificateEncodingException("getSelfCert: " + e2.getMessage());
        } catch (InvalidAlgorithmParameterException e3) {
            throw new SignatureException("Unsupported PSSParameterSpec: " + e3.getMessage());
        }
    }

    public X509Certificate getSelfCertificate(X500Name x500Name, long j2) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, NoSuchProviderException {
        return getSelfCertificate(x500Name, new Date(), j2);
    }

    public PKCS10 getCertRequest(X500Name x500Name) throws SignatureException, InvalidKeyException {
        PKCS10 pkcs10 = new PKCS10(this.publicKey);
        try {
            Signature signature = Signature.getInstance(this.sigAlg);
            signature.initSign(this.privateKey);
            pkcs10.encodeAndSign(x500Name, signature);
            return pkcs10;
        } catch (IOException e2) {
            throw new SignatureException(this.sigAlg + " IOException");
        } catch (NoSuchAlgorithmException e3) {
            throw new SignatureException(this.sigAlg + " unavailable?");
        } catch (CertificateException e4) {
            throw new SignatureException(this.sigAlg + " CertificateException");
        }
    }
}
