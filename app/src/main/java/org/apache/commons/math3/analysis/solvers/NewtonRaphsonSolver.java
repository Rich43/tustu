package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/NewtonRaphsonSolver.class */
public class NewtonRaphsonSolver extends AbstractUnivariateDifferentiableSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public NewtonRaphsonSolver() {
        this(1.0E-6d);
    }

    public NewtonRaphsonSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver, org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, UnivariateDifferentiableFunction f2, double min, double max) throws TooManyEvaluationsException {
        return super.solve(maxEval, f2, UnivariateSolverUtils.midpoint(min, max));
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException {
        double startValue = getStartValue();
        double absoluteAccuracy = getAbsoluteAccuracy();
        double d2 = startValue;
        while (true) {
            double x0 = d2;
            DerivativeStructure y0 = computeObjectiveValueAndDerivative(x0);
            double x1 = x0 - (y0.getValue() / y0.getPartialDerivative(1));
            if (FastMath.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }
            d2 = x1;
        }
    }
}
