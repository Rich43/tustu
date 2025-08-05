package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldMatrix.class */
public interface FieldMatrix<T extends FieldElement<T>> extends AnyMatrix {
    Field<T> getField();

    FieldMatrix<T> createMatrix(int i2, int i3) throws NotStrictlyPositiveException;

    FieldMatrix<T> copy();

    FieldMatrix<T> add(FieldMatrix<T> fieldMatrix) throws MatrixDimensionMismatchException;

    FieldMatrix<T> subtract(FieldMatrix<T> fieldMatrix) throws MatrixDimensionMismatchException;

    FieldMatrix<T> scalarAdd(T t2);

    FieldMatrix<T> scalarMultiply(T t2);

    FieldMatrix<T> multiply(FieldMatrix<T> fieldMatrix) throws DimensionMismatchException;

    FieldMatrix<T> preMultiply(FieldMatrix<T> fieldMatrix) throws DimensionMismatchException;

    FieldMatrix<T> power(int i2) throws NotPositiveException, NonSquareMatrixException;

    T[][] getData();

    FieldMatrix<T> getSubMatrix(int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    FieldMatrix<T> getSubMatrix(int[] iArr, int[] iArr2) throws OutOfRangeException, NullArgumentException, NoDataException;

    void copySubMatrix(int i2, int i3, int i4, int i5, T[][] tArr) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException;

    void copySubMatrix(int[] iArr, int[] iArr2, T[][] tArr) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, NoDataException;

    void setSubMatrix(T[][] tArr, int i2, int i3) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException;

    FieldMatrix<T> getRowMatrix(int i2) throws OutOfRangeException;

    void setRowMatrix(int i2, FieldMatrix<T> fieldMatrix) throws OutOfRangeException, MatrixDimensionMismatchException;

    FieldMatrix<T> getColumnMatrix(int i2) throws OutOfRangeException;

    void setColumnMatrix(int i2, FieldMatrix<T> fieldMatrix) throws OutOfRangeException, MatrixDimensionMismatchException;

    FieldVector<T> getRowVector(int i2) throws OutOfRangeException;

    void setRowVector(int i2, FieldVector<T> fieldVector) throws OutOfRangeException, MatrixDimensionMismatchException;

    FieldVector<T> getColumnVector(int i2) throws OutOfRangeException;

    void setColumnVector(int i2, FieldVector<T> fieldVector) throws OutOfRangeException, MatrixDimensionMismatchException;

    T[] getRow(int i2) throws OutOfRangeException;

    void setRow(int i2, T[] tArr) throws OutOfRangeException, MatrixDimensionMismatchException;

    T[] getColumn(int i2) throws OutOfRangeException;

    void setColumn(int i2, T[] tArr) throws OutOfRangeException, MatrixDimensionMismatchException;

    T getEntry(int i2, int i3) throws OutOfRangeException;

    void setEntry(int i2, int i3, T t2) throws OutOfRangeException;

    void addToEntry(int i2, int i3, T t2) throws OutOfRangeException;

    void multiplyEntry(int i2, int i3, T t2) throws OutOfRangeException;

    FieldMatrix<T> transpose();

    T getTrace() throws NonSquareMatrixException;

    T[] operate(T[] tArr) throws DimensionMismatchException;

    FieldVector<T> operate(FieldVector<T> fieldVector) throws DimensionMismatchException;

    T[] preMultiply(T[] tArr) throws DimensionMismatchException;

    FieldVector<T> preMultiply(FieldVector<T> fieldVector) throws DimensionMismatchException;

    T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor);

    T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor);

    T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor);

    T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor);

    T walkInColumnOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    T walkInColumnOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor);

    T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor);

    T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;
}
