package org.apache.commons.math3.geometry.euclidean.twod;

import com.sun.glass.events.WindowEvent;
import java.text.NumberFormat;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/twod/Vector2D.class */
public class Vector2D implements Vector<Euclidean2D> {
    public static final Vector2D ZERO = new Vector2D(0.0d, 0.0d);
    public static final Vector2D NaN = new Vector2D(Double.NaN, Double.NaN);
    public static final Vector2D POSITIVE_INFINITY = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector2D NEGATIVE_INFINITY = new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    private static final long serialVersionUID = 266938651998679754L;

    /* renamed from: x, reason: collision with root package name */
    private final double f13018x;

    /* renamed from: y, reason: collision with root package name */
    private final double f13019y;

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(double d2, Vector vector) {
        return subtract(d2, (Vector<Euclidean2D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(Vector vector) {
        return subtract((Vector<Euclidean2D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(double d2, Vector vector) {
        return add(d2, (Vector<Euclidean2D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(Vector vector) {
        return add((Vector<Euclidean2D>) vector);
    }

    public Vector2D(double x2, double y2) {
        this.f13018x = x2;
        this.f13019y = y2;
    }

    public Vector2D(double[] v2) throws DimensionMismatchException {
        if (v2.length != 2) {
            throw new DimensionMismatchException(v2.length, 2);
        }
        this.f13018x = v2[0];
        this.f13019y = v2[1];
    }

    public Vector2D(double a2, Vector2D u2) {
        this.f13018x = a2 * u2.f13018x;
        this.f13019y = a2 * u2.f13019y;
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2) {
        this.f13018x = (a1 * u1.f13018x) + (a2 * u2.f13018x);
        this.f13019y = (a1 * u1.f13019y) + (a2 * u2.f13019y);
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3) {
        this.f13018x = (a1 * u1.f13018x) + (a2 * u2.f13018x) + (a3 * u3.f13018x);
        this.f13019y = (a1 * u1.f13019y) + (a2 * u2.f13019y) + (a3 * u3.f13019y);
    }

    public Vector2D(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3, double a4, Vector2D u4) {
        this.f13018x = (a1 * u1.f13018x) + (a2 * u2.f13018x) + (a3 * u3.f13018x) + (a4 * u4.f13018x);
        this.f13019y = (a1 * u1.f13019y) + (a2 * u2.f13019y) + (a3 * u3.f13019y) + (a4 * u4.f13019y);
    }

    public double getX() {
        return this.f13018x;
    }

    public double getY() {
        return this.f13019y;
    }

    public double[] toArray() {
        return new double[]{this.f13018x, this.f13019y};
    }

    @Override // org.apache.commons.math3.geometry.Point
    public Space getSpace() {
        return Euclidean2D.getInstance();
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D getZero() {
        return ZERO;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm1() {
        return FastMath.abs(this.f13018x) + FastMath.abs(this.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm() {
        return FastMath.sqrt((this.f13018x * this.f13018x) + (this.f13019y * this.f13019y));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormSq() {
        return (this.f13018x * this.f13018x) + (this.f13019y * this.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormInf() {
        return FastMath.max(FastMath.abs(this.f13018x), FastMath.abs(this.f13019y));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D add(Vector<Euclidean2D> v2) {
        Vector2D v22 = (Vector2D) v2;
        return new Vector2D(this.f13018x + v22.getX(), this.f13019y + v22.getY());
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D add(double factor, Vector<Euclidean2D> v2) {
        Vector2D v22 = (Vector2D) v2;
        return new Vector2D(this.f13018x + (factor * v22.getX()), this.f13019y + (factor * v22.getY()));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D subtract(Vector<Euclidean2D> p2) {
        Vector2D p3 = (Vector2D) p2;
        return new Vector2D(this.f13018x - p3.f13018x, this.f13019y - p3.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D subtract(double factor, Vector<Euclidean2D> v2) {
        Vector2D v22 = (Vector2D) v2;
        return new Vector2D(this.f13018x - (factor * v22.getX()), this.f13019y - (factor * v22.getY()));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D normalize() throws MathArithmeticException {
        double s2 = getNorm();
        if (s2 == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
        }
        return scalarMultiply(1.0d / s2);
    }

    public static double angle(Vector2D v1, Vector2D v2) throws MathArithmeticException {
        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        double dot = v1.dotProduct(v2);
        double threshold = normProduct * 0.9999d;
        if (dot < (-threshold) || dot > threshold) {
            double n2 = FastMath.abs(MathArrays.linearCombination(v1.f13018x, v2.f13019y, -v1.f13019y, v2.f13018x));
            if (dot >= 0.0d) {
                return FastMath.asin(n2 / normProduct);
            }
            return 3.141592653589793d - FastMath.asin(n2 / normProduct);
        }
        return FastMath.acos(dot / normProduct);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D negate() {
        return new Vector2D(-this.f13018x, -this.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector2D scalarMultiply(double a2) {
        return new Vector2D(a2 * this.f13018x, a2 * this.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public boolean isNaN() {
        return Double.isNaN(this.f13018x) || Double.isNaN(this.f13019y);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f13018x) || Double.isInfinite(this.f13019y));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distance1(Vector<Euclidean2D> p2) {
        Vector2D p3 = (Vector2D) p2;
        double dx = FastMath.abs(p3.f13018x - this.f13018x);
        double dy = FastMath.abs(p3.f13019y - this.f13019y);
        return dx + dy;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distance(Vector<Euclidean2D> p2) {
        return distance((Point<Euclidean2D>) p2);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public double distance(Point<Euclidean2D> p2) {
        Vector2D p3 = (Vector2D) p2;
        double dx = p3.f13018x - this.f13018x;
        double dy = p3.f13019y - this.f13019y;
        return FastMath.sqrt((dx * dx) + (dy * dy));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceInf(Vector<Euclidean2D> p2) {
        Vector2D p3 = (Vector2D) p2;
        double dx = FastMath.abs(p3.f13018x - this.f13018x);
        double dy = FastMath.abs(p3.f13019y - this.f13019y);
        return FastMath.max(dx, dy);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceSq(Vector<Euclidean2D> p2) {
        Vector2D p3 = (Vector2D) p2;
        double dx = p3.f13018x - this.f13018x;
        double dy = p3.f13019y - this.f13019y;
        return (dx * dx) + (dy * dy);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double dotProduct(Vector<Euclidean2D> v2) {
        Vector2D v22 = (Vector2D) v2;
        return MathArrays.linearCombination(this.f13018x, v22.f13018x, this.f13019y, v22.f13019y);
    }

    public double crossProduct(Vector2D p1, Vector2D p2) {
        double x1 = p2.getX() - p1.getX();
        double y1 = getY() - p1.getY();
        double x2 = getX() - p1.getX();
        double y2 = p2.getY() - p1.getY();
        return MathArrays.linearCombination(x1, y1, -x2, y2);
    }

    public static double distance(Vector2D p1, Vector2D p2) {
        return p1.distance((Vector<Euclidean2D>) p2);
    }

    public static double distanceInf(Vector2D p1, Vector2D p2) {
        return p1.distanceInf(p2);
    }

    public static double distanceSq(Vector2D p1, Vector2D p2) {
        return p1.distanceSq(p2);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Vector2D) {
            Vector2D rhs = (Vector2D) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.f13018x == rhs.f13018x && this.f13019y == rhs.f13019y;
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return WindowEvent.FOCUS_GAINED;
        }
        return 122 * ((76 * MathUtils.hash(this.f13018x)) + MathUtils.hash(this.f13019y));
    }

    public String toString() {
        return Vector2DFormat.getInstance().format(this);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public String toString(NumberFormat format) {
        return new Vector2DFormat(format).format(this);
    }
}
