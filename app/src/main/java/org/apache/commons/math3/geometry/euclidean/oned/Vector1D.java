package org.apache.commons.math3.geometry.euclidean.oned;

import java.text.NumberFormat;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/oned/Vector1D.class */
public class Vector1D implements Vector<Euclidean1D> {
    public static final Vector1D ZERO = new Vector1D(0.0d);
    public static final Vector1D ONE = new Vector1D(1.0d);
    public static final Vector1D NaN = new Vector1D(Double.NaN);
    public static final Vector1D POSITIVE_INFINITY = new Vector1D(Double.POSITIVE_INFINITY);
    public static final Vector1D NEGATIVE_INFINITY = new Vector1D(Double.NEGATIVE_INFINITY);
    private static final long serialVersionUID = 7556674948671647925L;

    /* renamed from: x, reason: collision with root package name */
    private final double f13000x;

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(double d2, Vector vector) {
        return subtract(d2, (Vector<Euclidean1D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(Vector vector) {
        return subtract((Vector<Euclidean1D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(double d2, Vector vector) {
        return add(d2, (Vector<Euclidean1D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(Vector vector) {
        return add((Vector<Euclidean1D>) vector);
    }

    public Vector1D(double x2) {
        this.f13000x = x2;
    }

    public Vector1D(double a2, Vector1D u2) {
        this.f13000x = a2 * u2.f13000x;
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2) {
        this.f13000x = (a1 * u1.f13000x) + (a2 * u2.f13000x);
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3) {
        this.f13000x = (a1 * u1.f13000x) + (a2 * u2.f13000x) + (a3 * u3.f13000x);
    }

    public Vector1D(double a1, Vector1D u1, double a2, Vector1D u2, double a3, Vector1D u3, double a4, Vector1D u4) {
        this.f13000x = (a1 * u1.f13000x) + (a2 * u2.f13000x) + (a3 * u3.f13000x) + (a4 * u4.f13000x);
    }

    public double getX() {
        return this.f13000x;
    }

    @Override // org.apache.commons.math3.geometry.Point
    public Space getSpace() {
        return Euclidean1D.getInstance();
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D getZero() {
        return ZERO;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm1() {
        return FastMath.abs(this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm() {
        return FastMath.abs(this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormSq() {
        return this.f13000x * this.f13000x;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormInf() {
        return FastMath.abs(this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D add(Vector<Euclidean1D> v2) {
        Vector1D v1 = (Vector1D) v2;
        return new Vector1D(this.f13000x + v1.getX());
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D add(double factor, Vector<Euclidean1D> v2) {
        Vector1D v1 = (Vector1D) v2;
        return new Vector1D(this.f13000x + (factor * v1.getX()));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D subtract(Vector<Euclidean1D> p2) {
        Vector1D p3 = (Vector1D) p2;
        return new Vector1D(this.f13000x - p3.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D subtract(double factor, Vector<Euclidean1D> v2) {
        Vector1D v1 = (Vector1D) v2;
        return new Vector1D(this.f13000x - (factor * v1.getX()));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D normalize() throws MathArithmeticException {
        double s2 = getNorm();
        if (s2 == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
        }
        return scalarMultiply(1.0d / s2);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D negate() {
        return new Vector1D(-this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector1D scalarMultiply(double a2) {
        return new Vector1D(a2 * this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public boolean isNaN() {
        return Double.isNaN(this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public boolean isInfinite() {
        return !isNaN() && Double.isInfinite(this.f13000x);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distance1(Vector<Euclidean1D> p2) {
        Vector1D p3 = (Vector1D) p2;
        double dx = FastMath.abs(p3.f13000x - this.f13000x);
        return dx;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    @Deprecated
    public double distance(Vector<Euclidean1D> p2) {
        return distance((Point<Euclidean1D>) p2);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public double distance(Point<Euclidean1D> p2) {
        Vector1D p3 = (Vector1D) p2;
        double dx = p3.f13000x - this.f13000x;
        return FastMath.abs(dx);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceInf(Vector<Euclidean1D> p2) {
        Vector1D p3 = (Vector1D) p2;
        double dx = FastMath.abs(p3.f13000x - this.f13000x);
        return dx;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceSq(Vector<Euclidean1D> p2) {
        Vector1D p3 = (Vector1D) p2;
        double dx = p3.f13000x - this.f13000x;
        return dx * dx;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double dotProduct(Vector<Euclidean1D> v2) {
        Vector1D v1 = (Vector1D) v2;
        return this.f13000x * v1.f13000x;
    }

    public static double distance(Vector1D p1, Vector1D p2) {
        return p1.distance((Vector<Euclidean1D>) p2);
    }

    public static double distanceInf(Vector1D p1, Vector1D p2) {
        return p1.distanceInf(p2);
    }

    public static double distanceSq(Vector1D p1, Vector1D p2) {
        return p1.distanceSq(p2);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Vector1D) {
            Vector1D rhs = (Vector1D) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.f13000x == rhs.f13000x;
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return 7785;
        }
        return 997 * MathUtils.hash(this.f13000x);
    }

    public String toString() {
        return Vector1DFormat.getInstance().format(this);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public String toString(NumberFormat format) {
        return new Vector1DFormat(format).format(this);
    }
}
