package org.apache.commons.math3.analysis.interpolation;

import java.io.Serializable;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/NevilleInterpolator.class */
public class NevilleInterpolator implements UnivariateInterpolator, Serializable {
    static final long serialVersionUID = 3003707660147873733L;

    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public PolynomialFunctionLagrangeForm interpolate(double[] x2, double[] y2) throws NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        return new PolynomialFunctionLagrangeForm(x2, y2);
    }
}
