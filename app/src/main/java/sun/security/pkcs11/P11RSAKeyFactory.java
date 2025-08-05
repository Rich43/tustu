package sun.security.pkcs11;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.rsa.RSAKeyFactory;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11RSAKeyFactory.class */
final class P11RSAKeyFactory extends P11KeyFactory {
    P11RSAKeyFactory(Token token, String str) {
        super(token, str);
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        try {
            if (publicKey instanceof RSAPublicKey) {
                RSAPublicKey rSAPublicKey = (RSAPublicKey) publicKey;
                return generatePublic(rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
            }
            if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
                return implTranslatePublicKey(RSAPublicKeyImpl.newKey(publicKey.getEncoded()));
            }
            throw new InvalidKeyException("PublicKey must be instance of RSAPublicKey or have X.509 encoding");
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not create RSA public key", e2);
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        try {
            if (privateKey instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
                return generatePrivate(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient());
            }
            if (privateKey instanceof RSAPrivateKey) {
                RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) privateKey;
                return generatePrivate(rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent());
            }
            if ("PKCS#8".equals(privateKey.getFormat())) {
                return implTranslatePrivateKey(RSAPrivateCrtKeyImpl.newKey(privateKey.getEncoded()));
            }
            throw new InvalidKeyException("Private key must be instance of RSAPrivate(Crt)Key or have PKCS#8 encoding");
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not create RSA private key", e2);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return implTranslatePublicKey(RSAPublicKeyImpl.newKey(((X509EncodedKeySpec) keySpec).getEncoded()));
            } catch (InvalidKeyException e2) {
                throw new InvalidKeySpecException("Could not create RSA public key", e2);
            }
        }
        if (!(keySpec instanceof RSAPublicKeySpec)) {
            throw new InvalidKeySpecException("Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys");
        }
        try {
            RSAPublicKeySpec rSAPublicKeySpec = (RSAPublicKeySpec) keySpec;
            return generatePublic(rSAPublicKeySpec.getModulus(), rSAPublicKeySpec.getPublicExponent());
        } catch (InvalidKeyException | PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create RSA public key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return implTranslatePrivateKey(RSAPrivateCrtKeyImpl.newKey(((PKCS8EncodedKeySpec) keySpec).getEncoded()));
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not create RSA private key", e2);
            }
        }
        try {
            if (keySpec instanceof RSAPrivateCrtKeySpec) {
                RSAPrivateCrtKeySpec rSAPrivateCrtKeySpec = (RSAPrivateCrtKeySpec) keySpec;
                return generatePrivate(rSAPrivateCrtKeySpec.getModulus(), rSAPrivateCrtKeySpec.getPublicExponent(), rSAPrivateCrtKeySpec.getPrivateExponent(), rSAPrivateCrtKeySpec.getPrimeP(), rSAPrivateCrtKeySpec.getPrimeQ(), rSAPrivateCrtKeySpec.getPrimeExponentP(), rSAPrivateCrtKeySpec.getPrimeExponentQ(), rSAPrivateCrtKeySpec.getCrtCoefficient());
            }
            if (keySpec instanceof RSAPrivateKeySpec) {
                RSAPrivateKeySpec rSAPrivateKeySpec = (RSAPrivateKeySpec) keySpec;
                return generatePrivate(rSAPrivateKeySpec.getModulus(), rSAPrivateKeySpec.getPrivateExponent());
            }
            throw new InvalidKeySpecException("Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys");
        } catch (InvalidKeyException | PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create RSA private key", e3);
        }
    }

    private PublicKey generatePublic(BigInteger bigInteger, BigInteger bigInteger2) throws PKCS11Exception, InvalidKeyException {
        RSAKeyFactory.checkKeyLengths(bigInteger.bitLength(), bigInteger2, -1, 65536);
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 2L, 0L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 2L), new CK_ATTRIBUTE(256L, 0L), new CK_ATTRIBUTE(288L, bigInteger), new CK_ATTRIBUTE(290L, bigInteger2)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PublicKey publicKey = P11Key.publicKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "RSA", bigInteger.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return publicKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    private PrivateKey generatePrivate(BigInteger bigInteger, BigInteger bigInteger2) throws PKCS11Exception, InvalidKeyException {
        RSAKeyFactory.checkKeyLengths(bigInteger.bitLength(), null, -1, 65536);
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 3L, 0L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3L), new CK_ATTRIBUTE(256L, 0L), new CK_ATTRIBUTE(288L, bigInteger), new CK_ATTRIBUTE(291L, bigInteger2)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PrivateKey privateKey = P11Key.privateKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "RSA", bigInteger.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return privateKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    private PrivateKey generatePrivate(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8) throws PKCS11Exception, InvalidKeyException {
        RSAKeyFactory.checkKeyLengths(bigInteger.bitLength(), bigInteger2, -1, 65536);
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 3L, 0L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3L), new CK_ATTRIBUTE(256L, 0L), new CK_ATTRIBUTE(288L, bigInteger), new CK_ATTRIBUTE(290L, bigInteger2), new CK_ATTRIBUTE(291L, bigInteger3), new CK_ATTRIBUTE(292L, bigInteger4), new CK_ATTRIBUTE(293L, bigInteger5), new CK_ATTRIBUTE(294L, bigInteger6), new CK_ATTRIBUTE(295L, bigInteger7), new CK_ATTRIBUTE(296L, bigInteger8)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PrivateKey privateKey = P11Key.privateKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "RSA", bigInteger.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return privateKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPublicKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(RSAPublicKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(288L), new CK_ATTRIBUTE(290L)};
            try {
                this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                p11Key.releaseKeyID();
                return cls.cast(new RSAPublicKeySpec(ck_attributeArr[0].getBigInteger(), ck_attributeArr[1].getBigInteger()));
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPrivateKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (p11Key.sensitive || !p11Key.extractable) {
            throw new InvalidKeySpecException("Key is sensitive or not extractable");
        }
        if (cls.isAssignableFrom(RSAPrivateCrtKeySpec.class)) {
            if (p11Key instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) p11Key;
                return cls.cast(new RSAPrivateCrtKeySpec(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient(), rSAPrivateCrtKey.getParams()));
            }
            if (!cls.isAssignableFrom(RSAPrivateKeySpec.class)) {
                throw new InvalidKeySpecException("RSAPrivateCrtKeySpec can only be used with CRT keys");
            }
            if (!(p11Key instanceof RSAPrivateKey)) {
                throw new InvalidKeySpecException("Key must be an instance of RSAPrivateKeySpec. Was " + ((Object) p11Key.getClass()));
            }
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) p11Key;
            return cls.cast(new RSAPrivateKeySpec(rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent(), rSAPrivateKey.getParams()));
        }
        throw new InvalidKeySpecException("Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    KeyFactory implGetSoftwareFactory() throws GeneralSecurityException {
        return KeyFactory.getInstance("RSA", P11Util.getSunRsaSignProvider());
    }
}
