package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/TricubicSplineInterpolator.class */
public class TricubicSplineInterpolator implements TrivariateGridInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator
    public TricubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[] zval, double[][][] fval) throws NumberIsTooSmallException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval.length == 0 || yval.length == 0 || zval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != fval.length) {
            throw new DimensionMismatchException(xval.length, fval.length);
        }
        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        MathArrays.checkOrder(zval);
        int xLen = xval.length;
        int yLen = yval.length;
        int zLen = zval.length;
        double[][][] fvalXY = new double[zLen][xLen][yLen];
        double[][][] fvalZX = new double[yLen][zLen][xLen];
        for (int i2 = 0; i2 < xLen; i2++) {
            if (fval[i2].length != yLen) {
                throw new DimensionMismatchException(fval[i2].length, yLen);
            }
            for (int j2 = 0; j2 < yLen; j2++) {
                if (fval[i2][j2].length != zLen) {
                    throw new DimensionMismatchException(fval[i2][j2].length, zLen);
                }
                for (int k2 = 0; k2 < zLen; k2++) {
                    double v2 = fval[i2][j2][k2];
                    fvalXY[k2][i2][j2] = v2;
                    fvalZX[j2][k2][i2] = v2;
                }
            }
        }
        BicubicSplineInterpolator bsi = new BicubicSplineInterpolator(true);
        BicubicSplineInterpolatingFunction[] xSplineYZ = new BicubicSplineInterpolatingFunction[xLen];
        for (int i3 = 0; i3 < xLen; i3++) {
            xSplineYZ[i3] = bsi.interpolate(yval, zval, fval[i3]);
        }
        BicubicSplineInterpolatingFunction[] ySplineZX = new BicubicSplineInterpolatingFunction[yLen];
        for (int j3 = 0; j3 < yLen; j3++) {
            ySplineZX[j3] = bsi.interpolate(zval, xval, fvalZX[j3]);
        }
        BicubicSplineInterpolatingFunction[] zSplineXY = new BicubicSplineInterpolatingFunction[zLen];
        for (int k3 = 0; k3 < zLen; k3++) {
            zSplineXY[k3] = bsi.interpolate(xval, yval, fvalXY[k3]);
        }
        double[][][] dFdX = new double[xLen][yLen][zLen];
        double[][][] dFdY = new double[xLen][yLen][zLen];
        double[][][] d2FdXdY = new double[xLen][yLen][zLen];
        for (int k4 = 0; k4 < zLen; k4++) {
            BicubicSplineInterpolatingFunction f2 = zSplineXY[k4];
            for (int i4 = 0; i4 < xLen; i4++) {
                double x2 = xval[i4];
                for (int j4 = 0; j4 < yLen; j4++) {
                    double y2 = yval[j4];
                    dFdX[i4][j4][k4] = f2.partialDerivativeX(x2, y2);
                    dFdY[i4][j4][k4] = f2.partialDerivativeY(x2, y2);
                    d2FdXdY[i4][j4][k4] = f2.partialDerivativeXY(x2, y2);
                }
            }
        }
        double[][][] dFdZ = new double[xLen][yLen][zLen];
        double[][][] d2FdYdZ = new double[xLen][yLen][zLen];
        for (int i5 = 0; i5 < xLen; i5++) {
            BicubicSplineInterpolatingFunction f3 = xSplineYZ[i5];
            for (int j5 = 0; j5 < yLen; j5++) {
                double y3 = yval[j5];
                for (int k5 = 0; k5 < zLen; k5++) {
                    double z2 = zval[k5];
                    dFdZ[i5][j5][k5] = f3.partialDerivativeY(y3, z2);
                    d2FdYdZ[i5][j5][k5] = f3.partialDerivativeXY(y3, z2);
                }
            }
        }
        double[][][] d2FdZdX = new double[xLen][yLen][zLen];
        for (int j6 = 0; j6 < yLen; j6++) {
            BicubicSplineInterpolatingFunction f4 = ySplineZX[j6];
            for (int k6 = 0; k6 < zLen; k6++) {
                double z3 = zval[k6];
                for (int i6 = 0; i6 < xLen; i6++) {
                    d2FdZdX[i6][j6][k6] = f4.partialDerivativeXY(z3, xval[i6]);
                }
            }
        }
        double[][][] d3FdXdYdZ = new double[xLen][yLen][zLen];
        for (int i7 = 0; i7 < xLen; i7++) {
            int nI = nextIndex(i7, xLen);
            int pI = previousIndex(i7);
            for (int j7 = 0; j7 < yLen; j7++) {
                int nJ = nextIndex(j7, yLen);
                int pJ = previousIndex(j7);
                for (int k7 = 0; k7 < zLen; k7++) {
                    int nK = nextIndex(k7, zLen);
                    int pK = previousIndex(k7);
                    d3FdXdYdZ[i7][j7][k7] = (((((((fval[nI][nJ][nK] - fval[nI][pJ][nK]) - fval[pI][nJ][nK]) + fval[pI][pJ][nK]) - fval[nI][nJ][pK]) + fval[nI][pJ][pK]) + fval[pI][nJ][pK]) - fval[pI][pJ][pK]) / (((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ])) * (zval[nK] - zval[pK]));
                }
            }
        }
        return new TricubicSplineInterpolatingFunction(xval, yval, zval, fval, dFdX, dFdY, dFdZ, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
    }

    private int nextIndex(int i2, int max) {
        int index = i2 + 1;
        return index < max ? index : index - 1;
    }

    private int previousIndex(int i2) {
        int index = i2 - 1;
        if (index >= 0) {
            return index;
        }
        return 0;
    }
}
