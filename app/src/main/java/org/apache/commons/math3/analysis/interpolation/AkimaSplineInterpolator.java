package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/AkimaSplineInterpolator.class */
public class AkimaSplineInterpolator implements UnivariateInterpolator {
    private static final int MINIMUM_NUMBER_POINTS = 5;

    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public PolynomialSplineFunction interpolate(double[] xvals, double[] yvals) throws NumberIsTooSmallException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xvals == null || yvals == null) {
            throw new NullArgumentException();
        }
        if (xvals.length != yvals.length) {
            throw new DimensionMismatchException(xvals.length, yvals.length);
        }
        if (xvals.length < 5) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(xvals.length), 5, true);
        }
        MathArrays.checkOrder(xvals);
        int numberOfDiffAndWeightElements = xvals.length - 1;
        double[] differences = new double[numberOfDiffAndWeightElements];
        double[] weights = new double[numberOfDiffAndWeightElements];
        for (int i2 = 0; i2 < differences.length; i2++) {
            differences[i2] = (yvals[i2 + 1] - yvals[i2]) / (xvals[i2 + 1] - xvals[i2]);
        }
        for (int i3 = 1; i3 < weights.length; i3++) {
            weights[i3] = FastMath.abs(differences[i3] - differences[i3 - 1]);
        }
        double[] firstDerivatives = new double[xvals.length];
        for (int i4 = 2; i4 < firstDerivatives.length - 2; i4++) {
            double wP = weights[i4 + 1];
            double wM = weights[i4 - 1];
            if (Precision.equals(wP, 0.0d) && Precision.equals(wM, 0.0d)) {
                double xv = xvals[i4];
                double xvP = xvals[i4 + 1];
                double xvM = xvals[i4 - 1];
                firstDerivatives[i4] = (((xvP - xv) * differences[i4 - 1]) + ((xv - xvM) * differences[i4])) / (xvP - xvM);
            } else {
                firstDerivatives[i4] = ((wP * differences[i4 - 1]) + (wM * differences[i4])) / (wP + wM);
            }
        }
        firstDerivatives[0] = differentiateThreePoint(xvals, yvals, 0, 0, 1, 2);
        firstDerivatives[1] = differentiateThreePoint(xvals, yvals, 1, 0, 1, 2);
        firstDerivatives[xvals.length - 2] = differentiateThreePoint(xvals, yvals, xvals.length - 2, xvals.length - 3, xvals.length - 2, xvals.length - 1);
        firstDerivatives[xvals.length - 1] = differentiateThreePoint(xvals, yvals, xvals.length - 1, xvals.length - 3, xvals.length - 2, xvals.length - 1);
        return interpolateHermiteSorted(xvals, yvals, firstDerivatives);
    }

    private double differentiateThreePoint(double[] xvals, double[] yvals, int indexOfDifferentiation, int indexOfFirstSample, int indexOfSecondsample, int indexOfThirdSample) {
        double x0 = yvals[indexOfFirstSample];
        double x1 = yvals[indexOfSecondsample];
        double x2 = yvals[indexOfThirdSample];
        double t2 = xvals[indexOfDifferentiation] - xvals[indexOfFirstSample];
        double t1 = xvals[indexOfSecondsample] - xvals[indexOfFirstSample];
        double t22 = xvals[indexOfThirdSample] - xvals[indexOfFirstSample];
        double a2 = ((x2 - x0) - ((t22 / t1) * (x1 - x0))) / ((t22 * t22) - (t1 * t22));
        double b2 = ((x1 - x0) - ((a2 * t1) * t1)) / t1;
        return (2.0d * a2 * t2) + b2;
    }

    private PolynomialSplineFunction interpolateHermiteSorted(double[] xvals, double[] yvals, double[] firstDerivatives) {
        if (xvals.length != yvals.length) {
            throw new DimensionMismatchException(xvals.length, yvals.length);
        }
        if (xvals.length != firstDerivatives.length) {
            throw new DimensionMismatchException(xvals.length, firstDerivatives.length);
        }
        if (xvals.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(xvals.length), 2, true);
        }
        int size = xvals.length - 1;
        PolynomialFunction[] polynomials = new PolynomialFunction[size];
        double[] coefficients = new double[4];
        for (int i2 = 0; i2 < polynomials.length; i2++) {
            double w2 = xvals[i2 + 1] - xvals[i2];
            double w22 = w2 * w2;
            double yv = yvals[i2];
            double yvP = yvals[i2 + 1];
            double fd = firstDerivatives[i2];
            double fdP = firstDerivatives[i2 + 1];
            coefficients[0] = yv;
            coefficients[1] = firstDerivatives[i2];
            coefficients[2] = ((((3.0d * (yvP - yv)) / w2) - (2.0d * fd)) - fdP) / w2;
            coefficients[3] = ((((2.0d * (yv - yvP)) / w2) + fd) + fdP) / w22;
            polynomials[i2] = new PolynomialFunction(coefficients);
        }
        return new PolynomialSplineFunction(xvals, polynomials);
    }
}
