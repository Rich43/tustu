package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/* loaded from: jce.jar:javax/crypto/spec/RC5ParameterSpec.class */
public class RC5ParameterSpec implements AlgorithmParameterSpec {
    private byte[] iv;
    private int version;
    private int rounds;
    private int wordSize;

    public RC5ParameterSpec(int i2, int i3, int i4) {
        this.iv = null;
        this.version = i2;
        this.rounds = i3;
        this.wordSize = i4;
    }

    public RC5ParameterSpec(int i2, int i3, int i4, byte[] bArr) {
        this(i2, i3, i4, bArr, 0);
    }

    public RC5ParameterSpec(int i2, int i3, int i4, byte[] bArr, int i5) {
        this.iv = null;
        this.version = i2;
        this.rounds = i3;
        this.wordSize = i4;
        if (bArr == null) {
            throw new IllegalArgumentException("IV missing");
        }
        if (i5 < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        }
        int i6 = (i4 / 8) * 2;
        if (bArr.length - i5 < i6) {
            throw new IllegalArgumentException("IV too short");
        }
        this.iv = new byte[i6];
        System.arraycopy(bArr, i5, this.iv, 0, i6);
    }

    public int getVersion() {
        return this.version;
    }

    public int getRounds() {
        return this.rounds;
    }

    public int getWordSize() {
        return this.wordSize;
    }

    public byte[] getIV() {
        if (this.iv == null) {
            return null;
        }
        return (byte[]) this.iv.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RC5ParameterSpec)) {
            return false;
        }
        RC5ParameterSpec rC5ParameterSpec = (RC5ParameterSpec) obj;
        return this.version == rC5ParameterSpec.version && this.rounds == rC5ParameterSpec.rounds && this.wordSize == rC5ParameterSpec.wordSize && Arrays.equals(this.iv, rC5ParameterSpec.iv);
    }

    public int hashCode() {
        int i2 = 0;
        if (this.iv != null) {
            for (int i3 = 1; i3 < this.iv.length; i3++) {
                i2 += this.iv[i3] * i3;
            }
        }
        return i2 + this.version + this.rounds + this.wordSize;
    }
}
