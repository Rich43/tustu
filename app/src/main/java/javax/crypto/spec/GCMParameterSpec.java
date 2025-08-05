package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/spec/GCMParameterSpec.class */
public class GCMParameterSpec implements AlgorithmParameterSpec {
    private byte[] iv;
    private int tLen;

    public GCMParameterSpec(int i2, byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("src array is null");
        }
        init(i2, bArr, 0, bArr.length);
    }

    public GCMParameterSpec(int i2, byte[] bArr, int i3, int i4) {
        init(i2, bArr, i3, i4);
    }

    private void init(int i2, byte[] bArr, int i3, int i4) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Length argument is negative");
        }
        this.tLen = i2;
        if (bArr == null || i4 < 0 || i3 < 0 || i4 > bArr.length - i3) {
            throw new IllegalArgumentException("Invalid buffer arguments");
        }
        this.iv = new byte[i4];
        System.arraycopy(bArr, i3, this.iv, 0, i4);
    }

    public int getTLen() {
        return this.tLen;
    }

    public byte[] getIV() {
        return (byte[]) this.iv.clone();
    }
}
