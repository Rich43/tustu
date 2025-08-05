package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.SecondMoment;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/SummaryStatistics.class */
public class SummaryStatistics implements StatisticalSummary, Serializable {
    private static final long serialVersionUID = -2021321786743555871L;

    /* renamed from: n, reason: collision with root package name */
    private long f13098n = 0;
    private SecondMoment secondMoment = new SecondMoment();
    private Sum sum = new Sum();
    private SumOfSquares sumsq = new SumOfSquares();
    private Min min = new Min();
    private Max max = new Max();
    private SumOfLogs sumLog = new SumOfLogs();
    private GeometricMean geoMean = new GeometricMean(this.sumLog);
    private Mean mean = new Mean(this.secondMoment);
    private Variance variance = new Variance(this.secondMoment);
    private StorelessUnivariateStatistic sumImpl = this.sum;
    private StorelessUnivariateStatistic sumsqImpl = this.sumsq;
    private StorelessUnivariateStatistic minImpl = this.min;
    private StorelessUnivariateStatistic maxImpl = this.max;
    private StorelessUnivariateStatistic sumLogImpl = this.sumLog;
    private StorelessUnivariateStatistic geoMeanImpl = this.geoMean;
    private StorelessUnivariateStatistic meanImpl = this.mean;
    private StorelessUnivariateStatistic varianceImpl = this.variance;

    public SummaryStatistics() {
    }

    public SummaryStatistics(SummaryStatistics original) throws NullArgumentException {
        copy(original, this);
    }

    public StatisticalSummary getSummary() {
        return new StatisticalSummaryValues(getMean(), getVariance(), getN(), getMax(), getMin(), getSum());
    }

    public void addValue(double value) {
        this.sumImpl.increment(value);
        this.sumsqImpl.increment(value);
        this.minImpl.increment(value);
        this.maxImpl.increment(value);
        this.sumLogImpl.increment(value);
        this.secondMoment.increment(value);
        if (this.meanImpl != this.mean) {
            this.meanImpl.increment(value);
        }
        if (this.varianceImpl != this.variance) {
            this.varianceImpl.increment(value);
        }
        if (this.geoMeanImpl != this.geoMean) {
            this.geoMeanImpl.increment(value);
        }
        this.f13098n++;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.f13098n;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getSum() {
        return this.sumImpl.getResult();
    }

    public double getSumsq() {
        return this.sumsqImpl.getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMean() {
        return this.meanImpl.getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        double stdDev = Double.NaN;
        if (getN() > 0) {
            if (getN() > 1) {
                stdDev = FastMath.sqrt(getVariance());
            } else {
                stdDev = 0.0d;
            }
        }
        return stdDev;
    }

    public double getQuadraticMean() {
        long size = getN();
        if (size > 0) {
            return FastMath.sqrt(getSumsq() / size);
        }
        return Double.NaN;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getVariance() {
        return this.varianceImpl.getResult();
    }

    public double getPopulationVariance() {
        Variance populationVariance = new Variance(this.secondMoment);
        populationVariance.setBiasCorrected(false);
        return populationVariance.getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMax() {
        return this.maxImpl.getResult();
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMin() {
        return this.minImpl.getResult();
    }

    public double getGeometricMean() {
        return this.geoMeanImpl.getResult();
    }

    public double getSumOfLogs() {
        return this.sumLogImpl.getResult();
    }

    public double getSecondMoment() {
        return this.secondMoment.getResult();
    }

    public String toString() {
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("SummaryStatistics:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("sum: ").append(getSum()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("geometric mean: ").append(getGeometricMean()).append("\n");
        outBuffer.append("variance: ").append(getVariance()).append("\n");
        outBuffer.append("population variance: ").append(getPopulationVariance()).append("\n");
        outBuffer.append("second moment: ").append(getSecondMoment()).append("\n");
        outBuffer.append("sum of squares: ").append(getSumsq()).append("\n");
        outBuffer.append("standard deviation: ").append(getStandardDeviation()).append("\n");
        outBuffer.append("sum of logs: ").append(getSumOfLogs()).append("\n");
        return outBuffer.toString();
    }

    public void clear() {
        this.f13098n = 0L;
        this.minImpl.clear();
        this.maxImpl.clear();
        this.sumImpl.clear();
        this.sumLogImpl.clear();
        this.sumsqImpl.clear();
        this.geoMeanImpl.clear();
        this.secondMoment.clear();
        if (this.meanImpl != this.mean) {
            this.meanImpl.clear();
        }
        if (this.varianceImpl != this.variance) {
            this.varianceImpl.clear();
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SummaryStatistics)) {
            return false;
        }
        SummaryStatistics stat = (SummaryStatistics) object;
        return Precision.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) && Precision.equalsIncludingNaN(stat.getMax(), getMax()) && Precision.equalsIncludingNaN(stat.getMean(), getMean()) && Precision.equalsIncludingNaN(stat.getMin(), getMin()) && Precision.equalsIncludingNaN((float) stat.getN(), (float) getN()) && Precision.equalsIncludingNaN(stat.getSum(), getSum()) && Precision.equalsIncludingNaN(stat.getSumsq(), getSumsq()) && Precision.equalsIncludingNaN(stat.getVariance(), getVariance());
    }

    public int hashCode() {
        int result = 31 + MathUtils.hash(getGeometricMean());
        return (((((((((((((((result * 31) + MathUtils.hash(getGeometricMean())) * 31) + MathUtils.hash(getMax())) * 31) + MathUtils.hash(getMean())) * 31) + MathUtils.hash(getMin())) * 31) + MathUtils.hash(getN())) * 31) + MathUtils.hash(getSum())) * 31) + MathUtils.hash(getSumsq())) * 31) + MathUtils.hash(getVariance());
    }

    public StorelessUnivariateStatistic getSumImpl() {
        return this.sumImpl;
    }

    public void setSumImpl(StorelessUnivariateStatistic sumImpl) throws MathIllegalStateException {
        checkEmpty();
        this.sumImpl = sumImpl;
    }

    public StorelessUnivariateStatistic getSumsqImpl() {
        return this.sumsqImpl;
    }

    public void setSumsqImpl(StorelessUnivariateStatistic sumsqImpl) throws MathIllegalStateException {
        checkEmpty();
        this.sumsqImpl = sumsqImpl;
    }

    public StorelessUnivariateStatistic getMinImpl() {
        return this.minImpl;
    }

    public void setMinImpl(StorelessUnivariateStatistic minImpl) throws MathIllegalStateException {
        checkEmpty();
        this.minImpl = minImpl;
    }

    public StorelessUnivariateStatistic getMaxImpl() {
        return this.maxImpl;
    }

    public void setMaxImpl(StorelessUnivariateStatistic maxImpl) throws MathIllegalStateException {
        checkEmpty();
        this.maxImpl = maxImpl;
    }

    public StorelessUnivariateStatistic getSumLogImpl() {
        return this.sumLogImpl;
    }

    public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl) throws MathIllegalStateException {
        checkEmpty();
        this.sumLogImpl = sumLogImpl;
        this.geoMean.setSumLogImpl(sumLogImpl);
    }

    public StorelessUnivariateStatistic getGeoMeanImpl() {
        return this.geoMeanImpl;
    }

    public void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl) throws MathIllegalStateException {
        checkEmpty();
        this.geoMeanImpl = geoMeanImpl;
    }

    public StorelessUnivariateStatistic getMeanImpl() {
        return this.meanImpl;
    }

    public void setMeanImpl(StorelessUnivariateStatistic meanImpl) throws MathIllegalStateException {
        checkEmpty();
        this.meanImpl = meanImpl;
    }

    public StorelessUnivariateStatistic getVarianceImpl() {
        return this.varianceImpl;
    }

    public void setVarianceImpl(StorelessUnivariateStatistic varianceImpl) throws MathIllegalStateException {
        checkEmpty();
        this.varianceImpl = varianceImpl;
    }

    private void checkEmpty() throws MathIllegalStateException {
        if (this.f13098n > 0) {
            throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, Long.valueOf(this.f13098n));
        }
    }

    public SummaryStatistics copy() throws NullArgumentException {
        SummaryStatistics result = new SummaryStatistics();
        copy(this, result);
        return result;
    }

    public static void copy(SummaryStatistics source, SummaryStatistics dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.maxImpl = source.maxImpl.copy();
        dest.minImpl = source.minImpl.copy();
        dest.sumImpl = source.sumImpl.copy();
        dest.sumLogImpl = source.sumLogImpl.copy();
        dest.sumsqImpl = source.sumsqImpl.copy();
        dest.secondMoment = source.secondMoment.copy();
        dest.f13098n = source.f13098n;
        if (source.getVarianceImpl() instanceof Variance) {
            dest.varianceImpl = new Variance(dest.secondMoment);
        } else {
            dest.varianceImpl = source.varianceImpl.copy();
        }
        if (source.meanImpl instanceof Mean) {
            dest.meanImpl = new Mean(dest.secondMoment);
        } else {
            dest.meanImpl = source.meanImpl.copy();
        }
        if (source.getGeoMeanImpl() instanceof GeometricMean) {
            dest.geoMeanImpl = new GeometricMean((SumOfLogs) dest.sumLogImpl);
        } else {
            dest.geoMeanImpl = source.geoMeanImpl.copy();
        }
        if (source.geoMean == source.geoMeanImpl) {
            dest.geoMean = (GeometricMean) dest.geoMeanImpl;
        } else {
            GeometricMean.copy(source.geoMean, dest.geoMean);
        }
        if (source.max == source.maxImpl) {
            dest.max = (Max) dest.maxImpl;
        } else {
            Max.copy(source.max, dest.max);
        }
        if (source.mean == source.meanImpl) {
            dest.mean = (Mean) dest.meanImpl;
        } else {
            Mean.copy(source.mean, dest.mean);
        }
        if (source.min == source.minImpl) {
            dest.min = (Min) dest.minImpl;
        } else {
            Min.copy(source.min, dest.min);
        }
        if (source.sum == source.sumImpl) {
            dest.sum = (Sum) dest.sumImpl;
        } else {
            Sum.copy(source.sum, dest.sum);
        }
        if (source.variance == source.varianceImpl) {
            dest.variance = (Variance) dest.varianceImpl;
        } else {
            Variance.copy(source.variance, dest.variance);
        }
        if (source.sumLog == source.sumLogImpl) {
            dest.sumLog = (SumOfLogs) dest.sumLogImpl;
        } else {
            SumOfLogs.copy(source.sumLog, dest.sumLog);
        }
        if (source.sumsq == source.sumsqImpl) {
            dest.sumsq = (SumOfSquares) dest.sumsqImpl;
        } else {
            SumOfSquares.copy(source.sumsq, dest.sumsq);
        }
    }
}
