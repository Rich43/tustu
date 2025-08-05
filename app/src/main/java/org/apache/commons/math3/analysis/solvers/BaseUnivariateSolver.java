package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BaseUnivariateSolver.class */
public interface BaseUnivariateSolver<FUNC extends UnivariateFunction> {
    int getMaxEvaluations();

    int getEvaluations();

    double getAbsoluteAccuracy();

    double getRelativeAccuracy();

    double getFunctionValueAccuracy();

    double solve(int i2, FUNC func, double d2, double d3) throws TooManyEvaluationsException, MathIllegalArgumentException;

    double solve(int i2, FUNC func, double d2, double d3, double d4) throws TooManyEvaluationsException, MathIllegalArgumentException;

    double solve(int i2, FUNC func, double d2);
}
