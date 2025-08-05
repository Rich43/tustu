package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Histogram;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Coding.class */
class Coding implements Comparable<Coding>, CodingMethod, Histogram.BitMetric {
    public static final int B_MAX = 5;
    public static final int H_MAX = 256;
    public static final int S_MAX = 2;

    /* renamed from: B, reason: collision with root package name */
    private final int f11844B;

    /* renamed from: H, reason: collision with root package name */
    private final int f11845H;

    /* renamed from: L, reason: collision with root package name */
    private final int f11846L;

    /* renamed from: S, reason: collision with root package name */
    private final int f11847S;
    private final int del;
    private final int min;
    private final int max;
    private final int umin;
    private final int umax;
    private final int[] byteMin;
    private final int[] byteMax;
    private static Map<Coding, Coding> codeMap;
    private static final byte[] byteBitWidths;
    static boolean verboseStringForDebug;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Coding.class.desiredAssertionStatus();
        byteBitWidths = new byte[256];
        for (int i2 = 0; i2 < byteBitWidths.length; i2++) {
            byteBitWidths[i2] = (byte) ceil_lg2(i2 + 1);
        }
        int i3 = 10;
        while (true) {
            int i4 = i3;
            if (i4 >= 0) {
                if (!$assertionsDisabled && bitWidth(i4) != ceil_lg2(i4 + 1)) {
                    throw new AssertionError();
                }
                i3 = (i4 << 1) - (i4 >> 3);
            } else {
                verboseStringForDebug = false;
                return;
            }
        }
    }

    private static int saturate32(long j2) {
        if (j2 > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j2 < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j2;
    }

    private static long codeRangeLong(int i2, int i3) {
        return codeRangeLong(i2, i3, i2);
    }

    private static long codeRangeLong(int i2, int i3, int i4) {
        if (!$assertionsDisabled && (i4 < 0 || i4 > i2)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (i2 < 1 || i2 > 5)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (i3 < 1 || i3 > 256)) {
            throw new AssertionError();
        }
        if (i4 == 0) {
            return 0L;
        }
        if (i2 == 1) {
            return i3;
        }
        int i5 = 256 - i3;
        long j2 = 0;
        long j3 = 1;
        for (int i6 = 1; i6 <= i4; i6++) {
            j2 += j3;
            j3 *= i3;
        }
        long j4 = j2 * i5;
        if (i4 == i2) {
            j4 += j3;
        }
        return j4;
    }

    public static int codeMax(int i2, int i3, int i4, int i5) {
        long j2;
        long jCodeRangeLong = codeRangeLong(i2, i3, i5);
        if (jCodeRangeLong == 0) {
            return -1;
        }
        if (i4 == 0 || jCodeRangeLong >= EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH) {
            return saturate32(jCodeRangeLong - 1);
        }
        long j3 = jCodeRangeLong;
        while (true) {
            j2 = j3 - 1;
            if (!isNegativeCode(j2, i4)) {
                break;
            }
            j3 = j2;
        }
        if (j2 < 0) {
            return -1;
        }
        int iDecodeSign32 = decodeSign32(j2, i4);
        if (iDecodeSign32 < 0) {
            return Integer.MAX_VALUE;
        }
        return iDecodeSign32;
    }

    public static int codeMin(int i2, int i3, int i4, int i5) {
        long j2;
        long jCodeRangeLong = codeRangeLong(i2, i3, i5);
        if (jCodeRangeLong >= EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH && i5 == i2) {
            return Integer.MIN_VALUE;
        }
        if (i4 == 0) {
            return 0;
        }
        long j3 = jCodeRangeLong;
        while (true) {
            j2 = j3 - 1;
            if (isNegativeCode(j2, i4)) {
                break;
            }
            j3 = j2;
        }
        if (j2 < 0) {
            return 0;
        }
        return decodeSign32(j2, i4);
    }

    private static long toUnsigned32(int i2) {
        return (i2 << 32) >>> 32;
    }

    private static boolean isNegativeCode(long j2, int i2) {
        if (!$assertionsDisabled && i2 <= 0) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || j2 >= -1) {
            return ((((int) j2) + 1) & ((1 << i2) - 1)) == 0;
        }
        throw new AssertionError();
    }

    private static boolean hasNegativeCode(int i2, int i3) {
        if ($assertionsDisabled || i3 > 0) {
            return 0 > i2 && i2 >= (((-1) >>> i3) ^ (-1));
        }
        throw new AssertionError();
    }

    private static int decodeSign32(long j2, int i2) {
        int i3;
        if (!$assertionsDisabled && j2 != toUnsigned32((int) j2)) {
            throw new AssertionError((Object) Long.toHexString(j2));
        }
        if (i2 == 0) {
            return (int) j2;
        }
        if (isNegativeCode(j2, i2)) {
            i3 = (((int) j2) >>> i2) ^ (-1);
        } else {
            i3 = ((int) j2) - (((int) j2) >>> i2);
        }
        if ($assertionsDisabled || i2 != 1 || i3 == ((((int) j2) >>> 1) ^ (-(((int) j2) & 1)))) {
            return i3;
        }
        throw new AssertionError();
    }

    private static long encodeSign32(int i2, int i3) {
        long unsigned32;
        if (i3 == 0) {
            return toUnsigned32(i2);
        }
        int i4 = (1 << i3) - 1;
        if (!hasNegativeCode(i2, i3)) {
            unsigned32 = i2 + (toUnsigned32(i2) / i4);
        } else {
            unsigned32 = ((-i2) << i3) - 1;
        }
        long unsigned322 = toUnsigned32((int) unsigned32);
        if (!$assertionsDisabled && i2 != decodeSign32(unsigned322, i3)) {
            throw new AssertionError((Object) (Long.toHexString(unsigned322) + " -> " + Integer.toHexString(i2) + " != " + Integer.toHexString(decodeSign32(unsigned322, i3))));
        }
        return unsigned322;
    }

    public static void writeInt(byte[] bArr, int[] iArr, int i2, int i3, int i4, int i5) {
        long jEncodeSign32 = encodeSign32(i2, i5);
        if (!$assertionsDisabled && jEncodeSign32 != toUnsigned32((int) jEncodeSign32)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && jEncodeSign32 >= codeRangeLong(i3, i4)) {
            throw new AssertionError((Object) Long.toHexString(jEncodeSign32));
        }
        int i6 = 256 - i4;
        long j2 = jEncodeSign32;
        int i7 = iArr[0];
        for (int i8 = 0; i8 < i3 - 1 && j2 >= i6; i8++) {
            j2 = (j2 - i6) / i4;
            int i9 = i7;
            i7++;
            bArr[i9] = (byte) (i6 + (r0 % i4));
        }
        bArr[i7] = (byte) j2;
        iArr[0] = i7 + 1;
    }

    public static int readInt(byte[] bArr, int[] iArr, int i2, int i3, int i4) {
        int i5 = 256 - i3;
        long j2 = 0;
        long j3 = 1;
        int i6 = iArr[0];
        for (int i7 = 0; i7 < i2; i7++) {
            int i8 = i6;
            i6++;
            int i9 = bArr[i8] & 255;
            j2 += i9 * j3;
            j3 *= i3;
            if (i9 < i5) {
                break;
            }
        }
        iArr[0] = i6;
        return decodeSign32(j2, i4);
    }

    public static int readIntFrom(InputStream inputStream, int i2, int i3, int i4) throws IOException {
        int i5 = 256 - i3;
        long j2 = 0;
        long j3 = 1;
        for (int i6 = 0; i6 < i2; i6++) {
            int i7 = inputStream.read();
            if (i7 < 0) {
                throw new RuntimeException("unexpected EOF");
            }
            j2 += i7 * j3;
            j3 *= i3;
            if (i7 < i5) {
                break;
            }
        }
        if ($assertionsDisabled || (j2 >= 0 && j2 < codeRangeLong(i2, i3))) {
            return decodeSign32(j2, i4);
        }
        throw new AssertionError();
    }

    private Coding(int i2, int i3, int i4) {
        this(i2, i3, i4, 0);
    }

    private Coding(int i2, int i3, int i4, int i5) {
        this.f11844B = i2;
        this.f11845H = i3;
        this.f11846L = 256 - i3;
        this.f11847S = i4;
        this.del = i5;
        this.min = codeMin(i2, i3, i4, i2);
        this.max = codeMax(i2, i3, i4, i2);
        this.umin = codeMin(i2, i3, 0, i2);
        this.umax = codeMax(i2, i3, 0, i2);
        this.byteMin = new int[i2];
        this.byteMax = new int[i2];
        for (int i6 = 1; i6 <= i2; i6++) {
            this.byteMin[i6 - 1] = codeMin(i2, i3, i4, i6);
            this.byteMax[i6 - 1] = codeMax(i2, i3, i4, i6);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Coding)) {
            return false;
        }
        Coding coding = (Coding) obj;
        return this.f11844B == coding.f11844B && this.f11845H == coding.f11845H && this.f11847S == coding.f11847S && this.del == coding.del;
    }

    public int hashCode() {
        return (this.del << 14) + (this.f11847S << 11) + (this.f11844B << 8) + (this.f11845H << 0);
    }

    private static synchronized Coding of(int i2, int i3, int i4, int i5) {
        if (codeMap == null) {
            codeMap = new HashMap();
        }
        Coding coding = new Coding(i2, i3, i4, i5);
        Coding coding2 = codeMap.get(coding);
        if (coding2 == null) {
            coding2 = coding;
            codeMap.put(coding, coding);
        }
        return coding2;
    }

    public static Coding of(int i2, int i3) {
        return of(i2, i3, 0, 0);
    }

    public static Coding of(int i2, int i3, int i4) {
        return of(i2, i3, i4, 0);
    }

    public boolean canRepresentValue(int i2) {
        if (isSubrange()) {
            return canRepresentUnsigned(i2);
        }
        return canRepresentSigned(i2);
    }

    public boolean canRepresentSigned(int i2) {
        return i2 >= this.min && i2 <= this.max;
    }

    public boolean canRepresentUnsigned(int i2) {
        return i2 >= this.umin && i2 <= this.umax;
    }

    public int readFrom(byte[] bArr, int[] iArr) {
        return readInt(bArr, iArr, this.f11844B, this.f11845H, this.f11847S);
    }

    public void writeTo(byte[] bArr, int[] iArr, int i2) {
        writeInt(bArr, iArr, i2, this.f11844B, this.f11845H, this.f11847S);
    }

    public int readFrom(InputStream inputStream) throws IOException {
        return readIntFrom(inputStream, this.f11844B, this.f11845H, this.f11847S);
    }

    public void writeTo(OutputStream outputStream, int i2) throws IOException {
        byte[] bArr = new byte[this.f11844B];
        int[] iArr = new int[1];
        writeInt(bArr, iArr, i2, this.f11844B, this.f11845H, this.f11847S);
        outputStream.write(bArr, 0, iArr[0]);
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void readArrayFrom(InputStream inputStream, int[] iArr, int i2, int i3) throws IOException {
        for (int i4 = i2; i4 < i3; i4++) {
            iArr[i4] = readFrom(inputStream);
        }
        for (int i5 = 0; i5 < this.del; i5++) {
            long jReduceToUnsignedRange = 0;
            for (int i6 = i2; i6 < i3; i6++) {
                jReduceToUnsignedRange += iArr[i6];
                if (isSubrange()) {
                    jReduceToUnsignedRange = reduceToUnsignedRange(jReduceToUnsignedRange);
                }
                iArr[i6] = (int) jReduceToUnsignedRange;
            }
        }
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public void writeArrayTo(OutputStream outputStream, int[] iArr, int i2, int i3) throws IOException {
        int[] iArrMakeDeltas;
        if (i3 <= i2) {
            return;
        }
        for (int i4 = 0; i4 < this.del; i4++) {
            if (!isSubrange()) {
                iArrMakeDeltas = makeDeltas(iArr, i2, i3, 0, 0);
            } else {
                iArrMakeDeltas = makeDeltas(iArr, i2, i3, this.min, this.max);
            }
            int[] iArr2 = iArrMakeDeltas;
            iArr = iArr2;
            i2 = 0;
            i3 = iArr2.length;
        }
        byte[] bArr = new byte[256];
        int length = bArr.length - this.f11844B;
        int[] iArr3 = {0};
        int i5 = i2;
        while (i5 < i3) {
            while (iArr3[0] <= length) {
                int i6 = i5;
                i5++;
                writeTo(bArr, iArr3, iArr[i6]);
                if (i5 >= i3) {
                    break;
                }
            }
            outputStream.write(bArr, 0, iArr3[0]);
            iArr3[0] = 0;
        }
    }

    boolean isSubrange() {
        return this.max < Integer.MAX_VALUE && (((long) this.max) - ((long) this.min)) + 1 <= 2147483647L;
    }

    boolean isFullRange() {
        return this.max == Integer.MAX_VALUE && this.min == Integer.MIN_VALUE;
    }

    int getRange() {
        if ($assertionsDisabled || isSubrange()) {
            return (this.max - this.min) + 1;
        }
        throw new AssertionError();
    }

    Coding setB(int i2) {
        return of(i2, this.f11845H, this.f11847S, this.del);
    }

    Coding setH(int i2) {
        return of(this.f11844B, i2, this.f11847S, this.del);
    }

    Coding setS(int i2) {
        return of(this.f11844B, this.f11845H, i2, this.del);
    }

    Coding setL(int i2) {
        return setH(256 - i2);
    }

    Coding setD(int i2) {
        return of(this.f11844B, this.f11845H, this.f11847S, i2);
    }

    Coding getDeltaCoding() {
        return setD(this.del + 1);
    }

    Coding getValueCoding() {
        if (isDelta()) {
            return of(this.f11844B, this.f11845H, 0, this.del - 1);
        }
        return this;
    }

    int reduceToUnsignedRange(long j2) {
        if (j2 == ((int) j2) && canRepresentUnsigned((int) j2)) {
            return (int) j2;
        }
        int range = getRange();
        if (!$assertionsDisabled && range <= 0) {
            throw new AssertionError();
        }
        long j3 = j2 % range;
        if (j3 < 0) {
            j3 += range;
        }
        if ($assertionsDisabled || canRepresentUnsigned((int) j3)) {
            return (int) j3;
        }
        throw new AssertionError();
    }

    int reduceToSignedRange(int i2) {
        if (canRepresentSigned(i2)) {
            return i2;
        }
        return reduceToSignedRange(i2, this.min, this.max);
    }

    static int reduceToSignedRange(int i2, int i3, int i4) {
        int i5 = (i4 - i3) + 1;
        if (!$assertionsDisabled && i5 <= 0) {
            throw new AssertionError();
        }
        int i6 = i2 - i3;
        if (i6 < 0 && i2 >= 0) {
            i6 -= i5;
            if (!$assertionsDisabled && i6 < 0) {
                throw new AssertionError();
            }
        }
        int i7 = i6 % i5;
        if (i7 < 0) {
            i7 += i5;
        }
        int i8 = i7 + i3;
        if ($assertionsDisabled || (i3 <= i8 && i8 <= i4)) {
            return i8;
        }
        throw new AssertionError();
    }

    boolean isSigned() {
        return this.min < 0;
    }

    boolean isDelta() {
        return this.del != 0;
    }

    public int B() {
        return this.f11844B;
    }

    public int H() {
        return this.f11845H;
    }

    public int L() {
        return this.f11846L;
    }

    public int S() {
        return this.f11847S;
    }

    public int del() {
        return this.del;
    }

    public int min() {
        return this.min;
    }

    public int max() {
        return this.max;
    }

    public int umin() {
        return this.umin;
    }

    public int umax() {
        return this.umax;
    }

    public int byteMin(int i2) {
        return this.byteMin[i2 - 1];
    }

    public int byteMax(int i2) {
        return this.byteMax[i2 - 1];
    }

    @Override // java.lang.Comparable
    public int compareTo(Coding coding) {
        int i2 = this.del - coding.del;
        if (i2 == 0) {
            i2 = this.f11844B - coding.f11844B;
        }
        if (i2 == 0) {
            i2 = this.f11845H - coding.f11845H;
        }
        if (i2 == 0) {
            i2 = this.f11847S - coding.f11847S;
        }
        return i2;
    }

    public int distanceFrom(Coding coding) {
        int iCeil_lg2;
        int i2 = this.del - coding.del;
        if (i2 < 0) {
            i2 = -i2;
        }
        int i3 = this.f11847S - coding.f11847S;
        if (i3 < 0) {
            i3 = -i3;
        }
        int i4 = this.f11844B - coding.f11844B;
        if (i4 < 0) {
            i4 = -i4;
        }
        if (this.f11845H == coding.f11845H) {
            iCeil_lg2 = 0;
        } else {
            int hl = getHL();
            int hl2 = coding.getHL();
            int i5 = hl * hl;
            int i6 = hl2 * hl2;
            if (i5 > i6) {
                iCeil_lg2 = ceil_lg2(1 + ((i5 - 1) / i6));
            } else {
                iCeil_lg2 = ceil_lg2(1 + ((i6 - 1) / i5));
            }
        }
        int i7 = (5 * (i2 + i3 + i4)) + iCeil_lg2;
        if ($assertionsDisabled || i7 != 0 || compareTo(coding) == 0) {
            return i7;
        }
        throw new AssertionError();
    }

    private int getHL() {
        if (this.f11845H <= 128) {
            return this.f11845H;
        }
        if (this.f11846L >= 1) {
            return 16384 / this.f11846L;
        }
        return 32768;
    }

    static int ceil_lg2(int i2) {
        if (!$assertionsDisabled && i2 - 1 < 0) {
            throw new AssertionError();
        }
        int i3 = 0;
        for (int i4 = i2 - 1; i4 != 0; i4 >>= 1) {
            i3++;
        }
        return i3;
    }

    static int bitWidth(int i2) {
        if (i2 < 0) {
            i2 ^= -1;
        }
        int i3 = 0;
        int i4 = i2;
        if (i4 < byteBitWidths.length) {
            return byteBitWidths[i4];
        }
        int i5 = i4 >>> 16;
        if (i5 != 0) {
            i4 = i5;
            i3 = 0 + 16;
        }
        int i6 = i4 >>> 8;
        if (i6 != 0) {
            i4 = i6;
            i3 += 8;
        }
        return i3 + byteBitWidths[i4];
    }

    static int[] makeDeltas(int[] iArr, int i2, int i3, int i4, int i5) {
        if (!$assertionsDisabled && i5 < i4) {
            throw new AssertionError();
        }
        int i6 = i3 - i2;
        int[] iArr2 = new int[i6];
        int i7 = 0;
        if (i4 == i5) {
            for (int i8 = 0; i8 < i6; i8++) {
                int i9 = iArr[i2 + i8];
                iArr2[i8] = i9 - i7;
                i7 = i9;
            }
        } else {
            for (int i10 = 0; i10 < i6; i10++) {
                int i11 = iArr[i2 + i10];
                if (!$assertionsDisabled && (i11 < 0 || i11 + i4 > i5)) {
                    throw new AssertionError();
                }
                int i12 = i11 - i7;
                if (!$assertionsDisabled && i12 != i11 - i7) {
                    throw new AssertionError();
                }
                i7 = i11;
                iArr2[i10] = reduceToSignedRange(i12, i4, i5);
            }
        }
        return iArr2;
    }

    boolean canRepresent(int i2, int i3) {
        if (!$assertionsDisabled && i2 > i3) {
            throw new AssertionError();
        }
        if (this.del <= 0) {
            return canRepresentSigned(i3) && canRepresentSigned(i2);
        }
        if (isSubrange()) {
            return canRepresentUnsigned(i3) && canRepresentUnsigned(i2);
        }
        return isFullRange();
    }

    boolean canRepresent(int[] iArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 == 0 || isFullRange()) {
            return true;
        }
        int i5 = iArr[i2];
        int i6 = i5;
        for (int i7 = 1; i7 < i4; i7++) {
            int i8 = iArr[i2 + i7];
            if (i5 < i8) {
                i5 = i8;
            }
            if (i6 > i8) {
                i6 = i8;
            }
        }
        return canRepresent(i6, i5);
    }

    @Override // com.sun.java.util.jar.pack.Histogram.BitMetric
    public double getBitLength(int i2) {
        return getLength(i2) * 8.0d;
    }

    public int getLength(int i2) {
        if (isDelta() && isSubrange()) {
            if (!canRepresentUnsigned(i2)) {
                return Integer.MAX_VALUE;
            }
            i2 = reduceToSignedRange(i2);
        }
        if (i2 >= 0) {
            for (int i3 = 0; i3 < this.f11844B; i3++) {
                if (i2 <= this.byteMax[i3]) {
                    return i3 + 1;
                }
            }
            return Integer.MAX_VALUE;
        }
        for (int i4 = 0; i4 < this.f11844B; i4++) {
            if (i2 >= this.byteMin[i4]) {
                return i4 + 1;
            }
        }
        return Integer.MAX_VALUE;
    }

    public int getLength(int[] iArr, int i2, int i3) {
        int[] iArrMakeDeltas;
        int i4 = i3 - i2;
        if (this.f11844B == 1) {
            return i4;
        }
        if (this.f11846L == 0) {
            return i4 * this.f11844B;
        }
        if (isDelta()) {
            if (!isSubrange()) {
                iArrMakeDeltas = makeDeltas(iArr, i2, i3, 0, 0);
            } else {
                iArrMakeDeltas = makeDeltas(iArr, i2, i3, this.min, this.max);
            }
            iArr = iArrMakeDeltas;
            i2 = 0;
        }
        int i5 = i4;
        for (int i6 = 1; i6 <= this.f11844B; i6++) {
            int i7 = this.byteMax[i6 - 1];
            int i8 = this.byteMin[i6 - 1];
            int i9 = 0;
            for (int i10 = 0; i10 < i4; i10++) {
                int i11 = iArr[i2 + i10];
                if (i11 >= 0) {
                    if (i11 > i7) {
                        i9++;
                    }
                } else if (i11 < i8) {
                    i9++;
                }
            }
            if (i9 == 0) {
                break;
            }
            if (i6 == this.f11844B) {
                return Integer.MAX_VALUE;
            }
            i5 += i9;
        }
        return i5;
    }

    @Override // com.sun.java.util.jar.pack.CodingMethod
    public byte[] getMetaCoding(Coding coding) {
        if (coding == this) {
            return new byte[]{0};
        }
        int iIndexOf = BandStructure.indexOf(this);
        if (iIndexOf > 0) {
            return new byte[]{(byte) iIndexOf};
        }
        return new byte[]{116, (byte) (this.del + (2 * this.f11847S) + (8 * (this.f11844B - 1))), (byte) (this.f11845H - 1)};
    }

    public static int parseMetaCoding(byte[] bArr, int i2, Coding coding, CodingMethod[] codingMethodArr) {
        int i3 = i2 + 1;
        int i4 = bArr[i2] & 255;
        if (1 <= i4 && i4 <= 115) {
            Coding codingCodingForIndex = BandStructure.codingForIndex(i4);
            if (!$assertionsDisabled && codingCodingForIndex == null) {
                throw new AssertionError();
            }
            codingMethodArr[0] = codingCodingForIndex;
            return i3;
        }
        if (i4 == 116) {
            int i5 = i3 + 1;
            int i6 = bArr[i3] & 255;
            int i7 = i5 + 1;
            int i8 = i6 % 2;
            int i9 = (i6 / 2) % 4;
            int i10 = (i6 / 8) + 1;
            int i11 = (bArr[i5] & 255) + 1;
            if (1 > i10 || i10 > 5 || 0 > i9 || i9 > 2 || 1 > i11 || i11 > 256 || 0 > i8 || i8 > 1 || ((i10 == 1 && i11 != 256) || (i10 == 5 && i11 == 256))) {
                throw new RuntimeException("Bad arb. coding: (" + i10 + "," + i11 + "," + i9 + "," + i8);
            }
            codingMethodArr[0] = of(i10, i11, i9, i8);
            return i7;
        }
        return i3 - 1;
    }

    public String keyString() {
        return "(" + this.f11844B + "," + this.f11845H + "," + this.f11847S + "," + this.del + ")";
    }

    public String toString() {
        return "Coding" + keyString();
    }

    String stringForDebug() {
        String str = keyString() + " L=" + this.f11846L + " r=[" + (this.min == Integer.MIN_VALUE ? "min" : "" + this.min) + "," + (this.max == Integer.MAX_VALUE ? "max" : "" + this.max) + "]";
        if (isSubrange()) {
            str = str + " subrange";
        } else if (!isFullRange()) {
            str = str + " MIDRANGE";
        }
        if (verboseStringForDebug) {
            String str2 = str + " {";
            int i2 = 0;
            for (int i3 = 1; i3 <= this.f11844B; i3++) {
                int iSaturate32 = saturate32((this.byteMax[i3 - 1] - this.byteMin[i3 - 1]) + 1);
                if (!$assertionsDisabled && iSaturate32 != saturate32(codeRangeLong(this.f11844B, this.f11845H, i3))) {
                    throw new AssertionError();
                }
                int i4 = iSaturate32 - i2;
                i2 = i4;
                str2 = str2 + " #" + i3 + "=" + (i4 == Integer.MAX_VALUE ? "max" : "" + i4);
            }
            str = str2 + " }";
        }
        return str;
    }
}
