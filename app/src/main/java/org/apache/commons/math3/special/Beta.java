package org.apache.commons.math3.special;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/special/Beta.class */
public class Beta {
    private static final double DEFAULT_EPSILON = 1.0E-14d;
    private static final double HALF_LOG_TWO_PI = 0.9189385332046727d;
    private static final double[] DELTA = {0.08333333333333333d, -2.777777777777778E-5d, 7.936507936507937E-8d, -5.952380952380953E-10d, 8.417508417508329E-12d, -1.917526917518546E-13d, 6.410256405103255E-15d, -2.955065141253382E-16d, 1.7964371635940225E-17d, -1.3922896466162779E-18d, 1.338028550140209E-19d, -1.542460098679661E-20d, 1.9770199298095743E-21d, -2.3406566479399704E-22d, 1.713480149663986E-23d};

    private Beta() {
    }

    public static double regularizedBeta(double x2, double a2, double b2) {
        return regularizedBeta(x2, a2, b2, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x2, double a2, double b2, double epsilon) {
        return regularizedBeta(x2, a2, b2, epsilon, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x2, double a2, double b2, int maxIterations) {
        return regularizedBeta(x2, a2, b2, DEFAULT_EPSILON, maxIterations);
    }

    public static double regularizedBeta(double x2, final double a2, final double b2, double epsilon, int maxIterations) {
        double ret;
        if (Double.isNaN(x2) || Double.isNaN(a2) || Double.isNaN(b2) || x2 < 0.0d || x2 > 1.0d || a2 <= 0.0d || b2 <= 0.0d) {
            ret = Double.NaN;
        } else if (x2 > (a2 + 1.0d) / ((2.0d + b2) + a2) && 1.0d - x2 <= (b2 + 1.0d) / ((2.0d + b2) + a2)) {
            ret = 1.0d - regularizedBeta(1.0d - x2, b2, a2, epsilon, maxIterations);
        } else {
            ContinuedFraction fraction = new ContinuedFraction() { // from class: org.apache.commons.math3.special.Beta.1
                @Override // org.apache.commons.math3.util.ContinuedFraction
                protected double getB(int n2, double x3) {
                    double ret2;
                    if (n2 % 2 == 0) {
                        double m2 = n2 / 2.0d;
                        ret2 = ((m2 * (b2 - m2)) * x3) / (((a2 + (2.0d * m2)) - 1.0d) * (a2 + (2.0d * m2)));
                    } else {
                        double m3 = (n2 - 1.0d) / 2.0d;
                        ret2 = (-(((a2 + m3) * ((a2 + b2) + m3)) * x3)) / ((a2 + (2.0d * m3)) * ((a2 + (2.0d * m3)) + 1.0d));
                    }
                    return ret2;
                }

                @Override // org.apache.commons.math3.util.ContinuedFraction
                protected double getA(int n2, double x3) {
                    return 1.0d;
                }
            };
            ret = (FastMath.exp((((a2 * FastMath.log(x2)) + (b2 * FastMath.log1p(-x2))) - FastMath.log(a2)) - logBeta(a2, b2)) * 1.0d) / fraction.evaluate(x2, epsilon, maxIterations);
        }
        return ret;
    }

    @Deprecated
    public static double logBeta(double a2, double b2, double epsilon, int maxIterations) {
        return logBeta(a2, b2);
    }

    private static double logGammaSum(double a2, double b2) throws OutOfRangeException {
        if (a2 < 1.0d || a2 > 2.0d) {
            throw new OutOfRangeException(Double.valueOf(a2), Double.valueOf(1.0d), Double.valueOf(2.0d));
        }
        if (b2 < 1.0d || b2 > 2.0d) {
            throw new OutOfRangeException(Double.valueOf(b2), Double.valueOf(1.0d), Double.valueOf(2.0d));
        }
        double x2 = (a2 - 1.0d) + (b2 - 1.0d);
        if (x2 <= 0.5d) {
            return Gamma.logGamma1p(1.0d + x2);
        }
        if (x2 <= 1.5d) {
            return Gamma.logGamma1p(x2) + FastMath.log1p(x2);
        }
        return Gamma.logGamma1p(x2 - 1.0d) + FastMath.log(x2 * (1.0d + x2));
    }

    private static double logGammaMinusLogGammaSum(double a2, double b2) throws NumberIsTooSmallException, OutOfRangeException {
        double d2;
        double w2;
        if (a2 < 0.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(a2), Double.valueOf(0.0d), true);
        }
        if (b2 < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(b2), Double.valueOf(10.0d), true);
        }
        if (a2 <= b2) {
            d2 = b2 + (a2 - 0.5d);
            w2 = deltaMinusDeltaSum(a2, b2);
        } else {
            d2 = a2 + (b2 - 0.5d);
            w2 = deltaMinusDeltaSum(b2, a2);
        }
        double u2 = d2 * FastMath.log1p(a2 / b2);
        double v2 = a2 * (FastMath.log(b2) - 1.0d);
        return u2 <= v2 ? (w2 - u2) - v2 : (w2 - v2) - u2;
    }

    private static double deltaMinusDeltaSum(double a2, double b2) throws NumberIsTooSmallException, OutOfRangeException {
        if (a2 < 0.0d || a2 > b2) {
            throw new OutOfRangeException(Double.valueOf(a2), 0, Double.valueOf(b2));
        }
        if (b2 < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(b2), 10, true);
        }
        double h2 = a2 / b2;
        double p2 = h2 / (1.0d + h2);
        double q2 = 1.0d / (1.0d + h2);
        double q22 = q2 * q2;
        double[] s2 = new double[DELTA.length];
        s2[0] = 1.0d;
        for (int i2 = 1; i2 < s2.length; i2++) {
            s2[i2] = 1.0d + q2 + (q22 * s2[i2 - 1]);
        }
        double sqrtT = 10.0d / b2;
        double t2 = sqrtT * sqrtT;
        double w2 = DELTA[DELTA.length - 1] * s2[s2.length - 1];
        for (int i3 = DELTA.length - 2; i3 >= 0; i3--) {
            w2 = (t2 * w2) + (DELTA[i3] * s2[i3]);
        }
        return (w2 * p2) / b2;
    }

    private static double sumDeltaMinusDeltaSum(double p2, double q2) {
        if (p2 < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(p2), Double.valueOf(10.0d), true);
        }
        if (q2 < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(q2), Double.valueOf(10.0d), true);
        }
        double a2 = FastMath.min(p2, q2);
        double b2 = FastMath.max(p2, q2);
        double sqrtT = 10.0d / a2;
        double t2 = sqrtT * sqrtT;
        double z2 = DELTA[DELTA.length - 1];
        for (int i2 = DELTA.length - 2; i2 >= 0; i2--) {
            z2 = (t2 * z2) + DELTA[i2];
        }
        return (z2 / a2) + deltaMinusDeltaSum(a2, b2);
    }

    public static double logBeta(double p2, double q2) {
        if (Double.isNaN(p2) || Double.isNaN(q2) || p2 <= 0.0d || q2 <= 0.0d) {
            return Double.NaN;
        }
        double a2 = FastMath.min(p2, q2);
        double b2 = FastMath.max(p2, q2);
        if (a2 >= 10.0d) {
            double w2 = sumDeltaMinusDeltaSum(a2, b2);
            double h2 = a2 / b2;
            double c2 = h2 / (1.0d + h2);
            double u2 = (-(a2 - 0.5d)) * FastMath.log(c2);
            double v2 = b2 * FastMath.log1p(h2);
            if (u2 <= v2) {
                return (((((-0.5d) * FastMath.log(b2)) + HALF_LOG_TWO_PI) + w2) - u2) - v2;
            }
            return (((((-0.5d) * FastMath.log(b2)) + HALF_LOG_TWO_PI) + w2) - v2) - u2;
        }
        if (a2 <= 2.0d) {
            if (a2 < 1.0d) {
                if (b2 >= 10.0d) {
                    return Gamma.logGamma(a2) + logGammaMinusLogGammaSum(a2, b2);
                }
                return FastMath.log((Gamma.gamma(a2) * Gamma.gamma(b2)) / Gamma.gamma(a2 + b2));
            }
            if (b2 <= 2.0d) {
                return (Gamma.logGamma(a2) + Gamma.logGamma(b2)) - logGammaSum(a2, b2);
            }
            if (b2 < 10.0d) {
                double prod = 1.0d;
                double bred = b2;
                while (bred > 2.0d) {
                    bred -= 1.0d;
                    prod *= bred / (a2 + bred);
                }
                return FastMath.log(prod) + Gamma.logGamma(a2) + (Gamma.logGamma(bred) - logGammaSum(a2, bred));
            }
            return Gamma.logGamma(a2) + logGammaMinusLogGammaSum(a2, b2);
        }
        if (b2 > 1000.0d) {
            int n2 = (int) FastMath.floor(a2 - 1.0d);
            double prod2 = 1.0d;
            double ared = a2;
            for (int i2 = 0; i2 < n2; i2++) {
                ared -= 1.0d;
                prod2 *= ared / (1.0d + (ared / b2));
            }
            return (FastMath.log(prod2) - (n2 * FastMath.log(b2))) + Gamma.logGamma(ared) + logGammaMinusLogGammaSum(ared, b2);
        }
        double prod1 = 1.0d;
        double ared2 = a2;
        while (ared2 > 2.0d) {
            ared2 -= 1.0d;
            double h3 = ared2 / b2;
            prod1 *= h3 / (1.0d + h3);
        }
        if (b2 < 10.0d) {
            double prod22 = 1.0d;
            double bred2 = b2;
            while (bred2 > 2.0d) {
                bred2 -= 1.0d;
                prod22 *= bred2 / (ared2 + bred2);
            }
            return FastMath.log(prod1) + FastMath.log(prod22) + Gamma.logGamma(ared2) + (Gamma.logGamma(bred2) - logGammaSum(ared2, bred2));
        }
        return FastMath.log(prod1) + Gamma.logGamma(ared2) + logGammaMinusLogGammaSum(ared2, b2);
    }
}
