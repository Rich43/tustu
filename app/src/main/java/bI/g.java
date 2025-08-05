package bI;

/* loaded from: TunerStudioMS.jar:bI/g.class */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private static final int[] f7098a = {7, 12, 17, 22, 5, 9, 14, 20, 4, 11, 16, 23, 6, 10, 15, 21};

    /* renamed from: b, reason: collision with root package name */
    private static final int[] f7099b = new int[64];

    public static byte[] a(byte[] bArr) {
        int length = bArr.length;
        int i2 = ((length + 8) >>> 6) + 1;
        byte[] bArr2 = new byte[(i2 << 6) - length];
        bArr2[0] = Byte.MIN_VALUE;
        long j2 = length << 3;
        for (int i3 = 0; i3 < 8; i3++) {
            bArr2[(bArr2.length - 8) + i3] = (byte) j2;
            j2 >>>= 8;
        }
        int i4 = 1732584193;
        int i5 = -271733879;
        int i6 = -1732584194;
        int i7 = 271733878;
        int[] iArr = new int[16];
        for (int i8 = 0; i8 < i2; i8++) {
            int i9 = i8 << 6;
            int i10 = 0;
            while (i10 < 64) {
                iArr[i10 >>> 2] = ((i9 < length ? bArr[i9] : bArr2[i9 - length]) << 24) | (iArr[i10 >>> 2] >>> 8);
                i10++;
                i9++;
            }
            int i11 = i4;
            int i12 = i5;
            int i13 = i6;
            int i14 = i7;
            for (int i15 = 0; i15 < 64; i15++) {
                int i16 = i15 >>> 4;
                int i17 = 0;
                int i18 = i15;
                switch (i16) {
                    case 0:
                        i17 = (i5 & i6) | ((i5 ^ (-1)) & i7);
                        break;
                    case 1:
                        i17 = (i5 & i7) | (i6 & (i7 ^ (-1)));
                        i18 = ((i18 * 5) + 1) & 15;
                        break;
                    case 2:
                        i17 = (i5 ^ i6) ^ i7;
                        i18 = ((i18 * 3) + 5) & 15;
                        break;
                    case 3:
                        i17 = i6 ^ (i5 | (i7 ^ (-1)));
                        i18 = (i18 * 7) & 15;
                        break;
                }
                int iRotateLeft = i5 + Integer.rotateLeft(i4 + i17 + iArr[i18] + f7099b[i15], f7098a[(i16 << 2) | (i15 & 3)]);
                i4 = i7;
                i7 = i6;
                i6 = i5;
                i5 = iRotateLeft;
            }
            i4 += i11;
            i5 += i12;
            i6 += i13;
            i7 += i14;
        }
        byte[] bArr3 = new byte[16];
        int i19 = 0;
        int i20 = 0;
        while (i20 < 4) {
            int i21 = i20 == 0 ? i4 : i20 == 1 ? i5 : i20 == 2 ? i6 : i7;
            for (int i22 = 0; i22 < 4; i22++) {
                int i23 = i19;
                i19++;
                bArr3[i23] = (byte) i21;
                i21 >>>= 8;
            }
            i20++;
        }
        return bArr3;
    }

    static {
        for (int i2 = 0; i2 < 64; i2++) {
            f7099b[i2] = (int) (4.294967296E9d * Math.abs(Math.sin(i2 + 1)));
        }
    }
}
