package org.apache.commons.math3.linear;

import java.util.Arrays;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/TriDiagonalTransformer.class */
class TriDiagonalTransformer {
    private final double[][] householderVectors;
    private final double[] main;
    private final double[] secondary;
    private RealMatrix cachedQ;
    private RealMatrix cachedQt;
    private RealMatrix cachedT;

    TriDiagonalTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m2 = matrix.getRowDimension();
        this.householderVectors = matrix.getData();
        this.main = new double[m2];
        this.secondary = new double[m2 - 1];
        this.cachedQ = null;
        this.cachedQt = null;
        this.cachedT = null;
        transform();
    }

    public RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    public RealMatrix getQT() {
        if (this.cachedQt == null) {
            int m2 = this.householderVectors.length;
            double[][] qta = new double[m2][m2];
            for (int k2 = m2 - 1; k2 >= 1; k2--) {
                double[] hK = this.householderVectors[k2 - 1];
                qta[k2][k2] = 1.0d;
                if (hK[k2] != 0.0d) {
                    double inv = 1.0d / (this.secondary[k2 - 1] * hK[k2]);
                    double beta = 1.0d / this.secondary[k2 - 1];
                    qta[k2][k2] = 1.0d + (beta * hK[k2]);
                    for (int i2 = k2 + 1; i2 < m2; i2++) {
                        qta[k2][i2] = beta * hK[i2];
                    }
                    for (int j2 = k2 + 1; j2 < m2; j2++) {
                        double beta2 = 0.0d;
                        for (int i3 = k2 + 1; i3 < m2; i3++) {
                            beta2 += qta[j2][i3] * hK[i3];
                        }
                        double beta3 = beta2 * inv;
                        qta[j2][k2] = beta3 * hK[k2];
                        for (int i4 = k2 + 1; i4 < m2; i4++) {
                            double[] dArr = qta[j2];
                            int i5 = i4;
                            dArr[i5] = dArr[i5] + (beta3 * hK[i4]);
                        }
                    }
                }
            }
            qta[0][0] = 1.0d;
            this.cachedQt = MatrixUtils.createRealMatrix(qta);
        }
        return this.cachedQt;
    }

    public RealMatrix getT() {
        if (this.cachedT == null) {
            int m2 = this.main.length;
            double[][] ta = new double[m2][m2];
            for (int i2 = 0; i2 < m2; i2++) {
                ta[i2][i2] = this.main[i2];
                if (i2 > 0) {
                    ta[i2][i2 - 1] = this.secondary[i2 - 1];
                }
                if (i2 < this.main.length - 1) {
                    ta[i2][i2 + 1] = this.secondary[i2];
                }
            }
            this.cachedT = MatrixUtils.createRealMatrix(ta);
        }
        return this.cachedT;
    }

    double[][] getHouseholderVectorsRef() {
        return this.householderVectors;
    }

    double[] getMainDiagonalRef() {
        return this.main;
    }

    double[] getSecondaryDiagonalRef() {
        return this.secondary;
    }

    private void transform() {
        int m2 = this.householderVectors.length;
        double[] z2 = new double[m2];
        for (int k2 = 0; k2 < m2 - 1; k2++) {
            double[] hK = this.householderVectors[k2];
            this.main[k2] = hK[k2];
            double xNormSqr = 0.0d;
            for (int j2 = k2 + 1; j2 < m2; j2++) {
                double c2 = hK[j2];
                xNormSqr += c2 * c2;
            }
            double a2 = hK[k2 + 1] > 0.0d ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
            this.secondary[k2] = a2;
            if (a2 != 0.0d) {
                int i2 = k2 + 1;
                hK[i2] = hK[i2] - a2;
                double beta = (-1.0d) / (a2 * hK[k2 + 1]);
                Arrays.fill(z2, k2 + 1, m2, 0.0d);
                for (int i3 = k2 + 1; i3 < m2; i3++) {
                    double[] hI = this.householderVectors[i3];
                    double hKI = hK[i3];
                    double zI = hI[i3] * hKI;
                    for (int j3 = i3 + 1; j3 < m2; j3++) {
                        double hIJ = hI[j3];
                        zI += hIJ * hK[j3];
                        int i4 = j3;
                        z2[i4] = z2[i4] + (hIJ * hKI);
                    }
                    z2[i3] = beta * (z2[i3] + zI);
                }
                double gamma = 0.0d;
                for (int i5 = k2 + 1; i5 < m2; i5++) {
                    gamma += z2[i5] * hK[i5];
                }
                double gamma2 = gamma * (beta / 2.0d);
                for (int i6 = k2 + 1; i6 < m2; i6++) {
                    int i7 = i6;
                    z2[i7] = z2[i7] - (gamma2 * hK[i6]);
                }
                for (int i8 = k2 + 1; i8 < m2; i8++) {
                    double[] hI2 = this.householderVectors[i8];
                    for (int j4 = i8; j4 < m2; j4++) {
                        int i9 = j4;
                        hI2[i9] = hI2[i9] - ((hK[i8] * z2[j4]) + (z2[i8] * hK[j4]));
                    }
                }
            }
        }
        this.main[m2 - 1] = this.householderVectors[m2 - 1][m2 - 1];
    }
}
