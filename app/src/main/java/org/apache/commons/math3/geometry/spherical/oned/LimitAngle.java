package org.apache.commons.math3.geometry.spherical.oned;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/LimitAngle.class */
public class LimitAngle implements Hyperplane<Sphere1D> {
    private S1Point location;
    private boolean direct;
    private final double tolerance;

    public LimitAngle(S1Point location, boolean direct, double tolerance) {
        this.location = location;
        this.direct = direct;
        this.tolerance = tolerance;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public LimitAngle copySelf() {
        return this;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getOffset(Point<Sphere1D> point) {
        double delta = ((S1Point) point).getAlpha() - this.location.getAlpha();
        return this.direct ? delta : -delta;
    }

    public boolean isDirect() {
        return this.direct;
    }

    public LimitAngle getReverse() {
        return new LimitAngle(this.location, !this.direct, this.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public SubLimitAngle wholeHyperplane() {
        return new SubLimitAngle(this, null);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public ArcsSet wholeSpace() {
        return new ArcsSet(this.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public boolean sameOrientationAs(Hyperplane<Sphere1D> other) {
        return !(this.direct ^ ((LimitAngle) other).direct);
    }

    public S1Point getLocation() {
        return this.location;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public Point<Sphere1D> project(Point<Sphere1D> point) {
        return this.location;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getTolerance() {
        return this.tolerance;
    }
}
