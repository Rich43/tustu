package org.apache.commons.math3.stat.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/DBSCANClusterer.class */
public class DBSCANClusterer<T extends Clusterable<T>> {
    private final double eps;
    private final int minPts;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/DBSCANClusterer$PointStatus.class */
    private enum PointStatus {
        NOISE,
        PART_OF_CLUSTER
    }

    public DBSCANClusterer(double eps, int minPts) throws NotPositiveException {
        if (eps < 0.0d) {
            throw new NotPositiveException(Double.valueOf(eps));
        }
        if (minPts < 0) {
            throw new NotPositiveException(Integer.valueOf(minPts));
        }
        this.eps = eps;
        this.minPts = minPts;
    }

    public double getEps() {
        return this.eps;
    }

    public int getMinPts() {
        return this.minPts;
    }

    public List<Cluster<T>> cluster(Collection<T> points) throws NullArgumentException {
        MathUtils.checkNotNull(points);
        List<Cluster<T>> clusters = new ArrayList<>();
        Map<Clusterable<T>, PointStatus> visited = new HashMap<>();
        for (T t2 : points) {
            if (visited.get(t2) == null) {
                List<T> neighbors = getNeighbors(t2, points);
                if (neighbors.size() >= this.minPts) {
                    Cluster<T> cluster = new Cluster<>(null);
                    clusters.add(expandCluster(cluster, t2, neighbors, points, visited));
                } else {
                    visited.put(t2, PointStatus.NOISE);
                }
            }
        }
        return clusters;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Cluster<T> expandCluster(Cluster<T> cluster, T point, List<T> neighbors, Collection<T> points, Map<Clusterable<T>, PointStatus> map) {
        cluster.addPoint(point);
        map.put(point, PointStatus.PART_OF_CLUSTER);
        List<T> seeds = new ArrayList(neighbors);
        for (int index = 0; index < seeds.size(); index++) {
            T current = seeds.get(index);
            PointStatus pStatus = (PointStatus) map.get(current);
            if (pStatus == null) {
                List<T> currentNeighbors = getNeighbors(current, points);
                if (currentNeighbors.size() >= this.minPts) {
                    seeds = merge(seeds, currentNeighbors);
                }
            }
            if (pStatus != PointStatus.PART_OF_CLUSTER) {
                map.put(current, PointStatus.PART_OF_CLUSTER);
                cluster.addPoint(current);
            }
        }
        return cluster;
    }

    private List<T> getNeighbors(T point, Collection<T> points) {
        List<T> neighbors = new ArrayList<>();
        for (T neighbor : points) {
            if (point != neighbor && neighbor.distanceFrom(point) <= this.eps) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private List<T> merge(List<T> one, List<T> two) {
        Set<T> oneSet = new HashSet<>(one);
        for (T item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }
}
