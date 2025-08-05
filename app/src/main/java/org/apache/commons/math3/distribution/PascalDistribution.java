package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/PascalDistribution.class */
public class PascalDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 6751309484392813623L;
    private final int numberOfSuccesses;
    private final double probabilityOfSuccess;
    private final double logProbabilityOfSuccess;
    private final double log1mProbabilityOfSuccess;

    public PascalDistribution(int r2, double p2) throws OutOfRangeException, NotStrictlyPositiveException {
        this(new Well19937c(), r2, p2);
    }

    public PascalDistribution(RandomGenerator rng, int r2, double p2) throws OutOfRangeException, NotStrictlyPositiveException {
        super(rng);
        if (r2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SUCCESSES, Integer.valueOf(r2));
        }
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        this.numberOfSuccesses = r2;
        this.probabilityOfSuccess = p2;
        this.logProbabilityOfSuccess = FastMath.log(p2);
        this.log1mProbabilityOfSuccess = FastMath.log1p(-p2);
    }

    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        double ret;
        if (x2 < 0) {
            ret = 0.0d;
        } else {
            ret = CombinatoricsUtils.binomialCoefficientDouble((x2 + this.numberOfSuccesses) - 1, this.numberOfSuccesses - 1) * FastMath.pow(this.probabilityOfSuccess, this.numberOfSuccesses) * FastMath.pow(1.0d - this.probabilityOfSuccess, x2);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution
    public double logProbability(int x2) {
        double ret;
        if (x2 < 0) {
            ret = Double.NEGATIVE_INFINITY;
        } else {
            ret = CombinatoricsUtils.binomialCoefficientLog((x2 + this.numberOfSuccesses) - 1, this.numberOfSuccesses - 1) + (this.logProbabilityOfSuccess * this.numberOfSuccesses) + (this.log1mProbabilityOfSuccess * x2);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        double ret;
        if (x2 < 0) {
            ret = 0.0d;
        } else {
            ret = Beta.regularizedBeta(this.probabilityOfSuccess, this.numberOfSuccesses, x2 + 1.0d);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        double p2 = getProbabilityOfSuccess();
        double r2 = getNumberOfSuccesses();
        return (r2 * (1.0d - p2)) / p2;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        double p2 = getProbabilityOfSuccess();
        double r2 = getNumberOfSuccesses();
        return (r2 * (1.0d - p2)) / (p2 * p2);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return 0;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }
}
