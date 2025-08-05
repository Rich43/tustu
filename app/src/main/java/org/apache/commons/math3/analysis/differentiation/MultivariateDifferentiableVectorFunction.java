package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/MultivariateDifferentiableVectorFunction.class */
public interface MultivariateDifferentiableVectorFunction extends MultivariateVectorFunction {
    DerivativeStructure[] value(DerivativeStructure[] derivativeStructureArr) throws MathIllegalArgumentException;
}
