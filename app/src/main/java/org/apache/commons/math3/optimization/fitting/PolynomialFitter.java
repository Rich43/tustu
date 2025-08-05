package org.apache.commons.math3.optimization.fitting;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/PolynomialFitter.class */
public class PolynomialFitter extends CurveFitter<PolynomialFunction.Parametric> {

    @Deprecated
    private final int degree;

    @Deprecated
    public PolynomialFitter(int degree, DifferentiableMultivariateVectorOptimizer optimizer) {
        super(optimizer);
        this.degree = degree;
    }

    public PolynomialFitter(DifferentiableMultivariateVectorOptimizer optimizer) {
        super(optimizer);
        this.degree = -1;
    }

    @Deprecated
    public double[] fit() {
        return fit((PolynomialFitter) new PolynomialFunction.Parametric(), new double[this.degree + 1]);
    }

    public double[] fit(int maxEval, double[] guess) {
        return fit(maxEval, new PolynomialFunction.Parametric(), guess);
    }

    public double[] fit(double[] guess) {
        return fit((PolynomialFitter) new PolynomialFunction.Parametric(), guess);
    }
}
