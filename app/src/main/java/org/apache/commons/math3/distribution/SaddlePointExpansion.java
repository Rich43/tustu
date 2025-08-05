package org.apache.commons.math3.distribution;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/SaddlePointExpansion.class */
final class SaddlePointExpansion {
    private static final double HALF_LOG_2_PI = 0.5d * FastMath.log(6.283185307179586d);
    private static final double[] EXACT_STIRLING_ERRORS = {0.0d, 0.15342640972002736d, 0.08106146679532726d, 0.05481412105191765d, 0.0413406959554093d, 0.03316287351993629d, 0.02767792568499834d, 0.023746163656297496d, 0.020790672103765093d, 0.018488450532673187d, 0.016644691189821193d, 0.015134973221917378d, 0.013876128823070748d, 0.012810465242920227d, 0.01189670994589177d, 0.011104559758206917d, 0.010411265261972096d, 0.009799416126158804d, 0.009255462182712733d, 0.008768700134139386d, 0.00833056343336287d, 0.00793411456431402d, 0.007573675487951841d, 0.007244554301320383d, 0.00694284010720953d, 0.006665247032707682d, 0.006408994188004207d, 0.006171712263039458d, 0.0059513701127588475d, 0.0057462165130101155d, 0.005554733551962801d};

    private SaddlePointExpansion() {
    }

    static double getStirlingError(double z2) {
        double ret;
        if (z2 < 15.0d) {
            double z22 = 2.0d * z2;
            if (FastMath.floor(z22) == z22) {
                ret = EXACT_STIRLING_ERRORS[(int) z22];
            } else {
                ret = ((Gamma.logGamma(z2 + 1.0d) - ((z2 + 0.5d) * FastMath.log(z2))) + z2) - HALF_LOG_2_PI;
            }
        } else {
            double z23 = z2 * z2;
            ret = (0.08333333333333333d - ((0.002777777777777778d - ((7.936507936507937E-4d - ((5.952380952380953E-4d - (8.417508417508417E-4d / z23)) / z23)) / z23)) / z23)) / z2;
        }
        return ret;
    }

    static double getDeviancePart(double x2, double mu) {
        double ret;
        if (FastMath.abs(x2 - mu) < 0.1d * (x2 + mu)) {
            double d2 = x2 - mu;
            double v2 = d2 / (x2 + mu);
            double s1 = v2 * d2;
            double s2 = Double.NaN;
            double ej = 2.0d * x2 * v2;
            double v3 = v2 * v2;
            int j2 = 1;
            while (s1 != s2) {
                s2 = s1;
                ej *= v3;
                s1 = s2 + (ej / ((j2 * 2) + 1));
                j2++;
            }
            ret = s1;
        } else {
            ret = ((x2 * FastMath.log(x2 / mu)) + mu) - x2;
        }
        return ret;
    }

    static double logBinomialProbability(int x2, int n2, double p2, double q2) {
        double ret;
        if (x2 == 0) {
            if (p2 < 0.1d) {
                ret = (-getDeviancePart(n2, n2 * q2)) - (n2 * p2);
            } else {
                ret = n2 * FastMath.log(q2);
            }
        } else if (x2 != n2) {
            double ret2 = (((getStirlingError(n2) - getStirlingError(x2)) - getStirlingError(n2 - x2)) - getDeviancePart(x2, n2 * p2)) - getDeviancePart(n2 - x2, n2 * q2);
            double f2 = ((6.283185307179586d * x2) * (n2 - x2)) / n2;
            ret = ((-0.5d) * FastMath.log(f2)) + ret2;
        } else if (q2 < 0.1d) {
            ret = (-getDeviancePart(n2, n2 * p2)) - (n2 * q2);
        } else {
            ret = n2 * FastMath.log(p2);
        }
        return ret;
    }
}
