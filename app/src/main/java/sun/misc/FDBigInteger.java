package sun.misc;

import java.math.BigInteger;
import java.util.Arrays;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/* loaded from: rt.jar:sun/misc/FDBigInteger.class */
public class FDBigInteger {
    static final int[] SMALL_5_POW;
    static final long[] LONG_5_POW;
    private static final int MAX_FIVE_POW = 340;
    private static final FDBigInteger[] POW_5_CACHE;
    public static final FDBigInteger ZERO;
    private static final long LONG_MASK = 4294967295L;
    private int[] data;
    private int offset;
    private int nWords;
    private boolean isImmutable = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FDBigInteger.class.desiredAssertionStatus();
        SMALL_5_POW = new int[]{1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125};
        LONG_5_POW = new long[]{1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L};
        POW_5_CACHE = new FDBigInteger[340];
        int i2 = 0;
        while (i2 < SMALL_5_POW.length) {
            FDBigInteger fDBigInteger = new FDBigInteger(new int[]{SMALL_5_POW[i2]}, 0);
            fDBigInteger.makeImmutable();
            POW_5_CACHE[i2] = fDBigInteger;
            i2++;
        }
        FDBigInteger fDBigInteger2 = POW_5_CACHE[i2 - 1];
        while (i2 < 340) {
            FDBigInteger fDBigIntegerMult = fDBigInteger2.mult(5);
            fDBigInteger2 = fDBigIntegerMult;
            POW_5_CACHE[i2] = fDBigIntegerMult;
            fDBigInteger2.makeImmutable();
            i2++;
        }
        ZERO = new FDBigInteger(new int[0], 0);
        ZERO.makeImmutable();
    }

    private FDBigInteger(int[] iArr, int i2) {
        this.data = iArr;
        this.offset = i2;
        this.nWords = iArr.length;
        trimLeadingZeros();
    }

    public FDBigInteger(long j2, char[] cArr, int i2, int i3) {
        int i4;
        this.data = new int[Math.max((i3 + 8) / 9, 2)];
        this.data[0] = (int) j2;
        this.data[1] = (int) (j2 >>> 32);
        this.offset = 0;
        this.nWords = 2;
        int i5 = i2;
        int i6 = i3 - 5;
        while (i5 < i6) {
            int i7 = i5 + 5;
            int i8 = i5;
            i5++;
            int i9 = cArr[i8];
            while (true) {
                i4 = i9 - 48;
                if (i5 < i7) {
                    int i10 = i5;
                    i5++;
                    i9 = (10 * i4) + cArr[i10];
                }
            }
            multAddMe(Config.MAX_REPEAT_NUM, i4);
        }
        int i11 = 1;
        int i12 = 0;
        while (i5 < i3) {
            int i13 = i5;
            i5++;
            i12 = ((10 * i12) + cArr[i13]) - 48;
            i11 *= 10;
        }
        if (i11 != 1) {
            multAddMe(i11, i12);
        }
        trimLeadingZeros();
    }

    public static FDBigInteger valueOfPow52(int i2, int i3) {
        if (i2 != 0) {
            if (i3 == 0) {
                return big5pow(i2);
            }
            if (i2 < SMALL_5_POW.length) {
                int i4 = SMALL_5_POW[i2];
                int i5 = i3 >> 5;
                int i6 = i3 & 31;
                if (i6 == 0) {
                    return new FDBigInteger(new int[]{i4}, i5);
                }
                return new FDBigInteger(new int[]{i4 << i6, i4 >>> (32 - i6)}, i5);
            }
            return big5pow(i2).leftShift(i3);
        }
        return valueOfPow2(i3);
    }

    public static FDBigInteger valueOfMulPow52(long j2, int i2, int i3) {
        int[] iArr;
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError(i2);
        }
        if (!$assertionsDisabled && i3 < 0) {
            throw new AssertionError(i3);
        }
        int i4 = (int) j2;
        int i5 = (int) (j2 >>> 32);
        int i6 = i3 >> 5;
        int i7 = i3 & 31;
        if (i2 != 0) {
            if (i2 < SMALL_5_POW.length) {
                long j3 = SMALL_5_POW[i2] & 4294967295L;
                long j4 = (i4 & 4294967295L) * j3;
                int i8 = (int) j4;
                long j5 = ((i5 & 4294967295L) * j3) + (j4 >>> 32);
                int i9 = (int) j5;
                int i10 = (int) (j5 >>> 32);
                if (i7 == 0) {
                    return new FDBigInteger(new int[]{i8, i9, i10}, i6);
                }
                return new FDBigInteger(new int[]{i8 << i7, (i9 << i7) | (i8 >>> (32 - i7)), (i10 << i7) | (i9 >>> (32 - i7)), i10 >>> (32 - i7)}, i6);
            }
            FDBigInteger fDBigIntegerBig5pow = big5pow(i2);
            if (i5 == 0) {
                iArr = new int[fDBigIntegerBig5pow.nWords + 1 + (i3 != 0 ? 1 : 0)];
                mult(fDBigIntegerBig5pow.data, fDBigIntegerBig5pow.nWords, i4, iArr);
            } else {
                iArr = new int[fDBigIntegerBig5pow.nWords + 2 + (i3 != 0 ? 1 : 0)];
                mult(fDBigIntegerBig5pow.data, fDBigIntegerBig5pow.nWords, i4, i5, iArr);
            }
            return new FDBigInteger(iArr, fDBigIntegerBig5pow.offset).leftShift(i3);
        }
        if (i3 != 0) {
            if (i7 == 0) {
                return new FDBigInteger(new int[]{i4, i5}, i6);
            }
            return new FDBigInteger(new int[]{i4 << i7, (i5 << i7) | (i4 >>> (32 - i7)), i5 >>> (32 - i7)}, i6);
        }
        return new FDBigInteger(new int[]{i4, i5}, 0);
    }

    private static FDBigInteger valueOfPow2(int i2) {
        return new FDBigInteger(new int[]{1 << (i2 & 31)}, i2 >> 5);
    }

    private void trimLeadingZeros() {
        int i2 = this.nWords;
        if (i2 > 0) {
            int i3 = i2 - 1;
            if (this.data[i3] == 0) {
                while (i3 > 0 && this.data[i3 - 1] == 0) {
                    i3--;
                }
                this.nWords = i3;
                if (i3 == 0) {
                    this.offset = 0;
                }
            }
        }
    }

    public int getNormalizationBias() {
        if (this.nWords == 0) {
            throw new IllegalArgumentException("Zero value cannot be normalized");
        }
        int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(this.data[this.nWords - 1]);
        return iNumberOfLeadingZeros < 4 ? 28 + iNumberOfLeadingZeros : iNumberOfLeadingZeros - 4;
    }

    private static void leftShift(int[] iArr, int i2, int[] iArr2, int i3, int i4, int i5) {
        while (i2 > 0) {
            int i6 = i5 << i3;
            i5 = iArr[i2 - 1];
            iArr2[i2] = i6 | (i5 >>> i4);
            i2--;
        }
        iArr2[0] = i5 << i3;
    }

    public FDBigInteger leftShift(int i2) {
        int[] iArr;
        if (i2 == 0 || this.nWords == 0) {
            return this;
        }
        int i3 = i2 >> 5;
        int i4 = i2 & 31;
        if (this.isImmutable) {
            if (i4 == 0) {
                return new FDBigInteger(Arrays.copyOf(this.data, this.nWords), this.offset + i3);
            }
            int i5 = 32 - i4;
            int i6 = this.nWords - 1;
            int i7 = this.data[i6];
            int i8 = i7 >>> i5;
            if (i8 != 0) {
                iArr = new int[this.nWords + 1];
                iArr[this.nWords] = i8;
            } else {
                iArr = new int[this.nWords];
            }
            leftShift(this.data, i6, iArr, i4, i5, i7);
            return new FDBigInteger(iArr, this.offset + i3);
        }
        if (i4 != 0) {
            int i9 = 32 - i4;
            if ((this.data[0] << i4) == 0) {
                int i10 = 0;
                int i11 = this.data[0];
                while (i10 < this.nWords - 1) {
                    int i12 = i11 >>> i9;
                    i11 = this.data[i10 + 1];
                    this.data[i10] = i12 | (i11 << i4);
                    i10++;
                }
                int i13 = i11 >>> i9;
                this.data[i10] = i13;
                if (i13 == 0) {
                    this.nWords--;
                }
                this.offset++;
            } else {
                int i14 = this.nWords - 1;
                int i15 = this.data[i14];
                int i16 = i15 >>> i9;
                int[] iArr2 = this.data;
                int[] iArr3 = this.data;
                if (i16 != 0) {
                    if (this.nWords == this.data.length) {
                        int[] iArr4 = new int[this.nWords + 1];
                        iArr2 = iArr4;
                        this.data = iArr4;
                    }
                    int i17 = this.nWords;
                    this.nWords = i17 + 1;
                    iArr2[i17] = i16;
                }
                leftShift(iArr3, i14, iArr2, i4, i9, i15);
            }
        }
        this.offset += i3;
        return this;
    }

    private int size() {
        return this.nWords + this.offset;
    }

    public int quoRemIteration(FDBigInteger fDBigInteger) throws IllegalArgumentException {
        if (!$assertionsDisabled && this.isImmutable) {
            throw new AssertionError((Object) "cannot modify immutable value");
        }
        int size = size();
        int size2 = fDBigInteger.size();
        if (size < size2) {
            int iMultAndCarryBy10 = multAndCarryBy10(this.data, this.nWords, this.data);
            if (iMultAndCarryBy10 != 0) {
                int[] iArr = this.data;
                int i2 = this.nWords;
                this.nWords = i2 + 1;
                iArr[i2] = iMultAndCarryBy10;
                return 0;
            }
            trimLeadingZeros();
            return 0;
        }
        if (size > size2) {
            throw new IllegalArgumentException("disparate values");
        }
        long j2 = (this.data[this.nWords - 1] & 4294967295L) / (fDBigInteger.data[fDBigInteger.nWords - 1] & 4294967295L);
        if (multDiffMe(j2, fDBigInteger) != 0) {
            long j3 = 0;
            int i3 = fDBigInteger.offset - this.offset;
            int[] iArr2 = fDBigInteger.data;
            int[] iArr3 = this.data;
            while (j3 == 0) {
                int i4 = 0;
                for (int i5 = i3; i5 < this.nWords; i5++) {
                    long j4 = j3 + (iArr3[i5] & 4294967295L) + (iArr2[i4] & 4294967295L);
                    iArr3[i5] = (int) j4;
                    j3 = j4 >>> 32;
                    i4++;
                }
                if (!$assertionsDisabled && j3 != 0 && j3 != 1) {
                    throw new AssertionError(j3);
                }
                j2--;
            }
        }
        int iMultAndCarryBy102 = multAndCarryBy10(this.data, this.nWords, this.data);
        if (!$assertionsDisabled && iMultAndCarryBy102 != 0) {
            throw new AssertionError(iMultAndCarryBy102);
        }
        trimLeadingZeros();
        return (int) j2;
    }

    public FDBigInteger multBy10() {
        if (this.nWords == 0) {
            return this;
        }
        if (this.isImmutable) {
            int[] iArr = new int[this.nWords + 1];
            iArr[this.nWords] = multAndCarryBy10(this.data, this.nWords, iArr);
            return new FDBigInteger(iArr, this.offset);
        }
        int iMultAndCarryBy10 = multAndCarryBy10(this.data, this.nWords, this.data);
        if (iMultAndCarryBy10 != 0) {
            if (this.nWords == this.data.length) {
                if (this.data[0] == 0) {
                    int[] iArr2 = this.data;
                    int[] iArr3 = this.data;
                    int i2 = this.nWords - 1;
                    this.nWords = i2;
                    System.arraycopy(iArr2, 1, iArr3, 0, i2);
                    this.offset++;
                } else {
                    this.data = Arrays.copyOf(this.data, this.data.length + 1);
                }
            }
            int[] iArr4 = this.data;
            int i3 = this.nWords;
            this.nWords = i3 + 1;
            iArr4[i3] = iMultAndCarryBy10;
        } else {
            trimLeadingZeros();
        }
        return this;
    }

    public FDBigInteger multByPow52(int i2, int i3) {
        if (this.nWords == 0) {
            return this;
        }
        FDBigInteger fDBigInteger = this;
        if (i2 != 0) {
            int i4 = i3 != 0 ? 1 : 0;
            if (i2 < SMALL_5_POW.length) {
                int[] iArr = new int[this.nWords + 1 + i4];
                mult(this.data, this.nWords, SMALL_5_POW[i2], iArr);
                fDBigInteger = new FDBigInteger(iArr, this.offset);
            } else {
                FDBigInteger fDBigIntegerBig5pow = big5pow(i2);
                int[] iArr2 = new int[this.nWords + fDBigIntegerBig5pow.size() + i4];
                mult(this.data, this.nWords, fDBigIntegerBig5pow.data, fDBigIntegerBig5pow.nWords, iArr2);
                fDBigInteger = new FDBigInteger(iArr2, this.offset + fDBigIntegerBig5pow.offset);
            }
        }
        return fDBigInteger.leftShift(i3);
    }

    private static void mult(int[] iArr, int i2, int[] iArr2, int i3, int[] iArr3) {
        for (int i4 = 0; i4 < i2; i4++) {
            long j2 = iArr[i4] & 4294967295L;
            long j3 = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                long j4 = j3 + (iArr3[i4 + i5] & 4294967295L) + (j2 * (iArr2[i5] & 4294967295L));
                iArr3[i4 + i5] = (int) j4;
                j3 = j4 >>> 32;
            }
            iArr3[i4 + i3] = (int) j3;
        }
    }

    public FDBigInteger leftInplaceSub(FDBigInteger fDBigInteger) {
        FDBigInteger fDBigInteger2;
        if (!$assertionsDisabled && size() < fDBigInteger.size()) {
            throw new AssertionError((Object) "result should be positive");
        }
        if (this.isImmutable) {
            fDBigInteger2 = new FDBigInteger((int[]) this.data.clone(), this.offset);
        } else {
            fDBigInteger2 = this;
        }
        int i2 = fDBigInteger.offset - fDBigInteger2.offset;
        int[] iArr = fDBigInteger.data;
        int[] iArr2 = fDBigInteger2.data;
        int i3 = fDBigInteger.nWords;
        int i4 = fDBigInteger2.nWords;
        if (i2 < 0) {
            int i5 = i4 - i2;
            if (i5 < iArr2.length) {
                System.arraycopy(iArr2, 0, iArr2, -i2, i4);
                Arrays.fill(iArr2, 0, -i2, 0);
            } else {
                int[] iArr3 = new int[i5];
                System.arraycopy(iArr2, 0, iArr3, -i2, i4);
                iArr2 = iArr3;
                fDBigInteger2.data = iArr3;
            }
            fDBigInteger2.offset = fDBigInteger.offset;
            i4 = i5;
            fDBigInteger2.nWords = i5;
            i2 = 0;
        }
        long j2 = 0;
        int i6 = i2;
        int i7 = 0;
        while (i7 < i3 && i6 < i4) {
            long j3 = ((iArr2[i6] & 4294967295L) - (iArr[i7] & 4294967295L)) + j2;
            iArr2[i6] = (int) j3;
            j2 = j3 >> 32;
            i7++;
            i6++;
        }
        while (j2 != 0 && i6 < i4) {
            long j4 = (iArr2[i6] & 4294967295L) + j2;
            iArr2[i6] = (int) j4;
            j2 = j4 >> 32;
            i6++;
        }
        if (!$assertionsDisabled && j2 != 0) {
            throw new AssertionError(j2);
        }
        fDBigInteger2.trimLeadingZeros();
        return fDBigInteger2;
    }

    public FDBigInteger rightInplaceSub(FDBigInteger fDBigInteger) {
        if (!$assertionsDisabled && size() < fDBigInteger.size()) {
            throw new AssertionError((Object) "result should be positive");
        }
        if (fDBigInteger.isImmutable) {
            fDBigInteger = new FDBigInteger((int[]) fDBigInteger.data.clone(), fDBigInteger.offset);
        }
        int i2 = this.offset - fDBigInteger.offset;
        int[] iArr = fDBigInteger.data;
        int[] iArr2 = this.data;
        int i3 = fDBigInteger.nWords;
        int i4 = this.nWords;
        if (i2 < 0) {
            if (i4 < iArr.length) {
                System.arraycopy(iArr, 0, iArr, -i2, i3);
                Arrays.fill(iArr, 0, -i2, 0);
            } else {
                int[] iArr3 = new int[i4];
                System.arraycopy(iArr, 0, iArr3, -i2, i3);
                iArr = iArr3;
                fDBigInteger.data = iArr3;
            }
            fDBigInteger.offset = this.offset;
            int i5 = i3 - i2;
            i2 = 0;
        } else {
            int i6 = i4 + i2;
            if (i6 >= iArr.length) {
                int[] iArrCopyOf = Arrays.copyOf(iArr, i6);
                iArr = iArrCopyOf;
                fDBigInteger.data = iArrCopyOf;
            }
        }
        int i7 = 0;
        long j2 = 0;
        while (i7 < i2) {
            long j3 = (0 - (iArr[i7] & 4294967295L)) + j2;
            iArr[i7] = (int) j3;
            j2 = j3 >> 32;
            i7++;
        }
        for (int i8 = 0; i8 < i4; i8++) {
            long j4 = ((iArr2[i8] & 4294967295L) - (iArr[i7] & 4294967295L)) + j2;
            iArr[i7] = (int) j4;
            j2 = j4 >> 32;
            i7++;
        }
        if (!$assertionsDisabled && j2 != 0) {
            throw new AssertionError(j2);
        }
        fDBigInteger.nWords = i7;
        fDBigInteger.trimLeadingZeros();
        return fDBigInteger;
    }

    private static int checkZeroTail(int[] iArr, int i2) {
        while (i2 > 0) {
            i2--;
            if (iArr[i2] != 0) {
                return 1;
            }
        }
        return 0;
    }

    public int cmp(FDBigInteger fDBigInteger) {
        int i2 = this.nWords + this.offset;
        int i3 = fDBigInteger.nWords + fDBigInteger.offset;
        if (i2 > i3) {
            return 1;
        }
        if (i2 < i3) {
            return -1;
        }
        int i4 = this.nWords;
        int i5 = fDBigInteger.nWords;
        while (i4 > 0 && i5 > 0) {
            i4--;
            int i6 = this.data[i4];
            i5--;
            int i7 = fDBigInteger.data[i5];
            if (i6 != i7) {
                return (((long) i6) & 4294967295L) < (((long) i7) & 4294967295L) ? -1 : 1;
            }
        }
        if (i4 > 0) {
            return checkZeroTail(this.data, i4);
        }
        if (i5 > 0) {
            return -checkZeroTail(fDBigInteger.data, i5);
        }
        return 0;
    }

    public int cmpPow52(int i2, int i3) {
        if (i2 == 0) {
            int i4 = i3 >> 5;
            int i5 = i3 & 31;
            int i6 = this.nWords + this.offset;
            if (i6 > i4 + 1) {
                return 1;
            }
            if (i6 < i4 + 1) {
                return -1;
            }
            int i7 = this.data[this.nWords - 1];
            int i8 = 1 << i5;
            if (i7 != i8) {
                return (((long) i7) & 4294967295L) < (((long) i8) & 4294967295L) ? -1 : 1;
            }
            return checkZeroTail(this.data, this.nWords - 1);
        }
        return cmp(big5pow(i2).leftShift(i3));
    }

    public int addAndCmp(FDBigInteger fDBigInteger, FDBigInteger fDBigInteger2) {
        FDBigInteger fDBigInteger3;
        FDBigInteger fDBigInteger4;
        int i2;
        int i3;
        int size = fDBigInteger.size();
        int size2 = fDBigInteger2.size();
        if (size >= size2) {
            fDBigInteger3 = fDBigInteger;
            fDBigInteger4 = fDBigInteger2;
            i2 = size;
            i3 = size2;
        } else {
            fDBigInteger3 = fDBigInteger2;
            fDBigInteger4 = fDBigInteger;
            i2 = size2;
            i3 = size;
        }
        int size3 = size();
        if (i2 == 0) {
            return size3 == 0 ? 0 : 1;
        }
        if (i3 == 0) {
            return cmp(fDBigInteger3);
        }
        if (i2 > size3) {
            return -1;
        }
        if (i2 + 1 < size3) {
            return 1;
        }
        long j2 = fDBigInteger3.data[fDBigInteger3.nWords - 1] & 4294967295L;
        if (i3 == i2) {
            j2 += fDBigInteger4.data[fDBigInteger4.nWords - 1] & 4294967295L;
        }
        if ((j2 >>> 32) == 0) {
            if (((j2 + 1) >>> 32) == 0) {
                if (i2 < size3) {
                    return 1;
                }
                long j3 = this.data[this.nWords - 1] & 4294967295L;
                if (j3 < j2) {
                    return -1;
                }
                if (j3 > j2 + 1) {
                    return 1;
                }
            }
        } else {
            if (i2 + 1 > size3) {
                return -1;
            }
            long j4 = j2 >>> 32;
            long j5 = this.data[this.nWords - 1] & 4294967295L;
            if (j5 < j4) {
                return -1;
            }
            if (j5 > j4 + 1) {
                return 1;
            }
        }
        return cmp(fDBigInteger3.add(fDBigInteger4));
    }

    public void makeImmutable() {
        this.isImmutable = true;
    }

    private FDBigInteger mult(int i2) {
        if (this.nWords == 0) {
            return this;
        }
        int[] iArr = new int[this.nWords + 1];
        mult(this.data, this.nWords, i2, iArr);
        return new FDBigInteger(iArr, this.offset);
    }

    private FDBigInteger mult(FDBigInteger fDBigInteger) {
        if (this.nWords == 0) {
            return this;
        }
        if (size() == 1) {
            return fDBigInteger.mult(this.data[0]);
        }
        if (fDBigInteger.nWords == 0) {
            return fDBigInteger;
        }
        if (fDBigInteger.size() == 1) {
            return mult(fDBigInteger.data[0]);
        }
        int[] iArr = new int[this.nWords + fDBigInteger.nWords];
        mult(this.data, this.nWords, fDBigInteger.data, fDBigInteger.nWords, iArr);
        return new FDBigInteger(iArr, this.offset + fDBigInteger.offset);
    }

    private FDBigInteger add(FDBigInteger fDBigInteger) {
        FDBigInteger fDBigInteger2;
        int i2;
        FDBigInteger fDBigInteger3;
        int i3;
        int size = size();
        int size2 = fDBigInteger.size();
        if (size >= size2) {
            fDBigInteger2 = this;
            i2 = size;
            fDBigInteger3 = fDBigInteger;
            i3 = size2;
        } else {
            fDBigInteger2 = fDBigInteger;
            i2 = size2;
            fDBigInteger3 = this;
            i3 = size;
        }
        int[] iArr = new int[i2 + 1];
        int i4 = 0;
        long j2 = 0;
        while (i4 < i3) {
            long j3 = j2 + (i4 < fDBigInteger2.offset ? 0L : fDBigInteger2.data[i4 - fDBigInteger2.offset] & 4294967295L) + (i4 < fDBigInteger3.offset ? 0L : fDBigInteger3.data[i4 - fDBigInteger3.offset] & 4294967295L);
            iArr[i4] = (int) j3;
            j2 = j3 >> 32;
            i4++;
        }
        while (i4 < i2) {
            long j4 = j2 + (i4 < fDBigInteger2.offset ? 0L : fDBigInteger2.data[i4 - fDBigInteger2.offset] & 4294967295L);
            iArr[i4] = (int) j4;
            j2 = j4 >> 32;
            i4++;
        }
        iArr[i2] = (int) j2;
        return new FDBigInteger(iArr, 0);
    }

    private void multAddMe(int i2, int i3) {
        long j2 = i2 & 4294967295L;
        long j3 = (j2 * (this.data[0] & 4294967295L)) + (i3 & 4294967295L);
        this.data[0] = (int) j3;
        long j4 = j3 >>> 32;
        for (int i4 = 1; i4 < this.nWords; i4++) {
            long j5 = j4 + (j2 * (this.data[i4] & 4294967295L));
            this.data[i4] = (int) j5;
            j4 = j5 >>> 32;
        }
        if (j4 != 0) {
            int[] iArr = this.data;
            int i5 = this.nWords;
            this.nWords = i5 + 1;
            iArr[i5] = (int) j4;
        }
    }

    private long multDiffMe(long j2, FDBigInteger fDBigInteger) {
        long j3 = 0;
        if (j2 != 0) {
            int i2 = fDBigInteger.offset - this.offset;
            if (i2 >= 0) {
                int[] iArr = fDBigInteger.data;
                int[] iArr2 = this.data;
                int i3 = 0;
                int i4 = i2;
                while (i3 < fDBigInteger.nWords) {
                    long j4 = j3 + ((iArr2[i4] & 4294967295L) - (j2 * (iArr[i3] & 4294967295L)));
                    iArr2[i4] = (int) j4;
                    j3 = j4 >> 32;
                    i3++;
                    i4++;
                }
            } else {
                int i5 = -i2;
                int[] iArr3 = new int[this.nWords + i5];
                int i6 = 0;
                int i7 = 0;
                int[] iArr4 = fDBigInteger.data;
                while (i7 < i5 && i6 < fDBigInteger.nWords) {
                    long j5 = j3 - (j2 * (iArr4[i6] & 4294967295L));
                    iArr3[i7] = (int) j5;
                    j3 = j5 >> 32;
                    i6++;
                    i7++;
                }
                int i8 = 0;
                int[] iArr5 = this.data;
                while (i6 < fDBigInteger.nWords) {
                    long j6 = j3 + ((iArr5[i8] & 4294967295L) - (j2 * (iArr4[i6] & 4294967295L)));
                    iArr3[i7] = (int) j6;
                    j3 = j6 >> 32;
                    i6++;
                    i8++;
                    i7++;
                }
                this.nWords += i5;
                this.offset -= i5;
                this.data = iArr3;
            }
        }
        return j3;
    }

    private static int multAndCarryBy10(int[] iArr, int i2, int[] iArr2) {
        long j2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            long j3 = ((iArr[i3] & 4294967295L) * 10) + j2;
            iArr2[i3] = (int) j3;
            j2 = j3 >>> 32;
        }
        return (int) j2;
    }

    private static void mult(int[] iArr, int i2, int i3, int[] iArr2) {
        long j2 = i3 & 4294967295L;
        long j3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            long j4 = ((iArr[i4] & 4294967295L) * j2) + j3;
            iArr2[i4] = (int) j4;
            j3 = j4 >>> 32;
        }
        iArr2[i2] = (int) j3;
    }

    private static void mult(int[] iArr, int i2, int i3, int i4, int[] iArr2) {
        long j2 = i3 & 4294967295L;
        long j3 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            long j4 = (j2 * (iArr[i5] & 4294967295L)) + j3;
            iArr2[i5] = (int) j4;
            j3 = j4 >>> 32;
        }
        iArr2[i2] = (int) j3;
        long j5 = i4 & 4294967295L;
        long j6 = 0;
        for (int i6 = 0; i6 < i2; i6++) {
            long j7 = (iArr2[i6 + 1] & 4294967295L) + (j5 * (iArr[i6] & 4294967295L)) + j6;
            iArr2[i6 + 1] = (int) j7;
            j6 = j7 >>> 32;
        }
        iArr2[i2 + 1] = (int) j6;
    }

    private static FDBigInteger big5pow(int i2) {
        if (!$assertionsDisabled && i2 < 0) {
            throw new AssertionError(i2);
        }
        if (i2 < 340) {
            return POW_5_CACHE[i2];
        }
        return big5powRec(i2);
    }

    private static FDBigInteger big5powRec(int i2) {
        if (i2 < 340) {
            return POW_5_CACHE[i2];
        }
        int i3 = i2 >> 1;
        int i4 = i2 - i3;
        FDBigInteger fDBigIntegerBig5powRec = big5powRec(i3);
        if (i4 < SMALL_5_POW.length) {
            return fDBigIntegerBig5powRec.mult(SMALL_5_POW[i4]);
        }
        return fDBigIntegerBig5powRec.mult(big5powRec(i4));
    }

    public String toHexString() {
        if (this.nWords == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder((this.nWords + this.offset) * 8);
        for (int i2 = this.nWords - 1; i2 >= 0; i2--) {
            String hexString = Integer.toHexString(this.data[i2]);
            for (int length = hexString.length(); length < 8; length++) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        for (int i3 = this.offset; i3 > 0; i3--) {
            sb.append("00000000");
        }
        return sb.toString();
    }

    public BigInteger toBigInteger() {
        byte[] bArr = new byte[(this.nWords * 4) + 1];
        for (int i2 = 0; i2 < this.nWords; i2++) {
            int i3 = this.data[i2];
            bArr[(bArr.length - (4 * i2)) - 1] = (byte) i3;
            bArr[(bArr.length - (4 * i2)) - 2] = (byte) (i3 >> 8);
            bArr[(bArr.length - (4 * i2)) - 3] = (byte) (i3 >> 16);
            bArr[(bArr.length - (4 * i2)) - 4] = (byte) (i3 >> 24);
        }
        return new BigInteger(bArr).shiftLeft(this.offset * 32);
    }

    public String toString() {
        return toBigInteger().toString();
    }
}
