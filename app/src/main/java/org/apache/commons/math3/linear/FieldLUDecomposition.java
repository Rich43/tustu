package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldLUDecomposition.class */
public class FieldLUDecomposition<T extends FieldElement<T>> {
    private final Field<T> field;
    private T[][] lu;
    private int[] pivot;
    private boolean even;
    private boolean singular;
    private FieldMatrix<T> cachedL;
    private FieldMatrix<T> cachedU;
    private FieldMatrix<T> cachedP;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v79, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r0v90, types: [org.apache.commons.math3.FieldElement] */
    public FieldLUDecomposition(FieldMatrix<T> fieldMatrix) {
        if (!fieldMatrix.isSquare()) {
            throw new NonSquareMatrixException(fieldMatrix.getRowDimension(), fieldMatrix.getColumnDimension());
        }
        int columnDimension = fieldMatrix.getColumnDimension();
        this.field = fieldMatrix.getField();
        this.lu = (T[][]) fieldMatrix.getData();
        this.pivot = new int[columnDimension];
        this.cachedL = null;
        this.cachedU = null;
        this.cachedP = null;
        for (int i2 = 0; i2 < columnDimension; i2++) {
            this.pivot[i2] = i2;
        }
        this.even = true;
        this.singular = false;
        for (int i3 = 0; i3 < columnDimension; i3++) {
            this.field.getZero();
            for (int i4 = 0; i4 < i3; i4++) {
                T[] tArr = this.lu[i4];
                T t2 = tArr[i3];
                for (int i5 = 0; i5 < i4; i5++) {
                    t2 = (FieldElement) t2.subtract(tArr[i5].multiply(this.lu[i5][i3]));
                }
                tArr[i3] = t2;
            }
            int i6 = i3;
            for (int i7 = i3; i7 < columnDimension; i7++) {
                T[] tArr2 = this.lu[i7];
                T t3 = tArr2[i3];
                for (int i8 = 0; i8 < i3; i8++) {
                    t3 = (FieldElement) t3.subtract(tArr2[i8].multiply(this.lu[i8][i3]));
                }
                tArr2[i3] = t3;
                if (this.lu[i6][i3].equals(this.field.getZero())) {
                    i6++;
                }
            }
            if (i6 >= columnDimension) {
                this.singular = true;
                return;
            }
            if (i6 != i3) {
                this.field.getZero();
                for (int i9 = 0; i9 < columnDimension; i9++) {
                    T t4 = this.lu[i6][i9];
                    this.lu[i6][i9] = this.lu[i3][i9];
                    this.lu[i3][i9] = t4;
                }
                int i10 = this.pivot[i6];
                this.pivot[i6] = this.pivot[i3];
                this.pivot[i3] = i10;
                this.even = !this.even;
            }
            T t5 = this.lu[i3][i3];
            for (int i11 = i3 + 1; i11 < columnDimension; i11++) {
                FieldElement[] fieldElementArr = this.lu[i11];
                fieldElementArr[i3] = (FieldElement) fieldElementArr[i3].divide(t5);
            }
        }
    }

    public FieldMatrix<T> getL() throws OutOfRangeException {
        if (this.cachedL == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedL = new Array2DRowFieldMatrix(this.field, m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                T[] luI = this.lu[i2];
                for (int j2 = 0; j2 < i2; j2++) {
                    this.cachedL.setEntry(i2, j2, luI[j2]);
                }
                this.cachedL.setEntry(i2, i2, this.field.getOne());
            }
        }
        return this.cachedL;
    }

    public FieldMatrix<T> getU() throws OutOfRangeException {
        if (this.cachedU == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedU = new Array2DRowFieldMatrix(this.field, m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                T[] luI = this.lu[i2];
                for (int j2 = i2; j2 < m2; j2++) {
                    this.cachedU.setEntry(i2, j2, luI[j2]);
                }
            }
        }
        return this.cachedU;
    }

    public FieldMatrix<T> getP() throws OutOfRangeException {
        if (this.cachedP == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedP = new Array2DRowFieldMatrix(this.field, m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                this.cachedP.setEntry(i2, this.pivot[i2], this.field.getOne());
            }
        }
        return this.cachedP;
    }

    public int[] getPivot() {
        return (int[]) this.pivot.clone();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [org.apache.commons.math3.FieldElement] */
    public T getDeterminant() {
        if (this.singular) {
            return this.field.getZero();
        }
        int length = this.pivot.length;
        T one = this.even ? this.field.getOne() : (T) this.field.getZero().subtract(this.field.getOne());
        for (int i2 = 0; i2 < length; i2++) {
            one = (FieldElement) one.multiply(this.lu[i2][i2]);
        }
        return one;
    }

    public FieldDecompositionSolver<T> getSolver() {
        return new Solver(this.field, this.lu, this.pivot, this.singular);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/FieldLUDecomposition$Solver.class */
    private static class Solver<T extends FieldElement<T>> implements FieldDecompositionSolver<T> {
        private final Field<T> field;
        private final T[][] lu;
        private final int[] pivot;
        private final boolean singular;

        private Solver(Field<T> field, T[][] lu, int[] pivot, boolean singular) {
            this.field = field;
            this.lu = lu;
            this.pivot = pivot;
            this.singular = singular;
        }

        @Override // org.apache.commons.math3.linear.FieldDecompositionSolver
        public boolean isNonSingular() {
            return !this.singular;
        }

        @Override // org.apache.commons.math3.linear.FieldDecompositionSolver
        public FieldVector<T> solve(FieldVector<T> b2) {
            try {
                return solve((ArrayFieldVector) b2);
            } catch (ClassCastException e2) {
                int m2 = this.pivot.length;
                if (b2.getDimension() != m2) {
                    throw new DimensionMismatchException(b2.getDimension(), m2);
                }
                if (this.singular) {
                    throw new SingularMatrixException();
                }
                FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, m2);
                for (int row = 0; row < m2; row++) {
                    fieldElementArr[row] = b2.getEntry(this.pivot[row]);
                }
                for (int col = 0; col < m2; col++) {
                    FieldElement fieldElement = fieldElementArr[col];
                    for (int i2 = col + 1; i2 < m2; i2++) {
                        fieldElementArr[i2] = (FieldElement) fieldElementArr[i2].subtract(fieldElement.multiply(this.lu[i2][col]));
                    }
                }
                for (int col2 = m2 - 1; col2 >= 0; col2--) {
                    fieldElementArr[col2] = (FieldElement) fieldElementArr[col2].divide(this.lu[col2][col2]);
                    FieldElement fieldElement2 = fieldElementArr[col2];
                    for (int i3 = 0; i3 < col2; i3++) {
                        fieldElementArr[i3] = (FieldElement) fieldElementArr[i3].subtract(fieldElement2.multiply(this.lu[i3][col2]));
                    }
                }
                return new ArrayFieldVector((Field) this.field, fieldElementArr, false);
            }
        }

        public ArrayFieldVector<T> solve(ArrayFieldVector<T> b2) {
            int m2 = this.pivot.length;
            int length = b2.getDimension();
            if (length != m2) {
                throw new DimensionMismatchException(length, m2);
            }
            if (this.singular) {
                throw new SingularMatrixException();
            }
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, m2);
            for (int row = 0; row < m2; row++) {
                fieldElementArr[row] = b2.getEntry(this.pivot[row]);
            }
            for (int col = 0; col < m2; col++) {
                FieldElement fieldElement = fieldElementArr[col];
                for (int i2 = col + 1; i2 < m2; i2++) {
                    fieldElementArr[i2] = (FieldElement) fieldElementArr[i2].subtract(fieldElement.multiply(this.lu[i2][col]));
                }
            }
            for (int col2 = m2 - 1; col2 >= 0; col2--) {
                fieldElementArr[col2] = (FieldElement) fieldElementArr[col2].divide(this.lu[col2][col2]);
                FieldElement fieldElement2 = fieldElementArr[col2];
                for (int i3 = 0; i3 < col2; i3++) {
                    fieldElementArr[i3] = (FieldElement) fieldElementArr[i3].subtract(fieldElement2.multiply(this.lu[i3][col2]));
                }
            }
            return new ArrayFieldVector<>(fieldElementArr, false);
        }

        @Override // org.apache.commons.math3.linear.FieldDecompositionSolver
        public FieldMatrix<T> solve(FieldMatrix<T> b2) {
            int m2 = this.pivot.length;
            if (b2.getRowDimension() != m2) {
                throw new DimensionMismatchException(b2.getRowDimension(), m2);
            }
            if (this.singular) {
                throw new SingularMatrixException();
            }
            int nColB = b2.getColumnDimension();
            FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(this.field, m2, nColB);
            for (int row = 0; row < m2; row++) {
                FieldElement[] fieldElementArr2 = fieldElementArr[row];
                int pRow = this.pivot[row];
                for (int col = 0; col < nColB; col++) {
                    fieldElementArr2[col] = b2.getEntry(pRow, col);
                }
            }
            for (int col2 = 0; col2 < m2; col2++) {
                FieldElement[] fieldElementArr3 = fieldElementArr[col2];
                for (int i2 = col2 + 1; i2 < m2; i2++) {
                    FieldElement[] fieldElementArr4 = fieldElementArr[i2];
                    T luICol = this.lu[i2][col2];
                    for (int j2 = 0; j2 < nColB; j2++) {
                        fieldElementArr4[j2] = (FieldElement) fieldElementArr4[j2].subtract(fieldElementArr3[j2].multiply(luICol));
                    }
                }
            }
            for (int col3 = m2 - 1; col3 >= 0; col3--) {
                FieldElement[] fieldElementArr5 = fieldElementArr[col3];
                T luDiag = this.lu[col3][col3];
                for (int j3 = 0; j3 < nColB; j3++) {
                    fieldElementArr5[j3] = (FieldElement) fieldElementArr5[j3].divide(luDiag);
                }
                for (int i3 = 0; i3 < col3; i3++) {
                    FieldElement[] fieldElementArr6 = fieldElementArr[i3];
                    T luICol2 = this.lu[i3][col3];
                    for (int j4 = 0; j4 < nColB; j4++) {
                        fieldElementArr6[j4] = (FieldElement) fieldElementArr6[j4].subtract(fieldElementArr5[j4].multiply(luICol2));
                    }
                }
            }
            return new Array2DRowFieldMatrix((Field) this.field, fieldElementArr, false);
        }

        @Override // org.apache.commons.math3.linear.FieldDecompositionSolver
        public FieldMatrix<T> getInverse() throws OutOfRangeException {
            int m2 = this.pivot.length;
            T one = this.field.getOne();
            FieldMatrix<T> identity = new Array2DRowFieldMatrix<>(this.field, m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                identity.setEntry(i2, i2, one);
            }
            return solve(identity);
        }
    }
}
