package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/HypergeometricDistribution.class */
public class HypergeometricDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = -436928820673516179L;
    private final int numberOfSuccesses;
    private final int populationSize;
    private final int sampleSize;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;

    public HypergeometricDistribution(int populationSize, int numberOfSuccesses, int sampleSize) throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        this(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
    }

    public HypergeometricDistribution(RandomGenerator rng, int populationSize, int numberOfSuccesses, int sampleSize) throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        super(rng);
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (populationSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.POPULATION_SIZE, Integer.valueOf(populationSize));
        }
        if (numberOfSuccesses < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_SUCCESSES, Integer.valueOf(numberOfSuccesses));
        }
        if (sampleSize < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        if (numberOfSuccesses > populationSize) {
            throw new NumberIsTooLargeException(LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, Integer.valueOf(numberOfSuccesses), Integer.valueOf(populationSize), true);
        }
        if (sampleSize > populationSize) {
            throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, Integer.valueOf(sampleSize), Integer.valueOf(populationSize), true);
        }
        this.numberOfSuccesses = numberOfSuccesses;
        this.populationSize = populationSize;
        this.sampleSize = sampleSize;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        double ret;
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x2 < domain[0]) {
            ret = 0.0d;
        } else if (x2 >= domain[1]) {
            ret = 1.0d;
        } else {
            ret = innerCumulativeProbability(domain[0], x2, 1);
        }
        return ret;
    }

    private int[] getDomain(int n2, int m2, int k2) {
        return new int[]{getLowerDomain(n2, m2, k2), getUpperDomain(m2, k2)};
    }

    private int getLowerDomain(int n2, int m2, int k2) {
        return FastMath.max(0, m2 - (n2 - k2));
    }

    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    public int getPopulationSize() {
        return this.populationSize;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    private int getUpperDomain(int m2, int k2) {
        return FastMath.min(k2, m2);
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
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x2 < domain[0] || x2 > domain[1]) {
            ret = Double.NEGATIVE_INFINITY;
        } else {
            double p2 = this.sampleSize / this.populationSize;
            double q2 = (this.populationSize - this.sampleSize) / this.populationSize;
            double p1 = SaddlePointExpansion.logBinomialProbability(x2, this.numberOfSuccesses, p2, q2);
            double p22 = SaddlePointExpansion.logBinomialProbability(this.sampleSize - x2, this.populationSize - this.numberOfSuccesses, p2, q2);
            double p3 = SaddlePointExpansion.logBinomialProbability(this.sampleSize, this.populationSize, p2, q2);
            ret = (p1 + p22) - p3;
        }
        return ret;
    }

    public double upperCumulativeProbability(int x2) {
        double ret;
        int[] domain = getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (x2 <= domain[0]) {
            ret = 1.0d;
        } else if (x2 > domain[1]) {
            ret = 0.0d;
        } else {
            ret = innerCumulativeProbability(domain[1], x2, -1);
        }
        return ret;
    }

    private double innerCumulativeProbability(int x0, int x1, int dx) {
        double dProbability = probability(x0);
        while (true) {
            double ret = dProbability;
            if (x0 != x1) {
                x0 += dx;
                dProbability = ret + probability(x0);
            } else {
                return ret;
            }
        }
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        return getSampleSize() * (getNumberOfSuccesses() / getPopulationSize());
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double N2 = getPopulationSize();
        double m2 = getNumberOfSuccesses();
        double n2 = getSampleSize();
        return (((n2 * m2) * (N2 - n2)) * (N2 - m2)) / ((N2 * N2) * (N2 - 1.0d));
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return FastMath.max(0, (getSampleSize() + getNumberOfSuccesses()) - getPopulationSize());
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return FastMath.min(getNumberOfSuccesses(), getSampleSize());
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }
}
