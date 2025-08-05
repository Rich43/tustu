package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.GoalType;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/univariate/BaseUnivariateOptimizer.class */
public interface BaseUnivariateOptimizer<FUNC extends UnivariateFunction> extends BaseOptimizer<UnivariatePointValuePair> {
    UnivariatePointValuePair optimize(int i2, FUNC func, GoalType goalType, double d2, double d3);

    UnivariatePointValuePair optimize(int i2, FUNC func, GoalType goalType, double d2, double d3, double d4);
}
