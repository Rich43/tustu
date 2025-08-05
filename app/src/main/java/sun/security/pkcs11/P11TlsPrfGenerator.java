package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.security.internal.spec.TlsPrfParameterSpec;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_TLS_MAC_PARAMS;
import sun.security.pkcs11.wrapper.CK_TLS_PRF_PARAMS;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11TlsPrfGenerator.class */
final class P11TlsPrfGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsPrfGenerator must be initialized using a TlsPrfParameterSpec";
    private final Token token;
    private final String algorithm;
    private final long mechanism;
    private TlsPrfParameterSpec spec;
    private P11Key p11Key;
    private static final SecretKey NULL_KEY = new SecretKey() { // from class: sun.security.pkcs11.P11TlsPrfGenerator.1
        @Override // java.security.Key
        public byte[] getEncoded() {
            return new byte[0];
        }

        @Override // java.security.Key
        public String getFormat() {
            return "RAW";
        }

        @Override // java.security.Key
        public String getAlgorithm() {
            return "Generic";
        }
    };

    P11TlsPrfGenerator(Token token, String str, long j2) throws PKCS11Exception {
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
        if (!(algorithmParameterSpec instanceof TlsPrfParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsPrfParameterSpec) algorithmParameterSpec;
        SecretKey secret = this.spec.getSecret();
        if (secret == null) {
            secret = NULL_KEY;
        }
        try {
            this.p11Key = P11SecretKeyFactory.convertKey(this.token, secret, null);
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
        if (this.spec == null) {
            throw new IllegalStateException("TlsPrfGenerator must be initialized");
        }
        byte[] seed = this.spec.getSeed();
        if (this.mechanism == 996) {
            int i2 = 0;
            if (this.spec.getLabel().equals("server finished")) {
                i2 = 1;
            }
            if (this.spec.getLabel().equals("client finished")) {
                i2 = 2;
            }
            if (i2 != 0) {
                CK_TLS_MAC_PARAMS ck_tls_mac_params = new CK_TLS_MAC_PARAMS(Functions.getHashMechId(this.spec.getPRFHashAlg()), this.spec.getOutputLength(), i2);
                Session opSession = null;
                long keyID = this.p11Key.getKeyID();
                try {
                    try {
                        opSession = this.token.getOpSession();
                        this.token.p11.C_SignInit(opSession.id(), new CK_MECHANISM(this.mechanism, ck_tls_mac_params), keyID);
                        this.token.p11.C_SignUpdate(opSession.id(), 0L, seed, 0, seed.length);
                        SecretKeySpec secretKeySpec = new SecretKeySpec(this.token.p11.C_SignFinal(opSession.id(), this.spec.getOutputLength()), "TlsPrf");
                        this.p11Key.releaseKeyID();
                        this.token.releaseSession(opSession);
                        return secretKeySpec;
                    } finally {
                    }
                } catch (PKCS11Exception e2) {
                    throw new ProviderException("Could not calculate PRF", e2);
                }
            }
            throw new ProviderException("Only Finished message authentication code generation supported for TLS 1.2.");
        }
        byte[] bytesUTF8 = P11Util.getBytesUTF8(this.spec.getLabel());
        if (this.mechanism == PKCS11Constants.CKM_NSS_TLS_PRF_GENERAL) {
            Session opSession2 = null;
            long keyID2 = this.p11Key.getKeyID();
            try {
                try {
                    opSession2 = this.token.getOpSession();
                    this.token.p11.C_SignInit(opSession2.id(), new CK_MECHANISM(this.mechanism), keyID2);
                    this.token.p11.C_SignUpdate(opSession2.id(), 0L, bytesUTF8, 0, bytesUTF8.length);
                    this.token.p11.C_SignUpdate(opSession2.id(), 0L, seed, 0, seed.length);
                    SecretKeySpec secretKeySpec2 = new SecretKeySpec(this.token.p11.C_SignFinal(opSession2.id(), this.spec.getOutputLength()), "TlsPrf");
                    this.p11Key.releaseKeyID();
                    this.token.releaseSession(opSession2);
                    return secretKeySpec2;
                } finally {
                }
            } catch (PKCS11Exception e3) {
                throw new ProviderException("Could not calculate PRF", e3);
            }
        }
        byte[] bArr = new byte[this.spec.getOutputLength()];
        CK_TLS_PRF_PARAMS ck_tls_prf_params = new CK_TLS_PRF_PARAMS(seed, bytesUTF8, bArr);
        Session opSession3 = null;
        long keyID3 = this.p11Key.getKeyID();
        try {
            try {
                opSession3 = this.token.getOpSession();
                this.token.p11.C_DeriveKey(opSession3.id(), new CK_MECHANISM(this.mechanism, ck_tls_prf_params), keyID3, null);
                SecretKeySpec secretKeySpec3 = new SecretKeySpec(bArr, "TlsPrf");
                this.p11Key.releaseKeyID();
                this.token.releaseSession(opSession3);
                return secretKeySpec3;
            } catch (PKCS11Exception e4) {
                throw new ProviderException("Could not calculate PRF", e4);
            }
        } finally {
            this.p11Key.releaseKeyID();
            this.token.releaseSession(opSession3);
        }
    }
}
