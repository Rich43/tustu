package org.apache.commons.math3.linear;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/AbstractRealMatrix.class */
public abstract class AbstractRealMatrix extends RealLinearOperator implements RealMatrix {
    private static final RealMatrixFormat DEFAULT_FORMAT = RealMatrixFormat.getInstance(Locale.US);

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public abstract int getRowDimension();

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public abstract int getColumnDimension();

    @Override // org.apache.commons.math3.linear.RealMatrix
    public abstract RealMatrix createMatrix(int i2, int i3) throws NotStrictlyPositiveException;

    @Override // org.apache.commons.math3.linear.RealMatrix
    public abstract RealMatrix copy();

    @Override // org.apache.commons.math3.linear.RealMatrix
    public abstract double getEntry(int i2, int i3) throws OutOfRangeException;

    @Override // org.apache.commons.math3.linear.RealMatrix
    public abstract void setEntry(int i2, int i3, double d2) throws OutOfRangeException;

    static {
        DEFAULT_FORMAT.getFormat().setMinimumFractionDigits(1);
    }

    protected AbstractRealMatrix() {
    }

    protected AbstractRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        if (rowDimension < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(rowDimension));
        }
        if (columnDimension < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(columnDimension));
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix add(RealMatrix m2) throws OutOfRangeException, NotStrictlyPositiveException, MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) + m2.getEntry(row, col));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix subtract(RealMatrix m2) throws OutOfRangeException, NotStrictlyPositiveException, MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) - m2.getEntry(row, col));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix scalarAdd(double d2) throws OutOfRangeException, NotStrictlyPositiveException {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) + d2);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix scalarMultiply(double d2) throws OutOfRangeException, NotStrictlyPositiveException {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        RealMatrix out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, getEntry(row, col) * d2);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix multiply(RealMatrix m2) throws OutOfRangeException, NotStrictlyPositiveException, DimensionMismatchException {
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        int nRows = getRowDimension();
        int nCols = m2.getColumnDimension();
        int nSum = getColumnDimension();
        RealMatrix out = createMatrix(nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                double sum = 0.0d;
                for (int i2 = 0; i2 < nSum; i2++) {
                    sum += getEntry(row, i2) * m2.getEntry(i2, col);
                }
                out.setEntry(row, col, sum);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix preMultiply(RealMatrix m2) throws DimensionMismatchException {
        return m2.multiply(this);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix power(int p2) throws NotPositiveException, DimensionMismatchException {
        if (p2 < 0) {
            throw new NotPositiveException(LocalizedFormats.NOT_POSITIVE_EXPONENT, Integer.valueOf(p2));
        }
        if (!isSquare()) {
            throw new NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        if (p2 == 0) {
            return MatrixUtils.createRealIdentityMatrix(getRowDimension());
        }
        if (p2 == 1) {
            return copy();
        }
        int power = p2 - 1;
        char[] binaryRepresentation = Integer.toBinaryString(power).toCharArray();
        ArrayList<Integer> nonZeroPositions = new ArrayList<>();
        int maxI = -1;
        for (int i2 = 0; i2 < binaryRepresentation.length; i2++) {
            if (binaryRepresentation[i2] == '1') {
                int pos = (binaryRepresentation.length - i2) - 1;
                nonZeroPositions.add(Integer.valueOf(pos));
                if (maxI == -1) {
                    maxI = pos;
                }
            }
        }
        RealMatrix[] results = new RealMatrix[maxI + 1];
        results[0] = copy();
        for (int i3 = 1; i3 <= maxI; i3++) {
            results[i3] = results[i3 - 1].multiply(results[i3 - 1]);
        }
        RealMatrix result = copy();
        Iterator i$ = nonZeroPositions.iterator();
        while (i$.hasNext()) {
            Integer i4 = i$.next();
            result = result.multiply(results[i4.intValue()]);
        }
        return result;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double[][] getData() {
        double[][] data = new double[getRowDimension()][getColumnDimension()];
        for (int i2 = 0; i2 < data.length; i2++) {
            double[] dataI = data[i2];
            for (int j2 = 0; j2 < dataI.length; j2++) {
                dataI[j2] = getEntry(i2, j2);
            }
        }
        return data;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double getNorm() {
        return walkInColumnOrder(new RealMatrixPreservingVisitor() { // from class: org.apache.commons.math3.linear.AbstractRealMatrix.1
            private double endRow;
            private double columnSum;
            private double maxColSum;

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
                this.endRow = endRow;
                this.columnSum = 0.0d;
                this.maxColSum = 0.0d;
            }

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                this.columnSum += FastMath.abs(value);
                if (row == this.endRow) {
                    this.maxColSum = FastMath.max(this.maxColSum, this.columnSum);
                    this.columnSum = 0.0d;
                }
            }

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public double end() {
                return this.maxColSum;
            }
        });
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double getFrobeniusNorm() {
        return walkInOptimizedOrder(new RealMatrixPreservingVisitor() { // from class: org.apache.commons.math3.linear.AbstractRealMatrix.2
            private double sum;

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
                this.sum = 0.0d;
            }

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                this.sum += value * value;
            }

            @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public double end() {
                return FastMath.sqrt(this.sum);
            }
        });
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        RealMatrix subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i2 = startRow; i2 <= endRow; i2++) {
            for (int j2 = startColumn; j2 <= endColumn; j2++) {
                subMatrix.setEntry(i2 - startRow, j2 - startColumn, getEntry(i2, j2));
            }
        }
        return subMatrix;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws OutOfRangeException, NotStrictlyPositiveException, NullArgumentException, NoDataException {
        MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
        RealMatrix subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
        subMatrix.walkInOptimizedOrder(new DefaultRealMatrixChangingVisitor() { // from class: org.apache.commons.math3.linear.AbstractRealMatrix.3
            @Override // org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor, org.apache.commons.math3.linear.RealMatrixChangingVisitor
            public double visit(int row, int column, double value) {
                return AbstractRealMatrix.this.getEntry(selectedRows[row], selectedColumns[column]);
            }
        });
        return subMatrix;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final double[][] destination) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        int rowsCount = (endRow + 1) - startRow;
        int columnsCount = (endColumn + 1) - startColumn;
        if (destination.length < rowsCount || destination[0].length < columnsCount) {
            throw new MatrixDimensionMismatchException(destination.length, destination[0].length, rowsCount, columnsCount);
        }
        for (int i2 = 1; i2 < rowsCount; i2++) {
            if (destination[i2].length < columnsCount) {
                throw new MatrixDimensionMismatchException(destination.length, destination[i2].length, rowsCount, columnsCount);
            }
        }
        walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math3.linear.AbstractRealMatrix.4
            private int startRow;
            private int startColumn;

            @Override // org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow2, int endRow2, int startColumn2, int endColumn2) {
                this.startRow = startRow2;
                this.startColumn = startColumn2;
            }

            @Override // org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                destination[row - this.startRow][column - this.startColumn] = value;
            }
        }, startRow, endRow, startColumn, endColumn);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, double[][] destination) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, NoDataException {
        MatrixUtils.checkSubMatrixIndex(this, selectedRows, selectedColumns);
        int nCols = selectedColumns.length;
        if (destination.length < selectedRows.length || destination[0].length < nCols) {
            throw new MatrixDimensionMismatchException(destination.length, destination[0].length, selectedRows.length, selectedColumns.length);
        }
        for (int i2 = 0; i2 < selectedRows.length; i2++) {
            double[] destinationI = destination[i2];
            if (destinationI.length < nCols) {
                throw new MatrixDimensionMismatchException(destination.length, destinationI.length, selectedRows.length, selectedColumns.length);
            }
            for (int j2 = 0; j2 < selectedColumns.length; j2++) {
                destinationI[j2] = getEntry(selectedRows[i2], selectedColumns[j2]);
            }
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        MathUtils.checkNotNull(subMatrix);
        int nRows = subMatrix.length;
        if (nRows == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        int nCols = subMatrix[0].length;
        if (nCols == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        for (int r2 = 1; r2 < nRows; r2++) {
            if (subMatrix[r2].length != nCols) {
                throw new DimensionMismatchException(nCols, subMatrix[r2].length);
            }
        }
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        MatrixUtils.checkRowIndex(this, (nRows + row) - 1);
        MatrixUtils.checkColumnIndex(this, (nCols + column) - 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            for (int j2 = 0; j2 < nCols; j2++) {
                setEntry(row + i2, column + j2, subMatrix[i2][j2]);
            }
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix getRowMatrix(int row) throws OutOfRangeException, NotStrictlyPositiveException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        RealMatrix out = createMatrix(1, nCols);
        for (int i2 = 0; i2 < nCols; i2++) {
            out.setEntry(0, i2, getEntry(row, i2));
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setRowMatrix(int row, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, matrix.getEntry(0, i2));
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix getColumnMatrix(int column) throws OutOfRangeException, NotStrictlyPositiveException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        RealMatrix out = createMatrix(nRows, 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            out.setEntry(i2, 0, getEntry(i2, column));
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setColumnMatrix(int column, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, matrix.getEntry(i2, 0));
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealVector getRowVector(int row) throws OutOfRangeException {
        return new ArrayRealVector(getRow(row), false);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setRowVector(int row, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (vector.getDimension() != nCols) {
            throw new MatrixDimensionMismatchException(1, vector.getDimension(), 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, vector.getEntry(i2));
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealVector getColumnVector(int column) throws OutOfRangeException {
        return new ArrayRealVector(getColumn(column), false);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setColumnVector(int column, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (vector.getDimension() != nRows) {
            throw new MatrixDimensionMismatchException(vector.getDimension(), 1, nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, vector.getEntry(i2));
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double[] getRow(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        double[] out = new double[nCols];
        for (int i2 = 0; i2 < nCols; i2++) {
            out[i2] = getEntry(row, i2);
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setRow(int row, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, array[i2]);
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double[] getColumn(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        double[] out = new double[nRows];
        for (int i2 = 0; i2 < nRows; i2++) {
            out[i2] = getEntry(i2, column);
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void setColumn(int column, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, array[i2]);
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        setEntry(row, column, getEntry(row, column) + increment);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        setEntry(row, column, getEntry(row, column) * factor);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealMatrix transpose() throws NotStrictlyPositiveException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        final RealMatrix out = createMatrix(nCols, nRows);
        walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math3.linear.AbstractRealMatrix.5
            @Override // org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) throws OutOfRangeException {
                out.setEntry(column, row, value);
            }
        });
        return out;
    }

    @Override // org.apache.commons.math3.linear.AnyMatrix
    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double getTrace() throws NonSquareMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new NonSquareMatrixException(nRows, nCols);
        }
        double trace = 0.0d;
        for (int i2 = 0; i2 < nRows; i2++) {
            trace += getEntry(i2, i2);
        }
        return trace;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double[] operate(double[] v2) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v2.length != nCols) {
            throw new DimensionMismatchException(v2.length, nCols);
        }
        double[] out = new double[nRows];
        for (int row = 0; row < nRows; row++) {
            double sum = 0.0d;
            for (int i2 = 0; i2 < nCols; i2++) {
                sum += getEntry(row, i2) * v2[i2];
            }
            out[row] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.RealMatrix
    public RealVector operate(RealVector v2) throws DimensionMismatchException {
        try {
            return new ArrayRealVector(operate(((ArrayRealVector) v2).getDataRef()), false);
        } catch (ClassCastException e2) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v2.getDimension() != nCols) {
                throw new DimensionMismatchException(v2.getDimension(), nCols);
            }
            double[] out = new double[nRows];
            for (int row = 0; row < nRows; row++) {
                double sum = 0.0d;
                for (int i2 = 0; i2 < nCols; i2++) {
                    sum += getEntry(row, i2) * v2.getEntry(i2);
                }
                out[row] = sum;
            }
            return new ArrayRealVector(out, false);
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
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
                sum += getEntry(i2, col) * v2[i2];
            }
            out[col] = sum;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public RealVector preMultiply(RealVector v2) throws DimensionMismatchException {
        try {
            return new ArrayRealVector(preMultiply(((ArrayRealVector) v2).getDataRef()), false);
        } catch (ClassCastException e2) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v2.getDimension() != nRows) {
                throw new DimensionMismatchException(v2.getDimension(), nRows);
            }
            double[] out = new double[nCols];
            for (int col = 0; col < nCols; col++) {
                double sum = 0.0d;
                for (int i2 = 0; i2 < nRows; i2++) {
                    sum += getEntry(i2, col) * v2.getEntry(i2);
                }
                out[col] = sum;
            }
            return new ArrayRealVector(out, false);
        }
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor) throws OutOfRangeException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor) throws OutOfRangeException {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                double oldValue = getEntry(row, column);
                double newValue = visitor.visit(row, column, oldValue);
                setEntry(row, column, newValue);
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor) {
        return walkInRowOrder(visitor);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor) {
        return walkInRowOrder(visitor);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override // org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        String fullClassName = getClass().getName();
        String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(46) + 1);
        res.append(shortClassName);
        res.append(DEFAULT_FORMAT.format(this));
        return res.toString();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof RealMatrix)) {
            return false;
        }
        RealMatrix m2 = (RealMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m2.getColumnDimension() != nCols || m2.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (getEntry(row, col) != m2.getEntry(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        int ret = (7 * 31) + nRows;
        int ret2 = (ret * 31) + nCols;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + (((11 * (row + 1)) + (17 * (col + 1))) * MathUtils.hash(getEntry(row, col)));
            }
        }
        return ret2;
    }
}
