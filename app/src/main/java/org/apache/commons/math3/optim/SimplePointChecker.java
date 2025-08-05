package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/SimplePointChecker.class */
public class SimplePointChecker<PAIR extends Pair<double[], ? extends Object>> extends AbstractConvergenceChecker<PAIR> {
    private static final int ITERATION_CHECK_DISABLED = -1;
    private final int maxIterationCount;

    public SimplePointChecker(double relativeThreshold, double absoluteThreshold) {
        super(relativeThreshold, absoluteThreshold);
        this.maxIterationCount = -1;
    }

    public SimplePointChecker(double relativeThreshold, double absoluteThreshold, int maxIter) {
        super(relativeThreshold, absoluteThreshold);
        if (maxIter <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIter));
        }
        this.maxIterationCount = maxIter;
    }

    @Override // org.apache.commons.math3.optim.AbstractConvergenceChecker, org.apache.commons.math3.optim.ConvergenceChecker
    public boolean converged(int iteration, PAIR previous, PAIR current) {
        if (this.maxIterationCount != -1 && iteration >= this.maxIterationCount) {
            return true;
        }
        double[] p2 = (double[]) previous.getKey();
        double[] c2 = (double[]) current.getKey();
        for (int i2 = 0; i2 < p2.length; i2++) {
            double pi = p2[i2];
            double ci = c2[i2];
            double difference = FastMath.abs(pi - ci);
            double size = FastMath.max(FastMath.abs(pi), FastMath.abs(ci));
            if (difference > size * getRelativeThreshold() && difference > getAbsoluteThreshold()) {
                return false;
            }
        }
        return true;
    }
}
