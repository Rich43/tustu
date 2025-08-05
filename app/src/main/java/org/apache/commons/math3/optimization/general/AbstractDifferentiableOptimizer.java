package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.differentiation.GradientFunction;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/general/AbstractDifferentiableOptimizer.class */
public abstract class AbstractDifferentiableOptimizer extends BaseAbstractMultivariateOptimizer<MultivariateDifferentiableFunction> {
    private MultivariateVectorFunction gradient;

    protected AbstractDifferentiableOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    protected double[] computeObjectiveGradient(double[] evaluationPoint) {
        return this.gradient.value(evaluationPoint);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    @Deprecated
    public PointValuePair optimizeInternal(int maxEval, MultivariateDifferentiableFunction f2, GoalType goalType, double[] startPoint) {
        return optimizeInternal(maxEval, f2, goalType, new InitialGuess(startPoint));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    public PointValuePair optimizeInternal(int maxEval, MultivariateDifferentiableFunction f2, GoalType goalType, OptimizationData... optData) {
        this.gradient = new GradientFunction(f2);
        return super.optimizeInternal(maxEval, (int) f2, goalType, optData);
    }
}
