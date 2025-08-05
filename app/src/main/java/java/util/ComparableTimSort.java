package java.util;

/* loaded from: rt.jar:java/util/ComparableTimSort.class */
class ComparableTimSort {
    private static final int MIN_MERGE = 32;

    /* renamed from: a, reason: collision with root package name */
    private final Object[] f12549a;
    private static final int MIN_GALLOP = 7;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
    private Object[] tmp;
    private int tmpBase;
    private int tmpLen;
    private final int[] runBase;
    private final int[] runLen;
    static final /* synthetic */ boolean $assertionsDisabled;
    private int minGallop = 7;
    private int stackSize = 0;

    static {
        $assertionsDisabled = !ComparableTimSort.class.desiredAssertionStatus();
    }

    private ComparableTimSort(Object[] objArr, Object[] objArr2, int i2, int i3) {
        this.f12549a = objArr;
        int length = objArr.length;
        int i4 = length < 512 ? length >>> 1 : 256;
        if (objArr2 == null || i3 < i4 || i2 + i4 > objArr2.length) {
            this.tmp = new Object[i4];
            this.tmpBase = 0;
            this.tmpLen = i4;
        } else {
            this.tmp = objArr2;
            this.tmpBase = i2;
            this.tmpLen = i3;
        }
        int i5 = length < 120 ? 5 : length < 1542 ? 10 : length < 119151 ? 24 : 49;
        this.runBase = new int[i5];
        this.runLen = new int[i5];
    }

    static void sort(Object[] objArr, int i2, int i3, Object[] objArr2, int i4, int i5) {
        if (!$assertionsDisabled && (objArr == null || i2 < 0 || i2 > i3 || i3 > objArr.length)) {
            throw new AssertionError();
        }
        int i6 = i3 - i2;
        if (i6 < 2) {
            return;
        }
        if (i6 < 32) {
            binarySort(objArr, i2, i3, i2 + countRunAndMakeAscending(objArr, i2, i3));
            return;
        }
        ComparableTimSort comparableTimSort = new ComparableTimSort(objArr, objArr2, i4, i5);
        int iMinRunLength = minRunLength(i6);
        do {
            int iCountRunAndMakeAscending = countRunAndMakeAscending(objArr, i2, i3);
            if (iCountRunAndMakeAscending < iMinRunLength) {
                int i7 = i6 <= iMinRunLength ? i6 : iMinRunLength;
                binarySort(objArr, i2, i2 + i7, i2 + iCountRunAndMakeAscending);
                iCountRunAndMakeAscending = i7;
            }
            comparableTimSort.pushRun(i2, iCountRunAndMakeAscending);
            comparableTimSort.mergeCollapse();
            i2 += iCountRunAndMakeAscending;
            i6 -= iCountRunAndMakeAscending;
        } while (i6 != 0);
        if (!$assertionsDisabled && i2 != i3) {
            throw new AssertionError();
        }
        comparableTimSort.mergeForceCollapse();
        if (!$assertionsDisabled && comparableTimSort.stackSize != 1) {
            throw new AssertionError();
        }
    }

    private static void binarySort(Object[] objArr, int i2, int i3, int i4) {
        if (!$assertionsDisabled && (i2 > i4 || i4 > i3)) {
            throw new AssertionError();
        }
        if (i4 == i2) {
            i4++;
        }
        while (i4 < i3) {
            Comparable comparable = (Comparable) objArr[i4];
            int i5 = i2;
            int i6 = i4;
            if (!$assertionsDisabled && i5 > i6) {
                throw new AssertionError();
            }
            while (i5 < i6) {
                int i7 = (i5 + i6) >>> 1;
                if (comparable.compareTo(objArr[i7]) < 0) {
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
                    objArr[i5 + 2] = objArr[i5 + 1];
                    break;
                default:
                    System.arraycopy(objArr, i5, objArr, i5 + 1, i8);
                    continue;
                    objArr[i5] = comparable;
                    i4++;
            }
            objArr[i5 + 1] = objArr[i5];
            objArr[i5] = comparable;
            i4++;
        }
    }

    private static int countRunAndMakeAscending(Object[] objArr, int i2, int i3) {
        if (!$assertionsDisabled && i2 >= i3) {
            throw new AssertionError();
        }
        int i4 = i2 + 1;
        if (i4 == i3) {
            return 1;
        }
        int i5 = i4 + 1;
        if (((Comparable) objArr[i4]).compareTo(objArr[i2]) < 0) {
            while (i5 < i3 && ((Comparable) objArr[i5]).compareTo(objArr[i5 - 1]) < 0) {
                i5++;
            }
            reverseRange(objArr, i2, i5);
        } else {
            while (i5 < i3 && ((Comparable) objArr[i5]).compareTo(objArr[i5 - 1]) >= 0) {
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
        int iGallopRight = gallopRight((Comparable) this.f12549a[i5], this.f12549a, i3, i4, 0);
        if (!$assertionsDisabled && iGallopRight < 0) {
            throw new AssertionError();
        }
        int i7 = i3 + iGallopRight;
        int i8 = i4 - iGallopRight;
        if (i8 == 0) {
            return;
        }
        int iGallopLeft = gallopLeft((Comparable) this.f12549a[(i7 + i8) - 1], this.f12549a, i5, i6, i6 - 1);
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

    private static int gallopLeft(Comparable<Object> comparable, Object[] objArr, int i2, int i3, int i4) {
        int i5;
        int i6;
        if (!$assertionsDisabled && (i3 <= 0 || i4 < 0 || i4 >= i3)) {
            throw new AssertionError();
        }
        int i7 = 0;
        int i8 = 1;
        if (comparable.compareTo(objArr[i2 + i4]) > 0) {
            int i9 = i3 - i4;
            while (i8 < i9 && comparable.compareTo(objArr[i2 + i4 + i8]) > 0) {
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
            while (i8 < i10 && comparable.compareTo(objArr[(i2 + i4) - i8]) <= 0) {
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
            if (comparable.compareTo(objArr[i2 + i13]) > 0) {
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

    private static int gallopRight(Comparable<Object> comparable, Object[] objArr, int i2, int i3, int i4) {
        int i5;
        int i6;
        if (!$assertionsDisabled && (i3 <= 0 || i4 < 0 || i4 >= i3)) {
            throw new AssertionError();
        }
        int i7 = 1;
        int i8 = 0;
        if (comparable.compareTo(objArr[i2 + i4]) < 0) {
            int i9 = i4 + 1;
            while (i7 < i9 && comparable.compareTo(objArr[(i2 + i4) - i7]) < 0) {
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
            while (i7 < i11 && comparable.compareTo(objArr[i2 + i4 + i7]) >= 0) {
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
            if (comparable.compareTo(objArr[i2 + i13]) < 0) {
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

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00aa, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x010f, code lost:
    
        if (java.util.ComparableTimSort.$assertionsDisabled != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0114, code lost:
    
        if (r8 <= 1) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0119, code lost:
    
        if (r10 > 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0123, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0124, code lost:
    
        r0 = gallopRight((java.lang.Comparable) r0[r14], r0, r13, r8, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0139, code lost:
    
        if (r0 == 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x013c, code lost:
    
        java.lang.System.arraycopy(r0, r13, r0, r15, r0);
        r15 = r15 + r0;
        r13 = r13 + r0;
        r8 = r8 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x015e, code lost:
    
        if (r8 > 1) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0164, code lost:
    
        r1 = r15;
        r15 = r15 + 1;
        r3 = r14;
        r14 = r14 + 1;
        r0[r1] = r0[r3];
        r10 = r10 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0179, code lost:
    
        if (r10 != 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x017f, code lost:
    
        r0 = gallopLeft((java.lang.Comparable) r0[r13], r0, r14, r10, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0195, code lost:
    
        if (r0 == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0198, code lost:
    
        java.lang.System.arraycopy(r0, r14, r0, r15, r0);
        r15 = r15 + r0;
        r14 = r14 + r0;
        r10 = r10 - r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01bc, code lost:
    
        if (r10 != 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01c2, code lost:
    
        r1 = r15;
        r15 = r15 + 1;
        r3 = r13;
        r13 = r13 + 1;
        r0[r1] = r0[r3];
        r8 = r8 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01d7, code lost:
    
        if (r8 != 1) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01dd, code lost:
    
        r16 = r16 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01e4, code lost:
    
        if (r0 < 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01e7, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01eb, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01f0, code lost:
    
        if (r0 < 7) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01f3, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01f7, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01f9, code lost:
    
        if ((r0 | r1) != false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01fe, code lost:
    
        if (r16 >= 0) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0201, code lost:
    
        r16 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0102 A[PHI: r8 r10 r13 r14 r15 r17 r18
  0x0102: PHI (r8v5 int) = (r8v3 int), (r8v2 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r10v5 int) = (r10v3 int), (r10v10 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r13v5 int) = (r13v3 int), (r13v2 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r14v5 int) = (r14v3 int), (r14v10 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r15v6 int) = (r15v4 int), (r15v14 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r17v3 int) = (r17v2 int), (r17v5 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]
  0x0102: PHI (r18v3 int) = (r18v2 int), (r18v5 int) binds: [B:36:0x00fc, B:33:0x00db] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void mergeLo(int r7, int r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 656
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.mergeLo(int, int, int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00d0, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0135, code lost:
    
        if (java.util.ComparableTimSort.$assertionsDisabled != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0139, code lost:
    
        if (r10 <= 0) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x013f, code lost:
    
        if (r12 > 1) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0149, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x014a, code lost:
    
        r0 = r10 - gallopRight((java.lang.Comparable) r0[r17], r0, r9, r10, r10 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0162, code lost:
    
        if (r0 == 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0165, code lost:
    
        r18 = r18 - r0;
        r16 = r16 - r0;
        r10 = r10 - r0;
        java.lang.System.arraycopy(r0, r16 + 1, r0, r18 + 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x018a, code lost:
    
        if (r10 != 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0190, code lost:
    
        r1 = r18;
        r18 = r18 - 1;
        r3 = r17;
        r17 = r17 - 1;
        r0[r1] = r0[r3];
        r12 = r12 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01a6, code lost:
    
        if (r12 != 1) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01ac, code lost:
    
        r0 = r12 - gallopLeft((java.lang.Comparable) r0[r16], r0, r0, r12, r12 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01c8, code lost:
    
        if (r0 == 0) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01cb, code lost:
    
        r18 = r18 - r0;
        r17 = r17 - r0;
        r12 = r12 - r0;
        java.lang.System.arraycopy(r0, r17 + 1, r0, r18 + 1, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01f4, code lost:
    
        if (r12 > 1) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01fa, code lost:
    
        r1 = r18;
        r18 = r18 - 1;
        r3 = r16;
        r16 = r16 - 1;
        r0[r1] = r0[r3];
        r10 = r10 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x020e, code lost:
    
        if (r10 != 0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0214, code lost:
    
        r19 = r19 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x021b, code lost:
    
        if (r0 < 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x021e, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0222, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0227, code lost:
    
        if (r0 < 7) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x022a, code lost:
    
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x022e, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0230, code lost:
    
        if ((r0 | r1) != false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0235, code lost:
    
        if (r19 >= 0) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0238, code lost:
    
        r19 = 0;
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0128 A[PHI: r10 r12 r16 r17 r18 r20 r21
  0x0128: PHI (r10v5 int) = (r10v3 int), (r10v10 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r12v5 int) = (r12v3 int), (r12v2 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r16v6 int) = (r16v3 int), (r16v11 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r17v5 int) = (r17v3 int), (r17v2 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r18v7 int) = (r18v4 int), (r18v15 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r20v3 int) = (r20v2 int), (r20v5 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r21v3 int) = (r21v2 int), (r21v5 int) binds: [B:36:0x0122, B:33:0x0100] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void mergeHi(int r9, int r10, int r11, int r12) {
        /*
            Method dump skipped, instructions count: 729
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ComparableTimSort.mergeHi(int, int, int, int):void");
    }

    private Object[] ensureCapacity(int i2) {
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
                iMin = Math.min(i7, this.f12549a.length >>> 1);
            }
            this.tmp = new Object[iMin];
            this.tmpLen = iMin;
            this.tmpBase = 0;
        }
        return this.tmp;
    }
}
