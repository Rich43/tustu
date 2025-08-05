package org.apache.commons.math3.ml.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.ml.clustering.Clusterable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/Cluster.class */
public class Cluster<T extends Clusterable> implements Serializable {
    private static final long serialVersionUID = -3442297081515880464L;
    private final List<T> points = new ArrayList();

    public void addPoint(T point) {
        this.points.add(point);
    }

    public List<T> getPoints() {
        return this.points;
    }
}
