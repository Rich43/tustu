package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/CholeskyDecomposition.class */
public class CholeskyDecomposition {
    public static final double DEFAULT_RELATIVE_SYMMETRY_THRESHOLD = 1.0E-15d;
    public static final double DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD = 1.0E-10d;
    private double[][] lTData;
    private RealMatrix cachedL;
    private RealMatrix cachedLT;

    public CholeskyDecomposition(RealMatrix matrix) {
        this(matrix, 1.0E-15d, 1.0E-10d);
    }

    public CholeskyDecomposition(RealMatrix matrix, double relativeSymmetryThreshold, double absolutePositivityThreshold) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int order = matrix.getRowDimension();
        this.lTData = matrix.getData();
        this.cachedL = null;
        this.cachedLT = null;
        for (int i2 = 0; i2 < order; i2++) {
            double[] lI = this.lTData[i2];
            for (int j2 = i2 + 1; j2 < order; j2++) {
                double[] lJ = this.lTData[j2];
                double lIJ = lI[j2];
                double lJI = lJ[i2];
                double maxDelta = relativeSymmetryThreshold * FastMath.max(FastMath.abs(lIJ), FastMath.abs(lJI));
                if (FastMath.abs(lIJ - lJI) > maxDelta) {
                    throw new NonSymmetricMatrixException(i2, j2, relativeSymmetryThreshold);
                }
                lJ[i2] = 0.0d;
            }
        }
        for (int i3 = 0; i3 < order; i3++) {
            double[] ltI = this.lTData[i3];
            if (ltI[i3] <= absolutePositivityThreshold) {
                throw new NonPositiveDefiniteMatrixException(ltI[i3], i3, absolutePositivityThreshold);
            }
            ltI[i3] = FastMath.sqrt(ltI[i3]);
            double inverse = 1.0d / ltI[i3];
            for (int q2 = order - 1; q2 > i3; q2--) {
                int i4 = q2;
                ltI[i4] = ltI[i4] * inverse;
                double[] ltQ = this.lTData[q2];
                for (int p2 = q2; p2 < order; p2++) {
                    int i5 = p2;
                    ltQ[i5] = ltQ[i5] - (ltI[q2] * ltI[p2]);
                }
            }
        }
    }

    public RealMatrix getL() {
        if (this.cachedL == null) {
            this.cachedL = getLT().transpose();
        }
        return this.cachedL;
    }

    public RealMatrix getLT() {
        if (this.cachedLT == null) {
            this.cachedLT = MatrixUtils.createRealMatrix(this.lTData);
        }
        return this.cachedLT;
    }

    public double getDeterminant() {
        double determinant = 1.0d;
        for (int i2 = 0; i2 < this.lTData.length; i2++) {
            double lTii = this.lTData[i2][i2];
            determinant *= lTii * lTii;
        }
        return determinant;
    }

    public DecompositionSolver getSolver() {
        return new Solver(this.lTData);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/CholeskyDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private final double[][] lTData;

        private Solver(double[][] lTData) {
            this.lTData = lTData;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            return true;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            int m2 = this.lTData.length;
            if (b2.getDimension() != m2) {
                throw new DimensionMismatchException(b2.getDimension(), m2);
            }
            double[] x2 = b2.toArray();
            for (int j2 = 0; j2 < m2; j2++) {
                double[] lJ = this.lTData[j2];
                int i2 = j2;
                x2[i2] = x2[i2] / lJ[j2];
                double xJ = x2[j2];
                for (int i3 = j2 + 1; i3 < m2; i3++) {
                    int i4 = i3;
                    x2[i4] = x2[i4] - (xJ * lJ[i3]);
                }
            }
            for (int j3 = m2 - 1; j3 >= 0; j3--) {
                int i5 = j3;
                x2[i5] = x2[i5] / this.lTData[j3][j3];
                double xJ2 = x2[j3];
                for (int i6 = 0; i6 < j3; i6++) {
                    int i7 = i6;
                    x2[i7] = x2[i7] - (xJ2 * this.lTData[i6][j3]);
                }
            }
            return new ArrayRealVector(x2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) {
            int m2 = this.lTData.length;
            if (b2.getRowDimension() != m2) {
                throw new DimensionMismatchException(b2.getRowDimension(), m2);
            }
            int nColB = b2.getColumnDimension();
            double[][] x2 = b2.getData();
            for (int j2 = 0; j2 < m2; j2++) {
                double[] lJ = this.lTData[j2];
                double lJJ = lJ[j2];
                double[] xJ = x2[j2];
                for (int k2 = 0; k2 < nColB; k2++) {
                    int i2 = k2;
                    xJ[i2] = xJ[i2] / lJJ;
                }
                for (int i3 = j2 + 1; i3 < m2; i3++) {
                    double[] xI = x2[i3];
                    double lJI = lJ[i3];
                    for (int k3 = 0; k3 < nColB; k3++) {
                        int i4 = k3;
                        xI[i4] = xI[i4] - (xJ[k3] * lJI);
                    }
                }
            }
            for (int j3 = m2 - 1; j3 >= 0; j3--) {
                double lJJ2 = this.lTData[j3][j3];
                double[] xJ2 = x2[j3];
                for (int k4 = 0; k4 < nColB; k4++) {
                    int i5 = k4;
                    xJ2[i5] = xJ2[i5] / lJJ2;
                }
                for (int i6 = 0; i6 < j3; i6++) {
                    double[] xI2 = x2[i6];
                    double lIJ = this.lTData[i6][j3];
                    for (int k5 = 0; k5 < nColB; k5++) {
                        int i7 = k5;
                        xI2[i7] = xI2[i7] - (xJ2[k5] * lIJ);
                    }
                }
            }
            return new Array2DRowRealMatrix(x2);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.lTData.length));
        }
    }
}
