package org.apache.commons.math3.optim;

import org.apache.commons.math3.util.Incrementor;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/OptimizationProblem.class */
public interface OptimizationProblem<PAIR> {
    Incrementor getEvaluationCounter();

    Incrementor getIterationCounter();

    ConvergenceChecker<PAIR> getConvergenceChecker();
}
