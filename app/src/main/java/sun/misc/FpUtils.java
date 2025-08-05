package sun.misc;

/* loaded from: rt.jar:sun/misc/FpUtils.class */
public class FpUtils {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FpUtils.class.desiredAssertionStatus();
    }

    private FpUtils() {
    }

    @Deprecated
    public static int getExponent(double d2) {
        return Math.getExponent(d2);
    }

    @Deprecated
    public static int getExponent(float f2) {
        return Math.getExponent(f2);
    }

    @Deprecated
    public static double rawCopySign(double d2, double d3) {
        return Math.copySign(d2, d3);
    }

    @Deprecated
    public static float rawCopySign(float f2, float f3) {
        return Math.copySign(f2, f3);
    }

    @Deprecated
    public static boolean isFinite(double d2) {
        return Double.isFinite(d2);
    }

    @Deprecated
    public static boolean isFinite(float f2) {
        return Float.isFinite(f2);
    }

    public static boolean isInfinite(double d2) {
        return Double.isInfinite(d2);
    }

    public static boolean isInfinite(float f2) {
        return Float.isInfinite(f2);
    }

    public static boolean isNaN(double d2) {
        return Double.isNaN(d2);
    }

    public static boolean isNaN(float f2) {
        return Float.isNaN(f2);
    }

    public static boolean isUnordered(double d2, double d3) {
        return isNaN(d2) || isNaN(d3);
    }

    public static boolean isUnordered(float f2, float f3) {
        return isNaN(f2) || isNaN(f3);
    }

    public static int ilogb(double d2) {
        int exponent = getExponent(d2);
        switch (exponent) {
            case -1023:
                if (d2 == 0.0d) {
                    return -268435456;
                }
                long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2) & DoubleConsts.SIGNIF_BIT_MASK;
                if (!$assertionsDisabled && jDoubleToRawLongBits == 0) {
                    throw new AssertionError();
                }
                while (jDoubleToRawLongBits < 4503599627370496L) {
                    jDoubleToRawLongBits *= 2;
                    exponent--;
                }
                int i2 = exponent + 1;
                if ($assertionsDisabled || (i2 >= -1074 && i2 < -1022)) {
                    return i2;
                }
                throw new AssertionError();
            case 1024:
                if (isNaN(d2)) {
                    return 1073741824;
                }
                return 268435456;
            default:
                if ($assertionsDisabled || (exponent >= -1022 && exponent <= 1023)) {
                    return exponent;
                }
                throw new AssertionError();
        }
    }

    public static int ilogb(float f2) {
        int exponent = getExponent(f2);
        switch (exponent) {
            case -127:
                if (f2 == 0.0f) {
                    return -268435456;
                }
                int iFloatToRawIntBits = Float.floatToRawIntBits(f2) & FloatConsts.SIGNIF_BIT_MASK;
                if (!$assertionsDisabled && iFloatToRawIntBits == 0) {
                    throw new AssertionError();
                }
                while (iFloatToRawIntBits < 8388608) {
                    iFloatToRawIntBits *= 2;
                    exponent--;
                }
                int i2 = exponent + 1;
                if ($assertionsDisabled || (i2 >= -149 && i2 < -126)) {
                    return i2;
                }
                throw new AssertionError();
            case 128:
                if (isNaN(f2)) {
                    return 1073741824;
                }
                return 268435456;
            default:
                if ($assertionsDisabled || (exponent >= -126 && exponent <= 127)) {
                    return exponent;
                }
                throw new AssertionError();
        }
    }

    @Deprecated
    public static double scalb(double d2, int i2) {
        return Math.scalb(d2, i2);
    }

    @Deprecated
    public static float scalb(float f2, int i2) {
        return Math.scalb(f2, i2);
    }

    @Deprecated
    public static double nextAfter(double d2, double d3) {
        return Math.nextAfter(d2, d3);
    }

    @Deprecated
    public static float nextAfter(float f2, double d2) {
        return Math.nextAfter(f2, d2);
    }

    @Deprecated
    public static double nextUp(double d2) {
        return Math.nextUp(d2);
    }

    @Deprecated
    public static float nextUp(float f2) {
        return Math.nextUp(f2);
    }

    @Deprecated
    public static double nextDown(double d2) {
        return Math.nextDown(d2);
    }

    @Deprecated
    public static double nextDown(float f2) {
        return Math.nextDown(f2);
    }

    @Deprecated
    public static double copySign(double d2, double d3) {
        return StrictMath.copySign(d2, d3);
    }

    @Deprecated
    public static float copySign(float f2, float f3) {
        return StrictMath.copySign(f2, f3);
    }

    @Deprecated
    public static double ulp(double d2) {
        return Math.ulp(d2);
    }

    @Deprecated
    public static float ulp(float f2) {
        return Math.ulp(f2);
    }

    @Deprecated
    public static double signum(double d2) {
        return Math.signum(d2);
    }

    @Deprecated
    public static float signum(float f2) {
        return Math.signum(f2);
    }
}
