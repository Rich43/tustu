package org.apache.commons.math3.geometry.spherical.twod;

import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Transform;
import org.apache.commons.math3.geometry.spherical.oned.Arc;
import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.apache.commons.math3.geometry.spherical.oned.S1Point;
import org.apache.commons.math3.geometry.spherical.oned.Sphere1D;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Circle.class */
public class Circle implements Hyperplane<Sphere2D>, Embedding<Sphere2D, Sphere1D> {
    private Vector3D pole;

    /* renamed from: x, reason: collision with root package name */
    private Vector3D f13020x;

    /* renamed from: y, reason: collision with root package name */
    private Vector3D f13021y;
    private final double tolerance;

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSpace(Point point) {
        return toSpace((Point<Sphere1D>) point);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSubSpace(Point point) {
        return toSubSpace((Point<Sphere2D>) point);
    }

    public Circle(Vector3D pole, double tolerance) {
        reset(pole);
        this.tolerance = tolerance;
    }

    public Circle(S2Point first, S2Point second, double tolerance) {
        reset(first.getVector().crossProduct(second.getVector()));
        this.tolerance = tolerance;
    }

    private Circle(Vector3D pole, Vector3D x2, Vector3D y2, double tolerance) {
        this.pole = pole;
        this.f13020x = x2;
        this.f13021y = y2;
        this.tolerance = tolerance;
    }

    public Circle(Circle circle) {
        this(circle.pole, circle.f13020x, circle.f13021y, circle.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public Circle copySelf() {
        return new Circle(this);
    }

    public void reset(Vector3D newPole) {
        this.pole = newPole.normalize();
        this.f13020x = newPole.orthogonal();
        this.f13021y = Vector3D.crossProduct(newPole, this.f13020x).normalize();
    }

    public void revertSelf() {
        this.f13021y = this.f13021y.negate();
        this.pole = this.pole.negate();
    }

    public Circle getReverse() {
        return new Circle(this.pole.negate(), this.f13020x, this.f13021y.negate(), this.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public Point<Sphere2D> project(Point<Sphere2D> point) {
        return toSpace((Point<Sphere1D>) toSubSpace(point));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getTolerance() {
        return this.tolerance;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public S1Point toSubSpace(Point<Sphere2D> point) {
        return new S1Point(getPhase(((S2Point) point).getVector()));
    }

    public double getPhase(Vector3D direction) {
        return 3.141592653589793d + FastMath.atan2(-direction.dotProduct(this.f13021y), -direction.dotProduct(this.f13020x));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public S2Point toSpace(Point<Sphere1D> point) {
        return new S2Point(getPointAt(((S1Point) point).getAlpha()));
    }

    public Vector3D getPointAt(double alpha) {
        return new Vector3D(FastMath.cos(alpha), this.f13020x, FastMath.sin(alpha), this.f13021y);
    }

    public Vector3D getXAxis() {
        return this.f13020x;
    }

    public Vector3D getYAxis() {
        return this.f13021y;
    }

    public Vector3D getPole() {
        return this.pole;
    }

    public Arc getInsideArc(Circle other) {
        double alpha = getPhase(other.pole);
        return new Arc(alpha - 1.5707963267948966d, alpha + 1.5707963267948966d, this.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public SubCircle wholeHyperplane() {
        return new SubCircle(this, new ArcsSet(this.tolerance));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public SphericalPolygonsSet wholeSpace() {
        return new SphericalPolygonsSet(this.tolerance);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getOffset(Point<Sphere2D> point) {
        return getOffset(((S2Point) point).getVector());
    }

    public double getOffset(Vector3D direction) {
        return Vector3D.angle(this.pole, direction) - 1.5707963267948966d;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public boolean sameOrientationAs(Hyperplane<Sphere2D> other) {
        Circle otherC = (Circle) other;
        return Vector3D.dotProduct(this.pole, otherC.pole) >= 0.0d;
    }

    public static Transform<Sphere2D, Sphere1D> getTransform(Rotation rotation) {
        return new CircleTransform(rotation);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/Circle$CircleTransform.class */
    private static class CircleTransform implements Transform<Sphere2D, Sphere1D> {
        private final Rotation rotation;

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Hyperplane apply(Hyperplane hyperplane) {
            return apply((Hyperplane<Sphere2D>) hyperplane);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public /* bridge */ /* synthetic */ Point apply(Point point) {
            return apply((Point<Sphere2D>) point);
        }

        CircleTransform(Rotation rotation) {
            this.rotation = rotation;
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public S2Point apply(Point<Sphere2D> point) {
            return new S2Point(this.rotation.applyTo(((S2Point) point).getVector()));
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public Circle apply(Hyperplane<Sphere2D> hyperplane) {
            Circle circle = (Circle) hyperplane;
            return new Circle(this.rotation.applyTo(circle.pole), this.rotation.applyTo(circle.f13020x), this.rotation.applyTo(circle.f13021y), circle.tolerance);
        }

        @Override // org.apache.commons.math3.geometry.partitioning.Transform
        public SubHyperplane<Sphere1D> apply(SubHyperplane<Sphere1D> sub, Hyperplane<Sphere2D> original, Hyperplane<Sphere2D> transformed) {
            return sub;
        }
    }
}
