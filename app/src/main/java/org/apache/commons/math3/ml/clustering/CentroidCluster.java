package org.apache.commons.math3.ml.clustering;

import org.apache.commons.math3.ml.clustering.Clusterable;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/CentroidCluster.class */
public class CentroidCluster<T extends Clusterable> extends Cluster<T> {
    private static final long serialVersionUID = -3075288519071812288L;
    private final Clusterable center;

    public CentroidCluster(Clusterable center) {
        this.center = center;
    }

    public Clusterable getCenter() {
        return this.center;
    }
}
