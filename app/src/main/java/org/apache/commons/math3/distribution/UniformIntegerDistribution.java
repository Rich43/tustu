package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/UniformIntegerDistribution.class */
public class UniformIntegerDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20120109;
    private final int lower;
    private final int upper;

    public UniformIntegerDistribution(int lower, int upper) throws NumberIsTooLargeException {
        this(new Well19937c(), lower, upper);
    }

    public UniformIntegerDistribution(RandomGenerator rng, int lower, int upper) throws NumberIsTooLargeException {
        super(rng);
        if (lower > upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Integer.valueOf(lower), Integer.valueOf(upper), true);
        }
        this.lower = lower;
        this.upper = upper;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        if (x2 < this.lower || x2 > this.upper) {
            return 0.0d;
        }
        return 1.0d / ((this.upper - this.lower) + 1);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        if (x2 < this.lower) {
            return 0.0d;
        }
        if (x2 > this.upper) {
            return 1.0d;
        }
        return ((x2 - this.lower) + 1.0d) / ((this.upper - this.lower) + 1.0d);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        return 0.5d * (this.lower + this.upper);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        double n2 = (this.upper - this.lower) + 1;
        return ((n2 * n2) - 1.0d) / 12.0d;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return this.lower;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return this.upper;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution, org.apache.commons.math3.distribution.IntegerDistribution
    public int sample() {
        int max = (this.upper - this.lower) + 1;
        if (max > 0) {
            return this.lower + this.random.nextInt(max);
        }
        while (true) {
            int r2 = this.random.nextInt();
            if (r2 >= this.lower && r2 <= this.upper) {
                return r2;
            }
        }
    }
}
