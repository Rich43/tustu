package org.apache.commons.math3.random;

import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/UncorrelatedRandomVectorGenerator.class */
public class UncorrelatedRandomVectorGenerator implements RandomVectorGenerator {
    private final NormalizedRandomGenerator generator;
    private final double[] mean;
    private final double[] standardDeviation;

    public UncorrelatedRandomVectorGenerator(double[] mean, double[] standardDeviation, NormalizedRandomGenerator generator) {
        if (mean.length != standardDeviation.length) {
            throw new DimensionMismatchException(mean.length, standardDeviation.length);
        }
        this.mean = (double[]) mean.clone();
        this.standardDeviation = (double[]) standardDeviation.clone();
        this.generator = generator;
    }

    public UncorrelatedRandomVectorGenerator(int dimension, NormalizedRandomGenerator generator) {
        this.mean = new double[dimension];
        this.standardDeviation = new double[dimension];
        Arrays.fill(this.standardDeviation, 1.0d);
        this.generator = generator;
    }

    @Override // org.apache.commons.math3.random.RandomVectorGenerator
    public double[] nextVector() {
        double[] random = new double[this.mean.length];
        for (int i2 = 0; i2 < random.length; i2++) {
            random[i2] = this.mean[i2] + (this.standardDeviation[i2] * this.generator.nextNormalizedDouble());
        }
        return random;
    }
}
