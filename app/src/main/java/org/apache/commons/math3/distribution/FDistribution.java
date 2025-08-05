package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/FDistribution.class */
public class FDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -8516354193418641566L;
    private final double numeratorDegreesOfFreedom;
    private final double denominatorDegreesOfFreedom;
    private final double solverAbsoluteAccuracy;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;

    public FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) throws NotStrictlyPositiveException {
        this(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, 1.0E-9d);
    }

    public FDistribution(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        this(new Well19937c(), numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, inverseCumAccuracy);
    }

    public FDistribution(RandomGenerator rng, double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) throws NotStrictlyPositiveException {
        this(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, 1.0E-9d);
    }

    public FDistribution(RandomGenerator rng, double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        super(rng);
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (numeratorDegreesOfFreedom <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(numeratorDegreesOfFreedom));
        }
        if (denominatorDegreesOfFreedom <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(denominatorDegreesOfFreedom));
        }
        this.numeratorDegreesOfFreedom = numeratorDegreesOfFreedom;
        this.denominatorDegreesOfFreedom = denominatorDegreesOfFreedom;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        return FastMath.exp(logDensity(x2));
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        double nhalf = this.numeratorDegreesOfFreedom / 2.0d;
        double mhalf = this.denominatorDegreesOfFreedom / 2.0d;
        double logx = FastMath.log(x2);
        double logn = FastMath.log(this.numeratorDegreesOfFreedom);
        double logm = FastMath.log(this.denominatorDegreesOfFreedom);
        double lognxm = FastMath.log((this.numeratorDegreesOfFreedom * x2) + this.denominatorDegreesOfFreedom);
        return ((((((nhalf * logn) + (nhalf * logx)) - logx) + (mhalf * logm)) - (nhalf * lognxm)) - (mhalf * lognxm)) - Beta.logBeta(nhalf, mhalf);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double ret;
        if (x2 <= 0.0d) {
            ret = 0.0d;
        } else {
            double n2 = this.numeratorDegreesOfFreedom;
            double m2 = this.denominatorDegreesOfFreedom;
            ret = Beta.regularizedBeta((n2 * x2) / (m2 + (n2 * x2)), 0.5d * n2, 0.5d * m2);
        }
        return ret;
    }

    public double getNumeratorDegreesOfFreedom() {
        return this.numeratorDegreesOfFreedom;
    }

    public double getDenominatorDegreesOfFreedom() {
        return this.denominatorDegreesOfFreedom;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 2.0d) {
            return denominatorDF / (denominatorDF - 2.0d);
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double denominatorDF = getDenominatorDegreesOfFreedom();
        if (denominatorDF > 4.0d) {
            double numeratorDF = getNumeratorDegreesOfFreedom();
            double denomDFMinusTwo = denominatorDF - 2.0d;
            return ((2.0d * (denominatorDF * denominatorDF)) * ((numeratorDF + denominatorDF) - 2.0d)) / ((numeratorDF * (denomDFMinusTwo * denomDFMinusTwo)) * (denominatorDF - 4.0d));
        }
        return Double.NaN;
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
