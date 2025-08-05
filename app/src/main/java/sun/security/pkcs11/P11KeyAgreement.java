package sun.security.pkcs11;

import java.math.BigInteger;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.KeyUtil;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyAgreement.class */
final class P11KeyAgreement extends KeyAgreementSpi {
    private final Token token;
    private final String algorithm;
    private final long mechanism;
    private P11Key privateKey;
    private BigInteger publicValue;
    private int secretLen;
    private KeyAgreement multiPartyAgreement;

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyAgreement$AllowKDF.class */
    private static class AllowKDF {
        private static final boolean VALUE = getValue();

        private AllowKDF() {
        }

        private static boolean getValue() {
            return ((Boolean) AccessController.doPrivileged(() -> {
                return Boolean.valueOf(Boolean.getBoolean("jdk.crypto.KeyAgreement.legacyKDF"));
            })).booleanValue();
        }
    }

    P11KeyAgreement(Token token, String str, long j2) {
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (!(key instanceof PrivateKey)) {
            throw new InvalidKeyException("Key must be instance of PrivateKey");
        }
        this.privateKey = P11KeyFactory.convertKey(this.token, key, this.algorithm);
        this.publicValue = null;
        this.multiPartyAgreement = null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("Parameters not supported");
        }
        engineInit(key, secureRandom);
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected Key engineDoPhase(Key key, boolean z2) throws IllegalStateException, InvalidKeyException {
        BigInteger y2;
        BigInteger p2;
        BigInteger g2;
        if (this.privateKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        if (this.publicValue != null) {
            throw new IllegalStateException("Phase already executed");
        }
        if (this.multiPartyAgreement != null || !z2) {
            if (this.multiPartyAgreement == null) {
                try {
                    this.multiPartyAgreement = KeyAgreement.getInstance("DH", P11Util.getSunJceProvider());
                    this.multiPartyAgreement.init(this.privateKey);
                } catch (NoSuchAlgorithmException e2) {
                    throw new InvalidKeyException("Could not initialize multi party agreement", e2);
                }
            }
            return this.multiPartyAgreement.doPhase(key, z2);
        }
        if (!(key instanceof PublicKey) || !key.getAlgorithm().equals(this.algorithm)) {
            throw new InvalidKeyException("Key must be a PublicKey with algorithm DH");
        }
        if (key instanceof DHPublicKey) {
            DHPublicKey dHPublicKey = (DHPublicKey) key;
            KeyUtil.validate(dHPublicKey);
            y2 = dHPublicKey.getY();
            DHParameterSpec params = dHPublicKey.getParams();
            p2 = params.getP();
            g2 = params.getG();
        } else {
            try {
                DHPublicKeySpec dHPublicKeySpec = (DHPublicKeySpec) new P11DHKeyFactory(this.token, "DH").engineGetKeySpec(key, DHPublicKeySpec.class);
                KeyUtil.validate(dHPublicKeySpec);
                y2 = dHPublicKeySpec.getY();
                p2 = dHPublicKeySpec.getP();
                g2 = dHPublicKeySpec.getG();
            } catch (InvalidKeySpecException e3) {
                throw new InvalidKeyException("Could not obtain key values", e3);
            }
        }
        if (this.privateKey instanceof DHPrivateKey) {
            DHParameterSpec params2 = ((DHPrivateKey) this.privateKey).getParams();
            if (!p2.equals(params2.getP()) || !g2.equals(params2.getG())) {
                throw new InvalidKeyException("PublicKey DH parameters must match PrivateKey DH parameters");
            }
        }
        this.publicValue = y2;
        this.secretLen = (p2.bitLength() + 7) >> 3;
        return null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.multiPartyAgreement != null) {
            byte[] bArrGenerateSecret = this.multiPartyAgreement.generateSecret();
            this.multiPartyAgreement = null;
            return bArrGenerateSecret;
        }
        if (this.privateKey == null || this.publicValue == null) {
            throw new IllegalStateException("Not initialized correctly");
        }
        long keyID = this.privateKey.getKeyID();
        try {
            try {
                Session opSession = this.token.getOpSession();
                long jC_DeriveKey = this.token.p11.C_DeriveKey(opSession.id(), new CK_MECHANISM(this.mechanism, this.publicValue), keyID, this.token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L)}));
                CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L)};
                this.token.p11.C_GetAttributeValue(opSession.id(), jC_DeriveKey, ck_attributeArr);
                byte[] byteArray = ck_attributeArr[0].getByteArray();
                this.token.p11.C_DestroyObject(opSession.id(), jC_DeriveKey);
                if (byteArray.length != this.secretLen) {
                    if (byteArray.length > this.secretLen) {
                        throw new ProviderException("generated secret is out-of-range");
                    }
                    byte[] bArr = new byte[this.secretLen];
                    System.arraycopy(byteArray, 0, bArr, this.secretLen - byteArray.length, byteArray.length);
                    this.privateKey.releaseKeyID();
                    this.publicValue = null;
                    this.token.releaseSession(opSession);
                    return bArr;
                }
                this.privateKey.releaseKeyID();
                this.publicValue = null;
                this.token.releaseSession(opSession);
                return byteArray;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("Could not derive key", e2);
            }
        } catch (Throwable th) {
            this.privateKey.releaseKeyID();
            this.publicValue = null;
            this.token.releaseSession(null);
            throw th;
        }
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        if (this.multiPartyAgreement != null) {
            int iGenerateSecret = this.multiPartyAgreement.generateSecret(bArr, i2);
            this.multiPartyAgreement = null;
            return iGenerateSecret;
        }
        if (i2 + this.secretLen > bArr.length) {
            throw new ShortBufferException("Need " + this.secretLen + " bytes, only " + (bArr.length - i2) + " available");
        }
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        System.arraycopy(bArrEngineGenerateSecret, 0, bArr, i2, bArrEngineGenerateSecret.length);
        return bArrEngineGenerateSecret.length;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        int length;
        if (this.multiPartyAgreement != null) {
            SecretKey secretKeyGenerateSecret = this.multiPartyAgreement.generateSecret(str);
            this.multiPartyAgreement = null;
            return secretKeyGenerateSecret;
        }
        if (str == null) {
            throw new NoSuchAlgorithmException("Algorithm must not be null");
        }
        if (str.equals("TlsPremasterSecret")) {
            return nativeGenerateSecret(str);
        }
        if (!str.equalsIgnoreCase("TlsPremasterSecret") && !AllowKDF.VALUE) {
            throw new NoSuchAlgorithmException("Unsupported secret key algorithm: " + str);
        }
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        if (str.equalsIgnoreCase("DES")) {
            length = 8;
        } else if (str.equalsIgnoreCase("DESede")) {
            length = 24;
        } else if (str.equalsIgnoreCase("Blowfish")) {
            length = Math.min(56, bArrEngineGenerateSecret.length);
        } else if (str.equalsIgnoreCase("TlsPremasterSecret")) {
            length = bArrEngineGenerateSecret.length;
        } else {
            throw new NoSuchAlgorithmException("Unknown algorithm " + str);
        }
        if (bArrEngineGenerateSecret.length < length) {
            throw new InvalidKeyException("Secret too short");
        }
        if (str.equalsIgnoreCase("DES") || str.equalsIgnoreCase("DESede")) {
            for (int i2 = 0; i2 < length; i2 += 8) {
                P11SecretKeyFactory.fixDESParity(bArrEngineGenerateSecret, i2);
            }
        }
        return new SecretKeySpec(bArrEngineGenerateSecret, 0, length, str);
    }

    private SecretKey nativeGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] encoded;
        byte[] bArrTrimZeroes;
        if (this.privateKey == null || this.publicValue == null) {
            throw new IllegalStateException("Not initialized correctly");
        }
        Session objSession = null;
        long keyID = this.privateKey.getKeyID();
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L)});
                long jC_DeriveKey = this.token.p11.C_DeriveKey(objSession.id(), new CK_MECHANISM(this.mechanism, this.publicValue), keyID, attributes);
                CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(353L)};
                this.token.p11.C_GetAttributeValue(objSession.id(), jC_DeriveKey, ck_attributeArr);
                SecretKey secretKey = P11Key.secretKey(objSession, jC_DeriveKey, str, ((int) ck_attributeArr[0].getLong()) << 3, attributes);
                if ("RAW".equals(secretKey.getFormat()) && encoded != (bArrTrimZeroes = KeyUtil.trimZeroes((encoded = secretKey.getEncoded())))) {
                    secretKey = new SecretKeySpec(bArrTrimZeroes, str);
                }
                SecretKey secretKey2 = secretKey;
                this.privateKey.releaseKeyID();
                this.publicValue = null;
                this.token.releaseSession(objSession);
                return secretKey2;
            } catch (PKCS11Exception e2) {
                throw new InvalidKeyException("Could not derive key", e2);
            }
        } catch (Throwable th) {
            this.privateKey.releaseKeyID();
            this.publicValue = null;
            this.token.releaseSession(objSession);
            throw th;
        }
    }
}
