package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/AbstractUnivariateDifferentiableSolver.class */
public abstract class AbstractUnivariateDifferentiableSolver extends BaseAbstractUnivariateSolver<UnivariateDifferentiableFunction> implements UnivariateDifferentiableSolver {
    private UnivariateDifferentiableFunction function;

    protected AbstractUnivariateDifferentiableSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    protected AbstractUnivariateDifferentiableSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    protected DerivativeStructure computeObjectiveValueAndDerivative(double point) throws TooManyEvaluationsException {
        incrementEvaluationCount();
        return this.function.value(new DerivativeStructure(1, 1, 0, point));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    public void setup(int maxEval, UnivariateDifferentiableFunction f2, double min, double max, double startValue) throws NullArgumentException {
        super.setup(maxEval, (int) f2, min, max, startValue);
        this.function = f2;
    }
}
