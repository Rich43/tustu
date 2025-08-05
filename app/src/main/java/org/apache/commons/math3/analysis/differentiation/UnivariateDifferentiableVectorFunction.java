package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.UnivariateVectorFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/UnivariateDifferentiableVectorFunction.class */
public interface UnivariateDifferentiableVectorFunction extends UnivariateVectorFunction {
    DerivativeStructure[] value(DerivativeStructure derivativeStructure) throws MathIllegalArgumentException;
}
