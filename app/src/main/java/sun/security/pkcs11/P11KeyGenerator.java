package sun.security.pkcs11;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyGenerator.class */
final class P11KeyGenerator extends KeyGeneratorSpi {
    private final Token token;
    private final String algorithm;
    private long mechanism;
    private int keySize;
    private int significantKeySize;
    private long keyType;
    private boolean supportBothKeySizes;

    static int checkKeySize(long j2, int i2, Token token) throws ProviderException, InvalidAlgorithmParameterException {
        int i3;
        switch ((int) j2) {
            case 288:
                if (i2 != 64 && i2 != 56) {
                    throw new InvalidAlgorithmParameterException("DES key length must be 56 bits");
                }
                i3 = 56;
                break;
                break;
            case 304:
            case 305:
                if (i2 == 112 || i2 == 128) {
                    i3 = 112;
                    break;
                } else if (i2 == 168 || i2 == 192) {
                    i3 = 168;
                    break;
                } else {
                    throw new InvalidAlgorithmParameterException("DESede key length must be 112, or 168 bits");
                }
                break;
            default:
                try {
                    CK_MECHANISM_INFO mechanismInfo = token.getMechanismInfo(j2);
                    if (mechanismInfo == null) {
                        return i2;
                    }
                    int iMultiplyExact = mechanismInfo.iMinKeySize;
                    int iMultiplyExact2 = mechanismInfo.iMaxKeySize;
                    if (j2 != 272 || iMultiplyExact < 8) {
                        iMultiplyExact = Math.multiplyExact(iMultiplyExact, 8);
                        if (iMultiplyExact2 != Integer.MAX_VALUE) {
                            iMultiplyExact2 = Math.multiplyExact(iMultiplyExact2, 8);
                        }
                    }
                    if (iMultiplyExact < 40) {
                        iMultiplyExact = 40;
                    }
                    if (i2 < iMultiplyExact || i2 > iMultiplyExact2) {
                        throw new InvalidAlgorithmParameterException("Key length must be between " + iMultiplyExact + " and " + iMultiplyExact2 + " bits");
                    }
                    if (j2 == PKCS11Constants.CKM_AES_KEY_GEN && i2 != 128 && i2 != 192 && i2 != 256) {
                        throw new InvalidAlgorithmParameterException("AES key length must be " + iMultiplyExact + (iMultiplyExact2 >= 192 ? ", 192" : "") + (iMultiplyExact2 >= 256 ? ", or 256" : "") + " bits");
                    }
                    i3 = i2;
                    break;
                } catch (PKCS11Exception e2) {
                    throw new ProviderException("Cannot retrieve mechanism info", e2);
                }
        }
        return i3;
    }

    P11KeyGenerator(Token token, String str, long j2) throws PKCS11Exception {
        this.token = token;
        this.algorithm = str;
        this.mechanism = j2;
        if (this.mechanism == 305) {
            this.supportBothKeySizes = token.provider.config.isEnabled(304L) && token.getMechanismInfo(304L) != null;
        }
        setDefaultKeySize();
    }

    private void setDefaultKeySize() {
        switch ((int) this.mechanism) {
            case 272:
                this.keySize = 128;
                this.keyType = 18L;
                break;
            case 288:
                this.keySize = 64;
                this.keyType = 19L;
                break;
            case 304:
                this.keySize = 128;
                this.keyType = 20L;
                break;
            case 305:
                this.keySize = 192;
                this.keyType = 21L;
                break;
            case 4224:
                this.keySize = 128;
                this.keyType = 31L;
                break;
            case 4240:
                this.keySize = 128;
                this.keyType = 32L;
                break;
            default:
                throw new ProviderException("Unknown mechanism " + this.mechanism);
        }
        try {
            this.significantKeySize = checkKeySize(this.mechanism, this.keySize, this.token);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new ProviderException("Unsupported default key size", e2);
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        this.token.ensureValid();
        setDefaultKeySize();
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("AlgorithmParameterSpec not supported");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) throws ProviderException {
        this.token.ensureValid();
        try {
            int iCheckKeySize = checkKeySize(this.mechanism, i2, this.token);
            if (this.mechanism == 304 || this.mechanism == 305) {
                long j2 = iCheckKeySize == 112 ? 304L : 305L;
                if (this.mechanism != j2) {
                    if (this.supportBothKeySizes) {
                        this.mechanism = j2;
                        this.keyType = this.mechanism == 304 ? 20L : 21L;
                    } else {
                        throw new InvalidParameterException("Only " + this.significantKeySize + "-bit DESede is supported");
                    }
                }
            }
            this.keySize = i2;
            this.significantKeySize = iCheckKeySize;
        } catch (InvalidAlgorithmParameterException e2) {
            throw ((InvalidParameterException) new InvalidParameterException().initCause(e2));
        }
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        CK_ATTRIBUTE[] ck_attributeArr;
        Session objSession = null;
        try {
            try {
                objSession = this.token.getObjSession();
                switch ((int) this.keyType) {
                    case 19:
                    case 20:
                    case 21:
                        ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L)};
                        break;
                    default:
                        ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 4L), new CK_ATTRIBUTE(353L, this.keySize >> 3)};
                        break;
                }
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 4L, this.keyType, ck_attributeArr);
                SecretKey secretKey = P11Key.secretKey(objSession, this.token.p11.C_GenerateKey(objSession.id(), new CK_MECHANISM(this.mechanism), attributes), this.algorithm, this.significantKeySize, attributes);
                this.token.releaseSession(objSession);
                return secretKey;
            } catch (PKCS11Exception e2) {
                throw new ProviderException("Could not generate key", e2);
            }
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }
}
