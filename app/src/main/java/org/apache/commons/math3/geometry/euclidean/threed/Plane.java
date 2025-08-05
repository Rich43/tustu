package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/Plane.class */
public class Plane implements Hyperplane<Euclidean3D>, Embedding<Euclidean3D, Euclidean2D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private double originOffset;
    private Vector3D origin;

    /* renamed from: u, reason: collision with root package name */
    private Vector3D f13007u;

    /* renamed from: v, reason: collision with root package name */
    private Vector3D f13008v;

    /* renamed from: w, reason: collision with root package name */
    private Vector3D f13009w;
    private final double tolerance;

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSpace(Point point) {
        return toSpace((Point<Euclidean2D>) point);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSubSpace(Point point) {
        return toSubSpace((Point<Euclidean3D>) point);
    }

    public Plane(Vector3D normal, double tolerance) throws MathArithmeticException {
        setNormal(normal);
        this.tolerance = tolerance;
        this.originOffset = 0.0d;
        setFrame();
    }

    public Plane(Vector3D p2, Vector3D normal, double tolerance) throws MathArithmeticException {
        setNormal(normal);
        this.tolerance = tolerance;
        this.originOffset = -p2.dotProduct(this.f13009w);
        setFrame();
    }

    public Plane(Vector3D p1, Vector3D p2, Vector3D p3, double tolerance) throws MathArithmeticException {
        this(p1, p2.subtract((Vector<Euclidean3D>) p1).crossProduct(p3.subtract((Vector<Euclidean3D>) p1)), tolerance);
    }

    @Deprecated
    public Plane(Vector3D normal) throws MathArithmeticException {
        this(normal, 1.0E-10d);
    }

    @Deprecated
    public Plane(Vector3D p2, Vector3D normal) throws MathArithmeticException {
        this(p2, normal, 1.0E-10d);
    }

    @Deprecated
    public Plane(Vector3D p1, Vector3D p2, Vector3D p3) throws MathArithmeticException {
        this(p1, p2, p3, 1.0E-10d);
    }

    public Plane(Plane plane) {
        this.originOffset = plane.originOffset;
        this.origin = plane.origin;
        this.f13007u = plane.f13007u;
        this.f13008v = plane.f13008v;
        this.f13009w = plane.f13009w;
        this.tolerance = plane.tolerance;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public Plane copySelf() {
        return new Plane(this);
    }

    public void reset(Vector3D p2, Vector3D normal) throws MathArithmeticException {
        setNormal(normal);
        this.originOffset = -p2.dotProduct(this.f13009w);
        setFrame();
    }

    public void reset(Plane original) {
        this.originOffset = original.originOffset;
        this.origin = original.origin;
        this.f13007u = original.f13007u;
        this.f13008v = original.f13008v;
        this.f13009w = original.f13009w;
    }

    private void setNormal(Vector3D normal) throws MathArithmeticException {
        double norm = normal.getNorm();
        if (norm < 1.0E-10d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        this.f13009w = new Vector3D(1.0d / norm, normal);
    }

    private void setFrame() {
        this.origin = new Vector3D(-this.originOffset, this.f13009w);
        this.f13007u = this.f13009w.orthogonal();
        this.f13008v = Vector3D.crossProduct(this.f13009w, this.f13007u);
    }

    public Vector3D getOrigin() {
        return this.origin;
    }

    public Vector3D getNormal() {
        return this.f13009w;
    }

    public Vector3D getU() {
        return this.f13007u;
    }

    public Vector3D getV() {
        return this.f13008v;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public Point<Euclidean3D> project(Point<Euclidean3D> point) {
        return toSpace((Vector<Euclidean2D>) toSubSpace(point));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getTolerance() {
        return this.tolerance;
    }

    public void revertSelf() {
        Vector3D tmp = this.f13007u;
        this.f13007u = this.f13008v;
        this.f13008v = tmp;
        this.f13009w = this.f13009w.negate();
        this.originOffset = -this.originOffset;
    }

    public Vector2D toSubSpace(Vector<Euclidean3D> vector) {
        return toSubSpace((Point<Euclidean3D>) vector);
    }

    public Vector3D toSpace(Vector<Euclidean2D> vector) {
        return toSpace((Point<Euclidean2D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public Vector2D toSubSpace(Point<Euclidean3D> point) {
        Vector3D p3D = (Vector3D) point;
        return new Vector2D(p3D.dotProduct(this.f13007u), p3D.dotProduct(this.f13008v));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public Vector3D toSpace(Point<Euclidean2D> point) {
        Vector2D p2D = (Vector2D) point;
        return new Vector3D(p2D.getX(), this.f13007u, p2D.getY(), this.f13008v, -this.originOffset, this.f13009w);
    }

    public Vector3D getPointAt(Vector2D inPlane, double offset) {
        return new Vector3D(inPlane.getX(), this.f13007u, inPlane.getY(), this.f13008v, offset - this.originOffset, this.f13009w);
    }

    public boolean isSimilarTo(Plane plane) throws MathArithmeticException {
        double angle = Vector3D.angle(this.f13009w, plane.f13009w);
        return (angle < 1.0E-10d && FastMath.abs(this.originOffset - plane.originOffset) < this.tolerance) || (angle > 3.141592653489793d && FastMath.abs(this.originOffset + plane.originOffset) < this.tolerance);
    }

    public Plane rotate(Vector3D center, Rotation rotation) {
        Vector3D delta = this.origin.subtract((Vector<Euclidean3D>) center);
        Plane plane = new Plane(center.add((Vector<Euclidean3D>) rotation.applyTo(delta)), rotation.applyTo(this.f13009w), this.tolerance);
        plane.f13007u = rotation.applyTo(this.f13007u);
        plane.f13008v = rotation.applyTo(this.f13008v);
        return plane;
    }

    public Plane translate(Vector3D translation) {
        Plane plane = new Plane(this.origin.add((Vector<Euclidean3D>) translation), this.f13009w, this.tolerance);
        plane.f13007u = this.f13007u;
        plane.f13008v = this.f13008v;
        return plane;
    }

    public Vector3D intersection(Line line) {
        Vector3D direction = line.getDirection();
        double dot = this.f13009w.dotProduct(direction);
        if (FastMath.abs(dot) < 1.0E-10d) {
            return null;
        }
        Vector3D point = line.toSpace((Point<Euclidean1D>) Vector1D.ZERO);
        double k2 = (-(this.originOffset + this.f13009w.dotProduct(point))) / dot;
        return new Vector3D(1.0d, point, k2, direction);
    }

    public Line intersection(Plane other) {
        Vector3D direction = Vector3D.crossProduct(this.f13009w, other.f13009w);
        if (direction.getNorm() < this.tolerance) {
            return null;
        }
        Vector3D point = intersection(this, other, new Plane(direction, this.tolerance));
        return new Line(point, point.add((Vector<Euclidean3D>) direction), this.tolerance);
    }

    public static Vector3D intersection(Plane plane1, Plane plane2, Plane plane3) {
        double a1 = plane1.f13009w.getX();
        double b1 = plane1.f13009w.getY();
        double c1 = plane1.f13009w.getZ();
        double d1 = plane1.originOffset;
        double a2 = plane2.f13009w.getX();
        double b2 = plane2.f13009w.getY();
        double c2 = plane2.f13009w.getZ();
        double d2 = plane2.originOffset;
        double a3 = plane3.f13009w.getX();
        double b3 = plane3.f13009w.getY();
        double c3 = plane3.f13009w.getZ();
        double d3 = plane3.originOffset;
        double a23 = (b2 * c3) - (b3 * c2);
        double b23 = (c2 * a3) - (c3 * a2);
        double c23 = (a2 * b3) - (a3 * b2);
        double determinant = (a1 * a23) + (b1 * b23) + (c1 * c23);
        if (FastMath.abs(determinant) < 1.0E-10d) {
            return null;
        }
        double r2 = 1.0d / determinant;
        return new Vector3D(((((-a23) * d1) - (((c1 * b3) - (c3 * b1)) * d2)) - (((c2 * b1) - (c1 * b2)) * d3)) * r2, ((((-b23) * d1) - (((c3 * a1) - (c1 * a3)) * d2)) - (((c1 * a2) - (c2 * a1)) * d3)) * r2, ((((-c23) * d1) - (((b1 * a3) - (b3 * a1)) * d2)) - (((b2 * a1) - (b1 * a2)) * d3)) * r2);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public SubPlane wholeHyperplane() {
        return new SubPlane(this, new PolygonsSet(this.tolerance));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public PolyhedronsSet wholeSpace() {
        return new PolyhedronsSet(this.tolerance);
    }

    public boolean contains(Vector3D p2) {
        return FastMath.abs(getOffset((Vector<Euclidean3D>) p2)) < this.tolerance;
    }

    public double getOffset(Plane plane) {
        return this.originOffset + (sameOrientationAs(plane) ? -plane.originOffset : plane.originOffset);
    }

    public double getOffset(Vector<Euclidean3D> vector) {
        return getOffset((Point<Euclidean3D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public double getOffset(Point<Euclidean3D> point) {
        return ((Vector3D) point).dotProduct(this.f13009w) + this.originOffset;
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Hyperplane
    public boolean sameOrientationAs(Hyperplane<Euclidean3D> other) {
        return ((Plane) other).f13009w.dotProduct(this.f13009w) > 0.0d;
    }
}
