package org.apache.commons.math3.optimization;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/BaseOptimizer.class */
public interface BaseOptimizer<PAIR> {
    int getMaxEvaluations();

    int getEvaluations();

    ConvergenceChecker<PAIR> getConvergenceChecker();
}
