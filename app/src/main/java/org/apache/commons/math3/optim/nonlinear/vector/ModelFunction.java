package org.apache.commons.math3.optim.nonlinear.vector;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.OptimizationData;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/ModelFunction.class */
public class ModelFunction implements OptimizationData {
    private final MultivariateVectorFunction model;

    public ModelFunction(MultivariateVectorFunction m2) {
        this.model = m2;
    }

    public MultivariateVectorFunction getModelFunction() {
        return this.model;
    }
}
