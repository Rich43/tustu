package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/ContinuedFraction.class */
public abstract class ContinuedFraction {
    private static final double DEFAULT_EPSILON = 1.0E-8d;

    protected abstract double getA(int i2, double d2);

    protected abstract double getB(int i2, double d2);

    protected ContinuedFraction() {
    }

    public double evaluate(double x2) throws ConvergenceException {
        return evaluate(x2, 1.0E-8d, Integer.MAX_VALUE);
    }

    public double evaluate(double x2, double epsilon) throws ConvergenceException {
        return evaluate(x2, epsilon, Integer.MAX_VALUE);
    }

    public double evaluate(double x2, int maxIterations) throws ConvergenceException, MaxCountExceededException {
        return evaluate(x2, 1.0E-8d, maxIterations);
    }

    public double evaluate(double x2, double epsilon, int maxIterations) throws ConvergenceException, MaxCountExceededException {
        double hPrev = getA(0, x2);
        if (Precision.equals(hPrev, 0.0d, 1.0E-50d)) {
            hPrev = 1.0E-50d;
        }
        int n2 = 1;
        double dPrev = 0.0d;
        double cPrev = hPrev;
        double hN = hPrev;
        while (n2 < maxIterations) {
            double a2 = getA(n2, x2);
            double b2 = getB(n2, x2);
            double dN = a2 + (b2 * dPrev);
            if (Precision.equals(dN, 0.0d, 1.0E-50d)) {
                dN = 1.0E-50d;
            }
            double cN = a2 + (b2 / cPrev);
            if (Precision.equals(cN, 0.0d, 1.0E-50d)) {
                cN = 1.0E-50d;
            }
            double dN2 = 1.0d / dN;
            double deltaN = cN * dN2;
            hN = hPrev * deltaN;
            if (Double.isInfinite(hN)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, Double.valueOf(x2));
            }
            if (Double.isNaN(hN)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, Double.valueOf(x2));
            }
            if (FastMath.abs(deltaN - 1.0d) < epsilon) {
                break;
            }
            dPrev = dN2;
            cPrev = cN;
            hPrev = hN;
            n2++;
        }
        if (n2 >= maxIterations) {
            throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, Integer.valueOf(maxIterations), Double.valueOf(x2));
        }
        return hN;
    }
}
