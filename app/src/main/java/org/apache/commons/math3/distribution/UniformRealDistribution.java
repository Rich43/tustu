package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/UniformRealDistribution.class */
public class UniformRealDistribution extends AbstractRealDistribution {

    @Deprecated
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 20120109;
    private final double lower;
    private final double upper;

    public UniformRealDistribution() {
        this(0.0d, 1.0d);
    }

    public UniformRealDistribution(double lower, double upper) throws NumberIsTooLargeException {
        this(new Well19937c(), lower, upper);
    }

    @Deprecated
    public UniformRealDistribution(double lower, double upper, double inverseCumAccuracy) throws NumberIsTooLargeException {
        this(new Well19937c(), lower, upper);
    }

    @Deprecated
    public UniformRealDistribution(RandomGenerator rng, double lower, double upper, double inverseCumAccuracy) {
        this(rng, lower, upper);
    }

    public UniformRealDistribution(RandomGenerator rng, double lower, double upper) throws NumberIsTooLargeException {
        super(rng);
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper), false);
        }
        this.lower = lower;
        this.upper = upper;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        if (x2 < this.lower || x2 > this.upper) {
            return 0.0d;
        }
        return 1.0d / (this.upper - this.lower);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        if (x2 <= this.lower) {
            return 0.0d;
        }
        if (x2 >= this.upper) {
            return 1.0d;
        }
        return (x2 - this.lower) / (this.upper - this.lower);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        return (p2 * (this.upper - this.lower)) + this.lower;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return 0.5d * (this.lower + this.upper);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        double ul = this.upper - this.lower;
        return (ul * ul) / 12.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return this.lower;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return this.upper;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double sample() {
        double u2 = this.random.nextDouble();
        return (u2 * this.upper) + ((1.0d - u2) * this.lower);
    }
}
