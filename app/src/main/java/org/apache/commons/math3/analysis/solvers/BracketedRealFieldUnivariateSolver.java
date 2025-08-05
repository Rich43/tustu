package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BracketedRealFieldUnivariateSolver.class */
public interface BracketedRealFieldUnivariateSolver<T extends RealFieldElement<T>> {
    int getMaxEvaluations();

    int getEvaluations();

    T getAbsoluteAccuracy();

    T getRelativeAccuracy();

    T getFunctionValueAccuracy();

    T solve(int i2, RealFieldUnivariateFunction<T> realFieldUnivariateFunction, T t2, T t3, AllowedSolution allowedSolution);

    T solve(int i2, RealFieldUnivariateFunction<T> realFieldUnivariateFunction, T t2, T t3, T t4, AllowedSolution allowedSolution);
}
