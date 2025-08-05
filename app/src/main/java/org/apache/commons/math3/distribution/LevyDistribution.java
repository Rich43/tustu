package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/LevyDistribution.class */
public class LevyDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20130314;
    private final double mu;

    /* renamed from: c, reason: collision with root package name */
    private final double f12988c;
    private final double halfC;

    public LevyDistribution(double mu, double c2) {
        this(new Well19937c(), mu, c2);
    }

    public LevyDistribution(RandomGenerator rng, double mu, double c2) {
        super(rng);
        this.mu = mu;
        this.f12988c = c2;
        this.halfC = 0.5d * c2;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        if (x2 < this.mu) {
            return Double.NaN;
        }
        double delta = x2 - this.mu;
        double f2 = this.halfC / delta;
        return (FastMath.sqrt(f2 / 3.141592653589793d) * FastMath.exp(-f2)) / delta;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        if (x2 < this.mu) {
            return Double.NaN;
        }
        double delta = x2 - this.mu;
        double f2 = this.halfC / delta;
        return ((0.5d * FastMath.log(f2 / 3.141592653589793d)) - f2) - FastMath.log(delta);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        if (x2 < this.mu) {
            return Double.NaN;
        }
        return Erf.erfc(FastMath.sqrt(this.halfC / (x2 - this.mu)));
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        double t2 = Erf.erfcInv(p2);
        return this.mu + (this.halfC / (t2 * t2));
    }

    public double getScale() {
        return this.f12988c;
    }

    public double getLocation() {
        return this.mu;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return Double.POSITIVE_INFINITY;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return Double.POSITIVE_INFINITY;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return this.mu;
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
