package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/TDistribution.class */
public class TDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -5852615386664158222L;
    private final double degreesOfFreedom;
    private final double solverAbsoluteAccuracy;
    private final double factor;

    public TDistribution(double degreesOfFreedom) throws NotStrictlyPositiveException {
        this(degreesOfFreedom, 1.0E-9d);
    }

    public TDistribution(double degreesOfFreedom, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        this(new Well19937c(), degreesOfFreedom, inverseCumAccuracy);
    }

    public TDistribution(RandomGenerator rng, double degreesOfFreedom) throws NotStrictlyPositiveException {
        this(rng, degreesOfFreedom, 1.0E-9d);
    }

    public TDistribution(RandomGenerator rng, double degreesOfFreedom, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        super(rng);
        if (degreesOfFreedom <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(degreesOfFreedom));
        }
        this.degreesOfFreedom = degreesOfFreedom;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
        double nPlus1Over2 = (degreesOfFreedom + 1.0d) / 2.0d;
        this.factor = (Gamma.logGamma(nPlus1Over2) - (0.5d * (FastMath.log(3.141592653589793d) + FastMath.log(degreesOfFreedom)))) - Gamma.logGamma(degreesOfFreedom / 2.0d);
    }

    public double getDegreesOfFreedom() {
        return this.degreesOfFreedom;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        return FastMath.exp(logDensity(x2));
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        double n2 = this.degreesOfFreedom;
        double nPlus1Over2 = (n2 + 1.0d) / 2.0d;
        return this.factor - (nPlus1Over2 * FastMath.log(1.0d + ((x2 * x2) / n2)));
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double ret;
        if (x2 == 0.0d) {
            ret = 0.5d;
        } else {
            double t2 = Beta.regularizedBeta(this.degreesOfFreedom / (this.degreesOfFreedom + (x2 * x2)), 0.5d * this.degreesOfFreedom, 0.5d);
            if (x2 < 0.0d) {
                ret = 0.5d * t2;
            } else {
                ret = 1.0d - (0.5d * t2);
            }
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        double df = getDegreesOfFreedom();
        if (df > 1.0d) {
            return 0.0d;
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        double df = getDegreesOfFreedom();
        if (df > 2.0d) {
            return df / (df - 2.0d);
        }
        if (df > 1.0d && df <= 2.0d) {
            return Double.POSITIVE_INFINITY;
        }
        return Double.NaN;
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
