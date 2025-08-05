package sun.java2d.marlin;

/* loaded from: rt.jar:sun/java2d/marlin/MergeSort.class */
final class MergeSort {
    public static final int INSERTION_SORT_THRESHOLD = 14;

    static void mergeSortNoCopy(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i2, int i3) {
        if (i2 > iArr.length || i2 > iArr2.length || i2 > iArr3.length || i2 > iArr4.length) {
            throw new ArrayIndexOutOfBoundsException("bad arguments: toIndex=" + i2);
        }
        mergeSort(iArr, iArr2, iArr, iArr3, iArr2, iArr4, i3, i2);
        if (i3 == 0 || iArr3[i3 - 1] <= iArr3[i3]) {
            System.arraycopy(iArr3, 0, iArr, 0, i2);
            System.arraycopy(iArr4, 0, iArr2, 0, i2);
            return;
        }
        int i4 = 0;
        int i5 = i3;
        for (int i6 = 0; i6 < i2; i6++) {
            if (i5 >= i2 || (i4 < i3 && iArr3[i4] <= iArr3[i5])) {
                iArr[i6] = iArr3[i4];
                iArr2[i6] = iArr4[i4];
                i4++;
            } else {
                iArr[i6] = iArr3[i5];
                iArr2[i6] = iArr4[i5];
                i5++;
            }
        }
    }

    private static void mergeSort(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 <= 14) {
            iArr4[i2] = iArr[i2];
            iArr6[i2] = iArr2[i2];
            int i5 = i2 + 1;
            int i6 = i2;
            while (true) {
                int i7 = i6;
                if (i5 < i3) {
                    int i8 = iArr[i5];
                    int i9 = iArr2[i5];
                    while (iArr4[i7] > i8) {
                        iArr4[i7 + 1] = iArr4[i7];
                        iArr6[i7 + 1] = iArr6[i7];
                        int i10 = i7;
                        i7--;
                        if (i10 == i2) {
                            break;
                        }
                    }
                    iArr4[i7 + 1] = i8;
                    iArr6[i7 + 1] = i9;
                    i6 = i5;
                    i5++;
                } else {
                    return;
                }
            }
        } else {
            int i11 = (i2 + i3) >> 1;
            mergeSort(iArr, iArr2, iArr4, iArr3, iArr6, iArr5, i2, i11);
            mergeSort(iArr, iArr2, iArr4, iArr3, iArr6, iArr5, i11, i3);
            if (iArr3[i3 - 1] <= iArr3[i2]) {
                int i12 = i11 - i2;
                int i13 = i3 - i11;
                int i14 = i12 != i13 ? 1 : 0;
                System.arraycopy(iArr3, i2, iArr4, i11 + i14, i12);
                System.arraycopy(iArr3, i11, iArr4, i2, i13);
                System.arraycopy(iArr5, i2, iArr6, i11 + i14, i12);
                System.arraycopy(iArr5, i11, iArr6, i2, i13);
                return;
            }
            if (iArr3[i11 - 1] <= iArr3[i11]) {
                System.arraycopy(iArr3, i2, iArr4, i2, i4);
                System.arraycopy(iArr5, i2, iArr6, i2, i4);
                return;
            }
            int i15 = i2;
            int i16 = i11;
            for (int i17 = i2; i17 < i3; i17++) {
                if (i16 >= i3 || (i15 < i11 && iArr3[i15] <= iArr3[i16])) {
                    iArr4[i17] = iArr3[i15];
                    iArr6[i17] = iArr5[i15];
                    i15++;
                } else {
                    iArr4[i17] = iArr3[i16];
                    iArr6[i17] = iArr5[i16];
                    i16++;
                }
            }
        }
    }

    private MergeSort() {
    }
}
