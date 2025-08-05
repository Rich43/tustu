package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/distance/ManhattanDistance.class */
public class ManhattanDistance implements DistanceMeasure {
    private static final long serialVersionUID = -9108154600539125566L;

    @Override // org.apache.commons.math3.ml.distance.DistanceMeasure
    public double compute(double[] a2, double[] b2) throws DimensionMismatchException {
        return MathArrays.distance1(a2, b2);
    }
}
