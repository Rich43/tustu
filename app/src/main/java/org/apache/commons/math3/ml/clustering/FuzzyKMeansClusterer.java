package org.apache.commons.math3.ml.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/clustering/FuzzyKMeansClusterer.class */
public class FuzzyKMeansClusterer<T extends Clusterable> extends Clusterer<T> {
    private static final double DEFAULT_EPSILON = 0.001d;

    /* renamed from: k, reason: collision with root package name */
    private final int f13040k;
    private final int maxIterations;
    private final double fuzziness;
    private final double epsilon;
    private final RandomGenerator random;
    private double[][] membershipMatrix;
    private List<T> points;
    private List<CentroidCluster<T>> clusters;

    public FuzzyKMeansClusterer(int k2, double fuzziness) throws NumberIsTooSmallException {
        this(k2, fuzziness, -1, new EuclideanDistance());
    }

    public FuzzyKMeansClusterer(int k2, double fuzziness, int maxIterations, DistanceMeasure measure) throws NumberIsTooSmallException {
        this(k2, fuzziness, maxIterations, measure, 0.001d, new JDKRandomGenerator());
    }

    public FuzzyKMeansClusterer(int k2, double fuzziness, int maxIterations, DistanceMeasure measure, double epsilon, RandomGenerator random) throws NumberIsTooSmallException {
        super(measure);
        if (fuzziness <= 1.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(fuzziness), Double.valueOf(1.0d), false);
        }
        this.f13040k = k2;
        this.fuzziness = fuzziness;
        this.maxIterations = maxIterations;
        this.epsilon = epsilon;
        this.random = random;
        this.membershipMatrix = (double[][]) null;
        this.points = null;
        this.clusters = null;
    }

    public int getK() {
        return this.f13040k;
    }

    public double getFuzziness() {
        return this.fuzziness;
    }

    public int getMaxIterations() {
        return this.maxIterations;
    }

    public double getEpsilon() {
        return this.epsilon;
    }

    public RandomGenerator getRandomGenerator() {
        return this.random;
    }

    public RealMatrix getMembershipMatrix() {
        if (this.membershipMatrix == null) {
            throw new MathIllegalStateException();
        }
        return MatrixUtils.createRealMatrix(this.membershipMatrix);
    }

    public List<T> getDataPoints() {
        return this.points;
    }

    public List<CentroidCluster<T>> getClusters() {
        return this.clusters;
    }

    public double getObjectiveFunctionValue() {
        if (this.points == null || this.clusters == null) {
            throw new MathIllegalStateException();
        }
        int i2 = 0;
        double objFunction = 0.0d;
        for (T point : this.points) {
            int j2 = 0;
            for (CentroidCluster<T> cluster : this.clusters) {
                double dist = distance(point, cluster.getCenter());
                objFunction += dist * dist * FastMath.pow(this.membershipMatrix[i2][j2], this.fuzziness);
                j2++;
            }
            i2++;
        }
        return objFunction;
    }

    @Override // org.apache.commons.math3.ml.clustering.Clusterer
    public List<CentroidCluster<T>> cluster(Collection<T> dataPoints) throws MathIllegalArgumentException {
        MathUtils.checkNotNull(dataPoints);
        int size = dataPoints.size();
        if (size < this.f13040k) {
            throw new NumberIsTooSmallException(Integer.valueOf(size), Integer.valueOf(this.f13040k), false);
        }
        this.points = Collections.unmodifiableList(new ArrayList(dataPoints));
        this.clusters = new ArrayList();
        this.membershipMatrix = new double[size][this.f13040k];
        double[][] oldMatrix = new double[size][this.f13040k];
        if (size == 0) {
            return this.clusters;
        }
        initializeMembershipMatrix();
        int pointDimension = this.points.get(0).getPoint().length;
        for (int i2 = 0; i2 < this.f13040k; i2++) {
            this.clusters.add(new CentroidCluster<>(new DoublePoint(new double[pointDimension])));
        }
        int iteration = 0;
        int max = this.maxIterations < 0 ? Integer.MAX_VALUE : this.maxIterations;
        do {
            saveMembershipMatrix(oldMatrix);
            updateClusterCenters();
            updateMembershipMatrix();
            double difference = calculateMaxMembershipChange(oldMatrix);
            if (difference <= this.epsilon) {
                break;
            }
            iteration++;
        } while (iteration < max);
        return this.clusters;
    }

    private void updateClusterCenters() {
        int j2 = 0;
        List<CentroidCluster<T>> newClusters = new ArrayList<>(this.f13040k);
        for (CentroidCluster<T> cluster : this.clusters) {
            Clusterable center = cluster.getCenter();
            int i2 = 0;
            double[] arr = new double[center.getPoint().length];
            double sum = 0.0d;
            for (T point : this.points) {
                double u2 = FastMath.pow(this.membershipMatrix[i2][j2], this.fuzziness);
                double[] pointArr = point.getPoint();
                for (int idx = 0; idx < arr.length; idx++) {
                    int i3 = idx;
                    arr[i3] = arr[i3] + (u2 * pointArr[idx]);
                }
                sum += u2;
                i2++;
            }
            MathArrays.scaleInPlace(1.0d / sum, arr);
            newClusters.add(new CentroidCluster<>(new DoublePoint(arr)));
            j2++;
        }
        this.clusters.clear();
        this.clusters = newClusters;
    }

    private void updateMembershipMatrix() {
        double membership;
        for (int i2 = 0; i2 < this.points.size(); i2++) {
            T point = this.points.get(i2);
            double maxMembership = Double.MIN_VALUE;
            int newCluster = -1;
            for (int j2 = 0; j2 < this.clusters.size(); j2++) {
                double sum = 0.0d;
                double distA = FastMath.abs(distance(point, this.clusters.get(j2).getCenter()));
                if (distA != 0.0d) {
                    Iterator i$ = this.clusters.iterator();
                    while (true) {
                        if (!i$.hasNext()) {
                            break;
                        }
                        CentroidCluster<T> c2 = i$.next();
                        double distB = FastMath.abs(distance(point, c2.getCenter()));
                        if (distB == 0.0d) {
                            sum = Double.POSITIVE_INFINITY;
                            break;
                        }
                        sum += FastMath.pow(distA / distB, 2.0d / (this.fuzziness - 1.0d));
                    }
                }
                if (sum == 0.0d) {
                    membership = 1.0d;
                } else if (sum == Double.POSITIVE_INFINITY) {
                    membership = 0.0d;
                } else {
                    membership = 1.0d / sum;
                }
                this.membershipMatrix[i2][j2] = membership;
                if (this.membershipMatrix[i2][j2] > maxMembership) {
                    maxMembership = this.membershipMatrix[i2][j2];
                    newCluster = j2;
                }
            }
            this.clusters.get(newCluster).addPoint(point);
        }
    }

    private void initializeMembershipMatrix() {
        for (int i2 = 0; i2 < this.points.size(); i2++) {
            for (int j2 = 0; j2 < this.f13040k; j2++) {
                this.membershipMatrix[i2][j2] = this.random.nextDouble();
            }
            this.membershipMatrix[i2] = MathArrays.normalizeArray(this.membershipMatrix[i2], 1.0d);
        }
    }

    private double calculateMaxMembershipChange(double[][] matrix) {
        double maxMembership = 0.0d;
        for (int i2 = 0; i2 < this.points.size(); i2++) {
            for (int j2 = 0; j2 < this.clusters.size(); j2++) {
                double v2 = FastMath.abs(this.membershipMatrix[i2][j2] - matrix[i2][j2]);
                maxMembership = FastMath.max(v2, maxMembership);
            }
        }
        return maxMembership;
    }

    private void saveMembershipMatrix(double[][] matrix) {
        for (int i2 = 0; i2 < this.points.size(); i2++) {
            System.arraycopy(this.membershipMatrix[i2], 0, matrix[i2], 0, this.clusters.size());
        }
    }
}
