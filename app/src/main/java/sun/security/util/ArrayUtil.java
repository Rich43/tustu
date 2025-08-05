package sun.security.util;

/* loaded from: rt.jar:sun/security/util/ArrayUtil.class */
public final class ArrayUtil {
    private static void swap(byte[] bArr, int i2, int i3) {
        byte b2 = bArr[i2];
        bArr[i2] = bArr[i3];
        bArr[i3] = b2;
    }

    public static void reverse(byte[] bArr) {
        int i2 = 0;
        for (int length = bArr.length - 1; i2 < length; length--) {
            swap(bArr, i2, length);
            i2++;
        }
    }
}
