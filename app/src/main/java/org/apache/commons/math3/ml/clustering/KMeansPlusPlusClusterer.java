package org.apache.commons.math3.ml.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/KMeansPlusPlusClusterer.class */
public class KMeansPlusPlusClusterer<T extends Clusterable> extends Clusterer<T> {

    /* renamed from: k, reason: collision with root package name */
    private final int f13041k;
    private final int maxIterations;
    private final RandomGenerator random;
    private final EmptyClusterStrategy emptyStrategy;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/KMeansPlusPlusClusterer$EmptyClusterStrategy.class */
    public enum EmptyClusterStrategy {
        LARGEST_VARIANCE,
        LARGEST_POINTS_NUMBER,
        FARTHEST_POINT,
        ERROR
    }

    public KMeansPlusPlusClusterer(int k2) {
        this(k2, -1);
    }

    public KMeansPlusPlusClusterer(int k2, int maxIterations) {
        this(k2, maxIterations, new EuclideanDistance());
    }

    public KMeansPlusPlusClusterer(int k2, int maxIterations, DistanceMeasure measure) {
        this(k2, maxIterations, measure, new JDKRandomGenerator());
    }

    public KMeansPlusPlusClusterer(int k2, int maxIterations, DistanceMeasure measure, RandomGenerator random) {
        this(k2, maxIterations, measure, random, EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    public KMeansPlusPlusClusterer(int k2, int maxIterations, DistanceMeasure measure, RandomGenerator random, EmptyClusterStrategy emptyStrategy) {
        super(measure);
        this.f13041k = k2;
        this.maxIterations = maxIterations;
        this.random = random;
        this.emptyStrategy = emptyStrategy;
    }

    public int getK() {
        return this.f13041k;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }

    public RandomGenerator getRandomGenerator() {
        return this.random;
    }

    public EmptyClusterStrategy getEmptyClusterStrategy() {
        return this.emptyStrategy;
    }

    @Override // org.apache.commons.math3.ml.clustering.Clusterer
    public List<CentroidCluster<T>> cluster(Collection<T> points) throws ConvergenceException, MathIllegalArgumentException {
        Clusterable newCenter;
        MathUtils.checkNotNull(points);
        if (points.size() < this.f13041k) {
            throw new NumberIsTooSmallException(Integer.valueOf(points.size()), Integer.valueOf(this.f13041k), false);
        }
        List<CentroidCluster<T>> clusters = chooseInitialCenters(points);
        int[] assignments = new int[points.size()];
        assignPointsToClusters(clusters, points, assignments);
        int max = this.maxIterations < 0 ? Integer.MAX_VALUE : this.maxIterations;
        for (int count = 0; count < max; count++) {
            boolean emptyCluster = false;
            List<CentroidCluster<T>> newClusters = new ArrayList<>();
            for (CentroidCluster<T> cluster : clusters) {
                if (cluster.getPoints().isEmpty()) {
                    switch (this.emptyStrategy) {
                        case LARGEST_VARIANCE:
                            newCenter = getPointFromLargestVarianceCluster(clusters);
                            break;
                        case LARGEST_POINTS_NUMBER:
                            newCenter = getPointFromLargestNumberCluster(clusters);
                            break;
                        case FARTHEST_POINT:
                            newCenter = getFarthestPoint(clusters);
                            break;
                        default:
                            throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, new Object[0]);
                    }
                    emptyCluster = true;
                } else {
                    newCenter = centroidOf(cluster.getPoints(), cluster.getCenter().getPoint().length);
                }
                newClusters.add(new CentroidCluster<>(newCenter));
            }
            int changes = assignPointsToClusters(newClusters, points, assignments);
            clusters = newClusters;
            if (changes == 0 && !emptyCluster) {
                return clusters;
            }
        }
        return clusters;
    }

    private int assignPointsToClusters(List<CentroidCluster<T>> clusters, Collection<T> points, int[] assignments) {
        int assignedDifferently = 0;
        int pointIndex = 0;
        for (T p2 : points) {
            int clusterIndex = getNearestCluster(clusters, p2);
            if (clusterIndex != assignments[pointIndex]) {
                assignedDifferently++;
            }
            CentroidCluster<T> cluster = clusters.get(clusterIndex);
            cluster.addPoint(p2);
            int i2 = pointIndex;
            pointIndex++;
            assignments[i2] = clusterIndex;
        }
        return assignedDifferently;
    }

    private List<CentroidCluster<T>> chooseInitialCenters(Collection<T> points) {
        List<T> pointList = Collections.unmodifiableList(new ArrayList(points));
        int numPoints = pointList.size();
        boolean[] taken = new boolean[numPoints];
        List<CentroidCluster<T>> resultSet = new ArrayList<>();
        int firstPointIndex = this.random.nextInt(numPoints);
        T firstPoint = pointList.get(firstPointIndex);
        resultSet.add(new CentroidCluster<>(firstPoint));
        taken[firstPointIndex] = true;
        double[] minDistSquared = new double[numPoints];
        for (int i2 = 0; i2 < numPoints; i2++) {
            if (i2 != firstPointIndex) {
                double d2 = distance(firstPoint, pointList.get(i2));
                minDistSquared[i2] = d2 * d2;
            }
        }
        while (resultSet.size() < this.f13041k) {
            double distSqSum = 0.0d;
            for (int i3 = 0; i3 < numPoints; i3++) {
                if (!taken[i3]) {
                    distSqSum += minDistSquared[i3];
                }
            }
            double r2 = this.random.nextDouble() * distSqSum;
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
            resultSet.add(new CentroidCluster<>(p2));
            taken[nextPointIndex] = true;
            if (resultSet.size() < this.f13041k) {
                for (int j2 = 0; j2 < numPoints; j2++) {
                    if (!taken[j2]) {
                        double d3 = distance(p2, pointList.get(j2));
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

    private T getPointFromLargestVarianceCluster(Collection<CentroidCluster<T>> clusters) throws ConvergenceException {
        double maxVariance = Double.NEGATIVE_INFINITY;
        Cluster<T> selected = null;
        for (CentroidCluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                Clusterable center = cluster.getCenter();
                Variance stat = new Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(distance(point, center));
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

    private T getPointFromLargestNumberCluster(Collection<? extends Cluster<T>> clusters) throws ConvergenceException {
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

    private T getFarthestPoint(Collection<CentroidCluster<T>> clusters) throws ConvergenceException {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<T> selectedCluster = null;
        int selectedPoint = -1;
        for (CentroidCluster<T> cluster : clusters) {
            Clusterable center = cluster.getCenter();
            List<T> points = cluster.getPoints();
            for (int i2 = 0; i2 < points.size(); i2++) {
                double distance = distance(points.get(i2), center);
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

    private int getNearestCluster(Collection<CentroidCluster<T>> clusters, T point) {
        double minDistance = Double.MAX_VALUE;
        int clusterIndex = 0;
        int minCluster = 0;
        for (CentroidCluster<T> c2 : clusters) {
            double distance = distance(point, c2.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = clusterIndex;
            }
            clusterIndex++;
        }
        return minCluster;
    }

    private Clusterable centroidOf(Collection<T> points, int dimension) {
        double[] centroid = new double[dimension];
        for (T p2 : points) {
            double[] point = p2.getPoint();
            for (int i2 = 0; i2 < centroid.length; i2++) {
                int i3 = i2;
                centroid[i3] = centroid[i3] + point[i2];
            }
        }
        for (int i4 = 0; i4 < centroid.length; i4++) {
            int i5 = i4;
            centroid[i5] = centroid[i5] / points.size();
        }
        return new DoublePoint(centroid);
    }
}
