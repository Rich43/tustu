package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/MultivariateDifferentiableFunction.class */
public interface MultivariateDifferentiableFunction extends MultivariateFunction {
    DerivativeStructure value(DerivativeStructure[] derivativeStructureArr) throws MathIllegalArgumentException;
}
