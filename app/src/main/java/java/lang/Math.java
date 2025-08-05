package java.lang;

import com.sun.glass.events.WindowEvent;
import java.util.Random;
import sun.misc.DoubleConsts;
import sun.misc.FloatConsts;

/* loaded from: rt.jar:java/lang/Math.class */
public final class Math {

    /* renamed from: E, reason: collision with root package name */
    public static final double f12438E = 2.718281828459045d;
    public static final double PI = 3.141592653589793d;
    private static long negativeZeroFloatBits;
    private static long negativeZeroDoubleBits;
    static double twoToTheDoubleScaleUp;
    static double twoToTheDoubleScaleDown;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Math.class.desiredAssertionStatus();
        negativeZeroFloatBits = Float.floatToRawIntBits(-0.0f);
        negativeZeroDoubleBits = Double.doubleToRawLongBits(-0.0d);
        twoToTheDoubleScaleUp = powerOfTwoD(512);
        twoToTheDoubleScaleDown = powerOfTwoD(-512);
    }

    private Math() {
    }

    public static double sin(double d2) {
        return StrictMath.sin(d2);
    }

    public static double cos(double d2) {
        return StrictMath.cos(d2);
    }

    public static double tan(double d2) {
        return StrictMath.tan(d2);
    }

    public static double asin(double d2) {
        return StrictMath.asin(d2);
    }

    public static double acos(double d2) {
        return StrictMath.acos(d2);
    }

    public static double atan(double d2) {
        return StrictMath.atan(d2);
    }

    public static double toRadians(double d2) {
        return (d2 / 180.0d) * 3.141592653589793d;
    }

    public static double toDegrees(double d2) {
        return (d2 * 180.0d) / 3.141592653589793d;
    }

    public static double exp(double d2) {
        return StrictMath.exp(d2);
    }

    public static double log(double d2) {
        return StrictMath.log(d2);
    }

    public static double log10(double d2) {
        return StrictMath.log10(d2);
    }

    public static double sqrt(double d2) {
        return StrictMath.sqrt(d2);
    }

    public static double cbrt(double d2) {
        return StrictMath.cbrt(d2);
    }

    public static double IEEEremainder(double d2, double d3) {
        return StrictMath.IEEEremainder(d2, d3);
    }

    public static double ceil(double d2) {
        return StrictMath.ceil(d2);
    }

    public static double floor(double d2) {
        return StrictMath.floor(d2);
    }

    public static double rint(double d2) {
        return StrictMath.rint(d2);
    }

    public static double atan2(double d2, double d3) {
        return StrictMath.atan2(d2, d3);
    }

    public static double pow(double d2, double d3) {
        return StrictMath.pow(d2, d3);
    }

    public static int round(float f2) {
        int iFloatToRawIntBits = Float.floatToRawIntBits(f2);
        int i2 = 149 - ((iFloatToRawIntBits & FloatConsts.EXP_BIT_MASK) >> 23);
        if ((i2 & (-32)) == 0) {
            int i3 = (iFloatToRawIntBits & FloatConsts.SIGNIF_BIT_MASK) | 8388608;
            if (iFloatToRawIntBits < 0) {
                i3 = -i3;
            }
            return ((i3 >> i2) + 1) >> 1;
        }
        return (int) f2;
    }

    public static long round(double d2) {
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2);
        long j2 = 1074 - ((jDoubleToRawLongBits & DoubleConsts.EXP_BIT_MASK) >> 52);
        if ((j2 & (-64)) == 0) {
            long j3 = (jDoubleToRawLongBits & DoubleConsts.SIGNIF_BIT_MASK) | 4503599627370496L;
            if (jDoubleToRawLongBits < 0) {
                j3 = -j3;
            }
            return ((j3 >> ((int) j2)) + 1) >> 1;
        }
        return (long) d2;
    }

    /* loaded from: rt.jar:java/lang/Math$RandomNumberGeneratorHolder.class */
    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();

        private RandomNumberGeneratorHolder() {
        }
    }

    public static double random() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
    }

    public static int addExact(int i2, int i3) {
        int i4 = i2 + i3;
        if (((i2 ^ i4) & (i3 ^ i4)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return i4;
    }

    public static long addExact(long j2, long j3) {
        long j4 = j2 + j3;
        if (((j2 ^ j4) & (j3 ^ j4)) < 0) {
            throw new ArithmeticException("long overflow");
        }
        return j4;
    }

    public static int subtractExact(int i2, int i3) {
        int i4 = i2 - i3;
        if (((i2 ^ i3) & (i2 ^ i4)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return i4;
    }

    public static long subtractExact(long j2, long j3) {
        long j4 = j2 - j3;
        if (((j2 ^ j3) & (j2 ^ j4)) < 0) {
            throw new ArithmeticException("long overflow");
        }
        return j4;
    }

    public static int multiplyExact(int i2, int i3) {
        long j2 = i2 * i3;
        if (((int) j2) != j2) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) j2;
    }

    public static long multiplyExact(long j2, long j3) {
        long j4 = j2 * j3;
        if (((abs(j2) | abs(j3)) >>> 31) != 0 && ((j3 != 0 && j4 / j3 != j2) || (j2 == Long.MIN_VALUE && j3 == -1))) {
            throw new ArithmeticException("long overflow");
        }
        return j4;
    }

    public static int incrementExact(int i2) {
        if (i2 == Integer.MAX_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return i2 + 1;
    }

    public static long incrementExact(long j2) {
        if (j2 == Long.MAX_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return j2 + 1;
    }

    public static int decrementExact(int i2) {
        if (i2 == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return i2 - 1;
    }

    public static long decrementExact(long j2) {
        if (j2 == Long.MIN_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return j2 - 1;
    }

    public static int negateExact(int i2) {
        if (i2 == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return -i2;
    }

    public static long negateExact(long j2) {
        if (j2 == Long.MIN_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return -j2;
    }

    public static int toIntExact(long j2) {
        if (((int) j2) != j2) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) j2;
    }

    public static int floorDiv(int i2, int i3) {
        int i4 = i2 / i3;
        if ((i2 ^ i3) < 0 && i4 * i3 != i2) {
            i4--;
        }
        return i4;
    }

    public static long floorDiv(long j2, long j3) {
        long j4 = j2 / j3;
        if ((j2 ^ j3) < 0 && j4 * j3 != j2) {
            j4--;
        }
        return j4;
    }

    public static int floorMod(int i2, int i3) {
        return i2 - (floorDiv(i2, i3) * i3);
    }

    public static long floorMod(long j2, long j3) {
        return j2 - (floorDiv(j2, j3) * j3);
    }

    public static int abs(int i2) {
        return i2 < 0 ? -i2 : i2;
    }

    public static long abs(long j2) {
        return j2 < 0 ? -j2 : j2;
    }

    public static float abs(float f2) {
        return f2 <= 0.0f ? 0.0f - f2 : f2;
    }

    public static double abs(double d2) {
        return d2 <= 0.0d ? 0.0d - d2 : d2;
    }

    public static int max(int i2, int i3) {
        return i2 >= i3 ? i2 : i3;
    }

    public static long max(long j2, long j3) {
        return j2 >= j3 ? j2 : j3;
    }

    public static float max(float f2, float f3) {
        if (f2 != f2) {
            return f2;
        }
        if (f2 == 0.0f && f3 == 0.0f && Float.floatToRawIntBits(f2) == negativeZeroFloatBits) {
            return f3;
        }
        return f2 >= f3 ? f2 : f3;
    }

    public static double max(double d2, double d3) {
        if (d2 != d2) {
            return d2;
        }
        if (d2 == 0.0d && d3 == 0.0d && Double.doubleToRawLongBits(d2) == negativeZeroDoubleBits) {
            return d3;
        }
        return d2 >= d3 ? d2 : d3;
    }

    public static int min(int i2, int i3) {
        return i2 <= i3 ? i2 : i3;
    }

    public static long min(long j2, long j3) {
        return j2 <= j3 ? j2 : j3;
    }

    public static float min(float f2, float f3) {
        if (f2 != f2) {
            return f2;
        }
        if (f2 == 0.0f && f3 == 0.0f && Float.floatToRawIntBits(f3) == negativeZeroFloatBits) {
            return f3;
        }
        return f2 <= f3 ? f2 : f3;
    }

    public static double min(double d2, double d3) {
        if (d2 != d2) {
            return d2;
        }
        if (d2 == 0.0d && d3 == 0.0d && Double.doubleToRawLongBits(d3) == negativeZeroDoubleBits) {
            return d3;
        }
        return d2 <= d3 ? d2 : d3;
    }

    public static double ulp(double d2) {
        int exponent = getExponent(d2);
        switch (exponent) {
            case -1023:
                return Double.MIN_VALUE;
            case 1024:
                return abs(d2);
            default:
                if (!$assertionsDisabled && (exponent > 1023 || exponent < -1022)) {
                    throw new AssertionError();
                }
                int i2 = exponent - 52;
                if (i2 >= -1022) {
                    return powerOfTwoD(i2);
                }
                return Double.longBitsToDouble(1 << (i2 - DoubleConsts.MIN_SUB_EXPONENT));
        }
    }

    public static float ulp(float f2) {
        int exponent = getExponent(f2);
        switch (exponent) {
            case -127:
                return Float.MIN_VALUE;
            case 128:
                return abs(f2);
            default:
                if (!$assertionsDisabled && (exponent > 127 || exponent < -126)) {
                    throw new AssertionError();
                }
                int i2 = exponent - 23;
                if (i2 >= -126) {
                    return powerOfTwoF(i2);
                }
                return Float.intBitsToFloat(1 << (i2 - FloatConsts.MIN_SUB_EXPONENT));
        }
    }

    public static double signum(double d2) {
        return (d2 == 0.0d || Double.isNaN(d2)) ? d2 : copySign(1.0d, d2);
    }

    public static float signum(float f2) {
        return (f2 == 0.0f || Float.isNaN(f2)) ? f2 : copySign(1.0f, f2);
    }

    public static double sinh(double d2) {
        return StrictMath.sinh(d2);
    }

    public static double cosh(double d2) {
        return StrictMath.cosh(d2);
    }

    public static double tanh(double d2) {
        return StrictMath.tanh(d2);
    }

    public static double hypot(double d2, double d3) {
        return StrictMath.hypot(d2, d3);
    }

    public static double expm1(double d2) {
        return StrictMath.expm1(d2);
    }

    public static double log1p(double d2) {
        return StrictMath.log1p(d2);
    }

    public static double copySign(double d2, double d3) {
        return Double.longBitsToDouble((Double.doubleToRawLongBits(d3) & Long.MIN_VALUE) | (Double.doubleToRawLongBits(d2) & Long.MAX_VALUE));
    }

    public static float copySign(float f2, float f3) {
        return Float.intBitsToFloat((Float.floatToRawIntBits(f3) & Integer.MIN_VALUE) | (Float.floatToRawIntBits(f2) & Integer.MAX_VALUE));
    }

    public static int getExponent(float f2) {
        return ((Float.floatToRawIntBits(f2) & FloatConsts.EXP_BIT_MASK) >> 23) - 127;
    }

    public static int getExponent(double d2) {
        return (int) (((Double.doubleToRawLongBits(d2) & DoubleConsts.EXP_BIT_MASK) >> 52) - 1023);
    }

    public static double nextAfter(double d2, double d3) {
        long j2;
        if (Double.isNaN(d2) || Double.isNaN(d3)) {
            return d2 + d3;
        }
        if (d2 == d3) {
            return d3;
        }
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2 + 0.0d);
        if (d3 > d2) {
            j2 = jDoubleToRawLongBits + (jDoubleToRawLongBits >= 0 ? 1L : -1L);
        } else {
            if (!$assertionsDisabled && d3 >= d2) {
                throw new AssertionError();
            }
            if (jDoubleToRawLongBits > 0) {
                j2 = jDoubleToRawLongBits - 1;
            } else if (jDoubleToRawLongBits < 0) {
                j2 = jDoubleToRawLongBits + 1;
            } else {
                j2 = -9223372036854775807L;
            }
        }
        return Double.longBitsToDouble(j2);
    }

    public static float nextAfter(float f2, double d2) {
        int i2;
        if (Float.isNaN(f2) || Double.isNaN(d2)) {
            return f2 + ((float) d2);
        }
        if (f2 == d2) {
            return (float) d2;
        }
        int iFloatToRawIntBits = Float.floatToRawIntBits(f2 + 0.0f);
        if (d2 > f2) {
            i2 = iFloatToRawIntBits + (iFloatToRawIntBits >= 0 ? 1 : -1);
        } else {
            if (!$assertionsDisabled && d2 >= f2) {
                throw new AssertionError();
            }
            if (iFloatToRawIntBits > 0) {
                i2 = iFloatToRawIntBits - 1;
            } else if (iFloatToRawIntBits < 0) {
                i2 = iFloatToRawIntBits + 1;
            } else {
                i2 = -2147483647;
            }
        }
        return Float.intBitsToFloat(i2);
    }

    public static double nextUp(double d2) {
        if (Double.isNaN(d2) || d2 == Double.POSITIVE_INFINITY) {
            return d2;
        }
        double d3 = d2 + 0.0d;
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d3) + (d3 >= 0.0d ? 1L : -1L));
    }

    public static float nextUp(float f2) {
        if (Float.isNaN(f2) || f2 == Float.POSITIVE_INFINITY) {
            return f2;
        }
        float f3 = f2 + 0.0f;
        return Float.intBitsToFloat(Float.floatToRawIntBits(f3) + (f3 >= 0.0f ? 1 : -1));
    }

    public static double nextDown(double d2) {
        if (Double.isNaN(d2) || d2 == Double.NEGATIVE_INFINITY) {
            return d2;
        }
        if (d2 == 0.0d) {
            return -4.9E-324d;
        }
        return Double.longBitsToDouble(Double.doubleToRawLongBits(d2) + (d2 > 0.0d ? -1L : 1L));
    }

    public static float nextDown(float f2) {
        if (Float.isNaN(f2) || f2 == Float.NEGATIVE_INFINITY) {
            return f2;
        }
        if (f2 == 0.0f) {
            return -1.4E-45f;
        }
        return Float.intBitsToFloat(Float.floatToRawIntBits(f2) + (f2 > 0.0f ? -1 : 1));
    }

    public static double scalb(double d2, int i2) {
        int iMin;
        int i3;
        double d3;
        if (i2 < 0) {
            iMin = max(i2, -2099);
            i3 = -512;
            d3 = twoToTheDoubleScaleDown;
        } else {
            iMin = min(i2, 2099);
            i3 = 512;
            d3 = twoToTheDoubleScaleUp;
        }
        int i4 = (iMin >> 8) >>> 23;
        int i5 = ((iMin + i4) & WindowEvent.RESIZE) - i4;
        double dPowerOfTwoD = d2 * powerOfTwoD(i5);
        int i6 = iMin;
        int i7 = i5;
        while (true) {
            int i8 = i6 - i7;
            if (i8 != 0) {
                dPowerOfTwoD *= d3;
                i6 = i8;
                i7 = i3;
            } else {
                return dPowerOfTwoD;
            }
        }
    }

    public static float scalb(float f2, int i2) {
        return (float) (f2 * powerOfTwoD(max(min(i2, 278), -278)));
    }

    static double powerOfTwoD(int i2) {
        if ($assertionsDisabled || (i2 >= -1022 && i2 <= 1023)) {
            return Double.longBitsToDouble(((i2 + 1023) << 52) & DoubleConsts.EXP_BIT_MASK);
        }
        throw new AssertionError();
    }

    static float powerOfTwoF(int i2) {
        if ($assertionsDisabled || (i2 >= -126 && i2 <= 127)) {
            return Float.intBitsToFloat(((i2 + 127) << 23) & FloatConsts.EXP_BIT_MASK);
        }
        throw new AssertionError();
    }
}
