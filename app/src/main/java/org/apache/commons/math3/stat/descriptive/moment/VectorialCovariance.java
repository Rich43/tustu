package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/VectorialCovariance.class */
public class VectorialCovariance implements Serializable {
    private static final long serialVersionUID = 4118372414238930270L;
    private final double[] sums;
    private final double[] productsSums;
    private final boolean isBiasCorrected;

    /* renamed from: n, reason: collision with root package name */
    private long f13100n = 0;

    public VectorialCovariance(int dimension, boolean isBiasCorrected) {
        this.sums = new double[dimension];
        this.productsSums = new double[(dimension * (dimension + 1)) / 2];
        this.isBiasCorrected = isBiasCorrected;
    }

    public void increment(double[] v2) throws DimensionMismatchException {
        if (v2.length != this.sums.length) {
            throw new DimensionMismatchException(v2.length, this.sums.length);
        }
        int k2 = 0;
        for (int i2 = 0; i2 < v2.length; i2++) {
            double[] dArr = this.sums;
            int i3 = i2;
            dArr[i3] = dArr[i3] + v2[i2];
            for (int j2 = 0; j2 <= i2; j2++) {
                double[] dArr2 = this.productsSums;
                int i4 = k2;
                k2++;
                dArr2[i4] = dArr2[i4] + (v2[i2] * v2[j2]);
            }
        }
        this.f13100n++;
    }

    public RealMatrix getResult() throws OutOfRangeException {
        int dimension = this.sums.length;
        RealMatrix result = MatrixUtils.createRealMatrix(dimension, dimension);
        if (this.f13100n > 1) {
            double c2 = 1.0d / (this.f13100n * (this.isBiasCorrected ? this.f13100n - 1 : this.f13100n));
            int k2 = 0;
            for (int i2 = 0; i2 < dimension; i2++) {
                for (int j2 = 0; j2 <= i2; j2++) {
                    int i3 = k2;
                    k2++;
                    double e2 = c2 * ((this.f13100n * this.productsSums[i3]) - (this.sums[i2] * this.sums[j2]));
                    result.setEntry(i2, j2, e2);
                    result.setEntry(j2, i2, e2);
                }
            }
        }
        return result;
    }

    public long getN() {
        return this.f13100n;
    }

    public void clear() {
        this.f13100n = 0L;
        Arrays.fill(this.sums, 0.0d);
        Arrays.fill(this.productsSums, 0.0d);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.isBiasCorrected ? 1231 : 1237);
        return (31 * ((31 * ((31 * result) + ((int) (this.f13100n ^ (this.f13100n >>> 32))))) + Arrays.hashCode(this.productsSums))) + Arrays.hashCode(this.sums);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VectorialCovariance)) {
            return false;
        }
        VectorialCovariance other = (VectorialCovariance) obj;
        if (this.isBiasCorrected != other.isBiasCorrected || this.f13100n != other.f13100n || !Arrays.equals(this.productsSums, other.productsSums) || !Arrays.equals(this.sums, other.sums)) {
            return false;
        }
        return true;
    }
}
