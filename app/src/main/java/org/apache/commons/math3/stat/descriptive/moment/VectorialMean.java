package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/moment/VectorialMean.class */
public class VectorialMean implements Serializable {
    private static final long serialVersionUID = 8223009086481006892L;
    private final Mean[] means;

    public VectorialMean(int dimension) {
        this.means = new Mean[dimension];
        for (int i2 = 0; i2 < dimension; i2++) {
            this.means[i2] = new Mean();
        }
    }

    public void increment(double[] v2) throws DimensionMismatchException {
        if (v2.length != this.means.length) {
            throw new DimensionMismatchException(v2.length, this.means.length);
        }
        for (int i2 = 0; i2 < v2.length; i2++) {
            this.means[i2].increment(v2[i2]);
        }
    }

    public double[] getResult() {
        double[] result = new double[this.means.length];
        for (int i2 = 0; i2 < result.length; i2++) {
            result[i2] = this.means[i2].getResult();
        }
        return result;
    }

    public long getN() {
        if (this.means.length == 0) {
            return 0L;
        }
        return this.means[0].getN();
    }

    public int hashCode() {
        int result = (31 * 1) + Arrays.hashCode(this.means);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VectorialMean)) {
            return false;
        }
        VectorialMean other = (VectorialMean) obj;
        if (!Arrays.equals(this.means, other.means)) {
            return false;
        }
        return true;
    }
}
