package org.apache.commons.math3.optim.nonlinear.vector;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointVectorValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/JacobianMultivariateVectorOptimizer.class */
public abstract class JacobianMultivariateVectorOptimizer extends MultivariateVectorOptimizer {
    private MultivariateMatrixFunction jacobian;

    protected JacobianMultivariateVectorOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
    }

    protected double[][] computeJacobian(double[] params) {
        return this.jacobian.value(params);
    }

    @Override // org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PointVectorValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException, DimensionMismatchException {
        return super.optimize(optData);
    }

    @Override // org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof ModelFunctionJacobian) {
                this.jacobian = ((ModelFunctionJacobian) data).getModelFunctionJacobian();
                return;
            }
        }
    }
}
