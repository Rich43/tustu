package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealMatrix.class */
public interface RealMatrix extends AnyMatrix {
    RealMatrix createMatrix(int i2, int i3) throws NotStrictlyPositiveException;

    RealMatrix copy();

    RealMatrix add(RealMatrix realMatrix) throws MatrixDimensionMismatchException;

    RealMatrix subtract(RealMatrix realMatrix) throws MatrixDimensionMismatchException;

    RealMatrix scalarAdd(double d2);

    RealMatrix scalarMultiply(double d2);

    RealMatrix multiply(RealMatrix realMatrix) throws DimensionMismatchException;

    RealMatrix preMultiply(RealMatrix realMatrix) throws DimensionMismatchException;

    RealMatrix power(int i2) throws NotPositiveException, NonSquareMatrixException;

    double[][] getData();

    double getNorm();

    double getFrobeniusNorm();

    RealMatrix getSubMatrix(int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    RealMatrix getSubMatrix(int[] iArr, int[] iArr2) throws OutOfRangeException, NullArgumentException, NoDataException;

    void copySubMatrix(int i2, int i3, int i4, int i5, double[][] dArr) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException;

    void copySubMatrix(int[] iArr, int[] iArr2, double[][] dArr) throws OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, NoDataException;

    void setSubMatrix(double[][] dArr, int i2, int i3) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException;

    RealMatrix getRowMatrix(int i2) throws OutOfRangeException;

    void setRowMatrix(int i2, RealMatrix realMatrix) throws OutOfRangeException, MatrixDimensionMismatchException;

    RealMatrix getColumnMatrix(int i2) throws OutOfRangeException;

    void setColumnMatrix(int i2, RealMatrix realMatrix) throws OutOfRangeException, MatrixDimensionMismatchException;

    RealVector getRowVector(int i2) throws OutOfRangeException;

    void setRowVector(int i2, RealVector realVector) throws OutOfRangeException, MatrixDimensionMismatchException;

    RealVector getColumnVector(int i2) throws OutOfRangeException;

    void setColumnVector(int i2, RealVector realVector) throws OutOfRangeException, MatrixDimensionMismatchException;

    double[] getRow(int i2) throws OutOfRangeException;

    void setRow(int i2, double[] dArr) throws OutOfRangeException, MatrixDimensionMismatchException;

    double[] getColumn(int i2) throws OutOfRangeException;

    void setColumn(int i2, double[] dArr) throws OutOfRangeException, MatrixDimensionMismatchException;

    double getEntry(int i2, int i3) throws OutOfRangeException;

    void setEntry(int i2, int i3, double d2) throws OutOfRangeException;

    void addToEntry(int i2, int i3, double d2) throws OutOfRangeException;

    void multiplyEntry(int i2, int i3, double d2) throws OutOfRangeException;

    RealMatrix transpose();

    double getTrace() throws NonSquareMatrixException;

    double[] operate(double[] dArr) throws DimensionMismatchException;

    RealVector operate(RealVector realVector) throws DimensionMismatchException;

    double[] preMultiply(double[] dArr) throws DimensionMismatchException;

    RealVector preMultiply(RealVector realVector) throws DimensionMismatchException;

    double walkInRowOrder(RealMatrixChangingVisitor realMatrixChangingVisitor);

    double walkInRowOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor);

    double walkInRowOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    double walkInRowOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    double walkInColumnOrder(RealMatrixChangingVisitor realMatrixChangingVisitor);

    double walkInColumnOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor);

    double walkInColumnOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    double walkInColumnOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    double walkInOptimizedOrder(RealMatrixChangingVisitor realMatrixChangingVisitor);

    double walkInOptimizedOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor);

    double walkInOptimizedOrder(RealMatrixChangingVisitor realMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;

    double walkInOptimizedOrder(RealMatrixPreservingVisitor realMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException;
}
