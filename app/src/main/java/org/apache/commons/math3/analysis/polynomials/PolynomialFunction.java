package org.apache.commons.math3.analysis.polynomials;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import sun.util.locale.LanguageTag;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialFunction.class */
public class PolynomialFunction implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction, Serializable {
    private static final long serialVersionUID = -7726511984200295583L;
    private final double[] coefficients;

    public PolynomialFunction(double[] c2) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(c2);
        int n2 = c2.length;
        if (n2 == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        while (n2 > 1 && c2[n2 - 1] == 0.0d) {
            n2--;
        }
        this.coefficients = new double[n2];
        System.arraycopy(c2, 0, this.coefficients, 0, n2);
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return evaluate(this.coefficients, x2);
    }

    public int degree() {
        return this.coefficients.length - 1;
    }

    public double[] getCoefficients() {
        return (double[]) this.coefficients.clone();
    }

    protected static double evaluate(double[] coefficients, double argument) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(coefficients);
        int n2 = coefficients.length;
        if (n2 == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        double result = coefficients[n2 - 1];
        for (int j2 = n2 - 2; j2 >= 0; j2--) {
            result = (argument * result) + coefficients[j2];
        }
        return result;
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(this.coefficients);
        int n2 = this.coefficients.length;
        if (n2 == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        DerivativeStructure result = new DerivativeStructure(t2.getFreeParameters(), t2.getOrder(), this.coefficients[n2 - 1]);
        for (int j2 = n2 - 2; j2 >= 0; j2--) {
            result = result.multiply(t2).add(this.coefficients[j2]);
        }
        return result;
    }

    public PolynomialFunction add(PolynomialFunction p2) {
        int lowLength = FastMath.min(this.coefficients.length, p2.coefficients.length);
        int highLength = FastMath.max(this.coefficients.length, p2.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (int i2 = 0; i2 < lowLength; i2++) {
            newCoefficients[i2] = this.coefficients[i2] + p2.coefficients[i2];
        }
        System.arraycopy(this.coefficients.length < p2.coefficients.length ? p2.coefficients : this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction subtract(PolynomialFunction p2) {
        int lowLength = FastMath.min(this.coefficients.length, p2.coefficients.length);
        int highLength = FastMath.max(this.coefficients.length, p2.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (int i2 = 0; i2 < lowLength; i2++) {
            newCoefficients[i2] = this.coefficients[i2] - p2.coefficients[i2];
        }
        if (this.coefficients.length < p2.coefficients.length) {
            for (int i3 = lowLength; i3 < highLength; i3++) {
                newCoefficients[i3] = -p2.coefficients[i3];
            }
        } else {
            System.arraycopy(this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        }
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction negate() {
        double[] newCoefficients = new double[this.coefficients.length];
        for (int i2 = 0; i2 < this.coefficients.length; i2++) {
            newCoefficients[i2] = -this.coefficients[i2];
        }
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction multiply(PolynomialFunction p2) {
        double[] newCoefficients = new double[(this.coefficients.length + p2.coefficients.length) - 1];
        for (int i2 = 0; i2 < newCoefficients.length; i2++) {
            newCoefficients[i2] = 0.0d;
            for (int j2 = FastMath.max(0, (i2 + 1) - p2.coefficients.length); j2 < FastMath.min(this.coefficients.length, i2 + 1); j2++) {
                int i3 = i2;
                newCoefficients[i3] = newCoefficients[i3] + (this.coefficients[j2] * p2.coefficients[i2 - j2]);
            }
        }
        return new PolynomialFunction(newCoefficients);
    }

    protected static double[] differentiate(double[] coefficients) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(coefficients);
        int n2 = coefficients.length;
        if (n2 == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        if (n2 == 1) {
            return new double[]{0.0d};
        }
        double[] result = new double[n2 - 1];
        for (int i2 = n2 - 1; i2 > 0; i2--) {
            result[i2 - 1] = i2 * coefficients[i2];
        }
        return result;
    }

    public PolynomialFunction polynomialDerivative() {
        return new PolynomialFunction(differentiate(this.coefficients));
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    public UnivariateFunction derivative() {
        return polynomialDerivative();
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder();
        if (this.coefficients[0] == 0.0d) {
            if (this.coefficients.length == 1) {
                return "0";
            }
        } else {
            s2.append(toString(this.coefficients[0]));
        }
        for (int i2 = 1; i2 < this.coefficients.length; i2++) {
            if (this.coefficients[i2] != 0.0d) {
                if (s2.length() > 0) {
                    if (this.coefficients[i2] < 0.0d) {
                        s2.append(" - ");
                    } else {
                        s2.append(" + ");
                    }
                } else if (this.coefficients[i2] < 0.0d) {
                    s2.append(LanguageTag.SEP);
                }
                double absAi = FastMath.abs(this.coefficients[i2]);
                if (absAi - 1.0d != 0.0d) {
                    s2.append(toString(absAi));
                    s2.append(' ');
                }
                s2.append(LanguageTag.PRIVATEUSE);
                if (i2 > 1) {
                    s2.append('^');
                    s2.append(Integer.toString(i2));
                }
            }
        }
        return s2.toString();
    }

    private static String toString(double coeff) {
        String c2 = Double.toString(coeff);
        if (c2.endsWith(".0")) {
            return c2.substring(0, c2.length() - 2);
        }
        return c2;
    }

    public int hashCode() {
        int result = (31 * 1) + Arrays.hashCode(this.coefficients);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolynomialFunction)) {
            return false;
        }
        PolynomialFunction other = (PolynomialFunction) obj;
        if (!Arrays.equals(this.coefficients, other.coefficients)) {
            return false;
        }
        return true;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialFunction$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... parameters) {
            double[] gradient = new double[parameters.length];
            double xn = 1.0d;
            for (int i2 = 0; i2 < parameters.length; i2++) {
                gradient[i2] = xn;
                xn *= x2;
            }
            return gradient;
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... parameters) throws NoDataException {
            return PolynomialFunction.evaluate(parameters, x2);
        }
    }
}
