package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import sun.security.internal.spec.TlsMasterSecretParameterSpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_SSL3_MASTER_KEY_DERIVE_PARAMS;
import sun.security.pkcs11.wrapper.CK_SSL3_RANDOM_DATA;
import sun.security.pkcs11.wrapper.CK_TLS12_MASTER_KEY_DERIVE_PARAMS;
import sun.security.pkcs11.wrapper.CK_VERSION;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11TlsMasterSecretGenerator.class */
public final class P11TlsMasterSecretGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsMasterSecretGenerator must be initialized using a TlsMasterSecretParameterSpec";
    private final Token token;
    private final String algorithm;
    private long mechanism;
    private TlsMasterSecretParameterSpec spec;
    private P11Key p11Key;
    int version;

    P11TlsMasterSecretGenerator(Token token, String str, long j2) throws PKCS11Exception {
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
        if (!(algorithmParameterSpec instanceof TlsMasterSecretParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsMasterSecretParameterSpec) algorithmParameterSpec;
        try {
            this.p11Key = P11SecretKeyFactory.convertKey(this.token, this.spec.getPremasterSecret(), null);
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

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        CK_VERSION ck_version;
        byte b2;
        byte b3;
        if (this.spec == null) {
            throw new IllegalStateException("TlsMasterSecretGenerator must be initialized");
        }
        boolean zEquals = this.p11Key.getAlgorithm().equals("TlsRsaPremasterSecret");
        if (this.version == 768) {
            this.mechanism = zEquals ? 881L : 883L;
        } else if (this.version == 769 || this.version == 770) {
            this.mechanism = zEquals ? 885L : 887L;
        } else if (this.version == 771) {
            this.mechanism = zEquals ? 992L : 994L;
        }
        if (zEquals) {
            ck_version = new CK_VERSION(0, 0);
        } else {
            ck_version = null;
        }
        CK_SSL3_RANDOM_DATA ck_ssl3_random_data = new CK_SSL3_RANDOM_DATA(this.spec.getClientRandom(), this.spec.getServerRandom());
        CK_MECHANISM ck_mechanism = null;
        if (this.version < 771) {
            ck_mechanism = new CK_MECHANISM(this.mechanism, new CK_SSL3_MASTER_KEY_DERIVE_PARAMS(ck_ssl3_random_data, ck_version));
        } else if (this.version == 771) {
            ck_mechanism = new CK_MECHANISM(this.mechanism, new CK_TLS12_MASTER_KEY_DERIVE_PARAMS(ck_ssl3_random_data, ck_version, Functions.getHashMechId(this.spec.getPRFHashAlg())));
        }
        Session objSession = null;
        long keyID = this.p11Key.getKeyID();
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[0]);
                long jC_DeriveKey = this.token.p11.C_DeriveKey(objSession.id(), ck_mechanism, keyID, attributes);
                if (ck_version == null) {
                    b2 = -1;
                    b3 = -1;
                } else {
                    b2 = ck_version.major;
                    b3 = ck_version.minor;
                }
                SecretKey secretKeyMasterSecretKey = P11Key.masterSecretKey(objSession, jC_DeriveKey, "TlsMasterSecret", 384, attributes, b2, b3);
                this.p11Key.releaseKeyID();
                this.token.releaseSession(objSession);
                return secretKeyMasterSecretKey;
            } catch (Exception e2) {
                throw new ProviderException("Could not generate key", e2);
            }
        } catch (Throwable th) {
            this.p11Key.releaseKeyID();
            this.token.releaseSession(objSession);
            throw th;
        }
    }
}
