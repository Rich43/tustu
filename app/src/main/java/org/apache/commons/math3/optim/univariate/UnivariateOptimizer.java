package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/UnivariateOptimizer.class */
public abstract class UnivariateOptimizer extends BaseOptimizer<UnivariatePointValuePair> {
    private UnivariateFunction function;
    private GoalType goal;
    private double start;
    private double min;
    private double max;

    protected UnivariateOptimizer(ConvergenceChecker<UnivariatePointValuePair> checker) {
        super(checker);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public UnivariatePointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        return (UnivariatePointValuePair) super.optimize(optData);
    }

    public GoalType getGoalType() {
        return this.goal;
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof SearchInterval) {
                SearchInterval interval = (SearchInterval) data;
                this.min = interval.getMin();
                this.max = interval.getMax();
                this.start = interval.getStartValue();
            } else if (data instanceof UnivariateObjectiveFunction) {
                this.function = ((UnivariateObjectiveFunction) data).getObjectiveFunction();
            } else if (data instanceof GoalType) {
                this.goal = (GoalType) data;
            }
        }
    }

    public double getStartValue() {
        return this.start;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    protected double computeObjectiveValue(double x2) throws MaxCountExceededException {
        super.incrementEvaluationCount();
        return this.function.value(x2);
    }
}
