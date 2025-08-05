package java.util;

import java.util.function.DoubleConsumer;

/* loaded from: rt.jar:java/util/DoubleSummaryStatistics.class */
public class DoubleSummaryStatistics implements DoubleConsumer {
    private long count;
    private double sum;
    private double sumCompensation;
    private double simpleSum;
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    @Override // java.util.function.DoubleConsumer
    public void accept(double d2) {
        this.count++;
        this.simpleSum += d2;
        sumWithCompensation(d2);
        this.min = Math.min(this.min, d2);
        this.max = Math.max(this.max, d2);
    }

    public void combine(DoubleSummaryStatistics doubleSummaryStatistics) {
        this.count += doubleSummaryStatistics.count;
        this.simpleSum += doubleSummaryStatistics.simpleSum;
        sumWithCompensation(doubleSummaryStatistics.sum);
        sumWithCompensation(doubleSummaryStatistics.sumCompensation);
        this.min = Math.min(this.min, doubleSummaryStatistics.min);
        this.max = Math.max(this.max, doubleSummaryStatistics.max);
    }

    private void sumWithCompensation(double d2) {
        double d3 = d2 - this.sumCompensation;
        double d4 = this.sum + d3;
        this.sumCompensation = (d4 - this.sum) - d3;
        this.sum = d4;
    }

    public final long getCount() {
        return this.count;
    }

    public final double getSum() {
        double d2 = this.sum + this.sumCompensation;
        if (Double.isNaN(d2) && Double.isInfinite(this.simpleSum)) {
            return this.simpleSum;
        }
        return d2;
    }

    public final double getMin() {
        return this.min;
    }

    public final double getMax() {
        return this.max;
    }

    public final double getAverage() {
        if (getCount() > 0) {
            return getSum() / getCount();
        }
        return 0.0d;
    }

    public String toString() {
        return String.format("%s{count=%d, sum=%f, min=%f, average=%f, max=%f}", getClass().getSimpleName(), Long.valueOf(getCount()), Double.valueOf(getSum()), Double.valueOf(getMin()), Double.valueOf(getAverage()), Double.valueOf(getMax()));
    }
}
