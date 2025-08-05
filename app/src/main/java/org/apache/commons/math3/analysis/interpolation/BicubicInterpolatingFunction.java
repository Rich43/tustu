package org.apache.commons.math3.analysis.interpolation;

import java.util.Arrays;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BicubicInterpolatingFunction.class */
public class BicubicInterpolatingFunction implements BivariateFunction {
    private static final int NUM_COEFF = 16;
    private static final double[][] AINV = {new double[]{1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{-3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d}, new double[]{-3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d}, new double[]{9.0d, -9.0d, -9.0d, 9.0d, 6.0d, 3.0d, -6.0d, -3.0d, 6.0d, -6.0d, 3.0d, -3.0d, 4.0d, 2.0d, 2.0d, 1.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -3.0d, -3.0d, 3.0d, 3.0d, -4.0d, 4.0d, -2.0d, 2.0d, -2.0d, -2.0d, -1.0d, -1.0d}, new double[]{2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -4.0d, -2.0d, 4.0d, 2.0d, -3.0d, 3.0d, -3.0d, 3.0d, -2.0d, -1.0d, -2.0d, -1.0d}, new double[]{4.0d, -4.0d, -4.0d, 4.0d, 2.0d, 2.0d, -2.0d, -2.0d, 2.0d, -2.0d, 2.0d, -2.0d, 1.0d, 1.0d, 1.0d, 1.0d}};
    private final double[] xval;
    private final double[] yval;
    private final BicubicFunction[][] splines;

    public BicubicInterpolatingFunction(double[] x2, double[] y2, double[][] f2, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY) throws NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        int xLen = x2.length;
        int yLen = y2.length;
        if (xLen == 0 || yLen == 0 || f2.length == 0 || f2[0].length == 0) {
            throw new NoDataException();
        }
        if (xLen != f2.length) {
            throw new DimensionMismatchException(xLen, f2.length);
        }
        if (xLen != dFdX.length) {
            throw new DimensionMismatchException(xLen, dFdX.length);
        }
        if (xLen != dFdY.length) {
            throw new DimensionMismatchException(xLen, dFdY.length);
        }
        if (xLen != d2FdXdY.length) {
            throw new DimensionMismatchException(xLen, d2FdXdY.length);
        }
        MathArrays.checkOrder(x2);
        MathArrays.checkOrder(y2);
        this.xval = (double[]) x2.clone();
        this.yval = (double[]) y2.clone();
        int lastI = xLen - 1;
        int lastJ = yLen - 1;
        this.splines = new BicubicFunction[lastI][lastJ];
        for (int i2 = 0; i2 < lastI; i2++) {
            if (f2[i2].length != yLen) {
                throw new DimensionMismatchException(f2[i2].length, yLen);
            }
            if (dFdX[i2].length != yLen) {
                throw new DimensionMismatchException(dFdX[i2].length, yLen);
            }
            if (dFdY[i2].length != yLen) {
                throw new DimensionMismatchException(dFdY[i2].length, yLen);
            }
            if (d2FdXdY[i2].length != yLen) {
                throw new DimensionMismatchException(d2FdXdY[i2].length, yLen);
            }
            int ip1 = i2 + 1;
            double xR = this.xval[ip1] - this.xval[i2];
            for (int j2 = 0; j2 < lastJ; j2++) {
                int jp1 = j2 + 1;
                double yR = this.yval[jp1] - this.yval[j2];
                double xRyR = xR * yR;
                double[] beta = {f2[i2][j2], f2[ip1][j2], f2[i2][jp1], f2[ip1][jp1], dFdX[i2][j2] * xR, dFdX[ip1][j2] * xR, dFdX[i2][jp1] * xR, dFdX[ip1][jp1] * xR, dFdY[i2][j2] * yR, dFdY[ip1][j2] * yR, dFdY[i2][jp1] * yR, dFdY[ip1][jp1] * yR, d2FdXdY[i2][j2] * xRyR, d2FdXdY[ip1][j2] * xRyR, d2FdXdY[i2][jp1] * xRyR, d2FdXdY[ip1][jp1] * xRyR};
                this.splines[i2][j2] = new BicubicFunction(computeSplineCoefficients(beta));
            }
        }
    }

    @Override // org.apache.commons.math3.analysis.BivariateFunction
    public double value(double x2, double y2) throws OutOfRangeException {
        int i2 = searchIndex(x2, this.xval);
        int j2 = searchIndex(y2, this.yval);
        double xN = (x2 - this.xval[i2]) / (this.xval[i2 + 1] - this.xval[i2]);
        double yN = (y2 - this.yval[j2]) / (this.yval[j2 + 1] - this.yval[j2]);
        return this.splines[i2][j2].value(xN, yN);
    }

    public boolean isValidPoint(double x2, double y2) {
        if (x2 < this.xval[0] || x2 > this.xval[this.xval.length - 1] || y2 < this.yval[0] || y2 > this.yval[this.yval.length - 1]) {
            return false;
        }
        return true;
    }

    private int searchIndex(double c2, double[] val) {
        int r2 = Arrays.binarySearch(val, c2);
        if (r2 == -1 || r2 == (-val.length) - 1) {
            throw new OutOfRangeException(Double.valueOf(c2), Double.valueOf(val[0]), Double.valueOf(val[val.length - 1]));
        }
        if (r2 < 0) {
            return (-r2) - 2;
        }
        int last = val.length - 1;
        if (r2 == last) {
            return last - 1;
        }
        return r2;
    }

    private double[] computeSplineCoefficients(double[] beta) {
        double[] a2 = new double[16];
        for (int i2 = 0; i2 < 16; i2++) {
            double result = 0.0d;
            double[] row = AINV[i2];
            for (int j2 = 0; j2 < 16; j2++) {
                result += row[j2] * beta[j2];
            }
            a2[i2] = result;
        }
        return a2;
    }
}
