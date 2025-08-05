package sun.security.internal.spec;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/spec/TlsMasterSecretParameterSpec.class */
public class TlsMasterSecretParameterSpec implements AlgorithmParameterSpec {
    private final SecretKey premasterSecret;
    private final int majorVersion;
    private final int minorVersion;
    private final byte[] clientRandom;
    private final byte[] serverRandom;
    private final byte[] extendedMasterSecretSessionHash;
    private final String prfHashAlg;
    private final int prfHashLength;
    private final int prfBlockSize;

    public TlsMasterSecretParameterSpec(SecretKey secretKey, int i2, int i3, byte[] bArr, byte[] bArr2, String str, int i4, int i5) {
        this(secretKey, i2, i3, bArr, bArr2, new byte[0], str, i4, i5);
    }

    public TlsMasterSecretParameterSpec(SecretKey secretKey, int i2, int i3, byte[] bArr, String str, int i4, int i5) {
        this(secretKey, i2, i3, new byte[0], new byte[0], bArr, str, i4, i5);
    }

    private TlsMasterSecretParameterSpec(SecretKey secretKey, int i2, int i3, byte[] bArr, byte[] bArr2, byte[] bArr3, String str, int i4, int i5) {
        if (secretKey == null) {
            throw new NullPointerException("premasterSecret must not be null");
        }
        this.premasterSecret = secretKey;
        this.majorVersion = checkVersion(i2);
        this.minorVersion = checkVersion(i3);
        this.clientRandom = (byte[]) bArr.clone();
        this.serverRandom = (byte[]) bArr2.clone();
        this.extendedMasterSecretSessionHash = bArr3 != null ? (byte[]) bArr3.clone() : new byte[0];
        this.prfHashAlg = str;
        this.prfHashLength = i4;
        this.prfBlockSize = i5;
    }

    static int checkVersion(int i2) {
        if (i2 < 0 || i2 > 255) {
            throw new IllegalArgumentException("Version must be between 0 and 255");
        }
        return i2;
    }

    public SecretKey getPremasterSecret() {
        return this.premasterSecret;
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

    public byte[] getExtendedMasterSecretSessionHash() {
        return (byte[]) this.extendedMasterSecretSessionHash.clone();
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
