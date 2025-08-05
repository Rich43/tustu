package sun.font;

import java.text.Bidi;

/* loaded from: rt.jar:sun/font/BidiUtils.class */
public final class BidiUtils {
    static final char NUMLEVELS = '>';

    public static void getLevels(Bidi bidi, byte[] bArr, int i2) {
        int length = i2 + bidi.getLength();
        if (i2 < 0 || length > bArr.length) {
            throw new IndexOutOfBoundsException("levels.length = " + bArr.length + " start: " + i2 + " limit: " + length);
        }
        int runCount = bidi.getRunCount();
        int i3 = i2;
        for (int i4 = 0; i4 < runCount; i4++) {
            int runLimit = i2 + bidi.getRunLimit(i4);
            byte runLevel = (byte) bidi.getRunLevel(i4);
            while (i3 < runLimit) {
                int i5 = i3;
                i3++;
                bArr[i5] = runLevel;
            }
        }
    }

    public static byte[] getLevels(Bidi bidi) {
        byte[] bArr = new byte[bidi.getLength()];
        getLevels(bidi, bArr, 0);
        return bArr;
    }

    public static int[] createVisualToLogicalMap(byte[] bArr) {
        int length = bArr.length;
        int[] iArr = new int[length];
        byte b2 = 63;
        byte b3 = 0;
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = i2;
            byte b4 = bArr[i2];
            if (b4 > b3) {
                b3 = b4;
            }
            if ((b4 & 1) != 0 && b4 < b2) {
                b2 = b4;
            }
        }
        while (b3 >= b2) {
            int i3 = 0;
            while (true) {
                if (i3 < length && bArr[i3] < b3) {
                    i3++;
                } else {
                    int i4 = i3;
                    i3++;
                    int i5 = i4;
                    if (i5 == bArr.length) {
                        break;
                    }
                    while (i3 < length && bArr[i3] >= b3) {
                        i3++;
                    }
                    for (int i6 = i3 - 1; i5 < i6; i6--) {
                        int i7 = iArr[i5];
                        iArr[i5] = iArr[i6];
                        iArr[i6] = i7;
                        i5++;
                    }
                }
            }
            b3 = (byte) (b3 - 1);
        }
        return iArr;
    }

    public static int[] createInverseMap(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        int[] iArr2 = new int[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr2[iArr[i2]] = i2;
        }
        return iArr2;
    }

    public static int[] createContiguousOrder(int[] iArr) {
        if (iArr != null) {
            return computeContiguousOrder(iArr, 0, iArr.length);
        }
        return null;
    }

    private static int[] computeContiguousOrder(int[] iArr, int i2, int i3) {
        int[] iArr2 = new int[i3 - i2];
        for (int i4 = 0; i4 < iArr2.length; i4++) {
            iArr2[i4] = i4 + i2;
        }
        for (int i5 = 0; i5 < iArr2.length - 1; i5++) {
            int i6 = i5;
            int i7 = iArr[iArr2[i6]];
            for (int i8 = i5; i8 < iArr2.length; i8++) {
                if (iArr[iArr2[i8]] < i7) {
                    i6 = i8;
                    i7 = iArr[iArr2[i6]];
                }
            }
            int i9 = iArr2[i5];
            iArr2[i5] = iArr2[i6];
            iArr2[i6] = i9;
        }
        if (i2 != 0) {
            for (int i10 = 0; i10 < iArr2.length; i10++) {
                int i11 = i10;
                iArr2[i11] = iArr2[i11] - i2;
            }
        }
        int i12 = 0;
        while (i12 < iArr2.length && iArr2[i12] == i12) {
            i12++;
        }
        if (i12 == iArr2.length) {
            return null;
        }
        return createInverseMap(iArr2);
    }

    public static int[] createNormalizedMap(int[] iArr, byte[] bArr, int i2, int i3) {
        boolean z2;
        byte b2;
        boolean z3;
        int i4;
        if (iArr != null) {
            if (i2 != 0 || i3 != iArr.length) {
                if (bArr == null) {
                    b2 = 0;
                    z2 = true;
                    z3 = true;
                } else if (bArr[i2] == bArr[i3 - 1]) {
                    b2 = bArr[i2];
                    z3 = (b2 & 1) == 0;
                    int i5 = i2;
                    while (i5 < i3 && bArr[i5] >= b2) {
                        if (z3) {
                            z3 = bArr[i5] == b2;
                        }
                        i5++;
                    }
                    z2 = i5 == i3;
                } else {
                    z2 = false;
                    b2 = 0;
                    z3 = false;
                }
                if (z2) {
                    if (z3) {
                        return null;
                    }
                    int[] iArr2 = new int[i3 - i2];
                    if ((b2 & 1) != 0) {
                        i4 = iArr[i3 - 1];
                    } else {
                        i4 = iArr[i2];
                    }
                    if (i4 == 0) {
                        System.arraycopy(iArr, i2, iArr2, 0, i3 - i2);
                    } else {
                        for (int i6 = 0; i6 < iArr2.length; i6++) {
                            iArr2[i6] = iArr[i6 + i2] - i4;
                        }
                    }
                    return iArr2;
                }
                return computeContiguousOrder(iArr, i2, i3);
            }
            return iArr;
        }
        return null;
    }

    public static void reorderVisually(byte[] bArr, Object[] objArr) {
        int length = bArr.length;
        byte b2 = 63;
        byte b3 = 0;
        for (byte b4 : bArr) {
            if (b4 > b3) {
                b3 = b4;
            }
            if ((b4 & 1) != 0 && b4 < b2) {
                b2 = b4;
            }
        }
        while (b3 >= b2) {
            int i2 = 0;
            while (true) {
                if (i2 < length && bArr[i2] < b3) {
                    i2++;
                } else {
                    int i3 = i2;
                    i2++;
                    int i4 = i3;
                    if (i4 == bArr.length) {
                        break;
                    }
                    while (i2 < length && bArr[i2] >= b3) {
                        i2++;
                    }
                    for (int i5 = i2 - 1; i4 < i5; i5--) {
                        Object obj = objArr[i4];
                        objArr[i4] = objArr[i5];
                        objArr[i5] = obj;
                        i4++;
                    }
                }
            }
            b3 = (byte) (b3 - 1);
        }
    }
}
