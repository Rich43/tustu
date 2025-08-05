package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/distance/EarthMoversDistance.class */
public class EarthMoversDistance implements DistanceMeasure {
    private static final long serialVersionUID = -5406732779747414922L;

    @Override // org.apache.commons.math3.ml.distance.DistanceMeasure
    public double compute(double[] a2, double[] b2) throws DimensionMismatchException {
        MathArrays.checkEqualLength(a2, b2);
        double lastDistance = 0.0d;
        double totalDistance = 0.0d;
        for (int i2 = 0; i2 < a2.length; i2++) {
            double currentDistance = (a2[i2] + lastDistance) - b2[i2];
            totalDistance += FastMath.abs(currentDistance);
            lastDistance = currentDistance;
        }
        return totalDistance;
    }
}
