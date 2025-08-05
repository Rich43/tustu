package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/AbstractMultivariateRealDistribution.class */
public abstract class AbstractMultivariateRealDistribution implements MultivariateRealDistribution {
    protected final RandomGenerator random;
    private final int dimension;

    @Override // org.apache.commons.math3.distribution.MultivariateRealDistribution
    public abstract double[] sample();

    protected AbstractMultivariateRealDistribution(RandomGenerator rng, int n2) {
        this.random = rng;
        this.dimension = n2;
    }

    @Override // org.apache.commons.math3.distribution.MultivariateRealDistribution
    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
    }

    @Override // org.apache.commons.math3.distribution.MultivariateRealDistribution
    public int getDimension() {
        return this.dimension;
    }

    @Override // org.apache.commons.math3.distribution.MultivariateRealDistribution
    public double[][] sample(int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        double[][] out = new double[sampleSize][this.dimension];
        for (int i2 = 0; i2 < sampleSize; i2++) {
            out[i2] = sample();
        }
        return out;
    }
}
