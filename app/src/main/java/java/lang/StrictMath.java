package java.lang;

import java.util.Random;
import sun.misc.DoubleConsts;

/* loaded from: rt.jar:java/lang/StrictMath.class */
public final class StrictMath {

    /* renamed from: E, reason: collision with root package name */
    public static final double f12444E = 2.718281828459045d;
    public static final double PI = 3.141592653589793d;
    static final /* synthetic */ boolean $assertionsDisabled;

    public static native double sin(double d2);

    public static native double cos(double d2);

    public static native double tan(double d2);

    public static native double asin(double d2);

    public static native double acos(double d2);

    public static native double atan(double d2);

    public static native double exp(double d2);

    public static native double log(double d2);

    public static native double log10(double d2);

    public static native double sqrt(double d2);

    public static native double cbrt(double d2);

    public static native double IEEEremainder(double d2, double d3);

    public static native double atan2(double d2, double d3);

    public static native double pow(double d2, double d3);

    public static native double sinh(double d2);

    public static native double cosh(double d2);

    public static native double tanh(double d2);

    public static native double hypot(double d2, double d3);

    public static native double expm1(double d2);

    public static native double log1p(double d2);

    static {
        $assertionsDisabled = !StrictMath.class.desiredAssertionStatus();
    }

    private StrictMath() {
    }

    public static double toRadians(double d2) {
        return (d2 / 180.0d) * 3.141592653589793d;
    }

    public static double toDegrees(double d2) {
        return (d2 * 180.0d) / 3.141592653589793d;
    }

    public static double ceil(double d2) {
        return floorOrCeil(d2, -0.0d, 1.0d, 1.0d);
    }

    public static double floor(double d2) {
        return floorOrCeil(d2, -1.0d, 0.0d, -1.0d);
    }

    private static double floorOrCeil(double d2, double d3, double d4, double d5) {
        int exponent = Math.getExponent(d2);
        if (exponent < 0) {
            return d2 == 0.0d ? d2 : d2 < 0.0d ? d3 : d4;
        }
        if (exponent >= 52) {
            return d2;
        }
        if (!$assertionsDisabled && (exponent < 0 || exponent > 51)) {
            throw new AssertionError();
        }
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2);
        long j2 = DoubleConsts.SIGNIF_BIT_MASK >> exponent;
        if ((j2 & jDoubleToRawLongBits) == 0) {
            return d2;
        }
        double dLongBitsToDouble = Double.longBitsToDouble(jDoubleToRawLongBits & (j2 ^ (-1)));
        if (d5 * d2 > 0.0d) {
            dLongBitsToDouble += d5;
        }
        return dLongBitsToDouble;
    }

    public static double rint(double d2) {
        double dCopySign = Math.copySign(1.0d, d2);
        double dAbs = Math.abs(d2);
        if (dAbs < 4.503599627370496E15d) {
            dAbs = (4.503599627370496E15d + dAbs) - 4.503599627370496E15d;
        }
        return dCopySign * dAbs;
    }

    public static int round(float f2) {
        return Math.round(f2);
    }

    public static long round(double d2) {
        return Math.round(d2);
    }

    /* loaded from: rt.jar:java/lang/StrictMath$RandomNumberGeneratorHolder.class */
    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();

        private RandomNumberGeneratorHolder() {
        }
    }

    public static double random() {
        return RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
    }

    public static int addExact(int i2, int i3) {
        return Math.addExact(i2, i3);
    }

    public static long addExact(long j2, long j3) {
        return Math.addExact(j2, j3);
    }

    public static int subtractExact(int i2, int i3) {
        return Math.subtractExact(i2, i3);
    }

    public static long subtractExact(long j2, long j3) {
        return Math.subtractExact(j2, j3);
    }

    public static int multiplyExact(int i2, int i3) {
        return Math.multiplyExact(i2, i3);
    }

    public static long multiplyExact(long j2, long j3) {
        return Math.multiplyExact(j2, j3);
    }

    public static int toIntExact(long j2) {
        return Math.toIntExact(j2);
    }

    public static int floorDiv(int i2, int i3) {
        return Math.floorDiv(i2, i3);
    }

    public static long floorDiv(long j2, long j3) {
        return Math.floorDiv(j2, j3);
    }

    public static int floorMod(int i2, int i3) {
        return Math.floorMod(i2, i3);
    }

    public static long floorMod(long j2, long j3) {
        return Math.floorMod(j2, j3);
    }

    public static int abs(int i2) {
        return Math.abs(i2);
    }

    public static long abs(long j2) {
        return Math.abs(j2);
    }

    public static float abs(float f2) {
        return Math.abs(f2);
    }

    public static double abs(double d2) {
        return Math.abs(d2);
    }

    public static int max(int i2, int i3) {
        return Math.max(i2, i3);
    }

    public static long max(long j2, long j3) {
        return Math.max(j2, j3);
    }

    public static float max(float f2, float f3) {
        return Math.max(f2, f3);
    }

    public static double max(double d2, double d3) {
        return Math.max(d2, d3);
    }

    public static int min(int i2, int i3) {
        return Math.min(i2, i3);
    }

    public static long min(long j2, long j3) {
        return Math.min(j2, j3);
    }

    public static float min(float f2, float f3) {
        return Math.min(f2, f3);
    }

    public static double min(double d2, double d3) {
        return Math.min(d2, d3);
    }

    public static double ulp(double d2) {
        return Math.ulp(d2);
    }

    public static float ulp(float f2) {
        return Math.ulp(f2);
    }

    public static double signum(double d2) {
        return Math.signum(d2);
    }

    public static float signum(float f2) {
        return Math.signum(f2);
    }

    public static double copySign(double d2, double d3) {
        return Math.copySign(d2, Double.isNaN(d3) ? 1.0d : d3);
    }

    public static float copySign(float f2, float f3) {
        return Math.copySign(f2, Float.isNaN(f3) ? 1.0f : f3);
    }

    public static int getExponent(float f2) {
        return Math.getExponent(f2);
    }

    public static int getExponent(double d2) {
        return Math.getExponent(d2);
    }

    public static double nextAfter(double d2, double d3) {
        return Math.nextAfter(d2, d3);
    }

    public static float nextAfter(float f2, double d2) {
        return Math.nextAfter(f2, d2);
    }

    public static double nextUp(double d2) {
        return Math.nextUp(d2);
    }

    public static float nextUp(float f2) {
        return Math.nextUp(f2);
    }

    public static double nextDown(double d2) {
        return Math.nextDown(d2);
    }

    public static float nextDown(float f2) {
        return Math.nextDown(f2);
    }

    public static double scalb(double d2, int i2) {
        return Math.scalb(d2, i2);
    }

    public static float scalb(float f2, int i2) {
        return Math.scalb(f2, i2);
    }
}
