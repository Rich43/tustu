package org.apache.commons.math3.ml.clustering;

import java.io.Serializable;
import java.util.Arrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/DoublePoint.class */
public class DoublePoint implements Clusterable, Serializable {
    private static final long serialVersionUID = 3946024775784901369L;
    private final double[] point;

    public DoublePoint(double[] point) {
        this.point = point;
    }

    public DoublePoint(int[] point) {
        this.point = new double[point.length];
        for (int i2 = 0; i2 < point.length; i2++) {
            this.point[i2] = point[i2];
        }
    }

    @Override // org.apache.commons.math3.ml.clustering.Clusterable
    public double[] getPoint() {
        return this.point;
    }

    public boolean equals(Object other) {
        if (!(other instanceof DoublePoint)) {
            return false;
        }
        return Arrays.equals(this.point, ((DoublePoint) other).point);
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}
