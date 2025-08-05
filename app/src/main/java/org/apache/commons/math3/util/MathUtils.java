package org.apache.commons.math3.util;

import java.util.Arrays;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathUtils.class */
public final class MathUtils {
    public static final double TWO_PI = 6.283185307179586d;
    public static final double PI_SQUARED = 9.869604401089358d;

    private MathUtils() {
    }

    public static int hash(double value) {
        return new Double(value).hashCode();
    }

    public static boolean equals(double x2, double y2) {
        return new Double(x2).equals(new Double(y2));
    }

    public static int hash(double[] value) {
        return Arrays.hashCode(value);
    }

    public static double normalizeAngle(double a2, double center) {
        return a2 - (6.283185307179586d * FastMath.floor(((a2 + 3.141592653589793d) - center) / 6.283185307179586d));
    }

    public static <T extends RealFieldElement<T>> T max(T e1, T e2) {
        return ((RealFieldElement) e1.subtract(e2)).getReal() >= 0.0d ? e1 : e2;
    }

    public static <T extends RealFieldElement<T>> T min(T e1, T e2) {
        return ((RealFieldElement) e1.subtract(e2)).getReal() >= 0.0d ? e2 : e1;
    }

    public static double reduce(double a2, double period, double offset) {
        double p2 = FastMath.abs(period);
        return (a2 - (p2 * FastMath.floor((a2 - offset) / p2))) - offset;
    }

    public static byte copySign(byte magnitude, byte sign) throws MathArithmeticException {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Byte.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return (byte) (-magnitude);
    }

    public static short copySign(short magnitude, short sign) throws MathArithmeticException {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Short.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return (short) (-magnitude);
    }

    public static int copySign(int magnitude, int sign) throws MathArithmeticException {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static long copySign(long magnitude, long sign) throws MathArithmeticException {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    public static void checkFinite(double x2) throws NotFiniteNumberException {
        if (Double.isInfinite(x2) || Double.isNaN(x2)) {
            throw new NotFiniteNumberException(Double.valueOf(x2), new Object[0]);
        }
    }

    public static void checkFinite(double[] val) throws NotFiniteNumberException {
        for (int i2 = 0; i2 < val.length; i2++) {
            double x2 = val[i2];
            if (Double.isInfinite(x2) || Double.isNaN(x2)) {
                throw new NotFiniteNumberException(LocalizedFormats.ARRAY_ELEMENT, Double.valueOf(x2), Integer.valueOf(i2));
            }
        }
    }

    public static void checkNotNull(Object o2, Localizable pattern, Object... args) throws NullArgumentException {
        if (o2 == null) {
            throw new NullArgumentException(pattern, args);
        }
    }

    public static void checkNotNull(Object o2) throws NullArgumentException {
        if (o2 == null) {
            throw new NullArgumentException();
        }
    }
}
