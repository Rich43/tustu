package org.apache.commons.math3.stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/EuclideanIntegerPoint.class */
public class EuclideanIntegerPoint implements Clusterable<EuclideanIntegerPoint>, Serializable {
    private static final long serialVersionUID = 3946024775784901369L;
    private final int[] point;

    public EuclideanIntegerPoint(int[] point) {
        this.point = point;
    }

    public int[] getPoint() {
        return this.point;
    }

    @Override // org.apache.commons.math3.stat.clustering.Clusterable
    public double distanceFrom(EuclideanIntegerPoint p2) {
        return MathArrays.distance(this.point, p2.getPoint());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.math3.stat.clustering.Clusterable
    public EuclideanIntegerPoint centroidOf(Collection<EuclideanIntegerPoint> points) {
        int[] centroid = new int[getPoint().length];
        for (EuclideanIntegerPoint p2 : points) {
            for (int i2 = 0; i2 < centroid.length; i2++) {
                int i3 = i2;
                centroid[i3] = centroid[i3] + p2.getPoint()[i2];
            }
        }
        for (int i4 = 0; i4 < centroid.length; i4++) {
            int i5 = i4;
            centroid[i5] = centroid[i5] / points.size();
        }
        return new EuclideanIntegerPoint(centroid);
    }

    public boolean equals(Object other) {
        if (!(other instanceof EuclideanIntegerPoint)) {
            return false;
        }
        return Arrays.equals(this.point, ((EuclideanIntegerPoint) other).point);
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}
