package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.MultivariateFunction;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseMultivariateSimpleBoundsOptimizer.class */
public interface BaseMultivariateSimpleBoundsOptimizer<FUNC extends MultivariateFunction> extends BaseMultivariateOptimizer<FUNC> {
    PointValuePair optimize(int i2, FUNC func, GoalType goalType, double[] dArr, double[] dArr2, double[] dArr3);
}
