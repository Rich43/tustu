package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/JacobianFunction.class */
public class JacobianFunction implements MultivariateMatrixFunction {

    /* renamed from: f, reason: collision with root package name */
    private final MultivariateDifferentiableVectorFunction f12957f;

    public JacobianFunction(MultivariateDifferentiableVectorFunction f2) {
        this.f12957f = f2;
    }

    @Override // org.apache.commons.math3.analysis.MultivariateMatrixFunction
    public double[][] value(double[] point) throws MathIllegalArgumentException {
        DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (int i2 = 0; i2 < point.length; i2++) {
            dsX[i2] = new DerivativeStructure(point.length, 1, i2, point[i2]);
        }
        DerivativeStructure[] dsY = this.f12957f.value(dsX);
        double[][] y2 = new double[dsY.length][point.length];
        int[] orders = new int[point.length];
        for (int i3 = 0; i3 < dsY.length; i3++) {
            for (int j2 = 0; j2 < point.length; j2++) {
                orders[j2] = 1;
                y2[i3][j2] = dsY[i3].getPartialDerivative(orders);
                orders[j2] = 0;
            }
        }
        return y2;
    }
}
