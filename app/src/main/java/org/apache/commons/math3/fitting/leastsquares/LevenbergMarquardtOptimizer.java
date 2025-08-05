package org.apache.commons.math3.fitting.leastsquares;

import java.util.Arrays;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LevenbergMarquardtOptimizer.class */
public class LevenbergMarquardtOptimizer implements LeastSquaresOptimizer {
    private static final double TWO_EPS = 2.0d * Precision.EPSILON;
    private final double initialStepBoundFactor;
    private final double costRelativeTolerance;
    private final double parRelativeTolerance;
    private final double orthoTolerance;
    private final double qrRankingThreshold;

    public LevenbergMarquardtOptimizer() {
        this(100.0d, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor, double costRelativeTolerance, double parRelativeTolerance, double orthoTolerance, double qrRankingThreshold) {
        this.initialStepBoundFactor = initialStepBoundFactor;
        this.costRelativeTolerance = costRelativeTolerance;
        this.parRelativeTolerance = parRelativeTolerance;
        this.orthoTolerance = orthoTolerance;
        this.qrRankingThreshold = qrRankingThreshold;
    }

    public LevenbergMarquardtOptimizer withInitialStepBoundFactor(double newInitialStepBoundFactor) {
        return new LevenbergMarquardtOptimizer(newInitialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
    }

    public LevenbergMarquardtOptimizer withCostRelativeTolerance(double newCostRelativeTolerance) {
        return new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, newCostRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
    }

    public LevenbergMarquardtOptimizer withParameterRelativeTolerance(double newParRelativeTolerance) {
        return new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, newParRelativeTolerance, this.orthoTolerance, this.qrRankingThreshold);
    }

    public LevenbergMarquardtOptimizer withOrthoTolerance(double newOrthoTolerance) {
        return new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, newOrthoTolerance, this.qrRankingThreshold);
    }

    public LevenbergMarquardtOptimizer withRankingThreshold(double newQRRankingThreshold) {
        return new LevenbergMarquardtOptimizer(this.initialStepBoundFactor, this.costRelativeTolerance, this.parRelativeTolerance, this.orthoTolerance, newQRRankingThreshold);
    }

    public double getInitialStepBoundFactor() {
        return this.initialStepBoundFactor;
    }

    public double getCostRelativeTolerance() {
        return this.costRelativeTolerance;
    }

    public double getParameterRelativeTolerance() {
        return this.parRelativeTolerance;
    }

    public double getOrthoTolerance() {
        return this.orthoTolerance;
    }

    public double getRankingThreshold() {
        return this.qrRankingThreshold;
    }

    /* JADX WARN: Code restructure failed: missing block: B:122:0x052c, code lost:
    
        return new org.apache.commons.math3.fitting.leastsquares.OptimumImpl(r36, r0.getCount(), r0.getCount());
     */
    @Override // org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum optimize(org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem r15) throws org.apache.commons.math3.exception.ConvergenceException, org.apache.commons.math3.exception.MaxCountExceededException {
        /*
            Method dump skipped, instructions count: 1457
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer.optimize(org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem):org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer$Optimum");
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LevenbergMarquardtOptimizer$InternalData.class */
    private static class InternalData {
        private final double[][] weightedJacobian;
        private final int[] permutation;
        private final int rank;
        private final double[] diagR;
        private final double[] jacNorm;
        private final double[] beta;

        InternalData(double[][] weightedJacobian, int[] permutation, int rank, double[] diagR, double[] jacNorm, double[] beta) {
            this.weightedJacobian = weightedJacobian;
            this.permutation = permutation;
            this.rank = rank;
            this.diagR = diagR;
            this.jacNorm = jacNorm;
            this.beta = beta;
        }
    }

    private double determineLMParameter(double[] qy, double delta, double[] diag, InternalData internalData, int solvedCols, double[] work1, double[] work2, double[] work3, double[] lmDir, double lmPar) {
        double[][] weightedJacobian = internalData.weightedJacobian;
        int[] permutation = internalData.permutation;
        int rank = internalData.rank;
        double[] diagR = internalData.diagR;
        int nC = weightedJacobian[0].length;
        for (int j2 = 0; j2 < rank; j2++) {
            lmDir[permutation[j2]] = qy[j2];
        }
        for (int j3 = rank; j3 < nC; j3++) {
            lmDir[permutation[j3]] = 0.0d;
        }
        for (int k2 = rank - 1; k2 >= 0; k2--) {
            int pk = permutation[k2];
            double ypk = lmDir[pk] / diagR[pk];
            for (int i2 = 0; i2 < k2; i2++) {
                int i3 = permutation[i2];
                lmDir[i3] = lmDir[i3] - (ypk * weightedJacobian[i2][pk]);
            }
            lmDir[pk] = ypk;
        }
        double dxNorm = 0.0d;
        for (int j4 = 0; j4 < solvedCols; j4++) {
            int pj = permutation[j4];
            double s2 = diag[pj] * lmDir[pj];
            work1[pj] = s2;
            dxNorm += s2 * s2;
        }
        double dxNorm2 = FastMath.sqrt(dxNorm);
        double fp = dxNorm2 - delta;
        if (fp <= 0.1d * delta) {
            return 0.0d;
        }
        double parl = 0.0d;
        if (rank == solvedCols) {
            for (int j5 = 0; j5 < solvedCols; j5++) {
                int pj2 = permutation[j5];
                work1[pj2] = work1[pj2] * (diag[pj2] / dxNorm2);
            }
            double sum2 = 0.0d;
            for (int j6 = 0; j6 < solvedCols; j6++) {
                int pj3 = permutation[j6];
                double sum = 0.0d;
                for (int i4 = 0; i4 < j6; i4++) {
                    sum += weightedJacobian[i4][pj3] * work1[permutation[i4]];
                }
                double s3 = (work1[pj3] - sum) / diagR[pj3];
                work1[pj3] = s3;
                sum2 += s3 * s3;
            }
            parl = fp / (delta * sum2);
        }
        double sum22 = 0.0d;
        for (int j7 = 0; j7 < solvedCols; j7++) {
            int pj4 = permutation[j7];
            double sum3 = 0.0d;
            for (int i5 = 0; i5 <= j7; i5++) {
                sum3 += weightedJacobian[i5][pj4] * qy[i5];
            }
            double sum4 = sum3 / diag[pj4];
            sum22 += sum4 * sum4;
        }
        double gNorm = FastMath.sqrt(sum22);
        double paru = gNorm / delta;
        if (paru == 0.0d) {
            paru = Precision.SAFE_MIN / FastMath.min(delta, 0.1d);
        }
        double lmPar2 = FastMath.min(paru, FastMath.max(lmPar, parl));
        if (lmPar2 == 0.0d) {
            lmPar2 = gNorm / dxNorm2;
        }
        for (int countdown = 10; countdown >= 0; countdown--) {
            if (lmPar2 == 0.0d) {
                lmPar2 = FastMath.max(Precision.SAFE_MIN, 0.001d * paru);
            }
            double sPar = FastMath.sqrt(lmPar2);
            for (int j8 = 0; j8 < solvedCols; j8++) {
                int pj5 = permutation[j8];
                work1[pj5] = sPar * diag[pj5];
            }
            determineLMDirection(qy, work1, work2, internalData, solvedCols, work3, lmDir);
            double dxNorm3 = 0.0d;
            for (int j9 = 0; j9 < solvedCols; j9++) {
                int pj6 = permutation[j9];
                double s4 = diag[pj6] * lmDir[pj6];
                work3[pj6] = s4;
                dxNorm3 += s4 * s4;
            }
            double dxNorm4 = FastMath.sqrt(dxNorm3);
            double previousFP = fp;
            fp = dxNorm4 - delta;
            if (FastMath.abs(fp) <= 0.1d * delta || (parl == 0.0d && fp <= previousFP && previousFP < 0.0d)) {
                return lmPar2;
            }
            for (int j10 = 0; j10 < solvedCols; j10++) {
                int pj7 = permutation[j10];
                work1[pj7] = (work3[pj7] * diag[pj7]) / dxNorm4;
            }
            for (int j11 = 0; j11 < solvedCols; j11++) {
                int pj8 = permutation[j11];
                work1[pj8] = work1[pj8] / work2[j11];
                double tmp = work1[pj8];
                for (int i6 = j11 + 1; i6 < solvedCols; i6++) {
                    int i7 = permutation[i6];
                    work1[i7] = work1[i7] - (weightedJacobian[i6][pj8] * tmp);
                }
            }
            double sum23 = 0.0d;
            for (int j12 = 0; j12 < solvedCols; j12++) {
                double s5 = work1[permutation[j12]];
                sum23 += s5 * s5;
            }
            double correction = fp / (delta * sum23);
            if (fp > 0.0d) {
                parl = FastMath.max(parl, lmPar2);
            } else if (fp < 0.0d) {
                paru = FastMath.min(paru, lmPar2);
            }
            lmPar2 = FastMath.max(parl, lmPar2 + correction);
        }
        return lmPar2;
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, InternalData internalData, int solvedCols, double[] work, double[] lmDir) {
        double cos;
        double sin;
        int[] permutation = internalData.permutation;
        double[][] weightedJacobian = internalData.weightedJacobian;
        double[] diagR = internalData.diagR;
        for (int j2 = 0; j2 < solvedCols; j2++) {
            int pj = permutation[j2];
            for (int i2 = j2 + 1; i2 < solvedCols; i2++) {
                weightedJacobian[i2][pj] = weightedJacobian[j2][permutation[i2]];
            }
            lmDir[j2] = diagR[pj];
            work[j2] = qy[j2];
        }
        for (int j3 = 0; j3 < solvedCols; j3++) {
            int pj2 = permutation[j3];
            double dpj = diag[pj2];
            if (dpj != 0.0d) {
                Arrays.fill(lmDiag, j3 + 1, lmDiag.length, 0.0d);
            }
            lmDiag[j3] = dpj;
            double qtbpj = 0.0d;
            for (int k2 = j3; k2 < solvedCols; k2++) {
                int pk = permutation[k2];
                if (lmDiag[k2] != 0.0d) {
                    double rkk = weightedJacobian[k2][pk];
                    if (FastMath.abs(rkk) < FastMath.abs(lmDiag[k2])) {
                        double cotan = rkk / lmDiag[k2];
                        sin = 1.0d / FastMath.sqrt(1.0d + (cotan * cotan));
                        cos = sin * cotan;
                    } else {
                        double tan = lmDiag[k2] / rkk;
                        cos = 1.0d / FastMath.sqrt(1.0d + (tan * tan));
                        sin = cos * tan;
                    }
                    weightedJacobian[k2][pk] = (cos * rkk) + (sin * lmDiag[k2]);
                    double temp = (cos * work[k2]) + (sin * qtbpj);
                    qtbpj = ((-sin) * work[k2]) + (cos * qtbpj);
                    work[k2] = temp;
                    for (int i3 = k2 + 1; i3 < solvedCols; i3++) {
                        double rik = weightedJacobian[i3][pk];
                        double temp2 = (cos * rik) + (sin * lmDiag[i3]);
                        lmDiag[i3] = ((-sin) * rik) + (cos * lmDiag[i3]);
                        weightedJacobian[i3][pk] = temp2;
                    }
                }
            }
            lmDiag[j3] = weightedJacobian[j3][permutation[j3]];
            weightedJacobian[j3][permutation[j3]] = lmDir[j3];
        }
        int nSing = solvedCols;
        for (int j4 = 0; j4 < solvedCols; j4++) {
            if (lmDiag[j4] == 0.0d && nSing == solvedCols) {
                nSing = j4;
            }
            if (nSing < solvedCols) {
                work[j4] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j5 = nSing - 1; j5 >= 0; j5--) {
                int pj3 = permutation[j5];
                double sum = 0.0d;
                for (int i4 = j5 + 1; i4 < nSing; i4++) {
                    sum += weightedJacobian[i4][pj3] * work[i4];
                }
                work[j5] = (work[j5] - sum) / lmDiag[j5];
            }
        }
        for (int j6 = 0; j6 < lmDir.length; j6++) {
            lmDir[permutation[j6]] = work[j6];
        }
    }

    private InternalData qrDecomposition(RealMatrix jacobian, int solvedCols) throws ConvergenceException {
        double[][] weightedJacobian = jacobian.scalarMultiply(-1.0d).getData();
        int nR = weightedJacobian.length;
        int nC = weightedJacobian[0].length;
        int[] permutation = new int[nC];
        double[] diagR = new double[nC];
        double[] jacNorm = new double[nC];
        double[] beta = new double[nC];
        for (int k2 = 0; k2 < nC; k2++) {
            permutation[k2] = k2;
            double norm2 = 0.0d;
            for (double[] dArr : weightedJacobian) {
                double akk = dArr[k2];
                norm2 += akk * akk;
            }
            jacNorm[k2] = FastMath.sqrt(norm2);
        }
        for (int k3 = 0; k3 < nC; k3++) {
            int nextColumn = -1;
            double ak2 = Double.NEGATIVE_INFINITY;
            for (int i2 = k3; i2 < nC; i2++) {
                double norm22 = 0.0d;
                for (int j2 = k3; j2 < nR; j2++) {
                    double aki = weightedJacobian[j2][permutation[i2]];
                    norm22 += aki * aki;
                }
                if (!Double.isInfinite(norm22) && !Double.isNaN(norm22)) {
                    if (norm22 > ak2) {
                        nextColumn = i2;
                        ak2 = norm22;
                    }
                } else {
                    throw new ConvergenceException(LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN, Integer.valueOf(nR), Integer.valueOf(nC));
                }
            }
            if (ak2 <= this.qrRankingThreshold) {
                return new InternalData(weightedJacobian, permutation, k3, diagR, jacNorm, beta);
            }
            int pk = permutation[nextColumn];
            permutation[nextColumn] = permutation[k3];
            permutation[k3] = pk;
            double akk2 = weightedJacobian[k3][pk];
            double alpha = akk2 > 0.0d ? -FastMath.sqrt(ak2) : FastMath.sqrt(ak2);
            double betak = 1.0d / (ak2 - (akk2 * alpha));
            beta[pk] = betak;
            diagR[pk] = alpha;
            double[] dArr2 = weightedJacobian[k3];
            dArr2[pk] = dArr2[pk] - alpha;
            for (int dk = (nC - 1) - k3; dk > 0; dk--) {
                double gamma = 0.0d;
                for (int j3 = k3; j3 < nR; j3++) {
                    gamma += weightedJacobian[j3][pk] * weightedJacobian[j3][permutation[k3 + dk]];
                }
                double gamma2 = gamma * betak;
                for (int j4 = k3; j4 < nR; j4++) {
                    double[] dArr3 = weightedJacobian[j4];
                    int i3 = permutation[k3 + dk];
                    dArr3[i3] = dArr3[i3] - (gamma2 * weightedJacobian[j4][pk]);
                }
            }
        }
        return new InternalData(weightedJacobian, permutation, solvedCols, diagR, jacNorm, beta);
    }

    private void qTy(double[] y2, InternalData internalData) {
        double[][] weightedJacobian = internalData.weightedJacobian;
        int[] permutation = internalData.permutation;
        double[] beta = internalData.beta;
        int nR = weightedJacobian.length;
        int nC = weightedJacobian[0].length;
        for (int k2 = 0; k2 < nC; k2++) {
            int pk = permutation[k2];
            double gamma = 0.0d;
            for (int i2 = k2; i2 < nR; i2++) {
                gamma += weightedJacobian[i2][pk] * y2[i2];
            }
            double gamma2 = gamma * beta[pk];
            for (int i3 = k2; i3 < nR; i3++) {
                int i4 = i3;
                y2[i4] = y2[i4] - (gamma2 * weightedJacobian[i3][pk]);
            }
        }
    }
}
