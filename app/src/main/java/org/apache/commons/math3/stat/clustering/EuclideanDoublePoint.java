package org.apache.commons.math3.stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/EuclideanDoublePoint.class */
public class EuclideanDoublePoint implements Clusterable<EuclideanDoublePoint>, Serializable {
    private static final long serialVersionUID = 8026472786091227632L;
    private final double[] point;

    public EuclideanDoublePoint(double[] point) {
        this.point = point;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.stat.clustering.Clusterable
    public EuclideanDoublePoint centroidOf(Collection<EuclideanDoublePoint> points) {
        double[] centroid = new double[getPoint().length];
        for (EuclideanDoublePoint p2 : points) {
            for (int i2 = 0; i2 < centroid.length; i2++) {
                int i3 = i2;
                centroid[i3] = centroid[i3] + p2.getPoint()[i2];
            }
        }
        for (int i4 = 0; i4 < centroid.length; i4++) {
            int i5 = i4;
            centroid[i5] = centroid[i5] / points.size();
        }
        return new EuclideanDoublePoint(centroid);
    }

    @Override // org.apache.commons.math3.stat.clustering.Clusterable
    public double distanceFrom(EuclideanDoublePoint p2) {
        return MathArrays.distance(this.point, p2.getPoint());
    }

    public boolean equals(Object other) {
        if (!(other instanceof EuclideanDoublePoint)) {
            return false;
        }
        return Arrays.equals(this.point, ((EuclideanDoublePoint) other).point);
    }

    public double[] getPoint() {
        return this.point;
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}
