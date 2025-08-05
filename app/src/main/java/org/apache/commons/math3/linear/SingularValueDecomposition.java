package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SingularValueDecomposition.class */
public class SingularValueDecomposition {
    private static final double EPS = 2.220446049250313E-16d;
    private static final double TINY = 1.6033346880071782E-291d;
    private final double[] singularValues;

    /* renamed from: m, reason: collision with root package name */
    private final int f13034m;

    /* renamed from: n, reason: collision with root package name */
    private final int f13035n;
    private final boolean transposed;
    private final RealMatrix cachedU;
    private RealMatrix cachedUt;
    private RealMatrix cachedS;
    private final RealMatrix cachedV;
    private RealMatrix cachedVt;
    private final double tol;

    public SingularValueDecomposition(RealMatrix matrix) {
        double[][] A2;
        int kase;
        if (matrix.getRowDimension() < matrix.getColumnDimension()) {
            this.transposed = true;
            A2 = matrix.transpose().getData();
            this.f13034m = matrix.getColumnDimension();
            this.f13035n = matrix.getRowDimension();
        } else {
            this.transposed = false;
            A2 = matrix.getData();
            this.f13034m = matrix.getRowDimension();
            this.f13035n = matrix.getColumnDimension();
        }
        this.singularValues = new double[this.f13035n];
        double[][] U2 = new double[this.f13034m][this.f13035n];
        double[][] V2 = new double[this.f13035n][this.f13035n];
        double[] e2 = new double[this.f13035n];
        double[] work = new double[this.f13034m];
        int nct = FastMath.min(this.f13034m - 1, this.f13035n);
        int nrt = FastMath.max(0, this.f13035n - 2);
        for (int k2 = 0; k2 < FastMath.max(nct, nrt); k2++) {
            if (k2 < nct) {
                this.singularValues[k2] = 0.0d;
                for (int i2 = k2; i2 < this.f13034m; i2++) {
                    this.singularValues[k2] = FastMath.hypot(this.singularValues[k2], A2[i2][k2]);
                }
                if (this.singularValues[k2] != 0.0d) {
                    if (A2[k2][k2] < 0.0d) {
                        this.singularValues[k2] = -this.singularValues[k2];
                    }
                    for (int i3 = k2; i3 < this.f13034m; i3++) {
                        double[] dArr = A2[i3];
                        int i4 = k2;
                        dArr[i4] = dArr[i4] / this.singularValues[k2];
                    }
                    double[] dArr2 = A2[k2];
                    int i5 = k2;
                    dArr2[i5] = dArr2[i5] + 1.0d;
                }
                this.singularValues[k2] = -this.singularValues[k2];
            }
            for (int j2 = k2 + 1; j2 < this.f13035n; j2++) {
                if (k2 < nct && this.singularValues[k2] != 0.0d) {
                    double t2 = 0.0d;
                    for (int i6 = k2; i6 < this.f13034m; i6++) {
                        t2 += A2[i6][k2] * A2[i6][j2];
                    }
                    double t3 = (-t2) / A2[k2][k2];
                    for (int i7 = k2; i7 < this.f13034m; i7++) {
                        double[] dArr3 = A2[i7];
                        int i8 = j2;
                        dArr3[i8] = dArr3[i8] + (t3 * A2[i7][k2]);
                    }
                }
                e2[j2] = A2[k2][j2];
            }
            if (k2 < nct) {
                for (int i9 = k2; i9 < this.f13034m; i9++) {
                    U2[i9][k2] = A2[i9][k2];
                }
            }
            if (k2 < nrt) {
                e2[k2] = 0.0d;
                for (int i10 = k2 + 1; i10 < this.f13035n; i10++) {
                    e2[k2] = FastMath.hypot(e2[k2], e2[i10]);
                }
                if (e2[k2] != 0.0d) {
                    if (e2[k2 + 1] < 0.0d) {
                        e2[k2] = -e2[k2];
                    }
                    for (int i11 = k2 + 1; i11 < this.f13035n; i11++) {
                        int i12 = i11;
                        e2[i12] = e2[i12] / e2[k2];
                    }
                    int i13 = k2 + 1;
                    e2[i13] = e2[i13] + 1.0d;
                }
                e2[k2] = -e2[k2];
                if (k2 + 1 < this.f13034m && e2[k2] != 0.0d) {
                    for (int i14 = k2 + 1; i14 < this.f13034m; i14++) {
                        work[i14] = 0.0d;
                    }
                    for (int j3 = k2 + 1; j3 < this.f13035n; j3++) {
                        for (int i15 = k2 + 1; i15 < this.f13034m; i15++) {
                            int i16 = i15;
                            work[i16] = work[i16] + (e2[j3] * A2[i15][j3]);
                        }
                    }
                    for (int j4 = k2 + 1; j4 < this.f13035n; j4++) {
                        double t4 = (-e2[j4]) / e2[k2 + 1];
                        for (int i17 = k2 + 1; i17 < this.f13034m; i17++) {
                            double[] dArr4 = A2[i17];
                            int i18 = j4;
                            dArr4[i18] = dArr4[i18] + (t4 * work[i17]);
                        }
                    }
                }
                for (int i19 = k2 + 1; i19 < this.f13035n; i19++) {
                    V2[i19][k2] = e2[i19];
                }
            }
        }
        int p2 = this.f13035n;
        if (nct < this.f13035n) {
            this.singularValues[nct] = A2[nct][nct];
        }
        if (this.f13034m < p2) {
            this.singularValues[p2 - 1] = 0.0d;
        }
        if (nrt + 1 < p2) {
            e2[nrt] = A2[nrt][p2 - 1];
        }
        e2[p2 - 1] = 0.0d;
        for (int j5 = nct; j5 < this.f13035n; j5++) {
            for (int i20 = 0; i20 < this.f13034m; i20++) {
                U2[i20][j5] = 0.0d;
            }
            U2[j5][j5] = 1.0d;
        }
        for (int k3 = nct - 1; k3 >= 0; k3--) {
            if (this.singularValues[k3] != 0.0d) {
                for (int j6 = k3 + 1; j6 < this.f13035n; j6++) {
                    double t5 = 0.0d;
                    for (int i21 = k3; i21 < this.f13034m; i21++) {
                        t5 += U2[i21][k3] * U2[i21][j6];
                    }
                    double t6 = (-t5) / U2[k3][k3];
                    for (int i22 = k3; i22 < this.f13034m; i22++) {
                        double[] dArr5 = U2[i22];
                        int i23 = j6;
                        dArr5[i23] = dArr5[i23] + (t6 * U2[i22][k3]);
                    }
                }
                for (int i24 = k3; i24 < this.f13034m; i24++) {
                    U2[i24][k3] = -U2[i24][k3];
                }
                U2[k3][k3] = 1.0d + U2[k3][k3];
                for (int i25 = 0; i25 < k3 - 1; i25++) {
                    U2[i25][k3] = 0.0d;
                }
            } else {
                for (int i26 = 0; i26 < this.f13034m; i26++) {
                    U2[i26][k3] = 0.0d;
                }
                U2[k3][k3] = 1.0d;
            }
        }
        for (int k4 = this.f13035n - 1; k4 >= 0; k4--) {
            if (k4 < nrt && e2[k4] != 0.0d) {
                for (int j7 = k4 + 1; j7 < this.f13035n; j7++) {
                    double t7 = 0.0d;
                    for (int i27 = k4 + 1; i27 < this.f13035n; i27++) {
                        t7 += V2[i27][k4] * V2[i27][j7];
                    }
                    double t8 = (-t7) / V2[k4 + 1][k4];
                    for (int i28 = k4 + 1; i28 < this.f13035n; i28++) {
                        double[] dArr6 = V2[i28];
                        int i29 = j7;
                        dArr6[i29] = dArr6[i29] + (t8 * V2[i28][k4]);
                    }
                }
            }
            for (int i30 = 0; i30 < this.f13035n; i30++) {
                V2[i30][k4] = 0.0d;
            }
            V2[k4][k4] = 1.0d;
        }
        int pp = p2 - 1;
        while (p2 > 0) {
            int k5 = p2 - 2;
            while (true) {
                if (k5 >= 0) {
                    double threshold = TINY + (EPS * (FastMath.abs(this.singularValues[k5]) + FastMath.abs(this.singularValues[k5 + 1])));
                    if (FastMath.abs(e2[k5]) > threshold) {
                        k5--;
                    } else {
                        e2[k5] = 0.0d;
                    }
                }
            }
            if (k5 == p2 - 2) {
                kase = 4;
            } else {
                int ks = p2 - 1;
                while (true) {
                    if (ks >= k5 && ks != k5) {
                        if (FastMath.abs(this.singularValues[ks]) > TINY + (EPS * ((ks != p2 ? FastMath.abs(e2[ks]) : 0.0d) + (ks != k5 + 1 ? FastMath.abs(e2[ks - 1]) : 0.0d)))) {
                            ks--;
                        } else {
                            this.singularValues[ks] = 0.0d;
                        }
                    }
                }
                if (ks == k5) {
                    kase = 3;
                } else if (ks == p2 - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k5 = ks;
                }
            }
            int k6 = k5 + 1;
            switch (kase) {
                case 1:
                    double f2 = e2[p2 - 2];
                    e2[p2 - 2] = 0.0d;
                    for (int j8 = p2 - 2; j8 >= k6; j8--) {
                        double t9 = FastMath.hypot(this.singularValues[j8], f2);
                        double cs = this.singularValues[j8] / t9;
                        double sn = f2 / t9;
                        this.singularValues[j8] = t9;
                        if (j8 != k6) {
                            f2 = (-sn) * e2[j8 - 1];
                            e2[j8 - 1] = cs * e2[j8 - 1];
                        }
                        for (int i31 = 0; i31 < this.f13035n; i31++) {
                            double t10 = (cs * V2[i31][j8]) + (sn * V2[i31][p2 - 1]);
                            V2[i31][p2 - 1] = ((-sn) * V2[i31][j8]) + (cs * V2[i31][p2 - 1]);
                            V2[i31][j8] = t10;
                        }
                    }
                    break;
                case 2:
                    double f3 = e2[k6 - 1];
                    e2[k6 - 1] = 0.0d;
                    for (int j9 = k6; j9 < p2; j9++) {
                        double t11 = FastMath.hypot(this.singularValues[j9], f3);
                        double cs2 = this.singularValues[j9] / t11;
                        double sn2 = f3 / t11;
                        this.singularValues[j9] = t11;
                        f3 = (-sn2) * e2[j9];
                        e2[j9] = cs2 * e2[j9];
                        for (int i32 = 0; i32 < this.f13034m; i32++) {
                            double t12 = (cs2 * U2[i32][j9]) + (sn2 * U2[i32][k6 - 1]);
                            U2[i32][k6 - 1] = ((-sn2) * U2[i32][j9]) + (cs2 * U2[i32][k6 - 1]);
                            U2[i32][j9] = t12;
                        }
                    }
                    break;
                case 3:
                    double maxPm1Pm2 = FastMath.max(FastMath.abs(this.singularValues[p2 - 1]), FastMath.abs(this.singularValues[p2 - 2]));
                    double scale = FastMath.max(FastMath.max(FastMath.max(maxPm1Pm2, FastMath.abs(e2[p2 - 2])), FastMath.abs(this.singularValues[k6])), FastMath.abs(e2[k6]));
                    double sp = this.singularValues[p2 - 1] / scale;
                    double spm1 = this.singularValues[p2 - 2] / scale;
                    double epm1 = e2[p2 - 2] / scale;
                    double sk = this.singularValues[k6] / scale;
                    double ek = e2[k6] / scale;
                    double b2 = (((spm1 + sp) * (spm1 - sp)) + (epm1 * epm1)) / 2.0d;
                    double c2 = sp * epm1 * sp * epm1;
                    double shift = 0.0d;
                    if (b2 != 0.0d || c2 != 0.0d) {
                        double shift2 = FastMath.sqrt((b2 * b2) + c2);
                        shift = c2 / (b2 + (b2 < 0.0d ? -shift2 : shift2));
                    }
                    double f4 = ((sk + sp) * (sk - sp)) + shift;
                    double g2 = sk * ek;
                    for (int j10 = k6; j10 < p2 - 1; j10++) {
                        double t13 = FastMath.hypot(f4, g2);
                        double cs3 = f4 / t13;
                        double sn3 = g2 / t13;
                        if (j10 != k6) {
                            e2[j10 - 1] = t13;
                        }
                        double f5 = (cs3 * this.singularValues[j10]) + (sn3 * e2[j10]);
                        e2[j10] = (cs3 * e2[j10]) - (sn3 * this.singularValues[j10]);
                        double g3 = sn3 * this.singularValues[j10 + 1];
                        this.singularValues[j10 + 1] = cs3 * this.singularValues[j10 + 1];
                        for (int i33 = 0; i33 < this.f13035n; i33++) {
                            double t14 = (cs3 * V2[i33][j10]) + (sn3 * V2[i33][j10 + 1]);
                            V2[i33][j10 + 1] = ((-sn3) * V2[i33][j10]) + (cs3 * V2[i33][j10 + 1]);
                            V2[i33][j10] = t14;
                        }
                        double t15 = FastMath.hypot(f5, g3);
                        double cs4 = f5 / t15;
                        double sn4 = g3 / t15;
                        this.singularValues[j10] = t15;
                        f4 = (cs4 * e2[j10]) + (sn4 * this.singularValues[j10 + 1]);
                        this.singularValues[j10 + 1] = ((-sn4) * e2[j10]) + (cs4 * this.singularValues[j10 + 1]);
                        g2 = sn4 * e2[j10 + 1];
                        e2[j10 + 1] = cs4 * e2[j10 + 1];
                        if (j10 < this.f13034m - 1) {
                            for (int i34 = 0; i34 < this.f13034m; i34++) {
                                double t16 = (cs4 * U2[i34][j10]) + (sn4 * U2[i34][j10 + 1]);
                                U2[i34][j10 + 1] = ((-sn4) * U2[i34][j10]) + (cs4 * U2[i34][j10 + 1]);
                                U2[i34][j10] = t16;
                            }
                        }
                    }
                    e2[p2 - 2] = f4;
                    break;
                default:
                    if (this.singularValues[k6] <= 0.0d) {
                        this.singularValues[k6] = this.singularValues[k6] < 0.0d ? -this.singularValues[k6] : 0.0d;
                        for (int i35 = 0; i35 <= pp; i35++) {
                            V2[i35][k6] = -V2[i35][k6];
                        }
                    }
                    while (k6 < pp && this.singularValues[k6] < this.singularValues[k6 + 1]) {
                        double t17 = this.singularValues[k6];
                        this.singularValues[k6] = this.singularValues[k6 + 1];
                        this.singularValues[k6 + 1] = t17;
                        if (k6 < this.f13035n - 1) {
                            for (int i36 = 0; i36 < this.f13035n; i36++) {
                                double t18 = V2[i36][k6 + 1];
                                V2[i36][k6 + 1] = V2[i36][k6];
                                V2[i36][k6] = t18;
                            }
                        }
                        if (k6 < this.f13034m - 1) {
                            for (int i37 = 0; i37 < this.f13034m; i37++) {
                                double t19 = U2[i37][k6 + 1];
                                U2[i37][k6 + 1] = U2[i37][k6];
                                U2[i37][k6] = t19;
                            }
                        }
                        k6++;
                    }
                    p2--;
                    break;
            }
        }
        this.tol = FastMath.max(this.f13034m * this.singularValues[0] * EPS, FastMath.sqrt(Precision.SAFE_MIN));
        if (!this.transposed) {
            this.cachedU = MatrixUtils.createRealMatrix(U2);
            this.cachedV = MatrixUtils.createRealMatrix(V2);
        } else {
            this.cachedU = MatrixUtils.createRealMatrix(V2);
            this.cachedV = MatrixUtils.createRealMatrix(U2);
        }
    }

    public RealMatrix getU() {
        return this.cachedU;
    }

    public RealMatrix getUT() {
        if (this.cachedUt == null) {
            this.cachedUt = getU().transpose();
        }
        return this.cachedUt;
    }

    public RealMatrix getS() {
        if (this.cachedS == null) {
            this.cachedS = MatrixUtils.createRealDiagonalMatrix(this.singularValues);
        }
        return this.cachedS;
    }

    public double[] getSingularValues() {
        return (double[]) this.singularValues.clone();
    }

    public RealMatrix getV() {
        return this.cachedV;
    }

    public RealMatrix getVT() {
        if (this.cachedVt == null) {
            this.cachedVt = getV().transpose();
        }
        return this.cachedVt;
    }

    public RealMatrix getCovariance(double minSingularValue) throws NumberIsTooSmallException, OutOfRangeException {
        int p2 = this.singularValues.length;
        int dimension = 0;
        while (dimension < p2 && this.singularValues[dimension] >= minSingularValue) {
            dimension++;
        }
        if (dimension == 0) {
            throw new NumberIsTooLargeException(LocalizedFormats.TOO_LARGE_CUTOFF_SINGULAR_VALUE, Double.valueOf(minSingularValue), Double.valueOf(this.singularValues[0]), true);
        }
        final double[][] data = new double[dimension][p2];
        getVT().walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor() { // from class: org.apache.commons.math3.linear.SingularValueDecomposition.1
            @Override // org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor, org.apache.commons.math3.linear.RealMatrixPreservingVisitor
            public void visit(int row, int column, double value) {
                data[row][column] = value / SingularValueDecomposition.this.singularValues[row];
            }
        }, 0, dimension - 1, 0, p2 - 1);
        RealMatrix jv = new Array2DRowRealMatrix(data, false);
        return jv.transpose().multiply(jv);
    }

    public double getNorm() {
        return this.singularValues[0];
    }

    public double getConditionNumber() {
        return this.singularValues[0] / this.singularValues[this.f13035n - 1];
    }

    public double getInverseConditionNumber() {
        return this.singularValues[this.f13035n - 1] / this.singularValues[0];
    }

    public int getRank() {
        int r2 = 0;
        for (int i2 = 0; i2 < this.singularValues.length; i2++) {
            if (this.singularValues[i2] > this.tol) {
                r2++;
            }
        }
        return r2;
    }

    public DecompositionSolver getSolver() {
        return new Solver(this.singularValues, getUT(), getV(), getRank() == this.f13034m, this.tol);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SingularValueDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private final RealMatrix pseudoInverse;
        private boolean nonSingular;

        private Solver(double[] singularValues, RealMatrix uT, RealMatrix v2, boolean nonSingular, double tol) {
            double a2;
            double[][] suT = uT.getData();
            for (int i2 = 0; i2 < singularValues.length; i2++) {
                if (singularValues[i2] > tol) {
                    a2 = 1.0d / singularValues[i2];
                } else {
                    a2 = 0.0d;
                }
                double[] suTi = suT[i2];
                for (int j2 = 0; j2 < suTi.length; j2++) {
                    int i3 = j2;
                    suTi[i3] = suTi[i3] * a2;
                }
            }
            this.pseudoInverse = v2.multiply(new Array2DRowRealMatrix(suT, false));
            this.nonSingular = nonSingular;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            return this.pseudoInverse.operate(b2);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) {
            return this.pseudoInverse.multiply(b2);
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            return this.nonSingular;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            return this.pseudoInverse;
        }
    }
}
