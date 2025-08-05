package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/LaplaceDistribution.class */
public class LaplaceDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20141003;
    private final double mu;
    private final double beta;

    public LaplaceDistribution(double mu, double beta) {
        this(new Well19937c(), mu, beta);
    }

    public LaplaceDistribution(RandomGenerator rng, double mu, double beta) {
        super(rng);
        if (beta <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(beta));
        }
        this.mu = mu;
        this.beta = beta;
    }

    public double getLocation() {
        return this.mu;
    }

    public double getScale() {
        return this.beta;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        return FastMath.exp((-FastMath.abs(x2 - this.mu)) / this.beta) / (2.0d * this.beta);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        if (x2 <= this.mu) {
            return FastMath.exp((x2 - this.mu) / this.beta) / 2.0d;
        }
        return 1.0d - (FastMath.exp((this.mu - x2) / this.beta) / 2.0d);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), Double.valueOf(0.0d), Double.valueOf(1.0d));
        }
        if (p2 == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (p2 == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        double x2 = p2 > 0.5d ? -Math.log(2.0d - (2.0d * p2)) : Math.log(2.0d * p2);
        return this.mu + (this.beta * x2);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return this.mu;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return 2.0d * this.beta * this.beta;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportConnected() {
        return true;
    }
}
