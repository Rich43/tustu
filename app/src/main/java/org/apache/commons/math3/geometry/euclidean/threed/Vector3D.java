package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
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

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/Vector3D.class */
public class Vector3D implements Serializable, Vector<Euclidean3D> {
    public static final Vector3D ZERO = new Vector3D(0.0d, 0.0d, 0.0d);
    public static final Vector3D PLUS_I = new Vector3D(1.0d, 0.0d, 0.0d);
    public static final Vector3D MINUS_I = new Vector3D(-1.0d, 0.0d, 0.0d);
    public static final Vector3D PLUS_J = new Vector3D(0.0d, 1.0d, 0.0d);
    public static final Vector3D MINUS_J = new Vector3D(0.0d, -1.0d, 0.0d);
    public static final Vector3D PLUS_K = new Vector3D(0.0d, 0.0d, 1.0d);
    public static final Vector3D MINUS_K = new Vector3D(0.0d, 0.0d, -1.0d);
    public static final Vector3D NaN = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
    public static final Vector3D POSITIVE_INFINITY = new Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    public static final Vector3D NEGATIVE_INFINITY = new Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    private static final long serialVersionUID = 1313493323784566947L;

    /* renamed from: x, reason: collision with root package name */
    private final double f13015x;

    /* renamed from: y, reason: collision with root package name */
    private final double f13016y;

    /* renamed from: z, reason: collision with root package name */
    private final double f13017z;

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(double d2, Vector vector) {
        return subtract(d2, (Vector<Euclidean3D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector subtract(Vector vector) {
        return subtract((Vector<Euclidean3D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(double d2, Vector vector) {
        return add(d2, (Vector<Euclidean3D>) vector);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public /* bridge */ /* synthetic */ Vector add(Vector vector) {
        return add((Vector<Euclidean3D>) vector);
    }

    public Vector3D(double x2, double y2, double z2) {
        this.f13015x = x2;
        this.f13016y = y2;
        this.f13017z = z2;
    }

    public Vector3D(double[] v2) throws DimensionMismatchException {
        if (v2.length != 3) {
            throw new DimensionMismatchException(v2.length, 3);
        }
        this.f13015x = v2[0];
        this.f13016y = v2[1];
        this.f13017z = v2[2];
    }

    public Vector3D(double alpha, double delta) {
        double cosDelta = FastMath.cos(delta);
        this.f13015x = FastMath.cos(alpha) * cosDelta;
        this.f13016y = FastMath.sin(alpha) * cosDelta;
        this.f13017z = FastMath.sin(delta);
    }

    public Vector3D(double a2, Vector3D u2) {
        this.f13015x = a2 * u2.f13015x;
        this.f13016y = a2 * u2.f13016y;
        this.f13017z = a2 * u2.f13017z;
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2) {
        this.f13015x = MathArrays.linearCombination(a1, u1.f13015x, a2, u2.f13015x);
        this.f13016y = MathArrays.linearCombination(a1, u1.f13016y, a2, u2.f13016y);
        this.f13017z = MathArrays.linearCombination(a1, u1.f13017z, a2, u2.f13017z);
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3) {
        this.f13015x = MathArrays.linearCombination(a1, u1.f13015x, a2, u2.f13015x, a3, u3.f13015x);
        this.f13016y = MathArrays.linearCombination(a1, u1.f13016y, a2, u2.f13016y, a3, u3.f13016y);
        this.f13017z = MathArrays.linearCombination(a1, u1.f13017z, a2, u2.f13017z, a3, u3.f13017z);
    }

    public Vector3D(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3, double a4, Vector3D u4) {
        this.f13015x = MathArrays.linearCombination(a1, u1.f13015x, a2, u2.f13015x, a3, u3.f13015x, a4, u4.f13015x);
        this.f13016y = MathArrays.linearCombination(a1, u1.f13016y, a2, u2.f13016y, a3, u3.f13016y, a4, u4.f13016y);
        this.f13017z = MathArrays.linearCombination(a1, u1.f13017z, a2, u2.f13017z, a3, u3.f13017z, a4, u4.f13017z);
    }

    public double getX() {
        return this.f13015x;
    }

    public double getY() {
        return this.f13016y;
    }

    public double getZ() {
        return this.f13017z;
    }

    public double[] toArray() {
        return new double[]{this.f13015x, this.f13016y, this.f13017z};
    }

    @Override // org.apache.commons.math3.geometry.Point
    public Space getSpace() {
        return Euclidean3D.getInstance();
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D getZero() {
        return ZERO;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm1() {
        return FastMath.abs(this.f13015x) + FastMath.abs(this.f13016y) + FastMath.abs(this.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNorm() {
        return FastMath.sqrt((this.f13015x * this.f13015x) + (this.f13016y * this.f13016y) + (this.f13017z * this.f13017z));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormSq() {
        return (this.f13015x * this.f13015x) + (this.f13016y * this.f13016y) + (this.f13017z * this.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double getNormInf() {
        return FastMath.max(FastMath.max(FastMath.abs(this.f13015x), FastMath.abs(this.f13016y)), FastMath.abs(this.f13017z));
    }

    public double getAlpha() {
        return FastMath.atan2(this.f13016y, this.f13015x);
    }

    public double getDelta() {
        return FastMath.asin(this.f13017z / getNorm());
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D add(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        return new Vector3D(this.f13015x + v3.f13015x, this.f13016y + v3.f13016y, this.f13017z + v3.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D add(double factor, Vector<Euclidean3D> v2) {
        return new Vector3D(1.0d, this, factor, (Vector3D) v2);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D subtract(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        return new Vector3D(this.f13015x - v3.f13015x, this.f13016y - v3.f13016y, this.f13017z - v3.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D subtract(double factor, Vector<Euclidean3D> v2) {
        return new Vector3D(1.0d, this, -factor, (Vector3D) v2);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D normalize() throws MathArithmeticException {
        double s2 = getNorm();
        if (s2 == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
        }
        return scalarMultiply(1.0d / s2);
    }

    public Vector3D orthogonal() throws MathArithmeticException {
        double threshold = 0.6d * getNorm();
        if (threshold == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        if (FastMath.abs(this.f13015x) <= threshold) {
            double inverse = 1.0d / FastMath.sqrt((this.f13016y * this.f13016y) + (this.f13017z * this.f13017z));
            return new Vector3D(0.0d, inverse * this.f13017z, (-inverse) * this.f13016y);
        }
        if (FastMath.abs(this.f13016y) <= threshold) {
            double inverse2 = 1.0d / FastMath.sqrt((this.f13015x * this.f13015x) + (this.f13017z * this.f13017z));
            return new Vector3D((-inverse2) * this.f13017z, 0.0d, inverse2 * this.f13015x);
        }
        double inverse3 = 1.0d / FastMath.sqrt((this.f13015x * this.f13015x) + (this.f13016y * this.f13016y));
        return new Vector3D(inverse3 * this.f13016y, (-inverse3) * this.f13015x, 0.0d);
    }

    public static double angle(Vector3D v1, Vector3D v2) throws MathArithmeticException {
        double normProduct = v1.getNorm() * v2.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        double dot = v1.dotProduct(v2);
        double threshold = normProduct * 0.9999d;
        if (dot < (-threshold) || dot > threshold) {
            Vector3D v3 = crossProduct(v1, v2);
            if (dot >= 0.0d) {
                return FastMath.asin(v3.getNorm() / normProduct);
            }
            return 3.141592653589793d - FastMath.asin(v3.getNorm() / normProduct);
        }
        return FastMath.acos(dot / normProduct);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D negate() {
        return new Vector3D(-this.f13015x, -this.f13016y, -this.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public Vector3D scalarMultiply(double a2) {
        return new Vector3D(a2 * this.f13015x, a2 * this.f13016y, a2 * this.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public boolean isNaN() {
        return Double.isNaN(this.f13015x) || Double.isNaN(this.f13016y) || Double.isNaN(this.f13017z);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f13015x) || Double.isInfinite(this.f13016y) || Double.isInfinite(this.f13017z));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Vector3D) {
            Vector3D rhs = (Vector3D) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.f13015x == rhs.f13015x && this.f13016y == rhs.f13016y && this.f13017z == rhs.f13017z;
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return 642;
        }
        return 643 * ((164 * MathUtils.hash(this.f13015x)) + (3 * MathUtils.hash(this.f13016y)) + MathUtils.hash(this.f13017z));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double dotProduct(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        return MathArrays.linearCombination(this.f13015x, v3.f13015x, this.f13016y, v3.f13016y, this.f13017z, v3.f13017z);
    }

    public Vector3D crossProduct(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        return new Vector3D(MathArrays.linearCombination(this.f13016y, v3.f13017z, -this.f13017z, v3.f13016y), MathArrays.linearCombination(this.f13017z, v3.f13015x, -this.f13015x, v3.f13017z), MathArrays.linearCombination(this.f13015x, v3.f13016y, -this.f13016y, v3.f13015x));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distance1(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        double dx = FastMath.abs(v3.f13015x - this.f13015x);
        double dy = FastMath.abs(v3.f13016y - this.f13016y);
        double dz = FastMath.abs(v3.f13017z - this.f13017z);
        return dx + dy + dz;
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distance(Vector<Euclidean3D> v2) {
        return distance((Point<Euclidean3D>) v2);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public double distance(Point<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        double dx = v3.f13015x - this.f13015x;
        double dy = v3.f13016y - this.f13016y;
        double dz = v3.f13017z - this.f13017z;
        return FastMath.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceInf(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        double dx = FastMath.abs(v3.f13015x - this.f13015x);
        double dy = FastMath.abs(v3.f13016y - this.f13016y);
        double dz = FastMath.abs(v3.f13017z - this.f13017z);
        return FastMath.max(FastMath.max(dx, dy), dz);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public double distanceSq(Vector<Euclidean3D> v2) {
        Vector3D v3 = (Vector3D) v2;
        double dx = v3.f13015x - this.f13015x;
        double dy = v3.f13016y - this.f13016y;
        double dz = v3.f13017z - this.f13017z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.dotProduct(v2);
    }

    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return v1.crossProduct(v2);
    }

    public static double distance1(Vector3D v1, Vector3D v2) {
        return v1.distance1(v2);
    }

    public static double distance(Vector3D v1, Vector3D v2) {
        return v1.distance((Vector<Euclidean3D>) v2);
    }

    public static double distanceInf(Vector3D v1, Vector3D v2) {
        return v1.distanceInf(v2);
    }

    public static double distanceSq(Vector3D v1, Vector3D v2) {
        return v1.distanceSq(v2);
    }

    public String toString() {
        return Vector3DFormat.getInstance().format(this);
    }

    @Override // org.apache.commons.math3.geometry.Vector
    public String toString(NumberFormat format) {
        return new Vector3DFormat(format).format(this);
    }
}
