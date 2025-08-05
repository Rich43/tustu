package com.sun.imageio.plugins.png;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/RowFilter.class */
public class RowFilter {
    private static final int abs(int i2) {
        return i2 < 0 ? -i2 : i2;
    }

    protected static int subFilter(byte[] bArr, byte[] bArr2, int i2, int i3) {
        int iAbs = 0;
        for (int i4 = i2; i4 < i3 + i2; i4++) {
            int i5 = (bArr[i4] & 255) - (bArr[i4 - i2] & 255);
            bArr2[i4] = (byte) i5;
            iAbs += abs(i5);
        }
        return iAbs;
    }

    protected static int upFilter(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3) {
        int iAbs = 0;
        for (int i4 = i2; i4 < i3 + i2; i4++) {
            int i5 = (bArr[i4] & 255) - (bArr2[i4] & 255);
            bArr3[i4] = (byte) i5;
            iAbs += abs(i5);
        }
        return iAbs;
    }

    protected final int paethPredictor(int i2, int i3, int i4) {
        int i5 = (i2 + i3) - i4;
        int iAbs = abs(i5 - i2);
        int iAbs2 = abs(i5 - i3);
        int iAbs3 = abs(i5 - i4);
        if (iAbs <= iAbs2 && iAbs <= iAbs3) {
            return i2;
        }
        if (iAbs2 <= iAbs3) {
            return i3;
        }
        return i4;
    }

    public int filterRow(int i2, byte[] bArr, byte[] bArr2, byte[][] bArr3, int i3, int i4) {
        if (i2 != 3) {
            System.arraycopy(bArr, i4, bArr3[0], i4, i3);
            return 0;
        }
        int[] iArr = new int[5];
        for (int i5 = 0; i5 < 5; i5++) {
            iArr[i5] = Integer.MAX_VALUE;
        }
        int i6 = 0;
        for (int i7 = i4; i7 < i3 + i4; i7++) {
            i6 += bArr[i7] & 255;
        }
        iArr[0] = i6;
        iArr[1] = subFilter(bArr, bArr3[1], i4, i3);
        iArr[2] = upFilter(bArr, bArr2, bArr3[2], i4, i3);
        byte[] bArr4 = bArr3[3];
        int iAbs = 0;
        for (int i8 = i4; i8 < i3 + i4; i8++) {
            int i9 = (bArr[i8] & 255) - (((bArr[i8 - i4] & 255) + (bArr2[i8] & 255)) / 2);
            bArr4[i8] = (byte) i9;
            iAbs += abs(i9);
        }
        iArr[3] = iAbs;
        byte[] bArr5 = bArr3[4];
        int iAbs2 = 0;
        for (int i10 = i4; i10 < i3 + i4; i10++) {
            int iPaethPredictor = (bArr[i10] & 255) - paethPredictor(bArr[i10 - i4] & 255, bArr2[i10] & 255, bArr2[i10 - i4] & 255);
            bArr5[i10] = (byte) iPaethPredictor;
            iAbs2 += abs(iPaethPredictor);
        }
        iArr[4] = iAbs2;
        int i11 = iArr[0];
        int i12 = 0;
        for (int i13 = 1; i13 < 5; i13++) {
            if (iArr[i13] < i11) {
                i11 = iArr[i13];
                i12 = i13;
            }
        }
        if (i12 == 0) {
            System.arraycopy(bArr, i4, bArr3[0], i4, i3);
        }
        return i12;
    }
}
