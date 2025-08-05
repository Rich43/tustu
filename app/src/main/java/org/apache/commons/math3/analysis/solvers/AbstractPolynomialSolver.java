package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.NullArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/AbstractPolynomialSolver.class */
public abstract class AbstractPolynomialSolver extends BaseAbstractUnivariateSolver<PolynomialFunction> implements PolynomialSolver {
    private PolynomialFunction polynomialFunction;

    protected AbstractPolynomialSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    protected AbstractPolynomialSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    protected AbstractPolynomialSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    public void setup(int maxEval, PolynomialFunction f2, double min, double max, double startValue) throws NullArgumentException {
        super.setup(maxEval, (int) f2, min, max, startValue);
        this.polynomialFunction = f2;
    }

    protected double[] getCoefficients() {
        return this.polynomialFunction.getCoefficients();
    }
}
