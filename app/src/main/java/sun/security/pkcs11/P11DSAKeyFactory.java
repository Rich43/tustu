package sun.security.pkcs11;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11DSAKeyFactory.class */
final class P11DSAKeyFactory extends P11KeyFactory {
    P11DSAKeyFactory(Token token, String str) {
        super(token, str);
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        try {
            if (publicKey instanceof DSAPublicKey) {
                DSAPublicKey dSAPublicKey = (DSAPublicKey) publicKey;
                DSAParams params = dSAPublicKey.getParams();
                return generatePublic(dSAPublicKey.getY(), params.getP(), params.getQ(), params.getG());
            }
            if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
                return implTranslatePublicKey(new sun.security.provider.DSAPublicKey(publicKey.getEncoded()));
            }
            throw new InvalidKeyException("PublicKey must be instance of DSAPublicKey or have X.509 encoding");
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not create DSA public key", e2);
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        try {
            if (privateKey instanceof DSAPrivateKey) {
                DSAPrivateKey dSAPrivateKey = (DSAPrivateKey) privateKey;
                DSAParams params = dSAPrivateKey.getParams();
                return generatePrivate(dSAPrivateKey.getX(), params.getP(), params.getQ(), params.getG());
            }
            if ("PKCS#8".equals(privateKey.getFormat())) {
                return implTranslatePrivateKey(new sun.security.provider.DSAPrivateKey(privateKey.getEncoded()));
            }
            throw new InvalidKeyException("PrivateKey must be instance of DSAPrivateKey or have PKCS#8 encoding");
        } catch (PKCS11Exception e2) {
            throw new InvalidKeyException("Could not create DSA private key", e2);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return implTranslatePublicKey(new sun.security.provider.DSAPublicKey(((X509EncodedKeySpec) keySpec).getEncoded()));
            } catch (InvalidKeyException e2) {
                throw new InvalidKeySpecException("Could not create DSA public key", e2);
            }
        }
        if (!(keySpec instanceof DSAPublicKeySpec)) {
            throw new InvalidKeySpecException("Only DSAPublicKeySpec and X509EncodedKeySpec supported for DSA public keys");
        }
        try {
            DSAPublicKeySpec dSAPublicKeySpec = (DSAPublicKeySpec) keySpec;
            return generatePublic(dSAPublicKeySpec.getY(), dSAPublicKeySpec.getP(), dSAPublicKeySpec.getQ(), dSAPublicKeySpec.getG());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create DSA public key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return implTranslatePrivateKey(new sun.security.provider.DSAPrivateKey(((PKCS8EncodedKeySpec) keySpec).getEncoded()));
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not create DSA private key", e2);
            }
        }
        if (!(keySpec instanceof DSAPrivateKeySpec)) {
            throw new InvalidKeySpecException("Only DSAPrivateKeySpec and PKCS8EncodedKeySpec supported for DSA private keys");
        }
        try {
            DSAPrivateKeySpec dSAPrivateKeySpec = (DSAPrivateKeySpec) keySpec;
            return generatePrivate(dSAPrivateKeySpec.getX(), dSAPrivateKeySpec.getP(), dSAPrivateKeySpec.getQ(), dSAPrivateKeySpec.getG());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create DSA private key", e3);
        }
    }

    private PublicKey generatePublic(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 2L, 1L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 2L), new CK_ATTRIBUTE(256L, 1L), new CK_ATTRIBUTE(17L, bigInteger), new CK_ATTRIBUTE(304L, bigInteger2), new CK_ATTRIBUTE(305L, bigInteger3), new CK_ATTRIBUTE(306L, bigInteger4)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PublicKey publicKey = P11Key.publicKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "DSA", bigInteger2.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return publicKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    private PrivateKey generatePrivate(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 3L, 1L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3L), new CK_ATTRIBUTE(256L, 1L), new CK_ATTRIBUTE(17L, bigInteger), new CK_ATTRIBUTE(304L, bigInteger2), new CK_ATTRIBUTE(305L, bigInteger3), new CK_ATTRIBUTE(306L, bigInteger4)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PrivateKey privateKey = P11Key.privateKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "DSA", bigInteger2.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return privateKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPublicKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(DSAPublicKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(305L), new CK_ATTRIBUTE(306L)};
            try {
                this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                p11Key.releaseKeyID();
                return cls.cast(new DSAPublicKeySpec(ck_attributeArr[0].getBigInteger(), ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger(), ck_attributeArr[3].getBigInteger()));
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only DSAPublicKeySpec and X509EncodedKeySpec supported for DSA public keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPrivateKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(DSAPrivateKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(305L), new CK_ATTRIBUTE(306L)};
            try {
                this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                p11Key.releaseKeyID();
                return cls.cast(new DSAPrivateKeySpec(ck_attributeArr[0].getBigInteger(), ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger(), ck_attributeArr[3].getBigInteger()));
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only DSAPrivateKeySpec and PKCS8EncodedKeySpec supported for DSA private keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    KeyFactory implGetSoftwareFactory() throws GeneralSecurityException {
        return KeyFactory.getInstance("DSA", P11Util.getSunProvider());
    }
}
