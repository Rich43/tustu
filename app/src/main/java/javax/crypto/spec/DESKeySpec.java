package javax.crypto.spec;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

/* loaded from: jce.jar:javax/crypto/spec/DESKeySpec.class */
public class DESKeySpec implements KeySpec {
    public static final int DES_KEY_LEN = 8;
    private byte[] key;
    private static final byte[][] WEAK_KEYS = {new byte[]{1, 1, 1, 1, 1, 1, 1, 1}, new byte[]{-2, -2, -2, -2, -2, -2, -2, -2}, new byte[]{31, 31, 31, 31, 14, 14, 14, 14}, new byte[]{-32, -32, -32, -32, -15, -15, -15, -15}, new byte[]{1, -2, 1, -2, 1, -2, 1, -2}, new byte[]{31, -32, 31, -32, 14, -15, 14, -15}, new byte[]{1, -32, 1, -32, 1, -15, 1, -15}, new byte[]{31, -2, 31, -2, 14, -2, 14, -2}, new byte[]{1, 31, 1, 31, 1, 14, 1, 14}, new byte[]{-32, -2, -32, -2, -15, -2, -15, -2}, new byte[]{-2, 1, -2, 1, -2, 1, -2, 1}, new byte[]{-32, 31, -32, 31, -15, 14, -15, 14}, new byte[]{-32, 1, -32, 1, -15, 1, -15, 1}, new byte[]{-2, 31, -2, 31, -2, 14, -2, 14}, new byte[]{31, 1, 31, 1, 14, 1, 14, 1}, new byte[]{-2, -32, -2, -32, -2, -15, -2, -15}};

    public DESKeySpec(byte[] bArr) throws InvalidKeyException {
        this(bArr, 0);
    }

    public DESKeySpec(byte[] bArr, int i2) throws InvalidKeyException {
        if (bArr.length - i2 < 8) {
            throw new InvalidKeyException("Wrong key size");
        }
        this.key = new byte[8];
        System.arraycopy(bArr, i2, this.key, 0, 8);
    }

    public byte[] getKey() {
        return (byte[]) this.key.clone();
    }

    public static boolean isParityAdjusted(byte[] bArr, int i2) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("null key");
        }
        if (bArr.length - i2 < 8) {
            throw new InvalidKeyException("Wrong key size");
        }
        for (int i3 = 0; i3 < 8; i3++) {
            int i4 = i2;
            i2++;
            if ((Integer.bitCount(bArr[i4] & 255) & 1) == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWeak(byte[] bArr, int i2) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("null key");
        }
        if (bArr.length - i2 < 8) {
            throw new InvalidKeyException("Wrong key size");
        }
        for (int i3 = 0; i3 < WEAK_KEYS.length; i3++) {
            boolean z2 = true;
            for (int i4 = 0; i4 < 8 && z2; i4++) {
                if (WEAK_KEYS[i3][i4] != bArr[i4 + i2]) {
                    z2 = false;
                }
            }
            if (z2) {
                return z2;
            }
        }
        return false;
    }
}
