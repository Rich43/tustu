package org.apache.commons.math3.optim;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/ConvergenceChecker.class */
public interface ConvergenceChecker<PAIR> {
    boolean converged(int i2, PAIR pair, PAIR pair2);
}
