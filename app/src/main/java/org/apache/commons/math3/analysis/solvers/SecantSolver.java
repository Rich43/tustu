package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/SecantSolver.class */
public class SecantSolver extends AbstractUnivariateSolver {
    protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public SecantSolver() {
        super(1.0E-6d);
    }

    public SecantSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public SecantSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected final double doSolve() throws TooManyEvaluationsException, NoBracketingException {
        double x0 = getMin();
        double x1 = getMax();
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);
        if (f0 == 0.0d) {
            return x0;
        }
        if (f1 == 0.0d) {
            return x1;
        }
        verifyBracketing(x0, x1);
        double ftol = getFunctionValueAccuracy();
        double atol = getAbsoluteAccuracy();
        double rtol = getRelativeAccuracy();
        do {
            double x2 = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
            double fx = computeObjectiveValue(x2);
            if (fx == 0.0d) {
                return x2;
            }
            x0 = x1;
            f0 = f1;
            x1 = x2;
            f1 = fx;
            if (FastMath.abs(f1) <= ftol) {
                return x1;
            }
        } while (FastMath.abs(x1 - x0) >= FastMath.max(rtol * FastMath.abs(x1), atol));
        return x1;
    }
}
