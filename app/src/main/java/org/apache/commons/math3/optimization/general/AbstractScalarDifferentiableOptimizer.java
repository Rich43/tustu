package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.DifferentiableMultivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.DifferentiableMultivariateOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/AbstractScalarDifferentiableOptimizer.class */
public abstract class AbstractScalarDifferentiableOptimizer extends BaseAbstractMultivariateOptimizer<DifferentiableMultivariateFunction> implements DifferentiableMultivariateOptimizer {
    private MultivariateVectorFunction gradient;

    @Deprecated
    protected AbstractScalarDifferentiableOptimizer() {
    }

    protected AbstractScalarDifferentiableOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    protected double[] computeObjectiveGradient(double[] evaluationPoint) {
        return this.gradient.value(evaluationPoint);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    public PointValuePair optimizeInternal(int maxEval, DifferentiableMultivariateFunction f2, GoalType goalType, double[] startPoint) {
        this.gradient = f2.gradient();
        return super.optimizeInternal(maxEval, (int) f2, goalType, startPoint);
    }

    public PointValuePair optimize(int maxEval, MultivariateDifferentiableFunction f2, GoalType goalType, double[] startPoint) {
        return optimizeInternal(maxEval, FunctionUtils.toDifferentiableMultivariateFunction(f2), goalType, startPoint);
    }
}
