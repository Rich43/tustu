package sun.security.pkcs11;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11DHKeyFactory.class */
final class P11DHKeyFactory extends P11KeyFactory {
    P11DHKeyFactory(Token token, String str) {
        super(token, str);
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        try {
            if (publicKey instanceof DHPublicKey) {
                DHPublicKey dHPublicKey = (DHPublicKey) publicKey;
                DHParameterSpec params = dHPublicKey.getParams();
                return generatePublic(dHPublicKey.getY(), params.getP(), params.getG());
            }
            if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
                try {
                    return implTranslatePublicKey((PublicKey) implGetSoftwareFactory().translateKey(publicKey));
                } catch (GeneralSecurityException e2) {
                    throw new InvalidKeyException("Could not translate key", e2);
                }
            }
            throw new InvalidKeyException("PublicKey must be instance of DHPublicKey or have X.509 encoding");
        } catch (PKCS11Exception e3) {
            throw new InvalidKeyException("Could not create DH public key", e3);
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        try {
            if (privateKey instanceof DHPrivateKey) {
                DHPrivateKey dHPrivateKey = (DHPrivateKey) privateKey;
                DHParameterSpec params = dHPrivateKey.getParams();
                return generatePrivate(dHPrivateKey.getX(), params.getP(), params.getG());
            }
            if ("PKCS#8".equals(privateKey.getFormat())) {
                try {
                    return implTranslatePrivateKey((PrivateKey) implGetSoftwareFactory().translateKey(privateKey));
                } catch (GeneralSecurityException e2) {
                    throw new InvalidKeyException("Could not translate key", e2);
                }
            }
            throw new InvalidKeyException("PrivateKey must be instance of DHPrivateKey or have PKCS#8 encoding");
        } catch (PKCS11Exception e3) {
            throw new InvalidKeyException("Could not create DH private key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return implTranslatePublicKey(implGetSoftwareFactory().generatePublic(keySpec));
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not create DH public key", e2);
            }
        }
        if (!(keySpec instanceof DHPublicKeySpec)) {
            throw new InvalidKeySpecException("Only DHPublicKeySpec and X509EncodedKeySpec supported for DH public keys");
        }
        try {
            DHPublicKeySpec dHPublicKeySpec = (DHPublicKeySpec) keySpec;
            return generatePublic(dHPublicKeySpec.getY(), dHPublicKeySpec.getP(), dHPublicKeySpec.getG());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create DH public key", e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return implTranslatePrivateKey(implGetSoftwareFactory().generatePrivate(keySpec));
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not create DH private key", e2);
            }
        }
        if (!(keySpec instanceof DHPrivateKeySpec)) {
            throw new InvalidKeySpecException("Only DHPrivateKeySpec and PKCS8EncodedKeySpec supported for DH private keys");
        }
        try {
            DHPrivateKeySpec dHPrivateKeySpec = (DHPrivateKeySpec) keySpec;
            return generatePrivate(dHPrivateKeySpec.getX(), dHPrivateKeySpec.getP(), dHPrivateKeySpec.getG());
        } catch (PKCS11Exception e3) {
            throw new InvalidKeySpecException("Could not create DH private key", e3);
        }
    }

    private PublicKey generatePublic(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 2L, 2L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 2L), new CK_ATTRIBUTE(256L, 2L), new CK_ATTRIBUTE(17L, bigInteger), new CK_ATTRIBUTE(304L, bigInteger2), new CK_ATTRIBUTE(306L, bigInteger3)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PublicKey publicKey = P11Key.publicKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "DH", bigInteger2.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return publicKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    private PrivateKey generatePrivate(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes = this.token.getAttributes("import", 3L, 2L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3L), new CK_ATTRIBUTE(256L, 2L), new CK_ATTRIBUTE(17L, bigInteger), new CK_ATTRIBUTE(304L, bigInteger2), new CK_ATTRIBUTE(306L, bigInteger3)});
        Session objSession = null;
        try {
            objSession = this.token.getObjSession();
            PrivateKey privateKey = P11Key.privateKey(objSession, this.token.p11.C_CreateObject(objSession.id(), attributes), "DH", bigInteger2.bitLength(), attributes);
            this.token.releaseSession(objSession);
            return privateKey;
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPublicKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(DHPublicKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(306L)};
            try {
                this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                p11Key.releaseKeyID();
                return cls.cast(new DHPublicKeySpec(ck_attributeArr[0].getBigInteger(), ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger()));
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only DHPublicKeySpec and X509EncodedKeySpec supported for DH public keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    <T extends KeySpec> T implGetPrivateKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException {
        if (cls.isAssignableFrom(DHPrivateKeySpec.class)) {
            sessionArr[0] = this.token.getObjSession();
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L), new CK_ATTRIBUTE(304L), new CK_ATTRIBUTE(306L)};
            try {
                this.token.p11.C_GetAttributeValue(sessionArr[0].id(), p11Key.getKeyID(), ck_attributeArr);
                p11Key.releaseKeyID();
                return cls.cast(new DHPrivateKeySpec(ck_attributeArr[0].getBigInteger(), ck_attributeArr[1].getBigInteger(), ck_attributeArr[2].getBigInteger()));
            } catch (Throwable th) {
                p11Key.releaseKeyID();
                throw th;
            }
        }
        throw new InvalidKeySpecException("Only DHPrivateKeySpec and PKCS8EncodedKeySpec supported for DH private keys");
    }

    @Override // sun.security.pkcs11.P11KeyFactory
    KeyFactory implGetSoftwareFactory() throws GeneralSecurityException {
        return KeyFactory.getInstance("DH", P11Util.getSunJceProvider());
    }
}
