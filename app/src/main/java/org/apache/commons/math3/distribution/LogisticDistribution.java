package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/LogisticDistribution.class */
public class LogisticDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20141003;
    private final double mu;

    /* renamed from: s, reason: collision with root package name */
    private final double f12989s;

    public LogisticDistribution(double mu, double s2) {
        this(new Well19937c(), mu, s2);
    }

    public LogisticDistribution(RandomGenerator rng, double mu, double s2) {
        super(rng);
        if (s2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(s2));
        }
        this.mu = mu;
        this.f12989s = s2;
    }

    public double getLocation() {
        return this.mu;
    }

    public double getScale() {
        return this.f12989s;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        double z2 = (x2 - this.mu) / this.f12989s;
        double v2 = FastMath.exp(-z2);
        return ((1.0d / this.f12989s) * v2) / ((1.0d + v2) * (1.0d + v2));
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double z2 = (1.0d / this.f12989s) * (x2 - this.mu);
        return 1.0d / (1.0d + FastMath.exp(-z2));
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), Double.valueOf(0.0d), Double.valueOf(1.0d));
        }
        if (p2 == 0.0d) {
            return 0.0d;
        }
        if (p2 == 1.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return (this.f12989s * Math.log(p2 / (1.0d - p2))) + this.mu;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return this.mu;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return 3.289868133696453d * (1.0d / (this.f12989s * this.f12989s));
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
