package org.apache.commons.math3.analysis.polynomials;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialFunctionNewtonForm.class */
public class PolynomialFunctionNewtonForm implements UnivariateDifferentiableFunction {
    private double[] coefficients;

    /* renamed from: c, reason: collision with root package name */
    private final double[] f12976c;

    /* renamed from: a, reason: collision with root package name */
    private final double[] f12977a;
    private boolean coefficientsComputed;

    public PolynomialFunctionNewtonForm(double[] a2, double[] c2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        verifyInputArray(a2, c2);
        this.f12977a = new double[a2.length];
        this.f12976c = new double[c2.length];
        System.arraycopy(a2, 0, this.f12977a, 0, a2.length);
        System.arraycopy(c2, 0, this.f12976c, 0, c2.length);
        this.coefficientsComputed = false;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double z2) {
        return evaluate(this.f12977a, this.f12976c, z2);
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        verifyInputArray(this.f12977a, this.f12976c);
        int n2 = this.f12976c.length;
        DerivativeStructure value = new DerivativeStructure(t2.getFreeParameters(), t2.getOrder(), this.f12977a[n2]);
        for (int i2 = n2 - 1; i2 >= 0; i2--) {
            value = t2.subtract(this.f12976c[i2]).multiply(value).add(this.f12977a[i2]);
        }
        return value;
    }

    public int degree() {
        return this.f12976c.length;
    }

    public double[] getNewtonCoefficients() {
        double[] out = new double[this.f12977a.length];
        System.arraycopy(this.f12977a, 0, out, 0, this.f12977a.length);
        return out;
    }

    public double[] getCenters() {
        double[] out = new double[this.f12976c.length];
        System.arraycopy(this.f12976c, 0, out, 0, this.f12976c.length);
        return out;
    }

    public double[] getCoefficients() {
        if (!this.coefficientsComputed) {
            computeCoefficients();
        }
        double[] out = new double[this.coefficients.length];
        System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
        return out;
    }

    public static double evaluate(double[] a2, double[] c2, double z2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        verifyInputArray(a2, c2);
        int n2 = c2.length;
        double value = a2[n2];
        for (int i2 = n2 - 1; i2 >= 0; i2--) {
            value = a2[i2] + ((z2 - c2[i2]) * value);
        }
        return value;
    }

    protected void computeCoefficients() {
        int n2 = degree();
        this.coefficients = new double[n2 + 1];
        for (int i2 = 0; i2 <= n2; i2++) {
            this.coefficients[i2] = 0.0d;
        }
        this.coefficients[0] = this.f12977a[n2];
        for (int i3 = n2 - 1; i3 >= 0; i3--) {
            for (int j2 = n2 - i3; j2 > 0; j2--) {
                this.coefficients[j2] = this.coefficients[j2 - 1] - (this.f12976c[i3] * this.coefficients[j2]);
            }
            this.coefficients[0] = this.f12977a[i3] - (this.f12976c[i3] * this.coefficients[0]);
        }
        this.coefficientsComputed = true;
    }

    protected static void verifyInputArray(double[] a2, double[] c2) throws NullArgumentException, NoDataException, DimensionMismatchException {
        MathUtils.checkNotNull(a2);
        MathUtils.checkNotNull(c2);
        if (a2.length == 0 || c2.length == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        if (a2.length != c2.length + 1) {
            throw new DimensionMismatchException(LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_1, a2.length, c2.length);
        }
    }
}
