package org.apache.commons.math3.stat.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.clustering.Clusterable;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/Cluster.class */
public class Cluster<T extends Clusterable<T>> implements Serializable {
    private static final long serialVersionUID = -3442297081515880464L;
    private final List<T> points = new ArrayList();
    private final T center;

    public Cluster(T center) {
        this.center = center;
    }

    public void addPoint(T point) {
        this.points.add(point);
    }

    public List<T> getPoints() {
        return this.points;
    }

    public T getCenter() {
        return this.center;
    }
}
