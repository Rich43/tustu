package org.apache.commons.math3.distribution;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/AbstractIntegerDistribution.class */
public abstract class AbstractIntegerDistribution implements IntegerDistribution, Serializable {
    private static final long serialVersionUID = -1146319659338487221L;

    @Deprecated
    protected final RandomDataImpl randomData;
    protected final RandomGenerator random;

    @Deprecated
    protected AbstractIntegerDistribution() {
        this.randomData = new RandomDataImpl();
        this.random = null;
    }

    protected AbstractIntegerDistribution(RandomGenerator rng) {
        this.randomData = new RandomDataImpl();
        this.random = rng;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x0, int x1) throws NumberIsTooLargeException {
        if (x1 < x0) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Integer.valueOf(x0), Integer.valueOf(x1), true);
        }
        return cumulativeProbability(x1) - cumulativeProbability(x0);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        int lower = getSupportLowerBound();
        if (p2 == 0.0d) {
            return lower;
        }
        if (lower != Integer.MIN_VALUE) {
            lower--;
        } else if (checkedCumulativeProbability(lower) >= p2) {
            return lower;
        }
        int upper = getSupportUpperBound();
        if (p2 == 1.0d) {
            return upper;
        }
        double mu = getNumericalMean();
        double sigma = FastMath.sqrt(getNumericalVariance());
        boolean chebyshevApplies = (Double.isInfinite(mu) || Double.isNaN(mu) || Double.isInfinite(sigma) || Double.isNaN(sigma) || sigma == 0.0d) ? false : true;
        if (chebyshevApplies) {
            double k2 = FastMath.sqrt((1.0d - p2) / p2);
            double tmp = mu - (k2 * sigma);
            if (tmp > lower) {
                lower = ((int) FastMath.ceil(tmp)) - 1;
            }
            double tmp2 = mu + ((1.0d / k2) * sigma);
            if (tmp2 < upper) {
                upper = ((int) FastMath.ceil(tmp2)) - 1;
            }
        }
        return solveInverseCumulativeProbability(p2, lower, upper);
    }

    protected int solveInverseCumulativeProbability(double p2, int lower, int upper) throws MathInternalError {
        while (lower + 1 < upper) {
            int xm = (lower + upper) / 2;
            if (xm < lower || xm > upper) {
                xm = lower + ((upper - lower) / 2);
            }
            double pm = checkedCumulativeProbability(xm);
            if (pm >= p2) {
                upper = xm;
            } else {
                lower = xm;
            }
        }
        return upper;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
        this.randomData.reSeed(seed);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int sample() {
        return inverseCumulativeProbability(this.random.nextDouble());
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int[] sample(int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        int[] out = new int[sampleSize];
        for (int i2 = 0; i2 < sampleSize; i2++) {
            out[i2] = sample();
        }
        return out;
    }

    private double checkedCumulativeProbability(int argument) throws MathInternalError {
        double result = cumulativeProbability(argument);
        if (Double.isNaN(result)) {
            throw new MathInternalError(LocalizedFormats.DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN, Integer.valueOf(argument));
        }
        return result;
    }

    public double logProbability(int x2) {
        return FastMath.log(probability(x2));
    }
}
