package org.apache.commons.math3.optim;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/AbstractConvergenceChecker.class */
public abstract class AbstractConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
    private final double relativeThreshold;
    private final double absoluteThreshold;

    @Override // org.apache.commons.math3.optim.ConvergenceChecker
    public abstract boolean converged(int i2, PAIR pair, PAIR pair2);

    public AbstractConvergenceChecker(double relativeThreshold, double absoluteThreshold) {
        this.relativeThreshold = relativeThreshold;
        this.absoluteThreshold = absoluteThreshold;
    }

    public double getRelativeThreshold() {
        return this.relativeThreshold;
    }

    public double getAbsoluteThreshold() {
        return this.absoluteThreshold;
    }
}
