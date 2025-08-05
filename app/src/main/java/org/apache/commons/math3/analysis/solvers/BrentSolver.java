package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BrentSolver.class */
public class BrentSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public BrentSolver() {
        this(1.0E-6d);
    }

    public BrentSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public BrentSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    public BrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException, NoBracketingException, NumberIsTooLargeException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double yInitial = computeObjectiveValue(initial);
        if (FastMath.abs(yInitial) <= functionValueAccuracy) {
            return initial;
        }
        double yMin = computeObjectiveValue(min);
        if (FastMath.abs(yMin) <= functionValueAccuracy) {
            return min;
        }
        if (yInitial * yMin < 0.0d) {
            return brent(min, initial, yMin, yInitial);
        }
        double yMax = computeObjectiveValue(max);
        if (FastMath.abs(yMax) <= functionValueAccuracy) {
            return max;
        }
        if (yInitial * yMax < 0.0d) {
            return brent(initial, max, yInitial, yMax);
        }
        throw new NoBracketingException(min, max, yMin, yMax);
    }

    private double brent(double lo, double hi, double fLo, double fHi) {
        double p2;
        double q2;
        double a2 = lo;
        double fa = fLo;
        double b2 = hi;
        double fb = fHi;
        double c2 = a2;
        double fc = fa;
        double d2 = b2 - a2;
        double e2 = d2;
        double t2 = getAbsoluteAccuracy();
        double eps = getRelativeAccuracy();
        while (true) {
            if (FastMath.abs(fc) < FastMath.abs(fb)) {
                a2 = b2;
                b2 = c2;
                c2 = a2;
                fa = fb;
                fb = fc;
                fc = fa;
            }
            double tol = (2.0d * eps * FastMath.abs(b2)) + t2;
            double m2 = 0.5d * (c2 - b2);
            if (FastMath.abs(m2) <= tol || Precision.equals(fb, 0.0d)) {
                break;
            }
            if (FastMath.abs(e2) < tol || FastMath.abs(fa) <= FastMath.abs(fb)) {
                d2 = m2;
                e2 = d2;
            } else {
                double s2 = fb / fa;
                if (a2 == c2) {
                    p2 = 2.0d * m2 * s2;
                    q2 = 1.0d - s2;
                } else {
                    double q3 = fa / fc;
                    double r2 = fb / fc;
                    p2 = s2 * ((((2.0d * m2) * q3) * (q3 - r2)) - ((b2 - a2) * (r2 - 1.0d)));
                    q2 = (q3 - 1.0d) * (r2 - 1.0d) * (s2 - 1.0d);
                }
                if (p2 > 0.0d) {
                    q2 = -q2;
                } else {
                    p2 = -p2;
                }
                double s3 = e2;
                e2 = d2;
                if (p2 >= ((1.5d * m2) * q2) - FastMath.abs(tol * q2) || p2 >= FastMath.abs(0.5d * s3 * q2)) {
                    d2 = m2;
                    e2 = d2;
                } else {
                    d2 = p2 / q2;
                }
            }
            a2 = b2;
            fa = fb;
            if (FastMath.abs(d2) > tol) {
                b2 += d2;
            } else if (m2 > 0.0d) {
                b2 += tol;
            } else {
                b2 -= tol;
            }
            fb = computeObjectiveValue(b2);
            if ((fb > 0.0d && fc > 0.0d) || (fb <= 0.0d && fc <= 0.0d)) {
                c2 = a2;
                fc = fa;
                d2 = b2 - a2;
                e2 = d2;
            }
        }
        return b2;
    }
}
