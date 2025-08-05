package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Power.class */
public class Power implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: p, reason: collision with root package name */
    private final double f12964p;

    public Power(double p2) {
        this.f12964p = p2;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return FastMath.pow(x2, this.f12964p);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) {
        return t2.pow(this.f12964p);
    }
}
