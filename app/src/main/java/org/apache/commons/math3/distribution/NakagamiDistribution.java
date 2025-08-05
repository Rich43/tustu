package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/NakagamiDistribution.class */
public class NakagamiDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 20141003;
    private final double mu;
    private final double omega;
    private final double inverseAbsoluteAccuracy;

    public NakagamiDistribution(double mu, double omega) {
        this(mu, omega, 1.0E-9d);
    }

    public NakagamiDistribution(double mu, double omega, double inverseAbsoluteAccuracy) {
        this(new Well19937c(), mu, omega, inverseAbsoluteAccuracy);
    }

    public NakagamiDistribution(RandomGenerator rng, double mu, double omega, double inverseAbsoluteAccuracy) {
        super(rng);
        if (mu < 0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(mu), Double.valueOf(0.5d), true);
        }
        if (omega <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(omega));
        }
        this.mu = mu;
        this.omega = omega;
        this.inverseAbsoluteAccuracy = inverseAbsoluteAccuracy;
    }

    public double getShape() {
        return this.mu;
    }

    public double getScale() {
        return this.omega;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.inverseAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        if (x2 <= 0.0d) {
            return 0.0d;
        }
        return ((2.0d * FastMath.pow(this.mu, this.mu)) / (Gamma.gamma(this.mu) * FastMath.pow(this.omega, this.mu))) * FastMath.pow(x2, (2.0d * this.mu) - 1.0d) * FastMath.exp((((-this.mu) * x2) * x2) / this.omega);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        return Gamma.regularizedGammaP(this.mu, ((this.mu * x2) * x2) / this.omega);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return (Gamma.gamma(this.mu + 0.5d) / Gamma.gamma(this.mu)) * FastMath.sqrt(this.omega / this.mu);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        double v2 = Gamma.gamma(this.mu + 0.5d) / Gamma.gamma(this.mu);
        return this.omega * (1.0d - (((1.0d / this.mu) * v2) * v2));
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return 0.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportLowerBoundInclusive() {
        return true;
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
