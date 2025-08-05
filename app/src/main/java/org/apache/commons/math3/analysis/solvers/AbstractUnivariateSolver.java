package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/AbstractUnivariateSolver.class */
public abstract class AbstractUnivariateSolver extends BaseAbstractUnivariateSolver<UnivariateFunction> implements UnivariateSolver {
    protected AbstractUnivariateSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    protected AbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    protected AbstractUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }
}
