package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/SplineInterpolator.class */
public class SplineInterpolator implements UnivariateInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public PolynomialSplineFunction interpolate(double[] x2, double[] y2) throws NumberIsTooSmallException, NonMonotonicSequenceException, DimensionMismatchException {
        if (x2.length != y2.length) {
            throw new DimensionMismatchException(x2.length, y2.length);
        }
        if (x2.length < 3) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(x2.length), 3, true);
        }
        int n2 = x2.length - 1;
        MathArrays.checkOrder(x2);
        double[] h2 = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            h2[i2] = x2[i2 + 1] - x2[i2];
        }
        double[] mu = new double[n2];
        double[] z2 = new double[n2 + 1];
        mu[0] = 0.0d;
        z2[0] = 0.0d;
        for (int i3 = 1; i3 < n2; i3++) {
            double g2 = (2.0d * (x2[i3 + 1] - x2[i3 - 1])) - (h2[i3 - 1] * mu[i3 - 1]);
            mu[i3] = h2[i3] / g2;
            z2[i3] = (((3.0d * (((y2[i3 + 1] * h2[i3 - 1]) - (y2[i3] * (x2[i3 + 1] - x2[i3 - 1]))) + (y2[i3 - 1] * h2[i3]))) / (h2[i3 - 1] * h2[i3])) - (h2[i3 - 1] * z2[i3 - 1])) / g2;
        }
        double[] b2 = new double[n2];
        double[] c2 = new double[n2 + 1];
        double[] d2 = new double[n2];
        z2[n2] = 0.0d;
        c2[n2] = 0.0d;
        for (int j2 = n2 - 1; j2 >= 0; j2--) {
            c2[j2] = z2[j2] - (mu[j2] * c2[j2 + 1]);
            b2[j2] = ((y2[j2 + 1] - y2[j2]) / h2[j2]) - ((h2[j2] * (c2[j2 + 1] + (2.0d * c2[j2]))) / 3.0d);
            d2[j2] = (c2[j2 + 1] - c2[j2]) / (3.0d * h2[j2]);
        }
        PolynomialFunction[] polynomials = new PolynomialFunction[n2];
        double[] coefficients = new double[4];
        for (int i4 = 0; i4 < n2; i4++) {
            coefficients[0] = y2[i4];
            coefficients[1] = b2[i4];
            coefficients[2] = c2[i4];
            coefficients[3] = d2[i4];
            polynomials[i4] = new PolynomialFunction(coefficients);
        }
        return new PolynomialSplineFunction(x2, polynomials);
    }
}
