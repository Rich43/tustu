package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.ResizableDoubleArray;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/ExponentialDistribution.class */
public class ExponentialDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 2401296428283614780L;
    private static final double[] EXPONENTIAL_SA_QI;
    private final double mean;
    private final double logMean;
    private final double solverAbsoluteAccuracy;

    static {
        double LN2 = FastMath.log(2.0d);
        double qi = 0.0d;
        int i2 = 1;
        ResizableDoubleArray ra = new ResizableDoubleArray(20);
        while (qi < 1.0d) {
            qi += FastMath.pow(LN2, i2) / CombinatoricsUtils.factorial(i2);
            ra.addElement(qi);
            i2++;
        }
        EXPONENTIAL_SA_QI = ra.getElements();
    }

    public ExponentialDistribution(double mean) {
        this(mean, 1.0E-9d);
    }

    public ExponentialDistribution(double mean, double inverseCumAccuracy) {
        this(new Well19937c(), mean, inverseCumAccuracy);
    }

    public ExponentialDistribution(RandomGenerator rng, double mean) throws NotStrictlyPositiveException {
        this(rng, mean, 1.0E-9d);
    }

    public ExponentialDistribution(RandomGenerator rng, double mean, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        super(rng);
        if (mean <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(mean));
        }
        this.mean = mean;
        this.logMean = FastMath.log(mean);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        double logDensity = logDensity(x2);
        if (logDensity == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logDensity);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        if (x2 < 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        return ((-x2) / this.mean) - this.logMean;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        double ret;
        if (x2 <= 0.0d) {
            ret = 0.0d;
        } else {
            ret = 1.0d - FastMath.exp((-x2) / this.mean);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        double ret;
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), Double.valueOf(0.0d), Double.valueOf(1.0d));
        }
        if (p2 == 1.0d) {
            ret = Double.POSITIVE_INFINITY;
        } else {
            ret = (-this.mean) * FastMath.log(1.0d - p2);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double sample() {
        double u2;
        double a2 = 0.0d;
        double dNextDouble = this.random.nextDouble();
        while (true) {
            u2 = dNextDouble;
            if (u2 >= 0.5d) {
                break;
            }
            a2 += EXPONENTIAL_SA_QI[0];
            dNextDouble = u2 * 2.0d;
        }
        double u3 = u2 + (u2 - 1.0d);
        if (u3 <= EXPONENTIAL_SA_QI[0]) {
            return this.mean * (a2 + u3);
        }
        int i2 = 0;
        double umin = this.random.nextDouble();
        do {
            i2++;
            double u22 = this.random.nextDouble();
            if (u22 < umin) {
                umin = u22;
            }
        } while (u3 > EXPONENTIAL_SA_QI[i2]);
        return this.mean * (a2 + (umin * EXPONENTIAL_SA_QI[0]));
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return getMean();
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        double m2 = getMean();
        return m2 * m2;
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
