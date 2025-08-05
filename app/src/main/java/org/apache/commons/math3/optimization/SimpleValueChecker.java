package org.apache.commons.math3.optimization;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/SimpleValueChecker.class */
public class SimpleValueChecker extends AbstractConvergenceChecker<PointValuePair> {
    private static final int ITERATION_CHECK_DISABLED = -1;
    private final int maxIterationCount;

    @Deprecated
    public SimpleValueChecker() {
        this.maxIterationCount = -1;
    }

    public SimpleValueChecker(double relativeThreshold, double absoluteThreshold) {
        super(relativeThreshold, absoluteThreshold);
        this.maxIterationCount = -1;
    }

    public SimpleValueChecker(double relativeThreshold, double absoluteThreshold, int maxIter) {
        super(relativeThreshold, absoluteThreshold);
        if (maxIter <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIter));
        }
        this.maxIterationCount = maxIter;
    }

    @Override // org.apache.commons.math3.optimization.AbstractConvergenceChecker, org.apache.commons.math3.optimization.ConvergenceChecker
    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        if (this.maxIterationCount != -1 && iteration >= this.maxIterationCount) {
            return true;
        }
        double p2 = previous.getValue().doubleValue();
        double c2 = current.getValue().doubleValue();
        double difference = FastMath.abs(p2 - c2);
        double size = FastMath.max(FastMath.abs(p2), FastMath.abs(c2));
        return difference <= size * getRelativeThreshold() || difference <= getAbsoluteThreshold();
    }
}
