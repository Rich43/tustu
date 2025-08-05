package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BracketedUnivariateSolver.class */
public interface BracketedUnivariateSolver<FUNC extends UnivariateFunction> extends BaseUnivariateSolver<FUNC> {
    double solve(int i2, FUNC func, double d2, double d3, AllowedSolution allowedSolution);

    double solve(int i2, FUNC func, double d2, double d3, double d4, AllowedSolution allowedSolution);
}
