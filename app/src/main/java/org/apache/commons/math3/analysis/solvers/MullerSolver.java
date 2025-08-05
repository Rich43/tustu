package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/MullerSolver.class */
public class MullerSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public MullerSolver() {
        this(1.0E-6d);
    }

    public MullerSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public MullerSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double fMin = computeObjectiveValue(min);
        if (FastMath.abs(fMin) < functionValueAccuracy) {
            return min;
        }
        double fMax = computeObjectiveValue(max);
        if (FastMath.abs(fMax) < functionValueAccuracy) {
            return max;
        }
        double fInitial = computeObjectiveValue(initial);
        if (FastMath.abs(fInitial) < functionValueAccuracy) {
            return initial;
        }
        verifyBracketing(min, max);
        if (isBracketing(min, initial)) {
            return solve(min, initial, fMin, fInitial);
        }
        return solve(initial, max, fInitial, fMax);
    }

    private double solve(double min, double max, double fMin, double fMax) throws TooManyEvaluationsException {
        double x2;
        double relativeAccuracy = getRelativeAccuracy();
        double absoluteAccuracy = getAbsoluteAccuracy();
        double functionValueAccuracy = getFunctionValueAccuracy();
        double x0 = min;
        double y0 = fMin;
        double x22 = max;
        double y2 = fMax;
        double x1 = 0.5d * (x0 + x22);
        double y1 = computeObjectiveValue(x1);
        double d2 = Double.POSITIVE_INFINITY;
        while (true) {
            double oldx = d2;
            double d01 = (y1 - y0) / (x1 - x0);
            double d12 = (y2 - y1) / (x22 - x1);
            double d012 = (d12 - d01) / (x22 - x0);
            double c1 = d01 + ((x1 - x0) * d012);
            double delta = (c1 * c1) - ((4.0d * y1) * d012);
            double xplus = x1 + (((-2.0d) * y1) / (c1 + FastMath.sqrt(delta)));
            double xminus = x1 + (((-2.0d) * y1) / (c1 - FastMath.sqrt(delta)));
            x2 = isSequence(x0, xplus, x22) ? xplus : xminus;
            double y3 = computeObjectiveValue(x2);
            double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x2), absoluteAccuracy);
            if (FastMath.abs(x2 - oldx) <= tolerance || FastMath.abs(y3) <= functionValueAccuracy) {
                break;
            }
            boolean bisect = (x2 < x1 && x1 - x0 > 0.95d * (x22 - x0)) || (x2 > x1 && x22 - x1 > 0.95d * (x22 - x0)) || x2 == x1;
            if (!bisect) {
                x0 = x2 < x1 ? x0 : x1;
                y0 = x2 < x1 ? y0 : y1;
                x22 = x2 > x1 ? x22 : x1;
                y2 = x2 > x1 ? y2 : y1;
                x1 = x2;
                y1 = y3;
                d2 = x2;
            } else {
                double xm = 0.5d * (x0 + x22);
                double ym = computeObjectiveValue(xm);
                if (FastMath.signum(y0) + FastMath.signum(ym) == 0.0d) {
                    x22 = xm;
                    y2 = ym;
                } else {
                    x0 = xm;
                    y0 = ym;
                }
                x1 = 0.5d * (x0 + x22);
                y1 = computeObjectiveValue(x1);
                d2 = Double.POSITIVE_INFINITY;
            }
        }
        return x2;
    }
}
