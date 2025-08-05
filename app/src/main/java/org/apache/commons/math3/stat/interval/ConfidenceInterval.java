package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/ConfidenceInterval.class */
public class ConfidenceInterval {
    private double lowerBound;
    private double upperBound;
    private double confidenceLevel;

    public ConfidenceInterval(double lowerBound, double upperBound, double confidenceLevel) {
        checkParameters(lowerBound, upperBound, confidenceLevel);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.confidenceLevel = confidenceLevel;
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public double getConfidenceLevel() {
        return this.confidenceLevel;
    }

    public String toString() {
        return "[" + this.lowerBound + ";" + this.upperBound + "] (confidence level:" + this.confidenceLevel + ")";
    }

    private void checkParameters(double lower, double upper, double confidence) {
        if (lower >= upper) {
            throw new MathIllegalArgumentException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower), Double.valueOf(upper));
        }
        if (confidence <= 0.0d || confidence >= 1.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.OUT_OF_BOUNDS_CONFIDENCE_LEVEL, Double.valueOf(confidence), 0, 1);
        }
    }
}
