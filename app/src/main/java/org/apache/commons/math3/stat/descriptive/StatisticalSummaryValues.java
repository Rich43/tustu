package org.apache.commons.math3.stat.descriptive;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/StatisticalSummaryValues.class */
public class StatisticalSummaryValues implements Serializable, StatisticalSummary {
    private static final long serialVersionUID = -5108854841843722536L;
    private final double mean;
    private final double variance;

    /* renamed from: n, reason: collision with root package name */
    private final long f13097n;
    private final double max;
    private final double min;
    private final double sum;

    public StatisticalSummaryValues(double mean, double variance, long n2, double max, double min, double sum) {
        this.mean = mean;
        this.variance = variance;
        this.f13097n = n2;
        this.max = max;
        this.min = min;
        this.sum = sum;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMax() {
        return this.max;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getMin() {
        return this.min;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public long getN() {
        return this.f13097n;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getSum() {
        return this.sum;
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getStandardDeviation() {
        return FastMath.sqrt(this.variance);
    }

    @Override // org.apache.commons.math3.stat.descriptive.StatisticalSummary
    public double getVariance() {
        return this.variance;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof StatisticalSummaryValues)) {
            return false;
        }
        StatisticalSummaryValues stat = (StatisticalSummaryValues) object;
        return Precision.equalsIncludingNaN(stat.getMax(), getMax()) && Precision.equalsIncludingNaN(stat.getMean(), getMean()) && Precision.equalsIncludingNaN(stat.getMin(), getMin()) && Precision.equalsIncludingNaN((float) stat.getN(), (float) getN()) && Precision.equalsIncludingNaN(stat.getSum(), getSum()) && Precision.equalsIncludingNaN(stat.getVariance(), getVariance());
    }

    public int hashCode() {
        int result = 31 + MathUtils.hash(getMax());
        return (((((((((result * 31) + MathUtils.hash(getMean())) * 31) + MathUtils.hash(getMin())) * 31) + MathUtils.hash(getN())) * 31) + MathUtils.hash(getSum())) * 31) + MathUtils.hash(getVariance());
    }

    public String toString() {
        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("StatisticalSummaryValues:").append("\n");
        outBuffer.append("n: ").append(getN()).append("\n");
        outBuffer.append("min: ").append(getMin()).append("\n");
        outBuffer.append("max: ").append(getMax()).append("\n");
        outBuffer.append("mean: ").append(getMean()).append("\n");
        outBuffer.append("std dev: ").append(getStandardDeviation()).append("\n");
        outBuffer.append("variance: ").append(getVariance()).append("\n");
        outBuffer.append("sum: ").append(getSum()).append("\n");
        return outBuffer.toString();
    }
}
