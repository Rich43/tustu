package org.apache.commons.math3.stat.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/KMeansPlusPlusClusterer.class */
public class KMeansPlusPlusClusterer<T extends Clusterable<T>> {
    private final Random random;
    private final EmptyClusterStrategy emptyStrategy;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/clustering/KMeansPlusPlusClusterer$EmptyClusterStrategy.class */
    public enum EmptyClusterStrategy {
        LARGEST_VARIANCE,
        LARGEST_POINTS_NUMBER,
        FARTHEST_POINT,
        ERROR
    }

    public KMeansPlusPlusClusterer(Random random) {
        this(random, EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    public KMeansPlusPlusClusterer(Random random, EmptyClusterStrategy emptyStrategy) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
    }

    public List<Cluster<T>> cluster(Collection<T> points, int k2, int numTrials, int maxIterationsPerTrial) throws ConvergenceException, MathIllegalArgumentException {
        List<Cluster<T>> best = null;
        double bestVarianceSum = Double.POSITIVE_INFINITY;
        for (int i2 = 0; i2 < numTrials; i2++) {
            List<Cluster<T>> clusters = cluster(points, k2, maxIterationsPerTrial);
            double varianceSum = 0.0d;
            for (Cluster<T> cluster : clusters) {
                if (!cluster.getPoints().isEmpty()) {
                    Clusterable center = cluster.getCenter();
                    Variance stat = new Variance();
                    for (T point : cluster.getPoints()) {
                        stat.increment(point.distanceFrom(center));
                    }
                    varianceSum += stat.getResult();
                }
            }
            if (varianceSum <= bestVarianceSum) {
                best = clusters;
                bestVarianceSum = varianceSum;
            }
        }
        return best;
    }

    public List<Cluster<T>> cluster(Collection<T> points, int k2, int maxIterations) throws ConvergenceException, MathIllegalArgumentException {
        Clusterable farthestPoint;
        MathUtils.checkNotNull(points);
        if (points.size() < k2) {
            throw new NumberIsTooSmallException(Integer.valueOf(points.size()), Integer.valueOf(k2), false);
        }
        List<Cluster<T>> clusters = chooseInitialCenters(points, k2, this.random);
        int[] assignments = new int[points.size()];
        assignPointsToClusters(clusters, points, assignments);
        int max = maxIterations < 0 ? Integer.MAX_VALUE : maxIterations;
        for (int count = 0; count < max; count++) {
            boolean emptyCluster = false;
            List<Cluster<T>> newClusters = new ArrayList<>();
            for (Cluster<T> cluster : clusters) {
                if (cluster.getPoints().isEmpty()) {
                    switch (this.emptyStrategy) {
                        case LARGEST_VARIANCE:
                            farthestPoint = getPointFromLargestVarianceCluster(clusters);
                            break;
                        case LARGEST_POINTS_NUMBER:
                            farthestPoint = getPointFromLargestNumberCluster(clusters);
                            break;
                        case FARTHEST_POINT:
                            farthestPoint = getFarthestPoint(clusters);
                            break;
                        default:
                            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
                    }
                    emptyCluster = true;
                } else {
                    farthestPoint = (Clusterable) cluster.getCenter().centroidOf(cluster.getPoints());
                }
                newClusters.add(new Cluster<>(farthestPoint));
            }
            int changes = assignPointsToClusters(newClusters, points, assignments);
            clusters = newClusters;
            if (changes == 0 && !emptyCluster) {
                return clusters;
            }
        }
        return clusters;
    }

    private static <T extends Clusterable<T>> int assignPointsToClusters(List<Cluster<T>> clusters, Collection<T> points, int[] assignments) {
        int assignedDifferently = 0;
        int pointIndex = 0;
        for (T p2 : points) {
            int clusterIndex = getNearestCluster(clusters, p2);
            if (clusterIndex != assignments[pointIndex]) {
                assignedDifferently++;
            }
            Cluster<T> cluster = clusters.get(clusterIndex);
            cluster.addPoint(p2);
            int i2 = pointIndex;
            pointIndex++;
            assignments[i2] = clusterIndex;
        }
        return assignedDifferently;
    }

    private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(Collection<T> points, int k2, Random random) {
        List<T> pointList = Collections.unmodifiableList(new ArrayList(points));
        int numPoints = pointList.size();
        boolean[] taken = new boolean[numPoints];
        List<Cluster<T>> resultSet = new ArrayList<>();
        int firstPointIndex = random.nextInt(numPoints);
        T firstPoint = pointList.get(firstPointIndex);
        resultSet.add(new Cluster<>(firstPoint));
        taken[firstPointIndex] = true;
        double[] minDistSquared = new double[numPoints];
        for (int i2 = 0; i2 < numPoints; i2++) {
            if (i2 != firstPointIndex) {
                double d2 = firstPoint.distanceFrom(pointList.get(i2));
                minDistSquared[i2] = d2 * d2;
            }
        }
        while (resultSet.size() < k2) {
            double distSqSum = 0.0d;
            for (int i3 = 0; i3 < numPoints; i3++) {
                if (!taken[i3]) {
                    distSqSum += minDistSquared[i3];
                }
            }
            double r2 = random.nextDouble() * distSqSum;
            int nextPointIndex = -1;
            double sum = 0.0d;
            int i4 = 0;
            while (true) {
                if (i4 >= numPoints) {
                    break;
                }
                if (!taken[i4]) {
                    sum += minDistSquared[i4];
                    if (sum >= r2) {
                        nextPointIndex = i4;
                        break;
                    }
                }
                i4++;
            }
            if (nextPointIndex == -1) {
                int i5 = numPoints - 1;
                while (true) {
                    if (i5 < 0) {
                        break;
                    }
                    if (!taken[i5]) {
                        nextPointIndex = i5;
                        break;
                    }
                    i5--;
                }
            }
            if (nextPointIndex < 0) {
                break;
            }
            T p2 = pointList.get(nextPointIndex);
            resultSet.add(new Cluster<>(p2));
            taken[nextPointIndex] = true;
            if (resultSet.size() < k2) {
                for (int j2 = 0; j2 < numPoints; j2++) {
                    if (!taken[j2]) {
                        double d3 = p2.distanceFrom(pointList.get(j2));
                        double d22 = d3 * d3;
                        if (d22 < minDistSquared[j2]) {
                            minDistSquared[j2] = d22;
                        }
                    }
                }
            }
        }
        return resultSet;
    }

    private T getPointFromLargestVarianceCluster(Collection<Cluster<T>> clusters) throws ConvergenceException {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<T> selected = null;
        for (Cluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                Clusterable center = cluster.getCenter();
                Variance stat = new Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(point.distanceFrom(center));
                }
                double variance = stat.getResult();
                if (variance > maxVariance) {
                    maxVariance = variance;
                    selected = cluster;
                }
            }
        }
        if (selected == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
        }
        List<T> selectedPoints = selected.getPoints();
        return selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getPointFromLargestNumberCluster(Collection<Cluster<T>> clusters) throws ConvergenceException {
        int maxNumber = 0;
        Cluster<T> selected = null;
        for (Cluster<T> cluster : clusters) {
            int number = cluster.getPoints().size();
            if (number > maxNumber) {
                maxNumber = number;
                selected = cluster;
            }
        }
        if (selected == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
        }
        List<T> selectedPoints = selected.getPoints();
        return selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getFarthestPoint(Collection<Cluster<T>> clusters) throws ConvergenceException {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<T> selectedCluster = null;
        int selectedPoint = -1;
        for (Cluster<T> cluster : clusters) {
            Clusterable center = cluster.getCenter();
            List<T> points = cluster.getPoints();
            for (int i2 = 0; i2 < points.size(); i2++) {
                double distance = points.get(i2).distanceFrom(center);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    selectedCluster = cluster;
                    selectedPoint = i2;
                }
            }
        }
        if (selectedCluster == null) {
            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
        }
        return selectedCluster.getPoints().remove(selectedPoint);
    }

    private static <T extends Clusterable<T>> int getNearestCluster(Collection<Cluster<T>> clusters, T point) {
        double minDistance = Double.MAX_VALUE;
        int clusterIndex = 0;
        int minCluster = 0;
        for (Cluster<T> c2 : clusters) {
            double distance = point.distanceFrom(c2.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = clusterIndex;
            }
            clusterIndex++;
        }
        return minCluster;
    }
}
