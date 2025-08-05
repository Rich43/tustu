package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/UnivariateDifferentiableFunction.class */
public interface UnivariateDifferentiableFunction extends UnivariateFunction {
    DerivativeStructure value(DerivativeStructure derivativeStructure) throws DimensionMismatchException;
}
