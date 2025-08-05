package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_VERSION;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11TlsRsaPremasterSecretGenerator.class */
final class P11TlsRsaPremasterSecretGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsRsaPremasterSecretGenerator must be initialized using a TlsRsaPremasterSecretParameterSpec";
    private final Token token;
    private final String algorithm;
    private long mechanism;
    private int version;
    private TlsRsaPremasterSecretParameterSpec spec;

    P11TlsRsaPremasterSecretGenerator(Token token, String str, long j2) throws PKCS11Exception {
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
        if (!(algorithmParameterSpec instanceof TlsRsaPremasterSecretParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsRsaPremasterSecretParameterSpec) algorithmParameterSpec;
        this.version = (this.spec.getMajorVersion() << 8) | this.spec.getMinorVersion();
        if (this.version < 768 && this.version > 771) {
            throw new InvalidAlgorithmParameterException("Only SSL 3.0, TLS 1.0, TLS 1.1, and TLS 1.2 are supported");
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        if (this.spec == null) {
            throw new IllegalStateException("TlsRsaPremasterSecretGenerator must be initialized");
        }
        CK_VERSION ck_version = new CK_VERSION(this.spec.getMajorVersion(), this.spec.getMinorVersion());
        Session objSession = null;
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, 16L, new CK_ATTRIBUTE[0]);
                SecretKey secretKey = P11Key.secretKey(objSession, this.token.p11.C_GenerateKey(objSession.id(), new CK_MECHANISM(this.mechanism, ck_version), attributes), "TlsRsaPremasterSecret", 384, attributes);
                this.token.releaseSession(objSession);
                return secretKey;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("Could not generate premaster secret", e2);
            }
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }
}
