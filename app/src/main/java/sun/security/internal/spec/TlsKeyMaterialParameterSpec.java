package sun.security.internal.spec;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/spec/TlsKeyMaterialParameterSpec.class */
public class TlsKeyMaterialParameterSpec implements AlgorithmParameterSpec {
    private final SecretKey masterSecret;
    private final int majorVersion;
    private final int minorVersion;
    private final byte[] clientRandom;
    private final byte[] serverRandom;
    private final String cipherAlgorithm;
    private final int cipherKeyLength;
    private final int ivLength;
    private final int macKeyLength;
    private final int expandedCipherKeyLength;
    private final String prfHashAlg;
    private final int prfHashLength;
    private final int prfBlockSize;

    public TlsKeyMaterialParameterSpec(SecretKey secretKey, int i2, int i3, byte[] bArr, byte[] bArr2, String str, int i4, int i5, int i6, int i7, String str2, int i8, int i9) {
        if (!secretKey.getAlgorithm().equals("TlsMasterSecret")) {
            throw new IllegalArgumentException("Not a TLS master secret");
        }
        if (str == null) {
            throw new NullPointerException();
        }
        this.masterSecret = secretKey;
        this.majorVersion = TlsMasterSecretParameterSpec.checkVersion(i2);
        this.minorVersion = TlsMasterSecretParameterSpec.checkVersion(i3);
        this.clientRandom = (byte[]) bArr.clone();
        this.serverRandom = (byte[]) bArr2.clone();
        this.cipherAlgorithm = str;
        this.cipherKeyLength = checkSign(i4);
        this.expandedCipherKeyLength = checkSign(i5);
        this.ivLength = checkSign(i6);
        this.macKeyLength = checkSign(i7);
        this.prfHashAlg = str2;
        this.prfHashLength = i8;
        this.prfBlockSize = i9;
    }

    private static int checkSign(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Value must not be negative");
        }
        return i2;
    }

    public SecretKey getMasterSecret() {
        return this.masterSecret;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public byte[] getClientRandom() {
        return (byte[]) this.clientRandom.clone();
    }

    public byte[] getServerRandom() {
        return (byte[]) this.serverRandom.clone();
    }

    public String getCipherAlgorithm() {
        return this.cipherAlgorithm;
    }

    public int getCipherKeyLength() {
        return this.cipherKeyLength;
    }

    public int getExpandedCipherKeyLength() {
        if (this.majorVersion >= 3 && this.minorVersion >= 2) {
            return 0;
        }
        return this.expandedCipherKeyLength;
    }

    public int getIvLength() {
        return this.ivLength;
    }

    public int getMacKeyLength() {
        return this.macKeyLength;
    }

    public String getPRFHashAlg() {
        return this.prfHashAlg;
    }

    public int getPRFHashLength() {
        return this.prfHashLength;
    }

    public int getPRFBlockSize() {
        return this.prfBlockSize;
    }
}
