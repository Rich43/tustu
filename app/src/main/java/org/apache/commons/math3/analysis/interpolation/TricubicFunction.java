package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.TrivariateFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

/* compiled from: TricubicInterpolatingFunction.java */
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/TricubicFunction.class */
class TricubicFunction implements TrivariateFunction {

    /* renamed from: N, reason: collision with root package name */
    private static final short f12970N = 4;

    /* renamed from: a, reason: collision with root package name */
    private final double[][][] f12971a = new double[4][4][4];

    TricubicFunction(double[] aV2) {
        for (int i2 = 0; i2 < 4; i2++) {
            for (int j2 = 0; j2 < 4; j2++) {
                for (int k2 = 0; k2 < 4; k2++) {
                    this.f12971a[i2][j2][k2] = aV2[i2 + (4 * (j2 + (4 * k2)))];
                }
            }
        }
    }

    @Override // org.apache.commons.math3.analysis.TrivariateFunction
    public double value(double x2, double y2, double z2) throws OutOfRangeException {
        if (x2 < 0.0d || x2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(x2), 0, 1);
        }
        if (y2 < 0.0d || y2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(y2), 0, 1);
        }
        if (z2 < 0.0d || z2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(z2), 0, 1);
        }
        double x22 = x2 * x2;
        double x3 = x22 * x2;
        double[] pX = {1.0d, x2, x22, x3};
        double y22 = y2 * y2;
        double y3 = y22 * y2;
        double[] pY = {1.0d, y2, y22, y3};
        double z22 = z2 * z2;
        double z3 = z22 * z2;
        double[] pZ = {1.0d, z2, z22, z3};
        double result = 0.0d;
        for (int i2 = 0; i2 < 4; i2++) {
            for (int j2 = 0; j2 < 4; j2++) {
                for (int k2 = 0; k2 < 4; k2++) {
                    result += this.f12971a[i2][j2][k2] * pX[i2] * pY[j2] * pZ[k2];
                }
            }
        }
        return result;
    }
}
