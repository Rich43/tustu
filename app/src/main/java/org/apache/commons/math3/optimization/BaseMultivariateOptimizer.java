package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateFunction;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseMultivariateOptimizer.class */
public interface BaseMultivariateOptimizer<FUNC extends MultivariateFunction> extends BaseOptimizer<PointValuePair> {
    @Deprecated
    PointValuePair optimize(int i2, FUNC func, GoalType goalType, double[] dArr);
}
