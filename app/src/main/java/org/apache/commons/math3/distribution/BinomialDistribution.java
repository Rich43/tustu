package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/BinomialDistribution.class */
public class BinomialDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 6751309484392813623L;
    private final int numberOfTrials;
    private final double probabilityOfSuccess;

    public BinomialDistribution(int trials, double p2) {
        this(new Well19937c(), trials, p2);
    }

    public BinomialDistribution(RandomGenerator rng, int trials, double p2) {
        super(rng);
        if (trials < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_TRIALS, Integer.valueOf(trials));
        }
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        this.probabilityOfSuccess = p2;
        this.numberOfTrials = trials;
    }

    public int getNumberOfTrials() {
        return this.numberOfTrials;
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        double logProbability = logProbability(x2);
        if (logProbability == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logProbability);
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution
    public double logProbability(int x2) {
        double ret;
        if (this.numberOfTrials == 0) {
            return x2 == 0 ? 0.0d : Double.NEGATIVE_INFINITY;
        }
        if (x2 < 0 || x2 > this.numberOfTrials) {
            ret = Double.NEGATIVE_INFINITY;
        } else {
            ret = SaddlePointExpansion.logBinomialProbability(x2, this.numberOfTrials, this.probabilityOfSuccess, 1.0d - this.probabilityOfSuccess);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        double ret;
        if (x2 < 0) {
            ret = 0.0d;
        } else if (x2 >= this.numberOfTrials) {
            ret = 1.0d;
        } else {
            ret = 1.0d - Beta.regularizedBeta(this.probabilityOfSuccess, x2 + 1.0d, this.numberOfTrials - x2);
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        return this.numberOfTrials * this.probabilityOfSuccess;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        double p2 = this.probabilityOfSuccess;
        return this.numberOfTrials * p2 * (1.0d - p2);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        if (this.probabilityOfSuccess < 1.0d) {
            return 0;
        }
        return this.numberOfTrials;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        if (this.probabilityOfSuccess > 0.0d) {
            return this.numberOfTrials;
        }
        return 0;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }
}
