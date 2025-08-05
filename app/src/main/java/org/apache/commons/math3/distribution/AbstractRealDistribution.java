package org.apache.commons.math3.distribution;

import java.io.Serializable;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/AbstractRealDistribution.class */
public abstract class AbstractRealDistribution implements RealDistribution, Serializable {
    public static final double SOLVER_DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private static final long serialVersionUID = -38038050983108802L;

    @Deprecated
    protected RandomDataImpl randomData;
    protected final RandomGenerator random;
    private double solverAbsoluteAccuracy;

    @Deprecated
    protected AbstractRealDistribution() {
        this.randomData = new RandomDataImpl();
        this.solverAbsoluteAccuracy = 1.0E-6d;
        this.random = null;
    }

    protected AbstractRealDistribution(RandomGenerator rng) {
        this.randomData = new RandomDataImpl();
        this.solverAbsoluteAccuracy = 1.0E-6d;
        this.random = rng;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    @Deprecated
    public double cumulativeProbability(double x0, double x1) throws NumberIsTooLargeException {
        return probability(x0, x1);
    }

    public double probability(double x0, double x1) {
        if (x0 > x1) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Double.valueOf(x0), Double.valueOf(x1), true);
        }
        return cumulativeProbability(x1) - cumulativeProbability(x0);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(final double p2) throws OutOfRangeException, NullArgumentException, NoBracketingException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        double lowerBound = getSupportLowerBound();
        if (p2 == 0.0d) {
            return lowerBound;
        }
        double upperBound = getSupportUpperBound();
        if (p2 == 1.0d) {
            return upperBound;
        }
        double mu = getNumericalMean();
        double sig = FastMath.sqrt(getNumericalVariance());
        boolean chebyshevApplies = (Double.isInfinite(mu) || Double.isNaN(mu) || Double.isInfinite(sig) || Double.isNaN(sig)) ? false : true;
        if (lowerBound == Double.NEGATIVE_INFINITY) {
            if (chebyshevApplies) {
                lowerBound = mu - (sig * FastMath.sqrt((1.0d - p2) / p2));
            } else {
                double d2 = -1.0d;
                while (true) {
                    lowerBound = d2;
                    if (cumulativeProbability(lowerBound) < p2) {
                        break;
                    }
                    d2 = lowerBound * 2.0d;
                }
            }
        }
        if (upperBound == Double.POSITIVE_INFINITY) {
            if (chebyshevApplies) {
                upperBound = mu + (sig * FastMath.sqrt(p2 / (1.0d - p2)));
            } else {
                double d3 = 1.0d;
                while (true) {
                    upperBound = d3;
                    if (cumulativeProbability(upperBound) >= p2) {
                        break;
                    }
                    d3 = upperBound * 2.0d;
                }
            }
        }
        UnivariateFunction toSolve = new UnivariateFunction() { // from class: org.apache.commons.math3.distribution.AbstractRealDistribution.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) {
                return AbstractRealDistribution.this.cumulativeProbability(x2) - p2;
            }
        };
        double x2 = UnivariateSolverUtils.solve(toSolve, lowerBound, upperBound, getSolverAbsoluteAccuracy());
        if (!isSupportConnected()) {
            double dx = getSolverAbsoluteAccuracy();
            if (x2 - dx >= getSupportLowerBound()) {
                double px = cumulativeProbability(x2);
                if (cumulativeProbability(x2 - dx) == px) {
                    double upperBound2 = x2;
                    while (upperBound2 - lowerBound > dx) {
                        double midPoint = 0.5d * (lowerBound + upperBound2);
                        if (cumulativeProbability(midPoint) < px) {
                            lowerBound = midPoint;
                        } else {
                            upperBound2 = midPoint;
                        }
                    }
                    return upperBound2;
                }
            }
        }
        return x2;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
        this.randomData.reSeed(seed);
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double sample() {
        return inverseCumulativeProbability(this.random.nextDouble());
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double[] sample(int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        double[] out = new double[sampleSize];
        for (int i2 = 0; i2 < sampleSize; i2++) {
            out[i2] = sample();
        }
        return out;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double probability(double x2) {
        return 0.0d;
    }

    public double logDensity(double x2) {
        return FastMath.log(density(x2));
    }
}
