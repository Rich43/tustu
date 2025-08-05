package org.apache.commons.math3.optimization.linear;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/linear/AbstractLinearOptimizer.class */
public abstract class AbstractLinearOptimizer implements LinearOptimizer {
    public static final int DEFAULT_MAX_ITERATIONS = 100;
    private LinearObjectiveFunction function;
    private Collection<LinearConstraint> linearConstraints;
    private GoalType goal;
    private boolean nonNegative;
    private int maxIterations;
    private int iterations;

    protected abstract PointValuePair doOptimize() throws MathIllegalStateException;

    protected AbstractLinearOptimizer() {
        setMaxIterations(100);
    }

    protected boolean restrictToNonNegative() {
        return this.nonNegative;
    }

    protected GoalType getGoalType() {
        return this.goal;
    }

    protected LinearObjectiveFunction getFunction() {
        return this.function;
    }

    protected Collection<LinearConstraint> getConstraints() {
        return Collections.unmodifiableCollection(this.linearConstraints);
    }

    @Override // org.apache.commons.math3.optimization.linear.LinearOptimizer
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override // org.apache.commons.math3.optimization.linear.LinearOptimizer
    public int getMaxIterations() {
        return this.maxIterations;
    }

    @Override // org.apache.commons.math3.optimization.linear.LinearOptimizer
    public int getIterations() {
        return this.iterations;
    }

    protected void incrementIterationsCounter() throws MaxCountExceededException {
        int i2 = this.iterations + 1;
        this.iterations = i2;
        if (i2 > this.maxIterations) {
            throw new MaxCountExceededException(Integer.valueOf(this.maxIterations));
        }
    }

    @Override // org.apache.commons.math3.optimization.linear.LinearOptimizer
    public PointValuePair optimize(LinearObjectiveFunction f2, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative) throws MathIllegalStateException {
        this.function = f2;
        this.linearConstraints = constraints;
        this.goal = goalType;
        this.nonNegative = restrictToNonNegative;
        this.iterations = 0;
        return doOptimize();
    }
}
