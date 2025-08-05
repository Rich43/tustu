package org.apache.commons.math3.optim.nonlinear.vector;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.optim.OptimizationData;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/ModelFunctionJacobian.class */
public class ModelFunctionJacobian implements OptimizationData {
    private final MultivariateMatrixFunction jacobian;

    public ModelFunctionJacobian(MultivariateMatrixFunction j2) {
        this.jacobian = j2;
    }

    public MultivariateMatrixFunction getModelFunctionJacobian() {
        return this.jacobian;
    }
}
