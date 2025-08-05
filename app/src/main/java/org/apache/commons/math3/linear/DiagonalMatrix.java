package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/DiagonalMatrix.class */
public class DiagonalMatrix extends AbstractRealMatrix implements Serializable {
    private static final long serialVersionUID = 20121229;
    private final double[] data;

    public DiagonalMatrix(int dimension) throws NotStrictlyPositiveException {
        super(dimension, dimension);
        this.data = new double[dimension];
    }

    public DiagonalMatrix(double[] d2) {
        this(d2, true);
    }

    public DiagonalMatrix(double[] d2, boolean copyArray) throws NullArgumentException {
        MathUtils.checkNotNull(d2);
        this.data = copyArray ? (double[]) d2.clone() : d2;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, DimensionMismatchException {
        if (rowDimension != columnDimension) {
            throw new DimensionMismatchException(rowDimension, columnDimension);
        }
        return new DiagonalMatrix(rowDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix copy() {
        return new DiagonalMatrix(this.data);
    }

    public DiagonalMatrix add(DiagonalMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            outData[i2] = this.data[i2] + m2.data[i2];
        }
        return new DiagonalMatrix(outData, false);
    }

    public DiagonalMatrix subtract(DiagonalMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m2);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            outData[i2] = this.data[i2] - m2.data[i2];
        }
        return new DiagonalMatrix(outData, false);
    }

    public DiagonalMatrix multiply(DiagonalMatrix m2) throws DimensionMismatchException {
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            outData[i2] = this.data[i2] * m2.data[i2];
        }
        return new DiagonalMatrix(outData, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix multiply(RealMatrix m2) throws DimensionMismatchException {
        if (m2 instanceof DiagonalMatrix) {
            return multiply((DiagonalMatrix) m2);
        }
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        int nRows = m2.getRowDimension();
        int nCols = m2.getColumnDimension();
        double[][] product = new double[nRows][nCols];
        for (int r2 = 0; r2 < nRows; r2++) {
            for (int c2 = 0; c2 < nCols; c2++) {
                product[r2][c2] = this.data[r2] * m2.getEntry(r2, c2);
            }
        }
        return new Array2DRowRealMatrix(product, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[][] getData() {
        int dim = getRowDimension();
        double[][] out = new double[dim][dim];
        for (int i2 = 0; i2 < dim; i2++) {
            out[i2][i2] = this.data[i2];
        }
        return out;
    }

    public double[] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        if (row == column) {
            return this.data[row];
        }
        return 0.0d;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            this.data[row] = value;
        } else {
            ensureZero(value);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            double[] dArr = this.data;
            dArr[row] = dArr[row] + increment;
            return;
        }
        ensureZero(increment);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            double[] dArr = this.data;
            dArr[row] = dArr[row] * factor;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] operate(double[] v2) throws DimensionMismatchException {
        return multiply(new DiagonalMatrix(v2, false)).getDataRef();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] preMultiply(double[] v2) throws DimensionMismatchException {
        return operate(v2);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealVector preMultiply(RealVector v2) throws DimensionMismatchException {
        double[] vectorData;
        if (v2 instanceof ArrayRealVector) {
            vectorData = ((ArrayRealVector) v2).getDataRef();
        } else {
            vectorData = v2.toArray();
        }
        return MatrixUtils.createRealVector(preMultiply(vectorData));
    }

    private void ensureZero(double value) throws NumberIsTooLargeException {
        if (!Precision.equals(0.0d, value, 1)) {
            throw new NumberIsTooLargeException(Double.valueOf(FastMath.abs(value)), 0, true);
        }
    }

    public DiagonalMatrix inverse() throws SingularMatrixException {
        return inverse(0.0d);
    }

    public DiagonalMatrix inverse(double threshold) throws SingularMatrixException {
        if (isSingular(threshold)) {
            throw new SingularMatrixException();
        }
        double[] result = new double[this.data.length];
        for (int i2 = 0; i2 < this.data.length; i2++) {
            result[i2] = 1.0d / this.data[i2];
        }
        return new DiagonalMatrix(result, false);
    }

    public boolean isSingular(double threshold) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            if (Precision.equals(this.data[i2], 0.0d, threshold)) {
                return true;
            }
        }
        return false;
    }
}
