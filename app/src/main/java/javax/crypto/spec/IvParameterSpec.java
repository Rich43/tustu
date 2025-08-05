package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/spec/IvParameterSpec.class */
public class IvParameterSpec implements AlgorithmParameterSpec {
    private byte[] iv;

    public IvParameterSpec(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public IvParameterSpec(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new IllegalArgumentException("IV missing");
        }
        if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        }
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException("len is negative");
        }
        if (bArr.length - i2 < i3) {
            throw new IllegalArgumentException("IV buffer too short for given offset/length combination");
        }
        this.iv = new byte[i3];
        System.arraycopy(bArr, i2, this.iv, 0, i3);
    }

    public byte[] getIV() {
        return (byte[]) this.iv.clone();
    }
}
