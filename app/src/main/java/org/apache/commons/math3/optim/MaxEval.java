package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/MaxEval.class */
public class MaxEval implements OptimizationData {
    private final int maxEval;

    public MaxEval(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(max));
        }
        this.maxEval = max;
    }

    public int getMaxEval() {
        return this.maxEval;
    }

    public static MaxEval unlimited() {
        return new MaxEval(Integer.MAX_VALUE);
    }
}
