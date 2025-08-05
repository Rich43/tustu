package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/WeibullDistribution.class */
public class WeibullDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 8589540077390120676L;
    private final double shape;
    private final double scale;
    private final double solverAbsoluteAccuracy;
    private double numericalMean;
    private boolean numericalMeanIsCalculated;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;

    public WeibullDistribution(double alpha, double beta) throws NotStrictlyPositiveException {
        this(alpha, beta, 1.0E-9d);
    }

    public WeibullDistribution(double alpha, double beta, double inverseCumAccuracy) {
        this(new Well19937c(), alpha, beta, inverseCumAccuracy);
    }

    public WeibullDistribution(RandomGenerator rng, double alpha, double beta) throws NotStrictlyPositiveException {
        this(rng, alpha, beta, 1.0E-9d);
    }

    public WeibullDistribution(RandomGenerator rng, double alpha, double beta, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        super(rng);
        this.numericalMean = Double.NaN;
        this.numericalMeanIsCalculated = false;
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (alpha <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, Double.valueOf(alpha));
        }
        if (beta <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(beta));
        }
        this.scale = beta;
        this.shape = alpha;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getShape() {
        return this.shape;
    }

    public double getScale() {
        return this.scale;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        if (x2 < 0.0d) {
            return 0.0d;
        }
        double xscale = x2 / this.scale;
        double xscalepow = FastMath.pow(xscale, this.shape - 1.0d);
        double xscalepowshape = xscalepow * xscale;
        return (this.shape / this.scale) * xscalepow * FastMath.exp(-xscalepowshape);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        if (x2 < 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        double xscale = x2 / this.scale;
        double logxscalepow = FastMath.log(xscale) * (this.shape - 1.0d);
        double xscalepowshape = FastMath.exp(logxscalepow) * xscale;
        return (FastMath.log(this.shape / this.scale) + logxscalepow) - xscalepowshape;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double ret;
        if (x2 <= 0.0d) {
            ret = 0.0d;
        } else {
            ret = 1.0d - FastMath.exp(-FastMath.pow(x2 / this.scale, this.shape));
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) {
        double ret;
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), Double.valueOf(0.0d), Double.valueOf(1.0d));
        }
        if (p2 == 0.0d) {
            ret = 0.0d;
        } else if (p2 == 1.0d) {
            ret = Double.POSITIVE_INFINITY;
        } else {
            ret = this.scale * FastMath.pow(-FastMath.log1p(-p2), 1.0d / this.shape);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    protected double calculateNumericalMean() {
        double sh = getShape();
        double sc = getScale();
        return sc * FastMath.exp(Gamma.logGamma(1.0d + (1.0d / sh)));
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
        double sh = getShape();
        double sc = getScale();
        double mn = getNumericalMean();
        return ((sc * sc) * FastMath.exp(Gamma.logGamma(1.0d + (2.0d / sh)))) - (mn * mn);
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
