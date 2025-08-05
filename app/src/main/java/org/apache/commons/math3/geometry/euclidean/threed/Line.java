package org.apache.commons.math3.geometry.euclidean.threed;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Embedding;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/Line.class */
public class Line implements Embedding<Euclidean3D, Euclidean1D> {
    private static final double DEFAULT_TOLERANCE = 1.0E-10d;
    private Vector3D direction;
    private Vector3D zero;
    private final double tolerance;

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSpace(Point point) {
        return toSpace((Point<Euclidean1D>) point);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public /* bridge */ /* synthetic */ Point toSubSpace(Point point) {
        return toSubSpace((Point<Euclidean3D>) point);
    }

    public Line(Vector3D p1, Vector3D p2, double tolerance) throws MathIllegalArgumentException {
        reset(p1, p2);
        this.tolerance = tolerance;
    }

    public Line(Line line) {
        this.direction = line.direction;
        this.zero = line.zero;
        this.tolerance = line.tolerance;
    }

    @Deprecated
    public Line(Vector3D p1, Vector3D p2) throws MathIllegalArgumentException {
        this(p1, p2, 1.0E-10d);
    }

    public void reset(Vector3D p1, Vector3D p2) throws MathIllegalArgumentException {
        Vector3D delta = p2.subtract((Vector<Euclidean3D>) p1);
        double norm2 = delta.getNormSq();
        if (norm2 == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        this.direction = new Vector3D(1.0d / FastMath.sqrt(norm2), delta);
        this.zero = new Vector3D(1.0d, p1, (-p1.dotProduct(delta)) / norm2, delta);
    }

    public double getTolerance() {
        return this.tolerance;
    }

    public Line revert() {
        Line reverted = new Line(this);
        reverted.direction = reverted.direction.negate();
        return reverted;
    }

    public Vector3D getDirection() {
        return this.direction;
    }

    public Vector3D getOrigin() {
        return this.zero;
    }

    public double getAbscissa(Vector3D point) {
        return point.subtract((Vector<Euclidean3D>) this.zero).dotProduct(this.direction);
    }

    public Vector3D pointAt(double abscissa) {
        return new Vector3D(1.0d, this.zero, abscissa, this.direction);
    }

    public Vector1D toSubSpace(Vector<Euclidean3D> vector) {
        return toSubSpace((Point<Euclidean3D>) vector);
    }

    public Vector3D toSpace(Vector<Euclidean1D> vector) {
        return toSpace((Point<Euclidean1D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public Vector1D toSubSpace(Point<Euclidean3D> point) {
        return new Vector1D(getAbscissa((Vector3D) point));
    }

    @Override // org.apache.commons.math3.geometry.partitioning.Embedding
    public Vector3D toSpace(Point<Euclidean1D> point) {
        return pointAt(((Vector1D) point).getX());
    }

    public boolean isSimilarTo(Line line) throws MathArithmeticException {
        double angle = Vector3D.angle(this.direction, line.direction);
        return (angle < this.tolerance || angle > 3.141592653589793d - this.tolerance) && contains(line.zero);
    }

    public boolean contains(Vector3D p2) {
        return distance(p2) < this.tolerance;
    }

    public double distance(Vector3D p2) {
        Vector3D d2 = p2.subtract((Vector<Euclidean3D>) this.zero);
        Vector3D n2 = new Vector3D(1.0d, d2, -d2.dotProduct(this.direction), this.direction);
        return n2.getNorm();
    }

    public double distance(Line line) {
        Vector3D normal = Vector3D.crossProduct(this.direction, line.direction);
        double n2 = normal.getNorm();
        if (n2 < Precision.SAFE_MIN) {
            return distance(line.zero);
        }
        double offset = line.zero.subtract((Vector<Euclidean3D>) this.zero).dotProduct(normal) / n2;
        return FastMath.abs(offset);
    }

    public Vector3D closestPoint(Line line) {
        double cos = this.direction.dotProduct(line.direction);
        double n2 = 1.0d - (cos * cos);
        if (n2 < Precision.EPSILON) {
            return this.zero;
        }
        Vector3D delta0 = line.zero.subtract((Vector<Euclidean3D>) this.zero);
        double a2 = delta0.dotProduct(this.direction);
        double b2 = delta0.dotProduct(line.direction);
        return new Vector3D(1.0d, this.zero, (a2 - (b2 * cos)) / n2, this.direction);
    }

    public Vector3D intersection(Line line) {
        Vector3D closest = closestPoint(line);
        if (line.contains(closest)) {
            return closest;
        }
        return null;
    }

    public SubLine wholeLine() {
        return new SubLine(this, new IntervalsSet(this.tolerance));
    }
}
