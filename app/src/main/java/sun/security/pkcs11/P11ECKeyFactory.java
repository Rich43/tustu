package sun.security.pkcs11;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.DerValue;
import sun.security.util.ECUtil;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11ECKeyFactory.class */
final class P11ECKeyFactory extends P11KeyFactory {
    private static Provider sunECprovider;

    private static Provider getSunECProvider() {
        if (sunECprovider == null) {
            sunECprovider = Security.getProvider("SunEC");
            if (sunECprovider == null) {
                throw new RuntimeException("Cannot load SunEC provider");
            }
        }
        return sunECprovider;
    }

    P11ECKeyFactory(Token token, String str) {
        super(token, str);
    }

    static ECParameterSpec getECParameterSpec(String str) {
        return ECUtil.getECParameterSpec(getSunECProvider(), str);
    }

    static ECParameterSpec getECParameterSpec(int i2) {
        return ECUtil.getECParameterSpec(getSunECProvider(), i2);
    }

    static ECParameterSpec getECParameterSpec(ECParameterSpec eCParameterSpec) {
        return ECUtil.getECParameterSpec(getSunECProvider(), eCParameterSpec);
    }

    static ECParameterSpec decodeParameters(byte[] bArr) throws IOException {
        return ECUtil.getECParameterSpec(getSunECProvider(), bArr);
    }

    static byte[] encodeParameters(ECParameterSpec eCParameterSpec) {
        return ECUtil.encodeECParameterSpec(getSunECProvider(), eCParameterSpec);
    }

    static ECPoint decodePoint(byte[] bArr, EllipticCurve ellipticCurve) throws IOException {
        return ECUtil.decodePoint(bArr, ellipticCurve);
    }

    static byte[] getEncodedPublicValue(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof ECPublicKey) {
            ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
            return ECUtil.encodePoint(eCPublicKey.getW(), eCPublicKey.getParams().getCurve());
        }
        throw new InvalidKeyException("Key class not yet supported: " + publicKey.getClass().getName());
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        try {
            if (publicKey instanceof ECPublicKey) {
                ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
                return generatePublic(eCPublicKey.getW(), eCPublicKey.getParams());
            }
            if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
                try {
                    return implTranslatePublicKey(P11ECUtil.decodeX509ECPublicKey(publicKey.getEncoded()));
                } catch (InvalidKeySpecException e2) {
                    throw new InvalidKeyException(e2);
                }
            }
            throw new InvalidKeyException("PublicKey must be instance of ECPublicKey or have X.509 encoding");
        } catch (PKCS11Exception e3) {
            throw new InvalidKeyException("Could not create EC public key", e3);
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        try {
            if (privateKey instanceof ECPrivateKey) {
                ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
                return generatePrivate(eCPrivateKey.getS(), eCPrivateKey.getParams());
            }
            if ("PKCS#8".equals(privateKey.getFormat())) {
                try {
                    return implTranslatePrivateKey(P11ECUtil.decodePKCS8ECPrivateKey(privateKey.getEncoded()));
                } catch (InvalidKeySpecException e2) {
                    throw new InvalidKeyException(e2);
                }
            }
            throw new InvalidKeyException("PrivateKey must be instance of ECPrivateKey or have PKCS#8 encoding");
        } catch (PKCS11Exception e3) {
            throw new InvalidKeyException("Could not create EC private key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return implTranslatePublicKey(P11ECUtil.decodeX509ECPublicKey(((X509EncodedKeySpec) keySpec).getEncoded()));
            } catch (InvalidKeyException e2) {
                throw new InvalidKeySpecException("Could not create EC public key", e2);
            }
        }
        if (!(keySpec instanceof ECPublicKeySpec)) {
            throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
        }
        try {
            ECPublicKeySpec eCPublicKeySpec = (ECPublicKeySpec) keySpec;
            return generatePublic(eCPublicKeySpec.getW(), eCPublicKeySpec.getParams());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create EC public key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return implTranslatePrivateKey(P11ECUtil.decodePKCS8ECPrivateKey(((PKCS8EncodedKeySpec) keySpec).getEncoded()));
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not create EC private key", e2);
            }
        }
        if (!(keySpec instanceof ECPrivateKeySpec)) {
            throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
        }
        try {
            ECPrivateKeySpec eCPrivateKeySpec = (ECPrivateKeySpec) keySpec;
            return generatePrivate(eCPrivateKeySpec.getS(), eCPrivateKeySpec.getParams());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create EC private key", e3);
        }
    }

    private PublicKey generatePublic(ECPoint eCPoint, ECParameterSpec eCParameterSpec) throws PKCS11Exception {
        byte[] bArrEncodeECParameterSpec = ECUtil.encodeECParameterSpec(getSunECProvider(), eCParameterSpec);
        byte[] bArrEncodePoint = ECUtil.encodePoint(eCPoint, eCParameterSpec.getCurve());
        if (!this.token.config.getUseEcX963Encoding()) {
            try {
                bArrEncodePoint = new DerValue((byte) 4, bArrEncodePoint).toByteArray();
            } catch (IOException e2) {
                throw new IllegalArgumentException("Could not DER encode point", e2);
            }
        }
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 2L, 3L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 2L), new CK_ATTRIBUTE(256L, 3L), new CK_ATTRIBUTE(385L, bArrEncodePoint), new CK_ATTRIBUTE(384L, bArrEncodeECParameterSpec)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PublicKey publicKey = P11Key.publicKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "EC", eCParameterSpec.getCurve().getField().getFieldSize(), attributes);
            this.token.releaseSession(objSession);
            return publicKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    private PrivateKey generatePrivate(BigInteger bigInteger, ECParameterSpec eCParameterSpec) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 3L, 3L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3L), new CK_ATTRIBUTE(256L, 3L), new CK_ATTRIBUTE(17L, bigInteger), new CK_ATTRIBUTE(384L, ECUtil.encodeECParameterSpec(getSunECProvider(), eCParameterSpec))});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PrivateKey privateKey = P11Key.privateKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "EC", eCParameterSpec.getCurve().getField().getFieldSize(), attributes);
            this.token.releaseSession(objSession);
            return privateKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPublicKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(ECPublicKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(385L), new CK_ATTRIBUTE(384L)};
            try {
                try {
                    this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                    ECParameterSpec eCParameterSpecDecodeParameters = decodeParameters(ck_attributeArr[1].getByteArray());
                    T tCast = cls.cast(new ECPublicKeySpec(decodePoint(ck_attributeArr[0].getByteArray(), eCParameterSpecDecodeParameters.getCurve()), eCParameterSpecDecodeParameters));
                    p11Key.releaseKeyID();
                    return tCast;
                } catch (IOException e2) {
                    throw new InvalidKeySpecException("Could not parse key", e2);
                }
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPrivateKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(ECPrivateKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(384L)};
            try {
                try {
                    this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                    T tCast = cls.cast(new ECPrivateKeySpec(ck_attributeArr[0].getBigInteger(), decodeParameters(ck_attributeArr[1].getByteArray())));
                    p11Key.releaseKeyID();
                    return tCast;
                } catch (IOException e2) {
                    throw new InvalidKeySpecException("Could not parse key", e2);
                }
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    KeyFactory implGetSoftwareFactory() throws GeneralSecurityException {
        return KeyFactory.getInstance("EC", getSunECProvider());
    }
}
