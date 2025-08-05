package org.apache.commons.math3.analysis.differentiation;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/GradientFunction.class */
public class GradientFunction implements MultivariateVectorFunction {

    /* renamed from: f, reason: collision with root package name */
    private final MultivariateDifferentiableFunction f12956f;

    public GradientFunction(MultivariateDifferentiableFunction f2) {
        this.f12956f = f2;
    }

    @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
    public double[] value(double[] point) throws MathIllegalArgumentException {
        DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (int i2 = 0; i2 < point.length; i2++) {
            dsX[i2] = new DerivativeStructure(point.length, 1, i2, point[i2]);
        }
        DerivativeStructure dsY = this.f12956f.value(dsX);
        double[] y2 = new double[point.length];
        int[] orders = new int[point.length];
        for (int i3 = 0; i3 < point.length; i3++) {
            orders[i3] = 1;
            y2[i3] = dsY.getPartialDerivative(orders);
            orders[i3] = 0;
        }
        return y2;
    }
}
