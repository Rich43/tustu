package org.apache.commons.math3.analysis.polynomials;

import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialSplineFunction.class */
public class PolynomialSplineFunction implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double[] knots;
    private final PolynomialFunction[] polynomials;

    /* renamed from: n, reason: collision with root package name */
    private final int f12978n;

    public PolynomialSplineFunction(double[] knots, PolynomialFunction[] polynomials) throws NumberIsTooSmallException, NullArgumentException, NonMonotonicSequenceException, DimensionMismatchException {
        if (knots == null || polynomials == null) {
            throw new NullArgumentException();
        }
        if (knots.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.NOT_ENOUGH_POINTS_IN_SPLINE_PARTITION, 2, Integer.valueOf(knots.length), false);
        }
        if (knots.length - 1 != polynomials.length) {
            throw new DimensionMismatchException(polynomials.length, knots.length);
        }
        MathArrays.checkOrder(knots);
        this.f12978n = knots.length - 1;
        this.knots = new double[this.f12978n + 1];
        System.arraycopy(knots, 0, this.knots, 0, this.f12978n + 1);
        this.polynomials = new PolynomialFunction[this.f12978n];
        System.arraycopy(polynomials, 0, this.polynomials, 0, this.f12978n);
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double v2) {
        if (v2 < this.knots[0] || v2 > this.knots[this.f12978n]) {
            throw new OutOfRangeException(Double.valueOf(v2), Double.valueOf(this.knots[0]), Double.valueOf(this.knots[this.f12978n]));
        }
        int i2 = Arrays.binarySearch(this.knots, v2);
        if (i2 < 0) {
            i2 = (-i2) - 2;
        }
        if (i2 >= this.polynomials.length) {
            i2--;
        }
        return this.polynomials[i2].value(v2 - this.knots[i2]);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    public UnivariateFunction derivative() {
        return polynomialSplineDerivative();
    }

    public PolynomialSplineFunction polynomialSplineDerivative() {
        PolynomialFunction[] derivativePolynomials = new PolynomialFunction[this.f12978n];
        for (int i2 = 0; i2 < this.f12978n; i2++) {
            derivativePolynomials[i2] = this.polynomials[i2].polynomialDerivative();
        }
        return new PolynomialSplineFunction(this.knots, derivativePolynomials);
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) {
        double t0 = t2.getValue();
        if (t0 < this.knots[0] || t0 > this.knots[this.f12978n]) {
            throw new OutOfRangeException(Double.valueOf(t0), Double.valueOf(this.knots[0]), Double.valueOf(this.knots[this.f12978n]));
        }
        int i2 = Arrays.binarySearch(this.knots, t0);
        if (i2 < 0) {
            i2 = (-i2) - 2;
        }
        if (i2 >= this.polynomials.length) {
            i2--;
        }
        return this.polynomials[i2].value(t2.subtract(this.knots[i2]));
    }

    public int getN() {
        return this.f12978n;
    }

    public PolynomialFunction[] getPolynomials() {
        PolynomialFunction[] p2 = new PolynomialFunction[this.f12978n];
        System.arraycopy(this.polynomials, 0, p2, 0, this.f12978n);
        return p2;
    }

    public double[] getKnots() {
        double[] out = new double[this.f12978n + 1];
        System.arraycopy(this.knots, 0, out, 0, this.f12978n + 1);
        return out;
    }

    public boolean isValidPoint(double x2) {
        if (x2 < this.knots[0] || x2 > this.knots[this.f12978n]) {
            return false;
        }
        return true;
    }
}
