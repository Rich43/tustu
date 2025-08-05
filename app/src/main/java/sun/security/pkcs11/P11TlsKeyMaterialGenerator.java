package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import sun.security.internal.spec.TlsKeyMaterialParameterSpec;
import sun.security.internal.spec.TlsKeyMaterialSpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_OUT;
import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_PARAMS;
import sun.security.pkcs11.wrapper.CK_SSL3_RANDOM_DATA;
import sun.security.pkcs11.wrapper.CK_TLS12_KEY_MAT_PARAMS;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11TlsKeyMaterialGenerator.class */
public final class P11TlsKeyMaterialGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsKeyMaterialGenerator must be initialized using a TlsKeyMaterialParameterSpec";
    private final Token token;
    private final String algorithm;
    private long mechanism;
    private TlsKeyMaterialParameterSpec spec;
    private P11Key p11Key;
    private int version;

    P11TlsKeyMaterialGenerator(Token token, String str, long j2) throws PKCS11Exception {
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof TlsKeyMaterialParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsKeyMaterialParameterSpec) algorithmParameterSpec;
        try {
            this.p11Key = P11SecretKeyFactory.convertKey(this.token, this.spec.getMasterSecret(), "TlsMasterSecret");
            this.version = (this.spec.getMajorVersion() << 8) | this.spec.getMinorVersion();
            if (this.version < 768 && this.version > 771) {
                throw new InvalidAlgorithmParameterException("Only SSL 3.0, TLS 1.0, TLS 1.1, and TLS 1.2 are supported");
            }
        } catch (InvalidKeyException e2) {
            throw new InvalidAlgorithmParameterException("init() failed", e2);
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r17v3 */
    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        boolean z2;
        CK_ATTRIBUTE[] ck_attributeArr;
        SecretKey secretKey;
        SecretKey secretKey2;
        SecretKey secretKey3;
        SecretKey secretKey4;
        if (this.spec == null) {
            throw new IllegalStateException("TlsKeyMaterialGenerator must be initialized");
        }
        if (this.version == 768) {
            this.mechanism = 882L;
        } else if (this.version == 769 || this.version == 770) {
            this.mechanism = 886L;
        }
        int macKeyLength = this.spec.getMacKeyLength() << 3;
        int ivLength = this.spec.getIvLength() << 3;
        int expandedCipherKeyLength = this.spec.getExpandedCipherKeyLength() << 3;
        int cipherKeyLength = this.spec.getCipherKeyLength() << 3;
        if (expandedCipherKeyLength != 0) {
            z2 = true;
        } else {
            z2 = false;
            expandedCipherKeyLength = cipherKeyLength;
        }
        CK_SSL3_RANDOM_DATA ck_ssl3_random_data = new CK_SSL3_RANDOM_DATA(this.spec.getClientRandom(), this.spec.getServerRandom());
        CK_TLS12_KEY_MAT_PARAMS ck_tls12_key_mat_params = null;
        CK_MECHANISM ck_mechanism = null;
        if (this.version < 771) {
            ?? ck_ssl3_key_mat_params = new CK_SSL3_KEY_MAT_PARAMS(macKeyLength, cipherKeyLength, ivLength, z2, ck_ssl3_random_data);
            ck_mechanism = new CK_MECHANISM(this.mechanism, (CK_SSL3_KEY_MAT_PARAMS) ck_ssl3_key_mat_params);
            ck_tls12_key_mat_params = ck_ssl3_key_mat_params;
        } else if (this.version == 771) {
            CK_TLS12_KEY_MAT_PARAMS ck_tls12_key_mat_params2 = new CK_TLS12_KEY_MAT_PARAMS(macKeyLength, cipherKeyLength, ivLength, z2, ck_ssl3_random_data, Functions.getHashMechId(this.spec.getPRFHashAlg()));
            ck_mechanism = new CK_MECHANISM(this.mechanism, ck_tls12_key_mat_params2);
            ck_tls12_key_mat_params = ck_tls12_key_mat_params2;
        }
        String cipherAlgorithm = this.spec.getCipherAlgorithm();
        long keyType = P11SecretKeyFactory.getKeyType(cipherAlgorithm);
        if (keyType < 0) {
            if (cipherKeyLength != 0) {
                throw new ProviderException("Unknown algorithm: " + this.spec.getCipherAlgorithm());
            }
            keyType = 16;
        }
        try {
            try {
                Session objSession = this.token.getObjSession();
                if (cipherKeyLength != 0) {
                    ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(256L, keyType), new CK_ATTRIBUTE(353L, expandedCipherKeyLength >> 3)};
                } else {
                    ck_attributeArr = new CK_ATTRIBUTE[0];
                }
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, keyType, ck_attributeArr);
                try {
                    this.token.p11.C_DeriveKey(objSession.id(), ck_mechanism, this.p11Key.getKeyID(), attributes);
                    this.p11Key.releaseKeyID();
                    CK_SSL3_KEY_MAT_OUT ck_ssl3_key_mat_out = null;
                    if (ck_tls12_key_mat_params instanceof CK_SSL3_KEY_MAT_PARAMS) {
                        ck_ssl3_key_mat_out = ((CK_SSL3_KEY_MAT_PARAMS) ck_tls12_key_mat_params).pReturnedKeyMaterial;
                    } else if (ck_tls12_key_mat_params instanceof CK_TLS12_KEY_MAT_PARAMS) {
                        ck_ssl3_key_mat_out = ck_tls12_key_mat_params.pReturnedKeyMaterial;
                    }
                    if (macKeyLength != 0) {
                        secretKey = P11Key.secretKey(objSession, ck_ssl3_key_mat_out.hClientMacSecret, "MAC", macKeyLength, attributes);
                        secretKey2 = P11Key.secretKey(objSession, ck_ssl3_key_mat_out.hServerMacSecret, "MAC", macKeyLength, attributes);
                    } else {
                        secretKey = null;
                        secretKey2 = null;
                    }
                    if (cipherKeyLength != 0) {
                        secretKey3 = P11Key.secretKey(objSession, ck_ssl3_key_mat_out.hClientKey, cipherAlgorithm, expandedCipherKeyLength, attributes);
                        secretKey4 = P11Key.secretKey(objSession, ck_ssl3_key_mat_out.hServerKey, cipherAlgorithm, expandedCipherKeyLength, attributes);
                    } else {
                        secretKey3 = null;
                        secretKey4 = null;
                    }
                    TlsKeyMaterialSpec tlsKeyMaterialSpec = new TlsKeyMaterialSpec(secretKey, secretKey2, secretKey3, ck_ssl3_key_mat_out.pIVClient == null ? null : new IvParameterSpec(ck_ssl3_key_mat_out.pIVClient), secretKey4, ck_ssl3_key_mat_out.pIVServer == null ? null : new IvParameterSpec(ck_ssl3_key_mat_out.pIVServer));
                    this.token.releaseSession(objSession);
                    return tlsKeyMaterialSpec;
                } catch (Throwable th) {
                    this.p11Key.releaseKeyID();
                    throw th;
                }
            } catch (Exception e2) {
                throw new ProviderException("Could not generate key", e2);
            }
        } catch (Throwable th2) {
            this.token.releaseSession(null);
            throw th2;
        }
    }
}
