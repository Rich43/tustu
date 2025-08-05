package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.partitioning.Region;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/oned/Interval.class */
public class Interval {
    private final double lower;
    private final double upper;

    public Interval(double lower, double upper) {
        if (upper < lower) {
            throw new NumberIsTooSmallException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(upper), Double.valueOf(lower), true);
        }
        this.lower = lower;
        this.upper = upper;
    }

    public double getInf() {
        return this.lower;
    }

    @Deprecated
    public double getLower() {
        return getInf();
    }

    public double getSup() {
        return this.upper;
    }

    @Deprecated
    public double getUpper() {
        return getSup();
    }

    public double getSize() {
        return this.upper - this.lower;
    }

    @Deprecated
    public double getLength() {
        return getSize();
    }

    public double getBarycenter() {
        return 0.5d * (this.lower + this.upper);
    }

    @Deprecated
    public double getMidPoint() {
        return getBarycenter();
    }

    public Region.Location checkPoint(double point, double tolerance) {
        if (point < this.lower - tolerance || point > this.upper + tolerance) {
            return Region.Location.OUTSIDE;
        }
        if (point > this.lower + tolerance && point < this.upper - tolerance) {
            return Region.Location.INSIDE;
        }
        return Region.Location.BOUNDARY;
    }
}
