package org.apache.commons.math3.analysis.interpolation;

import java.io.Serializable;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/DividedDifferenceInterpolator.class */
public class DividedDifferenceInterpolator implements UnivariateInterpolator, Serializable {
    private static final long serialVersionUID = 107049519551235069L;

    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public PolynomialFunctionNewtonForm interpolate(double[] x2, double[] y2) throws NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        PolynomialFunctionLagrangeForm.verifyInterpolationArray(x2, y2, true);
        double[] c2 = new double[x2.length - 1];
        System.arraycopy(x2, 0, c2, 0, c2.length);
        double[] a2 = computeDividedDifference(x2, y2);
        return new PolynomialFunctionNewtonForm(a2, c2);
    }

    protected static double[] computeDividedDifference(double[] x2, double[] y2) throws NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        PolynomialFunctionLagrangeForm.verifyInterpolationArray(x2, y2, true);
        double[] divdiff = (double[]) y2.clone();
        int n2 = x2.length;
        double[] a2 = new double[n2];
        a2[0] = divdiff[0];
        for (int i2 = 1; i2 < n2; i2++) {
            for (int j2 = 0; j2 < n2 - i2; j2++) {
                double denominator = x2[j2 + i2] - x2[j2];
                divdiff[j2] = (divdiff[j2 + 1] - divdiff[j2]) / denominator;
            }
            a2[i2] = divdiff[0];
        }
        return a2;
    }
}
