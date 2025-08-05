package sun.text.bidi;

import java.text.Bidi;
import java.util.Arrays;
import sun.text.bidi.BidiBase;

/* loaded from: rt.jar:sun/text/bidi/BidiLine.class */
public final class BidiLine {
    static void setTrailingWSStart(BidiBase bidiBase) {
        byte[] bArr = bidiBase.dirProps;
        byte[] bArr2 = bidiBase.levels;
        int i2 = bidiBase.length;
        byte b2 = bidiBase.paraLevel;
        if (BidiBase.NoContextRTL(bArr[i2 - 1]) == 7) {
            bidiBase.trailingWSStart = i2;
            return;
        }
        while (i2 > 0 && (BidiBase.DirPropFlagNC(bArr[i2 - 1]) & BidiBase.MASK_WS) != 0) {
            i2--;
        }
        while (i2 > 0 && bArr2[i2 - 1] == b2) {
            i2--;
        }
        bidiBase.trailingWSStart = i2;
    }

    public static Bidi setLine(Bidi bidi, BidiBase bidiBase, Bidi bidi2, BidiBase bidiBase2, int i2, int i3) {
        int i4 = i3 - i2;
        bidiBase2.resultLength = i4;
        bidiBase2.originalLength = i4;
        bidiBase2.length = i4;
        bidiBase2.text = new char[i4];
        System.arraycopy(bidiBase.text, i2, bidiBase2.text, 0, i4);
        bidiBase2.paraLevel = bidiBase.GetParaLevelAt(i2);
        bidiBase2.paraCount = bidiBase.paraCount;
        bidiBase2.runs = new BidiRun[0];
        if (bidiBase.controlCount > 0) {
            for (int i5 = i2; i5 < i3; i5++) {
                if (BidiBase.IsBidiControlChar(bidiBase.text[i5])) {
                    bidiBase2.controlCount++;
                }
            }
            bidiBase2.resultLength -= bidiBase2.controlCount;
        }
        bidiBase2.getDirPropsMemory(i4);
        bidiBase2.dirProps = bidiBase2.dirPropsMemory;
        System.arraycopy(bidiBase.dirProps, i2, bidiBase2.dirProps, 0, i4);
        bidiBase2.getLevelsMemory(i4);
        bidiBase2.levels = bidiBase2.levelsMemory;
        System.arraycopy(bidiBase.levels, i2, bidiBase2.levels, 0, i4);
        bidiBase2.runCount = -1;
        if (bidiBase.direction != 2) {
            bidiBase2.direction = bidiBase.direction;
            if (bidiBase.trailingWSStart <= i2) {
                bidiBase2.trailingWSStart = 0;
            } else if (bidiBase.trailingWSStart < i3) {
                bidiBase2.trailingWSStart = bidiBase.trailingWSStart - i2;
            } else {
                bidiBase2.trailingWSStart = i4;
            }
        } else {
            byte[] bArr = bidiBase2.levels;
            setTrailingWSStart(bidiBase2);
            int i6 = bidiBase2.trailingWSStart;
            if (i6 == 0) {
                bidiBase2.direction = (byte) (bidiBase2.paraLevel & 1);
            } else {
                byte b2 = (byte) (bArr[0] & 1);
                if (i6 < i4 && (bidiBase2.paraLevel & 1) != b2) {
                    bidiBase2.direction = (byte) 2;
                } else {
                    int i7 = 1;
                    while (true) {
                        if (i7 == i6) {
                            bidiBase2.direction = b2;
                            break;
                        }
                        if ((bArr[i7] & 1) == b2) {
                            i7++;
                        } else {
                            bidiBase2.direction = (byte) 2;
                            break;
                        }
                    }
                }
            }
            switch (bidiBase2.direction) {
                case 0:
                    bidiBase2.paraLevel = (byte) ((bidiBase2.paraLevel + 1) & (-2));
                    bidiBase2.trailingWSStart = 0;
                    break;
                case 1:
                    bidiBase2.paraLevel = (byte) (bidiBase2.paraLevel | 1);
                    bidiBase2.trailingWSStart = 0;
                    break;
            }
        }
        bidiBase2.paraBidi = bidiBase;
        return bidi2;
    }

    static byte getLevelAt(BidiBase bidiBase, int i2) {
        if (bidiBase.direction != 2 || i2 >= bidiBase.trailingWSStart) {
            return bidiBase.GetParaLevelAt(i2);
        }
        return bidiBase.levels[i2];
    }

    static byte[] getLevels(BidiBase bidiBase) {
        int i2 = bidiBase.trailingWSStart;
        int i3 = bidiBase.length;
        if (i2 != i3) {
            Arrays.fill(bidiBase.levels, i2, i3, bidiBase.paraLevel);
            bidiBase.trailingWSStart = i3;
        }
        if (i3 < bidiBase.levels.length) {
            byte[] bArr = new byte[i3];
            System.arraycopy(bidiBase.levels, 0, bArr, 0, i3);
            return bArr;
        }
        return bidiBase.levels;
    }

    static BidiRun getLogicalRun(BidiBase bidiBase, int i2) {
        BidiRun bidiRun = new BidiRun();
        getRuns(bidiBase);
        int i3 = bidiBase.runCount;
        int i4 = 0;
        int i5 = 0;
        BidiRun bidiRun2 = bidiBase.runs[0];
        for (int i6 = 0; i6 < i3; i6++) {
            bidiRun2 = bidiBase.runs[i6];
            i5 = (bidiRun2.start + bidiRun2.limit) - i4;
            if (i2 >= bidiRun2.start && i2 < i5) {
                break;
            }
            i4 = bidiRun2.limit;
        }
        bidiRun.start = bidiRun2.start;
        bidiRun.limit = i5;
        bidiRun.level = bidiRun2.level;
        return bidiRun;
    }

    private static void getSingleRun(BidiBase bidiBase, byte b2) {
        bidiBase.runs = bidiBase.simpleRuns;
        bidiBase.runCount = 1;
        bidiBase.runs[0] = new BidiRun(0, bidiBase.length, b2);
    }

    private static void reorderLine(BidiBase bidiBase, byte b2, byte b3) {
        if (b3 <= (b2 | 1)) {
            return;
        }
        byte b4 = (byte) (b2 + 1);
        BidiRun[] bidiRunArr = bidiBase.runs;
        byte[] bArr = bidiBase.levels;
        int i2 = bidiBase.runCount;
        if (bidiBase.trailingWSStart < bidiBase.length) {
            i2--;
        }
        while (true) {
            b3 = (byte) (b3 - 1);
            if (b3 < b4) {
                break;
            }
            int i3 = 0;
            while (true) {
                if (i3 < i2 && bArr[bidiRunArr[i3].start] < b3) {
                    i3++;
                } else {
                    if (i3 >= i2) {
                        break;
                    }
                    int i4 = i3;
                    do {
                        i4++;
                        if (i4 >= i2) {
                            break;
                        }
                    } while (bArr[bidiRunArr[i4].start] >= b3);
                    for (int i5 = i4 - 1; i3 < i5; i5--) {
                        BidiRun bidiRun = bidiRunArr[i3];
                        bidiRunArr[i3] = bidiRunArr[i5];
                        bidiRunArr[i5] = bidiRun;
                        i3++;
                    }
                    if (i4 == i2) {
                        break;
                    } else {
                        i3 = i4 + 1;
                    }
                }
            }
        }
        if ((b4 & 1) == 0) {
            int i6 = 0;
            if (bidiBase.trailingWSStart == bidiBase.length) {
                i2--;
            }
            while (i6 < i2) {
                BidiRun bidiRun2 = bidiRunArr[i6];
                bidiRunArr[i6] = bidiRunArr[i2];
                bidiRunArr[i2] = bidiRun2;
                i6++;
                i2--;
            }
        }
    }

    static int getRunFromLogicalIndex(BidiBase bidiBase, int i2) {
        BidiRun[] bidiRunArr = bidiBase.runs;
        int i3 = bidiBase.runCount;
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            int i6 = bidiRunArr[i5].limit - i4;
            int i7 = bidiRunArr[i5].start;
            if (i2 >= i7 && i2 < i7 + i6) {
                return i5;
            }
            i4 += i6;
        }
        throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
    }

    static void getRuns(BidiBase bidiBase) {
        if (bidiBase.runCount >= 0) {
            return;
        }
        if (bidiBase.direction != 2) {
            getSingleRun(bidiBase, bidiBase.paraLevel);
        } else {
            int i2 = bidiBase.length;
            byte[] bArr = bidiBase.levels;
            byte b2 = 126;
            int i3 = bidiBase.trailingWSStart;
            int i4 = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                if (bArr[i5] != b2) {
                    i4++;
                    b2 = bArr[i5];
                }
            }
            if (i4 == 1 && i3 == i2) {
                getSingleRun(bidiBase, bArr[0]);
            } else {
                byte b3 = 62;
                byte b4 = 0;
                if (i3 < i2) {
                    i4++;
                }
                bidiBase.getRunsMemory(i4);
                BidiRun[] bidiRunArr = bidiBase.runsMemory;
                int i6 = 0;
                int i7 = 0;
                do {
                    int i8 = i7;
                    byte b5 = bArr[i7];
                    if (b5 < b3) {
                        b3 = b5;
                    }
                    if (b5 > b4) {
                        b4 = b5;
                    }
                    do {
                        i7++;
                        if (i7 >= i3) {
                            break;
                        }
                    } while (bArr[i7] == b5);
                    bidiRunArr[i6] = new BidiRun(i8, i7 - i8, b5);
                    i6++;
                } while (i7 < i3);
                if (i3 < i2) {
                    bidiRunArr[i6] = new BidiRun(i3, i2 - i3, bidiBase.paraLevel);
                    if (bidiBase.paraLevel < b3) {
                        b3 = bidiBase.paraLevel;
                    }
                }
                bidiBase.runs = bidiRunArr;
                bidiBase.runCount = i4;
                reorderLine(bidiBase, b3, b4);
                int i9 = 0;
                for (int i10 = 0; i10 < i4; i10++) {
                    bidiRunArr[i10].level = bArr[bidiRunArr[i10].start];
                    BidiRun bidiRun = bidiRunArr[i10];
                    int i11 = bidiRun.limit + i9;
                    bidiRun.limit = i11;
                    i9 = i11;
                }
                if (i6 < i4) {
                    bidiRunArr[(bidiBase.paraLevel & 1) != 0 ? 0 : i6].level = bidiBase.paraLevel;
                }
            }
        }
        if (bidiBase.insertPoints.size > 0) {
            for (int i12 = 0; i12 < bidiBase.insertPoints.size; i12++) {
                BidiBase.Point point = bidiBase.insertPoints.points[i12];
                bidiBase.runs[getRunFromLogicalIndex(bidiBase, point.pos)].insertRemove |= point.flag;
            }
        }
        if (bidiBase.controlCount > 0) {
            for (int i13 = 0; i13 < bidiBase.length; i13++) {
                if (BidiBase.IsBidiControlChar(bidiBase.text[i13])) {
                    bidiBase.runs[getRunFromLogicalIndex(bidiBase, i13)].insertRemove--;
                }
            }
        }
    }

    static int[] prepareReorder(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        byte b2 = 62;
        byte b3 = 0;
        int length = bArr.length;
        while (length > 0) {
            length--;
            byte b4 = bArr[length];
            if (b4 > 62) {
                return null;
            }
            if (b4 < b2) {
                b2 = b4;
            }
            if (b4 > b3) {
                b3 = b4;
            }
        }
        bArr2[0] = b2;
        bArr3[0] = b3;
        int[] iArr = new int[bArr.length];
        int length2 = bArr.length;
        while (length2 > 0) {
            length2--;
            iArr[length2] = length2;
        }
        return iArr;
    }

    static int[] reorderVisual(byte[] bArr) {
        byte[] bArr2 = new byte[1];
        byte[] bArr3 = new byte[1];
        int[] iArrPrepareReorder = prepareReorder(bArr, bArr2, bArr3);
        if (iArrPrepareReorder == null) {
            return null;
        }
        byte b2 = bArr2[0];
        byte b3 = bArr3[0];
        if (b2 == b3 && (b2 & 1) == 0) {
            return iArrPrepareReorder;
        }
        byte b4 = (byte) (b2 | 1);
        do {
            int i2 = 0;
            while (true) {
                if (i2 < bArr.length && bArr[i2] < b3) {
                    i2++;
                } else {
                    if (i2 >= bArr.length) {
                        break;
                    }
                    int i3 = i2;
                    do {
                        i3++;
                        if (i3 >= bArr.length) {
                            break;
                        }
                    } while (bArr[i3] >= b3);
                    for (int i4 = i3 - 1; i2 < i4; i4--) {
                        int i5 = iArrPrepareReorder[i2];
                        iArrPrepareReorder[i2] = iArrPrepareReorder[i4];
                        iArrPrepareReorder[i4] = i5;
                        i2++;
                    }
                    if (i3 == bArr.length) {
                        break;
                    }
                    i2 = i3 + 1;
                }
            }
            b3 = (byte) (b3 - 1);
        } while (b3 >= b4);
        return iArrPrepareReorder;
    }

    static int[] getVisualMap(BidiBase bidiBase) {
        BidiRun[] bidiRunArr = bidiBase.runs;
        int i2 = bidiBase.length > bidiBase.resultLength ? bidiBase.length : bidiBase.resultLength;
        int[] iArr = new int[i2];
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < bidiBase.runCount; i5++) {
            int i6 = bidiRunArr[i5].start;
            int i7 = bidiRunArr[i5].limit;
            if (bidiRunArr[i5].isEvenRun()) {
                do {
                    int i8 = i4;
                    i4++;
                    int i9 = i6;
                    i6++;
                    iArr[i8] = i9;
                    i3++;
                } while (i3 < i7);
            } else {
                int i10 = i6 + (i7 - i3);
                do {
                    int i11 = i4;
                    i4++;
                    i10--;
                    iArr[i11] = i10;
                    i3++;
                } while (i3 < i7);
            }
        }
        if (bidiBase.insertPoints.size > 0) {
            int i12 = 0;
            int i13 = bidiBase.runCount;
            BidiRun[] bidiRunArr2 = bidiBase.runs;
            for (int i14 = 0; i14 < i13; i14++) {
                int i15 = bidiRunArr2[i14].insertRemove;
                if ((i15 & 5) > 0) {
                    i12++;
                }
                if ((i15 & 10) > 0) {
                    i12++;
                }
            }
            int i16 = bidiBase.resultLength;
            int i17 = i13 - 1;
            while (i17 >= 0 && i12 > 0) {
                int i18 = bidiRunArr2[i17].insertRemove;
                if ((i18 & 10) > 0) {
                    i16--;
                    iArr[i16] = -1;
                    i12--;
                }
                int i19 = i17 > 0 ? bidiRunArr2[i17 - 1].limit : 0;
                for (int i20 = bidiRunArr2[i17].limit - 1; i20 >= i19 && i12 > 0; i20--) {
                    i16--;
                    iArr[i16] = iArr[i20];
                }
                if ((i18 & 5) > 0) {
                    i16--;
                    iArr[i16] = -1;
                    i12--;
                }
                i17--;
            }
        } else if (bidiBase.controlCount > 0) {
            int i21 = bidiBase.runCount;
            BidiRun[] bidiRunArr3 = bidiBase.runs;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            while (i24 < i21) {
                int i25 = bidiRunArr3[i24].limit - i22;
                int i26 = bidiRunArr3[i24].insertRemove;
                if (i26 == 0 && i23 == i22) {
                    i23 += i25;
                } else if (i26 == 0) {
                    int i27 = bidiRunArr3[i24].limit;
                    for (int i28 = i22; i28 < i27; i28++) {
                        int i29 = i23;
                        i23++;
                        iArr[i29] = iArr[i28];
                    }
                } else {
                    int i30 = bidiRunArr3[i24].start;
                    boolean zIsEvenRun = bidiRunArr3[i24].isEvenRun();
                    int i31 = (i30 + i25) - 1;
                    for (int i32 = 0; i32 < i25; i32++) {
                        int i33 = zIsEvenRun ? i30 + i32 : i31 - i32;
                        if (!BidiBase.IsBidiControlChar(bidiBase.text[i33])) {
                            int i34 = i23;
                            i23++;
                            iArr[i34] = i33;
                        }
                    }
                }
                i24++;
                i22 += i25;
            }
        }
        if (i2 == bidiBase.resultLength) {
            return iArr;
        }
        int[] iArr2 = new int[bidiBase.resultLength];
        System.arraycopy(iArr, 0, iArr2, 0, bidiBase.resultLength);
        return iArr2;
    }
}
