package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/OpenMapRealMatrix.class */
public class OpenMapRealMatrix extends AbstractRealMatrix implements SparseRealMatrix, Serializable {
    private static final long serialVersionUID = -5962461716457143437L;
    private final int rows;
    private final int columns;
    private final OpenIntToDoubleHashMap entries;

    public OpenMapRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        super(rowDimension, columnDimension);
        long lRow = rowDimension;
        long lCol = columnDimension;
        if (lRow * lCol >= 2147483647L) {
            throw new NumberIsTooLargeException(Long.valueOf(lRow * lCol), Integer.MAX_VALUE, false);
        }
        this.rows = rowDimension;
        this.columns = columnDimension;
        this.entries = new OpenIntToDoubleHashMap(0.0d);
    }

    public OpenMapRealMatrix(OpenMapRealMatrix matrix) {
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.entries = new OpenIntToDoubleHashMap(matrix.entries);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public OpenMapRealMatrix copy() {
        return new OpenMapRealMatrix(this);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        return new OpenMapRealMatrix(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    public OpenMapRealMatrix add(OpenMapRealMatrix m2) throws OutOfRangeException, MatrixDimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        OpenMapRealMatrix out = new OpenMapRealMatrix(this);
        OpenIntToDoubleHashMap.Iterator iterator = m2.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (row * this.columns);
            out.setEntry(row, col, getEntry(row, col) + iterator.value());
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public OpenMapRealMatrix subtract(RealMatrix m2) throws MatrixDimensionMismatchException {
        try {
            return subtract((OpenMapRealMatrix) m2);
        } catch (ClassCastException e2) {
            return (OpenMapRealMatrix) super.subtract(m2);
        }
    }

    public OpenMapRealMatrix subtract(OpenMapRealMatrix m2) throws OutOfRangeException, MatrixDimensionMismatchException, NoSuchElementException, ConcurrentModificationException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        OpenMapRealMatrix out = new OpenMapRealMatrix(this);
        OpenIntToDoubleHashMap.Iterator iterator = m2.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (row * this.columns);
            out.setEntry(row, col, getEntry(row, col) - iterator.value());
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix multiply(RealMatrix m2) throws OutOfRangeException, DimensionMismatchException, NoSuchElementException, ConcurrentModificationException, NumberIsTooLargeException {
        try {
            return multiply((OpenMapRealMatrix) m2);
        } catch (ClassCastException e2) {
            MatrixUtils.checkMultiplicationCompatible(this, m2);
            int outCols = m2.getColumnDimension();
            BlockRealMatrix out = new BlockRealMatrix(this.rows, outCols);
            OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                double value = iterator.value();
                int key = iterator.key();
                int i2 = key / this.columns;
                int k2 = key % this.columns;
                for (int j2 = 0; j2 < outCols; j2++) {
                    out.addToEntry(i2, j2, value * m2.getEntry(k2, j2));
                }
            }
            return out;
        }
    }

    public OpenMapRealMatrix multiply(OpenMapRealMatrix m2) throws DimensionMismatchException, NoSuchElementException, ConcurrentModificationException, NumberIsTooLargeException {
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        int outCols = m2.getColumnDimension();
        OpenMapRealMatrix out = new OpenMapRealMatrix(this.rows, outCols);
        OpenIntToDoubleHashMap.Iterator iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            double value = iterator.value();
            int key = iterator.key();
            int i2 = key / this.columns;
            int k2 = key % this.columns;
            for (int j2 = 0; j2 < outCols; j2++) {
                int rightKey = m2.computeKey(k2, j2);
                if (m2.entries.containsKey(rightKey)) {
                    int outKey = out.computeKey(i2, j2);
                    double outValue = out.entries.get(outKey) + (value * m2.entries.get(rightKey));
                    if (outValue == 0.0d) {
                        out.entries.remove(outKey);
                    } else {
                        out.entries.put(outKey, outValue);
                    }
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        return this.entries.get(computeKey(row, column));
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        if (value == 0.0d) {
            this.entries.remove(computeKey(row, column));
        } else {
            this.entries.put(computeKey(row, column), value);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) + increment;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) * factor;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    private int computeKey(int row, int column) {
        return (row * this.columns) + column;
    }
}
