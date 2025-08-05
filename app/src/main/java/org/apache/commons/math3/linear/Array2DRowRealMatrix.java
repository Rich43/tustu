package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/Array2DRowRealMatrix.class */
public class Array2DRowRealMatrix extends AbstractRealMatrix implements Serializable {
    private static final long serialVersionUID = -1067294169172445528L;
    private double[][] data;

    public Array2DRowRealMatrix() {
    }

    public Array2DRowRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        super(rowDimension, columnDimension);
        this.data = new double[rowDimension][columnDimension];
    }

    public Array2DRowRealMatrix(double[][] d2) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        copyIn(d2);
    }

    public Array2DRowRealMatrix(double[][] d2, boolean copyArray) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        if (copyArray) {
            copyIn(d2);
            return;
        }
        if (d2 == null) {
            throw new NullArgumentException();
        }
        int nRows = d2.length;
        if (nRows == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        int nCols = d2[0].length;
        if (nCols == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        for (int r2 = 1; r2 < nRows; r2++) {
            if (d2[r2].length != nCols) {
                throw new DimensionMismatchException(d2[r2].length, nCols);
            }
        }
        this.data = d2;
    }

    public Array2DRowRealMatrix(double[] v2) {
        int nRows = v2.length;
        this.data = new double[nRows][1];
        for (int row = 0; row < nRows; row++) {
            this.data[row][0] = v2[row];
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new Array2DRowRealMatrix(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix copy() {
        return new Array2DRowRealMatrix(copyOut(), false);
    }

    public Array2DRowRealMatrix add(Array2DRowRealMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        double[][] outData = new double[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            double[] dataRow = this.data[row];
            double[] mRow = m2.data[row];
            double[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col] + mRow[col];
            }
        }
        return new Array2DRowRealMatrix(outData, false);
    }

    public Array2DRowRealMatrix subtract(Array2DRowRealMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        double[][] outData = new double[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            double[] dataRow = this.data[row];
            double[] mRow = m2.data[row];
            double[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = dataRow[col] - mRow[col];
            }
        }
        return new Array2DRowRealMatrix(outData, false);
    }

    public Array2DRowRealMatrix multiply(Array2DRowRealMatrix m2) throws DimensionMismatchException {
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        int nRows = getRowDimension();
        int nCols = m2.getColumnDimension();
        int nSum = getColumnDimension();
        double[][] outData = new double[nRows][nCols];
        double[] mCol = new double[nSum];
        double[][] mData = m2.data;
        for (int col = 0; col < nCols; col++) {
            for (int mRow = 0; mRow < nSum; mRow++) {
                mCol[mRow] = mData[mRow][col];
            }
            for (int row = 0; row < nRows; row++) {
                double[] dataRow = this.data[row];
                double sum = 0.0d;
                for (int i2 = 0; i2 < nSum; i2++) {
                    sum += dataRow[i2] * mCol[i2];
                }
                outData[row][col] = sum;
            }
        }
        return new Array2DRowRealMatrix(outData, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[][] getData() {
        return copyOut();
    }

    public double[][] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        if (this.data == null) {
            if (row > 0) {
                throw new MathIllegalStateException(LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, Integer.valueOf(row));
            }
            if (column > 0) {
                throw new MathIllegalStateException(LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, Integer.valueOf(column));
            }
            MathUtils.checkNotNull(subMatrix);
            int nRows = subMatrix.length;
            if (nRows == 0) {
                throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
            }
            int nCols = subMatrix[0].length;
            if (nCols == 0) {
                throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
            }
            this.data = new double[subMatrix.length][nCols];
            for (int i2 = 0; i2 < this.data.length; i2++) {
                if (subMatrix[i2].length != nCols) {
                    throw new DimensionMismatchException(subMatrix[i2].length, nCols);
                }
                System.arraycopy(subMatrix[i2], 0, this.data[i2 + row], column, nCols);
            }
            return;
        }
        super.setSubMatrix(subMatrix, row, column);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        return this.data[row][column];
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        this.data[row][column] = value;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        double[] dArr = this.data[row];
        dArr[column] = dArr[column] + increment;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        double[] dArr = this.data[row];
        dArr[column] = dArr[column] * factor;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        if (this.data == null || this.data[0] == null) {
            return 0;
        }
        return this.data[0].length;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] operate(double[] v2) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v2.length != nCols) {
            throw new DimensionMismatchException(v2.length, nCols);
        }
        double[] out = new double[nRows];
        for (int row = 0; row < nRows; row++) {
            double[] dataRow = this.data[row];
            double sum = 0.0d;
            for (int i2 = 0; i2 < nCols; i2++) {
                sum += dataRow[i2] * v2[i2];
            }
            out[row] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] preMultiply(double[] v2) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v2.length != nRows) {
            throw new DimensionMismatchException(v2.length, nRows);
        }
        double[] out = new double[nCols];
        for (int col = 0; col < nCols; col++) {
            double sum = 0.0d;
            for (int i2 = 0; i2 < nRows; i2++) {
                sum += this.data[i2][col] * v2[i2];
            }
            out[col] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i2 = 0; i2 < rows; i2++) {
            double[] rowI = this.data[i2];
            for (int j2 = 0; j2 < columns; j2++) {
                rowI[j2] = visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i2 = 0; i2 < rows; i2++) {
            double[] rowI = this.data[i2];
            for (int j2 = 0; j2 < columns; j2++) {
                visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i2 = startRow; i2 <= endRow; i2++) {
            double[] rowI = this.data[i2];
            for (int j2 = startColumn; j2 <= endColumn; j2++) {
                rowI[j2] = visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i2 = startRow; i2 <= endRow; i2++) {
            double[] rowI = this.data[i2];
            for (int j2 = startColumn; j2 <= endColumn; j2++) {
                visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j2 = 0; j2 < columns; j2++) {
            for (int i2 = 0; i2 < rows; i2++) {
                double[] rowI = this.data[i2];
                rowI[j2] = visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j2 = 0; j2 < columns; j2++) {
            for (int i2 = 0; i2 < rows; i2++) {
                visitor.visit(i2, j2, this.data[i2][j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j2 = startColumn; j2 <= endColumn; j2++) {
            for (int i2 = startRow; i2 <= endRow; i2++) {
                double[] rowI = this.data[i2];
                rowI[j2] = visitor.visit(i2, j2, rowI[j2]);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j2 = startColumn; j2 <= endColumn; j2++) {
            for (int i2 = startRow; i2 <= endRow; i2++) {
                visitor.visit(i2, j2, this.data[i2][j2]);
            }
        }
        return visitor.end();
    }

    private double[][] copyOut() {
        int nRows = getRowDimension();
        double[][] out = new double[nRows][getColumnDimension()];
        for (int i2 = 0; i2 < nRows; i2++) {
            System.arraycopy(this.data[i2], 0, out[i2], 0, this.data[i2].length);
        }
        return out;
    }

    private void copyIn(double[][] in) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        setSubMatrix(in, 0, 0);
    }
}
