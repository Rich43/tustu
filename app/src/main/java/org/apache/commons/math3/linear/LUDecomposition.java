package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/LUDecomposition.class */
public class LUDecomposition {
    private static final double DEFAULT_TOO_SMALL = 1.0E-11d;
    private final double[][] lu;
    private final int[] pivot;
    private boolean even;
    private boolean singular;
    private RealMatrix cachedL;
    private RealMatrix cachedU;
    private RealMatrix cachedP;

    public LUDecomposition(RealMatrix matrix) {
        this(matrix, DEFAULT_TOO_SMALL);
    }

    public LUDecomposition(RealMatrix matrix, double singularityThreshold) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m2 = matrix.getColumnDimension();
        this.lu = matrix.getData();
        this.pivot = new int[m2];
        this.cachedL = null;
        this.cachedU = null;
        this.cachedP = null;
        for (int row = 0; row < m2; row++) {
            this.pivot[row] = row;
        }
        this.even = true;
        this.singular = false;
        for (int col = 0; col < m2; col++) {
            for (int row2 = 0; row2 < col; row2++) {
                double[] luRow = this.lu[row2];
                double sum = luRow[col];
                for (int i2 = 0; i2 < row2; i2++) {
                    sum -= luRow[i2] * this.lu[i2][col];
                }
                luRow[col] = sum;
            }
            int max = col;
            double largest = Double.NEGATIVE_INFINITY;
            for (int row3 = col; row3 < m2; row3++) {
                double[] luRow2 = this.lu[row3];
                double sum2 = luRow2[col];
                for (int i3 = 0; i3 < col; i3++) {
                    sum2 -= luRow2[i3] * this.lu[i3][col];
                }
                luRow2[col] = sum2;
                if (FastMath.abs(sum2) > largest) {
                    largest = FastMath.abs(sum2);
                    max = row3;
                }
            }
            if (FastMath.abs(this.lu[max][col]) < singularityThreshold) {
                this.singular = true;
                return;
            }
            if (max != col) {
                double[] luMax = this.lu[max];
                double[] luCol = this.lu[col];
                for (int i4 = 0; i4 < m2; i4++) {
                    double tmp = luMax[i4];
                    luMax[i4] = luCol[i4];
                    luCol[i4] = tmp;
                }
                int temp = this.pivot[max];
                this.pivot[max] = this.pivot[col];
                this.pivot[col] = temp;
                this.even = !this.even;
            }
            double luDiag = this.lu[col][col];
            for (int row4 = col + 1; row4 < m2; row4++) {
                double[] dArr = this.lu[row4];
                int i5 = col;
                dArr[i5] = dArr[i5] / luDiag;
            }
        }
    }

    public RealMatrix getL() throws OutOfRangeException {
        if (this.cachedL == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedL = MatrixUtils.createRealMatrix(m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                double[] luI = this.lu[i2];
                for (int j2 = 0; j2 < i2; j2++) {
                    this.cachedL.setEntry(i2, j2, luI[j2]);
                }
                this.cachedL.setEntry(i2, i2, 1.0d);
            }
        }
        return this.cachedL;
    }

    public RealMatrix getU() throws OutOfRangeException {
        if (this.cachedU == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedU = MatrixUtils.createRealMatrix(m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                double[] luI = this.lu[i2];
                for (int j2 = i2; j2 < m2; j2++) {
                    this.cachedU.setEntry(i2, j2, luI[j2]);
                }
            }
        }
        return this.cachedU;
    }

    public RealMatrix getP() throws OutOfRangeException {
        if (this.cachedP == null && !this.singular) {
            int m2 = this.pivot.length;
            this.cachedP = MatrixUtils.createRealMatrix(m2, m2);
            for (int i2 = 0; i2 < m2; i2++) {
                this.cachedP.setEntry(i2, this.pivot[i2], 1.0d);
            }
        }
        return this.cachedP;
    }

    public int[] getPivot() {
        return (int[]) this.pivot.clone();
    }

    public double getDeterminant() {
        if (this.singular) {
            return 0.0d;
        }
        int m2 = this.pivot.length;
        double determinant = this.even ? 1.0d : -1.0d;
        for (int i2 = 0; i2 < m2; i2++) {
            determinant *= this.lu[i2][i2];
        }
        return determinant;
    }

    public DecompositionSolver getSolver() {
        return new Solver(this.lu, this.pivot, this.singular);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/LUDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private final double[][] lu;
        private final int[] pivot;
        private final boolean singular;

        private Solver(double[][] lu, int[] pivot, boolean singular) {
            this.lu = lu;
            this.pivot = pivot;
            this.singular = singular;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            return !this.singular;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            int m2 = this.pivot.length;
            if (b2.getDimension() != m2) {
                throw new DimensionMismatchException(b2.getDimension(), m2);
            }
            if (this.singular) {
                throw new SingularMatrixException();
            }
            double[] bp2 = new double[m2];
            for (int row = 0; row < m2; row++) {
                bp2[row] = b2.getEntry(this.pivot[row]);
            }
            for (int col = 0; col < m2; col++) {
                double bpCol = bp2[col];
                for (int i2 = col + 1; i2 < m2; i2++) {
                    int i3 = i2;
                    bp2[i3] = bp2[i3] - (bpCol * this.lu[i2][col]);
                }
            }
            for (int col2 = m2 - 1; col2 >= 0; col2--) {
                int i4 = col2;
                bp2[i4] = bp2[i4] / this.lu[col2][col2];
                double bpCol2 = bp2[col2];
                for (int i5 = 0; i5 < col2; i5++) {
                    int i6 = i5;
                    bp2[i6] = bp2[i6] - (bpCol2 * this.lu[i5][col2]);
                }
            }
            return new ArrayRealVector(bp2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) {
            int m2 = this.pivot.length;
            if (b2.getRowDimension() != m2) {
                throw new DimensionMismatchException(b2.getRowDimension(), m2);
            }
            if (this.singular) {
                throw new SingularMatrixException();
            }
            int nColB = b2.getColumnDimension();
            double[][] bp2 = new double[m2][nColB];
            for (int row = 0; row < m2; row++) {
                double[] bpRow = bp2[row];
                int pRow = this.pivot[row];
                for (int col = 0; col < nColB; col++) {
                    bpRow[col] = b2.getEntry(pRow, col);
                }
            }
            for (int col2 = 0; col2 < m2; col2++) {
                double[] bpCol = bp2[col2];
                for (int i2 = col2 + 1; i2 < m2; i2++) {
                    double[] bpI = bp2[i2];
                    double luICol = this.lu[i2][col2];
                    for (int j2 = 0; j2 < nColB; j2++) {
                        int i3 = j2;
                        bpI[i3] = bpI[i3] - (bpCol[j2] * luICol);
                    }
                }
            }
            for (int col3 = m2 - 1; col3 >= 0; col3--) {
                double[] bpCol2 = bp2[col3];
                double luDiag = this.lu[col3][col3];
                for (int j3 = 0; j3 < nColB; j3++) {
                    int i4 = j3;
                    bpCol2[i4] = bpCol2[i4] / luDiag;
                }
                for (int i5 = 0; i5 < col3; i5++) {
                    double[] bpI2 = bp2[i5];
                    double luICol2 = this.lu[i5][col3];
                    for (int j4 = 0; j4 < nColB; j4++) {
                        int i6 = j4;
                        bpI2[i6] = bpI2[i6] - (bpCol2[j4] * luICol2);
                    }
                }
            }
            return new Array2DRowRealMatrix(bp2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.pivot.length));
        }
    }
}
