package javax.crypto.spec;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Locale;
import javax.crypto.SecretKey;

/* loaded from: jce.jar:javax/crypto/spec/SecretKeySpec.class */
public class SecretKeySpec implements KeySpec, SecretKey {
    private static final long serialVersionUID = 6577238317307289933L;
    private byte[] key;
    private String algorithm;

    public SecretKeySpec(byte[] bArr, String str) {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("Missing argument");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("Empty key");
        }
        this.key = (byte[]) bArr.clone();
        this.algorithm = str;
    }

    public SecretKeySpec(byte[] bArr, int i2, int i3, String str) {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("Missing argument");
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("Empty key");
        }
        if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        }
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException("len is negative");
        }
        if (bArr.length - i2 < i3) {
            throw new IllegalArgumentException("Invalid offset/length combination");
        }
        this.key = new byte[i3];
        System.arraycopy(bArr, i2, this.key, 0, i3);
        this.algorithm = str;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return this.algorithm;
    }

    @Override // java.security.Key
    public String getFormat() {
        return "RAW";
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 1; i3 < this.key.length; i3++) {
            i2 += this.key[i3] * i3;
        }
        if (this.algorithm.equalsIgnoreCase("TripleDES")) {
            return i2 ^ "desede".hashCode();
        }
        return i2 ^ this.algorithm.toLowerCase(Locale.ENGLISH).hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecretKey)) {
            return false;
        }
        String algorithm = ((SecretKey) obj).getAlgorithm();
        if (!algorithm.equalsIgnoreCase(this.algorithm) && ((!algorithm.equalsIgnoreCase("DESede") || !this.algorithm.equalsIgnoreCase("TripleDES")) && (!algorithm.equalsIgnoreCase("TripleDES") || !this.algorithm.equalsIgnoreCase("DESede")))) {
            return false;
        }
        return MessageDigest.isEqual(this.key, ((SecretKey) obj).getEncoded());
    }
}
