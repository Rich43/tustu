package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/CorrelatedRandomVectorGenerator.class */
public class CorrelatedRandomVectorGenerator implements RandomVectorGenerator {
    private final double[] mean;
    private final NormalizedRandomGenerator generator;
    private final double[] normalized;
    private final RealMatrix root;

    public CorrelatedRandomVectorGenerator(double[] mean, RealMatrix covariance, double small, NormalizedRandomGenerator generator) {
        int order = covariance.getRowDimension();
        if (mean.length != order) {
            throw new DimensionMismatchException(mean.length, order);
        }
        this.mean = (double[]) mean.clone();
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(covariance, small);
        this.root = decomposition.getRootMatrix();
        this.generator = generator;
        this.normalized = new double[decomposition.getRank()];
    }

    public CorrelatedRandomVectorGenerator(RealMatrix covariance, double small, NormalizedRandomGenerator generator) {
        int order = covariance.getRowDimension();
        this.mean = new double[order];
        for (int i2 = 0; i2 < order; i2++) {
            this.mean[i2] = 0.0d;
        }
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(covariance, small);
        this.root = decomposition.getRootMatrix();
        this.generator = generator;
        this.normalized = new double[decomposition.getRank()];
    }

    public NormalizedRandomGenerator getGenerator() {
        return this.generator;
    }

    public int getRank() {
        return this.normalized.length;
    }

    public RealMatrix getRootMatrix() {
        return this.root;
    }

    @Override // org.apache.commons.math3.random.RandomVectorGenerator
    public double[] nextVector() {
        for (int i2 = 0; i2 < this.normalized.length; i2++) {
            this.normalized[i2] = this.generator.nextNormalizedDouble();
        }
        double[] correlated = new double[this.mean.length];
        for (int i3 = 0; i3 < correlated.length; i3++) {
            correlated[i3] = this.mean[i3];
            for (int j2 = 0; j2 < this.root.getColumnDimension(); j2++) {
                int i4 = i3;
                correlated[i4] = correlated[i4] + (this.root.getEntry(i3, j2) * this.normalized[j2]);
            }
        }
        return correlated;
    }
}
