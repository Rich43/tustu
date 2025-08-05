package org.apache.commons.math3.linear;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/EigenDecomposition.class */
public class EigenDecomposition {
    private static final double EPSILON = 1.0E-12d;
    private byte maxIter;
    private double[] main;
    private double[] secondary;
    private TriDiagonalTransformer transformer;
    private double[] realEigenvalues;
    private double[] imagEigenvalues;
    private ArrayRealVector[] eigenvectors;
    private RealMatrix cachedV;
    private RealMatrix cachedD;
    private RealMatrix cachedVt;
    private final boolean isSymmetric;

    public EigenDecomposition(RealMatrix matrix) throws MathArithmeticException {
        this.maxIter = (byte) 30;
        double symTol = 10 * matrix.getRowDimension() * matrix.getColumnDimension() * Precision.EPSILON;
        this.isSymmetric = MatrixUtils.isSymmetric(matrix, symTol);
        if (this.isSymmetric) {
            transformToTridiagonal(matrix);
            findEigenVectors(this.transformer.getQ().getData());
        } else {
            SchurTransformer t2 = transformToSchur(matrix);
            findEigenVectorsFromSchur(t2);
        }
    }

    @Deprecated
    public EigenDecomposition(RealMatrix matrix, double splitTolerance) throws MathArithmeticException {
        this(matrix);
    }

    public EigenDecomposition(double[] main, double[] secondary) {
        this.maxIter = (byte) 30;
        this.isSymmetric = true;
        this.main = (double[]) main.clone();
        this.secondary = (double[]) secondary.clone();
        this.transformer = null;
        int size = main.length;
        double[][] z2 = new double[size][size];
        for (int i2 = 0; i2 < size; i2++) {
            z2[i2][i2] = 1.0d;
        }
        findEigenVectors(z2);
    }

    @Deprecated
    public EigenDecomposition(double[] main, double[] secondary, double splitTolerance) {
        this(main, secondary);
    }

    public RealMatrix getV() throws OutOfRangeException, MatrixDimensionMismatchException {
        if (this.cachedV == null) {
            int m2 = this.eigenvectors.length;
            this.cachedV = MatrixUtils.createRealMatrix(m2, m2);
            for (int k2 = 0; k2 < m2; k2++) {
                this.cachedV.setColumnVector(k2, this.eigenvectors[k2]);
            }
        }
        return this.cachedV;
    }

    public RealMatrix getD() throws OutOfRangeException {
        if (this.cachedD == null) {
            this.cachedD = MatrixUtils.createRealDiagonalMatrix(this.realEigenvalues);
            for (int i2 = 0; i2 < this.imagEigenvalues.length; i2++) {
                if (Precision.compareTo(this.imagEigenvalues[i2], 0.0d, 1.0E-12d) > 0) {
                    this.cachedD.setEntry(i2, i2 + 1, this.imagEigenvalues[i2]);
                } else if (Precision.compareTo(this.imagEigenvalues[i2], 0.0d, 1.0E-12d) < 0) {
                    this.cachedD.setEntry(i2, i2 - 1, this.imagEigenvalues[i2]);
                }
            }
        }
        return this.cachedD;
    }

    public RealMatrix getVT() throws OutOfRangeException, MatrixDimensionMismatchException {
        if (this.cachedVt == null) {
            int m2 = this.eigenvectors.length;
            this.cachedVt = MatrixUtils.createRealMatrix(m2, m2);
            for (int k2 = 0; k2 < m2; k2++) {
                this.cachedVt.setRowVector(k2, this.eigenvectors[k2]);
            }
        }
        return this.cachedVt;
    }

    public boolean hasComplexEigenvalues() {
        for (int i2 = 0; i2 < this.imagEigenvalues.length; i2++) {
            if (!Precision.equals(this.imagEigenvalues[i2], 0.0d, 1.0E-12d)) {
                return true;
            }
        }
        return false;
    }

    public double[] getRealEigenvalues() {
        return (double[]) this.realEigenvalues.clone();
    }

    public double getRealEigenvalue(int i2) {
        return this.realEigenvalues[i2];
    }

    public double[] getImagEigenvalues() {
        return (double[]) this.imagEigenvalues.clone();
    }

    public double getImagEigenvalue(int i2) {
        return this.imagEigenvalues[i2];
    }

    public RealVector getEigenvector(int i2) {
        return this.eigenvectors[i2].copy();
    }

    public double getDeterminant() {
        double determinant = 1.0d;
        double[] arr$ = this.realEigenvalues;
        for (double lambda : arr$) {
            determinant *= lambda;
        }
        return determinant;
    }

    public RealMatrix getSquareRoot() throws OutOfRangeException, MatrixDimensionMismatchException {
        if (!this.isSymmetric) {
            throw new MathUnsupportedOperationException();
        }
        double[] sqrtEigenValues = new double[this.realEigenvalues.length];
        for (int i2 = 0; i2 < this.realEigenvalues.length; i2++) {
            double eigen = this.realEigenvalues[i2];
            if (eigen <= 0.0d) {
                throw new MathUnsupportedOperationException();
            }
            sqrtEigenValues[i2] = FastMath.sqrt(eigen);
        }
        RealMatrix sqrtEigen = MatrixUtils.createRealDiagonalMatrix(sqrtEigenValues);
        RealMatrix v2 = getV();
        RealMatrix vT = getVT();
        return v2.multiply(sqrtEigen).multiply(vT);
    }

    public DecompositionSolver getSolver() {
        if (hasComplexEigenvalues()) {
            throw new MathUnsupportedOperationException();
        }
        return new Solver(this.realEigenvalues, this.imagEigenvalues, this.eigenvectors);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/EigenDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private double[] realEigenvalues;
        private double[] imagEigenvalues;
        private final ArrayRealVector[] eigenvectors;

        private Solver(double[] realEigenvalues, double[] imagEigenvalues, ArrayRealVector[] eigenvectors) {
            this.realEigenvalues = realEigenvalues;
            this.imagEigenvalues = imagEigenvalues;
            this.eigenvectors = eigenvectors;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m2 = this.realEigenvalues.length;
            if (b2.getDimension() != m2) {
                throw new DimensionMismatchException(b2.getDimension(), m2);
            }
            double[] bp2 = new double[m2];
            for (int i2 = 0; i2 < m2; i2++) {
                ArrayRealVector v2 = this.eigenvectors[i2];
                double[] vData = v2.getDataRef();
                double s2 = v2.dotProduct(b2) / this.realEigenvalues[i2];
                for (int j2 = 0; j2 < m2; j2++) {
                    int i3 = j2;
                    bp2[i3] = bp2[i3] + (s2 * vData[j2]);
                }
            }
            return new ArrayRealVector(bp2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m2 = this.realEigenvalues.length;
            if (b2.getRowDimension() != m2) {
                throw new DimensionMismatchException(b2.getRowDimension(), m2);
            }
            int nColB = b2.getColumnDimension();
            double[][] bp2 = new double[m2][nColB];
            double[] tmpCol = new double[m2];
            for (int k2 = 0; k2 < nColB; k2++) {
                for (int i2 = 0; i2 < m2; i2++) {
                    tmpCol[i2] = b2.getEntry(i2, k2);
                    bp2[i2][k2] = 0.0d;
                }
                for (int i3 = 0; i3 < m2; i3++) {
                    ArrayRealVector v2 = this.eigenvectors[i3];
                    double[] vData = v2.getDataRef();
                    double s2 = 0.0d;
                    for (int j2 = 0; j2 < m2; j2++) {
                        s2 += v2.getEntry(j2) * tmpCol[j2];
                    }
                    double s3 = s2 / this.realEigenvalues[i3];
                    for (int j3 = 0; j3 < m2; j3++) {
                        double[] dArr = bp2[j3];
                        int i4 = k2;
                        dArr[i4] = dArr[i4] + (s3 * vData[j3]);
                    }
                }
            }
            return new Array2DRowRealMatrix(bp2, false);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            double largestEigenvalueNorm = 0.0d;
            for (int i2 = 0; i2 < this.realEigenvalues.length; i2++) {
                largestEigenvalueNorm = FastMath.max(largestEigenvalueNorm, eigenvalueNorm(i2));
            }
            if (largestEigenvalueNorm == 0.0d) {
                return false;
            }
            for (int i3 = 0; i3 < this.realEigenvalues.length; i3++) {
                if (Precision.equals(eigenvalueNorm(i3) / largestEigenvalueNorm, 0.0d, 1.0E-12d)) {
                    return false;
                }
            }
            return true;
        }

        private double eigenvalueNorm(int i2) {
            double re = this.realEigenvalues[i2];
            double im = this.imagEigenvalues[i2];
            return FastMath.sqrt((re * re) + (im * im));
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            if (!isNonSingular()) {
                throw new SingularMatrixException();
            }
            int m2 = this.realEigenvalues.length;
            double[][] invData = new double[m2][m2];
            for (int i2 = 0; i2 < m2; i2++) {
                double[] invI = invData[i2];
                for (int j2 = 0; j2 < m2; j2++) {
                    double invIJ = 0.0d;
                    for (int k2 = 0; k2 < m2; k2++) {
                        double[] vK = this.eigenvectors[k2].getDataRef();
                        invIJ += (vK[i2] * vK[j2]) / this.realEigenvalues[k2];
                    }
                    invI[j2] = invIJ;
                }
            }
            return MatrixUtils.createRealMatrix(invData);
        }
    }

    private void transformToTridiagonal(RealMatrix matrix) {
        this.transformer = new TriDiagonalTransformer(matrix);
        this.main = this.transformer.getMainDiagonalRef();
        this.secondary = this.transformer.getSecondaryDiagonalRef();
    }

    private void findEigenVectors(double[][] householderMatrix) {
        int m2;
        double q2;
        double[][] z2 = (double[][]) householderMatrix.clone();
        int n2 = this.main.length;
        this.realEigenvalues = new double[n2];
        this.imagEigenvalues = new double[n2];
        double[] e2 = new double[n2];
        for (int i2 = 0; i2 < n2 - 1; i2++) {
            this.realEigenvalues[i2] = this.main[i2];
            e2[i2] = this.secondary[i2];
        }
        this.realEigenvalues[n2 - 1] = this.main[n2 - 1];
        e2[n2 - 1] = 0.0d;
        double maxAbsoluteValue = 0.0d;
        for (int i3 = 0; i3 < n2; i3++) {
            if (FastMath.abs(this.realEigenvalues[i3]) > maxAbsoluteValue) {
                maxAbsoluteValue = FastMath.abs(this.realEigenvalues[i3]);
            }
            if (FastMath.abs(e2[i3]) > maxAbsoluteValue) {
                maxAbsoluteValue = FastMath.abs(e2[i3]);
            }
        }
        if (maxAbsoluteValue != 0.0d) {
            for (int i4 = 0; i4 < n2; i4++) {
                if (FastMath.abs(this.realEigenvalues[i4]) <= Precision.EPSILON * maxAbsoluteValue) {
                    this.realEigenvalues[i4] = 0.0d;
                }
                if (FastMath.abs(e2[i4]) <= Precision.EPSILON * maxAbsoluteValue) {
                    e2[i4] = 0.0d;
                }
            }
        }
        for (int j2 = 0; j2 < n2; j2++) {
            int its = 0;
            do {
                m2 = j2;
                while (m2 < n2 - 1) {
                    double delta = FastMath.abs(this.realEigenvalues[m2]) + FastMath.abs(this.realEigenvalues[m2 + 1]);
                    if (FastMath.abs(e2[m2]) + delta == delta) {
                        break;
                    } else {
                        m2++;
                    }
                }
                if (m2 != j2) {
                    if (its == this.maxIter) {
                        throw new MaxCountExceededException(LocalizedFormats.CONVERGENCE_FAILED, Byte.valueOf(this.maxIter), new Object[0]);
                    }
                    its++;
                    double q3 = (this.realEigenvalues[j2 + 1] - this.realEigenvalues[j2]) / (2.0d * e2[j2]);
                    double t2 = FastMath.sqrt(1.0d + (q3 * q3));
                    if (q3 < 0.0d) {
                        q2 = (this.realEigenvalues[m2] - this.realEigenvalues[j2]) + (e2[j2] / (q3 - t2));
                    } else {
                        q2 = (this.realEigenvalues[m2] - this.realEigenvalues[j2]) + (e2[j2] / (q3 + t2));
                    }
                    double u2 = 0.0d;
                    double s2 = 1.0d;
                    double c2 = 1.0d;
                    int i5 = m2 - 1;
                    while (true) {
                        if (i5 < j2) {
                            break;
                        }
                        double p2 = s2 * e2[i5];
                        double h2 = c2 * e2[i5];
                        if (FastMath.abs(p2) >= FastMath.abs(q2)) {
                            double c3 = q2 / p2;
                            t2 = FastMath.sqrt((c3 * c3) + 1.0d);
                            e2[i5 + 1] = p2 * t2;
                            s2 = 1.0d / t2;
                            c2 = c3 * s2;
                        } else {
                            double s3 = p2 / q2;
                            t2 = FastMath.sqrt((s3 * s3) + 1.0d);
                            e2[i5 + 1] = q2 * t2;
                            c2 = 1.0d / t2;
                            s2 = s3 * c2;
                        }
                        if (e2[i5 + 1] == 0.0d) {
                            double[] dArr = this.realEigenvalues;
                            int i6 = i5 + 1;
                            dArr[i6] = dArr[i6] - u2;
                            e2[m2] = 0.0d;
                            break;
                        }
                        double q4 = this.realEigenvalues[i5 + 1] - u2;
                        t2 = ((this.realEigenvalues[i5] - q4) * s2) + (2.0d * c2 * h2);
                        u2 = s2 * t2;
                        this.realEigenvalues[i5 + 1] = q4 + u2;
                        q2 = (c2 * t2) - h2;
                        for (int ia = 0; ia < n2; ia++) {
                            double p3 = z2[ia][i5 + 1];
                            z2[ia][i5 + 1] = (s2 * z2[ia][i5]) + (c2 * p3);
                            z2[ia][i5] = (c2 * z2[ia][i5]) - (s2 * p3);
                        }
                        i5--;
                    }
                    if (t2 != 0.0d || i5 < j2) {
                        double[] dArr2 = this.realEigenvalues;
                        int i7 = j2;
                        dArr2[i7] = dArr2[i7] - u2;
                        e2[j2] = q2;
                        e2[m2] = 0.0d;
                    }
                }
            } while (m2 != j2);
        }
        for (int i8 = 0; i8 < n2; i8++) {
            int k2 = i8;
            double p4 = this.realEigenvalues[i8];
            for (int j3 = i8 + 1; j3 < n2; j3++) {
                if (this.realEigenvalues[j3] > p4) {
                    k2 = j3;
                    p4 = this.realEigenvalues[j3];
                }
            }
            if (k2 != i8) {
                this.realEigenvalues[k2] = this.realEigenvalues[i8];
                this.realEigenvalues[i8] = p4;
                for (int j4 = 0; j4 < n2; j4++) {
                    double p5 = z2[j4][i8];
                    z2[j4][i8] = z2[j4][k2];
                    z2[j4][k2] = p5;
                }
            }
        }
        double maxAbsoluteValue2 = 0.0d;
        for (int i9 = 0; i9 < n2; i9++) {
            if (FastMath.abs(this.realEigenvalues[i9]) > maxAbsoluteValue2) {
                maxAbsoluteValue2 = FastMath.abs(this.realEigenvalues[i9]);
            }
        }
        if (maxAbsoluteValue2 != 0.0d) {
            for (int i10 = 0; i10 < n2; i10++) {
                if (FastMath.abs(this.realEigenvalues[i10]) < Precision.EPSILON * maxAbsoluteValue2) {
                    this.realEigenvalues[i10] = 0.0d;
                }
            }
        }
        this.eigenvectors = new ArrayRealVector[n2];
        double[] tmp = new double[n2];
        for (int i11 = 0; i11 < n2; i11++) {
            for (int j5 = 0; j5 < n2; j5++) {
                tmp[j5] = z2[j5][i11];
            }
            this.eigenvectors[i11] = new ArrayRealVector(tmp);
        }
    }

    private SchurTransformer transformToSchur(RealMatrix matrix) {
        SchurTransformer schurTransform = new SchurTransformer(matrix);
        double[][] matT = schurTransform.getT().getData();
        this.realEigenvalues = new double[matT.length];
        this.imagEigenvalues = new double[matT.length];
        int i2 = 0;
        while (i2 < this.realEigenvalues.length) {
            if (i2 == this.realEigenvalues.length - 1 || Precision.equals(matT[i2 + 1][i2], 0.0d, 1.0E-12d)) {
                this.realEigenvalues[i2] = matT[i2][i2];
            } else {
                double x2 = matT[i2 + 1][i2 + 1];
                double p2 = 0.5d * (matT[i2][i2] - x2);
                double z2 = FastMath.sqrt(FastMath.abs((p2 * p2) + (matT[i2 + 1][i2] * matT[i2][i2 + 1])));
                this.realEigenvalues[i2] = x2 + p2;
                this.imagEigenvalues[i2] = z2;
                this.realEigenvalues[i2 + 1] = x2 + p2;
                this.imagEigenvalues[i2 + 1] = -z2;
                i2++;
            }
            i2++;
        }
        return schurTransform;
    }

    private Complex cdiv(double xr, double xi, double yr, double yi) {
        return new Complex(xr, xi).divide(new Complex(yr, yi));
    }

    private void findEigenVectorsFromSchur(SchurTransformer schur) throws MathArithmeticException {
        double[][] matrixT = schur.getT().getData();
        double[][] matrixP = schur.getP().getData();
        int n2 = matrixT.length;
        double norm = 0.0d;
        for (int i2 = 0; i2 < n2; i2++) {
            for (int j2 = FastMath.max(i2 - 1, 0); j2 < n2; j2++) {
                norm += FastMath.abs(matrixT[i2][j2]);
            }
        }
        if (Precision.equals(norm, 0.0d, 1.0E-12d)) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        double r2 = 0.0d;
        double s2 = 0.0d;
        double z2 = 0.0d;
        for (int idx = n2 - 1; idx >= 0; idx--) {
            double p2 = this.realEigenvalues[idx];
            double q2 = this.imagEigenvalues[idx];
            if (Precision.equals(q2, 0.0d)) {
                int l2 = idx;
                matrixT[idx][idx] = 1.0d;
                for (int i3 = idx - 1; i3 >= 0; i3--) {
                    double w2 = matrixT[i3][i3] - p2;
                    r2 = 0.0d;
                    for (int j3 = l2; j3 <= idx; j3++) {
                        r2 += matrixT[i3][j3] * matrixT[j3][idx];
                    }
                    if (Precision.compareTo(this.imagEigenvalues[i3], 0.0d, 1.0E-12d) < 0) {
                        z2 = w2;
                        s2 = r2;
                    } else {
                        l2 = i3;
                        if (!Precision.equals(this.imagEigenvalues[i3], 0.0d)) {
                            double x2 = matrixT[i3][i3 + 1];
                            double y2 = matrixT[i3 + 1][i3];
                            double t2 = ((x2 * s2) - (z2 * r2)) / (((this.realEigenvalues[i3] - p2) * (this.realEigenvalues[i3] - p2)) + (this.imagEigenvalues[i3] * this.imagEigenvalues[i3]));
                            matrixT[i3][idx] = t2;
                            if (FastMath.abs(x2) > FastMath.abs(z2)) {
                                matrixT[i3 + 1][idx] = ((-r2) - (w2 * t2)) / x2;
                            } else {
                                matrixT[i3 + 1][idx] = ((-s2) - (y2 * t2)) / z2;
                            }
                        } else if (w2 != 0.0d) {
                            matrixT[i3][idx] = (-r2) / w2;
                        } else {
                            matrixT[i3][idx] = (-r2) / (Precision.EPSILON * norm);
                        }
                        double t3 = FastMath.abs(matrixT[i3][idx]);
                        if (Precision.EPSILON * t3 * t3 > 1.0d) {
                            for (int j4 = i3; j4 <= idx; j4++) {
                                double[] dArr = matrixT[j4];
                                int i4 = idx;
                                dArr[i4] = dArr[i4] / t3;
                            }
                        }
                    }
                }
            } else if (q2 < 0.0d) {
                int l3 = idx - 1;
                if (FastMath.abs(matrixT[idx][idx - 1]) > FastMath.abs(matrixT[idx - 1][idx])) {
                    matrixT[idx - 1][idx - 1] = q2 / matrixT[idx][idx - 1];
                    matrixT[idx - 1][idx] = (-(matrixT[idx][idx] - p2)) / matrixT[idx][idx - 1];
                } else {
                    Complex result = cdiv(0.0d, -matrixT[idx - 1][idx], matrixT[idx - 1][idx - 1] - p2, q2);
                    matrixT[idx - 1][idx - 1] = result.getReal();
                    matrixT[idx - 1][idx] = result.getImaginary();
                }
                matrixT[idx][idx - 1] = 0.0d;
                matrixT[idx][idx] = 1.0d;
                for (int i5 = idx - 2; i5 >= 0; i5--) {
                    double ra = 0.0d;
                    double sa = 0.0d;
                    for (int j5 = l3; j5 <= idx; j5++) {
                        ra += matrixT[i5][j5] * matrixT[j5][idx - 1];
                        sa += matrixT[i5][j5] * matrixT[j5][idx];
                    }
                    double w3 = matrixT[i5][i5] - p2;
                    if (Precision.compareTo(this.imagEigenvalues[i5], 0.0d, 1.0E-12d) < 0) {
                        z2 = w3;
                        r2 = ra;
                        s2 = sa;
                    } else {
                        l3 = i5;
                        if (Precision.equals(this.imagEigenvalues[i5], 0.0d)) {
                            Complex c2 = cdiv(-ra, -sa, w3, q2);
                            matrixT[i5][idx - 1] = c2.getReal();
                            matrixT[i5][idx] = c2.getImaginary();
                        } else {
                            double x3 = matrixT[i5][i5 + 1];
                            double y3 = matrixT[i5 + 1][i5];
                            double vr = (((this.realEigenvalues[i5] - p2) * (this.realEigenvalues[i5] - p2)) + (this.imagEigenvalues[i5] * this.imagEigenvalues[i5])) - (q2 * q2);
                            double vi = (this.realEigenvalues[i5] - p2) * 2.0d * q2;
                            if (Precision.equals(vr, 0.0d) && Precision.equals(vi, 0.0d)) {
                                vr = Precision.EPSILON * norm * (FastMath.abs(w3) + FastMath.abs(q2) + FastMath.abs(x3) + FastMath.abs(y3) + FastMath.abs(z2));
                            }
                            Complex c3 = cdiv(((x3 * r2) - (z2 * ra)) + (q2 * sa), ((x3 * s2) - (z2 * sa)) - (q2 * ra), vr, vi);
                            matrixT[i5][idx - 1] = c3.getReal();
                            matrixT[i5][idx] = c3.getImaginary();
                            if (FastMath.abs(x3) > FastMath.abs(z2) + FastMath.abs(q2)) {
                                matrixT[i5 + 1][idx - 1] = (((-ra) - (w3 * matrixT[i5][idx - 1])) + (q2 * matrixT[i5][idx])) / x3;
                                matrixT[i5 + 1][idx] = (((-sa) - (w3 * matrixT[i5][idx])) - (q2 * matrixT[i5][idx - 1])) / x3;
                            } else {
                                Complex c22 = cdiv((-r2) - (y3 * matrixT[i5][idx - 1]), (-s2) - (y3 * matrixT[i5][idx]), z2, q2);
                                matrixT[i5 + 1][idx - 1] = c22.getReal();
                                matrixT[i5 + 1][idx] = c22.getImaginary();
                            }
                        }
                        double t4 = FastMath.max(FastMath.abs(matrixT[i5][idx - 1]), FastMath.abs(matrixT[i5][idx]));
                        if (Precision.EPSILON * t4 * t4 > 1.0d) {
                            for (int j6 = i5; j6 <= idx; j6++) {
                                double[] dArr2 = matrixT[j6];
                                int i6 = idx - 1;
                                dArr2[i6] = dArr2[i6] / t4;
                                double[] dArr3 = matrixT[j6];
                                int i7 = idx;
                                dArr3[i7] = dArr3[i7] / t4;
                            }
                        }
                    }
                }
            }
        }
        for (int j7 = n2 - 1; j7 >= 0; j7--) {
            for (int i8 = 0; i8 <= n2 - 1; i8++) {
                double z3 = 0.0d;
                for (int k2 = 0; k2 <= FastMath.min(j7, n2 - 1); k2++) {
                    z3 += matrixP[i8][k2] * matrixT[k2][j7];
                }
                matrixP[i8][j7] = z3;
            }
        }
        this.eigenvectors = new ArrayRealVector[n2];
        double[] tmp = new double[n2];
        for (int i9 = 0; i9 < n2; i9++) {
            for (int j8 = 0; j8 < n2; j8++) {
                tmp[j8] = matrixP[j8][i9];
            }
            this.eigenvectors[i9] = new ArrayRealVector(tmp);
        }
    }
}
