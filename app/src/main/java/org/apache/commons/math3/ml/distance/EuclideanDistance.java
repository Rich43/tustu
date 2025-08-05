package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/distance/EuclideanDistance.class */
public class EuclideanDistance implements DistanceMeasure {
    private static final long serialVersionUID = 1717556319784040040L;

    @Override // org.apache.commons.math3.ml.distance.DistanceMeasure
    public double compute(double[] a2, double[] b2) throws DimensionMismatchException {
        return MathArrays.distance(a2, b2);
    }
}
