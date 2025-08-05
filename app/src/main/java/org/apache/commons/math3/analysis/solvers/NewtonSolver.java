package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/NewtonSolver.class */
public class NewtonSolver extends AbstractDifferentiableUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public NewtonSolver() {
        this(1.0E-6d);
    }

    public NewtonSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver, org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, DifferentiableUnivariateFunction f2, double min, double max) throws TooManyEvaluationsException {
        return super.solve(maxEval, f2, UnivariateSolverUtils.midpoint(min, max));
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException {
        double startValue = getStartValue();
        double absoluteAccuracy = getAbsoluteAccuracy();
        double d2 = startValue;
        while (true) {
            double x0 = d2;
            double x1 = x0 - (computeObjectiveValue(x0) / computeDerivativeObjectiveValue(x0));
            if (FastMath.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }
            d2 = x1;
        }
    }
}
