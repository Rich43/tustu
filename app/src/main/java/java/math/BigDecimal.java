package java.math;

import com.sun.corba.se.impl.util.Version;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import sun.misc.DoubleConsts;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/math/BigDecimal.class */
public class BigDecimal extends Number implements Comparable<BigDecimal> {
    private final BigInteger intVal;
    private final int scale;
    private transient int precision;
    private transient String stringCache;
    static final long INFLATED = Long.MIN_VALUE;
    private static final BigInteger INFLATED_BIGINT;
    private final transient long intCompact;
    private static final int MAX_COMPACT_DIGITS = 18;
    private static final long serialVersionUID = 6108874887143696463L;
    private static final ThreadLocal<StringBuilderHelper> threadLocalStringBuilderHelper;
    private static final BigDecimal[] zeroThroughTen;
    private static final BigDecimal[] ZERO_SCALED_BY;
    private static final long HALF_LONG_MAX_VALUE = 4611686018427387903L;
    private static final long HALF_LONG_MIN_VALUE = -4611686018427387904L;
    public static final BigDecimal ZERO;
    public static final BigDecimal ONE;
    public static final BigDecimal TEN;
    public static final int ROUND_UP = 0;
    public static final int ROUND_DOWN = 1;
    public static final int ROUND_CEILING = 2;
    public static final int ROUND_FLOOR = 3;
    public static final int ROUND_HALF_UP = 4;
    public static final int ROUND_HALF_DOWN = 5;
    public static final int ROUND_HALF_EVEN = 6;
    public static final int ROUND_UNNECESSARY = 7;
    private static final double[] double10pow;
    private static final float[] float10pow;
    private static final long[] LONG_TEN_POWERS_TABLE;
    private static volatile BigInteger[] BIG_TEN_POWERS_TABLE;
    private static final int BIG_TEN_POWERS_TABLE_INITLEN;
    private static final int BIG_TEN_POWERS_TABLE_MAX;
    private static final long[] THRESHOLDS_TABLE;
    private static final long DIV_NUM_BASE = 4294967296L;
    private static final long[][] LONGLONG_TEN_POWERS_TABLE;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Type inference failed for: r0v32, types: [long[], long[][]] */
    static {
        $assertionsDisabled = !BigDecimal.class.desiredAssertionStatus();
        INFLATED_BIGINT = BigInteger.valueOf(Long.MIN_VALUE);
        threadLocalStringBuilderHelper = new ThreadLocal<StringBuilderHelper>() { // from class: java.math.BigDecimal.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public StringBuilderHelper initialValue() {
                return new StringBuilderHelper();
            }
        };
        zeroThroughTen = new BigDecimal[]{new BigDecimal(BigInteger.ZERO, 0L, 0, 1), new BigDecimal(BigInteger.ONE, 1L, 0, 1), new BigDecimal(BigInteger.valueOf(2L), 2L, 0, 1), new BigDecimal(BigInteger.valueOf(3L), 3L, 0, 1), new BigDecimal(BigInteger.valueOf(4L), 4L, 0, 1), new BigDecimal(BigInteger.valueOf(5L), 5L, 0, 1), new BigDecimal(BigInteger.valueOf(6L), 6L, 0, 1), new BigDecimal(BigInteger.valueOf(7L), 7L, 0, 1), new BigDecimal(BigInteger.valueOf(8L), 8L, 0, 1), new BigDecimal(BigInteger.valueOf(9L), 9L, 0, 1), new BigDecimal(BigInteger.TEN, 10L, 0, 2)};
        ZERO_SCALED_BY = new BigDecimal[]{zeroThroughTen[0], new BigDecimal(BigInteger.ZERO, 0L, 1, 1), new BigDecimal(BigInteger.ZERO, 0L, 2, 1), new BigDecimal(BigInteger.ZERO, 0L, 3, 1), new BigDecimal(BigInteger.ZERO, 0L, 4, 1), new BigDecimal(BigInteger.ZERO, 0L, 5, 1), new BigDecimal(BigInteger.ZERO, 0L, 6, 1), new BigDecimal(BigInteger.ZERO, 0L, 7, 1), new BigDecimal(BigInteger.ZERO, 0L, 8, 1), new BigDecimal(BigInteger.ZERO, 0L, 9, 1), new BigDecimal(BigInteger.ZERO, 0L, 10, 1), new BigDecimal(BigInteger.ZERO, 0L, 11, 1), new BigDecimal(BigInteger.ZERO, 0L, 12, 1), new BigDecimal(BigInteger.ZERO, 0L, 13, 1), new BigDecimal(BigInteger.ZERO, 0L, 14, 1), new BigDecimal(BigInteger.ZERO, 0L, 15, 1)};
        ZERO = zeroThroughTen[0];
        ONE = zeroThroughTen[1];
        TEN = zeroThroughTen[10];
        double10pow = new double[]{1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d, 1000000.0d, 1.0E7d, 1.0E8d, 1.0E9d, 1.0E10d, 1.0E11d, 1.0E12d, 1.0E13d, 1.0E14d, 1.0E15d, 1.0E16d, 1.0E17d, 1.0E18d, 1.0E19d, 1.0E20d, 1.0E21d, 1.0E22d};
        float10pow = new float[]{1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f};
        LONG_TEN_POWERS_TABLE = new long[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, NativeMediaPlayer.ONE_SECOND, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
        BIG_TEN_POWERS_TABLE = new BigInteger[]{BigInteger.ONE, BigInteger.valueOf(10L), BigInteger.valueOf(100L), BigInteger.valueOf(1000L), BigInteger.valueOf(10000L), BigInteger.valueOf(100000L), BigInteger.valueOf(1000000L), BigInteger.valueOf(10000000L), BigInteger.valueOf(100000000L), BigInteger.valueOf(NativeMediaPlayer.ONE_SECOND), BigInteger.valueOf(10000000000L), BigInteger.valueOf(100000000000L), BigInteger.valueOf(1000000000000L), BigInteger.valueOf(10000000000000L), BigInteger.valueOf(100000000000000L), BigInteger.valueOf(1000000000000000L), BigInteger.valueOf(10000000000000000L), BigInteger.valueOf(100000000000000000L), BigInteger.valueOf(1000000000000000000L)};
        BIG_TEN_POWERS_TABLE_INITLEN = BIG_TEN_POWERS_TABLE.length;
        BIG_TEN_POWERS_TABLE_MAX = 16 * BIG_TEN_POWERS_TABLE_INITLEN;
        THRESHOLDS_TABLE = new long[]{Long.MAX_VALUE, 922337203685477580L, 92233720368547758L, 9223372036854775L, 922337203685477L, 92233720368547L, 9223372036854L, 922337203685L, 92233720368L, 9223372036L, 922337203, 92233720, 9223372, 922337, 92233, 9223, 922, 92, 9};
        LONGLONG_TEN_POWERS_TABLE = new long[]{new long[]{0, -8446744073709551616L}, new long[]{5, 7766279631452241920L}, new long[]{54, 3875820019684212736L}, new long[]{542, 1864712049423024128L}, new long[]{5421, 200376420520689664L}, new long[]{54210, 2003764205206896640L}, new long[]{542101, 1590897978359414784L}, new long[]{5421010, -2537764290115403776L}, new long[]{54210108, -6930898827444486144L}, new long[]{542101086, 4477988020393345024L}, new long[]{5421010862L, 7886392056514347008L}, new long[]{54210108624L, 5076944270305263616L}, new long[]{542101086242L, -4570789518076018688L}, new long[]{5421010862427L, -8814407033341083648L}, new long[]{54210108624275L, 4089650035136921600L}, new long[]{542101086242752L, 4003012203950112768L}, new long[]{5421010862427522L, 3136633892082024448L}, new long[]{54210108624275221L, -5527149226598858752L}, new long[]{542101086242752217L, 68739955140067328L}, new long[]{5421010862427522170L, 687399551400673280L}};
    }

    BigDecimal(BigInteger bigInteger, long j2, int i2, int i3) {
        this.scale = i2;
        this.precision = i3;
        this.intCompact = j2;
        this.intVal = bigInteger;
    }

    public BigDecimal(char[] cArr, int i2, int i3) {
        this(cArr, i2, i3, MathContext.UNLIMITED);
    }

    public BigDecimal(char[] cArr, int i2, int i3, MathContext mathContext) {
        long jCompactValFor;
        if ((cArr.length | i3 | i2) < 0 || i3 > cArr.length - i2) {
            throw new NumberFormatException("Bad offset or len arguments for char[] input.");
        }
        int iLongDigitLength = 0;
        int iAdjustScale = 0;
        long j2 = 0;
        BigInteger bigInteger = null;
        try {
            boolean z2 = false;
            if (cArr[i2] == '-') {
                z2 = true;
                i2++;
                i3--;
            } else if (cArr[i2] == '+') {
                i2++;
                i3--;
            }
            boolean z3 = false;
            long exp = 0;
            int i4 = 0;
            if (i3 <= 18) {
                while (true) {
                    if (i3 <= 0) {
                        break;
                    }
                    char c2 = cArr[i2];
                    if (c2 == '0') {
                        if (iLongDigitLength == 0) {
                            iLongDigitLength = 1;
                        } else if (j2 != 0) {
                            j2 *= 10;
                            iLongDigitLength++;
                        }
                        if (z3) {
                            iAdjustScale++;
                        }
                    } else if (c2 >= '1' && c2 <= '9') {
                        int i5 = c2 - '0';
                        iLongDigitLength = (iLongDigitLength == 1 && j2 == 0) ? iLongDigitLength : iLongDigitLength + 1;
                        j2 = (j2 * 10) + i5;
                        iAdjustScale = z3 ? iAdjustScale + 1 : iAdjustScale;
                    } else if (c2 == '.') {
                        if (z3) {
                            throw new NumberFormatException();
                        }
                        z3 = true;
                    } else if (Character.isDigit(c2)) {
                        int iDigit = Character.digit(c2, 10);
                        if (iDigit == 0) {
                            if (iLongDigitLength == 0) {
                                iLongDigitLength = 1;
                            } else if (j2 != 0) {
                                j2 *= 10;
                                iLongDigitLength++;
                            }
                        } else {
                            iLongDigitLength = (iLongDigitLength == 1 && j2 == 0) ? iLongDigitLength : iLongDigitLength + 1;
                            j2 = (j2 * 10) + iDigit;
                        }
                        iAdjustScale = z3 ? iAdjustScale + 1 : iAdjustScale;
                    } else if (c2 == 'e' || c2 == 'E') {
                        exp = parseExp(cArr, i2, i3);
                        if (((int) exp) != exp) {
                            throw new NumberFormatException();
                        }
                    } else {
                        throw new NumberFormatException();
                    }
                    i2++;
                    i3--;
                }
                if (iLongDigitLength == 0) {
                    throw new NumberFormatException();
                }
                iAdjustScale = exp != 0 ? adjustScale(iAdjustScale, exp) : iAdjustScale;
                jCompactValFor = z2 ? -j2 : j2;
                int i6 = mathContext.precision;
                int i7 = iLongDigitLength - i6;
                if (i6 > 0 && i7 > 0) {
                    while (i7 > 0) {
                        iAdjustScale = checkScaleNonZero(iAdjustScale - i7);
                        jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i7], mathContext.roundingMode.oldMode);
                        iLongDigitLength = longDigitLength(jCompactValFor);
                        i7 = iLongDigitLength - i6;
                    }
                }
            } else {
                char[] cArr2 = new char[i3];
                while (true) {
                    if (i3 <= 0) {
                        break;
                    }
                    char c3 = cArr[i2];
                    if ((c3 >= '0' && c3 <= '9') || Character.isDigit(c3)) {
                        if (c3 == '0' || Character.digit(c3, 10) == 0) {
                            if (iLongDigitLength == 0) {
                                cArr2[i4] = c3;
                                iLongDigitLength = 1;
                            } else if (i4 != 0) {
                                int i8 = i4;
                                i4++;
                                cArr2[i8] = c3;
                                iLongDigitLength++;
                            }
                        } else {
                            iLongDigitLength = (iLongDigitLength == 1 && i4 == 0) ? iLongDigitLength : iLongDigitLength + 1;
                            int i9 = i4;
                            i4++;
                            cArr2[i9] = c3;
                        }
                        if (z3) {
                            iAdjustScale++;
                        }
                    } else if (c3 == '.') {
                        if (z3) {
                            throw new NumberFormatException();
                        }
                        z3 = true;
                    } else {
                        if (c3 != 'e' && c3 != 'E') {
                            throw new NumberFormatException();
                        }
                        exp = parseExp(cArr, i2, i3);
                        if (((int) exp) != exp) {
                            throw new NumberFormatException();
                        }
                    }
                    i2++;
                    i3--;
                }
                if (iLongDigitLength == 0) {
                    throw new NumberFormatException();
                }
                iAdjustScale = exp != 0 ? adjustScale(iAdjustScale, exp) : iAdjustScale;
                bigInteger = new BigInteger(cArr2, z2 ? -1 : 1, iLongDigitLength);
                jCompactValFor = compactValFor(bigInteger);
                int i10 = mathContext.precision;
                if (i10 > 0 && iLongDigitLength > i10) {
                    if (jCompactValFor == Long.MIN_VALUE) {
                        int i11 = iLongDigitLength - i10;
                        while (true) {
                            if (i11 <= 0) {
                                break;
                            }
                            iAdjustScale = checkScaleNonZero(iAdjustScale - i11);
                            bigInteger = divideAndRoundByTenPow(bigInteger, i11, mathContext.roundingMode.oldMode);
                            jCompactValFor = compactValFor(bigInteger);
                            if (jCompactValFor != Long.MIN_VALUE) {
                                iLongDigitLength = longDigitLength(jCompactValFor);
                                break;
                            } else {
                                iLongDigitLength = bigDigitLength(bigInteger);
                                i11 = iLongDigitLength - i10;
                            }
                        }
                    }
                    if (jCompactValFor != Long.MIN_VALUE) {
                        int i12 = iLongDigitLength - i10;
                        while (i12 > 0) {
                            iAdjustScale = checkScaleNonZero(iAdjustScale - i12);
                            jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i12], mathContext.roundingMode.oldMode);
                            iLongDigitLength = longDigitLength(jCompactValFor);
                            i12 = iLongDigitLength - i10;
                        }
                        bigInteger = null;
                    }
                }
            }
            this.scale = iAdjustScale;
            this.precision = iLongDigitLength;
            this.intCompact = jCompactValFor;
            this.intVal = bigInteger;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new NumberFormatException();
        } catch (NegativeArraySizeException e3) {
            throw new NumberFormatException();
        }
    }

    private int adjustScale(int i2, long j2) {
        long j3 = i2 - j2;
        if (j3 > 2147483647L || j3 < -2147483648L) {
            throw new NumberFormatException("Scale out of range.");
        }
        return (int) j3;
    }

    private static long parseExp(char[] cArr, int i2, int i3) {
        int iDigit;
        long j2 = 0;
        int i4 = i2 + 1;
        char c2 = cArr[i4];
        int i5 = i3 - 1;
        boolean z2 = c2 == '-';
        if (z2 || c2 == '+') {
            i4++;
            c2 = cArr[i4];
            i5--;
        }
        if (i5 <= 0) {
            throw new NumberFormatException();
        }
        while (i5 > 10 && (c2 == '0' || Character.digit(c2, 10) == 0)) {
            i4++;
            c2 = cArr[i4];
            i5--;
        }
        if (i5 > 10) {
            throw new NumberFormatException();
        }
        while (true) {
            if (c2 >= '0' && c2 <= '9') {
                iDigit = c2 - '0';
            } else {
                iDigit = Character.digit(c2, 10);
                if (iDigit < 0) {
                    throw new NumberFormatException();
                }
            }
            j2 = (j2 * 10) + iDigit;
            if (i5 != 1) {
                i4++;
                c2 = cArr[i4];
                i5--;
            } else {
                if (z2) {
                    j2 = -j2;
                }
                return j2;
            }
        }
    }

    public BigDecimal(char[] cArr) {
        this(cArr, 0, cArr.length);
    }

    public BigDecimal(char[] cArr, MathContext mathContext) {
        this(cArr, 0, cArr.length, mathContext);
    }

    public BigDecimal(String str) {
        this(str.toCharArray(), 0, str.length());
    }

    public BigDecimal(String str, MathContext mathContext) {
        this(str.toCharArray(), 0, str.length(), mathContext);
    }

    public BigDecimal(double d2) {
        this(d2, MathContext.UNLIMITED);
    }

    public BigDecimal(double d2, MathContext mathContext) {
        BigInteger bigIntegerMultiply;
        if (Double.isInfinite(d2) || Double.isNaN(d2)) {
            throw new NumberFormatException("Infinite or NaN");
        }
        long jDoubleToLongBits = Double.doubleToLongBits(d2);
        int i2 = (jDoubleToLongBits >> 63) == 0 ? 1 : -1;
        int i3 = (int) ((jDoubleToLongBits >> 52) & 2047);
        long j2 = i3 == 0 ? (jDoubleToLongBits & DoubleConsts.SIGNIF_BIT_MASK) << 1 : (jDoubleToLongBits & DoubleConsts.SIGNIF_BIT_MASK) | 4503599627370496L;
        int i4 = i3 - 1075;
        if (j2 == 0) {
            this.intVal = BigInteger.ZERO;
            this.scale = 0;
            this.intCompact = 0L;
            this.precision = 1;
            return;
        }
        while ((j2 & 1) == 0) {
            j2 >>= 1;
            i4++;
        }
        int iCheckScaleNonZero = 0;
        long jCompactValFor = i2 * j2;
        if (i4 == 0) {
            bigIntegerMultiply = jCompactValFor == Long.MIN_VALUE ? INFLATED_BIGINT : null;
        } else {
            if (i4 < 0) {
                bigIntegerMultiply = BigInteger.valueOf(5L).pow(-i4).multiply(jCompactValFor);
                iCheckScaleNonZero = -i4;
            } else {
                bigIntegerMultiply = BigInteger.valueOf(2L).pow(i4).multiply(jCompactValFor);
            }
            jCompactValFor = compactValFor(bigIntegerMultiply);
        }
        int i5 = 0;
        int i6 = mathContext.precision;
        if (i6 > 0) {
            int i7 = mathContext.roundingMode.oldMode;
            if (jCompactValFor == Long.MIN_VALUE) {
                int iBigDigitLength = bigDigitLength(bigIntegerMultiply);
                while (true) {
                    i5 = iBigDigitLength;
                    int i8 = i5 - i6;
                    if (i8 <= 0) {
                        break;
                    }
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i8);
                    bigIntegerMultiply = divideAndRoundByTenPow(bigIntegerMultiply, i8, i7);
                    jCompactValFor = compactValFor(bigIntegerMultiply);
                    if (jCompactValFor != Long.MIN_VALUE) {
                        break;
                    } else {
                        iBigDigitLength = bigDigitLength(bigIntegerMultiply);
                    }
                }
            }
            if (jCompactValFor != Long.MIN_VALUE) {
                int iLongDigitLength = longDigitLength(jCompactValFor);
                while (true) {
                    i5 = iLongDigitLength;
                    int i9 = i5 - i6;
                    if (i9 <= 0) {
                        break;
                    }
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i9);
                    jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i9], mathContext.roundingMode.oldMode);
                    iLongDigitLength = longDigitLength(jCompactValFor);
                }
                bigIntegerMultiply = null;
            }
        }
        this.intVal = bigIntegerMultiply;
        this.intCompact = jCompactValFor;
        this.scale = iCheckScaleNonZero;
        this.precision = i5;
    }

    private static BigInteger toStrictBigInteger(BigInteger bigInteger) {
        return bigInteger.getClass() == BigInteger.class ? bigInteger : new BigInteger((byte[]) bigInteger.toByteArray().clone());
    }

    public BigDecimal(BigInteger bigInteger) {
        this.scale = 0;
        this.intVal = toStrictBigInteger(bigInteger);
        this.intCompact = compactValFor(this.intVal);
    }

    public BigDecimal(BigInteger bigInteger, MathContext mathContext) {
        this(toStrictBigInteger(bigInteger), 0, mathContext);
    }

    public BigDecimal(BigInteger bigInteger, int i2) {
        this.intVal = toStrictBigInteger(bigInteger);
        this.intCompact = compactValFor(this.intVal);
        this.scale = i2;
    }

    public BigDecimal(BigInteger bigInteger, int i2, MathContext mathContext) {
        BigInteger strictBigInteger = toStrictBigInteger(bigInteger);
        long jCompactValFor = compactValFor(strictBigInteger);
        int i3 = mathContext.precision;
        int i4 = 0;
        if (i3 > 0) {
            int i5 = mathContext.roundingMode.oldMode;
            if (jCompactValFor == Long.MIN_VALUE) {
                int iBigDigitLength = bigDigitLength(strictBigInteger);
                while (true) {
                    i4 = iBigDigitLength;
                    int i6 = i4 - i3;
                    if (i6 <= 0) {
                        break;
                    }
                    i2 = checkScaleNonZero(i2 - i6);
                    strictBigInteger = divideAndRoundByTenPow(strictBigInteger, i6, i5);
                    jCompactValFor = compactValFor(strictBigInteger);
                    if (jCompactValFor != Long.MIN_VALUE) {
                        break;
                    } else {
                        iBigDigitLength = bigDigitLength(strictBigInteger);
                    }
                }
            }
            if (jCompactValFor != Long.MIN_VALUE) {
                int iLongDigitLength = longDigitLength(jCompactValFor);
                while (true) {
                    i4 = iLongDigitLength;
                    int i7 = i4 - i3;
                    if (i7 <= 0) {
                        break;
                    }
                    i2 = checkScaleNonZero(i2 - i7);
                    jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i7], i5);
                    iLongDigitLength = longDigitLength(jCompactValFor);
                }
                strictBigInteger = null;
            }
        }
        this.intVal = strictBigInteger;
        this.intCompact = jCompactValFor;
        this.scale = i2;
        this.precision = i4;
    }

    public BigDecimal(int i2) {
        this.intCompact = i2;
        this.scale = 0;
        this.intVal = null;
    }

    public BigDecimal(int i2, MathContext mathContext) {
        int i3 = mathContext.precision;
        long jDivideAndRound = i2;
        int iCheckScaleNonZero = 0;
        int i4 = 0;
        if (i3 > 0) {
            int iLongDigitLength = longDigitLength(jDivideAndRound);
            while (true) {
                i4 = iLongDigitLength;
                int i5 = i4 - i3;
                if (i5 <= 0) {
                    break;
                }
                iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i5);
                jDivideAndRound = divideAndRound(jDivideAndRound, LONG_TEN_POWERS_TABLE[i5], mathContext.roundingMode.oldMode);
                iLongDigitLength = longDigitLength(jDivideAndRound);
            }
        }
        this.intVal = null;
        this.intCompact = jDivideAndRound;
        this.scale = iCheckScaleNonZero;
        this.precision = i4;
    }

    public BigDecimal(long j2) {
        this.intCompact = j2;
        this.intVal = j2 == Long.MIN_VALUE ? INFLATED_BIGINT : null;
        this.scale = 0;
    }

    public BigDecimal(long j2, MathContext mathContext) {
        int i2 = mathContext.precision;
        int i3 = mathContext.roundingMode.oldMode;
        int i4 = 0;
        int iCheckScaleNonZero = 0;
        BigInteger bigIntegerDivideAndRoundByTenPow = j2 == Long.MIN_VALUE ? INFLATED_BIGINT : null;
        if (i2 > 0) {
            if (j2 == Long.MIN_VALUE) {
                int iBigDigitLength = 19;
                while (true) {
                    i4 = iBigDigitLength;
                    int i5 = i4 - i2;
                    if (i5 <= 0) {
                        break;
                    }
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i5);
                    bigIntegerDivideAndRoundByTenPow = divideAndRoundByTenPow(bigIntegerDivideAndRoundByTenPow, i5, i3);
                    j2 = compactValFor(bigIntegerDivideAndRoundByTenPow);
                    if (j2 != Long.MIN_VALUE) {
                        break;
                    } else {
                        iBigDigitLength = bigDigitLength(bigIntegerDivideAndRoundByTenPow);
                    }
                }
            }
            if (j2 != Long.MIN_VALUE) {
                int iLongDigitLength = longDigitLength(j2);
                while (true) {
                    i4 = iLongDigitLength;
                    int i6 = i4 - i2;
                    if (i6 <= 0) {
                        break;
                    }
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i6);
                    j2 = divideAndRound(j2, LONG_TEN_POWERS_TABLE[i6], mathContext.roundingMode.oldMode);
                    iLongDigitLength = longDigitLength(j2);
                }
                bigIntegerDivideAndRoundByTenPow = null;
            }
        }
        this.intVal = bigIntegerDivideAndRoundByTenPow;
        this.intCompact = j2;
        this.scale = iCheckScaleNonZero;
        this.precision = i4;
    }

    public static BigDecimal valueOf(long j2, int i2) {
        if (i2 == 0) {
            return valueOf(j2);
        }
        if (j2 == 0) {
            return zeroValueOf(i2);
        }
        return new BigDecimal(j2 == Long.MIN_VALUE ? INFLATED_BIGINT : null, j2, i2, 0);
    }

    public static BigDecimal valueOf(long j2) {
        if (j2 >= 0 && j2 < zeroThroughTen.length) {
            return zeroThroughTen[(int) j2];
        }
        if (j2 != Long.MIN_VALUE) {
            return new BigDecimal((BigInteger) null, j2, 0, 0);
        }
        return new BigDecimal(INFLATED_BIGINT, j2, 0, 0);
    }

    static BigDecimal valueOf(long j2, int i2, int i3) {
        if (i2 == 0 && j2 >= 0 && j2 < zeroThroughTen.length) {
            return zeroThroughTen[(int) j2];
        }
        if (j2 == 0) {
            return zeroValueOf(i2);
        }
        return new BigDecimal(j2 == Long.MIN_VALUE ? INFLATED_BIGINT : null, j2, i2, i3);
    }

    static BigDecimal valueOf(BigInteger bigInteger, int i2, int i3) {
        long jCompactValFor = compactValFor(bigInteger);
        if (jCompactValFor == 0) {
            return zeroValueOf(i2);
        }
        if (i2 == 0 && jCompactValFor >= 0 && jCompactValFor < zeroThroughTen.length) {
            return zeroThroughTen[(int) jCompactValFor];
        }
        return new BigDecimal(bigInteger, jCompactValFor, i2, i3);
    }

    static BigDecimal zeroValueOf(int i2) {
        if (i2 >= 0 && i2 < ZERO_SCALED_BY.length) {
            return ZERO_SCALED_BY[i2];
        }
        return new BigDecimal(BigInteger.ZERO, 0L, i2, 1);
    }

    public static BigDecimal valueOf(double d2) {
        return new BigDecimal(Double.toString(d2));
    }

    public BigDecimal add(BigDecimal bigDecimal) {
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return add(this.intCompact, this.scale, bigDecimal.intCompact, bigDecimal.scale);
            }
            return add(this.intCompact, this.scale, bigDecimal.intVal, bigDecimal.scale);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return add(bigDecimal.intCompact, bigDecimal.scale, this.intVal, this.scale);
        }
        return add(this.intVal, this.scale, bigDecimal.intVal, bigDecimal.scale);
    }

    public BigDecimal add(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return add(bigDecimal);
        }
        BigDecimal bigDecimal2 = this;
        boolean z2 = bigDecimal2.signum() == 0;
        boolean z3 = bigDecimal.signum() == 0;
        if (z2 || z3) {
            int iMax = Math.max(bigDecimal2.scale(), bigDecimal.scale());
            if (z2 && z3) {
                return zeroValueOf(iMax);
            }
            BigDecimal bigDecimalDoRound = z2 ? doRound(bigDecimal, mathContext) : doRound(bigDecimal2, mathContext);
            if (bigDecimalDoRound.scale() == iMax) {
                return bigDecimalDoRound;
            }
            if (bigDecimalDoRound.scale() > iMax) {
                return stripZerosToMatchScale(bigDecimalDoRound.intVal, bigDecimalDoRound.intCompact, bigDecimalDoRound.scale, iMax);
            }
            int iPrecision = mathContext.precision - bigDecimalDoRound.precision();
            if (iPrecision >= iMax - bigDecimalDoRound.scale()) {
                return bigDecimalDoRound.setScale(iMax);
            }
            return bigDecimalDoRound.setScale(bigDecimalDoRound.scale() + iPrecision);
        }
        long j2 = bigDecimal2.scale - bigDecimal.scale;
        if (j2 != 0) {
            BigDecimal[] bigDecimalArrPreAlign = preAlign(bigDecimal2, bigDecimal, j2, mathContext);
            matchScale(bigDecimalArrPreAlign);
            bigDecimal2 = bigDecimalArrPreAlign[0];
            bigDecimal = bigDecimalArrPreAlign[1];
        }
        return doRound(bigDecimal2.inflated().add(bigDecimal.inflated()), bigDecimal2.scale, mathContext);
    }

    private BigDecimal[] preAlign(BigDecimal bigDecimal, BigDecimal bigDecimal2, long j2, MathContext mathContext) {
        BigDecimal bigDecimal3;
        BigDecimal bigDecimalValueOf;
        if (!$assertionsDisabled && j2 == 0) {
            throw new AssertionError();
        }
        if (j2 < 0) {
            bigDecimal3 = bigDecimal;
            bigDecimalValueOf = bigDecimal2;
        } else {
            bigDecimal3 = bigDecimal2;
            bigDecimalValueOf = bigDecimal;
        }
        long jPrecision = (bigDecimal3.scale - bigDecimal3.precision()) + mathContext.precision;
        long jPrecision2 = (bigDecimalValueOf.scale - bigDecimalValueOf.precision()) + 1;
        if (jPrecision2 > bigDecimal3.scale + 2 && jPrecision2 > jPrecision + 2) {
            bigDecimalValueOf = valueOf(bigDecimalValueOf.signum(), checkScale(Math.max(bigDecimal3.scale, jPrecision) + 3));
        }
        return new BigDecimal[]{bigDecimal3, bigDecimalValueOf};
    }

    public BigDecimal subtract(BigDecimal bigDecimal) {
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return add(this.intCompact, this.scale, -bigDecimal.intCompact, bigDecimal.scale);
            }
            return add(this.intCompact, this.scale, bigDecimal.intVal.negate(), bigDecimal.scale);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return add(-bigDecimal.intCompact, bigDecimal.scale, this.intVal, this.scale);
        }
        return add(this.intVal, this.scale, bigDecimal.intVal.negate(), bigDecimal.scale);
    }

    public BigDecimal subtract(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return subtract(bigDecimal);
        }
        return add(bigDecimal.negate(), mathContext);
    }

    public BigDecimal multiply(BigDecimal bigDecimal) {
        int iCheckScale = checkScale(this.scale + bigDecimal.scale);
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return multiply(this.intCompact, bigDecimal.intCompact, iCheckScale);
            }
            return multiply(this.intCompact, bigDecimal.intVal, iCheckScale);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return multiply(bigDecimal.intCompact, this.intVal, iCheckScale);
        }
        return multiply(this.intVal, bigDecimal.intVal, iCheckScale);
    }

    public BigDecimal multiply(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return multiply(bigDecimal);
        }
        int iCheckScale = checkScale(this.scale + bigDecimal.scale);
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return multiplyAndRound(this.intCompact, bigDecimal.intCompact, iCheckScale, mathContext);
            }
            return multiplyAndRound(this.intCompact, bigDecimal.intVal, iCheckScale, mathContext);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return multiplyAndRound(bigDecimal.intCompact, this.intVal, iCheckScale, mathContext);
        }
        return multiplyAndRound(this.intVal, bigDecimal.intVal, iCheckScale, mathContext);
    }

    public BigDecimal divide(BigDecimal bigDecimal, int i2, int i3) {
        if (i3 < 0 || i3 > 7) {
            throw new IllegalArgumentException("Invalid rounding mode");
        }
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return divide(this.intCompact, this.scale, bigDecimal.intCompact, bigDecimal.scale, i2, i3);
            }
            return divide(this.intCompact, this.scale, bigDecimal.intVal, bigDecimal.scale, i2, i3);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return divide(this.intVal, this.scale, bigDecimal.intCompact, bigDecimal.scale, i2, i3);
        }
        return divide(this.intVal, this.scale, bigDecimal.intVal, bigDecimal.scale, i2, i3);
    }

    public BigDecimal divide(BigDecimal bigDecimal, int i2, RoundingMode roundingMode) {
        return divide(bigDecimal, i2, roundingMode.oldMode);
    }

    public BigDecimal divide(BigDecimal bigDecimal, int i2) {
        return divide(bigDecimal, this.scale, i2);
    }

    public BigDecimal divide(BigDecimal bigDecimal, RoundingMode roundingMode) {
        return divide(bigDecimal, this.scale, roundingMode.oldMode);
    }

    public BigDecimal divide(BigDecimal bigDecimal) {
        if (bigDecimal.signum() == 0) {
            if (signum() == 0) {
                throw new ArithmeticException("Division undefined");
            }
            throw new ArithmeticException("Division by zero");
        }
        int iSaturateLong = saturateLong(this.scale - bigDecimal.scale);
        if (signum() == 0) {
            return zeroValueOf(iSaturateLong);
        }
        try {
            BigDecimal bigDecimalDivide = divide(bigDecimal, new MathContext((int) Math.min(precision() + ((long) Math.ceil((10.0d * bigDecimal.precision()) / 3.0d)), 2147483647L), RoundingMode.UNNECESSARY));
            if (iSaturateLong > bigDecimalDivide.scale()) {
                return bigDecimalDivide.setScale(iSaturateLong, 7);
            }
            return bigDecimalDivide;
        } catch (ArithmeticException e2) {
            throw new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.");
        }
    }

    public BigDecimal divide(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return divide(bigDecimal);
        }
        long j2 = this.scale - bigDecimal.scale;
        if (bigDecimal.signum() == 0) {
            if (signum() == 0) {
                throw new ArithmeticException("Division undefined");
            }
            throw new ArithmeticException("Division by zero");
        }
        if (signum() == 0) {
            return zeroValueOf(saturateLong(j2));
        }
        int iPrecision = precision();
        int iPrecision2 = bigDecimal.precision();
        if (this.intCompact != Long.MIN_VALUE) {
            if (bigDecimal.intCompact != Long.MIN_VALUE) {
                return divide(this.intCompact, iPrecision, bigDecimal.intCompact, iPrecision2, j2, mathContext);
            }
            return divide(this.intCompact, iPrecision, bigDecimal.intVal, iPrecision2, j2, mathContext);
        }
        if (bigDecimal.intCompact != Long.MIN_VALUE) {
            return divide(this.intVal, iPrecision, bigDecimal.intCompact, iPrecision2, j2, mathContext);
        }
        return divide(this.intVal, iPrecision, bigDecimal.intVal, iPrecision2, j2, mathContext);
    }

    public BigDecimal divideToIntegralValue(BigDecimal bigDecimal) {
        int iSaturateLong = saturateLong(this.scale - bigDecimal.scale);
        if (compareMagnitude(bigDecimal) < 0) {
            return zeroValueOf(iSaturateLong);
        }
        if (signum() == 0 && bigDecimal.signum() != 0) {
            return setScale(iSaturateLong, 7);
        }
        BigDecimal bigDecimalDivide = divide(bigDecimal, new MathContext((int) Math.min(precision() + ((long) Math.ceil((10.0d * bigDecimal.precision()) / 3.0d)) + Math.abs(scale() - bigDecimal.scale()) + 2, 2147483647L), RoundingMode.DOWN));
        if (bigDecimalDivide.scale > 0) {
            BigDecimal scale = bigDecimalDivide.setScale(0, RoundingMode.DOWN);
            bigDecimalDivide = stripZerosToMatchScale(scale.intVal, scale.intCompact, scale.scale, iSaturateLong);
        }
        if (bigDecimalDivide.scale < iSaturateLong) {
            bigDecimalDivide = bigDecimalDivide.setScale(iSaturateLong, 7);
        }
        return bigDecimalDivide;
    }

    public BigDecimal divideToIntegralValue(BigDecimal bigDecimal, MathContext mathContext) {
        int iPrecision;
        if (mathContext.precision == 0 || compareMagnitude(bigDecimal) < 0) {
            return divideToIntegralValue(bigDecimal);
        }
        int iSaturateLong = saturateLong(this.scale - bigDecimal.scale);
        BigDecimal bigDecimalDivide = divide(bigDecimal, new MathContext(mathContext.precision, RoundingMode.DOWN));
        if (bigDecimalDivide.scale() < 0) {
            if (subtract(bigDecimalDivide.multiply(bigDecimal)).compareMagnitude(bigDecimal) >= 0) {
                throw new ArithmeticException("Division impossible");
            }
        } else if (bigDecimalDivide.scale() > 0) {
            bigDecimalDivide = bigDecimalDivide.setScale(0, RoundingMode.DOWN);
        }
        if (iSaturateLong > bigDecimalDivide.scale() && (iPrecision = mathContext.precision - bigDecimalDivide.precision()) > 0) {
            return bigDecimalDivide.setScale(bigDecimalDivide.scale() + Math.min(iPrecision, iSaturateLong - bigDecimalDivide.scale));
        }
        return stripZerosToMatchScale(bigDecimalDivide.intVal, bigDecimalDivide.intCompact, bigDecimalDivide.scale, iSaturateLong);
    }

    public BigDecimal remainder(BigDecimal bigDecimal) {
        return divideAndRemainder(bigDecimal)[1];
    }

    public BigDecimal remainder(BigDecimal bigDecimal, MathContext mathContext) {
        return divideAndRemainder(bigDecimal, mathContext)[1];
    }

    public BigDecimal[] divideAndRemainder(BigDecimal bigDecimal) {
        BigDecimal[] bigDecimalArr = new BigDecimal[2];
        bigDecimalArr[0] = divideToIntegralValue(bigDecimal);
        bigDecimalArr[1] = subtract(bigDecimalArr[0].multiply(bigDecimal));
        return bigDecimalArr;
    }

    public BigDecimal[] divideAndRemainder(BigDecimal bigDecimal, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return divideAndRemainder(bigDecimal);
        }
        BigDecimal[] bigDecimalArr = new BigDecimal[2];
        bigDecimalArr[0] = divideToIntegralValue(bigDecimal, mathContext);
        bigDecimalArr[1] = subtract(bigDecimalArr[0].multiply(bigDecimal));
        return bigDecimalArr;
    }

    public BigDecimal pow(int i2) {
        if (i2 < 0 || i2 > 999999999) {
            throw new ArithmeticException("Invalid operation");
        }
        return new BigDecimal(inflated().pow(i2), checkScale(this.scale * i2));
    }

    public BigDecimal pow(int i2, MathContext mathContext) {
        if (mathContext.precision == 0) {
            return pow(i2);
        }
        if (i2 < -999999999 || i2 > 999999999) {
            throw new ArithmeticException("Invalid operation");
        }
        if (i2 == 0) {
            return ONE;
        }
        MathContext mathContext2 = mathContext;
        int iAbs = Math.abs(i2);
        if (mathContext.precision > 0) {
            int iLongDigitLength = longDigitLength(iAbs);
            if (iLongDigitLength > mathContext.precision) {
                throw new ArithmeticException("Invalid operation");
            }
            mathContext2 = new MathContext(mathContext.precision + iLongDigitLength + 1, mathContext.roundingMode);
        }
        BigDecimal bigDecimalDivide = ONE;
        boolean z2 = false;
        int i3 = 1;
        while (true) {
            iAbs += iAbs;
            if (iAbs < 0) {
                z2 = true;
                bigDecimalDivide = bigDecimalDivide.multiply(this, mathContext2);
            }
            if (i3 == 31) {
                break;
            }
            if (z2) {
                bigDecimalDivide = bigDecimalDivide.multiply(bigDecimalDivide, mathContext2);
            }
            i3++;
        }
        if (i2 < 0) {
            bigDecimalDivide = ONE.divide(bigDecimalDivide, mathContext2);
        }
        return doRound(bigDecimalDivide, mathContext);
    }

    public BigDecimal abs() {
        return signum() < 0 ? negate() : this;
    }

    public BigDecimal abs(MathContext mathContext) {
        return signum() < 0 ? negate(mathContext) : plus(mathContext);
    }

    public BigDecimal negate() {
        if (this.intCompact == Long.MIN_VALUE) {
            return new BigDecimal(this.intVal.negate(), Long.MIN_VALUE, this.scale, this.precision);
        }
        return valueOf(-this.intCompact, this.scale, this.precision);
    }

    public BigDecimal negate(MathContext mathContext) {
        return negate().plus(mathContext);
    }

    public BigDecimal plus() {
        return this;
    }

    public BigDecimal plus(MathContext mathContext) {
        if (mathContext.precision == 0) {
            return this;
        }
        return doRound(this, mathContext);
    }

    public int signum() {
        if (this.intCompact != Long.MIN_VALUE) {
            return Long.signum(this.intCompact);
        }
        return this.intVal.signum();
    }

    public int scale() {
        return this.scale;
    }

    public int precision() {
        int iBigDigitLength = this.precision;
        if (iBigDigitLength == 0) {
            long j2 = this.intCompact;
            if (j2 != Long.MIN_VALUE) {
                iBigDigitLength = longDigitLength(j2);
            } else {
                iBigDigitLength = bigDigitLength(this.intVal);
            }
            this.precision = iBigDigitLength;
        }
        return iBigDigitLength;
    }

    public BigInteger unscaledValue() {
        return inflated();
    }

    public BigDecimal round(MathContext mathContext) {
        return plus(mathContext);
    }

    public BigDecimal setScale(int i2, RoundingMode roundingMode) {
        return setScale(i2, roundingMode.oldMode);
    }

    public BigDecimal setScale(int i2, int i3) {
        if (i3 < 0 || i3 > 7) {
            throw new IllegalArgumentException("Invalid rounding mode");
        }
        int i4 = this.scale;
        if (i2 == i4) {
            return this;
        }
        if (signum() == 0) {
            return zeroValueOf(i2);
        }
        if (this.intCompact != Long.MIN_VALUE) {
            long j2 = this.intCompact;
            if (i2 > i4) {
                int iCheckScale = checkScale(i2 - i4);
                long jLongMultiplyPowerTen = longMultiplyPowerTen(j2, iCheckScale);
                if (jLongMultiplyPowerTen != Long.MIN_VALUE) {
                    return valueOf(jLongMultiplyPowerTen, i2);
                }
                return new BigDecimal(bigMultiplyPowerTen(iCheckScale), Long.MIN_VALUE, i2, this.precision > 0 ? this.precision + iCheckScale : 0);
            }
            int iCheckScale2 = checkScale(i4 - i2);
            if (iCheckScale2 < LONG_TEN_POWERS_TABLE.length) {
                return divideAndRound(j2, LONG_TEN_POWERS_TABLE[iCheckScale2], i2, i3, i2);
            }
            return divideAndRound(inflated(), bigTenToThe(iCheckScale2), i2, i3, i2);
        }
        if (i2 > i4) {
            int iCheckScale3 = checkScale(i2 - i4);
            return new BigDecimal(bigMultiplyPowerTen(this.intVal, iCheckScale3), Long.MIN_VALUE, i2, this.precision > 0 ? this.precision + iCheckScale3 : 0);
        }
        int iCheckScale4 = checkScale(i4 - i2);
        if (iCheckScale4 < LONG_TEN_POWERS_TABLE.length) {
            return divideAndRound(this.intVal, LONG_TEN_POWERS_TABLE[iCheckScale4], i2, i3, i2);
        }
        return divideAndRound(this.intVal, bigTenToThe(iCheckScale4), i2, i3, i2);
    }

    public BigDecimal setScale(int i2) {
        return setScale(i2, 7);
    }

    public BigDecimal movePointLeft(int i2) {
        BigDecimal bigDecimal = new BigDecimal(this.intVal, this.intCompact, checkScale(this.scale + i2), 0);
        return bigDecimal.scale < 0 ? bigDecimal.setScale(0, 7) : bigDecimal;
    }

    public BigDecimal movePointRight(int i2) {
        BigDecimal bigDecimal = new BigDecimal(this.intVal, this.intCompact, checkScale(this.scale - i2), 0);
        return bigDecimal.scale < 0 ? bigDecimal.setScale(0, 7) : bigDecimal;
    }

    public BigDecimal scaleByPowerOfTen(int i2) {
        return new BigDecimal(this.intVal, this.intCompact, checkScale(this.scale - i2), this.precision);
    }

    public BigDecimal stripTrailingZeros() {
        if (this.intCompact == 0 || (this.intVal != null && this.intVal.signum() == 0)) {
            return ZERO;
        }
        if (this.intCompact != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(this.intCompact, this.scale, Long.MIN_VALUE);
        }
        return createAndStripZerosToMatchScale(this.intVal, this.scale, Long.MIN_VALUE);
    }

    @Override // java.lang.Comparable
    public int compareTo(BigDecimal bigDecimal) {
        if (this.scale == bigDecimal.scale) {
            long j2 = this.intCompact;
            long j3 = bigDecimal.intCompact;
            if (j2 != Long.MIN_VALUE && j3 != Long.MIN_VALUE) {
                if (j2 != j3) {
                    return j2 > j3 ? 1 : -1;
                }
                return 0;
            }
        }
        int iSignum = signum();
        int iSignum2 = bigDecimal.signum();
        if (iSignum != iSignum2) {
            return iSignum > iSignum2 ? 1 : -1;
        }
        if (iSignum == 0) {
            return 0;
        }
        int iCompareMagnitude = compareMagnitude(bigDecimal);
        return iSignum > 0 ? iCompareMagnitude : -iCompareMagnitude;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0097 A[PHI: r9
  0x0097: PHI (r9v2 long) = (r9v0 long), (r9v3 long) binds: [B:28:0x0081, B:30:0x0094] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d4 A[PHI: r7
  0x00d4: PHI (r7v2 long) = (r7v0 long), (r7v3 long) binds: [B:38:0x00c1, B:40:0x00d1] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int compareMagnitude(java.math.BigDecimal r6) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.compareMagnitude(java.math.BigDecimal):int");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BigDecimal)) {
            return false;
        }
        BigDecimal bigDecimal = (BigDecimal) obj;
        if (obj == this) {
            return true;
        }
        if (this.scale != bigDecimal.scale) {
            return false;
        }
        long j2 = this.intCompact;
        long jCompactValFor = bigDecimal.intCompact;
        if (j2 != Long.MIN_VALUE) {
            if (jCompactValFor == Long.MIN_VALUE) {
                jCompactValFor = compactValFor(bigDecimal.intVal);
            }
            return jCompactValFor == j2;
        }
        if (jCompactValFor != Long.MIN_VALUE) {
            return jCompactValFor == compactValFor(this.intVal);
        }
        return inflated().equals(bigDecimal.inflated());
    }

    public BigDecimal min(BigDecimal bigDecimal) {
        return compareTo(bigDecimal) <= 0 ? this : bigDecimal;
    }

    public BigDecimal max(BigDecimal bigDecimal) {
        return compareTo(bigDecimal) >= 0 ? this : bigDecimal;
    }

    public int hashCode() {
        if (this.intCompact != Long.MIN_VALUE) {
            int i2 = (int) ((((int) (r8 >>> 32)) * 31) + ((this.intCompact < 0 ? -this.intCompact : this.intCompact) & 4294967295L));
            return (31 * (this.intCompact < 0 ? -i2 : i2)) + this.scale;
        }
        return (31 * this.intVal.hashCode()) + this.scale;
    }

    public String toString() {
        String str = this.stringCache;
        if (str == null) {
            String strLayoutChars = layoutChars(true);
            str = strLayoutChars;
            this.stringCache = strLayoutChars;
        }
        return str;
    }

    public String toEngineeringString() {
        return layoutChars(false);
    }

    public String toPlainString() {
        String string;
        StringBuilder sb;
        if (this.scale == 0) {
            if (this.intCompact != Long.MIN_VALUE) {
                return Long.toString(this.intCompact);
            }
            return this.intVal.toString();
        }
        if (this.scale < 0) {
            if (signum() == 0) {
                return "0";
            }
            int iCheckScaleNonZero = checkScaleNonZero(-this.scale);
            if (this.intCompact != Long.MIN_VALUE) {
                sb = new StringBuilder(20 + iCheckScaleNonZero);
                sb.append(this.intCompact);
            } else {
                String string2 = this.intVal.toString();
                sb = new StringBuilder(string2.length() + iCheckScaleNonZero);
                sb.append(string2);
            }
            for (int i2 = 0; i2 < iCheckScaleNonZero; i2++) {
                sb.append('0');
            }
            return sb.toString();
        }
        if (this.intCompact != Long.MIN_VALUE) {
            string = Long.toString(Math.abs(this.intCompact));
        } else {
            string = this.intVal.abs().toString();
        }
        return getValueString(signum(), string, this.scale);
    }

    private String getValueString(int i2, String str, int i3) {
        StringBuilder sb;
        int length = str.length() - i3;
        if (length == 0) {
            return (i2 < 0 ? "-0." : "0.") + str;
        }
        if (length > 0) {
            sb = new StringBuilder(str);
            sb.insert(length, '.');
            if (i2 < 0) {
                sb.insert(0, '-');
            }
        } else {
            sb = new StringBuilder((3 - length) + str.length());
            sb.append(i2 < 0 ? "-0." : "0.");
            for (int i4 = 0; i4 < (-length); i4++) {
                sb.append('0');
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public BigInteger toBigInteger() {
        return setScale(0, 1).inflated();
    }

    public BigInteger toBigIntegerExact() {
        return setScale(0, 7).inflated();
    }

    @Override // java.lang.Number
    public long longValue() {
        if (this.intCompact != Long.MIN_VALUE && this.scale == 0) {
            return this.intCompact;
        }
        if (signum() == 0 || fractionOnly() || this.scale <= -64) {
            return 0L;
        }
        return toBigInteger().longValue();
    }

    private boolean fractionOnly() {
        if ($assertionsDisabled || signum() != 0) {
            return precision() - this.scale <= 0;
        }
        throw new AssertionError();
    }

    public long longValueExact() {
        if (this.intCompact != Long.MIN_VALUE && this.scale == 0) {
            return this.intCompact;
        }
        if (signum() == 0) {
            return 0L;
        }
        if (fractionOnly()) {
            throw new ArithmeticException("Rounding necessary");
        }
        if (precision() - this.scale > 19) {
            throw new ArithmeticException("Overflow");
        }
        BigDecimal scale = setScale(0, 7);
        if (scale.precision() >= 19) {
            LongOverflow.check(scale);
        }
        return scale.inflated().longValue();
    }

    /* loaded from: rt.jar:java/math/BigDecimal$LongOverflow.class */
    private static class LongOverflow {
        private static final BigInteger LONGMIN = BigInteger.valueOf(Long.MIN_VALUE);
        private static final BigInteger LONGMAX = BigInteger.valueOf(Long.MAX_VALUE);

        private LongOverflow() {
        }

        public static void check(BigDecimal bigDecimal) {
            BigInteger bigIntegerInflated = bigDecimal.inflated();
            if (bigIntegerInflated.compareTo(LONGMIN) < 0 || bigIntegerInflated.compareTo(LONGMAX) > 0) {
                throw new ArithmeticException("Overflow");
            }
        }
    }

    @Override // java.lang.Number
    public int intValue() {
        return (this.intCompact == Long.MIN_VALUE || this.scale != 0) ? (int) longValue() : (int) this.intCompact;
    }

    public int intValueExact() {
        long jLongValueExact = longValueExact();
        if (((int) jLongValueExact) != jLongValueExact) {
            throw new ArithmeticException("Overflow");
        }
        return (int) jLongValueExact;
    }

    public short shortValueExact() {
        long jLongValueExact = longValueExact();
        if (((short) jLongValueExact) != jLongValueExact) {
            throw new ArithmeticException("Overflow");
        }
        return (short) jLongValueExact;
    }

    public byte byteValueExact() {
        long jLongValueExact = longValueExact();
        if (((byte) jLongValueExact) != jLongValueExact) {
            throw new ArithmeticException("Overflow");
        }
        return (byte) jLongValueExact;
    }

    @Override // java.lang.Number
    public float floatValue() {
        if (this.intCompact != Long.MIN_VALUE) {
            if (this.scale == 0) {
                return this.intCompact;
            }
            if (Math.abs(this.intCompact) < 4194304) {
                if (this.scale > 0 && this.scale < float10pow.length) {
                    return this.intCompact / float10pow[this.scale];
                }
                if (this.scale < 0 && this.scale > (-float10pow.length)) {
                    return this.intCompact * float10pow[-this.scale];
                }
            }
        }
        return Float.parseFloat(toString());
    }

    @Override // java.lang.Number
    public double doubleValue() {
        if (this.intCompact != Long.MIN_VALUE) {
            if (this.scale == 0) {
                return this.intCompact;
            }
            if (Math.abs(this.intCompact) < 4503599627370496L) {
                if (this.scale > 0 && this.scale < double10pow.length) {
                    return this.intCompact / double10pow[this.scale];
                }
                if (this.scale < 0 && this.scale > (-double10pow.length)) {
                    return this.intCompact * double10pow[-this.scale];
                }
            }
        }
        return Double.parseDouble(toString());
    }

    public BigDecimal ulp() {
        return valueOf(1L, scale(), 1);
    }

    /* loaded from: rt.jar:java/math/BigDecimal$StringBuilderHelper.class */
    static class StringBuilderHelper {
        final StringBuilder sb = new StringBuilder();
        final char[] cmpCharArray = new char[19];
        static final char[] DIGIT_TENS;
        static final char[] DIGIT_ONES;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BigDecimal.class.desiredAssertionStatus();
            DIGIT_TENS = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
            DIGIT_ONES = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        }

        StringBuilderHelper() {
        }

        StringBuilder getStringBuilder() {
            this.sb.setLength(0);
            return this.sb;
        }

        char[] getCompactCharArray() {
            return this.cmpCharArray;
        }

        int putIntCompact(long j2) {
            if (!$assertionsDisabled && j2 < 0) {
                throw new AssertionError();
            }
            int length = this.cmpCharArray.length;
            while (j2 > 2147483647L) {
                long j3 = j2 / 100;
                int i2 = (int) (j2 - (j3 * 100));
                j2 = j3;
                int i3 = length - 1;
                this.cmpCharArray[i3] = DIGIT_ONES[i2];
                length = i3 - 1;
                this.cmpCharArray[length] = DIGIT_TENS[i2];
            }
            int i4 = (int) j2;
            while (i4 >= 100) {
                int i5 = i4 / 100;
                int i6 = i4 - (i5 * 100);
                i4 = i5;
                int i7 = length - 1;
                this.cmpCharArray[i7] = DIGIT_ONES[i6];
                length = i7 - 1;
                this.cmpCharArray[length] = DIGIT_TENS[i6];
            }
            int i8 = length - 1;
            this.cmpCharArray[i8] = DIGIT_ONES[i4];
            if (i4 >= 10) {
                i8--;
                this.cmpCharArray[i8] = DIGIT_TENS[i4];
            }
            return i8;
        }
    }

    private String layoutChars(boolean z2) {
        int iPutIntCompact;
        char[] charArray;
        if (this.scale == 0) {
            if (this.intCompact != Long.MIN_VALUE) {
                return Long.toString(this.intCompact);
            }
            return this.intVal.toString();
        }
        if (this.scale == 2 && this.intCompact >= 0 && this.intCompact < 2147483647L) {
            int i2 = ((int) this.intCompact) % 100;
            return Integer.toString(((int) this.intCompact) / 100) + '.' + StringBuilderHelper.DIGIT_TENS[i2] + StringBuilderHelper.DIGIT_ONES[i2];
        }
        StringBuilderHelper stringBuilderHelper = threadLocalStringBuilderHelper.get();
        if (this.intCompact != Long.MIN_VALUE) {
            iPutIntCompact = stringBuilderHelper.putIntCompact(Math.abs(this.intCompact));
            charArray = stringBuilderHelper.getCompactCharArray();
        } else {
            iPutIntCompact = 0;
            charArray = this.intVal.abs().toString().toCharArray();
        }
        StringBuilder stringBuilder = stringBuilderHelper.getStringBuilder();
        if (signum() < 0) {
            stringBuilder.append('-');
        }
        int length = charArray.length - iPutIntCompact;
        long j2 = (-this.scale) + (length - 1);
        if (this.scale >= 0 && j2 >= -6) {
            int i3 = this.scale - length;
            if (i3 >= 0) {
                stringBuilder.append('0');
                stringBuilder.append('.');
                while (i3 > 0) {
                    stringBuilder.append('0');
                    i3--;
                }
                stringBuilder.append(charArray, iPutIntCompact, length);
            } else {
                stringBuilder.append(charArray, iPutIntCompact, -i3);
                stringBuilder.append('.');
                stringBuilder.append(charArray, (-i3) + iPutIntCompact, this.scale);
            }
        } else {
            if (z2) {
                stringBuilder.append(charArray[iPutIntCompact]);
                if (length > 1) {
                    stringBuilder.append('.');
                    stringBuilder.append(charArray, iPutIntCompact + 1, length - 1);
                }
            } else {
                int i4 = (int) (j2 % 3);
                if (i4 < 0) {
                    i4 += 3;
                }
                j2 -= i4;
                int i5 = i4 + 1;
                if (signum() == 0) {
                    switch (i5) {
                        case 1:
                            stringBuilder.append('0');
                            break;
                        case 2:
                            stringBuilder.append("0.00");
                            j2 += 3;
                            break;
                        case 3:
                            stringBuilder.append(Version.BUILD);
                            j2 += 3;
                            break;
                        default:
                            throw new AssertionError((Object) ("Unexpected sig value " + i5));
                    }
                } else if (i5 >= length) {
                    stringBuilder.append(charArray, iPutIntCompact, length);
                    for (int i6 = i5 - length; i6 > 0; i6--) {
                        stringBuilder.append('0');
                    }
                } else {
                    stringBuilder.append(charArray, iPutIntCompact, i5);
                    stringBuilder.append('.');
                    stringBuilder.append(charArray, iPutIntCompact + i5, length - i5);
                }
            }
            if (j2 != 0) {
                stringBuilder.append('E');
                if (j2 > 0) {
                    stringBuilder.append('+');
                }
                stringBuilder.append(j2);
            }
        }
        return stringBuilder.toString();
    }

    private static BigInteger bigTenToThe(int i2) {
        if (i2 < 0) {
            return BigInteger.ZERO;
        }
        if (i2 < BIG_TEN_POWERS_TABLE_MAX) {
            BigInteger[] bigIntegerArr = BIG_TEN_POWERS_TABLE;
            if (i2 < bigIntegerArr.length) {
                return bigIntegerArr[i2];
            }
            return expandBigIntegerTenPowers(i2);
        }
        return BigInteger.TEN.pow(i2);
    }

    private static BigInteger expandBigIntegerTenPowers(int i2) {
        BigInteger bigInteger;
        synchronized (BigDecimal.class) {
            BigInteger[] bigIntegerArr = BIG_TEN_POWERS_TABLE;
            int length = bigIntegerArr.length;
            if (length <= i2) {
                int i3 = length << 1;
                while (i3 <= i2) {
                    i3 <<= 1;
                }
                bigIntegerArr = (BigInteger[]) Arrays.copyOf(bigIntegerArr, i3);
                for (int i4 = length; i4 < i3; i4++) {
                    bigIntegerArr[i4] = bigIntegerArr[i4 - 1].multiply(BigInteger.TEN);
                }
                BIG_TEN_POWERS_TABLE = bigIntegerArr;
            }
            bigInteger = bigIntegerArr[i2];
        }
        return bigInteger;
    }

    private static long longMultiplyPowerTen(long j2, int i2) {
        if (j2 == 0 || i2 <= 0) {
            return j2;
        }
        long[] jArr = LONG_TEN_POWERS_TABLE;
        long[] jArr2 = THRESHOLDS_TABLE;
        if (i2 < jArr.length && i2 < jArr2.length) {
            long j3 = jArr[i2];
            if (j2 == 1) {
                return j3;
            }
            if (Math.abs(j2) <= jArr2[i2]) {
                return j2 * j3;
            }
            return Long.MIN_VALUE;
        }
        return Long.MIN_VALUE;
    }

    private BigInteger bigMultiplyPowerTen(int i2) {
        if (i2 <= 0) {
            return inflated();
        }
        if (this.intCompact != Long.MIN_VALUE) {
            return bigTenToThe(i2).multiply(this.intCompact);
        }
        return this.intVal.multiply(bigTenToThe(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BigInteger inflated() {
        if (this.intVal == null) {
            return BigInteger.valueOf(this.intCompact);
        }
        return this.intVal;
    }

    private static void matchScale(BigDecimal[] bigDecimalArr) {
        if (bigDecimalArr[0].scale == bigDecimalArr[1].scale) {
            return;
        }
        if (bigDecimalArr[0].scale < bigDecimalArr[1].scale) {
            bigDecimalArr[0] = bigDecimalArr[0].setScale(bigDecimalArr[1].scale, 7);
        } else if (bigDecimalArr[1].scale < bigDecimalArr[0].scale) {
            bigDecimalArr[1] = bigDecimalArr[1].setScale(bigDecimalArr[0].scale, 7);
        }
    }

    /* loaded from: rt.jar:java/math/BigDecimal$UnsafeHolder.class */
    private static class UnsafeHolder {
        private static final Unsafe unsafe;
        private static final long intCompactOffset;
        private static final long intValOffset;
        private static final long scaleOffset;

        private UnsafeHolder() {
        }

        static {
            try {
                unsafe = Unsafe.getUnsafe();
                intCompactOffset = unsafe.objectFieldOffset(BigDecimal.class.getDeclaredField("intCompact"));
                intValOffset = unsafe.objectFieldOffset(BigDecimal.class.getDeclaredField("intVal"));
                scaleOffset = unsafe.objectFieldOffset(BigDecimal.class.getDeclaredField("scale"));
            } catch (Exception e2) {
                throw new ExceptionInInitializerError(e2);
            }
        }

        static void setIntValAndScaleVolatile(BigDecimal bigDecimal, BigInteger bigInteger, int i2) {
            unsafe.putObjectVolatile(bigDecimal, intValOffset, bigInteger);
            unsafe.putIntVolatile(bigDecimal, scaleOffset, i2);
            unsafe.putLongVolatile(bigDecimal, intCompactOffset, BigDecimal.compactValFor(bigInteger));
        }

        static void setIntValVolatile(BigDecimal bigDecimal, BigInteger bigInteger) {
            unsafe.putObjectVolatile(bigDecimal, intValOffset, bigInteger);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        BigInteger bigInteger = (BigInteger) fields.get("intVal", (Object) null);
        if (bigInteger == null) {
            throw new StreamCorruptedException("Null or missing intVal in BigDecimal stream");
        }
        UnsafeHolder.setIntValAndScaleVolatile(this, toStrictBigInteger(bigInteger), fields.get("scale", 0));
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialized BigDecimal objects need data");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.intVal == null) {
            UnsafeHolder.setIntValVolatile(this, BigInteger.valueOf(this.intCompact));
        }
        objectOutputStream.defaultWriteObject();
    }

    static int longDigitLength(long j2) {
        if (!$assertionsDisabled && j2 == Long.MIN_VALUE) {
            throw new AssertionError();
        }
        if (j2 < 0) {
            j2 = -j2;
        }
        if (j2 < 10) {
            return 1;
        }
        int iNumberOfLeadingZeros = (((64 - Long.numberOfLeadingZeros(j2)) + 1) * 1233) >>> 12;
        long[] jArr = LONG_TEN_POWERS_TABLE;
        return (iNumberOfLeadingZeros >= jArr.length || j2 < jArr[iNumberOfLeadingZeros]) ? iNumberOfLeadingZeros : iNumberOfLeadingZeros + 1;
    }

    private static int bigDigitLength(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return 1;
        }
        int iBitLength = (int) (((bigInteger.bitLength() + 1) * 646456993) >>> 31);
        return bigInteger.compareMagnitude(bigTenToThe(iBitLength)) < 0 ? iBitLength : iBitLength + 1;
    }

    private int checkScale(long j2) {
        BigInteger bigInteger;
        int i2 = (int) j2;
        if (i2 != j2) {
            i2 = j2 > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (this.intCompact != 0 && ((bigInteger = this.intVal) == null || bigInteger.signum() != 0)) {
                throw new ArithmeticException(i2 > 0 ? "Underflow" : "Overflow");
            }
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long compactValFor(BigInteger bigInteger) {
        int[] iArr = bigInteger.mag;
        int length = iArr.length;
        if (length == 0) {
            return 0L;
        }
        int i2 = iArr[0];
        if (length > 2) {
            return Long.MIN_VALUE;
        }
        if (length == 2 && i2 < 0) {
            return Long.MIN_VALUE;
        }
        long j2 = length == 2 ? (iArr[1] & 4294967295L) + (i2 << 32) : i2 & 4294967295L;
        return bigInteger.signum < 0 ? -j2 : j2;
    }

    private static int longCompareMagnitude(long j2, long j3) {
        if (j2 < 0) {
            j2 = -j2;
        }
        if (j3 < 0) {
            j3 = -j3;
        }
        if (j2 < j3) {
            return -1;
        }
        return j2 == j3 ? 0 : 1;
    }

    private static int saturateLong(long j2) {
        int i2 = (int) j2;
        return j2 == ((long) i2) ? i2 : j2 < 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    private static void print(String str, BigDecimal bigDecimal) {
        System.err.format("%s:\tintCompact %d\tintVal %d\tscale %d\tprecision %d%n", str, Long.valueOf(bigDecimal.intCompact), bigDecimal.intVal, Integer.valueOf(bigDecimal.scale), Integer.valueOf(bigDecimal.precision));
    }

    private BigDecimal audit() {
        if (this.intCompact == Long.MIN_VALUE) {
            if (this.intVal == null) {
                print("audit", this);
                throw new AssertionError((Object) "null intVal");
            }
            if (this.precision > 0 && this.precision != bigDigitLength(this.intVal)) {
                print("audit", this);
                throw new AssertionError((Object) "precision mismatch");
            }
        } else {
            if (this.intVal != null) {
                long jLongValue = this.intVal.longValue();
                if (jLongValue != this.intCompact) {
                    print("audit", this);
                    throw new AssertionError((Object) ("Inconsistent state, intCompact=" + this.intCompact + "\t intVal=" + jLongValue));
                }
            }
            if (this.precision > 0 && this.precision != longDigitLength(this.intCompact)) {
                print("audit", this);
                throw new AssertionError((Object) "precision mismatch");
            }
        }
        return this;
    }

    private static int checkScaleNonZero(long j2) {
        int i2 = (int) j2;
        if (i2 != j2) {
            throw new ArithmeticException(i2 > 0 ? "Underflow" : "Overflow");
        }
        return i2;
    }

    private static int checkScale(long j2, long j3) {
        int i2 = (int) j3;
        if (i2 != j3) {
            i2 = j3 > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (j2 != 0) {
                throw new ArithmeticException(i2 > 0 ? "Underflow" : "Overflow");
            }
        }
        return i2;
    }

    private static int checkScale(BigInteger bigInteger, long j2) {
        int i2 = (int) j2;
        if (i2 != j2) {
            i2 = j2 > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            if (bigInteger.signum() != 0) {
                throw new ArithmeticException(i2 > 0 ? "Underflow" : "Overflow");
            }
        }
        return i2;
    }

    private static BigDecimal doRound(BigDecimal bigDecimal, MathContext mathContext) {
        int i2 = mathContext.precision;
        boolean z2 = false;
        if (i2 > 0) {
            BigInteger bigIntegerDivideAndRoundByTenPow = bigDecimal.intVal;
            long jCompactValFor = bigDecimal.intCompact;
            int iCheckScaleNonZero = bigDecimal.scale;
            int iPrecision = bigDecimal.precision();
            int i3 = mathContext.roundingMode.oldMode;
            if (jCompactValFor == Long.MIN_VALUE) {
                while (true) {
                    int i4 = iPrecision - i2;
                    if (i4 <= 0) {
                        break;
                    }
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i4);
                    bigIntegerDivideAndRoundByTenPow = divideAndRoundByTenPow(bigIntegerDivideAndRoundByTenPow, i4, i3);
                    z2 = true;
                    jCompactValFor = compactValFor(bigIntegerDivideAndRoundByTenPow);
                    if (jCompactValFor != Long.MIN_VALUE) {
                        iPrecision = longDigitLength(jCompactValFor);
                        break;
                    }
                    iPrecision = bigDigitLength(bigIntegerDivideAndRoundByTenPow);
                }
            }
            if (jCompactValFor != Long.MIN_VALUE) {
                int i5 = iPrecision - i2;
                while (i5 > 0) {
                    iCheckScaleNonZero = checkScaleNonZero(iCheckScaleNonZero - i5);
                    jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i5], mathContext.roundingMode.oldMode);
                    z2 = true;
                    iPrecision = longDigitLength(jCompactValFor);
                    i5 = iPrecision - i2;
                    bigIntegerDivideAndRoundByTenPow = null;
                }
            }
            return z2 ? new BigDecimal(bigIntegerDivideAndRoundByTenPow, jCompactValFor, iCheckScaleNonZero, iPrecision) : bigDecimal;
        }
        return bigDecimal;
    }

    private static BigDecimal doRound(long j2, int i2, MathContext mathContext) {
        int i3 = mathContext.precision;
        if (i3 > 0 && i3 < 19) {
            int iLongDigitLength = longDigitLength(j2);
            while (true) {
                int i4 = iLongDigitLength;
                int i5 = i4 - i3;
                if (i5 > 0) {
                    i2 = checkScaleNonZero(i2 - i5);
                    j2 = divideAndRound(j2, LONG_TEN_POWERS_TABLE[i5], mathContext.roundingMode.oldMode);
                    iLongDigitLength = longDigitLength(j2);
                } else {
                    return valueOf(j2, i2, i4);
                }
            }
        } else {
            return valueOf(j2, i2);
        }
    }

    private static BigDecimal doRound(BigInteger bigInteger, int i2, MathContext mathContext) {
        int i3 = mathContext.precision;
        int i4 = 0;
        if (i3 > 0) {
            long jCompactValFor = compactValFor(bigInteger);
            int i5 = mathContext.roundingMode.oldMode;
            if (jCompactValFor == Long.MIN_VALUE) {
                int iBigDigitLength = bigDigitLength(bigInteger);
                while (true) {
                    i4 = iBigDigitLength;
                    int i6 = i4 - i3;
                    if (i6 <= 0) {
                        break;
                    }
                    i2 = checkScaleNonZero(i2 - i6);
                    bigInteger = divideAndRoundByTenPow(bigInteger, i6, i5);
                    jCompactValFor = compactValFor(bigInteger);
                    if (jCompactValFor != Long.MIN_VALUE) {
                        break;
                    }
                    iBigDigitLength = bigDigitLength(bigInteger);
                }
            }
            if (jCompactValFor != Long.MIN_VALUE) {
                int iLongDigitLength = longDigitLength(jCompactValFor);
                while (true) {
                    int i7 = iLongDigitLength;
                    int i8 = i7 - i3;
                    if (i8 > 0) {
                        i2 = checkScaleNonZero(i2 - i8);
                        jCompactValFor = divideAndRound(jCompactValFor, LONG_TEN_POWERS_TABLE[i8], mathContext.roundingMode.oldMode);
                        iLongDigitLength = longDigitLength(jCompactValFor);
                    } else {
                        return valueOf(jCompactValFor, i2, i7);
                    }
                }
            }
        }
        return new BigDecimal(bigInteger, Long.MIN_VALUE, i2, i4);
    }

    private static BigInteger divideAndRoundByTenPow(BigInteger bigInteger, int i2, int i3) {
        BigInteger bigIntegerDivideAndRound;
        if (i2 < LONG_TEN_POWERS_TABLE.length) {
            bigIntegerDivideAndRound = divideAndRound(bigInteger, LONG_TEN_POWERS_TABLE[i2], i3);
        } else {
            bigIntegerDivideAndRound = divideAndRound(bigInteger, bigTenToThe(i2), i3);
        }
        return bigIntegerDivideAndRound;
    }

    private static BigDecimal divideAndRound(long j2, long j3, int i2, int i3, int i4) {
        long j4 = j2 / j3;
        if (i3 == 1 && i2 == i4) {
            return valueOf(j4, i2);
        }
        long j5 = j2 % j3;
        int i5 = ((j2 > 0L ? 1 : (j2 == 0L ? 0 : -1)) < 0) == ((j3 > 0L ? 1 : (j3 == 0L ? 0 : -1)) < 0) ? 1 : -1;
        if (j5 != 0) {
            return valueOf(needIncrement(j3, i3, i5, j4, j5) ? j4 + i5 : j4, i2);
        }
        if (i4 != i2) {
            return createAndStripZerosToMatchScale(j4, i2, i4);
        }
        return valueOf(j4, i2);
    }

    private static long divideAndRound(long j2, long j3, int i2) {
        long j4 = j2 / j3;
        if (i2 == 1) {
            return j4;
        }
        long j5 = j2 % j3;
        int i3 = ((j2 > 0L ? 1 : (j2 == 0L ? 0 : -1)) < 0) == ((j3 > 0L ? 1 : (j3 == 0L ? 0 : -1)) < 0) ? 1 : -1;
        if (j5 != 0) {
            return needIncrement(j3, i2, i3, j4, j5) ? j4 + i3 : j4;
        }
        return j4;
    }

    private static boolean commonNeedIncrement(int i2, int i3, int i4, boolean z2) {
        switch (i2) {
            case 0:
                return true;
            case 1:
                return false;
            case 2:
                return i3 > 0;
            case 3:
                return i3 < 0;
            case 4:
            case 5:
            case 6:
            default:
                if (!$assertionsDisabled && (i2 < 4 || i2 > 6)) {
                    throw new AssertionError((Object) ("Unexpected rounding mode" + ((Object) RoundingMode.valueOf(i2))));
                }
                if (i4 < 0) {
                    return false;
                }
                if (i4 > 0) {
                    return true;
                }
                if (!$assertionsDisabled && i4 != 0) {
                    throw new AssertionError();
                }
                switch (i2) {
                    case 4:
                        return true;
                    case 5:
                        return false;
                    case 6:
                        return z2;
                    default:
                        throw new AssertionError((Object) ("Unexpected rounding mode" + i2));
                }
            case 7:
                throw new ArithmeticException("Rounding necessary");
        }
    }

    private static boolean needIncrement(long j2, int i2, int i3, long j3, long j4) {
        int iLongCompareMagnitude;
        if (!$assertionsDisabled && j4 == 0) {
            throw new AssertionError();
        }
        if (j4 <= HALF_LONG_MIN_VALUE || j4 > HALF_LONG_MAX_VALUE) {
            iLongCompareMagnitude = 1;
        } else {
            iLongCompareMagnitude = longCompareMagnitude(2 * j4, j2);
        }
        return commonNeedIncrement(i2, i3, iLongCompareMagnitude, (j3 & 1) != 0);
    }

    private static BigInteger divideAndRound(BigInteger bigInteger, long j2, int i2) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        long jDivide = mutableBigInteger.divide(j2, mutableBigInteger2);
        boolean z2 = jDivide == 0;
        int i3 = j2 < 0 ? -bigInteger.signum : bigInteger.signum;
        if (!z2 && needIncrement(j2, i2, i3, mutableBigInteger2, jDivide)) {
            mutableBigInteger2.add(MutableBigInteger.ONE);
        }
        return mutableBigInteger2.toBigInteger(i3);
    }

    private static BigDecimal divideAndRound(BigInteger bigInteger, long j2, int i2, int i3, int i4) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        long jDivide = mutableBigInteger.divide(j2, mutableBigInteger2);
        boolean z2 = jDivide == 0;
        int i5 = j2 < 0 ? -bigInteger.signum : bigInteger.signum;
        if (!z2) {
            if (needIncrement(j2, i3, i5, mutableBigInteger2, jDivide)) {
                mutableBigInteger2.add(MutableBigInteger.ONE);
            }
            return mutableBigInteger2.toBigDecimal(i5, i2);
        }
        if (i4 != i2) {
            long compactValue = mutableBigInteger2.toCompactValue(i5);
            if (compactValue != Long.MIN_VALUE) {
                return createAndStripZerosToMatchScale(compactValue, i2, i4);
            }
            return createAndStripZerosToMatchScale(mutableBigInteger2.toBigInteger(i5), i2, i4);
        }
        return mutableBigInteger2.toBigDecimal(i5, i2);
    }

    private static boolean needIncrement(long j2, int i2, int i3, MutableBigInteger mutableBigInteger, long j3) {
        int iLongCompareMagnitude;
        if (!$assertionsDisabled && j3 == 0) {
            throw new AssertionError();
        }
        if (j3 <= HALF_LONG_MIN_VALUE || j3 > HALF_LONG_MAX_VALUE) {
            iLongCompareMagnitude = 1;
        } else {
            iLongCompareMagnitude = longCompareMagnitude(2 * j3, j2);
        }
        return commonNeedIncrement(i2, i3, iLongCompareMagnitude, mutableBigInteger.isOdd());
    }

    private static BigInteger divideAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i2) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(bigInteger2.mag);
        MutableBigInteger mutableBigIntegerDivide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger2);
        boolean zIsZero = mutableBigIntegerDivide.isZero();
        int i3 = bigInteger.signum != bigInteger2.signum ? -1 : 1;
        if (!zIsZero && needIncrement(mutableBigInteger3, i2, i3, mutableBigInteger2, mutableBigIntegerDivide)) {
            mutableBigInteger2.add(MutableBigInteger.ONE);
        }
        return mutableBigInteger2.toBigInteger(i3);
    }

    private static BigDecimal divideAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i2, int i3, int i4) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger.mag);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(bigInteger2.mag);
        MutableBigInteger mutableBigIntegerDivide = mutableBigInteger.divide(mutableBigInteger3, mutableBigInteger2);
        boolean zIsZero = mutableBigIntegerDivide.isZero();
        int i5 = bigInteger.signum != bigInteger2.signum ? -1 : 1;
        if (!zIsZero) {
            if (needIncrement(mutableBigInteger3, i3, i5, mutableBigInteger2, mutableBigIntegerDivide)) {
                mutableBigInteger2.add(MutableBigInteger.ONE);
            }
            return mutableBigInteger2.toBigDecimal(i5, i2);
        }
        if (i4 != i2) {
            long compactValue = mutableBigInteger2.toCompactValue(i5);
            if (compactValue != Long.MIN_VALUE) {
                return createAndStripZerosToMatchScale(compactValue, i2, i4);
            }
            return createAndStripZerosToMatchScale(mutableBigInteger2.toBigInteger(i5), i2, i4);
        }
        return mutableBigInteger2.toBigDecimal(i5, i2);
    }

    private static boolean needIncrement(MutableBigInteger mutableBigInteger, int i2, int i3, MutableBigInteger mutableBigInteger2, MutableBigInteger mutableBigInteger3) {
        if ($assertionsDisabled || !mutableBigInteger3.isZero()) {
            return commonNeedIncrement(i2, i3, mutableBigInteger3.compareHalf(mutableBigInteger), mutableBigInteger2.isOdd());
        }
        throw new AssertionError();
    }

    private static BigDecimal createAndStripZerosToMatchScale(BigInteger bigInteger, int i2, long j2) {
        while (bigInteger.compareMagnitude(BigInteger.TEN) >= 0 && i2 > j2 && !bigInteger.testBit(0)) {
            BigInteger[] bigIntegerArrDivideAndRemainder = bigInteger.divideAndRemainder(BigInteger.TEN);
            if (bigIntegerArrDivideAndRemainder[1].signum() != 0) {
                break;
            }
            bigInteger = bigIntegerArrDivideAndRemainder[0];
            i2 = checkScale(bigInteger, i2 - 1);
        }
        return valueOf(bigInteger, i2, 0);
    }

    private static BigDecimal createAndStripZerosToMatchScale(long j2, int i2, long j3) {
        while (Math.abs(j2) >= 10 && i2 > j3 && (j2 & 1) == 0 && j2 % 10 == 0) {
            j2 /= 10;
            i2 = checkScale(j2, i2 - 1);
        }
        return valueOf(j2, i2);
    }

    private static BigDecimal stripZerosToMatchScale(BigInteger bigInteger, long j2, int i2, int i3) {
        if (j2 != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(j2, i2, i3);
        }
        return createAndStripZerosToMatchScale(bigInteger == null ? INFLATED_BIGINT : bigInteger, i2, i3);
    }

    private static long add(long j2, long j3) {
        long j4 = j2 + j3;
        if (((j4 ^ j2) & (j4 ^ j3)) >= 0) {
            return j4;
        }
        return Long.MIN_VALUE;
    }

    private static BigDecimal add(long j2, long j3, int i2) {
        long jAdd = add(j2, j3);
        if (jAdd != Long.MIN_VALUE) {
            return valueOf(jAdd, i2);
        }
        return new BigDecimal(BigInteger.valueOf(j2).add(j3), i2);
    }

    private static BigDecimal add(long j2, int i2, long j3, int i3) {
        long j4 = i2 - i3;
        if (j4 == 0) {
            return add(j2, j3, i2);
        }
        if (j4 < 0) {
            int iCheckScale = checkScale(j2, -j4);
            long jLongMultiplyPowerTen = longMultiplyPowerTen(j2, iCheckScale);
            if (jLongMultiplyPowerTen != Long.MIN_VALUE) {
                return add(jLongMultiplyPowerTen, j3, i3);
            }
            BigInteger bigIntegerAdd = bigMultiplyPowerTen(j2, iCheckScale).add(j3);
            return (j2 ^ j3) >= 0 ? new BigDecimal(bigIntegerAdd, Long.MIN_VALUE, i3, 0) : valueOf(bigIntegerAdd, i3, 0);
        }
        int iCheckScale2 = checkScale(j3, j4);
        long jLongMultiplyPowerTen2 = longMultiplyPowerTen(j3, iCheckScale2);
        if (jLongMultiplyPowerTen2 != Long.MIN_VALUE) {
            return add(j2, jLongMultiplyPowerTen2, i2);
        }
        BigInteger bigIntegerAdd2 = bigMultiplyPowerTen(j3, iCheckScale2).add(j2);
        return (j2 ^ j3) >= 0 ? new BigDecimal(bigIntegerAdd2, Long.MIN_VALUE, i2, 0) : valueOf(bigIntegerAdd2, i2, 0);
    }

    private static BigDecimal add(long j2, int i2, BigInteger bigInteger, int i3) {
        BigInteger bigIntegerAdd;
        int i4 = i2;
        long j3 = i4 - i3;
        boolean z2 = Long.signum(j2) == bigInteger.signum;
        if (j3 < 0) {
            int iCheckScale = checkScale(j2, -j3);
            i4 = i3;
            long jLongMultiplyPowerTen = longMultiplyPowerTen(j2, iCheckScale);
            if (jLongMultiplyPowerTen == Long.MIN_VALUE) {
                bigIntegerAdd = bigInteger.add(bigMultiplyPowerTen(j2, iCheckScale));
            } else {
                bigIntegerAdd = bigInteger.add(jLongMultiplyPowerTen);
            }
        } else {
            bigIntegerAdd = bigMultiplyPowerTen(bigInteger, checkScale(bigInteger, j3)).add(j2);
        }
        return z2 ? new BigDecimal(bigIntegerAdd, Long.MIN_VALUE, i4, 0) : valueOf(bigIntegerAdd, i4, 0);
    }

    private static BigDecimal add(BigInteger bigInteger, int i2, BigInteger bigInteger2, int i3) {
        int i4 = i2;
        long j2 = i4 - i3;
        if (j2 != 0) {
            if (j2 < 0) {
                i4 = i3;
                bigInteger = bigMultiplyPowerTen(bigInteger, checkScale(bigInteger, -j2));
            } else {
                bigInteger2 = bigMultiplyPowerTen(bigInteger2, checkScale(bigInteger2, j2));
            }
        }
        BigInteger bigIntegerAdd = bigInteger.add(bigInteger2);
        return bigInteger.signum == bigInteger2.signum ? new BigDecimal(bigIntegerAdd, Long.MIN_VALUE, i4, 0) : valueOf(bigIntegerAdd, i4, 0);
    }

    private static BigInteger bigMultiplyPowerTen(long j2, int i2) {
        if (i2 <= 0) {
            return BigInteger.valueOf(j2);
        }
        return bigTenToThe(i2).multiply(j2);
    }

    private static BigInteger bigMultiplyPowerTen(BigInteger bigInteger, int i2) {
        if (i2 <= 0) {
            return bigInteger;
        }
        if (i2 < LONG_TEN_POWERS_TABLE.length) {
            return bigInteger.multiply(LONG_TEN_POWERS_TABLE[i2]);
        }
        return bigInteger.multiply(bigTenToThe(i2));
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:183)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:242)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
        */
    /* JADX WARN: Failed to calculate best type for var: r3v0 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:156)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:133)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:238)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Failed to calculate best type for var: r3v0 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:145)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:123)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:101)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:101)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /* JADX WARN: Not initialized variable reg: 3, insn: MOVE (r1 I:??) = (r3 I:??), block:B:54:0x01bc */
    private static java.math.BigDecimal divideSmallFastPath(long r11, int r13, long r14, int r16, long r17, java.math.MathContext r19) {
        /*
            Method dump skipped, instructions count: 555
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigDecimal.divideSmallFastPath(long, int, long, int, long, java.math.MathContext):java.math.BigDecimal");
    }

    private static BigDecimal divide(long j2, int i2, long j3, int i3, long j4, MathContext mathContext) {
        BigDecimal bigDecimalDivideAndRound;
        int i4 = mathContext.precision;
        if (i2 <= i3 && i3 < 18 && i4 < 18) {
            return divideSmallFastPath(j2, i2, j3, i3, j4, mathContext);
        }
        if (compareMagnitudeNormalized(j2, i2, j3, i3) > 0) {
            i3--;
        }
        int i5 = mathContext.roundingMode.oldMode;
        int iCheckScaleNonZero = checkScaleNonZero(((j4 + i3) - i2) + i4);
        if (checkScaleNonZero((i4 + i3) - i2) > 0) {
            int iCheckScaleNonZero2 = checkScaleNonZero((i4 + i3) - i2);
            if (longMultiplyPowerTen(j2, iCheckScaleNonZero2) == Long.MIN_VALUE) {
                bigDecimalDivideAndRound = divideAndRound(bigMultiplyPowerTen(j2, iCheckScaleNonZero2), j3, iCheckScaleNonZero, i5, checkScaleNonZero(j4));
            } else {
                bigDecimalDivideAndRound = divideAndRound(j3, j3, iCheckScaleNonZero, i5, checkScaleNonZero(j4));
            }
        } else {
            int iCheckScaleNonZero3 = checkScaleNonZero(i2 - i4);
            if (iCheckScaleNonZero3 == i3) {
                bigDecimalDivideAndRound = divideAndRound(j2, j3, iCheckScaleNonZero, i5, checkScaleNonZero(j4));
            } else {
                int iCheckScaleNonZero4 = checkScaleNonZero(iCheckScaleNonZero3 - i3);
                long jLongMultiplyPowerTen = longMultiplyPowerTen(j3, iCheckScaleNonZero4);
                if (jLongMultiplyPowerTen == Long.MIN_VALUE) {
                    bigDecimalDivideAndRound = divideAndRound(BigInteger.valueOf(j2), bigMultiplyPowerTen(j3, iCheckScaleNonZero4), iCheckScaleNonZero, i5, checkScaleNonZero(j4));
                } else {
                    bigDecimalDivideAndRound = divideAndRound(j2, jLongMultiplyPowerTen, iCheckScaleNonZero, i5, checkScaleNonZero(j4));
                }
            }
        }
        return doRound(bigDecimalDivideAndRound, mathContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static BigDecimal divide(BigInteger bigInteger, int i2, long j2, int i3, long j3, MathContext mathContext) {
        BigDecimal bigDecimalDivideAndRound;
        if ((-compareMagnitudeNormalized(j2, i3, bigInteger, i2)) > 0) {
            i3--;
        }
        int i4 = mathContext.precision;
        int i5 = mathContext.roundingMode.oldMode;
        int iCheckScaleNonZero = checkScaleNonZero(((j3 + i3) - i2) + i4);
        if (checkScaleNonZero((i4 + i3) - i2) > 0) {
            bigDecimalDivideAndRound = divideAndRound(bigMultiplyPowerTen(bigInteger, checkScaleNonZero((i4 + i3) - i2)), j2, iCheckScaleNonZero, i5, checkScaleNonZero(j3));
        } else {
            int iCheckScaleNonZero2 = checkScaleNonZero(i2 - i4);
            if (iCheckScaleNonZero2 == i3) {
                bigDecimalDivideAndRound = divideAndRound(bigInteger, j2, iCheckScaleNonZero, i5, checkScaleNonZero(j3));
            } else {
                int iCheckScaleNonZero3 = checkScaleNonZero(iCheckScaleNonZero2 - i3);
                if (longMultiplyPowerTen(j2, iCheckScaleNonZero3) == Long.MIN_VALUE) {
                    bigDecimalDivideAndRound = divideAndRound(bigInteger, bigMultiplyPowerTen(j2, iCheckScaleNonZero3), iCheckScaleNonZero, i5, checkScaleNonZero(j3));
                } else {
                    bigDecimalDivideAndRound = divideAndRound(bigInteger, (long) bigInteger, iCheckScaleNonZero, i5, checkScaleNonZero(j3));
                }
            }
        }
        return doRound(bigDecimalDivideAndRound, mathContext);
    }

    private static BigDecimal divide(long j2, int i2, BigInteger bigInteger, int i3, long j3, MathContext mathContext) {
        BigDecimal bigDecimalDivideAndRound;
        if (compareMagnitudeNormalized(j2, i2, bigInteger, i3) > 0) {
            i3--;
        }
        int i4 = mathContext.precision;
        int i5 = mathContext.roundingMode.oldMode;
        int iCheckScaleNonZero = checkScaleNonZero(((j3 + i3) - i2) + i4);
        if (checkScaleNonZero((i4 + i3) - i2) > 0) {
            bigDecimalDivideAndRound = divideAndRound(bigMultiplyPowerTen(j2, checkScaleNonZero((i4 + i3) - i2)), bigInteger, iCheckScaleNonZero, i5, checkScaleNonZero(j3));
        } else {
            bigDecimalDivideAndRound = divideAndRound(BigInteger.valueOf(j2), bigMultiplyPowerTen(bigInteger, checkScaleNonZero(checkScaleNonZero(i2 - i4) - i3)), iCheckScaleNonZero, i5, checkScaleNonZero(j3));
        }
        return doRound(bigDecimalDivideAndRound, mathContext);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i2, BigInteger bigInteger2, int i3, long j2, MathContext mathContext) {
        BigDecimal bigDecimalDivideAndRound;
        if (compareMagnitudeNormalized(bigInteger, i2, bigInteger2, i3) > 0) {
            i3--;
        }
        int i4 = mathContext.precision;
        int i5 = mathContext.roundingMode.oldMode;
        int iCheckScaleNonZero = checkScaleNonZero(((j2 + i3) - i2) + i4);
        if (checkScaleNonZero((i4 + i3) - i2) > 0) {
            bigDecimalDivideAndRound = divideAndRound(bigMultiplyPowerTen(bigInteger, checkScaleNonZero((i4 + i3) - i2)), bigInteger2, iCheckScaleNonZero, i5, checkScaleNonZero(j2));
        } else {
            bigDecimalDivideAndRound = divideAndRound(bigInteger, bigMultiplyPowerTen(bigInteger2, checkScaleNonZero(checkScaleNonZero(i2 - i4) - i3)), iCheckScaleNonZero, i5, checkScaleNonZero(j2));
        }
        return doRound(bigDecimalDivideAndRound, mathContext);
    }

    private static BigDecimal multiplyDivideAndRound(long j2, long j3, long j4, int i2, int i3, int i4) {
        int iSignum = Long.signum(j2) * Long.signum(j3) * Long.signum(j4);
        long jAbs = Math.abs(j2);
        long jAbs2 = Math.abs(j3);
        long j5 = jAbs >>> 32;
        long j6 = jAbs & 4294967295L;
        long j7 = jAbs2 >>> 32;
        long j8 = jAbs2 & 4294967295L;
        long j9 = j6 * j8;
        long j10 = j9 & 4294967295L;
        long j11 = (j5 * j8) + (j9 >>> 32);
        long j12 = j11 & 4294967295L;
        long j13 = j11 >>> 32;
        long j14 = (j6 * j7) + j12;
        long j15 = j14 & 4294967295L;
        long j16 = j13 + (j14 >>> 32);
        long j17 = j16 >>> 32;
        long j18 = (j5 * j7) + (j16 & 4294967295L);
        return divideAndRound128(make64(((j18 >>> 32) + j17) & 4294967295L, j18 & 4294967295L), make64(j15, j10), Math.abs(j4), iSignum, i2, i3, i4);
    }

    private static BigDecimal divideAndRound128(long j2, long j3, long j4, int i2, int i3, int i4, int i5) {
        long j5;
        long j6;
        long j7;
        long j8;
        if (j2 >= j4) {
            return null;
        }
        int iNumberOfLeadingZeros = Long.numberOfLeadingZeros(j4);
        long j9 = j4 << iNumberOfLeadingZeros;
        long j10 = j9 >>> 32;
        long j11 = j9 & 4294967295L;
        long j12 = j3 << iNumberOfLeadingZeros;
        long j13 = j12 >>> 32;
        long j14 = j12 & 4294967295L;
        long j15 = (j2 << iNumberOfLeadingZeros) | (j3 >>> (64 - iNumberOfLeadingZeros));
        long j16 = j15 & 4294967295L;
        if (j10 == 1) {
            j5 = j15;
            j6 = 0;
        } else if (j15 >= 0) {
            j5 = j15 / j10;
            j6 = j15 - (j5 * j10);
        } else {
            long[] jArrDivRemNegativeLong = divRemNegativeLong(j15, j10);
            j5 = jArrDivRemNegativeLong[1];
            j6 = jArrDivRemNegativeLong[0];
        }
        do {
            if (j5 < 4294967296L && !unsignedLongCompare(j5 * j11, make64(j6, j13))) {
                break;
            }
            j5--;
            j6 += j10;
        } while (j6 < 4294967296L);
        long jMulsub = mulsub(j16, j13, j10, j11, j5);
        long j17 = jMulsub & 4294967295L;
        if (j10 == 1) {
            j7 = jMulsub;
            j8 = 0;
        } else if (jMulsub >= 0) {
            j7 = jMulsub / j10;
            j8 = jMulsub - (j7 * j10);
        } else {
            long[] jArrDivRemNegativeLong2 = divRemNegativeLong(jMulsub, j10);
            j7 = jArrDivRemNegativeLong2[1];
            j8 = jArrDivRemNegativeLong2[0];
        }
        do {
            if (j7 < 4294967296L && !unsignedLongCompare(j7 * j11, make64(j8, j14))) {
                break;
            }
            j7--;
            j8 += j10;
        } while (j8 < 4294967296L);
        if (((int) j5) < 0) {
            MutableBigInteger mutableBigInteger = new MutableBigInteger(new int[]{(int) j5, (int) j7});
            if (i4 == 1 && i3 == i5) {
                return mutableBigInteger.toBigDecimal(i2, i3);
            }
            long jMulsub2 = mulsub(j17, j14, j10, j11, j7) >>> iNumberOfLeadingZeros;
            if (jMulsub2 != 0) {
                if (needIncrement(j9 >>> iNumberOfLeadingZeros, i4, i2, mutableBigInteger, jMulsub2)) {
                    mutableBigInteger.add(MutableBigInteger.ONE);
                }
                return mutableBigInteger.toBigDecimal(i2, i3);
            }
            if (i5 != i3) {
                return createAndStripZerosToMatchScale(mutableBigInteger.toBigInteger(i2), i3, i5);
            }
            return mutableBigInteger.toBigDecimal(i2, i3);
        }
        long jMake64 = make64(j5, j7) * i2;
        if (i4 == 1 && i3 == i5) {
            return valueOf(jMake64, i3);
        }
        long jMulsub3 = mulsub(j17, j14, j10, j11, j7) >>> iNumberOfLeadingZeros;
        if (jMulsub3 != 0) {
            return valueOf(needIncrement(j9 >>> iNumberOfLeadingZeros, i4, i2, jMake64, jMulsub3) ? jMake64 + i2 : jMake64, i3);
        }
        if (i5 != i3) {
            return createAndStripZerosToMatchScale(jMake64, i3, i5);
        }
        return valueOf(jMake64, i3);
    }

    private static BigDecimal roundedTenPower(int i2, int i3, int i4, int i5) {
        if (i4 > i5) {
            int i6 = i4 - i5;
            if (i6 < i3) {
                return scaledTenPow(i3 - i6, i2, i5);
            }
            return valueOf(i2, i4 - i3);
        }
        return scaledTenPow(i3, i2, i4);
    }

    static BigDecimal scaledTenPow(int i2, int i3, int i4) {
        if (i2 < LONG_TEN_POWERS_TABLE.length) {
            return valueOf(i3 * LONG_TEN_POWERS_TABLE[i2], i4);
        }
        BigInteger bigIntegerBigTenToThe = bigTenToThe(i2);
        if (i3 == -1) {
            bigIntegerBigTenToThe = bigIntegerBigTenToThe.negate();
        }
        return new BigDecimal(bigIntegerBigTenToThe, Long.MIN_VALUE, i4, i2 + 1);
    }

    private static long[] divRemNegativeLong(long j2, long j3) {
        if (!$assertionsDisabled && j2 >= 0) {
            throw new AssertionError((Object) ("Non-negative numerator " + j2));
        }
        if (!$assertionsDisabled && j3 == 1) {
            throw new AssertionError((Object) "Unity denominator");
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
        return new long[]{j5, j4};
    }

    private static long make64(long j2, long j3) {
        return (j2 << 32) | j3;
    }

    private static long mulsub(long j2, long j3, long j4, long j5, long j6) {
        long j7 = j3 - (j6 * j5);
        return make64((j2 + (j7 >>> 32)) - (j6 * j4), j7 & 4294967295L);
    }

    private static boolean unsignedLongCompare(long j2, long j3) {
        return j2 + Long.MIN_VALUE > j3 + Long.MIN_VALUE;
    }

    private static boolean unsignedLongCompareEq(long j2, long j3) {
        return j2 + Long.MIN_VALUE >= j3 + Long.MIN_VALUE;
    }

    private static int compareMagnitudeNormalized(long j2, int i2, long j3, int i3) {
        int i4 = i2 - i3;
        if (i4 != 0) {
            if (i4 < 0) {
                j2 = longMultiplyPowerTen(j2, -i4);
            } else {
                j3 = longMultiplyPowerTen(j3, i4);
            }
        }
        if (j2 == Long.MIN_VALUE) {
            return 1;
        }
        if (j3 != Long.MIN_VALUE) {
            return longCompareMagnitude(j2, j3);
        }
        return -1;
    }

    private static int compareMagnitudeNormalized(long j2, int i2, BigInteger bigInteger, int i3) {
        int i4;
        if (j2 != 0 && (i4 = i2 - i3) < 0 && longMultiplyPowerTen(j2, -i4) == Long.MIN_VALUE) {
            return bigMultiplyPowerTen(j2, -i4).compareMagnitude(bigInteger);
        }
        return -1;
    }

    private static int compareMagnitudeNormalized(BigInteger bigInteger, int i2, BigInteger bigInteger2, int i3) {
        int i4 = i2 - i3;
        if (i4 < 0) {
            return bigMultiplyPowerTen(bigInteger, -i4).compareMagnitude(bigInteger2);
        }
        return bigInteger.compareMagnitude(bigMultiplyPowerTen(bigInteger2, i4));
    }

    private static long multiply(long j2, long j3) {
        long j4 = j2 * j3;
        if (((Math.abs(j2) | Math.abs(j3)) >>> 31) == 0 || j3 == 0 || j4 / j3 == j2) {
            return j4;
        }
        return Long.MIN_VALUE;
    }

    private static BigDecimal multiply(long j2, long j3, int i2) {
        long jMultiply = multiply(j2, j3);
        if (jMultiply != Long.MIN_VALUE) {
            return valueOf(jMultiply, i2);
        }
        return new BigDecimal(BigInteger.valueOf(j2).multiply(j3), Long.MIN_VALUE, i2, 0);
    }

    private static BigDecimal multiply(long j2, BigInteger bigInteger, int i2) {
        if (j2 == 0) {
            return zeroValueOf(i2);
        }
        return new BigDecimal(bigInteger.multiply(j2), Long.MIN_VALUE, i2, 0);
    }

    private static BigDecimal multiply(BigInteger bigInteger, BigInteger bigInteger2, int i2) {
        return new BigDecimal(bigInteger.multiply(bigInteger2), Long.MIN_VALUE, i2, 0);
    }

    private static BigDecimal multiplyAndRound(long j2, long j3, int i2, MathContext mathContext) {
        long jMultiply = multiply(j2, j3);
        if (jMultiply != Long.MIN_VALUE) {
            return doRound(jMultiply, i2, mathContext);
        }
        int i3 = 1;
        if (j2 < 0) {
            j2 = -j2;
            i3 = -1;
        }
        if (j3 < 0) {
            j3 = -j3;
            i3 *= -1;
        }
        long j4 = j2 >>> 32;
        long j5 = j2 & 4294967295L;
        long j6 = j3 >>> 32;
        long j7 = j3 & 4294967295L;
        long j8 = j5 * j7;
        long j9 = j8 & 4294967295L;
        long j10 = (j4 * j7) + (j8 >>> 32);
        long j11 = j10 & 4294967295L;
        long j12 = j10 >>> 32;
        long j13 = (j5 * j6) + j11;
        long j14 = j13 & 4294967295L;
        long j15 = j12 + (j13 >>> 32);
        long j16 = j15 >>> 32;
        long j17 = (j4 * j6) + (j15 & 4294967295L);
        BigDecimal bigDecimalDoRound128 = doRound128(make64(((j17 >>> 32) + j16) & 4294967295L, j17 & 4294967295L), make64(j14, j9), i3, i2, mathContext);
        if (bigDecimalDoRound128 != null) {
            return bigDecimalDoRound128;
        }
        return doRound(new BigDecimal(BigInteger.valueOf(j2).multiply(j3 * i3), Long.MIN_VALUE, i2, 0), mathContext);
    }

    private static BigDecimal multiplyAndRound(long j2, BigInteger bigInteger, int i2, MathContext mathContext) {
        if (j2 == 0) {
            return zeroValueOf(i2);
        }
        return doRound(bigInteger.multiply(j2), i2, mathContext);
    }

    private static BigDecimal multiplyAndRound(BigInteger bigInteger, BigInteger bigInteger2, int i2, MathContext mathContext) {
        return doRound(bigInteger.multiply(bigInteger2), i2, mathContext);
    }

    private static BigDecimal doRound128(long j2, long j3, int i2, int i3, MathContext mathContext) {
        BigDecimal bigDecimalDivideAndRound128 = null;
        int iPrecision = precision(j2, j3) - mathContext.precision;
        if (iPrecision > 0 && iPrecision < LONG_TEN_POWERS_TABLE.length) {
            int iCheckScaleNonZero = checkScaleNonZero(i3 - iPrecision);
            bigDecimalDivideAndRound128 = divideAndRound128(j2, j3, LONG_TEN_POWERS_TABLE[iPrecision], i2, iCheckScaleNonZero, mathContext.roundingMode.oldMode, iCheckScaleNonZero);
        }
        if (bigDecimalDivideAndRound128 != null) {
            return doRound(bigDecimalDivideAndRound128, mathContext);
        }
        return null;
    }

    private static int precision(long j2, long j3) {
        if (j2 == 0) {
            if (j3 >= 0) {
                return longDigitLength(j3);
            }
            return unsignedLongCompareEq(j3, LONGLONG_TEN_POWERS_TABLE[0][1]) ? 20 : 19;
        }
        int iNumberOfLeadingZeros = (((128 - Long.numberOfLeadingZeros(j2)) + 1) * 1233) >>> 12;
        int i2 = iNumberOfLeadingZeros - 19;
        return (i2 >= LONGLONG_TEN_POWERS_TABLE.length || longLongCompareMagnitude(j2, j3, LONGLONG_TEN_POWERS_TABLE[i2][0], LONGLONG_TEN_POWERS_TABLE[i2][1])) ? iNumberOfLeadingZeros : iNumberOfLeadingZeros + 1;
    }

    private static boolean longLongCompareMagnitude(long j2, long j3, long j4, long j5) {
        return j2 != j4 ? j2 < j4 : j3 + Long.MIN_VALUE < j5 + Long.MIN_VALUE;
    }

    private static BigDecimal divide(long j2, int i2, long j3, int i3, int i4, int i5) {
        if (checkScale(j2, i4 + i3) > i2) {
            int i6 = (i4 + i3) - i2;
            if (i6 < LONG_TEN_POWERS_TABLE.length) {
                long jLongMultiplyPowerTen = longMultiplyPowerTen(j2, i6);
                if (jLongMultiplyPowerTen != Long.MIN_VALUE) {
                    return divideAndRound(jLongMultiplyPowerTen, j3, i4, i5, i4);
                }
                BigDecimal bigDecimalMultiplyDivideAndRound = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[i6], j2, j3, i4, i5, i4);
                if (bigDecimalMultiplyDivideAndRound != null) {
                    return bigDecimalMultiplyDivideAndRound;
                }
            }
            return divideAndRound(bigMultiplyPowerTen(j2, i6), j3, i4, i5, i4);
        }
        int iCheckScale = checkScale(j3, i2 - i4) - i3;
        if (iCheckScale < LONG_TEN_POWERS_TABLE.length) {
            long jLongMultiplyPowerTen2 = longMultiplyPowerTen(j3, iCheckScale);
            if (jLongMultiplyPowerTen2 != Long.MIN_VALUE) {
                return divideAndRound(j2, jLongMultiplyPowerTen2, i4, i5, i4);
            }
        }
        return divideAndRound(BigInteger.valueOf(j2), bigMultiplyPowerTen(j3, iCheckScale), i4, i5, i4);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i2, long j2, int i3, int i4, int i5) {
        if (checkScale(bigInteger, i4 + i3) > i2) {
            return divideAndRound(bigMultiplyPowerTen(bigInteger, (i4 + i3) - i2), j2, i4, i5, i4);
        }
        int iCheckScale = checkScale(j2, i2 - i4) - i3;
        if (iCheckScale < LONG_TEN_POWERS_TABLE.length) {
            long jLongMultiplyPowerTen = longMultiplyPowerTen(j2, iCheckScale);
            if (jLongMultiplyPowerTen != Long.MIN_VALUE) {
                return divideAndRound(bigInteger, jLongMultiplyPowerTen, i4, i5, i4);
            }
        }
        return divideAndRound(bigInteger, bigMultiplyPowerTen(j2, iCheckScale), i4, i5, i4);
    }

    private static BigDecimal divide(long j2, int i2, BigInteger bigInteger, int i3, int i4, int i5) {
        if (checkScale(j2, i4 + i3) > i2) {
            return divideAndRound(bigMultiplyPowerTen(j2, (i4 + i3) - i2), bigInteger, i4, i5, i4);
        }
        return divideAndRound(BigInteger.valueOf(j2), bigMultiplyPowerTen(bigInteger, checkScale(bigInteger, i2 - i4) - i3), i4, i5, i4);
    }

    private static BigDecimal divide(BigInteger bigInteger, int i2, BigInteger bigInteger2, int i3, int i4, int i5) {
        if (checkScale(bigInteger, i4 + i3) > i2) {
            return divideAndRound(bigMultiplyPowerTen(bigInteger, (i4 + i3) - i2), bigInteger2, i4, i5, i4);
        }
        return divideAndRound(bigInteger, bigMultiplyPowerTen(bigInteger2, checkScale(bigInteger2, i2 - i4) - i3), i4, i5, i4);
    }
}
