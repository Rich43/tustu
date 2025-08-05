package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.OptimizationData;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/univariate/SearchInterval.class */
public class SearchInterval implements OptimizationData {
    private final double lower;
    private final double upper;
    private final double start;

    public SearchInterval(double lo, double hi, double init) {
        if (lo >= hi) {
            throw new NumberIsTooLargeException(Double.valueOf(lo), Double.valueOf(hi), false);
        }
        if (init < lo || init > hi) {
            throw new OutOfRangeException(Double.valueOf(init), Double.valueOf(lo), Double.valueOf(hi));
        }
        this.lower = lo;
        this.upper = hi;
        this.start = init;
    }

    public SearchInterval(double lo, double hi) {
        this(lo, hi, 0.5d * (lo + hi));
    }

    public double getMin() {
        return this.lower;
    }

    public double getMax() {
        return this.upper;
    }

    public double getStartValue() {
        return this.start;
    }
}
