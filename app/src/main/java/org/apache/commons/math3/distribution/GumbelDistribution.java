package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/GumbelDistribution.class */
public class GumbelDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20141003;
    private static final double EULER = 0.5778636748954609d;
    private final double mu;
    private final double beta;

    public GumbelDistribution(double mu, double beta) {
        this(new Well19937c(), mu, beta);
    }

    public GumbelDistribution(RandomGenerator rng, double mu, double beta) {
        super(rng);
        if (beta <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(beta));
        }
        this.beta = beta;
        this.mu = mu;
    }

    public double getLocation() {
        return this.mu;
    }

    public double getScale() {
        return this.beta;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        double z2 = (x2 - this.mu) / this.beta;
        double t2 = FastMath.exp(-z2);
        return FastMath.exp((-z2) - t2) / this.beta;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double z2 = (x2 - this.mu) / this.beta;
        return FastMath.exp(-FastMath.exp(-z2));
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
        return this.mu - (FastMath.log(-FastMath.log(p2)) * this.beta);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return this.mu + (EULER * this.beta);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return 1.6449340668482264d * this.beta * this.beta;
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
