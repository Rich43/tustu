package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/HessenbergTransformer.class */
class HessenbergTransformer {
    private final double[][] householderVectors;
    private final double[] ort;
    private RealMatrix cachedP;
    private RealMatrix cachedPt;
    private RealMatrix cachedH;

    HessenbergTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m2 = matrix.getRowDimension();
        this.householderVectors = matrix.getData();
        this.ort = new double[m2];
        this.cachedP = null;
        this.cachedPt = null;
        this.cachedH = null;
        transform();
    }

    public RealMatrix getP() {
        if (this.cachedP == null) {
            int n2 = this.householderVectors.length;
            int high = n2 - 1;
            double[][] pa = new double[n2][n2];
            int i2 = 0;
            while (i2 < n2) {
                int j2 = 0;
                while (j2 < n2) {
                    pa[i2][j2] = i2 == j2 ? 1.0d : 0.0d;
                    j2++;
                }
                i2++;
            }
            for (int m2 = high - 1; m2 >= 1; m2--) {
                if (this.householderVectors[m2][m2 - 1] != 0.0d) {
                    for (int i3 = m2 + 1; i3 <= high; i3++) {
                        this.ort[i3] = this.householderVectors[i3][m2 - 1];
                    }
                    for (int j3 = m2; j3 <= high; j3++) {
                        double g2 = 0.0d;
                        for (int i4 = m2; i4 <= high; i4++) {
                            g2 += this.ort[i4] * pa[i4][j3];
                        }
                        double g3 = (g2 / this.ort[m2]) / this.householderVectors[m2][m2 - 1];
                        for (int i5 = m2; i5 <= high; i5++) {
                            double[] dArr = pa[i5];
                            int i6 = j3;
                            dArr[i6] = dArr[i6] + (g3 * this.ort[i5]);
                        }
                    }
                }
            }
            this.cachedP = MatrixUtils.createRealMatrix(pa);
        }
        return this.cachedP;
    }

    public RealMatrix getPT() {
        if (this.cachedPt == null) {
            this.cachedPt = getP().transpose();
        }
        return this.cachedPt;
    }

    public RealMatrix getH() {
        if (this.cachedH == null) {
            int m2 = this.householderVectors.length;
            double[][] h2 = new double[m2][m2];
            for (int i2 = 0; i2 < m2; i2++) {
                if (i2 > 0) {
                    h2[i2][i2 - 1] = this.householderVectors[i2][i2 - 1];
                }
                for (int j2 = i2; j2 < m2; j2++) {
                    h2[i2][j2] = this.householderVectors[i2][j2];
                }
            }
            this.cachedH = MatrixUtils.createRealMatrix(h2);
        }
        return this.cachedH;
    }

    double[][] getHouseholderVectorsRef() {
        return this.householderVectors;
    }

    private void transform() {
        int n2 = this.householderVectors.length;
        int high = n2 - 1;
        for (int m2 = 1; m2 <= high - 1; m2++) {
            double scale = 0.0d;
            for (int i2 = m2; i2 <= high; i2++) {
                scale += FastMath.abs(this.householderVectors[i2][m2 - 1]);
            }
            if (!Precision.equals(scale, 0.0d)) {
                double h2 = 0.0d;
                for (int i3 = high; i3 >= m2; i3--) {
                    this.ort[i3] = this.householderVectors[i3][m2 - 1] / scale;
                    h2 += this.ort[i3] * this.ort[i3];
                }
                double g2 = this.ort[m2] > 0.0d ? -FastMath.sqrt(h2) : FastMath.sqrt(h2);
                double h3 = h2 - (this.ort[m2] * g2);
                double[] dArr = this.ort;
                int i4 = m2;
                dArr[i4] = dArr[i4] - g2;
                for (int j2 = m2; j2 < n2; j2++) {
                    double f2 = 0.0d;
                    for (int i5 = high; i5 >= m2; i5--) {
                        f2 += this.ort[i5] * this.householderVectors[i5][j2];
                    }
                    double f3 = f2 / h3;
                    for (int i6 = m2; i6 <= high; i6++) {
                        double[] dArr2 = this.householderVectors[i6];
                        int i7 = j2;
                        dArr2[i7] = dArr2[i7] - (f3 * this.ort[i6]);
                    }
                }
                for (int i8 = 0; i8 <= high; i8++) {
                    double f4 = 0.0d;
                    for (int j3 = high; j3 >= m2; j3--) {
                        f4 += this.ort[j3] * this.householderVectors[i8][j3];
                    }
                    double f5 = f4 / h3;
                    for (int j4 = m2; j4 <= high; j4++) {
                        double[] dArr3 = this.householderVectors[i8];
                        int i9 = j4;
                        dArr3[i9] = dArr3[i9] - (f5 * this.ort[j4]);
                    }
                }
                this.ort[m2] = scale * this.ort[m2];
                this.householderVectors[m2][m2 - 1] = scale * g2;
            }
        }
    }
}
