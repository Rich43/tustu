package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.OptimizationData;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/ObjectiveFunctionGradient.class */
public class ObjectiveFunctionGradient implements OptimizationData {
    private final MultivariateVectorFunction gradient;

    public ObjectiveFunctionGradient(MultivariateVectorFunction g2) {
        this.gradient = g2;
    }

    public MultivariateVectorFunction getObjectiveFunctionGradient() {
        return this.gradient;
    }
}
