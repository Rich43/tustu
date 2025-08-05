package org.apache.commons.math3.special;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/special/Gamma.class */
public class Gamma {
    public static final double GAMMA = 0.5772156649015329d;
    public static final double LANCZOS_G = 4.7421875d;
    private static final double DEFAULT_EPSILON = 1.0E-14d;
    private static final double[] LANCZOS = {0.9999999999999971d, 57.15623566586292d, -59.59796035547549d, 14.136097974741746d, -0.4919138160976202d, 3.399464998481189E-5d, 4.652362892704858E-5d, -9.837447530487956E-5d, 1.580887032249125E-4d, -2.1026444172410488E-4d, 2.1743961811521265E-4d, -1.643181065367639E-4d, 8.441822398385275E-5d, -2.6190838401581408E-5d, 3.6899182659531625E-6d};
    private static final double HALF_LOG_2_PI = 0.5d * FastMath.log(6.283185307179586d);
    private static final double SQRT_TWO_PI = 2.5066282746310007d;
    private static final double C_LIMIT = 49.0d;
    private static final double S_LIMIT = 1.0E-5d;
    private static final double INV_GAMMA1P_M1_A0 = 6.116095104481416E-9d;
    private static final double INV_GAMMA1P_M1_A1 = 6.247308301164655E-9d;
    private static final double INV_GAMMA1P_M1_B1 = 0.203610414066807d;
    private static final double INV_GAMMA1P_M1_B2 = 0.026620534842894922d;
    private static final double INV_GAMMA1P_M1_B3 = 4.939449793824468E-4d;
    private static final double INV_GAMMA1P_M1_B4 = -8.514194324403149E-6d;
    private static final double INV_GAMMA1P_M1_B5 = -6.4304548177935305E-6d;
    private static final double INV_GAMMA1P_M1_B6 = 9.926418406727737E-7d;
    private static final double INV_GAMMA1P_M1_B7 = -6.077618957228252E-8d;
    private static final double INV_GAMMA1P_M1_B8 = 1.9575583661463974E-10d;
    private static final double INV_GAMMA1P_M1_P0 = 6.116095104481416E-9d;
    private static final double INV_GAMMA1P_M1_P1 = 6.8716741130671986E-9d;
    private static final double INV_GAMMA1P_M1_P2 = 6.820161668496171E-10d;
    private static final double INV_GAMMA1P_M1_P3 = 4.686843322948848E-11d;
    private static final double INV_GAMMA1P_M1_P4 = 1.5728330277104463E-12d;
    private static final double INV_GAMMA1P_M1_P5 = -1.2494415722763663E-13d;
    private static final double INV_GAMMA1P_M1_P6 = 4.343529937408594E-15d;
    private static final double INV_GAMMA1P_M1_Q1 = 0.3056961078365221d;
    private static final double INV_GAMMA1P_M1_Q2 = 0.054642130860422966d;
    private static final double INV_GAMMA1P_M1_Q3 = 0.004956830093825887d;
    private static final double INV_GAMMA1P_M1_Q4 = 2.6923694661863613E-4d;
    private static final double INV_GAMMA1P_M1_C = -0.42278433509846713d;
    private static final double INV_GAMMA1P_M1_C0 = 0.5772156649015329d;
    private static final double INV_GAMMA1P_M1_C1 = -0.6558780715202539d;
    private static final double INV_GAMMA1P_M1_C2 = -0.04200263503409524d;
    private static final double INV_GAMMA1P_M1_C3 = 0.16653861138229148d;
    private static final double INV_GAMMA1P_M1_C4 = -0.04219773455554433d;
    private static final double INV_GAMMA1P_M1_C5 = -0.009621971527876973d;
    private static final double INV_GAMMA1P_M1_C6 = 0.0072189432466631d;
    private static final double INV_GAMMA1P_M1_C7 = -0.0011651675918590652d;
    private static final double INV_GAMMA1P_M1_C8 = -2.1524167411495098E-4d;
    private static final double INV_GAMMA1P_M1_C9 = 1.280502823881162E-4d;
    private static final double INV_GAMMA1P_M1_C10 = -2.013485478078824E-5d;
    private static final double INV_GAMMA1P_M1_C11 = -1.2504934821426706E-6d;
    private static final double INV_GAMMA1P_M1_C12 = 1.133027231981696E-6d;
    private static final double INV_GAMMA1P_M1_C13 = -2.056338416977607E-7d;

    private Gamma() {
    }

    public static double logGamma(double x2) {
        double ret;
        if (Double.isNaN(x2) || x2 <= 0.0d) {
            ret = Double.NaN;
        } else {
            if (x2 < 0.5d) {
                return logGamma1p(x2) - FastMath.log(x2);
            }
            if (x2 <= 2.5d) {
                return logGamma1p((x2 - 0.5d) - 0.5d);
            }
            if (x2 <= 8.0d) {
                int n2 = (int) FastMath.floor(x2 - 1.5d);
                double prod = 1.0d;
                for (int i2 = 1; i2 <= n2; i2++) {
                    prod *= x2 - i2;
                }
                return logGamma1p(x2 - (n2 + 1)) + FastMath.log(prod);
            }
            double sum = lanczos(x2);
            double tmp = x2 + 4.7421875d + 0.5d;
            ret = (((x2 + 0.5d) * FastMath.log(tmp)) - tmp) + HALF_LOG_2_PI + FastMath.log(sum / x2);
        }
        return ret;
    }

    public static double regularizedGammaP(double a2, double x2) {
        return regularizedGammaP(a2, x2, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaP(double a2, double x2, double epsilon, int maxIterations) {
        double ret;
        double sum;
        if (Double.isNaN(a2) || Double.isNaN(x2) || a2 <= 0.0d || x2 < 0.0d) {
            ret = Double.NaN;
        } else if (x2 == 0.0d) {
            ret = 0.0d;
        } else if (x2 >= a2 + 1.0d) {
            ret = 1.0d - regularizedGammaQ(a2, x2, epsilon, maxIterations);
        } else {
            double n2 = 0.0d;
            double an2 = 1.0d / a2;
            double d2 = an2;
            while (true) {
                sum = d2;
                if (FastMath.abs(an2 / sum) <= epsilon || n2 >= maxIterations || sum >= Double.POSITIVE_INFINITY) {
                    break;
                }
                n2 += 1.0d;
                an2 *= x2 / (a2 + n2);
                d2 = sum + an2;
            }
            if (n2 >= maxIterations) {
                throw new MaxCountExceededException(Integer.valueOf(maxIterations));
            }
            if (Double.isInfinite(sum)) {
                ret = 1.0d;
            } else {
                ret = FastMath.exp(((-x2) + (a2 * FastMath.log(x2))) - logGamma(a2)) * sum;
            }
        }
        return ret;
    }

    public static double regularizedGammaQ(double a2, double x2) {
        return regularizedGammaQ(a2, x2, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaQ(final double a2, double x2, double epsilon, int maxIterations) {
        double ret;
        if (Double.isNaN(a2) || Double.isNaN(x2) || a2 <= 0.0d || x2 < 0.0d) {
            ret = Double.NaN;
        } else if (x2 == 0.0d) {
            ret = 1.0d;
        } else if (x2 < a2 + 1.0d) {
            ret = 1.0d - regularizedGammaP(a2, x2, epsilon, maxIterations);
        } else {
            ContinuedFraction cf = new ContinuedFraction() { // from class: org.apache.commons.math3.special.Gamma.1
                @Override // org.apache.commons.math3.util.ContinuedFraction
                protected double getA(int n2, double x3) {
                    return (((2.0d * n2) + 1.0d) - a2) + x3;
                }

                @Override // org.apache.commons.math3.util.ContinuedFraction
                protected double getB(int n2, double x3) {
                    return n2 * (a2 - n2);
                }
            };
            double ret2 = 1.0d / cf.evaluate(x2, epsilon, maxIterations);
            ret = FastMath.exp(((-x2) + (a2 * FastMath.log(x2))) - logGamma(a2)) * ret2;
        }
        return ret;
    }

    public static double digamma(double x2) {
        if (Double.isNaN(x2) || Double.isInfinite(x2)) {
            return x2;
        }
        if (x2 > 0.0d && x2 <= S_LIMIT) {
            return (-0.5772156649015329d) - (1.0d / x2);
        }
        if (x2 >= C_LIMIT) {
            double inv = 1.0d / (x2 * x2);
            return (FastMath.log(x2) - (0.5d / x2)) - (inv * (0.08333333333333333d + (inv * (0.008333333333333333d - (inv / 252.0d)))));
        }
        return digamma(x2 + 1.0d) - (1.0d / x2);
    }

    public static double trigamma(double x2) {
        if (Double.isNaN(x2) || Double.isInfinite(x2)) {
            return x2;
        }
        if (x2 > 0.0d && x2 <= S_LIMIT) {
            return 1.0d / (x2 * x2);
        }
        if (x2 >= C_LIMIT) {
            double inv = 1.0d / (x2 * x2);
            return (1.0d / x2) + (inv / 2.0d) + ((inv / x2) * (0.16666666666666666d - (inv * (0.03333333333333333d + (inv / 42.0d)))));
        }
        return trigamma(x2 + 1.0d) + (1.0d / (x2 * x2));
    }

    public static double lanczos(double x2) {
        double sum = 0.0d;
        for (int i2 = LANCZOS.length - 1; i2 > 0; i2--) {
            sum += LANCZOS[i2] / (x2 + i2);
        }
        return sum + LANCZOS[0];
    }

    public static double invGamma1pm1(double x2) {
        double ret;
        if (x2 < -0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(x2), Double.valueOf(-0.5d), true);
        }
        if (x2 > 1.5d) {
            throw new NumberIsTooLargeException(Double.valueOf(x2), Double.valueOf(1.5d), true);
        }
        double t2 = x2 <= 0.5d ? x2 : (x2 - 0.5d) - 0.5d;
        if (t2 < 0.0d) {
            double a2 = 6.116095104481416E-9d + (t2 * INV_GAMMA1P_M1_A1);
            double b2 = INV_GAMMA1P_M1_B7 + (t2 * INV_GAMMA1P_M1_B8);
            double c2 = INV_GAMMA1P_M1_C13 + (t2 * (a2 / (1.0d + (t2 * (INV_GAMMA1P_M1_B1 + (t2 * (INV_GAMMA1P_M1_B2 + (t2 * (INV_GAMMA1P_M1_B3 + (t2 * (INV_GAMMA1P_M1_B4 + (t2 * (INV_GAMMA1P_M1_B5 + (t2 * (INV_GAMMA1P_M1_B6 + (t2 * b2))))))))))))))));
            double c3 = INV_GAMMA1P_M1_C + (t2 * (INV_GAMMA1P_M1_C1 + (t2 * (INV_GAMMA1P_M1_C2 + (t2 * (INV_GAMMA1P_M1_C3 + (t2 * (INV_GAMMA1P_M1_C4 + (t2 * (INV_GAMMA1P_M1_C5 + (t2 * (INV_GAMMA1P_M1_C6 + (t2 * (INV_GAMMA1P_M1_C7 + (t2 * (INV_GAMMA1P_M1_C8 + (t2 * (INV_GAMMA1P_M1_C9 + (t2 * (INV_GAMMA1P_M1_C10 + (t2 * (INV_GAMMA1P_M1_C11 + (t2 * (INV_GAMMA1P_M1_C12 + (t2 * c2)))))))))))))))))))))))));
            if (x2 > 0.5d) {
                ret = (t2 * c3) / x2;
            } else {
                ret = x2 * (c3 + 0.5d + 0.5d);
            }
        } else {
            double p2 = INV_GAMMA1P_M1_P5 + (t2 * INV_GAMMA1P_M1_P6);
            double p3 = 6.116095104481416E-9d + (t2 * (INV_GAMMA1P_M1_P1 + (t2 * (INV_GAMMA1P_M1_P2 + (t2 * (INV_GAMMA1P_M1_P3 + (t2 * (INV_GAMMA1P_M1_P4 + (t2 * p2)))))))));
            double q2 = INV_GAMMA1P_M1_Q3 + (t2 * INV_GAMMA1P_M1_Q4);
            double c4 = 0.5772156649015329d + (t2 * (INV_GAMMA1P_M1_C1 + (t2 * (INV_GAMMA1P_M1_C2 + (t2 * (INV_GAMMA1P_M1_C3 + (t2 * (INV_GAMMA1P_M1_C4 + (t2 * (INV_GAMMA1P_M1_C5 + (t2 * (INV_GAMMA1P_M1_C6 + (t2 * (INV_GAMMA1P_M1_C7 + (t2 * (INV_GAMMA1P_M1_C8 + (t2 * (INV_GAMMA1P_M1_C9 + (t2 * (INV_GAMMA1P_M1_C10 + (t2 * (INV_GAMMA1P_M1_C11 + (t2 * (INV_GAMMA1P_M1_C12 + (t2 * (INV_GAMMA1P_M1_C13 + ((p3 / (1.0d + (t2 * (INV_GAMMA1P_M1_Q1 + (t2 * (INV_GAMMA1P_M1_Q2 + (t2 * q2))))))) * t2)))))))))))))))))))))))))));
            if (x2 > 0.5d) {
                ret = (t2 / x2) * ((c4 - 0.5d) - 0.5d);
            } else {
                ret = x2 * c4;
            }
        }
        return ret;
    }

    public static double logGamma1p(double x2) throws NumberIsTooSmallException, NumberIsTooLargeException {
        if (x2 < -0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(x2), Double.valueOf(-0.5d), true);
        }
        if (x2 > 1.5d) {
            throw new NumberIsTooLargeException(Double.valueOf(x2), Double.valueOf(1.5d), true);
        }
        return -FastMath.log1p(invGamma1pm1(x2));
    }

    public static double gamma(double x2) {
        double ret;
        if (x2 == FastMath.rint(x2) && x2 <= 0.0d) {
            return Double.NaN;
        }
        double absX = FastMath.abs(x2);
        if (absX > 20.0d) {
            double y2 = absX + 4.7421875d + 0.5d;
            double gammaAbs = (SQRT_TWO_PI / absX) * FastMath.pow(y2, absX + 0.5d) * FastMath.exp(-y2) * lanczos(absX);
            if (x2 > 0.0d) {
                ret = gammaAbs;
            } else {
                ret = (-3.141592653589793d) / ((x2 * FastMath.sin(3.141592653589793d * x2)) * gammaAbs);
            }
        } else if (x2 >= 1.0d) {
            double prod = 1.0d;
            double t2 = x2;
            while (t2 > 2.5d) {
                t2 -= 1.0d;
                prod *= t2;
            }
            ret = prod / (1.0d + invGamma1pm1(t2 - 1.0d));
        } else {
            double prod2 = x2;
            double t3 = x2;
            while (t3 < -0.5d) {
                t3 += 1.0d;
                prod2 *= t3;
            }
            ret = 1.0d / (prod2 * (1.0d + invGamma1pm1(t3)));
        }
        return ret;
    }
}
