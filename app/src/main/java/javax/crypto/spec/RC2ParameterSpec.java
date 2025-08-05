package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/* loaded from: jce.jar:javax/crypto/spec/RC2ParameterSpec.class */
public class RC2ParameterSpec implements AlgorithmParameterSpec {
    private byte[] iv;
    private int effectiveKeyBits;

    public RC2ParameterSpec(int i2) {
        this.iv = null;
        this.effectiveKeyBits = i2;
    }

    public RC2ParameterSpec(int i2, byte[] bArr) {
        this(i2, bArr, 0);
    }

    public RC2ParameterSpec(int i2, byte[] bArr, int i3) {
        this.iv = null;
        this.effectiveKeyBits = i2;
        if (bArr == null) {
            throw new IllegalArgumentException("IV missing");
        }
        if (bArr.length - i3 < 8) {
            throw new IllegalArgumentException("IV too short");
        }
        this.iv = new byte[8];
        System.arraycopy(bArr, i3, this.iv, 0, 8);
    }

    public int getEffectiveKeyBits() {
        return this.effectiveKeyBits;
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
        if (!(obj instanceof RC2ParameterSpec)) {
            return false;
        }
        RC2ParameterSpec rC2ParameterSpec = (RC2ParameterSpec) obj;
        return this.effectiveKeyBits == rC2ParameterSpec.effectiveKeyBits && Arrays.equals(this.iv, rC2ParameterSpec.iv);
    }

    public int hashCode() {
        int i2 = 0;
        if (this.iv != null) {
            for (int i3 = 1; i3 < this.iv.length; i3++) {
                i2 += this.iv[i3] * i3;
            }
        }
        return i2 + this.effectiveKeyBits;
    }
}
