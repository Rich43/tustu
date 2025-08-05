package sun.misc;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/misc/FloatingDecimal.class */
public class FloatingDecimal {
    static final int EXP_SHIFT = 52;
    static final long FRACT_HOB = 4503599627370496L;
    static final long EXP_ONE = 4607182418800017408L;
    static final int MAX_SMALL_BIN_EXP = 62;
    static final int MIN_SMALL_BIN_EXP = -21;
    static final int MAX_DECIMAL_DIGITS = 15;
    static final int MAX_DECIMAL_EXPONENT = 308;
    static final int MIN_DECIMAL_EXPONENT = -324;
    static final int BIG_DECIMAL_EXPONENT = 324;
    static final int MAX_NDIGITS = 1100;
    static final int SINGLE_EXP_SHIFT = 23;
    static final int SINGLE_FRACT_HOB = 8388608;
    static final int SINGLE_MAX_DECIMAL_DIGITS = 7;
    static final int SINGLE_MAX_DECIMAL_EXPONENT = 38;
    static final int SINGLE_MIN_DECIMAL_EXPONENT = -45;
    static final int SINGLE_MAX_NDIGITS = 200;
    static final int INT_DECIMAL_DIGITS = 9;
    private static final String INFINITY_REP = "Infinity";
    private static final int INFINITY_LENGTH;
    private static final String NAN_REP = "NaN";
    private static final int NAN_LENGTH;
    private static final BinaryToASCIIConverter B2AC_POSITIVE_INFINITY;
    private static final BinaryToASCIIConverter B2AC_NEGATIVE_INFINITY;
    private static final BinaryToASCIIConverter B2AC_NOT_A_NUMBER;
    private static final BinaryToASCIIConverter B2AC_POSITIVE_ZERO;
    private static final BinaryToASCIIConverter B2AC_NEGATIVE_ZERO;
    private static final ThreadLocal<BinaryToASCIIBuffer> threadLocalBinaryToASCIIBuffer;
    static final ASCIIToBinaryConverter A2BC_POSITIVE_INFINITY;
    static final ASCIIToBinaryConverter A2BC_NEGATIVE_INFINITY;
    static final ASCIIToBinaryConverter A2BC_NOT_A_NUMBER;
    static final ASCIIToBinaryConverter A2BC_POSITIVE_ZERO;
    static final ASCIIToBinaryConverter A2BC_NEGATIVE_ZERO;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$ASCIIToBinaryConverter.class */
    interface ASCIIToBinaryConverter {
        double doubleValue();

        float floatValue();
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$BinaryToASCIIConverter.class */
    public interface BinaryToASCIIConverter {
        String toJavaFormatString();

        void appendTo(Appendable appendable);

        int getDecimalExponent();

        int getDigits(char[] cArr);

        boolean isNegative();

        boolean isExceptional();

        boolean digitsRoundedUp();

        boolean decimalDigitsExact();
    }

    static {
        $assertionsDisabled = !FloatingDecimal.class.desiredAssertionStatus();
        INFINITY_LENGTH = "Infinity".length();
        NAN_LENGTH = "NaN".length();
        B2AC_POSITIVE_INFINITY = new ExceptionalBinaryToASCIIBuffer("Infinity", false);
        B2AC_NEGATIVE_INFINITY = new ExceptionalBinaryToASCIIBuffer("-Infinity", true);
        B2AC_NOT_A_NUMBER = new ExceptionalBinaryToASCIIBuffer("NaN", false);
        B2AC_POSITIVE_ZERO = new BinaryToASCIIBuffer(false, new char[]{'0'});
        B2AC_NEGATIVE_ZERO = new BinaryToASCIIBuffer(true, new char[]{'0'});
        threadLocalBinaryToASCIIBuffer = new ThreadLocal<BinaryToASCIIBuffer>() { // from class: sun.misc.FloatingDecimal.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public BinaryToASCIIBuffer initialValue() {
                return new BinaryToASCIIBuffer();
            }
        };
        A2BC_POSITIVE_INFINITY = new PreparedASCIIToBinaryBuffer(Double.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        A2BC_NEGATIVE_INFINITY = new PreparedASCIIToBinaryBuffer(Double.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        A2BC_NOT_A_NUMBER = new PreparedASCIIToBinaryBuffer(Double.NaN, Float.NaN);
        A2BC_POSITIVE_ZERO = new PreparedASCIIToBinaryBuffer(0.0d, 0.0f);
        A2BC_NEGATIVE_ZERO = new PreparedASCIIToBinaryBuffer(-0.0d, -0.0f);
    }

    public static String toJavaFormatString(double d2) {
        return getBinaryToASCIIConverter(d2).toJavaFormatString();
    }

    public static String toJavaFormatString(float f2) {
        return getBinaryToASCIIConverter(f2).toJavaFormatString();
    }

    public static void appendTo(double d2, Appendable appendable) {
        getBinaryToASCIIConverter(d2).appendTo(appendable);
    }

    public static void appendTo(float f2, Appendable appendable) {
        getBinaryToASCIIConverter(f2).appendTo(appendable);
    }

    public static double parseDouble(String str) throws NumberFormatException {
        return readJavaFormatString(str).doubleValue();
    }

    public static float parseFloat(String str) throws NumberFormatException {
        return readJavaFormatString(str).floatValue();
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$ExceptionalBinaryToASCIIBuffer.class */
    private static class ExceptionalBinaryToASCIIBuffer implements BinaryToASCIIConverter {
        private final String image;
        private boolean isNegative;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FloatingDecimal.class.desiredAssertionStatus();
        }

        public ExceptionalBinaryToASCIIBuffer(String str, boolean z2) {
            this.image = str;
            this.isNegative = z2;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public String toJavaFormatString() {
            return this.image;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public void appendTo(Appendable appendable) {
            if (appendable instanceof StringBuilder) {
                ((StringBuilder) appendable).append(this.image);
            } else if (appendable instanceof StringBuffer) {
                ((StringBuffer) appendable).append(this.image);
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public int getDecimalExponent() {
            throw new IllegalArgumentException("Exceptional value does not have an exponent");
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public int getDigits(char[] cArr) {
            throw new IllegalArgumentException("Exceptional value does not have digits");
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean isNegative() {
            return this.isNegative;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean isExceptional() {
            return true;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean digitsRoundedUp() {
            throw new IllegalArgumentException("Exceptional value is not rounded");
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean decimalDigitsExact() {
            throw new IllegalArgumentException("Exceptional value is not exact");
        }
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$BinaryToASCIIBuffer.class */
    static class BinaryToASCIIBuffer implements BinaryToASCIIConverter {
        private boolean isNegative;
        private int decExponent;
        private int firstDigitIndex;
        private int nDigits;
        private final char[] digits;
        private final char[] buffer;
        private boolean exactDecimalConversion;
        private boolean decimalDigitsRoundedUp;
        private static int[] insignificantDigitsNumber;
        private static final int[] N_5_BITS;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FloatingDecimal.class.desiredAssertionStatus();
            insignificantDigitsNumber = new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 17, 17, 17, 18, 18, 18, 19};
            N_5_BITS = new int[]{0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61};
        }

        BinaryToASCIIBuffer() {
            this.buffer = new char[26];
            this.exactDecimalConversion = false;
            this.decimalDigitsRoundedUp = false;
            this.digits = new char[20];
        }

        BinaryToASCIIBuffer(boolean z2, char[] cArr) {
            this.buffer = new char[26];
            this.exactDecimalConversion = false;
            this.decimalDigitsRoundedUp = false;
            this.isNegative = z2;
            this.decExponent = 0;
            this.digits = cArr;
            this.firstDigitIndex = 0;
            this.nDigits = cArr.length;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public String toJavaFormatString() {
            return new String(this.buffer, 0, getChars(this.buffer));
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public void appendTo(Appendable appendable) {
            int chars = getChars(this.buffer);
            if (appendable instanceof StringBuilder) {
                ((StringBuilder) appendable).append(this.buffer, 0, chars);
            } else if (appendable instanceof StringBuffer) {
                ((StringBuffer) appendable).append(this.buffer, 0, chars);
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public int getDecimalExponent() {
            return this.decExponent;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public int getDigits(char[] cArr) {
            System.arraycopy(this.digits, this.firstDigitIndex, cArr, 0, this.nDigits);
            return this.nDigits;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean isNegative() {
            return this.isNegative;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean isExceptional() {
            return false;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean digitsRoundedUp() {
            return this.decimalDigitsRoundedUp;
        }

        @Override // sun.misc.FloatingDecimal.BinaryToASCIIConverter
        public boolean decimalDigitsExact() {
            return this.exactDecimalConversion;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSign(boolean z2) {
            this.isNegative = z2;
        }

        private void developLongDigits(int i2, long j2, int i3) {
            int i4;
            int i5;
            if (i3 != 0) {
                long j3 = FDBigInteger.LONG_5_POW[i3] << i3;
                long j4 = j2 % j3;
                j2 /= j3;
                i2 += i3;
                if (j4 >= (j3 >> 1)) {
                    j2++;
                }
            }
            int length = this.digits.length - 1;
            if (j2 <= 2147483647L) {
                if (!$assertionsDisabled && j2 <= 0) {
                    throw new AssertionError(j2);
                }
                int i6 = (int) j2;
                while (true) {
                    i5 = i6 % 10;
                    i6 /= 10;
                    if (i5 != 0) {
                        break;
                    } else {
                        i2++;
                    }
                }
                while (i6 != 0) {
                    int i7 = length;
                    length--;
                    this.digits[i7] = (char) (i5 + 48);
                    i2++;
                    i5 = i6 % 10;
                    i6 /= 10;
                }
                this.digits[length] = (char) (i5 + 48);
            } else {
                while (true) {
                    i4 = (int) (j2 % 10);
                    j2 /= 10;
                    if (i4 != 0) {
                        break;
                    } else {
                        i2++;
                    }
                }
                while (j2 != 0) {
                    int i8 = length;
                    length--;
                    this.digits[i8] = (char) (i4 + 48);
                    i2++;
                    i4 = (int) (j2 % 10);
                    j2 /= 10;
                }
                this.digits[length] = (char) (i4 + 48);
            }
            this.decExponent = i2 + 1;
            this.firstDigitIndex = length;
            this.nDigits = this.digits.length - length;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dtoa(int i2, long j2, int i3, boolean z2) throws IllegalArgumentException {
            int i4;
            boolean z3;
            boolean z4;
            long jCmp;
            boolean z5;
            boolean z6;
            int iInsignificantDigitsForPow2;
            long j3;
            if (!$assertionsDisabled && j2 <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && (j2 & FloatingDecimal.FRACT_HOB) == 0) {
                throw new AssertionError();
            }
            int iNumberOfTrailingZeros = Long.numberOfTrailingZeros(j2);
            int i5 = 53 - iNumberOfTrailingZeros;
            this.decimalDigitsRoundedUp = false;
            this.exactDecimalConversion = false;
            int iMax = Math.max(0, (i5 - i2) - 1);
            if (i2 <= 62 && i2 >= FloatingDecimal.MIN_SMALL_BIN_EXP && iMax < FDBigInteger.LONG_5_POW.length && i5 + N_5_BITS[iMax] < 64 && iMax == 0) {
                if (i2 > i3) {
                    iInsignificantDigitsForPow2 = insignificantDigitsForPow2((i2 - i3) - 1);
                } else {
                    iInsignificantDigitsForPow2 = 0;
                }
                if (i2 >= 52) {
                    j3 = j2 << (i2 - 52);
                } else {
                    j3 = j2 >>> (52 - i2);
                }
                developLongDigits(0, j3, iInsignificantDigitsForPow2);
                return;
            }
            int iEstimateDecExp = estimateDecExp(j2, i2);
            int iMax2 = Math.max(0, -iEstimateDecExp);
            int i6 = iMax2 + iMax + i2;
            int iMax3 = Math.max(0, iEstimateDecExp);
            int i7 = iMax3 + iMax;
            int i8 = i6 - i3;
            long j4 = j2 >>> iNumberOfTrailingZeros;
            int i9 = i6 - (i5 - 1);
            int iMin = Math.min(i9, i7);
            int i10 = i9 - iMin;
            int i11 = i7 - iMin;
            int i12 = i8 - iMin;
            if (i5 == 1) {
                i12--;
            }
            if (i12 < 0) {
                i10 -= i12;
                i11 -= i12;
                i12 = 0;
            }
            int i13 = i5 + i10 + (iMax2 < N_5_BITS.length ? N_5_BITS[iMax2] : iMax2 * 3);
            int i14 = i11 + 1 + (iMax3 + 1 < N_5_BITS.length ? N_5_BITS[iMax3 + 1] : (iMax3 + 1) * 3);
            if (i13 < 64 && i14 < 64) {
                if (i13 < 32 && i14 < 32) {
                    int i15 = (((int) j4) * FDBigInteger.SMALL_5_POW[iMax2]) << i10;
                    int i16 = FDBigInteger.SMALL_5_POW[iMax3] << i11;
                    int i17 = FDBigInteger.SMALL_5_POW[iMax2] << i12;
                    int i18 = i16 * 10;
                    i4 = 0;
                    int i19 = i15 / i16;
                    int i20 = 10 * (i15 % i16);
                    int i21 = i17 * 10;
                    z3 = i20 < i21;
                    z4 = i20 + i21 > i18;
                    if (!$assertionsDisabled && i19 >= 10) {
                        throw new AssertionError(i19);
                    }
                    if (i19 != 0 || z4) {
                        i4 = 0 + 1;
                        this.digits[0] = (char) (48 + i19);
                    } else {
                        iEstimateDecExp--;
                    }
                    if (!z2 || iEstimateDecExp < -3 || iEstimateDecExp >= 8) {
                        z3 = false;
                        z4 = false;
                    }
                    while (!z3 && !z4) {
                        int i22 = i20 / i16;
                        i20 = 10 * (i20 % i16);
                        i21 *= 10;
                        if (!$assertionsDisabled && i22 >= 10) {
                            throw new AssertionError(i22);
                        }
                        if (i21 > 0) {
                            z3 = i20 < i21;
                            z6 = i20 + i21 > i18;
                        } else {
                            z3 = true;
                            z6 = true;
                        }
                        z4 = z6;
                        int i23 = i4;
                        i4++;
                        this.digits[i23] = (char) (48 + i22);
                    }
                    jCmp = (i20 << 1) - i18;
                    this.exactDecimalConversion = i20 == 0;
                } else {
                    long j5 = (j4 * FDBigInteger.LONG_5_POW[iMax2]) << i10;
                    long j6 = FDBigInteger.LONG_5_POW[iMax3] << i11;
                    long j7 = FDBigInteger.LONG_5_POW[iMax2] << i12;
                    long j8 = j6 * 10;
                    i4 = 0;
                    int i24 = (int) (j5 / j6);
                    long j9 = 10 * (j5 % j6);
                    long j10 = j7 * 10;
                    z3 = j9 < j10;
                    z4 = j9 + j10 > j8;
                    if (!$assertionsDisabled && i24 >= 10) {
                        throw new AssertionError(i24);
                    }
                    if (i24 != 0 || z4) {
                        i4 = 0 + 1;
                        this.digits[0] = (char) (48 + i24);
                    } else {
                        iEstimateDecExp--;
                    }
                    if (!z2 || iEstimateDecExp < -3 || iEstimateDecExp >= 8) {
                        z3 = false;
                        z4 = false;
                    }
                    while (!z3 && !z4) {
                        int i25 = (int) (j9 / j6);
                        j9 = 10 * (j9 % j6);
                        j10 *= 10;
                        if (!$assertionsDisabled && i25 >= 10) {
                            throw new AssertionError(i25);
                        }
                        if (j10 > 0) {
                            z3 = j9 < j10;
                            z5 = j9 + j10 > j8;
                        } else {
                            z3 = true;
                            z5 = true;
                        }
                        z4 = z5;
                        int i26 = i4;
                        i4++;
                        this.digits[i26] = (char) (48 + i25);
                    }
                    jCmp = (j9 << 1) - j8;
                    this.exactDecimalConversion = j9 == 0;
                }
            } else {
                FDBigInteger fDBigIntegerValueOfPow52 = FDBigInteger.valueOfPow52(iMax3, i11);
                int normalizationBias = fDBigIntegerValueOfPow52.getNormalizationBias();
                FDBigInteger fDBigIntegerLeftShift = fDBigIntegerValueOfPow52.leftShift(normalizationBias);
                FDBigInteger fDBigIntegerValueOfMulPow52 = FDBigInteger.valueOfMulPow52(j4, iMax2, i10 + normalizationBias);
                FDBigInteger fDBigIntegerValueOfPow522 = FDBigInteger.valueOfPow52(iMax2 + 1, i12 + normalizationBias + 1);
                FDBigInteger fDBigIntegerValueOfPow523 = FDBigInteger.valueOfPow52(iMax3 + 1, i11 + normalizationBias + 1);
                i4 = 0;
                int iQuoRemIteration = fDBigIntegerValueOfMulPow52.quoRemIteration(fDBigIntegerLeftShift);
                z3 = fDBigIntegerValueOfMulPow52.cmp(fDBigIntegerValueOfPow522) < 0;
                z4 = fDBigIntegerValueOfPow523.addAndCmp(fDBigIntegerValueOfMulPow52, fDBigIntegerValueOfPow522) <= 0;
                if (!$assertionsDisabled && iQuoRemIteration >= 10) {
                    throw new AssertionError(iQuoRemIteration);
                }
                if (iQuoRemIteration != 0 || z4) {
                    i4 = 0 + 1;
                    this.digits[0] = (char) (48 + iQuoRemIteration);
                } else {
                    iEstimateDecExp--;
                }
                if (!z2 || iEstimateDecExp < -3 || iEstimateDecExp >= 8) {
                    z3 = false;
                    z4 = false;
                }
                while (!z3 && !z4) {
                    int iQuoRemIteration2 = fDBigIntegerValueOfMulPow52.quoRemIteration(fDBigIntegerLeftShift);
                    if (!$assertionsDisabled && iQuoRemIteration2 >= 10) {
                        throw new AssertionError(iQuoRemIteration2);
                    }
                    fDBigIntegerValueOfPow522 = fDBigIntegerValueOfPow522.multBy10();
                    z3 = fDBigIntegerValueOfMulPow52.cmp(fDBigIntegerValueOfPow522) < 0;
                    z4 = fDBigIntegerValueOfPow523.addAndCmp(fDBigIntegerValueOfMulPow52, fDBigIntegerValueOfPow522) <= 0;
                    int i27 = i4;
                    i4++;
                    this.digits[i27] = (char) (48 + iQuoRemIteration2);
                }
                if (z4 && z3) {
                    fDBigIntegerValueOfMulPow52 = fDBigIntegerValueOfMulPow52.leftShift(1);
                    jCmp = fDBigIntegerValueOfMulPow52.cmp(fDBigIntegerValueOfPow523);
                } else {
                    jCmp = 0;
                }
                this.exactDecimalConversion = fDBigIntegerValueOfMulPow52.cmp(FDBigInteger.ZERO) == 0;
            }
            this.decExponent = iEstimateDecExp + 1;
            this.firstDigitIndex = 0;
            this.nDigits = i4;
            if (z4) {
                if (!z3) {
                    roundup();
                    return;
                }
                if (jCmp == 0) {
                    if ((this.digits[(this.firstDigitIndex + this.nDigits) - 1] & 1) != 0) {
                        roundup();
                    }
                } else if (jCmp > 0) {
                    roundup();
                }
            }
        }

        private void roundup() {
            int i2 = (this.firstDigitIndex + this.nDigits) - 1;
            char c2 = this.digits[i2];
            if (c2 == '9') {
                while (c2 == '9' && i2 > this.firstDigitIndex) {
                    this.digits[i2] = '0';
                    i2--;
                    c2 = this.digits[i2];
                }
                if (c2 == '9') {
                    this.decExponent++;
                    this.digits[this.firstDigitIndex] = '1';
                    return;
                }
            }
            this.digits[i2] = (char) (c2 + 1);
            this.decimalDigitsRoundedUp = true;
        }

        static int estimateDecExp(long j2, int i2) {
            double dLongBitsToDouble = ((Double.longBitsToDouble(FloatingDecimal.EXP_ONE | (j2 & DoubleConsts.SIGNIF_BIT_MASK)) - 1.5d) * 0.289529654d) + 0.176091259d + (i2 * 0.301029995663981d);
            long jDoubleToRawLongBits = Double.doubleToRawLongBits(dLongBitsToDouble);
            int i3 = ((int) ((jDoubleToRawLongBits & DoubleConsts.EXP_BIT_MASK) >> 52)) - 1023;
            boolean z2 = (jDoubleToRawLongBits & Long.MIN_VALUE) != 0;
            if (i3 >= 0 && i3 < 52) {
                long j3 = DoubleConsts.SIGNIF_BIT_MASK >> i3;
                int i4 = (int) (((jDoubleToRawLongBits & DoubleConsts.SIGNIF_BIT_MASK) | FloatingDecimal.FRACT_HOB) >> (52 - i3));
                return z2 ? (j3 & jDoubleToRawLongBits) == 0 ? -i4 : (-i4) - 1 : i4;
            }
            if (i3 < 0) {
                return ((jDoubleToRawLongBits & Long.MAX_VALUE) != 0 && z2) ? -1 : 0;
            }
            return (int) dLongBitsToDouble;
        }

        private static int insignificantDigits(int i2) {
            int i3 = 0;
            while (i2 >= 10) {
                i2 = (int) (i2 / 10);
                i3++;
            }
            return i3;
        }

        private static int insignificantDigitsForPow2(int i2) {
            if (i2 > 1 && i2 < insignificantDigitsNumber.length) {
                return insignificantDigitsNumber[i2];
            }
            return 0;
        }

        private int getChars(char[] cArr) {
            int i2;
            int i3;
            int i4;
            if (!$assertionsDisabled && this.nDigits > 19) {
                throw new AssertionError(this.nDigits);
            }
            int i5 = 0;
            if (this.isNegative) {
                cArr[0] = '-';
                i5 = 1;
            }
            if (this.decExponent > 0 && this.decExponent < 8) {
                int iMin = Math.min(this.nDigits, this.decExponent);
                System.arraycopy(this.digits, this.firstDigitIndex, cArr, i5, iMin);
                int i6 = i5 + iMin;
                if (iMin < this.decExponent) {
                    int i7 = this.decExponent - iMin;
                    Arrays.fill(cArr, i6, i6 + i7, '0');
                    int i8 = i6 + i7;
                    int i9 = i8 + 1;
                    cArr[i8] = '.';
                    i4 = i9 + 1;
                    cArr[i9] = '0';
                } else {
                    int i10 = i6 + 1;
                    cArr[i6] = '.';
                    if (iMin < this.nDigits) {
                        int i11 = this.nDigits - iMin;
                        System.arraycopy(this.digits, this.firstDigitIndex + iMin, cArr, i10, i11);
                        i4 = i10 + i11;
                    } else {
                        i4 = i10 + 1;
                        cArr[i10] = '0';
                    }
                }
            } else if (this.decExponent <= 0 && this.decExponent > -3) {
                int i12 = i5;
                int i13 = i5 + 1;
                cArr[i12] = '0';
                int i14 = i13 + 1;
                cArr[i13] = '.';
                if (this.decExponent != 0) {
                    Arrays.fill(cArr, i14, i14 - this.decExponent, '0');
                    i14 -= this.decExponent;
                }
                System.arraycopy(this.digits, this.firstDigitIndex, cArr, i14, this.nDigits);
                i4 = i14 + this.nDigits;
            } else {
                int i15 = i5;
                int i16 = i5 + 1;
                cArr[i15] = this.digits[this.firstDigitIndex];
                int i17 = i16 + 1;
                cArr[i16] = '.';
                if (this.nDigits > 1) {
                    System.arraycopy(this.digits, this.firstDigitIndex + 1, cArr, i17, this.nDigits - 1);
                    i2 = i17 + (this.nDigits - 1);
                } else {
                    i2 = i17 + 1;
                    cArr[i17] = '0';
                }
                int i18 = i2;
                int i19 = i2 + 1;
                cArr[i18] = 'E';
                if (this.decExponent <= 0) {
                    i19++;
                    cArr[i19] = '-';
                    i3 = (-this.decExponent) + 1;
                } else {
                    i3 = this.decExponent - 1;
                }
                if (i3 <= 9) {
                    int i20 = i19;
                    i4 = i19 + 1;
                    cArr[i20] = (char) (i3 + 48);
                } else if (i3 <= 99) {
                    int i21 = i19;
                    int i22 = i19 + 1;
                    cArr[i21] = (char) ((i3 / 10) + 48);
                    i4 = i22 + 1;
                    cArr[i22] = (char) ((i3 % 10) + 48);
                } else {
                    int i23 = i19;
                    int i24 = i19 + 1;
                    cArr[i23] = (char) ((i3 / 100) + 48);
                    int i25 = i3 % 100;
                    int i26 = i24 + 1;
                    cArr[i24] = (char) ((i25 / 10) + 48);
                    i4 = i26 + 1;
                    cArr[i26] = (char) ((i25 % 10) + 48);
                }
            }
            return i4;
        }
    }

    private static BinaryToASCIIBuffer getBinaryToASCIIBuffer() {
        return threadLocalBinaryToASCIIBuffer.get();
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$PreparedASCIIToBinaryBuffer.class */
    static class PreparedASCIIToBinaryBuffer implements ASCIIToBinaryConverter {
        private final double doubleVal;
        private final float floatVal;

        public PreparedASCIIToBinaryBuffer(double d2, float f2) {
            this.doubleVal = d2;
            this.floatVal = f2;
        }

        @Override // sun.misc.FloatingDecimal.ASCIIToBinaryConverter
        public double doubleValue() {
            return this.doubleVal;
        }

        @Override // sun.misc.FloatingDecimal.ASCIIToBinaryConverter
        public float floatValue() {
            return this.floatVal;
        }
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$ASCIIToBinaryBuffer.class */
    static class ASCIIToBinaryBuffer implements ASCIIToBinaryConverter {
        boolean isNegative;
        int decExponent;
        char[] digits;
        int nDigits;
        private static final double[] SMALL_10_POW;
        private static final float[] SINGLE_SMALL_10_POW;
        private static final double[] BIG_10_POW;
        private static final double[] TINY_10_POW;
        private static final int MAX_SMALL_TEN;
        private static final int SINGLE_MAX_SMALL_TEN;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FloatingDecimal.class.desiredAssertionStatus();
            SMALL_10_POW = new double[]{1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};
            SINGLE_SMALL_10_POW = new float[]{1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f};
            BIG_10_POW = new double[]{1.0E16d, 1.0E32d, 1.0E64d, 1.0E128d, 1.0E256d};
            TINY_10_POW = new double[]{1.0E-16d, 1.0E-32d, 1.0E-64d, 1.0E-128d, 1.0E-256d};
            MAX_SMALL_TEN = SMALL_10_POW.length - 1;
            SINGLE_MAX_SMALL_TEN = SINGLE_SMALL_10_POW.length - 1;
        }

        ASCIIToBinaryBuffer(boolean z2, int i2, char[] cArr, int i3) {
            this.isNegative = z2;
            this.decExponent = i2;
            this.digits = cArr;
            this.nDigits = i3;
        }

        /* JADX WARN: Code restructure failed: missing block: B:170:0x0499, code lost:
        
            if (r8.isNegative == false) goto L172;
         */
        /* JADX WARN: Code restructure failed: missing block: B:171:0x049c, code lost:
        
            r18 = r18 | Long.MIN_VALUE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:173:0x04a9, code lost:
        
            return java.lang.Double.longBitsToDouble(r18);
         */
        @Override // sun.misc.FloatingDecimal.ASCIIToBinaryConverter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public double doubleValue() {
            /*
                Method dump skipped, instructions count: 1194
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.misc.FloatingDecimal.ASCIIToBinaryBuffer.doubleValue():double");
        }

        /* JADX WARN: Code restructure failed: missing block: B:158:0x042f, code lost:
        
            if (r8.isNegative == false) goto L160;
         */
        /* JADX WARN: Code restructure failed: missing block: B:159:0x0432, code lost:
        
            r16 = r16 | Integer.MIN_VALUE;
         */
        /* JADX WARN: Code restructure failed: missing block: B:161:0x043e, code lost:
        
            return java.lang.Float.intBitsToFloat(r16);
         */
        @Override // sun.misc.FloatingDecimal.ASCIIToBinaryConverter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public float floatValue() {
            /*
                Method dump skipped, instructions count: 1087
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.misc.FloatingDecimal.ASCIIToBinaryBuffer.floatValue():float");
        }
    }

    public static BinaryToASCIIConverter getBinaryToASCIIConverter(double d2) {
        return getBinaryToASCIIConverter(d2, true);
    }

    static BinaryToASCIIConverter getBinaryToASCIIConverter(double d2, boolean z2) throws IllegalArgumentException {
        long j2;
        int i2;
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2);
        boolean z3 = (jDoubleToRawLongBits & Long.MIN_VALUE) != 0;
        long j3 = jDoubleToRawLongBits & DoubleConsts.SIGNIF_BIT_MASK;
        int i3 = (int) ((jDoubleToRawLongBits & DoubleConsts.EXP_BIT_MASK) >> 52);
        if (i3 == 2047) {
            if (j3 == 0) {
                return z3 ? B2AC_NEGATIVE_INFINITY : B2AC_POSITIVE_INFINITY;
            }
            return B2AC_NOT_A_NUMBER;
        }
        if (i3 != 0) {
            j2 = j3 | FRACT_HOB;
            i2 = 53;
        } else {
            if (j3 == 0) {
                return z3 ? B2AC_NEGATIVE_ZERO : B2AC_POSITIVE_ZERO;
            }
            int iNumberOfLeadingZeros = Long.numberOfLeadingZeros(j3);
            int i4 = iNumberOfLeadingZeros - 11;
            j2 = j3 << i4;
            i3 = 1 - i4;
            i2 = 64 - iNumberOfLeadingZeros;
        }
        BinaryToASCIIBuffer binaryToASCIIBuffer = getBinaryToASCIIBuffer();
        binaryToASCIIBuffer.setSign(z3);
        binaryToASCIIBuffer.dtoa(i3 - 1023, j2, i2, z2);
        return binaryToASCIIBuffer;
    }

    private static BinaryToASCIIConverter getBinaryToASCIIConverter(float f2) throws IllegalArgumentException {
        int i2;
        int i3;
        int iFloatToRawIntBits = Float.floatToRawIntBits(f2);
        boolean z2 = (iFloatToRawIntBits & Integer.MIN_VALUE) != 0;
        int i4 = iFloatToRawIntBits & FloatConsts.SIGNIF_BIT_MASK;
        int i5 = (iFloatToRawIntBits & FloatConsts.EXP_BIT_MASK) >> 23;
        if (i5 == 255) {
            if (i4 == 0) {
                return z2 ? B2AC_NEGATIVE_INFINITY : B2AC_POSITIVE_INFINITY;
            }
            return B2AC_NOT_A_NUMBER;
        }
        if (i5 == 0) {
            if (i4 == 0) {
                return z2 ? B2AC_NEGATIVE_ZERO : B2AC_POSITIVE_ZERO;
            }
            int iNumberOfLeadingZeros = Integer.numberOfLeadingZeros(i4);
            int i6 = iNumberOfLeadingZeros - 8;
            i2 = i4 << i6;
            i5 = 1 - i6;
            i3 = 32 - iNumberOfLeadingZeros;
        } else {
            i2 = i4 | 8388608;
            i3 = 24;
        }
        BinaryToASCIIBuffer binaryToASCIIBuffer = getBinaryToASCIIBuffer();
        binaryToASCIIBuffer.setSign(z2);
        binaryToASCIIBuffer.dtoa(i5 - 127, i2 << 29, i3, true);
        return binaryToASCIIBuffer;
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x027d A[Catch: StringIndexOutOfBoundsException -> 0x02f8, TryCatch #0 {StringIndexOutOfBoundsException -> 0x02f8, blocks: (B:3:0x0004, B:5:0x0014, B:6:0x001d, B:7:0x001e, B:8:0x0027, B:10:0x0042, B:11:0x0047, B:13:0x0056, B:15:0x0061, B:17:0x006e, B:22:0x007c, B:24:0x0087, B:28:0x0098, B:29:0x009e, B:34:0x00ac, B:36:0x00b5, B:40:0x00cd, B:42:0x00d2, B:45:0x00ee, B:47:0x00fd, B:58:0x0127, B:52:0x010f, B:53:0x0118, B:56:0x0121, B:61:0x0134, B:65:0x014a, B:79:0x0195, B:68:0x0161, B:73:0x017d, B:74:0x0186, B:77:0x018f, B:80:0x019b, B:91:0x01c0, B:95:0x01d6, B:99:0x01eb, B:100:0x0201, B:102:0x021f, B:109:0x0237, B:113:0x0250, B:114:0x0260, B:115:0x0266, B:120:0x0286, B:119:0x027d, B:126:0x029f, B:128:0x02a8, B:130:0x02b3, B:132:0x02be, B:134:0x02c9, B:141:0x02e0, B:142:0x02e6, B:144:0x02ea, B:92:0x01c9), top: B:149:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0298 A[PHI: r10 r13
  0x0298: PHI (r10v2 int) = (r10v1 int), (r10v4 int), (r10v1 int) binds: [B:94:0x01d3, B:122:0x0292, B:98:0x01e8] A[DONT_GENERATE, DONT_INLINE]
  0x0298: PHI (r13v4 int) = (r13v3 int), (r13v8 int), (r13v3 int) binds: [B:94:0x01d3, B:122:0x0292, B:98:0x01e8] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x02ea A[Catch: StringIndexOutOfBoundsException -> 0x02f8, TRY_ENTER, TryCatch #0 {StringIndexOutOfBoundsException -> 0x02f8, blocks: (B:3:0x0004, B:5:0x0014, B:6:0x001d, B:7:0x001e, B:8:0x0027, B:10:0x0042, B:11:0x0047, B:13:0x0056, B:15:0x0061, B:17:0x006e, B:22:0x007c, B:24:0x0087, B:28:0x0098, B:29:0x009e, B:34:0x00ac, B:36:0x00b5, B:40:0x00cd, B:42:0x00d2, B:45:0x00ee, B:47:0x00fd, B:58:0x0127, B:52:0x010f, B:53:0x0118, B:56:0x0121, B:61:0x0134, B:65:0x014a, B:79:0x0195, B:68:0x0161, B:73:0x017d, B:74:0x0186, B:77:0x018f, B:80:0x019b, B:91:0x01c0, B:95:0x01d6, B:99:0x01eb, B:100:0x0201, B:102:0x021f, B:109:0x0237, B:113:0x0250, B:114:0x0260, B:115:0x0266, B:120:0x0286, B:119:0x027d, B:126:0x029f, B:128:0x02a8, B:130:0x02b3, B:132:0x02be, B:134:0x02c9, B:141:0x02e0, B:142:0x02e6, B:144:0x02ea, B:92:0x01c9), top: B:149:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0134 A[Catch: StringIndexOutOfBoundsException -> 0x02f8, TryCatch #0 {StringIndexOutOfBoundsException -> 0x02f8, blocks: (B:3:0x0004, B:5:0x0014, B:6:0x001d, B:7:0x001e, B:8:0x0027, B:10:0x0042, B:11:0x0047, B:13:0x0056, B:15:0x0061, B:17:0x006e, B:22:0x007c, B:24:0x0087, B:28:0x0098, B:29:0x009e, B:34:0x00ac, B:36:0x00b5, B:40:0x00cd, B:42:0x00d2, B:45:0x00ee, B:47:0x00fd, B:58:0x0127, B:52:0x010f, B:53:0x0118, B:56:0x0121, B:61:0x0134, B:65:0x014a, B:79:0x0195, B:68:0x0161, B:73:0x017d, B:74:0x0186, B:77:0x018f, B:80:0x019b, B:91:0x01c0, B:95:0x01d6, B:99:0x01eb, B:100:0x0201, B:102:0x021f, B:109:0x0237, B:113:0x0250, B:114:0x0260, B:115:0x0266, B:120:0x0286, B:119:0x027d, B:126:0x029f, B:128:0x02a8, B:130:0x02b3, B:132:0x02be, B:134:0x02c9, B:141:0x02e0, B:142:0x02e6, B:144:0x02ea, B:92:0x01c9), top: B:149:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01c0 A[Catch: StringIndexOutOfBoundsException -> 0x02f8, TryCatch #0 {StringIndexOutOfBoundsException -> 0x02f8, blocks: (B:3:0x0004, B:5:0x0014, B:6:0x001d, B:7:0x001e, B:8:0x0027, B:10:0x0042, B:11:0x0047, B:13:0x0056, B:15:0x0061, B:17:0x006e, B:22:0x007c, B:24:0x0087, B:28:0x0098, B:29:0x009e, B:34:0x00ac, B:36:0x00b5, B:40:0x00cd, B:42:0x00d2, B:45:0x00ee, B:47:0x00fd, B:58:0x0127, B:52:0x010f, B:53:0x0118, B:56:0x0121, B:61:0x0134, B:65:0x014a, B:79:0x0195, B:68:0x0161, B:73:0x017d, B:74:0x0186, B:77:0x018f, B:80:0x019b, B:91:0x01c0, B:95:0x01d6, B:99:0x01eb, B:100:0x0201, B:102:0x021f, B:109:0x0237, B:113:0x0250, B:114:0x0260, B:115:0x0266, B:120:0x0286, B:119:0x027d, B:126:0x029f, B:128:0x02a8, B:130:0x02b3, B:132:0x02be, B:134:0x02c9, B:141:0x02e0, B:142:0x02e6, B:144:0x02ea, B:92:0x01c9), top: B:149:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01c9 A[Catch: StringIndexOutOfBoundsException -> 0x02f8, TryCatch #0 {StringIndexOutOfBoundsException -> 0x02f8, blocks: (B:3:0x0004, B:5:0x0014, B:6:0x001d, B:7:0x001e, B:8:0x0027, B:10:0x0042, B:11:0x0047, B:13:0x0056, B:15:0x0061, B:17:0x006e, B:22:0x007c, B:24:0x0087, B:28:0x0098, B:29:0x009e, B:34:0x00ac, B:36:0x00b5, B:40:0x00cd, B:42:0x00d2, B:45:0x00ee, B:47:0x00fd, B:58:0x0127, B:52:0x010f, B:53:0x0118, B:56:0x0121, B:61:0x0134, B:65:0x014a, B:79:0x0195, B:68:0x0161, B:73:0x017d, B:74:0x0186, B:77:0x018f, B:80:0x019b, B:91:0x01c0, B:95:0x01d6, B:99:0x01eb, B:100:0x0201, B:102:0x021f, B:109:0x0237, B:113:0x0250, B:114:0x0260, B:115:0x0266, B:120:0x0286, B:119:0x027d, B:126:0x029f, B:128:0x02a8, B:130:0x02b3, B:132:0x02be, B:134:0x02c9, B:141:0x02e0, B:142:0x02e6, B:144:0x02ea, B:92:0x01c9), top: B:149:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static sun.misc.FloatingDecimal.ASCIIToBinaryConverter readJavaFormatString(java.lang.String r7) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 794
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.FloatingDecimal.readJavaFormatString(java.lang.String):sun.misc.FloatingDecimal$ASCIIToBinaryConverter");
    }

    /* loaded from: rt.jar:sun/misc/FloatingDecimal$HexFloatPattern.class */
    private static class HexFloatPattern {
        private static final Pattern VALUE = Pattern.compile("([-+])?0[xX](((\\p{XDigit}+)\\.?)|((\\p{XDigit}*)\\.(\\p{XDigit}+)))[pP]([-+])?(\\p{Digit}+)[fFdD]?");

        private HexFloatPattern() {
        }
    }

    static ASCIIToBinaryConverter parseHexString(String str) {
        int length;
        String strStripLeadingZeros;
        int i2;
        long hexDigit;
        int i3;
        long j2;
        double dLongBitsToDouble;
        Matcher matcher = HexFloatPattern.VALUE.matcher(str);
        if (!matcher.matches()) {
            throw new NumberFormatException("For input string: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        String strGroup = matcher.group(1);
        boolean z2 = strGroup != null && strGroup.equals(LanguageTag.SEP);
        int length2 = 0;
        String strGroup2 = matcher.group(4);
        if (strGroup2 != null) {
            strStripLeadingZeros = stripLeadingZeros(strGroup2);
            length = strStripLeadingZeros.length();
        } else {
            String strStripLeadingZeros2 = stripLeadingZeros(matcher.group(6));
            length = strStripLeadingZeros2.length();
            String strGroup3 = matcher.group(7);
            length2 = strGroup3.length();
            strStripLeadingZeros = (strStripLeadingZeros2 == null ? "" : strStripLeadingZeros2) + strGroup3;
        }
        String strStripLeadingZeros3 = stripLeadingZeros(strStripLeadingZeros);
        int length3 = strStripLeadingZeros3.length();
        if (length >= 1) {
            i2 = 4 * (length - 1);
        } else {
            i2 = (-4) * ((length2 - length3) + 1);
        }
        if (length3 == 0) {
            return z2 ? A2BC_NEGATIVE_ZERO : A2BC_POSITIVE_ZERO;
        }
        String strGroup4 = matcher.group(8);
        boolean z3 = strGroup4 == null || strGroup4.equals(Marker.ANY_NON_NULL_MARKER);
        try {
            long j3 = ((z3 ? 1L : -1L) * Integer.parseInt(matcher.group(9))) + i2;
            boolean z4 = false;
            boolean z5 = false;
            long hexDigit2 = getHexDigit(strStripLeadingZeros3, 0);
            if (hexDigit2 == 1) {
                hexDigit = 0 | (hexDigit2 << 52);
                i3 = 48;
            } else if (hexDigit2 <= 3) {
                hexDigit = 0 | (hexDigit2 << 51);
                i3 = 47;
                j3++;
            } else if (hexDigit2 <= 7) {
                hexDigit = 0 | (hexDigit2 << 50);
                i3 = 46;
                j3 += 2;
            } else if (hexDigit2 <= 15) {
                hexDigit = 0 | (hexDigit2 << 49);
                i3 = 45;
                j3 += 3;
            } else {
                throw new AssertionError((Object) "Result from digit conversion too large!");
            }
            int i4 = 1;
            while (i4 < length3 && i3 >= 0) {
                hexDigit |= getHexDigit(strStripLeadingZeros3, i4) << i3;
                i3 -= 4;
                i4++;
            }
            if (i4 < length3) {
                long hexDigit3 = getHexDigit(strStripLeadingZeros3, i4);
                switch (i3) {
                    case -4:
                        z4 = (hexDigit3 & 8) != 0;
                        z5 = (hexDigit3 & 7) != 0;
                        break;
                    case -3:
                        hexDigit |= (hexDigit3 & 8) >> 3;
                        z4 = (hexDigit3 & 4) != 0;
                        z5 = (hexDigit3 & 3) != 0;
                        break;
                    case -2:
                        hexDigit |= (hexDigit3 & 12) >> 2;
                        z4 = (hexDigit3 & 2) != 0;
                        z5 = (hexDigit3 & 1) != 0;
                        break;
                    case -1:
                        hexDigit |= (hexDigit3 & 14) >> 1;
                        z4 = (hexDigit3 & 1) != 0;
                        break;
                    default:
                        throw new AssertionError((Object) "Unexpected shift distance remainder.");
                }
                while (true) {
                    i4++;
                    if (i4 < length3 && !z5) {
                        z5 = z5 || ((long) getHexDigit(strStripLeadingZeros3, i4)) != 0;
                    }
                }
            }
            int i5 = z2 ? Integer.MIN_VALUE : 0;
            if (j3 >= -126) {
                if (j3 > 127) {
                    i5 |= FloatConsts.EXP_BIT_MASK;
                } else {
                    boolean z6 = (hexDigit & ((1 << 28) - 1)) != 0 || z4 || z5;
                    int i6 = (int) (hexDigit >>> 28);
                    if ((i6 & 3) != 1 || z6) {
                        i6++;
                    }
                    i5 |= ((((int) j3) + 126) << 23) + (i6 >> 1);
                }
            } else if (j3 >= -150) {
                int i7 = (int) ((-98) - j3);
                if (!$assertionsDisabled && i7 < 29) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && i7 >= 53) {
                    throw new AssertionError();
                }
                boolean z7 = (hexDigit & ((1 << i7) - 1)) != 0 || z4 || z5;
                int i8 = (int) (hexDigit >>> i7);
                if ((i8 & 3) != 1 || z7) {
                    i8++;
                }
                i5 |= i8 >> 1;
            }
            float fIntBitsToFloat = Float.intBitsToFloat(i5);
            if (j3 > 1023) {
                return z2 ? A2BC_NEGATIVE_INFINITY : A2BC_POSITIVE_INFINITY;
            }
            if (j3 <= 1023 && j3 >= -1022) {
                j2 = (((j3 + 1023) << 52) & DoubleConsts.EXP_BIT_MASK) | (DoubleConsts.SIGNIF_BIT_MASK & hexDigit);
            } else {
                if (j3 < -1075) {
                    return z2 ? A2BC_NEGATIVE_ZERO : A2BC_POSITIVE_ZERO;
                }
                z5 = z5 || z4;
                int i9 = 53 - ((((int) j3) - DoubleConsts.MIN_SUB_EXPONENT) + 1);
                if (!$assertionsDisabled && (i9 < 1 || i9 > 53)) {
                    throw new AssertionError();
                }
                z4 = (hexDigit & (1 << (i9 - 1))) != 0;
                if (i9 > 1) {
                    z5 = z5 || (hexDigit & (((-1) << (i9 - 1)) ^ (-1))) != 0;
                }
                j2 = 0 | (DoubleConsts.SIGNIF_BIT_MASK & (hexDigit >> i9));
            }
            boolean z8 = (j2 & 1) == 0;
            if ((z8 && z4 && z5) || (!z8 && z4)) {
                j2++;
            }
            if (z2) {
                dLongBitsToDouble = Double.longBitsToDouble(j2 | Long.MIN_VALUE);
            } else {
                dLongBitsToDouble = Double.longBitsToDouble(j2);
            }
            return new PreparedASCIIToBinaryBuffer(dLongBitsToDouble, fIntBitsToFloat);
        } catch (NumberFormatException e2) {
            return z2 ? z3 ? A2BC_NEGATIVE_INFINITY : A2BC_NEGATIVE_ZERO : z3 ? A2BC_POSITIVE_INFINITY : A2BC_POSITIVE_ZERO;
        }
    }

    static String stripLeadingZeros(String str) {
        if (!str.isEmpty() && str.charAt(0) == '0') {
            for (int i2 = 1; i2 < str.length(); i2++) {
                if (str.charAt(i2) != '0') {
                    return str.substring(i2);
                }
            }
            return "";
        }
        return str;
    }

    static int getHexDigit(String str, int i2) {
        int iDigit = Character.digit(str.charAt(i2), 16);
        if (iDigit <= -1 || iDigit >= 16) {
            throw new AssertionError((Object) ("Unexpected failure of digit conversion of " + str.charAt(i2)));
        }
        return iDigit;
    }
}
