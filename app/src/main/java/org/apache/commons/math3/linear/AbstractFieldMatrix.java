package org.apache.commons.math3.linear;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/AbstractFieldMatrix.class */
public abstract class AbstractFieldMatrix<T extends FieldElement<T>> implements FieldMatrix<T> {
    private final Field<T> field;

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract FieldMatrix<T> createMatrix(int i2, int i3) throws NotStrictlyPositiveException;

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract FieldMatrix<T> copy();

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract T getEntry(int i2, int i3) throws OutOfRangeException;

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract void setEntry(int i2, int i3, T t2) throws OutOfRangeException;

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract void addToEntry(int i2, int i3, T t2) throws OutOfRangeException;

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public abstract void multiplyEntry(int i2, int i3, T t2) throws OutOfRangeException;

    @Override // org.apache.commons.math3.linear.AnyMatrix
    public abstract int getRowDimension();

    @Override // org.apache.commons.math3.linear.AnyMatrix
    public abstract int getColumnDimension();

    protected AbstractFieldMatrix() {
        this.field = null;
    }

    protected AbstractFieldMatrix(Field<T> field) {
        this.field = field;
    }

    protected AbstractFieldMatrix(Field<T> field, int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        if (rowDimension <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(rowDimension));
        }
        if (columnDimension <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(columnDimension));
        }
        this.field = field;
    }

    protected static <T extends FieldElement<T>> Field<T> extractField(T[][] d2) throws NullArgumentException, NoDataException {
        if (d2 == null) {
            throw new NullArgumentException();
        }
        if (d2.length == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        if (d2[0].length == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        return d2[0][0].getField();
    }

    protected static <T extends FieldElement<T>> Field<T> extractField(T[] d2) throws NoDataException {
        if (d2.length == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        return d2[0].getField();
    }

    @Deprecated
    protected static <T extends FieldElement<T>> T[][] buildArray(Field<T> field, int i2, int i3) {
        return (T[][]) ((FieldElement[][]) MathArrays.buildArray(field, i2, i3));
    }

    @Deprecated
    protected static <T extends FieldElement<T>> T[] buildArray(Field<T> field, int i2) {
        return (T[]) ((FieldElement[]) MathArrays.buildArray(field, i2));
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> add(FieldMatrix<T> m2) throws OutOfRangeException, NotStrictlyPositiveException, MatrixDimensionMismatchException {
        checkAdditionCompatible(m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).add(m2.getEntry(row, col)));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> subtract(FieldMatrix<T> m2) throws OutOfRangeException, NotStrictlyPositiveException, MatrixDimensionMismatchException {
        checkSubtractionCompatible(m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).subtract(m2.getEntry(row, col)));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> scalarAdd(T d2) throws OutOfRangeException, NotStrictlyPositiveException {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).add(d2));
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> scalarMultiply(T d2) throws OutOfRangeException, NotStrictlyPositiveException {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).multiply(d2));
            }
        }
        return out;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> multiply(FieldMatrix<T> m2) throws OutOfRangeException, NotStrictlyPositiveException, DimensionMismatchException {
        checkMultiplicationCompatible(m2);
        int nRows = getRowDimension();
        int nCols = m2.getColumnDimension();
        int nSum = getColumnDimension();
        FieldMatrix<T> out = createMatrix(nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                T sum = this.field.getZero();
                for (int i2 = 0; i2 < nSum; i2++) {
                    sum = (FieldElement) sum.add(getEntry(row, i2).multiply(m2.getEntry(i2, col)));
                }
                out.setEntry(row, col, sum);
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> preMultiply(FieldMatrix<T> m2) throws DimensionMismatchException {
        return m2.multiply(this);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> power(int p2) throws NotPositiveException, DimensionMismatchException {
        if (p2 < 0) {
            throw new NotPositiveException(Integer.valueOf(p2));
        }
        if (!isSquare()) {
            throw new NonSquareMatrixException(getRowDimension(), getColumnDimension());
        }
        if (p2 == 0) {
            return MatrixUtils.createFieldIdentityMatrix(getField(), getRowDimension());
        }
        if (p2 == 1) {
            return copy();
        }
        int power = p2 - 1;
        char[] binaryRepresentation = Integer.toBinaryString(power).toCharArray();
        ArrayList<Integer> nonZeroPositions = new ArrayList<>();
        for (int i2 = 0; i2 < binaryRepresentation.length; i2++) {
            if (binaryRepresentation[i2] == '1') {
                int pos = (binaryRepresentation.length - i2) - 1;
                nonZeroPositions.add(Integer.valueOf(pos));
            }
        }
        ArrayList<FieldMatrix<T>> results = new ArrayList<>(binaryRepresentation.length);
        results.add(0, copy());
        for (int i3 = 1; i3 < binaryRepresentation.length; i3++) {
            FieldMatrix<T> s2 = results.get(i3 - 1);
            FieldMatrix<T> r2 = s2.multiply(s2);
            results.add(i3, r2);
        }
        FieldMatrix<T> result = copy();
        Iterator i$ = nonZeroPositions.iterator();
        while (i$.hasNext()) {
            Integer i4 = i$.next();
            result = result.multiply(results.get(i4.intValue()));
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T[][] getData() {
        T[][] tArr = (T[][]) ((FieldElement[][]) MathArrays.buildArray(this.field, getRowDimension(), getColumnDimension()));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            FieldElement[] fieldElementArr = tArr[i2];
            for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                fieldElementArr[i3] = getEntry(i2, i3);
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        FieldMatrix<T> subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i2 = startRow; i2 <= endRow; i2++) {
            for (int j2 = startColumn; j2 <= endColumn; j2++) {
                subMatrix.setEntry(i2 - startRow, j2 - startColumn, getEntry(i2, j2));
            }
        }
        return subMatrix;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws OutOfRangeException, NotStrictlyPositiveException, NullArgumentException, NoDataException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        FieldMatrix<T> subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
        subMatrix.walkInOptimizedOrder(new DefaultFieldMatrixChangingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math3.linear.AbstractFieldMatrix.1
            @Override // org.apache.commons.math3.linear.DefaultFieldMatrixChangingVisitor, org.apache.commons.math3.linear.FieldMatrixChangingVisitor
            public T visit(int i2, int i3, T t2) {
                return (T) AbstractFieldMatrix.this.getEntry(selectedRows[i2], selectedColumns[i3]);
            }
        });
        return subMatrix;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final T[][] destination) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        int rowsCount = (endRow + 1) - startRow;
        int columnsCount = (endColumn + 1) - startColumn;
        if (destination.length < rowsCount || destination[0].length < columnsCount) {
            throw new MatrixDimensionMismatchException(destination.length, destination[0].length, rowsCount, columnsCount);
        }
        walkInOptimizedOrder(new DefaultFieldMatrixPreservingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math3.linear.AbstractFieldMatrix.2
            private int startRow;
            private int startColumn;

            @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
            public void start(int rows, int columns, int startRow2, int endRow2, int startColumn2, int endColumn2) {
                this.startRow = startRow2;
                this.startColumn = startColumn2;
            }

            @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
            public void visit(int row, int column, T value) {
                destination[row - this.startRow][column - this.startColumn] = value;
            }
        }, startRow, endRow, startColumn, endColumn);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, T[][] tArr) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, NoDataException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        if (tArr.length < selectedRows.length || tArr[0].length < selectedColumns.length) {
            throw new MatrixDimensionMismatchException(tArr.length, tArr[0].length, selectedRows.length, selectedColumns.length);
        }
        for (int i2 = 0; i2 < selectedRows.length; i2++) {
            FieldElement[] fieldElementArr = tArr[i2];
            for (int j2 = 0; j2 < selectedColumns.length; j2++) {
                fieldElementArr[j2] = getEntry(selectedRows[i2], selectedColumns[j2]);
            }
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setSubMatrix(T[][] subMatrix, int row, int column) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        if (subMatrix == null) {
            throw new NullArgumentException();
        }
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
        checkRowIndex(row);
        checkColumnIndex(column);
        checkRowIndex((nRows + row) - 1);
        checkColumnIndex((nCols + column) - 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            for (int j2 = 0; j2 < nCols; j2++) {
                setEntry(row + i2, column + j2, subMatrix[i2][j2]);
            }
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getRowMatrix(int row) throws OutOfRangeException, NotStrictlyPositiveException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        FieldMatrix<T> out = createMatrix(1, nCols);
        for (int i2 = 0; i2 < nCols; i2++) {
            out.setEntry(0, i2, getEntry(row, i2));
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setRowMatrix(int row, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, matrix.getEntry(0, i2));
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getColumnMatrix(int column) throws OutOfRangeException, NotStrictlyPositiveException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        FieldMatrix<T> out = createMatrix(nRows, 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            out.setEntry(i2, 0, getEntry(i2, column));
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setColumnMatrix(int column, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, matrix.getEntry(i2, 0));
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> getRowVector(int row) throws OutOfRangeException {
        return new ArrayFieldVector((Field) this.field, getRow(row), false);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setRowVector(int row, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (vector.getDimension() != nCols) {
            throw new MatrixDimensionMismatchException(1, vector.getDimension(), 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, vector.getEntry(i2));
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> getColumnVector(int column) throws OutOfRangeException {
        return new ArrayFieldVector((Field) this.field, getColumn(column), false);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setColumnVector(int column, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (vector.getDimension() != nRows) {
            throw new MatrixDimensionMismatchException(vector.getDimension(), 1, nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, vector.getEntry(i2));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T[] getRow(int i2) throws OutOfRangeException {
        checkRowIndex(i2);
        int columnDimension = getColumnDimension();
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, columnDimension));
        for (int i3 = 0; i3 < columnDimension; i3++) {
            tArr[i3] = getEntry(i2, i3);
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setRow(int row, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        for (int i2 = 0; i2 < nCols; i2++) {
            setEntry(row, i2, array[i2]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T[] getColumn(int i2) throws OutOfRangeException {
        checkColumnIndex(i2);
        int rowDimension = getRowDimension();
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, rowDimension));
        for (int i3 = 0; i3 < rowDimension; i3++) {
            tArr[i3] = getEntry(i3, i2);
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public void setColumn(int column, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        for (int i2 = 0; i2 < nRows; i2++) {
            setEntry(i2, column, array[i2]);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> transpose() throws NotStrictlyPositiveException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        final FieldMatrix<T> out = createMatrix(nCols, nRows);
        walkInOptimizedOrder(new DefaultFieldMatrixPreservingVisitor<T>(this.field.getZero()) { // from class: org.apache.commons.math3.linear.AbstractFieldMatrix.3
            @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
            public void visit(int row, int column, T value) throws OutOfRangeException {
                out.setEntry(column, row, value);
            }
        });
        return out;
    }

    @Override // org.apache.commons.math3.linear.AnyMatrix
    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T getTrace() throws NonSquareMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new NonSquareMatrixException(nRows, nCols);
        }
        T trace = this.field.getZero();
        for (int i2 = 0; i2 < nRows; i2++) {
            trace = (FieldElement) trace.add(getEntry(i2, i2));
        }
        return trace;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T[] operate(T[] tArr) throws DimensionMismatchException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != columnDimension) {
            throw new DimensionMismatchException(tArr.length, columnDimension);
        }
        T[] tArr2 = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, rowDimension));
        for (int i2 = 0; i2 < rowDimension; i2++) {
            T zero = this.field.getZero();
            for (int i3 = 0; i3 < columnDimension; i3++) {
                zero = (FieldElement) zero.add(getEntry(i2, i3).multiply(tArr[i3]));
            }
            tArr2[i2] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> operate(FieldVector<T> v2) throws DimensionMismatchException {
        try {
            return new ArrayFieldVector((Field) this.field, operate(((ArrayFieldVector) v2).getDataRef()), false);
        } catch (ClassCastException e2) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v2.getDimension() != nCols) {
                throw new DimensionMismatchException(v2.getDimension(), nCols);
            }
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, nRows);
            for (int row = 0; row < nRows; row++) {
                FieldElement zero = this.field.getZero();
                for (int i2 = 0; i2 < nCols; i2++) {
                    zero = (FieldElement) zero.add(getEntry(row, i2).multiply(v2.getEntry(i2)));
                }
                fieldElementArr[row] = zero;
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws DimensionMismatchException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != rowDimension) {
            throw new DimensionMismatchException(tArr.length, rowDimension);
        }
        T[] tArr2 = (T[]) ((FieldElement[]) MathArrays.buildArray(this.field, columnDimension));
        for (int i2 = 0; i2 < columnDimension; i2++) {
            T zero = this.field.getZero();
            for (int i3 = 0; i3 < rowDimension; i3++) {
                zero = (FieldElement) zero.add(getEntry(i3, i2).multiply(tArr[i3]));
            }
            tArr2[i2] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> preMultiply(FieldVector<T> v2) throws DimensionMismatchException {
        try {
            return new ArrayFieldVector((Field) this.field, preMultiply(((ArrayFieldVector) v2).getDataRef()), false);
        } catch (ClassCastException e2) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v2.getDimension() != nRows) {
                throw new DimensionMismatchException(v2.getDimension(), nRows);
            }
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, nCols);
            for (int col = 0; col < nCols; col++) {
                FieldElement zero = this.field.getZero();
                for (int i2 = 0; i2 < nRows; i2++) {
                    zero = (FieldElement) zero.add(getEntry(i2, col).multiply(v2.getEntry(i2)));
                }
                fieldElementArr[col] = zero;
            }
            return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
        }
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws OutOfRangeException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < rowDimension; i2++) {
            for (int i3 = 0; i3 < columnDimension; i3++) {
                setEntry(i2, i3, fieldMatrixChangingVisitor.visit(i2, i3, getEntry(i2, i3)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < rowDimension; i2++) {
            for (int i3 = 0; i3 < columnDimension; i3++) {
                fieldMatrixPreservingVisitor.visit(i2, i3, getEntry(i2, i3));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i2; i6 <= i3; i6++) {
            for (int i7 = i4; i7 <= i5; i7++) {
                setEntry(i6, i7, fieldMatrixChangingVisitor.visit(i6, i7, getEntry(i6, i7)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i2; i6 <= i3; i6++) {
            for (int i7 = i4; i7 <= i5; i7++) {
                fieldMatrixPreservingVisitor.visit(i6, i7, getEntry(i6, i7));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) throws OutOfRangeException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < columnDimension; i2++) {
            for (int i3 = 0; i3 < rowDimension; i3++) {
                setEntry(i3, i2, fieldMatrixChangingVisitor.visit(i3, i2, getEntry(i3, i2)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < columnDimension; i2++) {
            for (int i3 = 0; i3 < rowDimension; i3++) {
                fieldMatrixPreservingVisitor.visit(i3, i2, getEntry(i3, i2));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i4; i6 <= i5; i6++) {
            for (int i7 = i2; i7 <= i3; i7++) {
                setEntry(i7, i6, fieldMatrixChangingVisitor.visit(i7, i6, getEntry(i7, i6)));
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i4; i6 <= i5; i6++) {
            for (int i7 = i2; i7 <= i3; i7++) {
                fieldMatrixPreservingVisitor.visit(i7, i6, getEntry(i7, i6));
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) {
        return (T) walkInRowOrder(fieldMatrixChangingVisitor);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        return (T) walkInRowOrder(fieldMatrixPreservingVisitor);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        return (T) walkInRowOrder(fieldMatrixChangingVisitor, i2, i3, i4, i5);
    }

    @Override // org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        return (T) walkInRowOrder(fieldMatrixPreservingVisitor, i2, i3, i4, i5);
    }

    public String toString() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        StringBuffer res = new StringBuffer();
        String fullClassName = getClass().getName();
        String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(46) + 1);
        res.append(shortClassName).append(VectorFormat.DEFAULT_PREFIX);
        for (int i2 = 0; i2 < nRows; i2++) {
            if (i2 > 0) {
                res.append(",");
            }
            res.append(VectorFormat.DEFAULT_PREFIX);
            for (int j2 = 0; j2 < nCols; j2++) {
                if (j2 > 0) {
                    res.append(",");
                }
                res.append((Object) getEntry(i2, j2));
            }
            res.append("}");
        }
        res.append("}");
        return res.toString();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof FieldMatrix)) {
            return false;
        }
        FieldMatrix<?> m2 = (FieldMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m2.getColumnDimension() != nCols || m2.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (!getEntry(row, col).equals(m2.getEntry(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        int ret = (322562 * 31) + nRows;
        int ret2 = (ret * 31) + nCols;
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + (((11 * (row + 1)) + (17 * (col + 1))) * getEntry(row, col).hashCode());
            }
        }
        return ret2;
    }

    protected void checkRowIndex(int row) throws OutOfRangeException {
        if (row < 0 || row >= getRowDimension()) {
            throw new OutOfRangeException(LocalizedFormats.ROW_INDEX, Integer.valueOf(row), 0, Integer.valueOf(getRowDimension() - 1));
        }
    }

    protected void checkColumnIndex(int column) throws OutOfRangeException {
        if (column < 0 || column >= getColumnDimension()) {
            throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX, Integer.valueOf(column), 0, Integer.valueOf(getColumnDimension() - 1));
        }
    }

    protected void checkSubMatrixIndex(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkRowIndex(startRow);
        checkRowIndex(endRow);
        if (endRow < startRow) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(endRow), Integer.valueOf(startRow), true);
        }
        checkColumnIndex(startColumn);
        checkColumnIndex(endColumn);
        if (endColumn < startColumn) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, Integer.valueOf(endColumn), Integer.valueOf(startColumn), true);
        }
    }

    protected void checkSubMatrixIndex(int[] selectedRows, int[] selectedColumns) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (selectedRows == null || selectedColumns == null) {
            throw new NullArgumentException();
        }
        if (selectedRows.length == 0 || selectedColumns.length == 0) {
            throw new NoDataException();
        }
        for (int row : selectedRows) {
            checkRowIndex(row);
        }
        for (int column : selectedColumns) {
            checkColumnIndex(column);
        }
    }

    protected void checkAdditionCompatible(FieldMatrix<T> m2) throws MatrixDimensionMismatchException {
        if (getRowDimension() != m2.getRowDimension() || getColumnDimension() != m2.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(m2.getRowDimension(), m2.getColumnDimension(), getRowDimension(), getColumnDimension());
        }
    }

    protected void checkSubtractionCompatible(FieldMatrix<T> m2) throws MatrixDimensionMismatchException {
        if (getRowDimension() != m2.getRowDimension() || getColumnDimension() != m2.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(m2.getRowDimension(), m2.getColumnDimension(), getRowDimension(), getColumnDimension());
        }
    }

    protected void checkMultiplicationCompatible(FieldMatrix<T> m2) throws DimensionMismatchException {
        if (getColumnDimension() != m2.getRowDimension()) {
            throw new DimensionMismatchException(m2.getRowDimension(), getColumnDimension());
        }
    }
}
