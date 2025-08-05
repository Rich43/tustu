package org.apache.commons.math3.optimization;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/ConvergenceChecker.class */
public interface ConvergenceChecker<PAIR> {
    boolean converged(int i2, PAIR pair, PAIR pair2);
}
