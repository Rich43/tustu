package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/LinearInterpolator.class */
public class LinearInterpolator implements UnivariateInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public PolynomialSplineFunction interpolate(double[] x2, double[] y2) throws NumberIsTooSmallException, NonMonotonicSequenceException, DimensionMismatchException {
        if (x2.length != y2.length) {
            throw new DimensionMismatchException(x2.length, y2.length);
        }
        if (x2.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(x2.length), 2, true);
        }
        int n2 = x2.length - 1;
        MathArrays.checkOrder(x2);
        double[] m2 = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            m2[i2] = (y2[i2 + 1] - y2[i2]) / (x2[i2 + 1] - x2[i2]);
        }
        PolynomialFunction[] polynomials = new PolynomialFunction[n2];
        double[] coefficients = new double[2];
        for (int i3 = 0; i3 < n2; i3++) {
            coefficients[0] = y2[i3];
            coefficients[1] = m2[i3];
            polynomials[i3] = new PolynomialFunction(coefficients);
        }
        return new PolynomialSplineFunction(x2, polynomials);
    }
}
