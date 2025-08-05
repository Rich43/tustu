package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/gauss/GaussIntegrator.class */
public class GaussIntegrator {
    private final double[] points;
    private final double[] weights;

    public GaussIntegrator(double[] points, double[] weights) throws NonMonotonicSequenceException, DimensionMismatchException {
        if (points.length != weights.length) {
            throw new DimensionMismatchException(points.length, weights.length);
        }
        MathArrays.checkOrder(points, MathArrays.OrderDirection.INCREASING, true, true);
        this.points = (double[]) points.clone();
        this.weights = (double[]) weights.clone();
    }

    public GaussIntegrator(Pair<double[], double[]> pointsAndWeights) throws NonMonotonicSequenceException {
        this(pointsAndWeights.getFirst(), pointsAndWeights.getSecond());
    }

    public double integrate(UnivariateFunction f2) {
        double s2 = 0.0d;
        double c2 = 0.0d;
        for (int i2 = 0; i2 < this.points.length; i2++) {
            double x2 = this.points[i2];
            double w2 = this.weights[i2];
            double y2 = (w2 * f2.value(x2)) - c2;
            double t2 = s2 + y2;
            c2 = (t2 - s2) - y2;
            s2 = t2;
        }
        return s2;
    }

    public int getNumberOfPoints() {
        return this.points.length;
    }

    public double getPoint(int index) {
        return this.points[index];
    }

    public double getWeight(int index) {
        return this.weights[index];
    }
}
