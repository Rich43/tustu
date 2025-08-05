package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/GeometricDistribution.class */
public class GeometricDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20130507;
    private final double probabilityOfSuccess;
    private final double logProbabilityOfSuccess;
    private final double log1mProbabilityOfSuccess;

    public GeometricDistribution(double p2) {
        this(new Well19937c(), p2);
    }

    public GeometricDistribution(RandomGenerator rng, double p2) {
        super(rng);
        if (p2 <= 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, Double.valueOf(p2), 0, 1);
        }
        this.probabilityOfSuccess = p2;
        this.logProbabilityOfSuccess = FastMath.log(p2);
        this.log1mProbabilityOfSuccess = FastMath.log1p(-p2);
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        if (x2 < 0) {
            return 0.0d;
        }
        return FastMath.exp(this.log1mProbabilityOfSuccess * x2) * this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution
    public double logProbability(int x2) {
        if (x2 < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return (x2 * this.log1mProbabilityOfSuccess) + this.logProbabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        if (x2 < 0) {
            return 0.0d;
        }
        return -FastMath.expm1(this.log1mProbabilityOfSuccess * (x2 + 1));
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        return (1.0d - this.probabilityOfSuccess) / this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        return (1.0d - this.probabilityOfSuccess) / (this.probabilityOfSuccess * this.probabilityOfSuccess);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return 0;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution, org.apache.commons.math3.distribution.IntegerDistribution
    public int inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        if (p2 == 1.0d) {
            return Integer.MAX_VALUE;
        }
        if (p2 == 0.0d) {
            return 0;
        }
        return Math.max(0, (int) Math.ceil((FastMath.log1p(-p2) / this.log1mProbabilityOfSuccess) - 1.0d));
    }
}
