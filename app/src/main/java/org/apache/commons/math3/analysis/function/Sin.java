package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Sin.class */
public class Sin implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return FastMath.sin(x2);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public DifferentiableUnivariateFunction derivative() {
        return new Cos();
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) {
        return t2.sin();
    }
}
