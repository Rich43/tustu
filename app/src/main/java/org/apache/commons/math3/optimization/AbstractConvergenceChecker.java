package org.apache.commons.math3.optimization;

import org.apache.commons.math3.util.Precision;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/AbstractConvergenceChecker.class */
public abstract class AbstractConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {

    @Deprecated
    private static final double DEFAULT_RELATIVE_THRESHOLD = 100.0d * Precision.EPSILON;

    @Deprecated
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 100.0d * Precision.SAFE_MIN;
    private final double relativeThreshold;
    private final double absoluteThreshold;

    @Override // org.apache.commons.math3.optimization.ConvergenceChecker
    public abstract boolean converged(int i2, PAIR pair, PAIR pair2);

    @Deprecated
    public AbstractConvergenceChecker() {
        this.relativeThreshold = DEFAULT_RELATIVE_THRESHOLD;
        this.absoluteThreshold = DEFAULT_ABSOLUTE_THRESHOLD;
    }

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
