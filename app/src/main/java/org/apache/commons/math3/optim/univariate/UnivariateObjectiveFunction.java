package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.OptimizationData;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/UnivariateObjectiveFunction.class */
public class UnivariateObjectiveFunction implements OptimizationData {
    private final UnivariateFunction function;

    public UnivariateObjectiveFunction(UnivariateFunction f2) {
        this.function = f2;
    }

    public UnivariateFunction getObjectiveFunction() {
        return this.function;
    }
}
