package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SchurTransformer.class */
class SchurTransformer {
    private static final int MAX_ITERATIONS = 100;
    private final double[][] matrixP;
    private final double[][] matrixT;
    private RealMatrix cachedP;
    private RealMatrix cachedT;
    private RealMatrix cachedPt;
    private final double epsilon = Precision.EPSILON;

    SchurTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        HessenbergTransformer transformer = new HessenbergTransformer(matrix);
        this.matrixT = transformer.getH().getData();
        this.matrixP = transformer.getP().getData();
        this.cachedT = null;
        this.cachedP = null;
        this.cachedPt = null;
        transform();
    }

    public RealMatrix getP() {
        if (this.cachedP == null) {
            this.cachedP = MatrixUtils.createRealMatrix(this.matrixP);
        }
        return this.cachedP;
    }

    public RealMatrix getPT() {
        if (this.cachedPt == null) {
            this.cachedPt = getP().transpose();
        }
        return this.cachedPt;
    }

    public RealMatrix getT() {
        if (this.cachedT == null) {
            this.cachedT = MatrixUtils.createRealMatrix(this.matrixT);
        }
        return this.cachedT;
    }

    private void transform() {
        double z2;
        int n2 = this.matrixT.length;
        double norm = getNorm();
        ShiftInfo shift = new ShiftInfo();
        int iteration = 0;
        int iu = n2 - 1;
        while (iu >= 0) {
            int il = findSmallSubDiagonalElement(iu, norm);
            if (il == iu) {
                double[] dArr = this.matrixT[iu];
                int i2 = iu;
                dArr[i2] = dArr[i2] + shift.exShift;
                iu--;
                iteration = 0;
            } else if (il == iu - 1) {
                double p2 = (this.matrixT[iu - 1][iu - 1] - this.matrixT[iu][iu]) / 2.0d;
                double q2 = (p2 * p2) + (this.matrixT[iu][iu - 1] * this.matrixT[iu - 1][iu]);
                double[] dArr2 = this.matrixT[iu];
                int i3 = iu;
                dArr2[i3] = dArr2[i3] + shift.exShift;
                double[] dArr3 = this.matrixT[iu - 1];
                int i4 = iu - 1;
                dArr3[i4] = dArr3[i4] + shift.exShift;
                if (q2 >= 0.0d) {
                    double z3 = FastMath.sqrt(FastMath.abs(q2));
                    if (p2 >= 0.0d) {
                        z2 = p2 + z3;
                    } else {
                        z2 = p2 - z3;
                    }
                    double x2 = this.matrixT[iu][iu - 1];
                    double s2 = FastMath.abs(x2) + FastMath.abs(z2);
                    double p3 = x2 / s2;
                    double q3 = z2 / s2;
                    double r2 = FastMath.sqrt((p3 * p3) + (q3 * q3));
                    double p4 = p3 / r2;
                    double q4 = q3 / r2;
                    for (int j2 = iu - 1; j2 < n2; j2++) {
                        double z4 = this.matrixT[iu - 1][j2];
                        this.matrixT[iu - 1][j2] = (q4 * z4) + (p4 * this.matrixT[iu][j2]);
                        this.matrixT[iu][j2] = (q4 * this.matrixT[iu][j2]) - (p4 * z4);
                    }
                    for (int i5 = 0; i5 <= iu; i5++) {
                        double z5 = this.matrixT[i5][iu - 1];
                        this.matrixT[i5][iu - 1] = (q4 * z5) + (p4 * this.matrixT[i5][iu]);
                        this.matrixT[i5][iu] = (q4 * this.matrixT[i5][iu]) - (p4 * z5);
                    }
                    for (int i6 = 0; i6 <= n2 - 1; i6++) {
                        double z6 = this.matrixP[i6][iu - 1];
                        this.matrixP[i6][iu - 1] = (q4 * z6) + (p4 * this.matrixP[i6][iu]);
                        this.matrixP[i6][iu] = (q4 * this.matrixP[i6][iu]) - (p4 * z6);
                    }
                }
                iu -= 2;
                iteration = 0;
            } else {
                computeShift(il, iu, iteration, shift);
                iteration++;
                if (iteration > 100) {
                    throw new MaxCountExceededException(LocalizedFormats.CONVERGENCE_FAILED, 100, new Object[0]);
                }
                double[] hVec = new double[3];
                int im = initQRStep(il, iu, shift, hVec);
                performDoubleQRStep(il, im, iu, shift, hVec);
            }
        }
    }

    private double getNorm() {
        double norm = 0.0d;
        for (int i2 = 0; i2 < this.matrixT.length; i2++) {
            for (int j2 = FastMath.max(i2 - 1, 0); j2 < this.matrixT.length; j2++) {
                norm += FastMath.abs(this.matrixT[i2][j2]);
            }
        }
        return norm;
    }

    private int findSmallSubDiagonalElement(int startIdx, double norm) {
        int l2 = startIdx;
        while (l2 > 0) {
            double s2 = FastMath.abs(this.matrixT[l2 - 1][l2 - 1]) + FastMath.abs(this.matrixT[l2][l2]);
            if (s2 == 0.0d) {
                s2 = norm;
            }
            if (FastMath.abs(this.matrixT[l2][l2 - 1]) < this.epsilon * s2) {
                break;
            }
            l2--;
        }
        return l2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void computeShift(int l2, int idx, int iteration, ShiftInfo shiftInfo) {
        shiftInfo.f13031x = this.matrixT[idx][idx];
        shiftInfo.f13033w = 0.0d;
        shiftInfo.f13032y = 0.0d;
        if (l2 < idx) {
            shiftInfo.f13032y = this.matrixT[idx - 1][idx - 1];
            shiftInfo.f13033w = this.matrixT[idx][idx - 1] * this.matrixT[idx - 1][idx];
        }
        if (iteration == 10) {
            shiftInfo.exShift += shiftInfo.f13031x;
            for (int i2 = 0; i2 <= idx; i2++) {
                double[] dArr = this.matrixT[i2];
                int i3 = i2;
                dArr[i3] = dArr[i3] - shiftInfo.f13031x;
            }
            double s2 = FastMath.abs(this.matrixT[idx][idx - 1]) + FastMath.abs(this.matrixT[idx - 1][idx - 2]);
            shiftInfo.f13031x = 0.75d * s2;
            shiftInfo.f13032y = 0.75d * s2;
            shiftInfo.f13033w = (-0.4375d) * s2 * s2;
        }
        if (iteration == 30) {
            double s3 = (shiftInfo.f13032y - shiftInfo.f13031x) / 2.0d;
            double s4 = (s3 * s3) + shiftInfo.f13033w;
            if (s4 > 0.0d) {
                double s5 = FastMath.sqrt(s4);
                if (shiftInfo.f13032y < shiftInfo.f13031x) {
                    s5 = -s5;
                }
                double s6 = shiftInfo.f13031x - (shiftInfo.f13033w / (((shiftInfo.f13032y - shiftInfo.f13031x) / 2.0d) + s5));
                for (int i4 = 0; i4 <= idx; i4++) {
                    double[] dArr2 = this.matrixT[i4];
                    int i5 = i4;
                    dArr2[i5] = dArr2[i5] - s6;
                }
                shiftInfo.exShift += s6;
                shiftInfo.f13033w = 0.964d;
                shiftInfo.f13032y = 0.964d;
                4606858159626846732.f13031x = shiftInfo;
            }
        }
    }

    private int initQRStep(int il, int iu, ShiftInfo shift, double[] hVec) {
        int im = iu - 2;
        while (im >= il) {
            double z2 = this.matrixT[im][im];
            double r2 = shift.f13031x - z2;
            double s2 = shift.f13032y - z2;
            hVec[0] = (((r2 * s2) - shift.f13033w) / this.matrixT[im + 1][im]) + this.matrixT[im][im + 1];
            hVec[1] = ((this.matrixT[im + 1][im + 1] - z2) - r2) - s2;
            hVec[2] = this.matrixT[im + 2][im + 1];
            if (im == il) {
                break;
            }
            double lhs = FastMath.abs(this.matrixT[im][im - 1]) * (FastMath.abs(hVec[1]) + FastMath.abs(hVec[2]));
            double rhs = FastMath.abs(hVec[0]) * (FastMath.abs(this.matrixT[im - 1][im - 1]) + FastMath.abs(z2) + FastMath.abs(this.matrixT[im + 1][im + 1]));
            if (lhs < this.epsilon * rhs) {
                break;
            }
            im--;
        }
        return im;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x02eb A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void performDoubleQRStep(int r10, int r11, int r12, org.apache.commons.math3.linear.SchurTransformer.ShiftInfo r13, double[] r14) {
        /*
            Method dump skipped, instructions count: 805
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.linear.SchurTransformer.performDoubleQRStep(int, int, int, org.apache.commons.math3.linear.SchurTransformer$ShiftInfo, double[]):void");
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SchurTransformer$ShiftInfo.class */
    private static class ShiftInfo {

        /* renamed from: x, reason: collision with root package name */
        double f13031x;

        /* renamed from: y, reason: collision with root package name */
        double f13032y;

        /* renamed from: w, reason: collision with root package name */
        double f13033w;
        double exShift;

        private ShiftInfo() {
        }
    }
}
