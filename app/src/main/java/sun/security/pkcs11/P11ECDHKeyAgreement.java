package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_ECDH1_DERIVE_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11ECDHKeyAgreement.class */
final class P11ECDHKeyAgreement extends KeyAgreementSpi {
    private final Token token;
    private final String algorithm;
    private final long mechanism;
    private P11Key privateKey;
    private byte[] publicValue;
    private int secretLen;

    P11ECDHKeyAgreement(Token token, String str, long j2) {
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected void engineInit(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (!(key instanceof PrivateKey)) {
            throw new InvalidKeyException("Key must be instance of PrivateKey");
        }
        this.privateKey = P11KeyFactory.convertKey(this.token, key, "EC");
        this.publicValue = null;
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
        if (this.privateKey == null) {
            throw new IllegalStateException("Not initialized");
        }
        if (this.publicValue != null) {
            throw new IllegalStateException("Phase already executed");
        }
        if (!z2) {
            throw new IllegalStateException("Only two party agreement supported, lastPhase must be true");
        }
        if (!(key instanceof ECPublicKey)) {
            throw new InvalidKeyException("Key must be a PublicKey with algorithm EC");
        }
        ECPublicKey eCPublicKey = (ECPublicKey) key;
        this.secretLen = (eCPublicKey.getParams().getCurve().getField().getFieldSize() + 7) >> 3;
        this.publicValue = P11ECKeyFactory.getEncodedPublicValue(eCPublicKey);
        return null;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.privateKey == null || this.publicValue == null) {
            throw new IllegalStateException("Not initialized correctly");
        }
        Session opSession = null;
        long keyID = this.privateKey.getKeyID();
        try {
            try {
                opSession = this.token.getOpSession();
                long jC_DeriveKey = this.token.p11.C_DeriveKey(opSession.id(), new CK_MECHANISM(this.mechanism, new CK_ECDH1_DERIVE_PARAMS(1L, null, this.publicValue)), keyID, this.token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L)}));
                CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L)};
                this.token.p11.C_GetAttributeValue(opSession.id(), jC_DeriveKey, ck_attributeArr);
                byte[] byteArray = ck_attributeArr[0].getByteArray();
                this.token.p11.C_DestroyObject(opSession.id(), jC_DeriveKey);
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
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected int engineGenerateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        if (i2 + this.secretLen > bArr.length) {
            throw new ShortBufferException("Need " + this.secretLen + " bytes, only " + (bArr.length - i2) + " available");
        }
        byte[] bArrEngineGenerateSecret = engineGenerateSecret();
        System.arraycopy(bArrEngineGenerateSecret, 0, bArr, i2, bArrEngineGenerateSecret.length);
        return bArrEngineGenerateSecret.length;
    }

    @Override // javax.crypto.KeyAgreementSpi
    protected SecretKey engineGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        if (str == null) {
            throw new NoSuchAlgorithmException("Algorithm must not be null");
        }
        if (!str.equals("TlsPremasterSecret")) {
            throw new NoSuchAlgorithmException("Only supported for algorithm TlsPremasterSecret");
        }
        return nativeGenerateSecret(str);
    }

    private SecretKey nativeGenerateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        if (this.privateKey == null || this.publicValue == null) {
            throw new IllegalStateException("Not initialized correctly");
        }
        Session objSession = null;
        long keyID = this.privateKey.getKeyID();
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, 16L)};
                CK_ECDH1_DERIVE_PARAMS ck_ecdh1_derive_params = new CK_ECDH1_DERIVE_PARAMS(1L, null, this.publicValue);
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, 16L, ck_attributeArr);
                long jC_DeriveKey = this.token.p11.C_DeriveKey(objSession.id(), new CK_MECHANISM(this.mechanism, ck_ecdh1_derive_params), keyID, attributes);
                CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(353L)};
                this.token.p11.C_GetAttributeValue(objSession.id(), jC_DeriveKey, ck_attributeArr2);
                SecretKey secretKey = P11Key.secretKey(objSession, jC_DeriveKey, str, ((int) ck_attributeArr2[0].getLong()) << 3, attributes);
                this.privateKey.releaseKeyID();
                this.publicValue = null;
                this.token.releaseSession(objSession);
                return secretKey;
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
