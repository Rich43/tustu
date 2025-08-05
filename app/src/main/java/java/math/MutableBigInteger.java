package java.math;

import java.util.Arrays;

/* loaded from: rt.jar:java/math/MutableBigInteger.class */
class MutableBigInteger {
    int[] value;
    int intLen;
    int offset;
    static final MutableBigInteger ONE;
    static final int KNUTH_POW2_THRESH_LEN = 6;
    static final int KNUTH_POW2_THRESH_ZEROS = 3;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MutableBigInteger.class.desiredAssertionStatus();
        ONE = new MutableBigInteger(1);
    }

    MutableBigInteger() {
        this.offset = 0;
        this.value = new int[1];
        this.intLen = 0;
    }

    MutableBigInteger(int i2) {
        this.offset = 0;
        this.value = new int[1];
        this.intLen = 1;
        this.value[0] = i2;
    }

    MutableBigInteger(int[] iArr) {
        this.offset = 0;
        this.value = iArr;
        this.intLen = iArr.length;
    }

    MutableBigInteger(BigInteger bigInteger) {
        this.offset = 0;
        this.intLen = bigInteger.mag.length;
        this.value = Arrays.copyOf(bigInteger.mag, this.intLen);
    }

    MutableBigInteger(MutableBigInteger mutableBigInteger) {
        this.offset = 0;
        this.intLen = mutableBigInteger.intLen;
        this.value = Arrays.copyOfRange(mutableBigInteger.value, mutableBigInteger.offset, mutableBigInteger.offset + this.intLen);
    }

    private void ones(int i2) {
        if (i2 > this.value.length) {
            this.value = new int[i2];
        }
        Arrays.fill(this.value, -1);
        this.offset = 0;
        this.intLen = i2;
    }

    private int[] getMagnitudeArray() {
        if (this.offset > 0 || this.value.length != this.intLen) {
            return Arrays.copyOfRange(this.value, this.offset, this.offset + this.intLen);
        }
        return this.value;
    }

    private long toLong() {
        if (!$assertionsDisabled && this.intLen > 2) {
            throw new AssertionError((Object) "this MutableBigInteger exceeds the range of long");
        }
        if (this.intLen == 0) {
            return 0L;
        }
        long j2 = this.value[this.offset] & 4294967295L;
        return this.intLen == 2 ? (j2 << 32) | (this.value[this.offset + 1] & 4294967295L) : j2;
    }

    BigInteger toBigInteger(int i2) {
        if (this.intLen == 0 || i2 == 0) {
            return BigInteger.ZERO;
        }
        return new BigInteger(getMagnitudeArray(), i2);
    }

    BigInteger toBigInteger() {
        normalize();
        return toBigInteger(isZero() ? 0 : 1);
    }

    BigDecimal toBigDecimal(int i2, int i3) {
        if (this.intLen == 0 || i2 == 0) {
            return BigDecimal.zeroValueOf(i3);
        }
        int[] magnitudeArray = getMagnitudeArray();
        int length = magnitudeArray.length;
        int i4 = magnitudeArray[0];
        if (length > 2 || (i4 < 0 && length == 2)) {
            return new BigDecimal(new BigInteger(magnitudeArray, i2), Long.MIN_VALUE, i3, 0);
        }
        long j2 = length == 2 ? (magnitudeArray[1] & 4294967295L) | ((i4 & 4294967295L) << 32) : i4 & 4294967295L;
        return BigDecimal.valueOf(i2 == -1 ? -j2 : j2, i3);
    }

    long toCompactValue(int i2) {
        if (this.intLen == 0 || i2 == 0) {
            return 0L;
        }
        int[] magnitudeArray = getMagnitudeArray();
        int length = magnitudeArray.length;
        int i3 = magnitudeArray[0];
        if (length > 2) {
            return Long.MIN_VALUE;
        }
        if (i3 < 0 && length == 2) {
            return Long.MIN_VALUE;
        }
        long j2 = length == 2 ? (magnitudeArray[1] & 4294967295L) | ((i3 & 4294967295L) << 32) : i3 & 4294967295L;
        return i2 == -1 ? -j2 : j2;
    }

    void clear() {
        this.intLen = 0;
        this.offset = 0;
        int length = this.value.length;
        for (int i2 = 0; i2 < length; i2++) {
            this.value[i2] = 0;
        }
    }

    void reset() {
        this.intLen = 0;
        this.offset = 0;
    }

    final int compare(MutableBigInteger mutableBigInteger) {
        int i2 = mutableBigInteger.intLen;
        if (this.intLen < i2) {
            return -1;
        }
        if (this.intLen > i2) {
            return 1;
        }
        int[] iArr = mutableBigInteger.value;
        int i3 = this.offset;
        int i4 = mutableBigInteger.offset;
        while (i3 < this.intLen + this.offset) {
            int i5 = this.value[i3] - 2147483648;
            int i6 = iArr[i4] - 2147483648;
            if (i5 < i6) {
                return -1;
            }
            if (i5 <= i6) {
                i3++;
                i4++;
            } else {
                return 1;
            }
        }
        return 0;
    }

    private int compareShifted(MutableBigInteger mutableBigInteger, int i2) {
        int i3 = mutableBigInteger.intLen;
        int i4 = this.intLen - i2;
        if (i4 < i3) {
            return -1;
        }
        if (i4 > i3) {
            return 1;
        }
        int[] iArr = mutableBigInteger.value;
        int i5 = this.offset;
        int i6 = mutableBigInteger.offset;
        while (i5 < i4 + this.offset) {
            int i7 = this.value[i5] - 2147483648;
            int i8 = iArr[i6] - 2147483648;
            if (i7 < i8) {
                return -1;
            }
            if (i7 <= i8) {
                i5++;
                i6++;
            } else {
                return 1;
            }
        }
        return 0;
    }

    final int compareHalf(MutableBigInteger mutableBigInteger) {
        int i2 = mutableBigInteger.intLen;
        int i3 = this.intLen;
        if (i3 <= 0) {
            return i2 <= 0 ? 0 : -1;
        }
        if (i3 > i2) {
            return 1;
        }
        if (i3 < i2 - 1) {
            return -1;
        }
        int[] iArr = mutableBigInteger.value;
        int i4 = 0;
        int i5 = 0;
        if (i3 != i2) {
            if (iArr[0] == 1) {
                i4 = 0 + 1;
                i5 = Integer.MIN_VALUE;
            } else {
                return -1;
            }
        }
        int[] iArr2 = this.value;
        int i6 = this.offset;
        int i7 = i4;
        while (i6 < i3 + this.offset) {
            int i8 = i7;
            i7++;
            int i9 = iArr[i8];
            long j2 = ((i9 >>> 1) + i5) & 4294967295L;
            int i10 = i6;
            i6++;
            long j3 = iArr2[i10] & 4294967295L;
            if (j3 != j2) {
                return j3 < j2 ? -1 : 1;
            }
            i5 = (i9 & 1) << 31;
        }
        return i5 == 0 ? 0 : -1;
    }

    private final int getLowestSetBit() {
        if (this.intLen == 0) {
            return -1;
        }
        int i2 = this.intLen - 1;
        while (i2 > 0 && this.value[i2 + this.offset] == 0) {
            i2--;
        }
        int i3 = this.value[i2 + this.offset];
        if (i3 == 0) {
            return -1;
        }
        return (((this.intLen - 1) - i2) << 5) + Integer.numberOfTrailingZeros(i3);
    }

    private final int getInt(int i2) {
        return this.value[this.offset + i2];
    }

    private final long getLong(int i2) {
        return this.value[this.offset + i2] & 4294967295L;
    }

    final void normalize() {
        if (this.intLen == 0) {
            this.offset = 0;
            return;
        }
        int i2 = this.offset;
        if (this.value[i2] != 0) {
            return;
        }
        int i3 = i2 + this.intLen;
        do {
            i2++;
            if (i2 >= i3) {
                break;
            }
        } while (this.value[i2] == 0);
        int i4 = i2 - this.offset;
        this.intLen -= i4;
        this.offset = this.intLen == 0 ? 0 : this.offset + i4;
    }

    private final void ensureCapacity(int i2) {
        if (this.value.length < i2) {
            this.value = new int[i2];
            this.offset = 0;
            this.intLen = i2;
        }
    }

    int[] toIntArray() {
        int[] iArr = new int[this.intLen];
        for (int i2 = 0; i2 < this.intLen; i2++) {
            iArr[i2] = this.value[this.offset + i2];
        }
        return iArr;
    }

    void setInt(int i2, int i3) {
        this.value[this.offset + i2] = i3;
    }

    void setValue(int[] iArr, int i2) {
        this.value = iArr;
        this.intLen = i2;
        this.offset = 0;
    }

    void copyValue(MutableBigInteger mutableBigInteger) {
        int i2 = mutableBigInteger.intLen;
        if (this.value.length < i2) {
            this.value = new int[i2];
        }
        System.arraycopy(mutableBigInteger.value, mutableBigInteger.offset, this.value, 0, i2);
        this.intLen = i2;
        this.offset = 0;
    }

    void copyValue(int[] iArr) {
        int length = iArr.length;
        if (this.value.length < length) {
            this.value = new int[length];
        }
        System.arraycopy(iArr, 0, this.value, 0, length);
        this.intLen = length;
        this.offset = 0;
    }

    boolean isOne() {
        return this.intLen == 1 && this.value[this.offset] == 1;
    }

    boolean isZero() {
        return this.intLen == 0;
    }

    boolean isEven() {
        return this.intLen == 0 || (this.value[(this.offset + this.intLen) - 1] & 1) == 0;
    }

    boolean isOdd() {
        return !isZero() && (this.value[(this.offset + this.intLen) - 1] & 1) == 1;
    }

    boolean isNormal() {
        if (this.intLen + this.offset > this.value.length) {
            return false;
        }
        return this.intLen == 0 || this.value[this.offset] != 0;
    }

    public String toString() {
        return toBigInteger(1).toString();
    }

    void safeRightShift(int i2) {
        if (i2 / 32 >= this.intLen) {
            reset();
        } else {
            rightShift(i2);
        }
    }

    void rightShift(int i2) {
        if (this.intLen == 0) {
            return;
        }
        int i3 = i2 & 31;
        this.intLen -= i2 >>> 5;
        if (i3 == 0) {
            return;
        }
        if (i3 >= BigInteger.bitLengthForInt(this.value[this.offset])) {
            primitiveLeftShift(32 - i3);
            this.intLen--;
        } else {
            primitiveRightShift(i3);
        }
    }

    void safeLeftShift(int i2) {
        if (i2 > 0) {
            leftShift(i2);
        }
    }

    void leftShift(int i2) {
        if (this.intLen == 0) {
            return;
        }
        int i3 = i2 >>> 5;
        int i4 = i2 & 31;
        int iBitLengthForInt = BigInteger.bitLengthForInt(this.value[this.offset]);
        if (i2 <= 32 - iBitLengthForInt) {
            primitiveLeftShift(i4);
            return;
        }
        int i5 = this.intLen + i3 + 1;
        if (i4 <= 32 - iBitLengthForInt) {
            i5--;
        }
        if (this.value.length < i5) {
            int[] iArr = new int[i5];
            for (int i6 = 0; i6 < this.intLen; i6++) {
                iArr[i6] = this.value[this.offset + i6];
            }
            setValue(iArr, i5);
        } else if (this.value.length - this.offset >= i5) {
            for (int i7 = 0; i7 < i5 - this.intLen; i7++) {
                this.value[this.offset + this.intLen + i7] = 0;
            }
        } else {
            for (int i8 = 0; i8 < this.intLen; i8++) {
                this.value[i8] = this.value[this.offset + i8];
            }
            for (int i9 = this.intLen; i9 < i5; i9++) {
                this.value[i9] = 0;
            }
            this.offset = 0;
        }
        this.intLen = i5;
        if (i4 == 0) {
            return;
        }
        if (i4 <= 32 - iBitLengthForInt) {
            primitiveLeftShift(i4);
        } else {
            primitiveRightShift(32 - i4);
        }
    }

    private int divadd(int[] iArr, int[] iArr2, int i2) {
        long j2 = 0;
        for (int length = iArr.length - 1; length >= 0; length--) {
            long j3 = (iArr[length] & 4294967295L) + (iArr2[length + i2] & 4294967295L) + j2;
            iArr2[length + i2] = (int) j3;
            j2 = j3 >>> 32;
        }
        return (int) j2;
    }

    private int mulsub(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        long j2 = i2 & 4294967295L;
        long j3 = 0;
        int i5 = i4 + i3;
        for (int i6 = i3 - 1; i6 >= 0; i6--) {
            long j4 = ((iArr2[i6] & 4294967295L) * j2) + j3;
            long j5 = iArr[i5] - j4;
            int i7 = i5;
            i5--;
            iArr[i7] = (int) j5;
            j3 = (j4 >>> 32) + ((j5 & 4294967295L) > (((long) (((int) j4) ^ (-1))) & 4294967295L) ? 1 : 0);
        }
        return (int) j3;
    }

    private int mulsubBorrow(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        long j2 = i2 & 4294967295L;
        long j3 = 0;
        int i5 = i4 + i3;
        for (int i6 = i3 - 1; i6 >= 0; i6--) {
            long j4 = ((iArr2[i6] & 4294967295L) * j2) + j3;
            int i7 = i5;
            i5--;
            j3 = (j4 >>> 32) + (((((long) iArr[i7]) - j4) & 4294967295L) > (((long) (((int) j4) ^ (-1))) & 4294967295L) ? 1 : 0);
        }
        return (int) j3;
    }

    private final void primitiveRightShift(int i2) {
        int[] iArr = this.value;
        int i3 = 32 - i2;
        int i4 = (this.offset + this.intLen) - 1;
        int i5 = iArr[i4];
        while (i4 > this.offset) {
            int i6 = i5;
            i5 = iArr[i4 - 1];
            iArr[i4] = (i5 << i3) | (i6 >>> i2);
            i4--;
        }
        int i7 = this.offset;
        iArr[i7] = iArr[i7] >>> i2;
    }

    private final void primitiveLeftShift(int i2) {
        int[] iArr = this.value;
        int i3 = 32 - i2;
        int i4 = this.offset;
        int i5 = iArr[i4];
        int i6 = (i4 + this.intLen) - 1;
        while (i4 < i6) {
            int i7 = i5;
            i5 = iArr[i4 + 1];
            iArr[i4] = (i7 << i2) | (i5 >>> i3);
            i4++;
        }
        int i8 = (this.offset + this.intLen) - 1;
        iArr[i8] = iArr[i8] << i2;
    }

    private BigInteger getLower(int i2) {
        if (isZero()) {
            return BigInteger.ZERO;
        }
        if (this.intLen < i2) {
            return toBigInteger(1);
        }
        int i3 = i2;
        while (i3 > 0 && this.value[(this.offset + this.intLen) - i3] == 0) {
            i3--;
        }
        return new BigInteger(Arrays.copyOfRange(this.value, (this.offset + this.intLen) - i3, this.offset + this.intLen), i3 > 0 ? 1 : 0);
    }

    private void keepLower(int i2) {
        if (this.intLen >= i2) {
            this.offset += this.intLen - i2;
            this.intLen = i2;
        }
    }

    void add(MutableBigInteger mutableBigInteger) {
        long j2;
        int i2 = this.intLen;
        int i3 = mutableBigInteger.intLen;
        int i4 = this.intLen > mutableBigInteger.intLen ? this.intLen : mutableBigInteger.intLen;
        int[] iArr = this.value.length < i4 ? new int[i4] : this.value;
        int length = iArr.length - 1;
        long j3 = 0;
        while (true) {
            j2 = j3;
            if (i2 <= 0 || i3 <= 0) {
                break;
            }
            i2--;
            i3--;
            long j4 = (this.value[i2 + this.offset] & 4294967295L) + (mutableBigInteger.value[i3 + mutableBigInteger.offset] & 4294967295L) + j2;
            int i5 = length;
            length--;
            iArr[i5] = (int) j4;
            j3 = j4 >>> 32;
        }
        while (i2 > 0) {
            i2--;
            if (j2 == 0 && iArr == this.value && length == i2 + this.offset) {
                return;
            }
            long j5 = (this.value[i2 + this.offset] & 4294967295L) + j2;
            int i6 = length;
            length--;
            iArr[i6] = (int) j5;
            j2 = j5 >>> 32;
        }
        while (i3 > 0) {
            i3--;
            long j6 = (mutableBigInteger.value[i3 + mutableBigInteger.offset] & 4294967295L) + j2;
            int i7 = length;
            length--;
            iArr[i7] = (int) j6;
            j2 = j6 >>> 32;
        }
        if (j2 > 0) {
            i4++;
            if (iArr.length < i4) {
                int[] iArr2 = new int[i4];
                System.arraycopy(iArr, 0, iArr2, 1, iArr.length);
                iArr2[0] = 1;
                iArr = iArr2;
            } else {
                int i8 = length;
                int i9 = length - 1;
                iArr[i8] = 1;
            }
        }
        this.value = iArr;
        this.intLen = i4;
        this.offset = iArr.length - i4;
    }

    void addShifted(MutableBigInteger mutableBigInteger, int i2) {
        long j2;
        if (mutableBigInteger.isZero()) {
            return;
        }
        int i3 = this.intLen;
        int i4 = mutableBigInteger.intLen + i2;
        int i5 = this.intLen > i4 ? this.intLen : i4;
        int[] iArr = this.value.length < i5 ? new int[i5] : this.value;
        int length = iArr.length - 1;
        long j3 = 0;
        while (true) {
            j2 = j3;
            if (i3 <= 0 || i4 <= 0) {
                break;
            }
            i3--;
            i4--;
            long j4 = (this.value[i3 + this.offset] & 4294967295L) + ((i4 + mutableBigInteger.offset < mutableBigInteger.value.length ? mutableBigInteger.value[i4 + mutableBigInteger.offset] : 0) & 4294967295L) + j2;
            int i6 = length;
            length--;
            iArr[i6] = (int) j4;
            j3 = j4 >>> 32;
        }
        while (i3 > 0) {
            i3--;
            if (j2 == 0 && iArr == this.value && length == i3 + this.offset) {
                return;
            }
            long j5 = (this.value[i3 + this.offset] & 4294967295L) + j2;
            int i7 = length;
            length--;
            iArr[i7] = (int) j5;
            j2 = j5 >>> 32;
        }
        while (i4 > 0) {
            i4--;
            long j6 = ((i4 + mutableBigInteger.offset < mutableBigInteger.value.length ? mutableBigInteger.value[i4 + mutableBigInteger.offset] : 0) & 4294967295L) + j2;
            int i8 = length;
            length--;
            iArr[i8] = (int) j6;
            j2 = j6 >>> 32;
        }
        if (j2 > 0) {
            i5++;
            if (iArr.length < i5) {
                int[] iArr2 = new int[i5];
                System.arraycopy(iArr, 0, iArr2, 1, iArr.length);
                iArr2[0] = 1;
                iArr = iArr2;
            } else {
                int i9 = length;
                int i10 = length - 1;
                iArr[i9] = 1;
            }
        }
        this.value = iArr;
        this.intLen = i5;
        this.offset = iArr.length - i5;
    }

    void addDisjoint(MutableBigInteger mutableBigInteger, int i2) {
        int[] iArr;
        if (mutableBigInteger.isZero()) {
            return;
        }
        int i3 = this.intLen;
        int i4 = mutableBigInteger.intLen + i2;
        int i5 = this.intLen > i4 ? this.intLen : i4;
        if (this.value.length < i5) {
            iArr = new int[i5];
        } else {
            iArr = this.value;
            Arrays.fill(this.value, this.offset + this.intLen, this.value.length, 0);
        }
        int length = iArr.length - 1;
        System.arraycopy(this.value, this.offset, iArr, (length + 1) - i3, i3);
        int i6 = i4 - i3;
        int i7 = length - i3;
        int iMin = Math.min(i6, mutableBigInteger.value.length - mutableBigInteger.offset);
        System.arraycopy(mutableBigInteger.value, mutableBigInteger.offset, iArr, (i7 + 1) - i6, iMin);
        for (int i8 = ((i7 + 1) - i6) + iMin; i8 < i7 + 1; i8++) {
            iArr[i8] = 0;
        }
        this.value = iArr;
        this.intLen = i5;
        this.offset = iArr.length - i5;
    }

    void addLower(MutableBigInteger mutableBigInteger, int i2) {
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        if (mutableBigInteger2.offset + mutableBigInteger2.intLen >= i2) {
            mutableBigInteger2.offset = (mutableBigInteger2.offset + mutableBigInteger2.intLen) - i2;
            mutableBigInteger2.intLen = i2;
        }
        mutableBigInteger2.normalize();
        add(mutableBigInteger2);
    }

    int subtract(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = this;
        int[] iArr = this.value;
        int iCompare = mutableBigInteger2.compare(mutableBigInteger);
        if (iCompare == 0) {
            reset();
            return 0;
        }
        if (iCompare < 0) {
            mutableBigInteger2 = mutableBigInteger;
            mutableBigInteger = mutableBigInteger2;
        }
        int i2 = mutableBigInteger2.intLen;
        if (iArr.length < i2) {
            iArr = new int[i2];
        }
        long j2 = 0;
        int i3 = mutableBigInteger2.intLen;
        int i4 = mutableBigInteger.intLen;
        int length = iArr.length - 1;
        while (i4 > 0) {
            i3--;
            i4--;
            j2 = ((mutableBigInteger2.value[i3 + mutableBigInteger2.offset] & 4294967295L) - (mutableBigInteger.value[i4 + mutableBigInteger.offset] & 4294967295L)) - ((int) (-(j2 >> 32)));
            int i5 = length;
            length--;
            iArr[i5] = (int) j2;
        }
        while (i3 > 0) {
            i3--;
            j2 = (mutableBigInteger2.value[i3 + mutableBigInteger2.offset] & 4294967295L) - ((int) (-(j2 >> 32)));
            int i6 = length;
            length--;
            iArr[i6] = (int) j2;
        }
        this.value = iArr;
        this.intLen = i2;
        this.offset = this.value.length - i2;
        normalize();
        return iCompare;
    }

    private int difference(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = this;
        int iCompare = mutableBigInteger2.compare(mutableBigInteger);
        if (iCompare == 0) {
            return 0;
        }
        if (iCompare < 0) {
            mutableBigInteger2 = mutableBigInteger;
            mutableBigInteger = mutableBigInteger2;
        }
        long j2 = 0;
        int i2 = mutableBigInteger2.intLen;
        int i3 = mutableBigInteger.intLen;
        while (i3 > 0) {
            i2--;
            i3--;
            j2 = ((mutableBigInteger2.value[mutableBigInteger2.offset + i2] & 4294967295L) - (mutableBigInteger.value[mutableBigInteger.offset + i3] & 4294967295L)) - ((int) (-(j2 >> 32)));
            mutableBigInteger2.value[mutableBigInteger2.offset + i2] = (int) j2;
        }
        while (i2 > 0) {
            i2--;
            j2 = (mutableBigInteger2.value[mutableBigInteger2.offset + i2] & 4294967295L) - ((int) (-(j2 >> 32)));
            mutableBigInteger2.value[mutableBigInteger2.offset + i2] = (int) j2;
        }
        mutableBigInteger2.normalize();
        return iCompare;
    }

    void multiply(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        int i2 = this.intLen;
        int i3 = mutableBigInteger.intLen;
        int i4 = i2 + i3;
        if (mutableBigInteger2.value.length < i4) {
            mutableBigInteger2.value = new int[i4];
        }
        mutableBigInteger2.offset = 0;
        mutableBigInteger2.intLen = i4;
        long j2 = 0;
        int i5 = i3 - 1;
        int i6 = (i3 + i2) - 1;
        while (i5 >= 0) {
            long j3 = ((mutableBigInteger.value[i5 + mutableBigInteger.offset] & 4294967295L) * (this.value[(i2 - 1) + this.offset] & 4294967295L)) + j2;
            mutableBigInteger2.value[i6] = (int) j3;
            j2 = j3 >>> 32;
            i5--;
            i6--;
        }
        mutableBigInteger2.value[i2 - 1] = (int) j2;
        for (int i7 = i2 - 2; i7 >= 0; i7--) {
            long j4 = 0;
            int i8 = i3 - 1;
            int i9 = i3 + i7;
            while (i8 >= 0) {
                long j5 = ((mutableBigInteger.value[i8 + mutableBigInteger.offset] & 4294967295L) * (this.value[i7 + this.offset] & 4294967295L)) + (mutableBigInteger2.value[i9] & 4294967295L) + j4;
                mutableBigInteger2.value[i9] = (int) j5;
                j4 = j5 >>> 32;
                i8--;
                i9--;
            }
            mutableBigInteger2.value[i7] = (int) j4;
        }
        mutableBigInteger2.normalize();
    }

    void mul(int i2, MutableBigInteger mutableBigInteger) {
        if (i2 == 1) {
            mutableBigInteger.copyValue(this);
            return;
        }
        if (i2 == 0) {
            mutableBigInteger.clear();
            return;
        }
        long j2 = i2 & 4294967295L;
        int[] iArr = mutableBigInteger.value.length < this.intLen + 1 ? new int[this.intLen + 1] : mutableBigInteger.value;
        long j3 = 0;
        for (int i3 = this.intLen - 1; i3 >= 0; i3--) {
            long j4 = (j2 * (this.value[i3 + this.offset] & 4294967295L)) + j3;
            iArr[i3 + 1] = (int) j4;
            j3 = j4 >>> 32;
        }
        if (j3 == 0) {
            mutableBigInteger.offset = 1;
            mutableBigInteger.intLen = this.intLen;
        } else {
            mutableBigInteger.offset = 0;
            mutableBigInteger.intLen = this.intLen + 1;
            iArr[0] = (int) j3;
        }
        mutableBigInteger.value = iArr;
    }

    int divideOneWord(int i2, MutableBigInteger mutableBigInteger) {
        int i3;
        long j2;
        long j3 = i2 & 4294967295L;
        if (this.intLen == 1) {
            long j4 = this.value[this.offset] & 4294967295L;
            int i4 = (int) (j4 / j3);
            int i5 = (int) (j4 - (i4 * j3));
            mutableBigInteger.value[0] = i4;
            mutableBigInteger.intLen = i4 == 0 ? 0 : 1;
            mutableBigInteger.offset = 0;
            return i5;
        }
        if (mutableBigInteger.value.length < this.intLen) {
            mutableBigInteger.value = new int[this.intLen];
        }
        mutableBigInteger.offset = 0;
        mutableBigInteger.intLen = this.intLen;
        int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(i2);
        int i6 = this.value[this.offset];
        long j5 = i6 & 4294967295L;
        if (j5 < j3) {
            mutableBigInteger.value[0] = 0;
        } else {
            mutableBigInteger.value[0] = (int) (j5 / j3);
            i6 = (int) (j5 - (mutableBigInteger.value[0] * j3));
            j5 = i6 & 4294967295L;
        }
        int i7 = this.intLen;
        while (true) {
            i7--;
            if (i7 <= 0) {
                break;
            }
            long j6 = (j5 << 32) | (this.value[(this.offset + this.intLen) - i7] & 4294967295L);
            if (j6 >= 0) {
                i3 = (int) (j6 / j3);
                j2 = j6 - (i3 * j3);
            } else {
                long jDivWord = divWord(j6, i2);
                i3 = (int) (jDivWord & 4294967295L);
                j2 = jDivWord >>> 32;
            }
            i6 = (int) j2;
            mutableBigInteger.value[this.intLen - i7] = i3;
            j5 = i6 & 4294967295L;
        }
        mutableBigInteger.normalize();
        if (iNumberOfLeadingZeros > 0) {
            return i6 % i2;
        }
        return i6;
    }

    MutableBigInteger divide(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        return divide(mutableBigInteger, mutableBigInteger2, true);
    }

    MutableBigInteger divide(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, boolean z2) {
        if (mutableBigInteger.intLen < 80 || this.intLen - mutableBigInteger.intLen < 40) {
            return divideKnuth(mutableBigInteger, mutableBigInteger2, z2);
        }
        return divideAndRemainderBurnikelZiegler(mutableBigInteger, mutableBigInteger2);
    }

    MutableBigInteger divideKnuth(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        return divideKnuth(mutableBigInteger, mutableBigInteger2, true);
    }

    MutableBigInteger divideKnuth(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, boolean z2) {
        int iMin;
        if (mutableBigInteger.intLen == 0) {
            throw new ArithmeticException("BigInteger divide by zero");
        }
        if (this.intLen == 0) {
            mutableBigInteger2.offset = 0;
            mutableBigInteger2.intLen = 0;
            if (z2) {
                return new MutableBigInteger();
            }
            return null;
        }
        int iCompare = compare(mutableBigInteger);
        if (iCompare < 0) {
            mutableBigInteger2.offset = 0;
            mutableBigInteger2.intLen = 0;
            if (z2) {
                return new MutableBigInteger(this);
            }
            return null;
        }
        if (iCompare == 0) {
            int[] iArr = mutableBigInteger2.value;
            mutableBigInteger2.intLen = 1;
            iArr[0] = 1;
            mutableBigInteger2.offset = 0;
            if (z2) {
                return new MutableBigInteger();
            }
            return null;
        }
        mutableBigInteger2.clear();
        if (mutableBigInteger.intLen == 1) {
            int iDivideOneWord = divideOneWord(mutableBigInteger.value[mutableBigInteger.offset], mutableBigInteger2);
            if (z2) {
                if (iDivideOneWord == 0) {
                    return new MutableBigInteger();
                }
                return new MutableBigInteger(iDivideOneWord);
            }
            return null;
        }
        if (this.intLen >= 6 && (iMin = Math.min(getLowestSetBit(), mutableBigInteger.getLowestSetBit())) >= 96) {
            MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
            MutableBigInteger mutableBigInteger4 = new MutableBigInteger(mutableBigInteger);
            mutableBigInteger3.rightShift(iMin);
            mutableBigInteger4.rightShift(iMin);
            MutableBigInteger mutableBigIntegerDivideKnuth = mutableBigInteger3.divideKnuth(mutableBigInteger4, mutableBigInteger2);
            mutableBigIntegerDivideKnuth.leftShift(iMin);
            return mutableBigIntegerDivideKnuth;
        }
        return divideMagnitude(mutableBigInteger, mutableBigInteger2, z2);
    }

    MutableBigInteger divideAndRemainderBurnikelZiegler(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        int i2 = this.intLen;
        int i3 = mutableBigInteger.intLen;
        mutableBigInteger2.intLen = 0;
        mutableBigInteger2.offset = 0;
        if (i2 < i3) {
            return this;
        }
        int iNumberOfLeadingZeros = 1 << (32 - Integer.numberOfLeadingZeros(i3 / 80));
        int i4 = (((i3 + iNumberOfLeadingZeros) - 1) / iNumberOfLeadingZeros) * iNumberOfLeadingZeros;
        long j2 = 32 * i4;
        int iMax = (int) Math.max(0L, j2 - mutableBigInteger.bitLength());
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(mutableBigInteger);
        mutableBigInteger3.safeLeftShift(iMax);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(this);
        mutableBigInteger4.safeLeftShift(iMax);
        int iBitLength = (int) ((mutableBigInteger4.bitLength() + j2) / j2);
        if (iBitLength < 2) {
            iBitLength = 2;
        }
        MutableBigInteger block = mutableBigInteger4.getBlock(iBitLength - 1, iBitLength, i4);
        MutableBigInteger block2 = mutableBigInteger4.getBlock(iBitLength - 2, iBitLength, i4);
        block2.addDisjoint(block, i4);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger();
        for (int i5 = iBitLength - 2; i5 > 0; i5--) {
            MutableBigInteger mutableBigIntegerDivide2n1n = block2.divide2n1n(mutableBigInteger3, mutableBigInteger5);
            block2 = mutableBigInteger4.getBlock(i5 - 1, iBitLength, i4);
            block2.addDisjoint(mutableBigIntegerDivide2n1n, i4);
            mutableBigInteger2.addShifted(mutableBigInteger5, i5 * i4);
        }
        MutableBigInteger mutableBigIntegerDivide2n1n2 = block2.divide2n1n(mutableBigInteger3, mutableBigInteger5);
        mutableBigInteger2.add(mutableBigInteger5);
        mutableBigIntegerDivide2n1n2.rightShift(iMax);
        return mutableBigIntegerDivide2n1n2;
    }

    private MutableBigInteger divide2n1n(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        int i2 = mutableBigInteger.intLen;
        if (i2 % 2 != 0 || i2 < 80) {
            return divideKnuth(mutableBigInteger, mutableBigInteger2);
        }
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        mutableBigInteger3.safeRightShift(32 * (i2 / 2));
        keepLower(i2 / 2);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
        addDisjoint(mutableBigInteger3.divide3n2n(mutableBigInteger, mutableBigInteger4), i2 / 2);
        MutableBigInteger mutableBigIntegerDivide3n2n = divide3n2n(mutableBigInteger, mutableBigInteger2);
        mutableBigInteger2.addDisjoint(mutableBigInteger4, i2 / 2);
        return mutableBigIntegerDivide3n2n;
    }

    private MutableBigInteger divide3n2n(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2) {
        MutableBigInteger mutableBigIntegerDivide2n1n;
        MutableBigInteger mutableBigInteger3;
        int i2 = mutableBigInteger.intLen / 2;
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(this);
        mutableBigInteger4.safeRightShift(32 * i2);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger(mutableBigInteger);
        mutableBigInteger5.safeRightShift(i2 * 32);
        BigInteger lower = mutableBigInteger.getLower(i2);
        if (compareShifted(mutableBigInteger, i2) < 0) {
            mutableBigIntegerDivide2n1n = mutableBigInteger4.divide2n1n(mutableBigInteger5, mutableBigInteger2);
            mutableBigInteger3 = new MutableBigInteger(mutableBigInteger2.toBigInteger().multiply(lower));
        } else {
            mutableBigInteger2.ones(i2);
            mutableBigInteger4.add(mutableBigInteger5);
            mutableBigInteger5.leftShift(32 * i2);
            mutableBigInteger4.subtract(mutableBigInteger5);
            mutableBigIntegerDivide2n1n = mutableBigInteger4;
            mutableBigInteger3 = new MutableBigInteger(lower);
            mutableBigInteger3.leftShift(32 * i2);
            mutableBigInteger3.subtract(new MutableBigInteger(lower));
        }
        mutableBigIntegerDivide2n1n.leftShift(32 * i2);
        mutableBigIntegerDivide2n1n.addLower(this, i2);
        while (mutableBigIntegerDivide2n1n.compare(mutableBigInteger3) < 0) {
            mutableBigIntegerDivide2n1n.add(mutableBigInteger);
            mutableBigInteger2.subtract(ONE);
        }
        mutableBigIntegerDivide2n1n.subtract(mutableBigInteger3);
        return mutableBigIntegerDivide2n1n;
    }

    private MutableBigInteger getBlock(int i2, int i3, int i4) {
        int i5;
        int i6 = i2 * i4;
        if (i6 >= this.intLen) {
            return new MutableBigInteger();
        }
        if (i2 == i3 - 1) {
            i5 = this.intLen;
        } else {
            i5 = (i2 + 1) * i4;
        }
        if (i5 > this.intLen) {
            return new MutableBigInteger();
        }
        return new MutableBigInteger(Arrays.copyOfRange(this.value, (this.offset + this.intLen) - i5, (this.offset + this.intLen) - i6));
    }

    long bitLength() {
        if (this.intLen == 0) {
            return 0L;
        }
        return (this.intLen * 32) - Integer.numberOfLeadingZeros(this.value[this.offset]);
    }

    long divide(long j2, MutableBigInteger mutableBigInteger) {
        if (j2 == 0) {
            throw new ArithmeticException("BigInteger divide by zero");
        }
        if (this.intLen == 0) {
            mutableBigInteger.offset = 0;
            mutableBigInteger.intLen = 0;
            return 0L;
        }
        if (j2 < 0) {
            j2 = -j2;
        }
        int i2 = (int) (j2 >>> 32);
        mutableBigInteger.clear();
        if (i2 == 0) {
            return divideOneWord((int) j2, mutableBigInteger) & 4294967295L;
        }
        return divideLongMagnitude(j2, mutableBigInteger).toLong();
    }

    private static void copyAndShift(int[] iArr, int i2, int i3, int[] iArr2, int i4, int i5) {
        int i6 = 32 - i5;
        int i7 = iArr[i2];
        for (int i8 = 0; i8 < i3 - 1; i8++) {
            int i9 = i7;
            i2++;
            i7 = iArr[i2];
            iArr2[i4 + i8] = (i9 << i5) | (i7 >>> i6);
        }
        iArr2[(i4 + i3) - 1] = i7 << i5;
    }

    private MutableBigInteger divideMagnitude(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, boolean z2) {
        int[] iArrCopyOfRange;
        MutableBigInteger mutableBigInteger3;
        int i2;
        int i3;
        int iMulsubBorrow;
        int i4;
        int i5;
        int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(mutableBigInteger.value[mutableBigInteger.offset]);
        int i6 = mutableBigInteger.intLen;
        if (iNumberOfLeadingZeros > 0) {
            iArrCopyOfRange = new int[i6];
            copyAndShift(mutableBigInteger.value, mutableBigInteger.offset, i6, iArrCopyOfRange, 0, iNumberOfLeadingZeros);
            if (Integer.numberOfLeadingZeros(this.value[this.offset]) >= iNumberOfLeadingZeros) {
                int[] iArr = new int[this.intLen + 1];
                mutableBigInteger3 = new MutableBigInteger(iArr);
                mutableBigInteger3.intLen = this.intLen;
                mutableBigInteger3.offset = 1;
                copyAndShift(this.value, this.offset, this.intLen, iArr, 1, iNumberOfLeadingZeros);
            } else {
                int[] iArr2 = new int[this.intLen + 2];
                mutableBigInteger3 = new MutableBigInteger(iArr2);
                mutableBigInteger3.intLen = this.intLen + 1;
                mutableBigInteger3.offset = 1;
                int i7 = this.offset;
                int i8 = 0;
                int i9 = 32 - iNumberOfLeadingZeros;
                int i10 = 1;
                while (i10 < this.intLen + 1) {
                    int i11 = i8;
                    i8 = this.value[i7];
                    iArr2[i10] = (i11 << iNumberOfLeadingZeros) | (i8 >>> i9);
                    i10++;
                    i7++;
                }
                iArr2[this.intLen + 1] = i8 << iNumberOfLeadingZeros;
            }
        } else {
            iArrCopyOfRange = Arrays.copyOfRange(mutableBigInteger.value, mutableBigInteger.offset, mutableBigInteger.offset + mutableBigInteger.intLen);
            mutableBigInteger3 = new MutableBigInteger(new int[this.intLen + 1]);
            System.arraycopy(this.value, this.offset, mutableBigInteger3.value, 1, this.intLen);
            mutableBigInteger3.intLen = this.intLen;
            mutableBigInteger3.offset = 1;
        }
        int i12 = mutableBigInteger3.intLen;
        int i13 = (i12 - i6) + 1;
        if (mutableBigInteger2.value.length < i13) {
            mutableBigInteger2.value = new int[i13];
            mutableBigInteger2.offset = 0;
        }
        mutableBigInteger2.intLen = i13;
        int[] iArr3 = mutableBigInteger2.value;
        if (mutableBigInteger3.intLen == i12) {
            mutableBigInteger3.offset = 0;
            mutableBigInteger3.value[0] = 0;
            mutableBigInteger3.intLen++;
        }
        int i14 = iArrCopyOfRange[0];
        long j2 = i14 & 4294967295L;
        int i15 = iArrCopyOfRange[1];
        for (int i16 = 0; i16 < i13 - 1; i16++) {
            boolean z3 = false;
            int i17 = mutableBigInteger3.value[i16 + mutableBigInteger3.offset];
            int i18 = i17 - 2147483648;
            int i19 = mutableBigInteger3.value[i16 + 1 + mutableBigInteger3.offset];
            if (i17 == i14) {
                i4 = -1;
                i5 = i17 + i19;
                z3 = i5 + Integer.MIN_VALUE < i18;
            } else {
                long j3 = (i17 << 32) | (i19 & 4294967295L);
                if (j3 >= 0) {
                    i4 = (int) (j3 / j2);
                    i5 = (int) (j3 - (i4 * j2));
                } else {
                    long jDivWord = divWord(j3, i14);
                    i4 = (int) (jDivWord & 4294967295L);
                    i5 = (int) (jDivWord >>> 32);
                }
            }
            if (i4 != 0) {
                if (!z3) {
                    long j4 = mutableBigInteger3.value[i16 + 2 + mutableBigInteger3.offset] & 4294967295L;
                    long j5 = ((i5 & 4294967295L) << 32) | j4;
                    long j6 = (i15 & 4294967295L) * (i4 & 4294967295L);
                    if (unsignedLongCompare(j6, j5)) {
                        i4--;
                        int i20 = (int) ((i5 & 4294967295L) + j2);
                        if ((i20 & 4294967295L) >= j2 && unsignedLongCompare(j6 - (i15 & 4294967295L), ((i20 & 4294967295L) << 32) | j4)) {
                            i4--;
                        }
                    }
                }
                mutableBigInteger3.value[i16 + mutableBigInteger3.offset] = 0;
                if (mulsub(mutableBigInteger3.value, iArrCopyOfRange, i4, i6, i16 + mutableBigInteger3.offset) - 2147483648 > i18) {
                    divadd(iArrCopyOfRange, mutableBigInteger3.value, i16 + 1 + mutableBigInteger3.offset);
                    i4--;
                }
                iArr3[i16] = i4;
            }
        }
        boolean z4 = false;
        int i21 = mutableBigInteger3.value[(i13 - 1) + mutableBigInteger3.offset];
        int i22 = i21 - 2147483648;
        int i23 = mutableBigInteger3.value[i13 + mutableBigInteger3.offset];
        if (i21 == i14) {
            i2 = -1;
            i3 = i21 + i23;
            z4 = i3 + Integer.MIN_VALUE < i22;
        } else {
            long j7 = (i21 << 32) | (i23 & 4294967295L);
            if (j7 >= 0) {
                i2 = (int) (j7 / j2);
                i3 = (int) (j7 - (i2 * j2));
            } else {
                long jDivWord2 = divWord(j7, i14);
                i2 = (int) (jDivWord2 & 4294967295L);
                i3 = (int) (jDivWord2 >>> 32);
            }
        }
        if (i2 != 0) {
            if (!z4) {
                long j8 = mutableBigInteger3.value[i13 + 1 + mutableBigInteger3.offset] & 4294967295L;
                long j9 = ((i3 & 4294967295L) << 32) | j8;
                long j10 = (i15 & 4294967295L) * (i2 & 4294967295L);
                if (unsignedLongCompare(j10, j9)) {
                    i2--;
                    int i24 = (int) ((i3 & 4294967295L) + j2);
                    if ((i24 & 4294967295L) >= j2 && unsignedLongCompare(j10 - (i15 & 4294967295L), ((i24 & 4294967295L) << 32) | j8)) {
                        i2--;
                    }
                }
            }
            mutableBigInteger3.value[(i13 - 1) + mutableBigInteger3.offset] = 0;
            if (z2) {
                iMulsubBorrow = mulsub(mutableBigInteger3.value, iArrCopyOfRange, i2, i6, (i13 - 1) + mutableBigInteger3.offset);
            } else {
                iMulsubBorrow = mulsubBorrow(mutableBigInteger3.value, iArrCopyOfRange, i2, i6, (i13 - 1) + mutableBigInteger3.offset);
            }
            if (iMulsubBorrow - 2147483648 > i22) {
                if (z2) {
                    divadd(iArrCopyOfRange, mutableBigInteger3.value, (i13 - 1) + 1 + mutableBigInteger3.offset);
                }
                i2--;
            }
            iArr3[i13 - 1] = i2;
        }
        if (z2) {
            if (iNumberOfLeadingZeros > 0) {
                mutableBigInteger3.rightShift(iNumberOfLeadingZeros);
            }
            mutableBigInteger3.normalize();
        }
        mutableBigInteger2.normalize();
        if (z2) {
            return mutableBigInteger3;
        }
        return null;
    }

    private MutableBigInteger divideLongMagnitude(long j2, MutableBigInteger mutableBigInteger) {
        int i2;
        int i3;
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(new int[this.intLen + 1]);
        System.arraycopy(this.value, this.offset, mutableBigInteger2.value, 1, this.intLen);
        mutableBigInteger2.intLen = this.intLen;
        mutableBigInteger2.offset = 1;
        int i4 = mutableBigInteger2.intLen;
        int i5 = (i4 - 2) + 1;
        if (mutableBigInteger.value.length < i5) {
            mutableBigInteger.value = new int[i5];
            mutableBigInteger.offset = 0;
        }
        mutableBigInteger.intLen = i5;
        int[] iArr = mutableBigInteger.value;
        int iNumberOfLeadingZeros = Long.numberOfLeadingZeros(j2);
        if (iNumberOfLeadingZeros > 0) {
            j2 <<= iNumberOfLeadingZeros;
            mutableBigInteger2.leftShift(iNumberOfLeadingZeros);
        }
        if (mutableBigInteger2.intLen == i4) {
            mutableBigInteger2.offset = 0;
            mutableBigInteger2.value[0] = 0;
            mutableBigInteger2.intLen++;
        }
        int i6 = (int) (j2 >>> 32);
        long j3 = i6 & 4294967295L;
        int i7 = (int) (j2 & 4294967295L);
        for (int i8 = 0; i8 < i5; i8++) {
            boolean z2 = false;
            int i9 = mutableBigInteger2.value[i8 + mutableBigInteger2.offset];
            int i10 = i9 - 2147483648;
            int i11 = mutableBigInteger2.value[i8 + 1 + mutableBigInteger2.offset];
            if (i9 == i6) {
                i2 = -1;
                i3 = i9 + i11;
                z2 = i3 + Integer.MIN_VALUE < i10;
            } else {
                long j4 = (i9 << 32) | (i11 & 4294967295L);
                if (j4 >= 0) {
                    i2 = (int) (j4 / j3);
                    i3 = (int) (j4 - (i2 * j3));
                } else {
                    long jDivWord = divWord(j4, i6);
                    i2 = (int) (jDivWord & 4294967295L);
                    i3 = (int) (jDivWord >>> 32);
                }
            }
            if (i2 != 0) {
                if (!z2) {
                    long j5 = mutableBigInteger2.value[i8 + 2 + mutableBigInteger2.offset] & 4294967295L;
                    long j6 = ((i3 & 4294967295L) << 32) | j5;
                    long j7 = (i7 & 4294967295L) * (i2 & 4294967295L);
                    if (unsignedLongCompare(j7, j6)) {
                        i2--;
                        int i12 = (int) ((i3 & 4294967295L) + j3);
                        if ((i12 & 4294967295L) >= j3 && unsignedLongCompare(j7 - (i7 & 4294967295L), ((i12 & 4294967295L) << 32) | j5)) {
                            i2--;
                        }
                    }
                }
                mutableBigInteger2.value[i8 + mutableBigInteger2.offset] = 0;
                if (mulsubLong(mutableBigInteger2.value, i6, i7, i2, i8 + mutableBigInteger2.offset) - 2147483648 > i10) {
                    divaddLong(i6, i7, mutableBigInteger2.value, i8 + 1 + mutableBigInteger2.offset);
                    i2--;
                }
                iArr[i8] = i2;
            }
        }
        if (iNumberOfLeadingZeros > 0) {
            mutableBigInteger2.rightShift(iNumberOfLeadingZeros);
        }
        mutableBigInteger.normalize();
        mutableBigInteger2.normalize();
        return mutableBigInteger2;
    }

    private int divaddLong(int i2, int i3, int[] iArr, int i4) {
        iArr[1 + i4] = (int) ((i3 & 4294967295L) + (iArr[1 + i4] & 4294967295L));
        long j2 = (i2 & 4294967295L) + (iArr[i4] & 4294967295L) + 0;
        iArr[i4] = (int) j2;
        return (int) (j2 >>> 32);
    }

    private int mulsubLong(int[] iArr, int i2, int i3, int i4, int i5) {
        long j2 = i4 & 4294967295L;
        int i6 = i5 + 2;
        long j3 = (i3 & 4294967295L) * j2;
        long j4 = iArr[i6] - j3;
        int i7 = i6 - 1;
        iArr[i6] = (int) j4;
        long j5 = ((i2 & 4294967295L) * j2) + (j3 >>> 32) + ((j4 & 4294967295L) > (((long) (((int) j3) ^ (-1))) & 4294967295L) ? 1 : 0);
        long j6 = iArr[i7] - j5;
        int i8 = i7 - 1;
        iArr[i7] = (int) j6;
        return (int) ((j5 >>> 32) + ((j6 & 4294967295L) > (((long) (((int) j5) ^ (-1))) & 4294967295L) ? 1 : 0));
    }

    private boolean unsignedLongCompare(long j2, long j3) {
        return j2 + Long.MIN_VALUE > j3 + Long.MIN_VALUE;
    }

    static long divWord(long j2, int i2) {
        long j3 = i2 & 4294967295L;
        if (j3 == 1) {
            return (0 << 32) | (((int) j2) & 4294967295L);
        }
        long j4 = (j2 >>> 1) / (j3 >>> 1);
        long j5 = j2 - (j4 * j3);
        while (j5 < 0) {
            j5 += j3;
            j4--;
        }
        while (j5 >= j3) {
            j5 -= j3;
            j4++;
        }
        return (j5 << 32) | (j4 & 4294967295L);
    }

    MutableBigInteger hybridGCD(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = this;
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
        while (mutableBigInteger.intLen != 0) {
            if (Math.abs(mutableBigInteger2.intLen - mutableBigInteger.intLen) < 2) {
                return mutableBigInteger2.binaryGCD(mutableBigInteger);
            }
            MutableBigInteger mutableBigIntegerDivide = mutableBigInteger2.divide(mutableBigInteger, mutableBigInteger3);
            mutableBigInteger2 = mutableBigInteger;
            mutableBigInteger = mutableBigIntegerDivide;
        }
        return mutableBigInteger2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ea, code lost:
    
        if (r10 <= 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ed, code lost:
    
        r6.leftShift(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f4, code lost:
    
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.math.MutableBigInteger binaryGCD(java.math.MutableBigInteger r5) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.MutableBigInteger.binaryGCD(java.math.MutableBigInteger):java.math.MutableBigInteger");
    }

    static int binaryGcd(int i2, int i3) {
        if (i3 == 0) {
            return i2;
        }
        if (i2 == 0) {
            return i3;
        }
        int iNumberOfTrailingZeros = Integer.numberOfTrailingZeros(i2);
        int iNumberOfTrailingZeros2 = Integer.numberOfTrailingZeros(i3);
        int iNumberOfTrailingZeros3 = i2 >>> iNumberOfTrailingZeros;
        int iNumberOfTrailingZeros4 = i3 >>> iNumberOfTrailingZeros2;
        int i4 = iNumberOfTrailingZeros < iNumberOfTrailingZeros2 ? iNumberOfTrailingZeros : iNumberOfTrailingZeros2;
        while (iNumberOfTrailingZeros3 != iNumberOfTrailingZeros4) {
            if (iNumberOfTrailingZeros3 - 2147483648 > iNumberOfTrailingZeros4 - 2147483648) {
                int i5 = iNumberOfTrailingZeros3 - iNumberOfTrailingZeros4;
                iNumberOfTrailingZeros3 = i5 >>> Integer.numberOfTrailingZeros(i5);
            } else {
                int i6 = iNumberOfTrailingZeros4 - iNumberOfTrailingZeros3;
                iNumberOfTrailingZeros4 = i6 >>> Integer.numberOfTrailingZeros(i6);
            }
        }
        return iNumberOfTrailingZeros3 << i4;
    }

    MutableBigInteger mutableModInverse(MutableBigInteger mutableBigInteger) {
        if (mutableBigInteger.isOdd()) {
            return modInverse(mutableBigInteger);
        }
        if (isEven()) {
            throw new ArithmeticException("BigInteger not invertible.");
        }
        int lowestSetBit = mutableBigInteger.getLowestSetBit();
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        mutableBigInteger2.rightShift(lowestSetBit);
        if (mutableBigInteger2.isOne()) {
            return modInverseMP2(lowestSetBit);
        }
        MutableBigInteger mutableBigIntegerModInverse = modInverse(mutableBigInteger2);
        MutableBigInteger mutableBigIntegerModInverseMP2 = modInverseMP2(lowestSetBit);
        MutableBigInteger mutableBigIntegerModInverseBP2 = modInverseBP2(mutableBigInteger2, lowestSetBit);
        MutableBigInteger mutableBigIntegerModInverseMP22 = mutableBigInteger2.modInverseMP2(lowestSetBit);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger();
        mutableBigIntegerModInverse.leftShift(lowestSetBit);
        mutableBigIntegerModInverse.multiply(mutableBigIntegerModInverseBP2, mutableBigInteger5);
        mutableBigIntegerModInverseMP2.multiply(mutableBigInteger2, mutableBigInteger3);
        mutableBigInteger3.multiply(mutableBigIntegerModInverseMP22, mutableBigInteger4);
        mutableBigInteger5.add(mutableBigInteger4);
        return mutableBigInteger5.divide(mutableBigInteger, mutableBigInteger3);
    }

    MutableBigInteger modInverseMP2(int i2) {
        if (isEven()) {
            throw new ArithmeticException("Non-invertible. (GCD != 1)");
        }
        if (i2 > 64) {
            return euclidModInverse(i2);
        }
        int iInverseMod32 = inverseMod32(this.value[(this.offset + this.intLen) - 1]);
        if (i2 < 33) {
            return new MutableBigInteger(i2 == 32 ? iInverseMod32 : iInverseMod32 & ((1 << i2) - 1));
        }
        long j2 = this.value[(this.offset + this.intLen) - 1] & 4294967295L;
        if (this.intLen > 1) {
            j2 |= this.value[(this.offset + this.intLen) - 2] << 32;
        }
        long j3 = iInverseMod32 & 4294967295L;
        long j4 = j3 * (2 - (j2 * j3));
        long j5 = i2 == 64 ? j4 : j4 & ((1 << i2) - 1);
        MutableBigInteger mutableBigInteger = new MutableBigInteger(new int[2]);
        mutableBigInteger.value[0] = (int) (j5 >>> 32);
        mutableBigInteger.value[1] = (int) j5;
        mutableBigInteger.intLen = 2;
        mutableBigInteger.normalize();
        return mutableBigInteger;
    }

    static int inverseMod32(int i2) {
        int i3 = i2 * (2 - (i2 * i2));
        int i4 = i3 * (2 - (i2 * i3));
        int i5 = i4 * (2 - (i2 * i4));
        return i5 * (2 - (i2 * i5));
    }

    static long inverseMod64(long j2) {
        long j3 = j2 * (2 - (j2 * j2));
        long j4 = j3 * (2 - (j2 * j3));
        long j5 = j4 * (2 - (j2 * j4));
        long j6 = j5 * (2 - (j2 * j5));
        long j7 = j6 * (2 - (j2 * j6));
        if ($assertionsDisabled || j7 * j2 == 1) {
            return j7;
        }
        throw new AssertionError();
    }

    static MutableBigInteger modInverseBP2(MutableBigInteger mutableBigInteger, int i2) {
        return fixup(new MutableBigInteger(1), new MutableBigInteger(mutableBigInteger), i2);
    }

    private MutableBigInteger modInverse(MutableBigInteger mutableBigInteger) {
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger(mutableBigInteger2);
        SignedMutableBigInteger signedMutableBigInteger = new SignedMutableBigInteger(1);
        SignedMutableBigInteger signedMutableBigInteger2 = new SignedMutableBigInteger();
        int i2 = 0;
        if (mutableBigInteger3.isEven()) {
            int lowestSetBit = mutableBigInteger3.getLowestSetBit();
            mutableBigInteger3.rightShift(lowestSetBit);
            signedMutableBigInteger2.leftShift(lowestSetBit);
            i2 = lowestSetBit;
        }
        while (!mutableBigInteger3.isOne()) {
            if (mutableBigInteger3.isZero()) {
                throw new ArithmeticException("BigInteger not invertible.");
            }
            if (mutableBigInteger3.compare(mutableBigInteger4) < 0) {
                MutableBigInteger mutableBigInteger5 = mutableBigInteger3;
                mutableBigInteger3 = mutableBigInteger4;
                mutableBigInteger4 = mutableBigInteger5;
                SignedMutableBigInteger signedMutableBigInteger3 = signedMutableBigInteger2;
                signedMutableBigInteger2 = signedMutableBigInteger;
                signedMutableBigInteger = signedMutableBigInteger3;
            }
            if (((mutableBigInteger3.value[(mutableBigInteger3.offset + mutableBigInteger3.intLen) - 1] ^ mutableBigInteger4.value[(mutableBigInteger4.offset + mutableBigInteger4.intLen) - 1]) & 3) == 0) {
                mutableBigInteger3.subtract(mutableBigInteger4);
                signedMutableBigInteger.signedSubtract(signedMutableBigInteger2);
            } else {
                mutableBigInteger3.add(mutableBigInteger4);
                signedMutableBigInteger.signedAdd(signedMutableBigInteger2);
            }
            int lowestSetBit2 = mutableBigInteger3.getLowestSetBit();
            mutableBigInteger3.rightShift(lowestSetBit2);
            signedMutableBigInteger2.leftShift(lowestSetBit2);
            i2 += lowestSetBit2;
        }
        if (signedMutableBigInteger.compare(mutableBigInteger2) >= 0) {
            signedMutableBigInteger.copyValue(signedMutableBigInteger.divide(mutableBigInteger2, new MutableBigInteger()));
        }
        if (signedMutableBigInteger.sign < 0) {
            signedMutableBigInteger.signedAdd(mutableBigInteger2);
        }
        return fixup(signedMutableBigInteger, mutableBigInteger2, i2);
    }

    static MutableBigInteger fixup(MutableBigInteger mutableBigInteger, MutableBigInteger mutableBigInteger2, int i2) {
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger();
        int i3 = -inverseMod32(mutableBigInteger2.value[(mutableBigInteger2.offset + mutableBigInteger2.intLen) - 1]);
        int i4 = i2 >> 5;
        for (int i5 = 0; i5 < i4; i5++) {
            mutableBigInteger2.mul(i3 * mutableBigInteger.value[(mutableBigInteger.offset + mutableBigInteger.intLen) - 1], mutableBigInteger3);
            mutableBigInteger.add(mutableBigInteger3);
            mutableBigInteger.intLen--;
        }
        int i6 = i2 & 31;
        if (i6 != 0) {
            mutableBigInteger2.mul((i3 * mutableBigInteger.value[(mutableBigInteger.offset + mutableBigInteger.intLen) - 1]) & ((1 << i6) - 1), mutableBigInteger3);
            mutableBigInteger.add(mutableBigInteger3);
            mutableBigInteger.rightShift(i6);
        }
        if (mutableBigInteger.compare(mutableBigInteger2) >= 0) {
            mutableBigInteger = mutableBigInteger.divide(mutableBigInteger2, new MutableBigInteger());
        }
        return mutableBigInteger;
    }

    MutableBigInteger euclidModInverse(int i2) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(1);
        mutableBigInteger.leftShift(i2);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(mutableBigInteger);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(this);
        MutableBigInteger mutableBigInteger4 = new MutableBigInteger();
        MutableBigInteger mutableBigIntegerDivide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger4);
        MutableBigInteger mutableBigInteger5 = new MutableBigInteger(mutableBigInteger4);
        MutableBigInteger mutableBigInteger6 = new MutableBigInteger(1);
        MutableBigInteger mutableBigInteger7 = new MutableBigInteger();
        while (!mutableBigIntegerDivide.isOne()) {
            MutableBigInteger mutableBigIntegerDivide2 = mutableBigInteger3.divide(mutableBigIntegerDivide, mutableBigInteger4);
            if (mutableBigIntegerDivide2.intLen == 0) {
                throw new ArithmeticException("BigInteger not invertible.");
            }
            mutableBigInteger3 = mutableBigIntegerDivide2;
            if (mutableBigInteger4.intLen == 1) {
                mutableBigInteger5.mul(mutableBigInteger4.value[mutableBigInteger4.offset], mutableBigInteger7);
            } else {
                mutableBigInteger4.multiply(mutableBigInteger5, mutableBigInteger7);
            }
            MutableBigInteger mutableBigInteger8 = mutableBigInteger4;
            MutableBigInteger mutableBigInteger9 = mutableBigInteger7;
            mutableBigInteger6.add(mutableBigInteger9);
            if (mutableBigInteger3.isOne()) {
                return mutableBigInteger6;
            }
            MutableBigInteger mutableBigIntegerDivide3 = mutableBigIntegerDivide.divide(mutableBigInteger3, mutableBigInteger9);
            if (mutableBigIntegerDivide3.intLen == 0) {
                throw new ArithmeticException("BigInteger not invertible.");
            }
            mutableBigIntegerDivide = mutableBigIntegerDivide3;
            if (mutableBigInteger9.intLen == 1) {
                mutableBigInteger6.mul(mutableBigInteger9.value[mutableBigInteger9.offset], mutableBigInteger8);
            } else {
                mutableBigInteger9.multiply(mutableBigInteger6, mutableBigInteger8);
            }
            mutableBigInteger4 = mutableBigInteger8;
            mutableBigInteger7 = mutableBigInteger9;
            mutableBigInteger5.add(mutableBigInteger4);
        }
        mutableBigInteger2.subtract(mutableBigInteger5);
        return mutableBigInteger2;
    }
}
