package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/SymmetricGaussIntegrator.class */
public class SymmetricGaussIntegrator extends GaussIntegrator {
    public SymmetricGaussIntegrator(double[] points, double[] weights) throws NonMonotonicSequenceException, DimensionMismatchException {
        super(points, weights);
    }

    public SymmetricGaussIntegrator(Pair<double[], double[]> pointsAndWeights) throws NonMonotonicSequenceException {
        this(pointsAndWeights.getFirst(), pointsAndWeights.getSecond());
    }

    @Override // org.apache.commons.math3.analysis.integration.gauss.GaussIntegrator
    public double integrate(UnivariateFunction f2) {
        int ruleLength = getNumberOfPoints();
        if (ruleLength == 1) {
            return getWeight(0) * f2.value(0.0d);
        }
        int iMax = ruleLength / 2;
        double s2 = 0.0d;
        double c2 = 0.0d;
        for (int i2 = 0; i2 < iMax; i2++) {
            double p2 = getPoint(i2);
            double w2 = getWeight(i2);
            double f1 = f2.value(p2);
            double f22 = f2.value(-p2);
            double y2 = (w2 * (f1 + f22)) - c2;
            double t2 = s2 + y2;
            c2 = (t2 - s2) - y2;
            s2 = t2;
        }
        if (ruleLength % 2 != 0) {
            double w3 = getWeight(iMax);
            s2 += (w3 * f2.value(0.0d)) - c2;
        }
        return s2;
    }
}
