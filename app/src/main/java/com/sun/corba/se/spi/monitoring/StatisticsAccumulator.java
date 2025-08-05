package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/StatisticsAccumulator.class */
public class StatisticsAccumulator {
    protected String unit;
    protected double max = Double.MIN_VALUE;
    protected double min = Double.MAX_VALUE;
    private long sampleCount = 0;
    private double sampleSum = 0.0d;
    private double sampleSquareSum = 0.0d;

    public void sample(double d2) {
        this.sampleCount++;
        if (d2 < this.min) {
            this.min = d2;
        }
        if (d2 > this.max) {
            this.max = d2;
        }
        this.sampleSum += d2;
        this.sampleSquareSum += d2 * d2;
    }

    public String getValue() {
        return toString();
    }

    public String toString() {
        return "Minimum Value = " + this.min + " " + this.unit + " Maximum Value = " + this.max + " " + this.unit + " Average Value = " + computeAverage() + " " + this.unit + " Standard Deviation = " + computeStandardDeviation() + " " + this.unit + " Samples Collected = " + this.sampleCount;
    }

    protected double computeAverage() {
        return this.sampleSum / this.sampleCount;
    }

    protected double computeStandardDeviation() {
        return Math.sqrt((this.sampleSquareSum - ((this.sampleSum * this.sampleSum) / this.sampleCount)) / (this.sampleCount - 1));
    }

    public StatisticsAccumulator(String str) {
        this.unit = str;
    }

    void clearState() {
        this.min = Double.MAX_VALUE;
        this.max = Double.MIN_VALUE;
        this.sampleCount = 0L;
        this.sampleSum = 0.0d;
        this.sampleSquareSum = 0.0d;
    }

    public void unitTestValidate(String str, double d2, double d3, long j2, double d4, double d5) {
        if (!str.equals(this.unit)) {
            throw new RuntimeException("Unit is not same as expected Unit\nUnit = " + this.unit + "ExpectedUnit = " + str);
        }
        if (this.min != d2) {
            throw new RuntimeException("Minimum value is not same as expected minimum value\nMin Value = " + this.min + "Expected Min Value = " + d2);
        }
        if (this.max != d3) {
            throw new RuntimeException("Maximum value is not same as expected maximum value\nMax Value = " + this.max + "Expected Max Value = " + d3);
        }
        if (this.sampleCount != j2) {
            throw new RuntimeException("Sample count is not same as expected Sample Count\nSampleCount = " + this.sampleCount + "Expected Sample Count = " + j2);
        }
        if (computeAverage() != d4) {
            throw new RuntimeException("Average is not same as expected Average\nAverage = " + computeAverage() + "Expected Average = " + d4);
        }
        if (Math.abs(computeStandardDeviation() - d5) > 1.0d) {
            throw new RuntimeException("Standard Deviation is not same as expected Std Deviation\nStandard Dev = " + computeStandardDeviation() + "Expected Standard Dev = " + d5);
        }
    }
}
