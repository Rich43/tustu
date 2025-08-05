package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.UnivariateMatrixFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/UnivariateDifferentiableMatrixFunction.class */
public interface UnivariateDifferentiableMatrixFunction extends UnivariateMatrixFunction {
    DerivativeStructure[][] value(DerivativeStructure derivativeStructure) throws MathIllegalArgumentException;
}
