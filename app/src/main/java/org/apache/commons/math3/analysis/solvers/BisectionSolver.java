package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BisectionSolver.class */
public class BisectionSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public BisectionSolver() {
        this(1.0E-6d);
    }

    public BisectionSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public BisectionSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected double doSolve() throws TooManyEvaluationsException {
        double min = getMin();
        double max = getMax();
        verifyInterval(min, max);
        double absoluteAccuracy = getAbsoluteAccuracy();
        do {
            double m2 = UnivariateSolverUtils.midpoint(min, max);
            double fmin = computeObjectiveValue(min);
            double fm = computeObjectiveValue(m2);
            if (fm * fmin > 0.0d) {
                min = m2;
            } else {
                max = m2;
            }
        } while (FastMath.abs(max - min) > absoluteAccuracy);
        return UnivariateSolverUtils.midpoint(min, max);
    }
}
