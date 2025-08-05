package java.util;

import java.lang.reflect.Array;

/* loaded from: rt.jar:java/util/TimSort.class */
class TimSort<T> {
    private static final int MIN_MERGE = 32;

    /* renamed from: a, reason: collision with root package name */
    private final T[] f12565a;

    /* renamed from: c, reason: collision with root package name */
    private final Comparator<? super T> f12566c;
    private static final int MIN_GALLOP = 7;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private T[] tmp;
    private int tmpBase;
    private int tmpLen;
    private final int[] runBase;
    private final int[] runLen;
    static final /* synthetic */ boolean $assertionsDisabled;
    private int minGallop = 7;
    private int stackSize = 0;

    static {
        $assertionsDisabled = !TimSort.class.desiredAssertionStatus();
    }

    private TimSort(T[] tArr, Comparator<? super T> comparator, T[] tArr2, int i2, int i3) {
        this.f12565a = tArr;
        this.f12566c = comparator;
        int length = tArr.length;
        int i4 = length < 512 ? length >>> 1 : 256;
        if (tArr2 == null || i3 < i4 || i2 + i4 > tArr2.length) {
            this.tmp = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i4));
            this.tmpBase = 0;
            this.tmpLen = i4;
        } else {
            this.tmp = tArr2;
            this.tmpBase = i2;
            this.tmpLen = i3;
        }
        int i5 = length < 120 ? 5 : length < 1542 ? 10 : length < 119151 ? 24 : 49;
        this.runBase = new int[i5];
        this.runLen = new int[i5];
    }

    static <T> void sort(T[] tArr, int i2, int i3, Comparator<? super T> comparator, T[] tArr2, int i4, int i5) {
        if (!$assertionsDisabled && (comparator == null || tArr == null || i2 < 0 || i2 > i3 || i3 > tArr.length)) {
            throw new AssertionError();
        }
        int i6 = i3 - i2;
        if (i6 < 2) {
            return;
        }
        if (i6 < 32) {
            binarySort(tArr, i2, i3, i2 + countRunAndMakeAscending(tArr, i2, i3, comparator), comparator);
            return;
        }
        TimSort timSort = new TimSort(tArr, comparator, tArr2, i4, i5);
        int iMinRunLength = minRunLength(i6);
        do {
            int iCountRunAndMakeAscending = countRunAndMakeAscending(tArr, i2, i3, comparator);
            if (iCountRunAndMakeAscending < iMinRunLength) {
                int i7 = i6 <= iMinRunLength ? i6 : iMinRunLength;
                binarySort(tArr, i2, i2 + i7, i2 + iCountRunAndMakeAscending, comparator);
                iCountRunAndMakeAscending = i7;
            }
            timSort.pushRun(i2, iCountRunAndMakeAscending);
            timSort.mergeCollapse();
            i2 += iCountRunAndMakeAscending;
            i6 -= iCountRunAndMakeAscending;
        } while (i6 != 0);
        if (!$assertionsDisabled && i2 != i3) {
            throw new AssertionError();
        }
        timSort.mergeForceCollapse();
        if (!$assertionsDisabled && timSort.stackSize != 1) {
            throw new AssertionError();
        }
    }

    private static <T> void binarySort(T[] tArr, int i2, int i3, int i4, Comparator<? super T> comparator) {
        if (!$assertionsDisabled && (i2 > i4 || i4 > i3)) {
            throw new AssertionError();
        }
        if (i4 == i2) {
            i4++;
        }
        while (i4 < i3) {
            T t2 = tArr[i4];
            int i5 = i2;
            int i6 = i4;
            if (!$assertionsDisabled && i5 > i6) {
                throw new AssertionError();
            }
            while (i5 < i6) {
                int i7 = (i5 + i6) >>> 1;
                if (comparator.compare(t2, tArr[i7]) < 0) {
                    i6 = i7;
                } else {
                    i5 = i7 + 1;
                }
            }
            if (!$assertionsDisabled && i5 != i6) {
                throw new AssertionError();
            }
            int i8 = i4 - i5;
            switch (i8) {
                case 1:
                    break;
                case 2:
                    tArr[i5 + 2] = tArr[i5 + 1];
                    break;
                default:
                    System.arraycopy(tArr, i5, tArr, i5 + 1, i8);
                    continue;
                    tArr[i5] = t2;
                    i4++;
            }
            tArr[i5 + 1] = tArr[i5];
            tArr[i5] = t2;
            i4++;
        }
    }

    private static <T> int countRunAndMakeAscending(T[] tArr, int i2, int i3, Comparator<? super T> comparator) {
        if (!$assertionsDisabled && i2 >= i3) {
            throw new AssertionError();
        }
        int i4 = i2 + 1;
        if (i4 == i3) {
            return 1;
        }
        int i5 = i4 + 1;
        if (comparator.compare(tArr[i4], tArr[i2]) < 0) {
            while (i5 < i3 && comparator.compare(tArr[i5], tArr[i5 - 1]) < 0) {
                i5++;
            }
            reverseRange(tArr, i2, i5);
        } else {
            while (i5 < i3 && comparator.compare(tArr[i5], tArr[i5 - 1]) >= 0) {
                i5++;
            }
        }
        return i5 - i2;
    }

    private static void reverseRange(Object[] objArr, int i2, int i3) {
        int i4 = i3 - 1;
        while (i2 < i4) {
            Object obj = objArr[i2];
            int i5 = i2;
            i2++;
            objArr[i5] = objArr[i4];
            int i6 = i4;
            i4--;
            objArr[i6] = obj;
        }
    }

    private static int minRunLength(int i2) {
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError();
        }
        int i3 = 0;
        while (i2 >= 32) {
            i3 |= i2 & 1;
            i2 >>= 1;
        }
        return i2 + i3;
    }

    private void pushRun(int i2, int i3) {
        this.runBase[this.stackSize] = i2;
        this.runLen[this.stackSize] = i3;
        this.stackSize++;
    }

    private void mergeCollapse() {
        while (this.stackSize > 1) {
            int i2 = this.stackSize - 2;
            if (i2 > 0 && this.runLen[i2 - 1] <= this.runLen[i2] + this.runLen[i2 + 1]) {
                if (this.runLen[i2 - 1] < this.runLen[i2 + 1]) {
                    i2--;
                }
                mergeAt(i2);
            } else if (this.runLen[i2] <= this.runLen[i2 + 1]) {
                mergeAt(i2);
            } else {
                return;
            }
        }
    }

    private void mergeForceCollapse() {
        while (this.stackSize > 1) {
            int i2 = this.stackSize - 2;
            if (i2 > 0 && this.runLen[i2 - 1] < this.runLen[i2 + 1]) {
                i2--;
            }
            mergeAt(i2);
        }
    }

    private void mergeAt(int i2) {
        if (!$assertionsDisabled && this.stackSize < 2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 != this.stackSize - 2 && i2 != this.stackSize - 3) {
            throw new AssertionError();
        }
        int i3 = this.runBase[i2];
        int i4 = this.runLen[i2];
        int i5 = this.runBase[i2 + 1];
        int i6 = this.runLen[i2 + 1];
        if (!$assertionsDisabled && (i4 <= 0 || i6 <= 0)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i3 + i4 != i5) {
            throw new AssertionError();
        }
        this.runLen[i2] = i4 + i6;
        if (i2 == this.stackSize - 3) {
            this.runBase[i2 + 1] = this.runBase[i2 + 2];
            this.runLen[i2 + 1] = this.runLen[i2 + 2];
        }
        this.stackSize--;
        int iGallopRight = gallopRight(this.f12565a[i5], this.f12565a, i3, i4, 0, this.f12566c);
        if (!$assertionsDisabled && iGallopRight < 0) {
            throw new AssertionError();
        }
        int i7 = i3 + iGallopRight;
        int i8 = i4 - iGallopRight;
        if (i8 == 0) {
            return;
        }
        int iGallopLeft = gallopLeft(this.f12565a[(i7 + i8) - 1], this.f12565a, i5, i6, i6 - 1, this.f12566c);
        if (!$assertionsDisabled && iGallopLeft < 0) {
            throw new AssertionError();
        }
        if (iGallopLeft == 0) {
            return;
        }
        if (i8 <= iGallopLeft) {
            mergeLo(i7, i8, i5, iGallopLeft);
        } else {
            mergeHi(i7, i8, i5, iGallopLeft);
        }
    }

    private static <T> int gallopLeft(T t2, T[] tArr, int i2, int i3, int i4, Comparator<? super T> comparator) {
        int i5;
        int i6;
        if (!$assertionsDisabled && (i3 <= 0 || i4 < 0 || i4 >= i3)) {
            throw new AssertionError();
        }
        int i7 = 0;
        int i8 = 1;
        if (comparator.compare(t2, tArr[i2 + i4]) > 0) {
            int i9 = i3 - i4;
            while (i8 < i9 && comparator.compare(t2, tArr[i2 + i4 + i8]) > 0) {
                i7 = i8;
                i8 = (i8 << 1) + 1;
                if (i8 <= 0) {
                    i8 = i9;
                }
            }
            if (i8 > i9) {
                i8 = i9;
            }
            i5 = i7 + i4;
            i6 = i8 + i4;
        } else {
            int i10 = i4 + 1;
            while (i8 < i10 && comparator.compare(t2, tArr[(i2 + i4) - i8]) <= 0) {
                i7 = i8;
                i8 = (i8 << 1) + 1;
                if (i8 <= 0) {
                    i8 = i10;
                }
            }
            if (i8 > i10) {
                i8 = i10;
            }
            int i11 = i7;
            i5 = i4 - i8;
            i6 = i4 - i11;
        }
        if (!$assertionsDisabled && (-1 > i5 || i5 >= i6 || i6 > i3)) {
            throw new AssertionError();
        }
        int i12 = i5 + 1;
        while (i12 < i6) {
            int i13 = i12 + ((i6 - i12) >>> 1);
            if (comparator.compare(t2, tArr[i2 + i13]) > 0) {
                i12 = i13 + 1;
            } else {
                i6 = i13;
            }
        }
        if ($assertionsDisabled || i12 == i6) {
            return i6;
        }
        throw new AssertionError();
    }

    private static <T> int gallopRight(T t2, T[] tArr, int i2, int i3, int i4, Comparator<? super T> comparator) {
        int i5;
        int i6;
        if (!$assertionsDisabled && (i3 <= 0 || i4 < 0 || i4 >= i3)) {
            throw new AssertionError();
        }
        int i7 = 1;
        int i8 = 0;
        if (comparator.compare(t2, tArr[i2 + i4]) < 0) {
            int i9 = i4 + 1;
            while (i7 < i9 && comparator.compare(t2, tArr[(i2 + i4) - i7]) < 0) {
                i8 = i7;
                i7 = (i7 << 1) + 1;
                if (i7 <= 0) {
                    i7 = i9;
                }
            }
            if (i7 > i9) {
                i7 = i9;
            }
            int i10 = i8;
            i5 = i4 - i7;
            i6 = i4 - i10;
        } else {
            int i11 = i3 - i4;
            while (i7 < i11 && comparator.compare(t2, tArr[i2 + i4 + i7]) >= 0) {
                i8 = i7;
                i7 = (i7 << 1) + 1;
                if (i7 <= 0) {
                    i7 = i11;
                }
            }
            if (i7 > i11) {
                i7 = i11;
            }
            i5 = i8 + i4;
            i6 = i7 + i4;
        }
        if (!$assertionsDisabled && (-1 > i5 || i5 >= i6 || i6 > i3)) {
            throw new AssertionError();
        }
        int i12 = i5 + 1;
        while (i12 < i6) {
            int i13 = i12 + ((i6 - i12) >>> 1);
            if (comparator.compare(t2, tArr[i2 + i13]) < 0) {
                i6 = i13;
            } else {
                i12 = i13 + 1;
            }
        }
        if ($assertionsDisabled || i12 == i6) {
            return i6;
        }
        throw new AssertionError();
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b0, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0114, code lost:
    
        if (java.util.TimSort.$assertionsDisabled != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0119, code lost:
    
        if (r9 <= 1) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x011e, code lost:
    
        if (r11 > 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0128, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0129, code lost:
    
        r0 = gallopRight(r0[r15], r0, r14, r9, 0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x013d, code lost:
    
        if (r0 == 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0140, code lost:
    
        java.lang.System.arraycopy(r0, r14, r0, r16, r0);
        r16 = r16 + r0;
        r14 = r14 + r0;
        r9 = r9 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0162, code lost:
    
        if (r9 > 1) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0168, code lost:
    
        r1 = r16;
        r16 = r16 + 1;
        r3 = r15;
        r15 = r15 + 1;
        r0[r1] = r0[r3];
        r11 = r11 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x017d, code lost:
    
        if (r11 != 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0183, code lost:
    
        r0 = gallopLeft(r0[r14], r0, r15, r11, 0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0198, code lost:
    
        if (r0 == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x019b, code lost:
    
        java.lang.System.arraycopy(r0, r15, r0, r16, r0);
        r16 = r16 + r0;
        r15 = r15 + r0;
        r11 = r11 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01bf, code lost:
    
        if (r11 != 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01c5, code lost:
    
        r1 = r16;
        r16 = r16 + 1;
        r3 = r14;
        r14 = r14 + 1;
        r0[r1] = r0[r3];
        r9 = r9 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01da, code lost:
    
        if (r9 != 1) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01e0, code lost:
    
        r18 = r18 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01e7, code lost:
    
        if (r0 < 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01ea, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01ee, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01f3, code lost:
    
        if (r0 < 7) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01f6, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01fa, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01fc, code lost:
    
        if ((r0 | r1) != false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0201, code lost:
    
        if (r18 >= 0) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0204, code lost:
    
        r18 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0107 A[PHI: r9 r11 r14 r15 r16 r19 r20
  0x0107: PHI (r9v5 int) = (r9v3 int), (r9v2 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r11v5 int) = (r11v3 int), (r11v10 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r14v5 int) = (r14v3 int), (r14v2 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r15v5 int) = (r15v3 int), (r15v10 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r16v6 int) = (r16v4 int), (r16v14 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r19v3 int) = (r19v2 int), (r19v5 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]
  0x0107: PHI (r20v3 int) = (r20v2 int), (r20v5 int) binds: [B:36:0x0101, B:33:0x00e0] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void mergeLo(int r8, int r9, int r10, int r11) {
        /*
            Method dump skipped, instructions count: 659
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.mergeLo(int, int, int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00d6, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x013a, code lost:
    
        if (java.util.TimSort.$assertionsDisabled != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x013e, code lost:
    
        if (r10 <= 0) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0144, code lost:
    
        if (r12 > 1) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x014e, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x014f, code lost:
    
        r0 = r10 - gallopRight(r0[r17], r0, r9, r10, r10 - 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0166, code lost:
    
        if (r0 == 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0169, code lost:
    
        r18 = r18 - r0;
        r16 = r16 - r0;
        r10 = r10 - r0;
        java.lang.System.arraycopy(r0, r16 + 1, r0, r18 + 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x018e, code lost:
    
        if (r10 != 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0194, code lost:
    
        r1 = r18;
        r18 = r18 - 1;
        r3 = r17;
        r17 = r17 - 1;
        r0[r1] = r0[r3];
        r12 = r12 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01aa, code lost:
    
        if (r12 != 1) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01b0, code lost:
    
        r0 = r12 - gallopLeft(r0[r16], r0, r0, r12, r12 - 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01cb, code lost:
    
        if (r0 == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01ce, code lost:
    
        r18 = r18 - r0;
        r17 = r17 - r0;
        r12 = r12 - r0;
        java.lang.System.arraycopy(r0, r17 + 1, r0, r18 + 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01f7, code lost:
    
        if (r12 > 1) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01fd, code lost:
    
        r1 = r18;
        r18 = r18 - 1;
        r3 = r16;
        r16 = r16 - 1;
        r0[r1] = r0[r3];
        r10 = r10 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0211, code lost:
    
        if (r10 != 0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0217, code lost:
    
        r20 = r20 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x021e, code lost:
    
        if (r0 < 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0221, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0225, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x022a, code lost:
    
        if (r0 < 7) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x022d, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0231, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0233, code lost:
    
        if ((r0 | r1) != false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0238, code lost:
    
        if (r20 >= 0) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x023b, code lost:
    
        r20 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x012d A[PHI: r10 r12 r16 r17 r18 r21 r22
  0x012d: PHI (r10v5 int) = (r10v3 int), (r10v10 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r12v5 int) = (r12v3 int), (r12v2 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r16v6 int) = (r16v3 int), (r16v11 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r17v5 int) = (r17v3 int), (r17v2 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r18v7 int) = (r18v4 int), (r18v15 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r21v3 int) = (r21v2 int), (r21v5 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]
  0x012d: PHI (r22v3 int) = (r22v2 int), (r22v5 int) binds: [B:36:0x0127, B:33:0x0105] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void mergeHi(int r9, int r10, int r11, int r12) {
        /*
            Method dump skipped, instructions count: 732
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimSort.mergeHi(int, int, int, int):void");
    }

    private T[] ensureCapacity(int i2) {
        int iMin;
        if (this.tmpLen < i2) {
            int i3 = i2 | (i2 >> 1);
            int i4 = i3 | (i3 >> 2);
            int i5 = i4 | (i4 >> 4);
            int i6 = i5 | (i5 >> 8);
            int i7 = (i6 | (i6 >> 16)) + 1;
            if (i7 < 0) {
                iMin = i2;
            } else {
                iMin = Math.min(i7, this.f12565a.length >>> 1);
            }
            this.tmp = (T[]) ((Object[]) Array.newInstance(this.f12565a.getClass().getComponentType(), iMin));
            this.tmpLen = iMin;
            this.tmpBase = 0;
        }
        return this.tmp;
    }
}
