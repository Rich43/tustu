package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/MaxIter.class */
public class MaxIter implements OptimizationData {
    private final int maxIter;

    public MaxIter(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(max));
        }
        this.maxIter = max;
    }

    public int getMaxIter() {
        return this.maxIter;
    }

    public static MaxIter unlimited() {
        return new MaxIter(Integer.MAX_VALUE);
    }
}
