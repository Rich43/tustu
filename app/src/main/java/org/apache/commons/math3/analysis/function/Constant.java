package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Constant.class */
public class Constant implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: c, reason: collision with root package name */
    private final double f12958c;

    public Constant(double c2) {
        this.f12958c = c2;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return this.f12958c;
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public DifferentiableUnivariateFunction derivative() {
        return new Constant(0.0d);
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) {
        return new DerivativeStructure(t2.getFreeParameters(), t2.getOrder(), this.f12958c);
    }
}
