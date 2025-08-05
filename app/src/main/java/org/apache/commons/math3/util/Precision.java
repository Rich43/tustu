package org.apache.commons.math3.util;

import java.math.BigDecimal;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Precision.class */
public class Precision {
    private static final long EXPONENT_OFFSET = 1023;
    private static final long SGN_MASK = Long.MIN_VALUE;
    private static final int SGN_MASK_FLOAT = Integer.MIN_VALUE;
    private static final double POSITIVE_ZERO = 0.0d;
    private static final long POSITIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(0.0d);
    private static final long NEGATIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(-0.0d);
    private static final int POSITIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(0.0f);
    private static final int NEGATIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(-0.0f);
    public static final double EPSILON = Double.longBitsToDouble(4368491638549381120L);
    public static final double SAFE_MIN = Double.longBitsToDouble(4503599627370496L);

    private Precision() {
    }

    public static int compareTo(double x2, double y2, double eps) {
        if (equals(x2, y2, eps)) {
            return 0;
        }
        if (x2 < y2) {
            return -1;
        }
        return 1;
    }

    public static int compareTo(double x2, double y2, int maxUlps) {
        if (equals(x2, y2, maxUlps)) {
            return 0;
        }
        if (x2 < y2) {
            return -1;
        }
        return 1;
    }

    public static boolean equals(float x2, float y2) {
        return equals(x2, y2, 1);
    }

    public static boolean equalsIncludingNaN(float x2, float y2) {
        if (x2 == x2 && y2 == y2) {
            return equals(x2, y2, 1);
        }
        return !(((x2 > x2 ? 1 : (x2 == x2 ? 0 : -1)) != 0) ^ ((y2 > y2 ? 1 : (y2 == y2 ? 0 : -1)) != 0));
    }

    public static boolean equals(float x2, float y2, float eps) {
        return equals(x2, y2, 1) || FastMath.abs(y2 - x2) <= eps;
    }

    public static boolean equalsIncludingNaN(float x2, float y2, float eps) {
        return equalsIncludingNaN(x2, y2) || FastMath.abs(y2 - x2) <= eps;
    }

    public static boolean equals(float x2, float y2, int maxUlps) {
        int deltaPlus;
        int deltaMinus;
        boolean isEqual;
        int xInt = Float.floatToRawIntBits(x2);
        int yInt = Float.floatToRawIntBits(y2);
        if (((xInt ^ yInt) & Integer.MIN_VALUE) == 0) {
            isEqual = FastMath.abs(xInt - yInt) <= maxUlps;
        } else {
            if (xInt < yInt) {
                deltaPlus = yInt - POSITIVE_ZERO_FLOAT_BITS;
                deltaMinus = xInt - NEGATIVE_ZERO_FLOAT_BITS;
            } else {
                deltaPlus = xInt - POSITIVE_ZERO_FLOAT_BITS;
                deltaMinus = yInt - NEGATIVE_ZERO_FLOAT_BITS;
            }
            if (deltaPlus > maxUlps) {
                isEqual = false;
            } else {
                isEqual = deltaMinus <= maxUlps - deltaPlus;
            }
        }
        return (!isEqual || Float.isNaN(x2) || Float.isNaN(y2)) ? false : true;
    }

    public static boolean equalsIncludingNaN(float x2, float y2, int maxUlps) {
        if (x2 == x2 && y2 == y2) {
            return equals(x2, y2, maxUlps);
        }
        return !(((x2 > x2 ? 1 : (x2 == x2 ? 0 : -1)) != 0) ^ ((y2 > y2 ? 1 : (y2 == y2 ? 0 : -1)) != 0));
    }

    public static boolean equals(double x2, double y2) {
        return equals(x2, y2, 1);
    }

    public static boolean equalsIncludingNaN(double x2, double y2) {
        if (x2 == x2 && y2 == y2) {
            return equals(x2, y2, 1);
        }
        return !(((x2 > x2 ? 1 : (x2 == x2 ? 0 : -1)) != 0) ^ ((y2 > y2 ? 1 : (y2 == y2 ? 0 : -1)) != 0));
    }

    public static boolean equals(double x2, double y2, double eps) {
        return equals(x2, y2, 1) || FastMath.abs(y2 - x2) <= eps;
    }

    public static boolean equalsWithRelativeTolerance(double x2, double y2, double eps) {
        if (equals(x2, y2, 1)) {
            return true;
        }
        double absoluteMax = FastMath.max(FastMath.abs(x2), FastMath.abs(y2));
        double relativeDifference = FastMath.abs((x2 - y2) / absoluteMax);
        return relativeDifference <= eps;
    }

    public static boolean equalsIncludingNaN(double x2, double y2, double eps) {
        return equalsIncludingNaN(x2, y2) || FastMath.abs(y2 - x2) <= eps;
    }

    public static boolean equals(double x2, double y2, int maxUlps) {
        long deltaPlus;
        long deltaMinus;
        boolean isEqual;
        long xInt = Double.doubleToRawLongBits(x2);
        long yInt = Double.doubleToRawLongBits(y2);
        if (((xInt ^ yInt) & Long.MIN_VALUE) == 0) {
            isEqual = FastMath.abs(xInt - yInt) <= ((long) maxUlps);
        } else {
            if (xInt < yInt) {
                deltaPlus = yInt - POSITIVE_ZERO_DOUBLE_BITS;
                deltaMinus = xInt - NEGATIVE_ZERO_DOUBLE_BITS;
            } else {
                deltaPlus = xInt - POSITIVE_ZERO_DOUBLE_BITS;
                deltaMinus = yInt - NEGATIVE_ZERO_DOUBLE_BITS;
            }
            if (deltaPlus > maxUlps) {
                isEqual = false;
            } else {
                isEqual = deltaMinus <= ((long) maxUlps) - deltaPlus;
            }
        }
        return (!isEqual || Double.isNaN(x2) || Double.isNaN(y2)) ? false : true;
    }

    public static boolean equalsIncludingNaN(double x2, double y2, int maxUlps) {
        if (x2 == x2 && y2 == y2) {
            return equals(x2, y2, maxUlps);
        }
        return !(((x2 > x2 ? 1 : (x2 == x2 ? 0 : -1)) != 0) ^ ((y2 > y2 ? 1 : (y2 == y2 ? 0 : -1)) != 0));
    }

    public static double round(double x2, int scale) {
        return round(x2, scale, 4);
    }

    public static double round(double x2, int scale, int roundingMethod) {
        try {
            double rounded = new BigDecimal(Double.toString(x2)).setScale(scale, roundingMethod).doubleValue();
            return rounded == 0.0d ? 0.0d * x2 : rounded;
        } catch (NumberFormatException e2) {
            if (Double.isInfinite(x2)) {
                return x2;
            }
            return Double.NaN;
        }
    }

    public static float round(float x2, int scale) {
        return round(x2, scale, 4);
    }

    public static float round(float x2, int scale, int roundingMethod) throws MathArithmeticException, MathIllegalArgumentException {
        float sign = FastMath.copySign(1.0f, x2);
        float factor = ((float) FastMath.pow(10.0d, scale)) * sign;
        return ((float) roundUnscaled(x2 * factor, sign, roundingMethod)) / factor;
    }

    private static double roundUnscaled(double unscaled, double sign, int roundingMethod) throws MathArithmeticException, MathIllegalArgumentException {
        switch (roundingMethod) {
            case 0:
                if (unscaled != FastMath.floor(unscaled)) {
                    unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, Double.POSITIVE_INFINITY));
                    break;
                }
                break;
            case 1:
                unscaled = FastMath.floor(FastMath.nextAfter(unscaled, Double.NEGATIVE_INFINITY));
                break;
            case 2:
                if (sign == -1.0d) {
                    unscaled = FastMath.floor(FastMath.nextAfter(unscaled, Double.NEGATIVE_INFINITY));
                    break;
                } else {
                    unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, Double.POSITIVE_INFINITY));
                    break;
                }
            case 3:
                if (sign == -1.0d) {
                    unscaled = FastMath.ceil(FastMath.nextAfter(unscaled, Double.POSITIVE_INFINITY));
                    break;
                } else {
                    unscaled = FastMath.floor(FastMath.nextAfter(unscaled, Double.NEGATIVE_INFINITY));
                    break;
                }
            case 4:
                double unscaled2 = FastMath.nextAfter(unscaled, Double.POSITIVE_INFINITY);
                if (unscaled2 - FastMath.floor(unscaled2) >= 0.5d) {
                    unscaled = FastMath.ceil(unscaled2);
                    break;
                } else {
                    unscaled = FastMath.floor(unscaled2);
                    break;
                }
            case 5:
                double unscaled3 = FastMath.nextAfter(unscaled, Double.NEGATIVE_INFINITY);
                if (unscaled3 - FastMath.floor(unscaled3) > 0.5d) {
                    unscaled = FastMath.ceil(unscaled3);
                    break;
                } else {
                    unscaled = FastMath.floor(unscaled3);
                    break;
                }
            case 6:
                double fraction = unscaled - FastMath.floor(unscaled);
                if (fraction <= 0.5d) {
                    if (fraction < 0.5d || FastMath.floor(unscaled) / 2.0d == FastMath.floor(FastMath.floor(unscaled) / 2.0d)) {
                        unscaled = FastMath.floor(unscaled);
                        break;
                    } else {
                        unscaled = FastMath.ceil(unscaled);
                        break;
                    }
                } else {
                    unscaled = FastMath.ceil(unscaled);
                    break;
                }
                break;
            case 7:
                if (unscaled != FastMath.floor(unscaled)) {
                    throw new MathArithmeticException();
                }
                break;
            default:
                throw new MathIllegalArgumentException(LocalizedFormats.INVALID_ROUNDING_METHOD, Integer.valueOf(roundingMethod), "ROUND_CEILING", 2, "ROUND_DOWN", 1, "ROUND_FLOOR", 3, "ROUND_HALF_DOWN", 5, "ROUND_HALF_EVEN", 6, "ROUND_HALF_UP", 4, "ROUND_UNNECESSARY", 7, "ROUND_UP", 0);
        }
        return unscaled;
    }

    public static double representableDelta(double x2, double originalDelta) {
        return (x2 + originalDelta) - x2;
    }
}
