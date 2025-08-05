package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Incrementor;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/AbstractOptimizationProblem.class */
public abstract class AbstractOptimizationProblem<PAIR> implements OptimizationProblem<PAIR> {
    private static final MaxEvalCallback MAX_EVAL_CALLBACK = new MaxEvalCallback();
    private static final MaxIterCallback MAX_ITER_CALLBACK = new MaxIterCallback();
    private final int maxEvaluations;
    private final int maxIterations;
    private final ConvergenceChecker<PAIR> checker;

    protected AbstractOptimizationProblem(int maxEvaluations, int maxIterations, ConvergenceChecker<PAIR> checker) {
        this.maxEvaluations = maxEvaluations;
        this.maxIterations = maxIterations;
        this.checker = checker;
    }

    @Override // org.apache.commons.math3.optim.OptimizationProblem
    public Incrementor getEvaluationCounter() {
        return new Incrementor(this.maxEvaluations, MAX_EVAL_CALLBACK);
    }

    @Override // org.apache.commons.math3.optim.OptimizationProblem
    public Incrementor getIterationCounter() {
        return new Incrementor(this.maxIterations, MAX_ITER_CALLBACK);
    }

    @Override // org.apache.commons.math3.optim.OptimizationProblem
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return this.checker;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/AbstractOptimizationProblem$MaxEvalCallback.class */
    private static class MaxEvalCallback implements Incrementor.MaxCountExceededCallback {
        private MaxEvalCallback() {
        }

        @Override // org.apache.commons.math3.util.Incrementor.MaxCountExceededCallback
        public void trigger(int max) {
            throw new TooManyEvaluationsException(Integer.valueOf(max));
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/AbstractOptimizationProblem$MaxIterCallback.class */
    private static class MaxIterCallback implements Incrementor.MaxCountExceededCallback {
        private MaxIterCallback() {
        }

        @Override // org.apache.commons.math3.util.Incrementor.MaxCountExceededCallback
        public void trigger(int max) {
            throw new TooManyIterationsException(Integer.valueOf(max));
        }
    }
}
