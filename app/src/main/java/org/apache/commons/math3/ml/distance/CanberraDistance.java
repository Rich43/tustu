package org.apache.commons.math3.ml.distance;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/distance/CanberraDistance.class */
public class CanberraDistance implements DistanceMeasure {
    private static final long serialVersionUID = -6972277381587032228L;

    @Override // org.apache.commons.math3.ml.distance.DistanceMeasure
    public double compute(double[] a2, double[] b2) throws DimensionMismatchException {
        MathArrays.checkEqualLength(a2, b2);
        double sum = 0.0d;
        for (int i2 = 0; i2 < a2.length; i2++) {
            double num = FastMath.abs(a2[i2] - b2[i2]);
            double denom = FastMath.abs(a2[i2]) + FastMath.abs(b2[i2]);
            sum += (num == 0.0d && denom == 0.0d) ? 0.0d : num / denom;
        }
        return sum;
    }
}
