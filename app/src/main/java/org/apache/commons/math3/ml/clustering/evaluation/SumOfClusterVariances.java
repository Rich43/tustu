package org.apache.commons.math3.ml.clustering.evaluation;

import java.util.List;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/evaluation/SumOfClusterVariances.class */
public class SumOfClusterVariances<T extends Clusterable> extends ClusterEvaluator<T> {
    public SumOfClusterVariances(DistanceMeasure measure) {
        super(measure);
    }

    @Override // org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator
    public double score(List<? extends Cluster<T>> clusters) {
        double varianceSum = 0.0d;
        for (Cluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                Clusterable center = centroidOf(cluster);
                Variance stat = new Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(distance(point, center));
                }
                varianceSum += stat.getResult();
            }
        }
        return varianceSum;
    }
}
