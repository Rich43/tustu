package org.apache.commons.math3.linear;

import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/QRDecomposition.class */
public class QRDecomposition {
    private double[][] qrt;
    private double[] rDiag;
    private RealMatrix cachedQ;
    private RealMatrix cachedQT;
    private RealMatrix cachedR;
    private RealMatrix cachedH;
    private final double threshold;

    public QRDecomposition(RealMatrix matrix) {
        this(matrix, 0.0d);
    }

    public QRDecomposition(RealMatrix matrix, double threshold) {
        this.threshold = threshold;
        int m2 = matrix.getRowDimension();
        int n2 = matrix.getColumnDimension();
        this.qrt = matrix.transpose().getData();
        this.rDiag = new double[FastMath.min(m2, n2)];
        this.cachedQ = null;
        this.cachedQT = null;
        this.cachedR = null;
        this.cachedH = null;
        decompose(this.qrt);
    }

    protected void decompose(double[][] matrix) {
        for (int minor = 0; minor < FastMath.min(matrix.length, matrix[0].length); minor++) {
            performHouseholderReflection(minor, matrix);
        }
    }

    protected void performHouseholderReflection(int minor, double[][] matrix) {
        double[] qrtMinor = matrix[minor];
        double xNormSqr = 0.0d;
        for (int row = minor; row < qrtMinor.length; row++) {
            double c2 = qrtMinor[row];
            xNormSqr += c2 * c2;
        }
        double a2 = qrtMinor[minor] > 0.0d ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
        this.rDiag[minor] = a2;
        if (a2 != 0.0d) {
            qrtMinor[minor] = qrtMinor[minor] - a2;
            for (int col = minor + 1; col < matrix.length; col++) {
                double[] qrtCol = matrix[col];
                double alpha = 0.0d;
                for (int row2 = minor; row2 < qrtCol.length; row2++) {
                    alpha -= qrtCol[row2] * qrtMinor[row2];
                }
                double alpha2 = alpha / (a2 * qrtMinor[minor]);
                for (int row3 = minor; row3 < qrtCol.length; row3++) {
                    int i2 = row3;
                    qrtCol[i2] = qrtCol[i2] - (alpha2 * qrtMinor[row3]);
                }
            }
        }
    }

    public RealMatrix getR() {
        if (this.cachedR == null) {
            int n2 = this.qrt.length;
            int m2 = this.qrt[0].length;
            double[][] ra = new double[m2][n2];
            for (int row = FastMath.min(m2, n2) - 1; row >= 0; row--) {
                ra[row][row] = this.rDiag[row];
                for (int col = row + 1; col < n2; col++) {
                    ra[row][col] = this.qrt[col][row];
                }
            }
            this.cachedR = MatrixUtils.createRealMatrix(ra);
        }
        return this.cachedR;
    }

    public RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    public RealMatrix getQT() {
        if (this.cachedQT == null) {
            int n2 = this.qrt.length;
            int m2 = this.qrt[0].length;
            double[][] qta = new double[m2][m2];
            for (int minor = m2 - 1; minor >= FastMath.min(m2, n2); minor--) {
                qta[minor][minor] = 1.0d;
            }
            for (int minor2 = FastMath.min(m2, n2) - 1; minor2 >= 0; minor2--) {
                double[] qrtMinor = this.qrt[minor2];
                qta[minor2][minor2] = 1.0d;
                if (qrtMinor[minor2] != 0.0d) {
                    for (int col = minor2; col < m2; col++) {
                        double alpha = 0.0d;
                        for (int row = minor2; row < m2; row++) {
                            alpha -= qta[col][row] * qrtMinor[row];
                        }
                        double alpha2 = alpha / (this.rDiag[minor2] * qrtMinor[minor2]);
                        for (int row2 = minor2; row2 < m2; row2++) {
                            double[] dArr = qta[col];
                            int i2 = row2;
                            dArr[i2] = dArr[i2] + ((-alpha2) * qrtMinor[row2]);
                        }
                    }
                }
            }
            this.cachedQT = MatrixUtils.createRealMatrix(qta);
        }
        return this.cachedQT;
    }

    public RealMatrix getH() {
        if (this.cachedH == null) {
            int n2 = this.qrt.length;
            int m2 = this.qrt[0].length;
            double[][] ha = new double[m2][n2];
            for (int i2 = 0; i2 < m2; i2++) {
                for (int j2 = 0; j2 < FastMath.min(i2 + 1, n2); j2++) {
                    ha[i2][j2] = this.qrt[j2][i2] / (-this.rDiag[j2]);
                }
            }
            this.cachedH = MatrixUtils.createRealMatrix(ha);
        }
        return this.cachedH;
    }

    public DecompositionSolver getSolver() {
        return new Solver(this.qrt, this.rDiag, this.threshold);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/QRDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private final double[][] qrt;
        private final double[] rDiag;
        private final double threshold;

        private Solver(double[][] qrt, double[] rDiag, double threshold) {
            this.qrt = qrt;
            this.rDiag = rDiag;
            this.threshold = threshold;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            double[] arr$ = this.rDiag;
            for (double diag : arr$) {
                if (FastMath.abs(diag) <= this.threshold) {
                    return false;
                }
            }
            return true;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            int n2 = this.qrt.length;
            int m2 = this.qrt[0].length;
            if (b2.getDimension() != m2) {
                throw new DimensionMismatchException(b2.getDimension(), m2);
            }
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            double[] x2 = new double[n2];
            double[] y2 = b2.toArray();
            for (int minor = 0; minor < FastMath.min(m2, n2); minor++) {
                double[] qrtMinor = this.qrt[minor];
                double dotProduct = 0.0d;
                for (int row = minor; row < m2; row++) {
                    dotProduct += y2[row] * qrtMinor[row];
                }
                double dotProduct2 = dotProduct / (this.rDiag[minor] * qrtMinor[minor]);
                for (int row2 = minor; row2 < m2; row2++) {
                    int i2 = row2;
                    y2[i2] = y2[i2] + (dotProduct2 * qrtMinor[row2]);
                }
            }
            for (int row3 = this.rDiag.length - 1; row3 >= 0; row3--) {
                int i3 = row3;
                y2[i3] = y2[i3] / this.rDiag[row3];
                double yRow = y2[row3];
                double[] qrtRow = this.qrt[row3];
                x2[row3] = yRow;
                for (int i4 = 0; i4 < row3; i4++) {
                    int i5 = i4;
                    y2[i5] = y2[i5] - (yRow * qrtRow[i4]);
                }
            }
            return new ArrayRealVector(x2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException {
            int n2 = this.qrt.length;
            int m2 = this.qrt[0].length;
            if (b2.getRowDimension() != m2) {
                throw new DimensionMismatchException(b2.getRowDimension(), m2);
            }
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int columns = b2.getColumnDimension();
            int cBlocks = ((columns + 52) - 1) / 52;
            double[][] xBlocks = BlockRealMatrix.createBlocksLayout(n2, columns);
            double[][] y2 = new double[b2.getRowDimension()][52];
            double[] alpha = new double[52];
            for (int kBlock = 0; kBlock < cBlocks; kBlock++) {
                int kStart = kBlock * 52;
                int kEnd = FastMath.min(kStart + 52, columns);
                int kWidth = kEnd - kStart;
                b2.copySubMatrix(0, m2 - 1, kStart, kEnd - 1, y2);
                for (int minor = 0; minor < FastMath.min(m2, n2); minor++) {
                    double[] qrtMinor = this.qrt[minor];
                    double factor = 1.0d / (this.rDiag[minor] * qrtMinor[minor]);
                    Arrays.fill(alpha, 0, kWidth, 0.0d);
                    for (int row = minor; row < m2; row++) {
                        double d2 = qrtMinor[row];
                        double[] yRow = y2[row];
                        for (int k2 = 0; k2 < kWidth; k2++) {
                            int i2 = k2;
                            alpha[i2] = alpha[i2] + (d2 * yRow[k2]);
                        }
                    }
                    for (int k3 = 0; k3 < kWidth; k3++) {
                        int i3 = k3;
                        alpha[i3] = alpha[i3] * factor;
                    }
                    for (int row2 = minor; row2 < m2; row2++) {
                        double d3 = qrtMinor[row2];
                        double[] yRow2 = y2[row2];
                        for (int k4 = 0; k4 < kWidth; k4++) {
                            int i4 = k4;
                            yRow2[i4] = yRow2[i4] + (alpha[k4] * d3);
                        }
                    }
                }
                for (int j2 = this.rDiag.length - 1; j2 >= 0; j2--) {
                    int jBlock = j2 / 52;
                    int jStart = jBlock * 52;
                    double factor2 = 1.0d / this.rDiag[j2];
                    double[] yJ = y2[j2];
                    double[] xBlock = xBlocks[(jBlock * cBlocks) + kBlock];
                    int index = (j2 - jStart) * kWidth;
                    for (int k5 = 0; k5 < kWidth; k5++) {
                        int i5 = k5;
                        yJ[i5] = yJ[i5] * factor2;
                        int i6 = index;
                        index++;
                        xBlock[i6] = yJ[k5];
                    }
                    double[] qrtJ = this.qrt[j2];
                    for (int i7 = 0; i7 < j2; i7++) {
                        double rIJ = qrtJ[i7];
                        double[] yI = y2[i7];
                        for (int k6 = 0; k6 < kWidth; k6++) {
                            int i8 = k6;
                            yI[i8] = yI[i8] - (yJ[k6] * rIJ);
                        }
                    }
                }
            }
            return new BlockRealMatrix(n2, columns, xBlocks, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.qrt[0].length));
        }
    }
}
