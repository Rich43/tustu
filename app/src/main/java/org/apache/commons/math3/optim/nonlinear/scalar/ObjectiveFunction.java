package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.OptimizationData;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/ObjectiveFunction.class */
public class ObjectiveFunction implements OptimizationData {
    private final MultivariateFunction function;

    public ObjectiveFunction(MultivariateFunction f2) {
        this.function = f2;
    }

    public MultivariateFunction getObjectiveFunction() {
        return this.function;
    }
}
