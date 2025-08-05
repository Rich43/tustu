package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/Array2DRowFieldMatrix.class */
public class Array2DRowFieldMatrix<T extends FieldElement<T>> extends AbstractFieldMatrix<T> implements Serializable {
    private static final long serialVersionUID = 7260756672015356458L;
    private T[][] data;

    public Array2DRowFieldMatrix(Field<T> field) {
        super(field);
    }

    public Array2DRowFieldMatrix(Field<T> field, int i2, int i3) throws NotStrictlyPositiveException {
        super(field, i2, i3);
        this.data = (T[][]) ((FieldElement[][]) MathArrays.buildArray(field, i2, i3));
    }

    public Array2DRowFieldMatrix(T[][] d2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        this(extractField(d2), d2);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[][] d2) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        super(field);
        copyIn(d2);
    }

    public Array2DRowFieldMatrix(T[][] d2, boolean copyArray) throws NullArgumentException, NoDataException, DimensionMismatchException {
        this(extractField(d2), d2, copyArray);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[][] d2, boolean copyArray) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        super(field);
        if (copyArray) {
            copyIn(d2);
            return;
        }
        MathUtils.checkNotNull(d2);
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
                throw new DimensionMismatchException(nCols, d2[r2].length);
            }
        }
        this.data = d2;
    }

    public Array2DRowFieldMatrix(T[] v2) throws NoDataException {
        this(extractField(v2), v2);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[] tArr) {
        super(field);
        int length = tArr.length;
        this.data = (T[][]) ((FieldElement[][]) MathArrays.buildArray(getField(), length, 1));
        for (int i2 = 0; i2 < length; i2++) {
            this.data[i2][0] = tArr[i2];
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new Array2DRowFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> copy() {
        return new Array2DRowFieldMatrix((Field) getField(), copyOut(), false);
    }

    public Array2DRowFieldMatrix<T> add(Array2DRowFieldMatrix<T> m2) throws MatrixDimensionMismatchException {
        checkAdditionCompatible(m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m2.data[row];
            FieldElement[] fieldElementArr2 = fieldElementArr[row];
            for (int col = 0; col < columnCount; col++) {
                fieldElementArr2[col] = (FieldElement) dataRow[col].add(mRow[col]);
            }
        }
        return new Array2DRowFieldMatrix<>((Field) getField(), fieldElementArr, false);
    }

    public Array2DRowFieldMatrix<T> subtract(Array2DRowFieldMatrix<T> m2) throws MatrixDimensionMismatchException {
        checkSubtractionCompatible(m2);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m2.data[row];
            FieldElement[] fieldElementArr2 = fieldElementArr[row];
            for (int col = 0; col < columnCount; col++) {
                fieldElementArr2[col] = (FieldElement) dataRow[col].subtract(mRow[col]);
            }
        }
        return new Array2DRowFieldMatrix<>((Field) getField(), fieldElementArr, false);
    }

    public Array2DRowFieldMatrix<T> multiply(Array2DRowFieldMatrix<T> m2) throws DimensionMismatchException {
        checkMultiplicationCompatible(m2);
        int nRows = getRowDimension();
        int nCols = m2.getColumnDimension();
        int nSum = getColumnDimension();
        FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(getField(), nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            T[] dataRow = this.data[row];
            FieldElement[] fieldElementArr2 = fieldElementArr[row];
            for (int col = 0; col < nCols; col++) {
                FieldElement zero = getField().getZero();
                for (int i2 = 0; i2 < nSum; i2++) {
                    zero = (FieldElement) zero.add(dataRow[i2].multiply(m2.data[i2][col]));
                }
                fieldElementArr2[col] = zero;
            }
        }
        return new Array2DRowFieldMatrix<>((Field) getField(), fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[][] getData() {
        return (T[][]) copyOut();
    }

    public T[][] getDataRef() {
        return this.data;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setSubMatrix(T[][] tArr, int i2, int i3) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        if (this.data == null) {
            if (i2 > 0) {
                throw new MathIllegalStateException(LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, Integer.valueOf(i2));
            }
            if (i3 > 0) {
                throw new MathIllegalStateException(LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, Integer.valueOf(i3));
            }
            if (tArr.length == 0) {
                throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
            }
            int length = tArr[0].length;
            if (length == 0) {
                throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
            }
            this.data = (T[][]) ((FieldElement[][]) MathArrays.buildArray(getField(), tArr.length, length));
            for (int i4 = 0; i4 < this.data.length; i4++) {
                if (tArr[i4].length != length) {
                    throw new DimensionMismatchException(length, tArr[i4].length);
                }
                System.arraycopy(tArr[i4], 0, this.data[i4 + i2], i3, length);
            }
            return;
        }
        super.setSubMatrix(tArr, i2, i3);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T getEntry(int row, int column) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        return this.data[row][column];
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setEntry(int row, int column, T value) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        this.data[row][column] = value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void addToEntry(int i2, int i3, T t2) throws OutOfRangeException {
        checkRowIndex(i2);
        checkColumnIndex(i3);
        ((T[][]) this.data)[i2][i3] = (FieldElement) this.data[i2][i3].add(t2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void multiplyEntry(int i2, int i3, T t2) throws OutOfRangeException {
        checkRowIndex(i2);
        checkColumnIndex(i3);
        ((T[][]) this.data)[i2][i3] = (FieldElement) this.data[i2][i3].multiply(t2);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        if (this.data == null || this.data[0] == null) {
            return 0;
        }
        return this.data[0].length;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v25, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] operate(T[] tArr) throws DimensionMismatchException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != columnDimension) {
            throw new DimensionMismatchException(tArr.length, columnDimension);
        }
        T[] tArr2 = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), rowDimension));
        for (int i2 = 0; i2 < rowDimension; i2++) {
            T[] tArr3 = this.data[i2];
            T zero = getField().getZero();
            for (int i3 = 0; i3 < columnDimension; i3++) {
                zero = (FieldElement) zero.add(tArr3[i3].multiply(tArr[i3]));
            }
            tArr2[i2] = zero;
        }
        return tArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws DimensionMismatchException {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        if (tArr.length != rowDimension) {
            throw new DimensionMismatchException(tArr.length, rowDimension);
        }
        T[] tArr2 = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), columnDimension));
        for (int i2 = 0; i2 < columnDimension; i2++) {
            T zero = getField().getZero();
            for (int i3 = 0; i3 < rowDimension; i3++) {
                zero = (FieldElement) zero.add(this.data[i3][i2].multiply(tArr[i3]));
            }
            tArr2[i2] = zero;
        }
        return tArr2;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < rowDimension; i2++) {
            Object[] objArr = this.data[i2];
            for (int i3 = 0; i3 < columnDimension; i3++) {
                objArr[i3] = fieldMatrixChangingVisitor.visit(i2, i3, objArr[i3]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < rowDimension; i2++) {
            T[] tArr = this.data[i2];
            for (int i3 = 0; i3 < columnDimension; i3++) {
                fieldMatrixPreservingVisitor.visit(i2, i3, tArr[i3]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i2; i6 <= i3; i6++) {
            Object[] objArr = this.data[i6];
            for (int i7 = i4; i7 <= i5; i7++) {
                objArr[i7] = fieldMatrixChangingVisitor.visit(i6, i7, objArr[i7]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i2; i6 <= i3; i6++) {
            T[] tArr = this.data[i6];
            for (int i7 = i4; i7 <= i5; i7++) {
                fieldMatrixPreservingVisitor.visit(i6, i7, tArr[i7]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixChangingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < columnDimension; i2++) {
            for (int i3 = 0; i3 < rowDimension; i3++) {
                Object[] objArr = this.data[i3];
                objArr[i2] = fieldMatrixChangingVisitor.visit(i3, i2, objArr[i2]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        int rowDimension = getRowDimension();
        int columnDimension = getColumnDimension();
        fieldMatrixPreservingVisitor.start(rowDimension, columnDimension, 0, rowDimension - 1, 0, columnDimension - 1);
        for (int i2 = 0; i2 < columnDimension; i2++) {
            for (int i3 = 0; i3 < rowDimension; i3++) {
                fieldMatrixPreservingVisitor.visit(i3, i2, this.data[i3][i2]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i4; i6 <= i5; i6++) {
            for (int i7 = i2; i7 <= i3; i7++) {
                Object[] objArr = this.data[i7];
                objArr[i6] = fieldMatrixChangingVisitor.visit(i7, i6, objArr[i6]);
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(getRowDimension(), getColumnDimension(), i2, i3, i4, i5);
        for (int i6 = i4; i6 <= i5; i6++) {
            for (int i7 = i2; i7 <= i3; i7++) {
                fieldMatrixPreservingVisitor.visit(i7, i6, this.data[i7][i6]);
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    private T[][] copyOut() {
        int rowDimension = getRowDimension();
        T[][] tArr = (T[][]) ((FieldElement[][]) MathArrays.buildArray(getField(), rowDimension, getColumnDimension()));
        for (int i2 = 0; i2 < rowDimension; i2++) {
            System.arraycopy(this.data[i2], 0, tArr[i2], 0, this.data[i2].length);
        }
        return tArr;
    }

    private void copyIn(T[][] in) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        setSubMatrix(in, 0, 0);
    }
}
