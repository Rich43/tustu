package org.apache.commons.math3.stat.correlation;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/Covariance.class */
public class Covariance {
    private final RealMatrix covarianceMatrix;

    /* renamed from: n, reason: collision with root package name */
    private final int f13093n;

    public Covariance() {
        this.covarianceMatrix = null;
        this.f13093n = 0;
    }

    public Covariance(double[][] data, boolean biasCorrected) throws MathIllegalArgumentException {
        this(new BlockRealMatrix(data), biasCorrected);
    }

    public Covariance(double[][] data) throws MathIllegalArgumentException {
        this(data, true);
    }

    public Covariance(RealMatrix matrix, boolean biasCorrected) throws MathIllegalArgumentException {
        checkSufficientData(matrix);
        this.f13093n = matrix.getRowDimension();
        this.covarianceMatrix = computeCovarianceMatrix(matrix, biasCorrected);
    }

    public Covariance(RealMatrix matrix) throws MathIllegalArgumentException {
        this(matrix, true);
    }

    public RealMatrix getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    public int getN() {
        return this.f13093n;
    }

    protected RealMatrix computeCovarianceMatrix(RealMatrix matrix, boolean biasCorrected) throws MathIllegalArgumentException {
        int dimension = matrix.getColumnDimension();
        Variance variance = new Variance(biasCorrected);
        RealMatrix outMatrix = new BlockRealMatrix(dimension, dimension);
        for (int i2 = 0; i2 < dimension; i2++) {
            for (int j2 = 0; j2 < i2; j2++) {
                double cov = covariance(matrix.getColumn(i2), matrix.getColumn(j2), biasCorrected);
                outMatrix.setEntry(i2, j2, cov);
                outMatrix.setEntry(j2, i2, cov);
            }
            outMatrix.setEntry(i2, i2, variance.evaluate(matrix.getColumn(i2)));
        }
        return outMatrix;
    }

    protected RealMatrix computeCovarianceMatrix(RealMatrix matrix) throws MathIllegalArgumentException {
        return computeCovarianceMatrix(matrix, true);
    }

    protected RealMatrix computeCovarianceMatrix(double[][] data, boolean biasCorrected) throws MathIllegalArgumentException {
        return computeCovarianceMatrix(new BlockRealMatrix(data), biasCorrected);
    }

    protected RealMatrix computeCovarianceMatrix(double[][] data) throws MathIllegalArgumentException {
        return computeCovarianceMatrix(data, true);
    }

    public double covariance(double[] xArray, double[] yArray, boolean biasCorrected) throws MathIllegalArgumentException {
        Mean mean = new Mean();
        double result = 0.0d;
        int length = xArray.length;
        if (length != yArray.length) {
            throw new MathIllegalArgumentException(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, Integer.valueOf(length), Integer.valueOf(yArray.length));
        }
        if (length < 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(length), 2);
        }
        double xMean = mean.evaluate(xArray);
        double yMean = mean.evaluate(yArray);
        for (int i2 = 0; i2 < length; i2++) {
            double xDev = xArray[i2] - xMean;
            double yDev = yArray[i2] - yMean;
            result += ((xDev * yDev) - result) / (i2 + 1);
        }
        return biasCorrected ? result * (length / (length - 1)) : result;
    }

    public double covariance(double[] xArray, double[] yArray) throws MathIllegalArgumentException {
        return covariance(xArray, yArray, true);
    }

    private void checkSufficientData(RealMatrix matrix) throws MathIllegalArgumentException {
        int nRows = matrix.getRowDimension();
        int nCols = matrix.getColumnDimension();
        if (nRows < 2 || nCols < 1) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, Integer.valueOf(nRows), Integer.valueOf(nCols));
        }
    }
}
