package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/MullerSolver2.class */
public class MullerSolver2 extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public MullerSolver2() {
        this(1.0E-6d);
    }

    public MullerSolver2(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public MullerSolver2(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double denominator;
        double x2;
        double min = getMin();
        double max = getMax();
        verifyInterval(min, max);
        double relativeAccuracy = getRelativeAccuracy();
        double absoluteAccuracy = getAbsoluteAccuracy();
        double functionValueAccuracy = getFunctionValueAccuracy();
        double x0 = min;
        double y0 = computeObjectiveValue(x0);
        if (FastMath.abs(y0) < functionValueAccuracy) {
            return x0;
        }
        double x1 = max;
        double y1 = computeObjectiveValue(x1);
        if (FastMath.abs(y1) < functionValueAccuracy) {
            return x1;
        }
        if (y0 * y1 > 0.0d) {
            throw new NoBracketingException(x0, x1, y0, y1);
        }
        double x22 = 0.5d * (x0 + x1);
        double y2 = computeObjectiveValue(x22);
        double d2 = Double.POSITIVE_INFINITY;
        while (true) {
            double oldx = d2;
            double q2 = (x22 - x1) / (x1 - x0);
            double a2 = q2 * ((y2 - ((1.0d + q2) * y1)) + (q2 * y0));
            double b2 = ((((2.0d * q2) + 1.0d) * y2) - (((1.0d + q2) * (1.0d + q2)) * y1)) + (q2 * q2 * y0);
            double c2 = (1.0d + q2) * y2;
            double delta = (b2 * b2) - ((4.0d * a2) * c2);
            if (delta >= 0.0d) {
                double dplus = b2 + FastMath.sqrt(delta);
                double dminus = b2 - FastMath.sqrt(delta);
                denominator = FastMath.abs(dplus) > FastMath.abs(dminus) ? dplus : dminus;
            } else {
                denominator = FastMath.sqrt((b2 * b2) - delta);
            }
            if (denominator != 0.0d) {
                double d3 = x22 - (((2.0d * c2) * (x22 - x1)) / denominator);
                while (true) {
                    x2 = d3;
                    if (x2 != x1 && x2 != x22) {
                        break;
                    }
                    d3 = x2 + absoluteAccuracy;
                }
            } else {
                x2 = min + (FastMath.random() * (max - min));
                oldx = Double.POSITIVE_INFINITY;
            }
            double y3 = computeObjectiveValue(x2);
            double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x2), absoluteAccuracy);
            if (FastMath.abs(x2 - oldx) <= tolerance || FastMath.abs(y3) <= functionValueAccuracy) {
                break;
            }
            x0 = x1;
            y0 = y1;
            x1 = x22;
            y1 = y2;
            x22 = x2;
            y2 = y3;
            d2 = x2;
        }
        return x2;
    }
}
