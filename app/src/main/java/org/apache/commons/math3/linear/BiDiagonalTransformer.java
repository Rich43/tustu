package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/BiDiagonalTransformer.class */
class BiDiagonalTransformer {
    private final double[][] householderVectors;
    private final double[] main;
    private final double[] secondary;
    private RealMatrix cachedU;
    private RealMatrix cachedB;
    private RealMatrix cachedV;

    BiDiagonalTransformer(RealMatrix matrix) {
        int m2 = matrix.getRowDimension();
        int n2 = matrix.getColumnDimension();
        int p2 = FastMath.min(m2, n2);
        this.householderVectors = matrix.getData();
        this.main = new double[p2];
        this.secondary = new double[p2 - 1];
        this.cachedU = null;
        this.cachedB = null;
        this.cachedV = null;
        if (m2 >= n2) {
            transformToUpperBiDiagonal();
        } else {
            transformToLowerBiDiagonal();
        }
    }

    public RealMatrix getU() {
        if (this.cachedU == null) {
            int m2 = this.householderVectors.length;
            int n2 = this.householderVectors[0].length;
            int p2 = this.main.length;
            int diagOffset = m2 >= n2 ? 0 : 1;
            double[] diagonal = m2 >= n2 ? this.main : this.secondary;
            double[][] ua = new double[m2][m2];
            for (int k2 = m2 - 1; k2 >= p2; k2--) {
                ua[k2][k2] = 1.0d;
            }
            for (int k3 = p2 - 1; k3 >= diagOffset; k3--) {
                double[] hK = this.householderVectors[k3];
                ua[k3][k3] = 1.0d;
                if (hK[k3 - diagOffset] != 0.0d) {
                    for (int j2 = k3; j2 < m2; j2++) {
                        double alpha = 0.0d;
                        for (int i2 = k3; i2 < m2; i2++) {
                            alpha -= ua[i2][j2] * this.householderVectors[i2][k3 - diagOffset];
                        }
                        double alpha2 = alpha / (diagonal[k3 - diagOffset] * hK[k3 - diagOffset]);
                        for (int i3 = k3; i3 < m2; i3++) {
                            double[] dArr = ua[i3];
                            int i4 = j2;
                            dArr[i4] = dArr[i4] + ((-alpha2) * this.householderVectors[i3][k3 - diagOffset]);
                        }
                    }
                }
            }
            if (diagOffset > 0) {
                ua[0][0] = 1.0d;
            }
            this.cachedU = MatrixUtils.createRealMatrix(ua);
        }
        return this.cachedU;
    }

    public RealMatrix getB() {
        if (this.cachedB == null) {
            int m2 = this.householderVectors.length;
            int n2 = this.householderVectors[0].length;
            double[][] ba2 = new double[m2][n2];
            for (int i2 = 0; i2 < this.main.length; i2++) {
                ba2[i2][i2] = this.main[i2];
                if (m2 < n2) {
                    if (i2 > 0) {
                        ba2[i2][i2 - 1] = this.secondary[i2 - 1];
                    }
                } else if (i2 < this.main.length - 1) {
                    ba2[i2][i2 + 1] = this.secondary[i2];
                }
            }
            this.cachedB = MatrixUtils.createRealMatrix(ba2);
        }
        return this.cachedB;
    }

    public RealMatrix getV() {
        if (this.cachedV == null) {
            int m2 = this.householderVectors.length;
            int n2 = this.householderVectors[0].length;
            int p2 = this.main.length;
            int diagOffset = m2 >= n2 ? 1 : 0;
            double[] diagonal = m2 >= n2 ? this.secondary : this.main;
            double[][] va = new double[n2][n2];
            for (int k2 = n2 - 1; k2 >= p2; k2--) {
                va[k2][k2] = 1.0d;
            }
            for (int k3 = p2 - 1; k3 >= diagOffset; k3--) {
                double[] hK = this.householderVectors[k3 - diagOffset];
                va[k3][k3] = 1.0d;
                if (hK[k3] != 0.0d) {
                    for (int j2 = k3; j2 < n2; j2++) {
                        double beta = 0.0d;
                        for (int i2 = k3; i2 < n2; i2++) {
                            beta -= va[i2][j2] * hK[i2];
                        }
                        double beta2 = beta / (diagonal[k3 - diagOffset] * hK[k3]);
                        for (int i3 = k3; i3 < n2; i3++) {
                            double[] dArr = va[i3];
                            int i4 = j2;
                            dArr[i4] = dArr[i4] + ((-beta2) * hK[i3]);
                        }
                    }
                }
            }
            if (diagOffset > 0) {
                va[0][0] = 1.0d;
            }
            this.cachedV = MatrixUtils.createRealMatrix(va);
        }
        return this.cachedV;
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

    boolean isUpperBiDiagonal() {
        return this.householderVectors.length >= this.householderVectors[0].length;
    }

    private void transformToUpperBiDiagonal() {
        int m2 = this.householderVectors.length;
        int n2 = this.householderVectors[0].length;
        for (int k2 = 0; k2 < n2; k2++) {
            double xNormSqr = 0.0d;
            for (int i2 = k2; i2 < m2; i2++) {
                double c2 = this.householderVectors[i2][k2];
                xNormSqr += c2 * c2;
            }
            double[] hK = this.householderVectors[k2];
            double a2 = hK[k2] > 0.0d ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
            this.main[k2] = a2;
            if (a2 != 0.0d) {
                int i3 = k2;
                hK[i3] = hK[i3] - a2;
                for (int j2 = k2 + 1; j2 < n2; j2++) {
                    double alpha = 0.0d;
                    for (int i4 = k2; i4 < m2; i4++) {
                        double[] hI = this.householderVectors[i4];
                        alpha -= hI[j2] * hI[k2];
                    }
                    double alpha2 = alpha / (a2 * this.householderVectors[k2][k2]);
                    for (int i5 = k2; i5 < m2; i5++) {
                        double[] hI2 = this.householderVectors[i5];
                        int i6 = j2;
                        hI2[i6] = hI2[i6] - (alpha2 * hI2[k2]);
                    }
                }
            }
            if (k2 < n2 - 1) {
                double xNormSqr2 = 0.0d;
                for (int j3 = k2 + 1; j3 < n2; j3++) {
                    double c3 = hK[j3];
                    xNormSqr2 += c3 * c3;
                }
                double b2 = hK[k2 + 1] > 0.0d ? -FastMath.sqrt(xNormSqr2) : FastMath.sqrt(xNormSqr2);
                this.secondary[k2] = b2;
                if (b2 != 0.0d) {
                    int i7 = k2 + 1;
                    hK[i7] = hK[i7] - b2;
                    for (int i8 = k2 + 1; i8 < m2; i8++) {
                        double[] hI3 = this.householderVectors[i8];
                        double beta = 0.0d;
                        for (int j4 = k2 + 1; j4 < n2; j4++) {
                            beta -= hI3[j4] * hK[j4];
                        }
                        double beta2 = beta / (b2 * hK[k2 + 1]);
                        for (int j5 = k2 + 1; j5 < n2; j5++) {
                            int i9 = j5;
                            hI3[i9] = hI3[i9] - (beta2 * hK[j5]);
                        }
                    }
                }
            }
        }
    }

    private void transformToLowerBiDiagonal() {
        int m2 = this.householderVectors.length;
        int n2 = this.householderVectors[0].length;
        for (int k2 = 0; k2 < m2; k2++) {
            double[] hK = this.householderVectors[k2];
            double xNormSqr = 0.0d;
            for (int j2 = k2; j2 < n2; j2++) {
                double c2 = hK[j2];
                xNormSqr += c2 * c2;
            }
            double a2 = hK[k2] > 0.0d ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
            this.main[k2] = a2;
            if (a2 != 0.0d) {
                int i2 = k2;
                hK[i2] = hK[i2] - a2;
                for (int i3 = k2 + 1; i3 < m2; i3++) {
                    double[] hI = this.householderVectors[i3];
                    double alpha = 0.0d;
                    for (int j3 = k2; j3 < n2; j3++) {
                        alpha -= hI[j3] * hK[j3];
                    }
                    double alpha2 = alpha / (a2 * this.householderVectors[k2][k2]);
                    for (int j4 = k2; j4 < n2; j4++) {
                        int i4 = j4;
                        hI[i4] = hI[i4] - (alpha2 * hK[j4]);
                    }
                }
            }
            if (k2 < m2 - 1) {
                double[] hKp1 = this.householderVectors[k2 + 1];
                double xNormSqr2 = 0.0d;
                for (int i5 = k2 + 1; i5 < m2; i5++) {
                    double c3 = this.householderVectors[i5][k2];
                    xNormSqr2 += c3 * c3;
                }
                double b2 = hKp1[k2] > 0.0d ? -FastMath.sqrt(xNormSqr2) : FastMath.sqrt(xNormSqr2);
                this.secondary[k2] = b2;
                if (b2 != 0.0d) {
                    int i6 = k2;
                    hKp1[i6] = hKp1[i6] - b2;
                    for (int j5 = k2 + 1; j5 < n2; j5++) {
                        double beta = 0.0d;
                        for (int i7 = k2 + 1; i7 < m2; i7++) {
                            double[] hI2 = this.householderVectors[i7];
                            beta -= hI2[j5] * hI2[k2];
                        }
                        double beta2 = beta / (b2 * hKp1[k2]);
                        for (int i8 = k2 + 1; i8 < m2; i8++) {
                            double[] hI3 = this.householderVectors[i8];
                            int i9 = j5;
                            hI3[i9] = hI3[i9] - (beta2 * hI3[k2]);
                        }
                    }
                }
            }
        }
    }
}
