package java.util;

/* loaded from: rt.jar:java/util/DualPivotQuicksort.class */
final class DualPivotQuicksort {
    private static final int MAX_RUN_COUNT = 67;
    private static final int MAX_RUN_LENGTH = 33;
    private static final int QUICKSORT_THRESHOLD = 286;
    private static final int INSERTION_SORT_THRESHOLD = 47;
    private static final int COUNTING_SORT_THRESHOLD_FOR_BYTE = 29;
    private static final int COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR = 3200;
    private static final int NUM_SHORT_VALUES = 65536;
    private static final int NUM_CHAR_VALUES = 65536;
    private static final int NUM_BYTE_VALUES = 256;

    private DualPivotQuicksort() {
    }

    static void sort(int[] iArr, int i2, int i3, int[] iArr2, int i4, int i5) {
        int[] iArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(iArr, i2, i3, true);
            return;
        }
        int[] iArr4 = new int[68];
        int i8 = 0;
        iArr4[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (iArr[i9] < iArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (iArr[i9 - 1] <= iArr[i9]);
            } else if (iArr[i9] > iArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (iArr[i9 - 1] >= iArr[i9]);
                int i10 = iArr4[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    int i12 = iArr[i10];
                    iArr[i10] = iArr[i11];
                    iArr[i11] = i12;
                }
            } else {
                int i13 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && iArr[i9 - 1] == iArr[i9]) {
                        i13--;
                    }
                } while (i13 != 0);
                sort(iArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr4[i8] = i9;
            } else {
                sort(iArr, i2, i3, true);
                return;
            }
        }
        int i14 = i3 + 1;
        if (iArr4[i8] == i3) {
            i8++;
            iArr4[i8] = i14;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i15 = 1;
        while (true) {
            int i16 = i15 << 1;
            i15 = i16;
            if (i16 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i17 = i14 - i2;
        if (iArr2 == null || i5 < i17 || i4 + i17 > iArr2.length) {
            iArr2 = new int[i17];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(iArr, i2, iArr2, i4, i17);
            iArr3 = iArr;
            i7 = 0;
            iArr = iArr2;
            i6 = i4 - i2;
        } else {
            iArr3 = iArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i18 = 0;
            for (int i19 = 0 + 2; i19 <= i8; i19 += 2) {
                int i20 = iArr4[i19];
                int i21 = iArr4[i19 - 1];
                int i22 = iArr4[i19 - 2];
                int i23 = i22;
                int i24 = i21;
                while (i22 < i20) {
                    if (i24 >= i20 || (i23 < i21 && iArr[i23 + i6] <= iArr[i24 + i6])) {
                        int i25 = i23;
                        i23++;
                        iArr3[i22 + i7] = iArr[i25 + i6];
                    } else {
                        int i26 = i24;
                        i24++;
                        iArr3[i22 + i7] = iArr[i26 + i6];
                    }
                    i22++;
                }
                i18++;
                iArr4[i18] = i20;
            }
            if ((i8 & 1) != 0) {
                int i27 = i14;
                int i28 = iArr4[i8 - 1];
                while (true) {
                    i27--;
                    if (i27 < i28) {
                        break;
                    } else {
                        iArr3[i27 + i7] = iArr[i27 + i6];
                    }
                }
                i18++;
                iArr4[i18] = i14;
            }
            int[] iArr5 = iArr;
            iArr = iArr3;
            iArr3 = iArr5;
            int i29 = i6;
            i6 = i7;
            i7 = i29;
            i8 = i18;
        }
    }

    private static void sort(int[] iArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        int i7 = iArr[i5 + 1];
                        while (i7 < iArr[i6]) {
                            iArr[i6 + 1] = iArr[i6];
                            int i8 = i6;
                            i6--;
                            if (i8 == i2) {
                                break;
                            }
                        }
                        iArr[i6 + 1] = i7;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (iArr[i2] < iArr[i2 - 1]) {
                        while (true) {
                            int i9 = i2;
                            int i10 = i2 + 1;
                            if (i10 > i3) {
                                break;
                            }
                            int i11 = iArr[i9];
                            int i12 = iArr[i10];
                            if (i11 < i12) {
                                i12 = i11;
                                i11 = iArr[i10];
                            }
                            while (true) {
                                i9--;
                                if (i11 >= iArr[i9]) {
                                    break;
                                } else {
                                    iArr[i9 + 2] = iArr[i9];
                                }
                            }
                            int i13 = i9 + 1;
                            iArr[i13 + 1] = i11;
                            while (true) {
                                i13--;
                                if (i12 < iArr[i13]) {
                                    iArr[i13 + 1] = iArr[i13];
                                }
                            }
                            iArr[i13 + 1] = i12;
                            i2 = i10 + 1;
                        }
                        int i14 = iArr[i3];
                        while (true) {
                            i3--;
                            if (i14 < iArr[i3]) {
                                iArr[i3 + 1] = iArr[i3];
                            } else {
                                iArr[i3 + 1] = i14;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i15 = (i4 >> 3) + (i4 >> 6) + 1;
            int i16 = (i2 + i3) >>> 1;
            int i17 = i16 - i15;
            int i18 = i17 - i15;
            int i19 = i16 + i15;
            int i20 = i19 + i15;
            if (iArr[i17] < iArr[i18]) {
                int i21 = iArr[i17];
                iArr[i17] = iArr[i18];
                iArr[i18] = i21;
            }
            if (iArr[i16] < iArr[i17]) {
                int i22 = iArr[i16];
                iArr[i16] = iArr[i17];
                iArr[i17] = i22;
                if (i22 < iArr[i18]) {
                    iArr[i17] = iArr[i18];
                    iArr[i18] = i22;
                }
            }
            if (iArr[i19] < iArr[i16]) {
                int i23 = iArr[i19];
                iArr[i19] = iArr[i16];
                iArr[i16] = i23;
                if (i23 < iArr[i17]) {
                    iArr[i16] = iArr[i17];
                    iArr[i17] = i23;
                    if (i23 < iArr[i18]) {
                        iArr[i17] = iArr[i18];
                        iArr[i18] = i23;
                    }
                }
            }
            if (iArr[i20] < iArr[i19]) {
                int i24 = iArr[i20];
                iArr[i20] = iArr[i19];
                iArr[i19] = i24;
                if (i24 < iArr[i16]) {
                    iArr[i19] = iArr[i16];
                    iArr[i16] = i24;
                    if (i24 < iArr[i17]) {
                        iArr[i16] = iArr[i17];
                        iArr[i17] = i24;
                        if (i24 < iArr[i18]) {
                            iArr[i17] = iArr[i18];
                            iArr[i18] = i24;
                        }
                    }
                }
            }
            int i25 = i2;
            int i26 = i3;
            if (iArr[i18] != iArr[i17] && iArr[i17] != iArr[i16] && iArr[i16] != iArr[i19] && iArr[i19] != iArr[i20]) {
                int i27 = iArr[i17];
                int i28 = iArr[i19];
                iArr[i17] = iArr[i2];
                iArr[i19] = iArr[i3];
                do {
                    i25++;
                } while (iArr[i25] < i27);
                do {
                    i26--;
                } while (iArr[i26] > i28);
                int i29 = i25 - 1;
                loop9: while (true) {
                    i29++;
                    if (i29 > i26) {
                        break;
                    }
                    int i30 = iArr[i29];
                    if (i30 < i27) {
                        iArr[i29] = iArr[i25];
                        iArr[i25] = i30;
                        i25++;
                    } else if (i30 > i28) {
                        while (iArr[i26] > i28) {
                            int i31 = i26;
                            i26--;
                            if (i31 == i29) {
                                break loop9;
                            }
                        }
                        if (iArr[i26] < i27) {
                            iArr[i29] = iArr[i25];
                            iArr[i25] = iArr[i26];
                            i25++;
                        } else {
                            iArr[i29] = iArr[i26];
                        }
                        iArr[i26] = i30;
                        i26--;
                    } else {
                        continue;
                    }
                }
                iArr[i2] = iArr[i25 - 1];
                iArr[i25 - 1] = i27;
                iArr[i3] = iArr[i26 + 1];
                iArr[i26 + 1] = i28;
                sort(iArr, i2, i25 - 2, z2);
                sort(iArr, i26 + 2, i3, false);
                if (i25 < i18 && i20 < i26) {
                    while (iArr[i25] == i27) {
                        i25++;
                    }
                    while (iArr[i26] == i28) {
                        i26--;
                    }
                    int i32 = i25 - 1;
                    loop13: while (true) {
                        i32++;
                        if (i32 > i26) {
                            break;
                        }
                        int i33 = iArr[i32];
                        if (i33 == i27) {
                            iArr[i32] = iArr[i25];
                            iArr[i25] = i33;
                            i25++;
                        } else if (i33 == i28) {
                            while (iArr[i26] == i28) {
                                int i34 = i26;
                                i26--;
                                if (i34 == i32) {
                                    break loop13;
                                }
                            }
                            if (iArr[i26] == i27) {
                                iArr[i32] = iArr[i25];
                                iArr[i25] = i27;
                                i25++;
                            } else {
                                iArr[i32] = iArr[i26];
                            }
                            iArr[i26] = i33;
                            i26--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(iArr, i25, i26, false);
                return;
            }
            int i35 = iArr[i16];
            for (int i36 = i25; i36 <= i26; i36++) {
                if (iArr[i36] != i35) {
                    int i37 = iArr[i36];
                    if (i37 < i35) {
                        iArr[i36] = iArr[i25];
                        iArr[i25] = i37;
                        i25++;
                    } else {
                        while (iArr[i26] > i35) {
                            i26--;
                        }
                        if (iArr[i26] < i35) {
                            iArr[i36] = iArr[i25];
                            iArr[i25] = iArr[i26];
                            i25++;
                        } else {
                            iArr[i36] = i35;
                        }
                        iArr[i26] = i37;
                        i26--;
                    }
                }
            }
            sort(iArr, i2, i25 - 1, z2);
            sort(iArr, i26 + 1, i3, false);
        }
    }

    static void sort(long[] jArr, int i2, int i3, long[] jArr2, int i4, int i5) {
        long[] jArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(jArr, i2, i3, true);
            return;
        }
        int[] iArr = new int[68];
        int i8 = 0;
        iArr[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (jArr[i9] < jArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (jArr[i9 - 1] <= jArr[i9]);
            } else if (jArr[i9] > jArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (jArr[i9 - 1] >= jArr[i9]);
                int i10 = iArr[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    long j2 = jArr[i10];
                    jArr[i10] = jArr[i11];
                    jArr[i11] = j2;
                }
            } else {
                int i12 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && jArr[i9 - 1] == jArr[i9]) {
                        i12--;
                    }
                } while (i12 != 0);
                sort(jArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr[i8] = i9;
            } else {
                sort(jArr, i2, i3, true);
                return;
            }
        }
        int i13 = i3 + 1;
        if (iArr[i8] == i3) {
            i8++;
            iArr[i8] = i13;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i14 = 1;
        while (true) {
            int i15 = i14 << 1;
            i14 = i15;
            if (i15 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i16 = i13 - i2;
        if (jArr2 == null || i5 < i16 || i4 + i16 > jArr2.length) {
            jArr2 = new long[i16];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(jArr, i2, jArr2, i4, i16);
            jArr3 = jArr;
            i7 = 0;
            jArr = jArr2;
            i6 = i4 - i2;
        } else {
            jArr3 = jArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i17 = 0;
            for (int i18 = 0 + 2; i18 <= i8; i18 += 2) {
                int i19 = iArr[i18];
                int i20 = iArr[i18 - 1];
                int i21 = iArr[i18 - 2];
                int i22 = i21;
                int i23 = i20;
                while (i21 < i19) {
                    if (i23 >= i19 || (i22 < i20 && jArr[i22 + i6] <= jArr[i23 + i6])) {
                        int i24 = i22;
                        i22++;
                        jArr3[i21 + i7] = jArr[i24 + i6];
                    } else {
                        int i25 = i23;
                        i23++;
                        jArr3[i21 + i7] = jArr[i25 + i6];
                    }
                    i21++;
                }
                i17++;
                iArr[i17] = i19;
            }
            if ((i8 & 1) != 0) {
                int i26 = i13;
                int i27 = iArr[i8 - 1];
                while (true) {
                    i26--;
                    if (i26 < i27) {
                        break;
                    } else {
                        jArr3[i26 + i7] = jArr[i26 + i6];
                    }
                }
                i17++;
                iArr[i17] = i13;
            }
            long[] jArr4 = jArr;
            jArr = jArr3;
            jArr3 = jArr4;
            int i28 = i6;
            i6 = i7;
            i7 = i28;
            i8 = i17;
        }
    }

    private static void sort(long[] jArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        long j2 = jArr[i5 + 1];
                        while (j2 < jArr[i6]) {
                            jArr[i6 + 1] = jArr[i6];
                            int i7 = i6;
                            i6--;
                            if (i7 == i2) {
                                break;
                            }
                        }
                        jArr[i6 + 1] = j2;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (jArr[i2] < jArr[i2 - 1]) {
                        while (true) {
                            int i8 = i2;
                            int i9 = i2 + 1;
                            if (i9 > i3) {
                                break;
                            }
                            long j3 = jArr[i8];
                            long j4 = jArr[i9];
                            if (j3 < j4) {
                                j4 = j3;
                                j3 = jArr[i9];
                            }
                            while (true) {
                                i8--;
                                if (j3 >= jArr[i8]) {
                                    break;
                                } else {
                                    jArr[i8 + 2] = jArr[i8];
                                }
                            }
                            int i10 = i8 + 1;
                            jArr[i10 + 1] = j3;
                            while (true) {
                                i10--;
                                if (j4 < jArr[i10]) {
                                    jArr[i10 + 1] = jArr[i10];
                                }
                            }
                            jArr[i10 + 1] = j4;
                            i2 = i9 + 1;
                        }
                        long j5 = jArr[i3];
                        while (true) {
                            i3--;
                            if (j5 < jArr[i3]) {
                                jArr[i3 + 1] = jArr[i3];
                            } else {
                                jArr[i3 + 1] = j5;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i11 = (i4 >> 3) + (i4 >> 6) + 1;
            int i12 = (i2 + i3) >>> 1;
            int i13 = i12 - i11;
            int i14 = i13 - i11;
            int i15 = i12 + i11;
            int i16 = i15 + i11;
            if (jArr[i13] < jArr[i14]) {
                long j6 = jArr[i13];
                jArr[i13] = jArr[i14];
                jArr[i14] = j6;
            }
            if (jArr[i12] < jArr[i13]) {
                long j7 = jArr[i12];
                jArr[i12] = jArr[i13];
                jArr[i13] = j7;
                if (j7 < jArr[i14]) {
                    jArr[i13] = jArr[i14];
                    jArr[i14] = j7;
                }
            }
            if (jArr[i15] < jArr[i12]) {
                long j8 = jArr[i15];
                jArr[i15] = jArr[i12];
                jArr[i12] = j8;
                if (j8 < jArr[i13]) {
                    jArr[i12] = jArr[i13];
                    jArr[i13] = j8;
                    if (j8 < jArr[i14]) {
                        jArr[i13] = jArr[i14];
                        jArr[i14] = j8;
                    }
                }
            }
            if (jArr[i16] < jArr[i15]) {
                long j9 = jArr[i16];
                jArr[i16] = jArr[i15];
                jArr[i15] = j9;
                if (j9 < jArr[i12]) {
                    jArr[i15] = jArr[i12];
                    jArr[i12] = j9;
                    if (j9 < jArr[i13]) {
                        jArr[i12] = jArr[i13];
                        jArr[i13] = j9;
                        if (j9 < jArr[i14]) {
                            jArr[i13] = jArr[i14];
                            jArr[i14] = j9;
                        }
                    }
                }
            }
            int i17 = i2;
            int i18 = i3;
            if (jArr[i14] != jArr[i13] && jArr[i13] != jArr[i12] && jArr[i12] != jArr[i15] && jArr[i15] != jArr[i16]) {
                long j10 = jArr[i13];
                long j11 = jArr[i15];
                jArr[i13] = jArr[i2];
                jArr[i15] = jArr[i3];
                do {
                    i17++;
                } while (jArr[i17] < j10);
                do {
                    i18--;
                } while (jArr[i18] > j11);
                int i19 = i17 - 1;
                loop9: while (true) {
                    i19++;
                    if (i19 > i18) {
                        break;
                    }
                    long j12 = jArr[i19];
                    if (j12 < j10) {
                        jArr[i19] = jArr[i17];
                        jArr[i17] = j12;
                        i17++;
                    } else if (j12 > j11) {
                        while (jArr[i18] > j11) {
                            int i20 = i18;
                            i18--;
                            if (i20 == i19) {
                                break loop9;
                            }
                        }
                        if (jArr[i18] < j10) {
                            jArr[i19] = jArr[i17];
                            jArr[i17] = jArr[i18];
                            i17++;
                        } else {
                            jArr[i19] = jArr[i18];
                        }
                        jArr[i18] = j12;
                        i18--;
                    } else {
                        continue;
                    }
                }
                jArr[i2] = jArr[i17 - 1];
                jArr[i17 - 1] = j10;
                jArr[i3] = jArr[i18 + 1];
                jArr[i18 + 1] = j11;
                sort(jArr, i2, i17 - 2, z2);
                sort(jArr, i18 + 2, i3, false);
                if (i17 < i14 && i16 < i18) {
                    while (jArr[i17] == j10) {
                        i17++;
                    }
                    while (jArr[i18] == j11) {
                        i18--;
                    }
                    int i21 = i17 - 1;
                    loop13: while (true) {
                        i21++;
                        if (i21 > i18) {
                            break;
                        }
                        long j13 = jArr[i21];
                        if (j13 == j10) {
                            jArr[i21] = jArr[i17];
                            jArr[i17] = j13;
                            i17++;
                        } else if (j13 == j11) {
                            while (jArr[i18] == j11) {
                                int i22 = i18;
                                i18--;
                                if (i22 == i21) {
                                    break loop13;
                                }
                            }
                            if (jArr[i18] == j10) {
                                jArr[i21] = jArr[i17];
                                jArr[i17] = j10;
                                i17++;
                            } else {
                                jArr[i21] = jArr[i18];
                            }
                            jArr[i18] = j13;
                            i18--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(jArr, i17, i18, false);
                return;
            }
            long j14 = jArr[i12];
            for (int i23 = i17; i23 <= i18; i23++) {
                if (jArr[i23] != j14) {
                    long j15 = jArr[i23];
                    if (j15 < j14) {
                        jArr[i23] = jArr[i17];
                        jArr[i17] = j15;
                        i17++;
                    } else {
                        while (jArr[i18] > j14) {
                            i18--;
                        }
                        if (jArr[i18] < j14) {
                            jArr[i23] = jArr[i17];
                            jArr[i17] = jArr[i18];
                            i17++;
                        } else {
                            jArr[i23] = j14;
                        }
                        jArr[i18] = j15;
                        i18--;
                    }
                }
            }
            sort(jArr, i2, i17 - 1, z2);
            sort(jArr, i18 + 1, i3, false);
        }
    }

    static void sort(short[] sArr, int i2, int i3, short[] sArr2, int i4, int i5) {
        if (i3 - i2 > COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR) {
            int[] iArr = new int[65536];
            int i6 = i2 - 1;
            while (true) {
                i6++;
                if (i6 > i3) {
                    break;
                }
                int i7 = sArr[i6] - Short.MIN_VALUE;
                iArr[i7] = iArr[i7] + 1;
            }
            int i8 = 65536;
            int i9 = i3 + 1;
            while (i9 > i2) {
                do {
                    i8--;
                } while (iArr[i8] == 0);
                short s2 = (short) (i8 + Short.MIN_VALUE);
                int i10 = iArr[i8];
                do {
                    i9--;
                    sArr[i9] = s2;
                    i10--;
                } while (i10 > 0);
            }
            return;
        }
        doSort(sArr, i2, i3, sArr2, i4, i5);
    }

    private static void doSort(short[] sArr, int i2, int i3, short[] sArr2, int i4, int i5) {
        short[] sArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(sArr, i2, i3, true);
            return;
        }
        int[] iArr = new int[68];
        int i8 = 0;
        iArr[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (sArr[i9] < sArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (sArr[i9 - 1] <= sArr[i9]);
            } else if (sArr[i9] > sArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (sArr[i9 - 1] >= sArr[i9]);
                int i10 = iArr[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    short s2 = sArr[i10];
                    sArr[i10] = sArr[i11];
                    sArr[i11] = s2;
                }
            } else {
                int i12 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && sArr[i9 - 1] == sArr[i9]) {
                        i12--;
                    }
                } while (i12 != 0);
                sort(sArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr[i8] = i9;
            } else {
                sort(sArr, i2, i3, true);
                return;
            }
        }
        int i13 = i3 + 1;
        if (iArr[i8] == i3) {
            i8++;
            iArr[i8] = i13;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i14 = 1;
        while (true) {
            int i15 = i14 << 1;
            i14 = i15;
            if (i15 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i16 = i13 - i2;
        if (sArr2 == null || i5 < i16 || i4 + i16 > sArr2.length) {
            sArr2 = new short[i16];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(sArr, i2, sArr2, i4, i16);
            sArr3 = sArr;
            i7 = 0;
            sArr = sArr2;
            i6 = i4 - i2;
        } else {
            sArr3 = sArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i17 = 0;
            for (int i18 = 0 + 2; i18 <= i8; i18 += 2) {
                int i19 = iArr[i18];
                int i20 = iArr[i18 - 1];
                int i21 = iArr[i18 - 2];
                int i22 = i21;
                int i23 = i20;
                while (i21 < i19) {
                    if (i23 >= i19 || (i22 < i20 && sArr[i22 + i6] <= sArr[i23 + i6])) {
                        int i24 = i22;
                        i22++;
                        sArr3[i21 + i7] = sArr[i24 + i6];
                    } else {
                        int i25 = i23;
                        i23++;
                        sArr3[i21 + i7] = sArr[i25 + i6];
                    }
                    i21++;
                }
                i17++;
                iArr[i17] = i19;
            }
            if ((i8 & 1) != 0) {
                int i26 = i13;
                int i27 = iArr[i8 - 1];
                while (true) {
                    i26--;
                    if (i26 < i27) {
                        break;
                    } else {
                        sArr3[i26 + i7] = sArr[i26 + i6];
                    }
                }
                i17++;
                iArr[i17] = i13;
            }
            short[] sArr4 = sArr;
            sArr = sArr3;
            sArr3 = sArr4;
            int i28 = i6;
            i6 = i7;
            i7 = i28;
            i8 = i17;
        }
    }

    private static void sort(short[] sArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        short s2 = sArr[i5 + 1];
                        while (s2 < sArr[i6]) {
                            sArr[i6 + 1] = sArr[i6];
                            int i7 = i6;
                            i6--;
                            if (i7 == i2) {
                                break;
                            }
                        }
                        sArr[i6 + 1] = s2;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (sArr[i2] < sArr[i2 - 1]) {
                        while (true) {
                            int i8 = i2;
                            int i9 = i2 + 1;
                            if (i9 > i3) {
                                break;
                            }
                            short s3 = sArr[i8];
                            short s4 = sArr[i9];
                            if (s3 < s4) {
                                s4 = s3;
                                s3 = sArr[i9];
                            }
                            while (true) {
                                i8--;
                                if (s3 >= sArr[i8]) {
                                    break;
                                } else {
                                    sArr[i8 + 2] = sArr[i8];
                                }
                            }
                            int i10 = i8 + 1;
                            sArr[i10 + 1] = s3;
                            while (true) {
                                i10--;
                                if (s4 < sArr[i10]) {
                                    sArr[i10 + 1] = sArr[i10];
                                }
                            }
                            sArr[i10 + 1] = s4;
                            i2 = i9 + 1;
                        }
                        short s5 = sArr[i3];
                        while (true) {
                            i3--;
                            if (s5 < sArr[i3]) {
                                sArr[i3 + 1] = sArr[i3];
                            } else {
                                sArr[i3 + 1] = s5;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i11 = (i4 >> 3) + (i4 >> 6) + 1;
            int i12 = (i2 + i3) >>> 1;
            int i13 = i12 - i11;
            int i14 = i13 - i11;
            int i15 = i12 + i11;
            int i16 = i15 + i11;
            if (sArr[i13] < sArr[i14]) {
                short s6 = sArr[i13];
                sArr[i13] = sArr[i14];
                sArr[i14] = s6;
            }
            if (sArr[i12] < sArr[i13]) {
                short s7 = sArr[i12];
                sArr[i12] = sArr[i13];
                sArr[i13] = s7;
                if (s7 < sArr[i14]) {
                    sArr[i13] = sArr[i14];
                    sArr[i14] = s7;
                }
            }
            if (sArr[i15] < sArr[i12]) {
                short s8 = sArr[i15];
                sArr[i15] = sArr[i12];
                sArr[i12] = s8;
                if (s8 < sArr[i13]) {
                    sArr[i12] = sArr[i13];
                    sArr[i13] = s8;
                    if (s8 < sArr[i14]) {
                        sArr[i13] = sArr[i14];
                        sArr[i14] = s8;
                    }
                }
            }
            if (sArr[i16] < sArr[i15]) {
                short s9 = sArr[i16];
                sArr[i16] = sArr[i15];
                sArr[i15] = s9;
                if (s9 < sArr[i12]) {
                    sArr[i15] = sArr[i12];
                    sArr[i12] = s9;
                    if (s9 < sArr[i13]) {
                        sArr[i12] = sArr[i13];
                        sArr[i13] = s9;
                        if (s9 < sArr[i14]) {
                            sArr[i13] = sArr[i14];
                            sArr[i14] = s9;
                        }
                    }
                }
            }
            int i17 = i2;
            int i18 = i3;
            if (sArr[i14] != sArr[i13] && sArr[i13] != sArr[i12] && sArr[i12] != sArr[i15] && sArr[i15] != sArr[i16]) {
                short s10 = sArr[i13];
                short s11 = sArr[i15];
                sArr[i13] = sArr[i2];
                sArr[i15] = sArr[i3];
                do {
                    i17++;
                } while (sArr[i17] < s10);
                do {
                    i18--;
                } while (sArr[i18] > s11);
                int i19 = i17 - 1;
                loop9: while (true) {
                    i19++;
                    if (i19 > i18) {
                        break;
                    }
                    short s12 = sArr[i19];
                    if (s12 < s10) {
                        sArr[i19] = sArr[i17];
                        sArr[i17] = s12;
                        i17++;
                    } else if (s12 > s11) {
                        while (sArr[i18] > s11) {
                            int i20 = i18;
                            i18--;
                            if (i20 == i19) {
                                break loop9;
                            }
                        }
                        if (sArr[i18] < s10) {
                            sArr[i19] = sArr[i17];
                            sArr[i17] = sArr[i18];
                            i17++;
                        } else {
                            sArr[i19] = sArr[i18];
                        }
                        sArr[i18] = s12;
                        i18--;
                    } else {
                        continue;
                    }
                }
                sArr[i2] = sArr[i17 - 1];
                sArr[i17 - 1] = s10;
                sArr[i3] = sArr[i18 + 1];
                sArr[i18 + 1] = s11;
                sort(sArr, i2, i17 - 2, z2);
                sort(sArr, i18 + 2, i3, false);
                if (i17 < i14 && i16 < i18) {
                    while (sArr[i17] == s10) {
                        i17++;
                    }
                    while (sArr[i18] == s11) {
                        i18--;
                    }
                    int i21 = i17 - 1;
                    loop13: while (true) {
                        i21++;
                        if (i21 > i18) {
                            break;
                        }
                        short s13 = sArr[i21];
                        if (s13 == s10) {
                            sArr[i21] = sArr[i17];
                            sArr[i17] = s13;
                            i17++;
                        } else if (s13 == s11) {
                            while (sArr[i18] == s11) {
                                int i22 = i18;
                                i18--;
                                if (i22 == i21) {
                                    break loop13;
                                }
                            }
                            if (sArr[i18] == s10) {
                                sArr[i21] = sArr[i17];
                                sArr[i17] = s10;
                                i17++;
                            } else {
                                sArr[i21] = sArr[i18];
                            }
                            sArr[i18] = s13;
                            i18--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(sArr, i17, i18, false);
                return;
            }
            short s14 = sArr[i12];
            for (int i23 = i17; i23 <= i18; i23++) {
                if (sArr[i23] != s14) {
                    short s15 = sArr[i23];
                    if (s15 < s14) {
                        sArr[i23] = sArr[i17];
                        sArr[i17] = s15;
                        i17++;
                    } else {
                        while (sArr[i18] > s14) {
                            i18--;
                        }
                        if (sArr[i18] < s14) {
                            sArr[i23] = sArr[i17];
                            sArr[i17] = sArr[i18];
                            i17++;
                        } else {
                            sArr[i23] = s14;
                        }
                        sArr[i18] = s15;
                        i18--;
                    }
                }
            }
            sort(sArr, i2, i17 - 1, z2);
            sort(sArr, i18 + 1, i3, false);
        }
    }

    static void sort(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5) {
        if (i3 - i2 > COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR) {
            int[] iArr = new int[65536];
            int i6 = i2 - 1;
            while (true) {
                i6++;
                if (i6 > i3) {
                    break;
                }
                char c2 = cArr[i6];
                iArr[c2] = iArr[c2] + 1;
            }
            int i7 = 65536;
            int i8 = i3 + 1;
            while (i8 > i2) {
                do {
                    i7--;
                } while (iArr[i7] == 0);
                char c3 = (char) i7;
                int i9 = iArr[i7];
                do {
                    i8--;
                    cArr[i8] = c3;
                    i9--;
                } while (i9 > 0);
            }
            return;
        }
        doSort(cArr, i2, i3, cArr2, i4, i5);
    }

    private static void doSort(char[] cArr, int i2, int i3, char[] cArr2, int i4, int i5) {
        char[] cArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(cArr, i2, i3, true);
            return;
        }
        int[] iArr = new int[68];
        int i8 = 0;
        iArr[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (cArr[i9] < cArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (cArr[i9 - 1] <= cArr[i9]);
            } else if (cArr[i9] > cArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (cArr[i9 - 1] >= cArr[i9]);
                int i10 = iArr[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    char c2 = cArr[i10];
                    cArr[i10] = cArr[i11];
                    cArr[i11] = c2;
                }
            } else {
                int i12 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && cArr[i9 - 1] == cArr[i9]) {
                        i12--;
                    }
                } while (i12 != 0);
                sort(cArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr[i8] = i9;
            } else {
                sort(cArr, i2, i3, true);
                return;
            }
        }
        int i13 = i3 + 1;
        if (iArr[i8] == i3) {
            i8++;
            iArr[i8] = i13;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i14 = 1;
        while (true) {
            int i15 = i14 << 1;
            i14 = i15;
            if (i15 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i16 = i13 - i2;
        if (cArr2 == null || i5 < i16 || i4 + i16 > cArr2.length) {
            cArr2 = new char[i16];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(cArr, i2, cArr2, i4, i16);
            cArr3 = cArr;
            i7 = 0;
            cArr = cArr2;
            i6 = i4 - i2;
        } else {
            cArr3 = cArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i17 = 0;
            for (int i18 = 0 + 2; i18 <= i8; i18 += 2) {
                int i19 = iArr[i18];
                int i20 = iArr[i18 - 1];
                int i21 = iArr[i18 - 2];
                int i22 = i21;
                int i23 = i20;
                while (i21 < i19) {
                    if (i23 >= i19 || (i22 < i20 && cArr[i22 + i6] <= cArr[i23 + i6])) {
                        int i24 = i22;
                        i22++;
                        cArr3[i21 + i7] = cArr[i24 + i6];
                    } else {
                        int i25 = i23;
                        i23++;
                        cArr3[i21 + i7] = cArr[i25 + i6];
                    }
                    i21++;
                }
                i17++;
                iArr[i17] = i19;
            }
            if ((i8 & 1) != 0) {
                int i26 = i13;
                int i27 = iArr[i8 - 1];
                while (true) {
                    i26--;
                    if (i26 < i27) {
                        break;
                    } else {
                        cArr3[i26 + i7] = cArr[i26 + i6];
                    }
                }
                i17++;
                iArr[i17] = i13;
            }
            char[] cArr4 = cArr;
            cArr = cArr3;
            cArr3 = cArr4;
            int i28 = i6;
            i6 = i7;
            i7 = i28;
            i8 = i17;
        }
    }

    private static void sort(char[] cArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        char c2 = cArr[i5 + 1];
                        while (c2 < cArr[i6]) {
                            cArr[i6 + 1] = cArr[i6];
                            int i7 = i6;
                            i6--;
                            if (i7 == i2) {
                                break;
                            }
                        }
                        cArr[i6 + 1] = c2;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (cArr[i2] < cArr[i2 - 1]) {
                        while (true) {
                            int i8 = i2;
                            int i9 = i2 + 1;
                            if (i9 > i3) {
                                break;
                            }
                            char c3 = cArr[i8];
                            char c4 = cArr[i9];
                            if (c3 < c4) {
                                c4 = c3;
                                c3 = cArr[i9];
                            }
                            while (true) {
                                i8--;
                                if (c3 >= cArr[i8]) {
                                    break;
                                } else {
                                    cArr[i8 + 2] = cArr[i8];
                                }
                            }
                            int i10 = i8 + 1;
                            cArr[i10 + 1] = c3;
                            while (true) {
                                i10--;
                                if (c4 < cArr[i10]) {
                                    cArr[i10 + 1] = cArr[i10];
                                }
                            }
                            cArr[i10 + 1] = c4;
                            i2 = i9 + 1;
                        }
                        char c5 = cArr[i3];
                        while (true) {
                            i3--;
                            if (c5 < cArr[i3]) {
                                cArr[i3 + 1] = cArr[i3];
                            } else {
                                cArr[i3 + 1] = c5;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i11 = (i4 >> 3) + (i4 >> 6) + 1;
            int i12 = (i2 + i3) >>> 1;
            int i13 = i12 - i11;
            int i14 = i13 - i11;
            int i15 = i12 + i11;
            int i16 = i15 + i11;
            if (cArr[i13] < cArr[i14]) {
                char c6 = cArr[i13];
                cArr[i13] = cArr[i14];
                cArr[i14] = c6;
            }
            if (cArr[i12] < cArr[i13]) {
                char c7 = cArr[i12];
                cArr[i12] = cArr[i13];
                cArr[i13] = c7;
                if (c7 < cArr[i14]) {
                    cArr[i13] = cArr[i14];
                    cArr[i14] = c7;
                }
            }
            if (cArr[i15] < cArr[i12]) {
                char c8 = cArr[i15];
                cArr[i15] = cArr[i12];
                cArr[i12] = c8;
                if (c8 < cArr[i13]) {
                    cArr[i12] = cArr[i13];
                    cArr[i13] = c8;
                    if (c8 < cArr[i14]) {
                        cArr[i13] = cArr[i14];
                        cArr[i14] = c8;
                    }
                }
            }
            if (cArr[i16] < cArr[i15]) {
                char c9 = cArr[i16];
                cArr[i16] = cArr[i15];
                cArr[i15] = c9;
                if (c9 < cArr[i12]) {
                    cArr[i15] = cArr[i12];
                    cArr[i12] = c9;
                    if (c9 < cArr[i13]) {
                        cArr[i12] = cArr[i13];
                        cArr[i13] = c9;
                        if (c9 < cArr[i14]) {
                            cArr[i13] = cArr[i14];
                            cArr[i14] = c9;
                        }
                    }
                }
            }
            int i17 = i2;
            int i18 = i3;
            if (cArr[i14] != cArr[i13] && cArr[i13] != cArr[i12] && cArr[i12] != cArr[i15] && cArr[i15] != cArr[i16]) {
                char c10 = cArr[i13];
                char c11 = cArr[i15];
                cArr[i13] = cArr[i2];
                cArr[i15] = cArr[i3];
                do {
                    i17++;
                } while (cArr[i17] < c10);
                do {
                    i18--;
                } while (cArr[i18] > c11);
                int i19 = i17 - 1;
                loop9: while (true) {
                    i19++;
                    if (i19 > i18) {
                        break;
                    }
                    char c12 = cArr[i19];
                    if (c12 < c10) {
                        cArr[i19] = cArr[i17];
                        cArr[i17] = c12;
                        i17++;
                    } else if (c12 > c11) {
                        while (cArr[i18] > c11) {
                            int i20 = i18;
                            i18--;
                            if (i20 == i19) {
                                break loop9;
                            }
                        }
                        if (cArr[i18] < c10) {
                            cArr[i19] = cArr[i17];
                            cArr[i17] = cArr[i18];
                            i17++;
                        } else {
                            cArr[i19] = cArr[i18];
                        }
                        cArr[i18] = c12;
                        i18--;
                    } else {
                        continue;
                    }
                }
                cArr[i2] = cArr[i17 - 1];
                cArr[i17 - 1] = c10;
                cArr[i3] = cArr[i18 + 1];
                cArr[i18 + 1] = c11;
                sort(cArr, i2, i17 - 2, z2);
                sort(cArr, i18 + 2, i3, false);
                if (i17 < i14 && i16 < i18) {
                    while (cArr[i17] == c10) {
                        i17++;
                    }
                    while (cArr[i18] == c11) {
                        i18--;
                    }
                    int i21 = i17 - 1;
                    loop13: while (true) {
                        i21++;
                        if (i21 > i18) {
                            break;
                        }
                        char c13 = cArr[i21];
                        if (c13 == c10) {
                            cArr[i21] = cArr[i17];
                            cArr[i17] = c13;
                            i17++;
                        } else if (c13 == c11) {
                            while (cArr[i18] == c11) {
                                int i22 = i18;
                                i18--;
                                if (i22 == i21) {
                                    break loop13;
                                }
                            }
                            if (cArr[i18] == c10) {
                                cArr[i21] = cArr[i17];
                                cArr[i17] = c10;
                                i17++;
                            } else {
                                cArr[i21] = cArr[i18];
                            }
                            cArr[i18] = c13;
                            i18--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(cArr, i17, i18, false);
                return;
            }
            char c14 = cArr[i12];
            for (int i23 = i17; i23 <= i18; i23++) {
                if (cArr[i23] != c14) {
                    char c15 = cArr[i23];
                    if (c15 < c14) {
                        cArr[i23] = cArr[i17];
                        cArr[i17] = c15;
                        i17++;
                    } else {
                        while (cArr[i18] > c14) {
                            i18--;
                        }
                        if (cArr[i18] < c14) {
                            cArr[i23] = cArr[i17];
                            cArr[i17] = cArr[i18];
                            i17++;
                        } else {
                            cArr[i23] = c14;
                        }
                        cArr[i18] = c15;
                        i18--;
                    }
                }
            }
            sort(cArr, i2, i17 - 1, z2);
            sort(cArr, i18 + 1, i3, false);
        }
    }

    static void sort(byte[] bArr, int i2, int i3) {
        if (i3 - i2 > 29) {
            int[] iArr = new int[256];
            int i4 = i2 - 1;
            while (true) {
                i4++;
                if (i4 > i3) {
                    break;
                }
                int i5 = bArr[i4] - Byte.MIN_VALUE;
                iArr[i5] = iArr[i5] + 1;
            }
            int i6 = 256;
            int i7 = i3 + 1;
            while (i7 > i2) {
                do {
                    i6--;
                } while (iArr[i6] == 0);
                byte b2 = (byte) (i6 - 128);
                int i8 = iArr[i6];
                do {
                    i7--;
                    bArr[i7] = b2;
                    i8--;
                } while (i8 > 0);
            }
            return;
        }
        int i9 = i2;
        while (true) {
            int i10 = i9;
            if (i9 < i3) {
                byte b3 = bArr[i9 + 1];
                while (b3 < bArr[i10]) {
                    bArr[i10 + 1] = bArr[i10];
                    int i11 = i10;
                    i10--;
                    if (i11 == i2) {
                        break;
                    }
                }
                bArr[i10 + 1] = b3;
                i9++;
            } else {
                return;
            }
        }
    }

    static void sort(float[] fArr, int i2, int i3, float[] fArr2, int i4, int i5) {
        while (i2 <= i3 && Float.isNaN(fArr[i3])) {
            i3--;
        }
        int i6 = i3;
        while (true) {
            i6--;
            if (i6 < i2) {
                break;
            }
            float f2 = fArr[i6];
            if (f2 != f2) {
                fArr[i6] = fArr[i3];
                fArr[i3] = f2;
                i3--;
            }
        }
        doSort(fArr, i2, i3, fArr2, i4, i5);
        int i7 = i3;
        while (i2 < i7) {
            int i8 = (i2 + i7) >>> 1;
            if (fArr[i8] < 0.0f) {
                i2 = i8 + 1;
            } else {
                i7 = i8;
            }
        }
        while (i2 <= i3 && Float.floatToRawIntBits(fArr[i2]) < 0) {
            i2++;
        }
        int i9 = i2;
        int i10 = i2 - 1;
        while (true) {
            i9++;
            if (i9 <= i3) {
                float f3 = fArr[i9];
                if (f3 == 0.0f) {
                    if (Float.floatToRawIntBits(f3) < 0) {
                        fArr[i9] = 0.0f;
                        i10++;
                        fArr[i10] = -0.0f;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static void doSort(float[] fArr, int i2, int i3, float[] fArr2, int i4, int i5) {
        float[] fArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(fArr, i2, i3, true);
            return;
        }
        int[] iArr = new int[68];
        int i8 = 0;
        iArr[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (fArr[i9] < fArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (fArr[i9 - 1] <= fArr[i9]);
            } else if (fArr[i9] > fArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (fArr[i9 - 1] >= fArr[i9]);
                int i10 = iArr[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    float f2 = fArr[i10];
                    fArr[i10] = fArr[i11];
                    fArr[i11] = f2;
                }
            } else {
                int i12 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && fArr[i9 - 1] == fArr[i9]) {
                        i12--;
                    }
                } while (i12 != 0);
                sort(fArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr[i8] = i9;
            } else {
                sort(fArr, i2, i3, true);
                return;
            }
        }
        int i13 = i3 + 1;
        if (iArr[i8] == i3) {
            i8++;
            iArr[i8] = i13;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i14 = 1;
        while (true) {
            int i15 = i14 << 1;
            i14 = i15;
            if (i15 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i16 = i13 - i2;
        if (fArr2 == null || i5 < i16 || i4 + i16 > fArr2.length) {
            fArr2 = new float[i16];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(fArr, i2, fArr2, i4, i16);
            fArr3 = fArr;
            i7 = 0;
            fArr = fArr2;
            i6 = i4 - i2;
        } else {
            fArr3 = fArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i17 = 0;
            for (int i18 = 0 + 2; i18 <= i8; i18 += 2) {
                int i19 = iArr[i18];
                int i20 = iArr[i18 - 1];
                int i21 = iArr[i18 - 2];
                int i22 = i21;
                int i23 = i20;
                while (i21 < i19) {
                    if (i23 >= i19 || (i22 < i20 && fArr[i22 + i6] <= fArr[i23 + i6])) {
                        int i24 = i22;
                        i22++;
                        fArr3[i21 + i7] = fArr[i24 + i6];
                    } else {
                        int i25 = i23;
                        i23++;
                        fArr3[i21 + i7] = fArr[i25 + i6];
                    }
                    i21++;
                }
                i17++;
                iArr[i17] = i19;
            }
            if ((i8 & 1) != 0) {
                int i26 = i13;
                int i27 = iArr[i8 - 1];
                while (true) {
                    i26--;
                    if (i26 < i27) {
                        break;
                    } else {
                        fArr3[i26 + i7] = fArr[i26 + i6];
                    }
                }
                i17++;
                iArr[i17] = i13;
            }
            float[] fArr4 = fArr;
            fArr = fArr3;
            fArr3 = fArr4;
            int i28 = i6;
            i6 = i7;
            i7 = i28;
            i8 = i17;
        }
    }

    private static void sort(float[] fArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        float f2 = fArr[i5 + 1];
                        while (f2 < fArr[i6]) {
                            fArr[i6 + 1] = fArr[i6];
                            int i7 = i6;
                            i6--;
                            if (i7 == i2) {
                                break;
                            }
                        }
                        fArr[i6 + 1] = f2;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (fArr[i2] < fArr[i2 - 1]) {
                        while (true) {
                            int i8 = i2;
                            int i9 = i2 + 1;
                            if (i9 > i3) {
                                break;
                            }
                            float f3 = fArr[i8];
                            float f4 = fArr[i9];
                            if (f3 < f4) {
                                f4 = f3;
                                f3 = fArr[i9];
                            }
                            while (true) {
                                i8--;
                                if (f3 >= fArr[i8]) {
                                    break;
                                } else {
                                    fArr[i8 + 2] = fArr[i8];
                                }
                            }
                            int i10 = i8 + 1;
                            fArr[i10 + 1] = f3;
                            while (true) {
                                i10--;
                                if (f4 < fArr[i10]) {
                                    fArr[i10 + 1] = fArr[i10];
                                }
                            }
                            fArr[i10 + 1] = f4;
                            i2 = i9 + 1;
                        }
                        float f5 = fArr[i3];
                        while (true) {
                            i3--;
                            if (f5 < fArr[i3]) {
                                fArr[i3 + 1] = fArr[i3];
                            } else {
                                fArr[i3 + 1] = f5;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i11 = (i4 >> 3) + (i4 >> 6) + 1;
            int i12 = (i2 + i3) >>> 1;
            int i13 = i12 - i11;
            int i14 = i13 - i11;
            int i15 = i12 + i11;
            int i16 = i15 + i11;
            if (fArr[i13] < fArr[i14]) {
                float f6 = fArr[i13];
                fArr[i13] = fArr[i14];
                fArr[i14] = f6;
            }
            if (fArr[i12] < fArr[i13]) {
                float f7 = fArr[i12];
                fArr[i12] = fArr[i13];
                fArr[i13] = f7;
                if (f7 < fArr[i14]) {
                    fArr[i13] = fArr[i14];
                    fArr[i14] = f7;
                }
            }
            if (fArr[i15] < fArr[i12]) {
                float f8 = fArr[i15];
                fArr[i15] = fArr[i12];
                fArr[i12] = f8;
                if (f8 < fArr[i13]) {
                    fArr[i12] = fArr[i13];
                    fArr[i13] = f8;
                    if (f8 < fArr[i14]) {
                        fArr[i13] = fArr[i14];
                        fArr[i14] = f8;
                    }
                }
            }
            if (fArr[i16] < fArr[i15]) {
                float f9 = fArr[i16];
                fArr[i16] = fArr[i15];
                fArr[i15] = f9;
                if (f9 < fArr[i12]) {
                    fArr[i15] = fArr[i12];
                    fArr[i12] = f9;
                    if (f9 < fArr[i13]) {
                        fArr[i12] = fArr[i13];
                        fArr[i13] = f9;
                        if (f9 < fArr[i14]) {
                            fArr[i13] = fArr[i14];
                            fArr[i14] = f9;
                        }
                    }
                }
            }
            int i17 = i2;
            int i18 = i3;
            if (fArr[i14] != fArr[i13] && fArr[i13] != fArr[i12] && fArr[i12] != fArr[i15] && fArr[i15] != fArr[i16]) {
                float f10 = fArr[i13];
                float f11 = fArr[i15];
                fArr[i13] = fArr[i2];
                fArr[i15] = fArr[i3];
                do {
                    i17++;
                } while (fArr[i17] < f10);
                do {
                    i18--;
                } while (fArr[i18] > f11);
                int i19 = i17 - 1;
                loop9: while (true) {
                    i19++;
                    if (i19 > i18) {
                        break;
                    }
                    float f12 = fArr[i19];
                    if (f12 < f10) {
                        fArr[i19] = fArr[i17];
                        fArr[i17] = f12;
                        i17++;
                    } else if (f12 > f11) {
                        while (fArr[i18] > f11) {
                            int i20 = i18;
                            i18--;
                            if (i20 == i19) {
                                break loop9;
                            }
                        }
                        if (fArr[i18] < f10) {
                            fArr[i19] = fArr[i17];
                            fArr[i17] = fArr[i18];
                            i17++;
                        } else {
                            fArr[i19] = fArr[i18];
                        }
                        fArr[i18] = f12;
                        i18--;
                    } else {
                        continue;
                    }
                }
                fArr[i2] = fArr[i17 - 1];
                fArr[i17 - 1] = f10;
                fArr[i3] = fArr[i18 + 1];
                fArr[i18 + 1] = f11;
                sort(fArr, i2, i17 - 2, z2);
                sort(fArr, i18 + 2, i3, false);
                if (i17 < i14 && i16 < i18) {
                    while (fArr[i17] == f10) {
                        i17++;
                    }
                    while (fArr[i18] == f11) {
                        i18--;
                    }
                    int i21 = i17 - 1;
                    loop13: while (true) {
                        i21++;
                        if (i21 > i18) {
                            break;
                        }
                        float f13 = fArr[i21];
                        if (f13 == f10) {
                            fArr[i21] = fArr[i17];
                            fArr[i17] = f13;
                            i17++;
                        } else if (f13 == f11) {
                            while (fArr[i18] == f11) {
                                int i22 = i18;
                                i18--;
                                if (i22 == i21) {
                                    break loop13;
                                }
                            }
                            if (fArr[i18] == f10) {
                                fArr[i21] = fArr[i17];
                                fArr[i17] = fArr[i18];
                                i17++;
                            } else {
                                fArr[i21] = fArr[i18];
                            }
                            fArr[i18] = f13;
                            i18--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(fArr, i17, i18, false);
                return;
            }
            float f14 = fArr[i12];
            for (int i23 = i17; i23 <= i18; i23++) {
                if (fArr[i23] != f14) {
                    float f15 = fArr[i23];
                    if (f15 < f14) {
                        fArr[i23] = fArr[i17];
                        fArr[i17] = f15;
                        i17++;
                    } else {
                        while (fArr[i18] > f14) {
                            i18--;
                        }
                        if (fArr[i18] < f14) {
                            fArr[i23] = fArr[i17];
                            fArr[i17] = fArr[i18];
                            i17++;
                        } else {
                            fArr[i23] = fArr[i18];
                        }
                        fArr[i18] = f15;
                        i18--;
                    }
                }
            }
            sort(fArr, i2, i17 - 1, z2);
            sort(fArr, i18 + 1, i3, false);
        }
    }

    static void sort(double[] dArr, int i2, int i3, double[] dArr2, int i4, int i5) {
        while (i2 <= i3 && Double.isNaN(dArr[i3])) {
            i3--;
        }
        int i6 = i3;
        while (true) {
            i6--;
            if (i6 < i2) {
                break;
            }
            double d2 = dArr[i6];
            if (d2 != d2) {
                dArr[i6] = dArr[i3];
                dArr[i3] = d2;
                i3--;
            }
        }
        doSort(dArr, i2, i3, dArr2, i4, i5);
        int i7 = i3;
        while (i2 < i7) {
            int i8 = (i2 + i7) >>> 1;
            if (dArr[i8] < 0.0d) {
                i2 = i8 + 1;
            } else {
                i7 = i8;
            }
        }
        while (i2 <= i3 && Double.doubleToRawLongBits(dArr[i2]) < 0) {
            i2++;
        }
        int i9 = i2;
        int i10 = i2 - 1;
        while (true) {
            i9++;
            if (i9 <= i3) {
                double d3 = dArr[i9];
                if (d3 != 0.0d) {
                    return;
                }
                if (Double.doubleToRawLongBits(d3) < 0) {
                    dArr[i9] = 0.0d;
                    i10++;
                    dArr[i10] = -0.0d;
                }
            } else {
                return;
            }
        }
    }

    private static void doSort(double[] dArr, int i2, int i3, double[] dArr2, int i4, int i5) {
        double[] dArr3;
        int i6;
        int i7;
        if (i3 - i2 < QUICKSORT_THRESHOLD) {
            sort(dArr, i2, i3, true);
            return;
        }
        int[] iArr = new int[68];
        int i8 = 0;
        iArr[0] = i2;
        int i9 = i2;
        while (i9 < i3) {
            if (dArr[i9] < dArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (dArr[i9 - 1] <= dArr[i9]);
            } else if (dArr[i9] > dArr[i9 + 1]) {
                do {
                    i9++;
                    if (i9 > i3) {
                        break;
                    }
                } while (dArr[i9 - 1] >= dArr[i9]);
                int i10 = iArr[i8] - 1;
                int i11 = i9;
                while (true) {
                    i10++;
                    i11--;
                    if (i10 >= i11) {
                        break;
                    }
                    double d2 = dArr[i10];
                    dArr[i10] = dArr[i11];
                    dArr[i11] = d2;
                }
            } else {
                int i12 = 33;
                do {
                    i9++;
                    if (i9 <= i3 && dArr[i9 - 1] == dArr[i9]) {
                        i12--;
                    }
                } while (i12 != 0);
                sort(dArr, i2, i3, true);
                return;
            }
            i8++;
            if (i8 != 67) {
                iArr[i8] = i9;
            } else {
                sort(dArr, i2, i3, true);
                return;
            }
        }
        int i13 = i3 + 1;
        if (iArr[i8] == i3) {
            i8++;
            iArr[i8] = i13;
        } else if (i8 == 1) {
            return;
        }
        byte b2 = 0;
        int i14 = 1;
        while (true) {
            int i15 = i14 << 1;
            i14 = i15;
            if (i15 >= i8) {
                break;
            } else {
                b2 = (byte) (b2 ^ 1);
            }
        }
        int i16 = i13 - i2;
        if (dArr2 == null || i5 < i16 || i4 + i16 > dArr2.length) {
            dArr2 = new double[i16];
            i4 = 0;
        }
        if (b2 == 0) {
            System.arraycopy(dArr, i2, dArr2, i4, i16);
            dArr3 = dArr;
            i7 = 0;
            dArr = dArr2;
            i6 = i4 - i2;
        } else {
            dArr3 = dArr2;
            i6 = 0;
            i7 = i4 - i2;
        }
        while (i8 > 1) {
            int i17 = 0;
            for (int i18 = 0 + 2; i18 <= i8; i18 += 2) {
                int i19 = iArr[i18];
                int i20 = iArr[i18 - 1];
                int i21 = iArr[i18 - 2];
                int i22 = i21;
                int i23 = i20;
                while (i21 < i19) {
                    if (i23 >= i19 || (i22 < i20 && dArr[i22 + i6] <= dArr[i23 + i6])) {
                        int i24 = i22;
                        i22++;
                        dArr3[i21 + i7] = dArr[i24 + i6];
                    } else {
                        int i25 = i23;
                        i23++;
                        dArr3[i21 + i7] = dArr[i25 + i6];
                    }
                    i21++;
                }
                i17++;
                iArr[i17] = i19;
            }
            if ((i8 & 1) != 0) {
                int i26 = i13;
                int i27 = iArr[i8 - 1];
                while (true) {
                    i26--;
                    if (i26 < i27) {
                        break;
                    } else {
                        dArr3[i26 + i7] = dArr[i26 + i6];
                    }
                }
                i17++;
                iArr[i17] = i13;
            }
            double[] dArr4 = dArr;
            dArr = dArr3;
            dArr3 = dArr4;
            int i28 = i6;
            i6 = i7;
            i7 = i28;
            i8 = i17;
        }
    }

    private static void sort(double[] dArr, int i2, int i3, boolean z2) {
        int i4 = (i3 - i2) + 1;
        if (i4 < 47) {
            if (z2) {
                int i5 = i2;
                while (true) {
                    int i6 = i5;
                    if (i5 < i3) {
                        double d2 = dArr[i5 + 1];
                        while (d2 < dArr[i6]) {
                            dArr[i6 + 1] = dArr[i6];
                            int i7 = i6;
                            i6--;
                            if (i7 == i2) {
                                break;
                            }
                        }
                        dArr[i6 + 1] = d2;
                        i5++;
                    } else {
                        return;
                    }
                }
            } else {
                while (i2 < i3) {
                    i2++;
                    if (dArr[i2] < dArr[i2 - 1]) {
                        while (true) {
                            int i8 = i2;
                            int i9 = i2 + 1;
                            if (i9 > i3) {
                                break;
                            }
                            double d3 = dArr[i8];
                            double d4 = dArr[i9];
                            if (d3 < d4) {
                                d4 = d3;
                                d3 = dArr[i9];
                            }
                            while (true) {
                                i8--;
                                if (d3 >= dArr[i8]) {
                                    break;
                                } else {
                                    dArr[i8 + 2] = dArr[i8];
                                }
                            }
                            int i10 = i8 + 1;
                            dArr[i10 + 1] = d3;
                            while (true) {
                                i10--;
                                if (d4 < dArr[i10]) {
                                    dArr[i10 + 1] = dArr[i10];
                                }
                            }
                            dArr[i10 + 1] = d4;
                            i2 = i9 + 1;
                        }
                        double d5 = dArr[i3];
                        while (true) {
                            i3--;
                            if (d5 < dArr[i3]) {
                                dArr[i3 + 1] = dArr[i3];
                            } else {
                                dArr[i3 + 1] = d5;
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            int i11 = (i4 >> 3) + (i4 >> 6) + 1;
            int i12 = (i2 + i3) >>> 1;
            int i13 = i12 - i11;
            int i14 = i13 - i11;
            int i15 = i12 + i11;
            int i16 = i15 + i11;
            if (dArr[i13] < dArr[i14]) {
                double d6 = dArr[i13];
                dArr[i13] = dArr[i14];
                dArr[i14] = d6;
            }
            if (dArr[i12] < dArr[i13]) {
                double d7 = dArr[i12];
                dArr[i12] = dArr[i13];
                dArr[i13] = d7;
                if (d7 < dArr[i14]) {
                    dArr[i13] = dArr[i14];
                    dArr[i14] = d7;
                }
            }
            if (dArr[i15] < dArr[i12]) {
                double d8 = dArr[i15];
                dArr[i15] = dArr[i12];
                dArr[i12] = d8;
                if (d8 < dArr[i13]) {
                    dArr[i12] = dArr[i13];
                    dArr[i13] = d8;
                    if (d8 < dArr[i14]) {
                        dArr[i13] = dArr[i14];
                        dArr[i14] = d8;
                    }
                }
            }
            if (dArr[i16] < dArr[i15]) {
                double d9 = dArr[i16];
                dArr[i16] = dArr[i15];
                dArr[i15] = d9;
                if (d9 < dArr[i12]) {
                    dArr[i15] = dArr[i12];
                    dArr[i12] = d9;
                    if (d9 < dArr[i13]) {
                        dArr[i12] = dArr[i13];
                        dArr[i13] = d9;
                        if (d9 < dArr[i14]) {
                            dArr[i13] = dArr[i14];
                            dArr[i14] = d9;
                        }
                    }
                }
            }
            int i17 = i2;
            int i18 = i3;
            if (dArr[i14] != dArr[i13] && dArr[i13] != dArr[i12] && dArr[i12] != dArr[i15] && dArr[i15] != dArr[i16]) {
                double d10 = dArr[i13];
                double d11 = dArr[i15];
                dArr[i13] = dArr[i2];
                dArr[i15] = dArr[i3];
                do {
                    i17++;
                } while (dArr[i17] < d10);
                do {
                    i18--;
                } while (dArr[i18] > d11);
                int i19 = i17 - 1;
                loop9: while (true) {
                    i19++;
                    if (i19 > i18) {
                        break;
                    }
                    double d12 = dArr[i19];
                    if (d12 < d10) {
                        dArr[i19] = dArr[i17];
                        dArr[i17] = d12;
                        i17++;
                    } else if (d12 > d11) {
                        while (dArr[i18] > d11) {
                            int i20 = i18;
                            i18--;
                            if (i20 == i19) {
                                break loop9;
                            }
                        }
                        if (dArr[i18] < d10) {
                            dArr[i19] = dArr[i17];
                            dArr[i17] = dArr[i18];
                            i17++;
                        } else {
                            dArr[i19] = dArr[i18];
                        }
                        dArr[i18] = d12;
                        i18--;
                    } else {
                        continue;
                    }
                }
                dArr[i2] = dArr[i17 - 1];
                dArr[i17 - 1] = d10;
                dArr[i3] = dArr[i18 + 1];
                dArr[i18 + 1] = d11;
                sort(dArr, i2, i17 - 2, z2);
                sort(dArr, i18 + 2, i3, false);
                if (i17 < i14 && i16 < i18) {
                    while (dArr[i17] == d10) {
                        i17++;
                    }
                    while (dArr[i18] == d11) {
                        i18--;
                    }
                    int i21 = i17 - 1;
                    loop13: while (true) {
                        i21++;
                        if (i21 > i18) {
                            break;
                        }
                        double d13 = dArr[i21];
                        if (d13 == d10) {
                            dArr[i21] = dArr[i17];
                            dArr[i17] = d13;
                            i17++;
                        } else if (d13 == d11) {
                            while (dArr[i18] == d11) {
                                int i22 = i18;
                                i18--;
                                if (i22 == i21) {
                                    break loop13;
                                }
                            }
                            if (dArr[i18] == d10) {
                                dArr[i21] = dArr[i17];
                                dArr[i17] = dArr[i18];
                                i17++;
                            } else {
                                dArr[i21] = dArr[i18];
                            }
                            dArr[i18] = d13;
                            i18--;
                        } else {
                            continue;
                        }
                    }
                }
                sort(dArr, i17, i18, false);
                return;
            }
            double d14 = dArr[i12];
            for (int i23 = i17; i23 <= i18; i23++) {
                if (dArr[i23] != d14) {
                    double d15 = dArr[i23];
                    if (d15 < d14) {
                        dArr[i23] = dArr[i17];
                        dArr[i17] = d15;
                        i17++;
                    } else {
                        while (dArr[i18] > d14) {
                            i18--;
                        }
                        if (dArr[i18] < d14) {
                            dArr[i23] = dArr[i17];
                            dArr[i17] = dArr[i18];
                            i17++;
                        } else {
                            dArr[i23] = dArr[i18];
                        }
                        dArr[i18] = d15;
                        i18--;
                    }
                }
            }
            sort(dArr, i2, i17 - 1, z2);
            sort(dArr, i18 + 1, i3, false);
        }
    }
}
