package sun.security.internal.spec;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/spec/TlsPrfParameterSpec.class */
public class TlsPrfParameterSpec implements AlgorithmParameterSpec {
    private final SecretKey secret;
    private final String label;
    private final byte[] seed;
    private final int outputLength;
    private final String prfHashAlg;
    private final int prfHashLength;
    private final int prfBlockSize;

    public TlsPrfParameterSpec(SecretKey secretKey, String str, byte[] bArr, int i2, String str2, int i3, int i4) {
        if (str == null || bArr == null) {
            throw new NullPointerException("label and seed must not be null");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("outputLength must be positive");
        }
        this.secret = secretKey;
        this.label = str;
        this.seed = (byte[]) bArr.clone();
        this.outputLength = i2;
        this.prfHashAlg = str2;
        this.prfHashLength = i3;
        this.prfBlockSize = i4;
    }

    public SecretKey getSecret() {
        return this.secret;
    }

    public String getLabel() {
        return this.label;
    }

    public byte[] getSeed() {
        return (byte[]) this.seed.clone();
    }

    public int getOutputLength() {
        return this.outputLength;
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
