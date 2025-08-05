package org.apache.commons.math3.optim.nonlinear.vector.jacobian;

import java.util.Arrays;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/vector/jacobian/LevenbergMarquardtOptimizer.class */
public class LevenbergMarquardtOptimizer extends AbstractLeastSquaresOptimizer {
    private static final double TWO_EPS = 2.0d * Precision.EPSILON;
    private int solvedCols;
    private double[] diagR;
    private double[] jacNorm;
    private double[] beta;
    private int[] permutation;
    private int rank;
    private double lmPar;
    private double[] lmDir;
    private final double initialStepBoundFactor;
    private final double costRelativeTolerance;
    private final double parRelativeTolerance;
    private final double orthoTolerance;
    private final double qrRankingThreshold;
    private double[] weightedResidual;
    private double[][] weightedJacobian;

    public LevenbergMarquardtOptimizer() {
        this(100.0d, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(100.0d, checker, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor, ConvergenceChecker<PointVectorValuePair> checker, double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance, double threshold) {
        super(checker);
        this.initialStepBoundFactor = initialStepBoundFactor;
        this.costRelativeTolerance = costRelativeTolerance;
        this.parRelativeTolerance = parRelativeTolerance;
        this.orthoTolerance = orthoTolerance;
        this.qrRankingThreshold = threshold;
    }

    public LevenbergMarquardtOptimizer(double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance) {
        this(100.0d, costRelativeTolerance, parRelativeTolerance, orthoTolerance, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor, double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance, double threshold) {
        super(null);
        this.initialStepBoundFactor = initialStepBoundFactor;
        this.costRelativeTolerance = costRelativeTolerance;
        this.parRelativeTolerance = parRelativeTolerance;
        this.orthoTolerance = orthoTolerance;
        this.qrRankingThreshold = threshold;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0539, code lost:
    
        setCost(r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0541, code lost:
    
        return r28;
     */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.commons.math3.optim.PointVectorValuePair doOptimize() throws org.apache.commons.math3.exception.ConvergenceException {
        /*
            Method dump skipped, instructions count: 1478
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer.doOptimize():org.apache.commons.math3.optim.PointVectorValuePair");
    }

    private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3) {
        int nC = this.weightedJacobian[0].length;
        for (int j2 = 0; j2 < this.rank; j2++) {
            this.lmDir[this.permutation[j2]] = qy[j2];
        }
        for (int j3 = this.rank; j3 < nC; j3++) {
            this.lmDir[this.permutation[j3]] = 0.0d;
        }
        for (int k2 = this.rank - 1; k2 >= 0; k2--) {
            int pk = this.permutation[k2];
            double ypk = this.lmDir[pk] / this.diagR[pk];
            for (int i2 = 0; i2 < k2; i2++) {
                double[] dArr = this.lmDir;
                int i3 = this.permutation[i2];
                dArr[i3] = dArr[i3] - (ypk * this.weightedJacobian[i2][pk]);
            }
            this.lmDir[pk] = ypk;
        }
        double dxNorm = 0.0d;
        for (int j4 = 0; j4 < this.solvedCols; j4++) {
            int pj = this.permutation[j4];
            double s2 = diag[pj] * this.lmDir[pj];
            work1[pj] = s2;
            dxNorm += s2 * s2;
        }
        double dxNorm2 = FastMath.sqrt(dxNorm);
        double fp = dxNorm2 - delta;
        if (fp <= 0.1d * delta) {
            this.lmPar = 0.0d;
            return;
        }
        double parl = 0.0d;
        if (this.rank == this.solvedCols) {
            for (int j5 = 0; j5 < this.solvedCols; j5++) {
                int pj2 = this.permutation[j5];
                work1[pj2] = work1[pj2] * (diag[pj2] / dxNorm2);
            }
            double sum2 = 0.0d;
            for (int j6 = 0; j6 < this.solvedCols; j6++) {
                int pj3 = this.permutation[j6];
                double sum = 0.0d;
                for (int i4 = 0; i4 < j6; i4++) {
                    sum += this.weightedJacobian[i4][pj3] * work1[this.permutation[i4]];
                }
                double s3 = (work1[pj3] - sum) / this.diagR[pj3];
                work1[pj3] = s3;
                sum2 += s3 * s3;
            }
            parl = fp / (delta * sum2);
        }
        double sum22 = 0.0d;
        for (int j7 = 0; j7 < this.solvedCols; j7++) {
            int pj4 = this.permutation[j7];
            double sum3 = 0.0d;
            for (int i5 = 0; i5 <= j7; i5++) {
                sum3 += this.weightedJacobian[i5][pj4] * qy[i5];
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
        }
        double gNorm = FastMath.sqrt(sum22);
        double paru = gNorm / delta;
        if (paru == 0.0d) {
            paru = Precision.SAFE_MIN / FastMath.min(delta, 0.1d);
        }
        this.lmPar = FastMath.min(paru, FastMath.max(this.lmPar, parl));
        if (this.lmPar == 0.0d) {
            this.lmPar = gNorm / dxNorm2;
        }
        for (int countdown = 10; countdown >= 0; countdown--) {
            if (this.lmPar == 0.0d) {
                this.lmPar = FastMath.max(Precision.SAFE_MIN, 0.001d * paru);
            }
            double sPar = FastMath.sqrt(this.lmPar);
            for (int j8 = 0; j8 < this.solvedCols; j8++) {
                int pj5 = this.permutation[j8];
                work1[pj5] = sPar * diag[pj5];
            }
            determineLMDirection(qy, work1, work2, work3);
            double dxNorm3 = 0.0d;
            for (int j9 = 0; j9 < this.solvedCols; j9++) {
                int pj6 = this.permutation[j9];
                double s4 = diag[pj6] * this.lmDir[pj6];
                work3[pj6] = s4;
                dxNorm3 += s4 * s4;
            }
            double dxNorm4 = FastMath.sqrt(dxNorm3);
            double previousFP = fp;
            fp = dxNorm4 - delta;
            if (FastMath.abs(fp) <= 0.1d * delta) {
                return;
            }
            if (parl == 0.0d && fp <= previousFP && previousFP < 0.0d) {
                return;
            }
            for (int j10 = 0; j10 < this.solvedCols; j10++) {
                int pj7 = this.permutation[j10];
                work1[pj7] = (work3[pj7] * diag[pj7]) / dxNorm4;
            }
            for (int j11 = 0; j11 < this.solvedCols; j11++) {
                int pj8 = this.permutation[j11];
                work1[pj8] = work1[pj8] / work2[j11];
                double tmp = work1[pj8];
                for (int i6 = j11 + 1; i6 < this.solvedCols; i6++) {
                    int i7 = this.permutation[i6];
                    work1[i7] = work1[i7] - (this.weightedJacobian[i6][pj8] * tmp);
                }
            }
            double sum23 = 0.0d;
            for (int j12 = 0; j12 < this.solvedCols; j12++) {
                double s5 = work1[this.permutation[j12]];
                sum23 += s5 * s5;
            }
            double correction = fp / (delta * sum23);
            if (fp > 0.0d) {
                parl = FastMath.max(parl, this.lmPar);
            } else if (fp < 0.0d) {
                paru = FastMath.min(paru, this.lmPar);
            }
            this.lmPar = FastMath.max(parl, this.lmPar + correction);
        }
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, double[] work) {
        double cos;
        double sin;
        for (int j2 = 0; j2 < this.solvedCols; j2++) {
            int pj = this.permutation[j2];
            for (int i2 = j2 + 1; i2 < this.solvedCols; i2++) {
                this.weightedJacobian[i2][pj] = this.weightedJacobian[j2][this.permutation[i2]];
            }
            this.lmDir[j2] = this.diagR[pj];
            work[j2] = qy[j2];
        }
        for (int j3 = 0; j3 < this.solvedCols; j3++) {
            int pj2 = this.permutation[j3];
            double dpj = diag[pj2];
            if (dpj != 0.0d) {
                Arrays.fill(lmDiag, j3 + 1, lmDiag.length, 0.0d);
            }
            lmDiag[j3] = dpj;
            double qtbpj = 0.0d;
            for (int k2 = j3; k2 < this.solvedCols; k2++) {
                int pk = this.permutation[k2];
                if (lmDiag[k2] != 0.0d) {
                    double rkk = this.weightedJacobian[k2][pk];
                    if (FastMath.abs(rkk) < FastMath.abs(lmDiag[k2])) {
                        double cotan = rkk / lmDiag[k2];
                        sin = 1.0d / FastMath.sqrt(1.0d + (cotan * cotan));
                        cos = sin * cotan;
                    } else {
                        double tan = lmDiag[k2] / rkk;
                        cos = 1.0d / FastMath.sqrt(1.0d + (tan * tan));
                        sin = cos * tan;
                    }
                    this.weightedJacobian[k2][pk] = (cos * rkk) + (sin * lmDiag[k2]);
                    double temp = (cos * work[k2]) + (sin * qtbpj);
                    qtbpj = ((-sin) * work[k2]) + (cos * qtbpj);
                    work[k2] = temp;
                    for (int i3 = k2 + 1; i3 < this.solvedCols; i3++) {
                        double rik = this.weightedJacobian[i3][pk];
                        double temp2 = (cos * rik) + (sin * lmDiag[i3]);
                        lmDiag[i3] = ((-sin) * rik) + (cos * lmDiag[i3]);
                        this.weightedJacobian[i3][pk] = temp2;
                    }
                }
            }
            lmDiag[j3] = this.weightedJacobian[j3][this.permutation[j3]];
            this.weightedJacobian[j3][this.permutation[j3]] = this.lmDir[j3];
        }
        int nSing = this.solvedCols;
        for (int j4 = 0; j4 < this.solvedCols; j4++) {
            if (lmDiag[j4] == 0.0d && nSing == this.solvedCols) {
                nSing = j4;
            }
            if (nSing < this.solvedCols) {
                work[j4] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j5 = nSing - 1; j5 >= 0; j5--) {
                int pj3 = this.permutation[j5];
                double sum = 0.0d;
                for (int i4 = j5 + 1; i4 < nSing; i4++) {
                    sum += this.weightedJacobian[i4][pj3] * work[i4];
                }
                work[j5] = (work[j5] - sum) / lmDiag[j5];
            }
        }
        for (int j6 = 0; j6 < this.lmDir.length; j6++) {
            this.lmDir[this.permutation[j6]] = work[j6];
        }
    }

    private void qrDecomposition(RealMatrix jacobian) throws ConvergenceException {
        this.weightedJacobian = jacobian.scalarMultiply(-1.0d).getData();
        int nR = this.weightedJacobian.length;
        int nC = this.weightedJacobian[0].length;
        for (int k2 = 0; k2 < nC; k2++) {
            this.permutation[k2] = k2;
            double norm2 = 0.0d;
            for (int i2 = 0; i2 < nR; i2++) {
                double akk = this.weightedJacobian[i2][k2];
                norm2 += akk * akk;
            }
            this.jacNorm[k2] = FastMath.sqrt(norm2);
        }
        for (int k3 = 0; k3 < nC; k3++) {
            int nextColumn = -1;
            double ak2 = Double.NEGATIVE_INFINITY;
            for (int i3 = k3; i3 < nC; i3++) {
                double norm22 = 0.0d;
                for (int j2 = k3; j2 < nR; j2++) {
                    double aki = this.weightedJacobian[j2][this.permutation[i3]];
                    norm22 += aki * aki;
                }
                if (!Double.isInfinite(norm22) && !Double.isNaN(norm22)) {
                    if (norm22 > ak2) {
                        nextColumn = i3;
                        ak2 = norm22;
                    }
                } else {
                    throw new ConvergenceException(LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, Integer.valueOf(nR), Integer.valueOf(nC));
                }
            }
            if (ak2 <= this.qrRankingThreshold) {
                this.rank = k3;
                return;
            }
            int pk = this.permutation[nextColumn];
            this.permutation[nextColumn] = this.permutation[k3];
            this.permutation[k3] = pk;
            double akk2 = this.weightedJacobian[k3][pk];
            double alpha = akk2 > 0.0d ? -FastMath.sqrt(ak2) : FastMath.sqrt(ak2);
            double betak = 1.0d / (ak2 - (akk2 * alpha));
            this.beta[pk] = betak;
            this.diagR[pk] = alpha;
            double[] dArr = this.weightedJacobian[k3];
            dArr[pk] = dArr[pk] - alpha;
            for (int dk = (nC - 1) - k3; dk > 0; dk--) {
                double gamma = 0.0d;
                for (int j3 = k3; j3 < nR; j3++) {
                    gamma += this.weightedJacobian[j3][pk] * this.weightedJacobian[j3][this.permutation[k3 + dk]];
                }
                double gamma2 = gamma * betak;
                for (int j4 = k3; j4 < nR; j4++) {
                    double[] dArr2 = this.weightedJacobian[j4];
                    int i4 = this.permutation[k3 + dk];
                    dArr2[i4] = dArr2[i4] - (gamma2 * this.weightedJacobian[j4][pk]);
                }
            }
        }
        this.rank = this.solvedCols;
    }

    private void qTy(double[] y2) {
        int nR = this.weightedJacobian.length;
        int nC = this.weightedJacobian[0].length;
        for (int k2 = 0; k2 < nC; k2++) {
            int pk = this.permutation[k2];
            double gamma = 0.0d;
            for (int i2 = k2; i2 < nR; i2++) {
                gamma += this.weightedJacobian[i2][pk] * y2[i2];
            }
            double gamma2 = gamma * this.beta[pk];
            for (int i3 = k2; i3 < nR; i3++) {
                int i4 = i3;
                y2[i4] = y2[i4] - (gamma2 * this.weightedJacobian[i3][pk]);
            }
        }
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}
