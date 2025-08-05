package org.apache.commons.math3.ml.clustering;

import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.clustering.evaluation.SumOfClusterVariances;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/MultiKMeansPlusPlusClusterer.class */
public class MultiKMeansPlusPlusClusterer<T extends Clusterable> extends Clusterer<T> {
    private final KMeansPlusPlusClusterer<T> clusterer;
    private final int numTrials;
    private final ClusterEvaluator<T> evaluator;

    public MultiKMeansPlusPlusClusterer(KMeansPlusPlusClusterer<T> clusterer, int numTrials) {
        this(clusterer, numTrials, new SumOfClusterVariances(clusterer.getDistanceMeasure()));
    }

    public MultiKMeansPlusPlusClusterer(KMeansPlusPlusClusterer<T> clusterer, int numTrials, ClusterEvaluator<T> evaluator) {
        super(clusterer.getDistanceMeasure());
        this.clusterer = clusterer;
        this.numTrials = numTrials;
        this.evaluator = evaluator;
    }

    public KMeansPlusPlusClusterer<T> getClusterer() {
        return this.clusterer;
    }

    public int getNumTrials() {
        return this.numTrials;
    }

    public ClusterEvaluator<T> getClusterEvaluator() {
        return this.evaluator;
    }

    @Override // org.apache.commons.math3.ml.clustering.Clusterer
    public List<CentroidCluster<T>> cluster(Collection<T> points) throws ConvergenceException, MathIllegalArgumentException {
        List<CentroidCluster<T>> best = null;
        double bestVarianceSum = Double.POSITIVE_INFINITY;
        for (int i2 = 0; i2 < this.numTrials; i2++) {
            List<CentroidCluster<T>> clusters = this.clusterer.cluster(points);
            double varianceSum = this.evaluator.score(clusters);
            if (this.evaluator.isBetterScore(varianceSum, bestVarianceSum)) {
                best = clusters;
                bestVarianceSum = varianceSum;
            }
        }
        return best;
    }
}
