package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseMultivariateVectorOptimizer.class */
public interface BaseMultivariateVectorOptimizer<FUNC extends MultivariateVectorFunction> extends BaseOptimizer<PointVectorValuePair> {
    @Deprecated
    PointVectorValuePair optimize(int i2, FUNC func, double[] dArr, double[] dArr2, double[] dArr3);
}
