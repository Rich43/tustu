package org.apache.commons.math3.analysis;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/DifferentiableMultivariateFunction.class */
public interface DifferentiableMultivariateFunction extends MultivariateFunction {
    MultivariateFunction partialDerivative(int i2);

    MultivariateVectorFunction gradient();
}
