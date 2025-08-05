package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/MultivariateSummaryStatistics.class */
public class MultivariateSummaryStatistics implements StatisticalMultivariateSummary, Serializable {
    private static final long serialVersionUID = 2271900808994826718L;

    /* renamed from: k, reason: collision with root package name */
    private int f13095k;

    /* renamed from: n, reason: collision with root package name */
    private long f13096n = 0;
    private StorelessUnivariateStatistic[] sumImpl;
    private StorelessUnivariateStatistic[] sumSqImpl;
    private StorelessUnivariateStatistic[] minImpl;
    private StorelessUnivariateStatistic[] maxImpl;
    private StorelessUnivariateStatistic[] sumLogImpl;
    private StorelessUnivariateStatistic[] geoMeanImpl;
    private StorelessUnivariateStatistic[] meanImpl;
    private VectorialCovariance covarianceImpl;

    public MultivariateSummaryStatistics(int k2, boolean isCovarianceBiasCorrected) {
        this.f13095k = k2;
        this.sumImpl = new StorelessUnivariateStatistic[k2];
        this.sumSqImpl = new StorelessUnivariateStatistic[k2];
        this.minImpl = new StorelessUnivariateStatistic[k2];
        this.maxImpl = new StorelessUnivariateStatistic[k2];
        this.sumLogImpl = new StorelessUnivariateStatistic[k2];
        this.geoMeanImpl = new StorelessUnivariateStatistic[k2];
        this.meanImpl = new StorelessUnivariateStatistic[k2];
        for (int i2 = 0; i2 < k2; i2++) {
            this.sumImpl[i2] = new Sum();
            this.sumSqImpl[i2] = new SumOfSquares();
            this.minImpl[i2] = new Min();
            this.maxImpl[i2] = new Max();
            this.sumLogImpl[i2] = new SumOfLogs();
            this.geoMeanImpl[i2] = new GeometricMean();
            this.meanImpl[i2] = new Mean();
        }
        this.covarianceImpl = new VectorialCovariance(k2, isCovarianceBiasCorrected);
    }

    public void addValue(double[] value) throws DimensionMismatchException {
        checkDimension(value.length);
        for (int i2 = 0; i2 < this.f13095k; i2++) {
            double v2 = value[i2];
            this.sumImpl[i2].increment(v2);
            this.sumSqImpl[i2].increment(v2);
            this.minImpl[i2].increment(v2);
            this.maxImpl[i2].increment(v2);
            this.sumLogImpl[i2].increment(v2);
            this.geoMeanImpl[i2].increment(v2);
            this.meanImpl[i2].increment(v2);
        }
        this.covarianceImpl.increment(value);
        this.f13096n++;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public int getDimension() {
        return this.f13095k;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public long getN() {
        return this.f13096n;
    }

    private double[] getResults(StorelessUnivariateStatistic[] stats) {
        double[] results = new double[stats.length];
        for (int i2 = 0; i2 < results.length; i2++) {
            results[i2] = stats[i2].getResult();
        }
        return results;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSum() {
        return getResults(this.sumImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSumSq() {
        return getResults(this.sumSqImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getSumLog() {
        return getResults(this.sumLogImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMean() {
        return getResults(this.meanImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getStandardDeviation() throws OutOfRangeException {
        double[] stdDev = new double[this.f13095k];
        if (getN() < 1) {
            Arrays.fill(stdDev, Double.NaN);
        } else if (getN() < 2) {
            Arrays.fill(stdDev, 0.0d);
        } else {
            RealMatrix matrix = this.covarianceImpl.getResult();
            for (int i2 = 0; i2 < this.f13095k; i2++) {
                stdDev[i2] = FastMath.sqrt(matrix.getEntry(i2, i2));
            }
        }
        return stdDev;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public RealMatrix getCovariance() {
        return this.covarianceImpl.getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMax() {
        return getResults(this.maxImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getMin() {
        return getResults(this.minImpl);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary
    public double[] getGeometricMean() {
        return getResults(this.geoMeanImpl);
    }

    public String toString() {
        String suffix = System.getProperty("line.separator");
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("MultivariateSummaryStatistics:" + suffix);
        outBuffer.append("n: " + getN() + suffix);
        append(outBuffer, getMin(), "min: ", ", ", suffix);
        append(outBuffer, getMax(), "max: ", ", ", suffix);
        append(outBuffer, getMean(), "mean: ", ", ", suffix);
        append(outBuffer, getGeometricMean(), "geometric mean: ", ", ", suffix);
        append(outBuffer, getSumSq(), "sum of squares: ", ", ", suffix);
        append(outBuffer, getSumLog(), "sum of logarithms: ", ", ", suffix);
        append(outBuffer, getStandardDeviation(), "standard deviation: ", ", ", suffix);
        outBuffer.append("covariance: " + getCovariance().toString() + suffix);
        return outBuffer.toString();
    }

    private void append(StringBuilder buffer, double[] data, String prefix, String separator, String suffix) {
        buffer.append(prefix);
        for (int i2 = 0; i2 < data.length; i2++) {
            if (i2 > 0) {
                buffer.append(separator);
            }
            buffer.append(data[i2]);
        }
        buffer.append(suffix);
    }

    public void clear() {
        this.f13096n = 0L;
        for (int i2 = 0; i2 < this.f13095k; i2++) {
            this.minImpl[i2].clear();
            this.maxImpl[i2].clear();
            this.sumImpl[i2].clear();
            this.sumLogImpl[i2].clear();
            this.sumSqImpl[i2].clear();
            this.geoMeanImpl[i2].clear();
            this.meanImpl[i2].clear();
        }
        this.covarianceImpl.clear();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MultivariateSummaryStatistics)) {
            return false;
        }
        MultivariateSummaryStatistics stat = (MultivariateSummaryStatistics) object;
        return MathArrays.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) && MathArrays.equalsIncludingNaN(stat.getMax(), getMax()) && MathArrays.equalsIncludingNaN(stat.getMean(), getMean()) && MathArrays.equalsIncludingNaN(stat.getMin(), getMin()) && Precision.equalsIncludingNaN((float) stat.getN(), (float) getN()) && MathArrays.equalsIncludingNaN(stat.getSum(), getSum()) && MathArrays.equalsIncludingNaN(stat.getSumSq(), getSumSq()) && MathArrays.equalsIncludingNaN(stat.getSumLog(), getSumLog()) && stat.getCovariance().equals(getCovariance());
    }

    public int hashCode() {
        int result = 31 + MathUtils.hash(getGeometricMean());
        return (((((((((((((((((result * 31) + MathUtils.hash(getGeometricMean())) * 31) + MathUtils.hash(getMax())) * 31) + MathUtils.hash(getMean())) * 31) + MathUtils.hash(getMin())) * 31) + MathUtils.hash(getN())) * 31) + MathUtils.hash(getSum())) * 31) + MathUtils.hash(getSumSq())) * 31) + MathUtils.hash(getSumLog())) * 31) + getCovariance().hashCode();
    }

    private void setImpl(StorelessUnivariateStatistic[] newImpl, StorelessUnivariateStatistic[] oldImpl) throws MathIllegalStateException, DimensionMismatchException {
        checkEmpty();
        checkDimension(newImpl.length);
        System.arraycopy(newImpl, 0, oldImpl, 0, newImpl.length);
    }

    public StorelessUnivariateStatistic[] getSumImpl() {
        return (StorelessUnivariateStatistic[]) this.sumImpl.clone();
    }

    public void setSumImpl(StorelessUnivariateStatistic[] sumImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumImpl, this.sumImpl);
    }

    public StorelessUnivariateStatistic[] getSumsqImpl() {
        return (StorelessUnivariateStatistic[]) this.sumSqImpl.clone();
    }

    public void setSumsqImpl(StorelessUnivariateStatistic[] sumsqImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumsqImpl, this.sumSqImpl);
    }

    public StorelessUnivariateStatistic[] getMinImpl() {
        return (StorelessUnivariateStatistic[]) this.minImpl.clone();
    }

    public void setMinImpl(StorelessUnivariateStatistic[] minImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(minImpl, this.minImpl);
    }

    public StorelessUnivariateStatistic[] getMaxImpl() {
        return (StorelessUnivariateStatistic[]) this.maxImpl.clone();
    }

    public void setMaxImpl(StorelessUnivariateStatistic[] maxImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(maxImpl, this.maxImpl);
    }

    public StorelessUnivariateStatistic[] getSumLogImpl() {
        return (StorelessUnivariateStatistic[]) this.sumLogImpl.clone();
    }

    public void setSumLogImpl(StorelessUnivariateStatistic[] sumLogImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumLogImpl, this.sumLogImpl);
    }

    public StorelessUnivariateStatistic[] getGeoMeanImpl() {
        return (StorelessUnivariateStatistic[]) this.geoMeanImpl.clone();
    }

    public void setGeoMeanImpl(StorelessUnivariateStatistic[] geoMeanImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(geoMeanImpl, this.geoMeanImpl);
    }

    public StorelessUnivariateStatistic[] getMeanImpl() {
        return (StorelessUnivariateStatistic[]) this.meanImpl.clone();
    }

    public void setMeanImpl(StorelessUnivariateStatistic[] meanImpl) throws MathIllegalStateException, DimensionMismatchException {
        setImpl(meanImpl, this.meanImpl);
    }

    private void checkEmpty() throws MathIllegalStateException {
        if (this.f13096n > 0) {
            throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, Long.valueOf(this.f13096n));
        }
    }

    private void checkDimension(int dimension) throws DimensionMismatchException {
        if (dimension != this.f13095k) {
            throw new DimensionMismatchException(dimension, this.f13095k);
        }
    }
}
