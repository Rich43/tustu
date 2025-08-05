package org.apache.commons.math3.geometry.spherical.oned;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/Arc.class */
public class Arc {
    private final double lower;
    private final double upper;
    private final double middle;
    private final double tolerance;

    public Arc(double lower, double upper, double tolerance) throws NumberIsTooLargeException {
        this.tolerance = tolerance;
        if (Precision.equals(lower, upper, 0) || upper - lower >= 6.283185307179586d) {
            this.lower = 0.0d;
            this.upper = 6.283185307179586d;
            this.middle = 3.141592653589793d;
        } else {
            if (lower <= upper) {
                this.lower = MathUtils.normalizeAngle(lower, 3.141592653589793d);
                this.upper = this.lower + (upper - lower);
                this.middle = 0.5d * (this.lower + this.upper);
                return;
            }
            throw new NumberIsTooLargeException(LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, Double.valueOf(lower), Double.valueOf(upper), true);
        }
    }

    public double getInf() {
        return this.lower;
    }

    public double getSup() {
        return this.upper;
    }

    public double getSize() {
        return this.upper - this.lower;
    }

    public double getBarycenter() {
        return this.middle;
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public Region.Location checkPoint(double point) {
        double normalizedPoint = MathUtils.normalizeAngle(point, this.middle);
        if (normalizedPoint < this.lower - this.tolerance || normalizedPoint > this.upper + this.tolerance) {
            return Region.Location.OUTSIDE;
        }
        if (normalizedPoint <= this.lower + this.tolerance || normalizedPoint >= this.upper - this.tolerance) {
            return getSize() >= 6.283185307179586d - this.tolerance ? Region.Location.INSIDE : Region.Location.BOUNDARY;
        }
        return Region.Location.INSIDE;
    }
}
