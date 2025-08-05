package sun.security.util;

/* loaded from: rt.jar:sun/security/util/ByteArrays.class */
public class ByteArrays {
    public static boolean isEqual(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null) {
            return false;
        }
        if (bArr.length == 0) {
            return bArr2.length == 0;
        }
        int i6 = i3 - i2;
        int i7 = i5 - i4;
        if (i7 == 0) {
            return i6 == 0;
        }
        int i8 = 0 | (i6 - i7);
        for (int i9 = 0; i9 < i6; i9++) {
            i8 |= bArr[i2 + i9] ^ bArr2[i4 + (((i9 - i7) >>> 31) * i9)];
        }
        return i8 == 0;
    }
}
