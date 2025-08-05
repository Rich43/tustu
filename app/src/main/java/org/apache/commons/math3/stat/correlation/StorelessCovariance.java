package org.apache.commons.math3.stat.correlation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/StorelessCovariance.class */
public class StorelessCovariance extends Covariance {
    private StorelessBivariateCovariance[] covMatrix;
    private int dimension;

    public StorelessCovariance(int dim) {
        this(dim, true);
    }

    public StorelessCovariance(int dim, boolean biasCorrected) {
        this.dimension = dim;
        this.covMatrix = new StorelessBivariateCovariance[(this.dimension * (this.dimension + 1)) / 2];
        initializeMatrix(biasCorrected);
    }

    private void initializeMatrix(boolean biasCorrected) {
        for (int i2 = 0; i2 < this.dimension; i2++) {
            for (int j2 = 0; j2 < this.dimension; j2++) {
                setElement(i2, j2, new StorelessBivariateCovariance(biasCorrected));
            }
        }
    }

    private int indexOf(int i2, int j2) {
        return j2 < i2 ? ((i2 * (i2 + 1)) / 2) + j2 : ((j2 * (j2 + 1)) / 2) + i2;
    }

    private StorelessBivariateCovariance getElement(int i2, int j2) {
        return this.covMatrix[indexOf(i2, j2)];
    }

    private void setElement(int i2, int j2, StorelessBivariateCovariance cov) {
        this.covMatrix[indexOf(i2, j2)] = cov;
    }

    public double getCovariance(int xIndex, int yIndex) throws NumberIsTooSmallException {
        return getElement(xIndex, yIndex).getResult();
    }

    public void increment(double[] data) throws DimensionMismatchException {
        int length = data.length;
        if (length != this.dimension) {
            throw new DimensionMismatchException(length, this.dimension);
        }
        for (int i2 = 0; i2 < length; i2++) {
            for (int j2 = i2; j2 < length; j2++) {
                getElement(i2, j2).increment(data[i2], data[j2]);
            }
        }
    }

    public void append(StorelessCovariance sc) throws DimensionMismatchException {
        if (sc.dimension != this.dimension) {
            throw new DimensionMismatchException(sc.dimension, this.dimension);
        }
        for (int i2 = 0; i2 < this.dimension; i2++) {
            for (int j2 = i2; j2 < this.dimension; j2++) {
                getElement(i2, j2).append(sc.getElement(i2, j2));
            }
        }
    }

    @Override // org.apache.commons.math3.stat.correlation.Covariance
    public RealMatrix getCovarianceMatrix() throws NumberIsTooSmallException {
        return MatrixUtils.createRealMatrix(getData());
    }

    public double[][] getData() throws NumberIsTooSmallException {
        double[][] data = new double[this.dimension][this.dimension];
        for (int i2 = 0; i2 < this.dimension; i2++) {
            for (int j2 = 0; j2 < this.dimension; j2++) {
                data[i2][j2] = getElement(i2, j2).getResult();
            }
        }
        return data;
    }

    @Override // org.apache.commons.math3.stat.correlation.Covariance
    public int getN() throws MathUnsupportedOperationException {
        throw new MathUnsupportedOperationException();
    }
}
