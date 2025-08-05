package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.BaseMultivariateOptimizer;
import org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/BaseAbstractMultivariateSimpleBoundsOptimizer.class */
public abstract class BaseAbstractMultivariateSimpleBoundsOptimizer<FUNC extends MultivariateFunction> extends BaseAbstractMultivariateOptimizer<FUNC> implements BaseMultivariateOptimizer<FUNC>, BaseMultivariateSimpleBoundsOptimizer<FUNC> {
    @Deprecated
    protected BaseAbstractMultivariateSimpleBoundsOptimizer() {
    }

    protected BaseAbstractMultivariateSimpleBoundsOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer, org.apache.commons.math3.optimization.BaseMultivariateOptimizer
    public PointValuePair optimize(int maxEval, FUNC f2, GoalType goalType, double[] startPoint) {
        return super.optimizeInternal(maxEval, (int) f2, goalType, new InitialGuess(startPoint));
    }

    @Override // org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer
    public PointValuePair optimize(int maxEval, FUNC f2, GoalType goalType, double[] startPoint, double[] lower, double[] upper) {
        return super.optimizeInternal(maxEval, (int) f2, goalType, new InitialGuess(startPoint), new SimpleBounds(lower, upper));
    }
}
