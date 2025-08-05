package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/AbstractDifferentiableUnivariateSolver.class */
public abstract class AbstractDifferentiableUnivariateSolver extends BaseAbstractUnivariateSolver<DifferentiableUnivariateFunction> implements DifferentiableUnivariateSolver {
    private UnivariateFunction functionDerivative;

    protected AbstractDifferentiableUnivariateSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    protected AbstractDifferentiableUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    protected double computeDerivativeObjectiveValue(double point) throws TooManyEvaluationsException {
        incrementEvaluationCount();
        return this.functionDerivative.value(point);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    public void setup(int maxEval, DifferentiableUnivariateFunction f2, double min, double max, double startValue) throws NullArgumentException {
        super.setup(maxEval, (int) f2, min, max, startValue);
        this.functionDerivative = f2.derivative();
    }
}
