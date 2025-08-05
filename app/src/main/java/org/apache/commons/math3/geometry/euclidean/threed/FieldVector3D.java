package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import java.text.NumberFormat;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/threed/FieldVector3D.class */
public class FieldVector3D<T extends RealFieldElement<T>> implements Serializable {
    private static final long serialVersionUID = 20130224;

    /* renamed from: x, reason: collision with root package name */
    private final T f13001x;

    /* renamed from: y, reason: collision with root package name */
    private final T f13002y;

    /* renamed from: z, reason: collision with root package name */
    private final T f13003z;

    public FieldVector3D(T x2, T y2, T z2) {
        this.f13001x = x2;
        this.f13002y = y2;
        this.f13003z = z2;
    }

    public FieldVector3D(T[] v2) throws DimensionMismatchException {
        if (v2.length != 3) {
            throw new DimensionMismatchException(v2.length, 3);
        }
        this.f13001x = v2[0];
        this.f13002y = v2[1];
        this.f13003z = v2[2];
    }

    public FieldVector3D(T alpha, T delta) {
        RealFieldElement realFieldElement = (RealFieldElement) delta.cos();
        this.f13001x = (T) ((RealFieldElement) alpha.cos()).multiply(realFieldElement);
        this.f13002y = (T) ((RealFieldElement) alpha.sin()).multiply(realFieldElement);
        this.f13003z = (T) delta.sin();
    }

    public FieldVector3D(T a2, FieldVector3D<T> u2) {
        this.f13001x = (T) a2.multiply(u2.f13001x);
        this.f13002y = (T) a2.multiply(u2.f13002y);
        this.f13003z = (T) a2.multiply(u2.f13003z);
    }

    public FieldVector3D(T a2, Vector3D u2) {
        this.f13001x = (T) a2.multiply(u2.getX());
        this.f13002y = (T) a2.multiply(u2.getY());
        this.f13003z = (T) a2.multiply(u2.getZ());
    }

    public FieldVector3D(double a2, FieldVector3D<T> u2) {
        this.f13001x = (T) u2.f13001x.multiply(a2);
        this.f13002y = (T) u2.f13002y.multiply(a2);
        this.f13003z = (T) u2.f13003z.multiply(a2);
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2) {
        this.f13001x = (T) a1.linearCombination(a1, u1.getX(), a2, u2.getX());
        this.f13002y = (T) a1.linearCombination(a1, u1.getY(), a2, u2.getY());
        this.f13003z = (T) a1.linearCombination(a1, u1.getZ(), a2, u2.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2) {
        this.f13001x = (T) a1.linearCombination(u1.getX(), a1, u2.getX(), a2);
        this.f13002y = (T) a1.linearCombination(u1.getY(), a1, u2.getY(), a2);
        this.f13003z = (T) a1.linearCombination(u1.getZ(), a1, u2.getZ(), a2);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2) {
        RealFieldElement x2 = u1.getX();
        this.f13001x = (T) x2.linearCombination(a1, (double) u1.getX(), a2, (double) u2.getX());
        this.f13002y = (T) x2.linearCombination(a1, (double) u1.getY(), a2, (double) u2.getY());
        this.f13003z = (T) x2.linearCombination(a1, (double) u1.getZ(), a2, (double) u2.getZ());
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2, T a3, FieldVector3D<T> u3) {
        this.f13001x = (T) a1.linearCombination(a1, u1.getX(), a2, u2.getX(), a3, u3.getX());
        this.f13002y = (T) a1.linearCombination(a1, u1.getY(), a2, u2.getY(), a3, u3.getY());
        this.f13003z = (T) a1.linearCombination(a1, u1.getZ(), a2, u2.getZ(), a3, u3.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2, T a3, Vector3D u3) {
        this.f13001x = (T) a1.linearCombination(u1.getX(), a1, u2.getX(), a2, u3.getX(), a3);
        this.f13002y = (T) a1.linearCombination(u1.getY(), a1, u2.getY(), a2, u3.getY(), a3);
        this.f13003z = (T) a1.linearCombination(u1.getZ(), a1, u2.getZ(), a2, u3.getZ(), a3);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2, double a3, FieldVector3D<T> u3) {
        RealFieldElement x2 = u1.getX();
        this.f13001x = (T) x2.linearCombination(a1, (double) u1.getX(), a2, (double) u2.getX(), a3, (double) u3.getX());
        this.f13002y = (T) x2.linearCombination(a1, (double) u1.getY(), a2, (double) u2.getY(), a3, (double) u3.getY());
        this.f13003z = (T) x2.linearCombination(a1, (double) u1.getZ(), a2, (double) u2.getZ(), a3, (double) u3.getZ());
    }

    public FieldVector3D(T a1, FieldVector3D<T> u1, T a2, FieldVector3D<T> u2, T a3, FieldVector3D<T> u3, T a4, FieldVector3D<T> u4) {
        this.f13001x = (T) a1.linearCombination(a1, u1.getX(), a2, u2.getX(), a3, u3.getX(), a4, u4.getX());
        this.f13002y = (T) a1.linearCombination(a1, u1.getY(), a2, u2.getY(), a3, u3.getY(), a4, u4.getY());
        this.f13003z = (T) a1.linearCombination(a1, u1.getZ(), a2, u2.getZ(), a3, u3.getZ(), a4, u4.getZ());
    }

    public FieldVector3D(T a1, Vector3D u1, T a2, Vector3D u2, T a3, Vector3D u3, T a4, Vector3D u4) {
        this.f13001x = (T) a1.linearCombination(u1.getX(), a1, u2.getX(), a2, u3.getX(), a3, u4.getX(), a4);
        this.f13002y = (T) a1.linearCombination(u1.getY(), a1, u2.getY(), a2, u3.getY(), a3, u4.getY(), a4);
        this.f13003z = (T) a1.linearCombination(u1.getZ(), a1, u2.getZ(), a2, u3.getZ(), a3, u4.getZ(), a4);
    }

    public FieldVector3D(double a1, FieldVector3D<T> u1, double a2, FieldVector3D<T> u2, double a3, FieldVector3D<T> u3, double a4, FieldVector3D<T> u4) {
        RealFieldElement x2 = u1.getX();
        this.f13001x = (T) x2.linearCombination(a1, (double) u1.getX(), a2, (double) u2.getX(), a3, (double) u3.getX(), a4, (double) u4.getX());
        this.f13002y = (T) x2.linearCombination(a1, (double) u1.getY(), a2, (double) u2.getY(), a3, (double) u3.getY(), a4, (double) u4.getY());
        this.f13003z = (T) x2.linearCombination(a1, (double) u1.getZ(), a2, (double) u2.getZ(), a3, (double) u3.getZ(), a4, (double) u4.getZ());
    }

    public T getX() {
        return this.f13001x;
    }

    public T getY() {
        return this.f13002y;
    }

    public T getZ() {
        return this.f13003z;
    }

    public T[] toArray() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(this.f13001x.getField2(), 3));
        tArr[0] = this.f13001x;
        tArr[1] = this.f13002y;
        tArr[2] = this.f13003z;
        return tArr;
    }

    public Vector3D toVector3D() {
        return new Vector3D(this.f13001x.getReal(), this.f13002y.getReal(), this.f13003z.getReal());
    }

    public T getNorm1() {
        return (T) ((RealFieldElement) ((RealFieldElement) this.f13001x.abs()).add((RealFieldElement) this.f13002y.abs())).add((RealFieldElement) this.f13003z.abs());
    }

    public T getNorm() {
        return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f13001x.multiply(this.f13001x)).add((RealFieldElement) this.f13002y.multiply(this.f13002y))).add((RealFieldElement) this.f13003z.multiply(this.f13003z))).sqrt();
    }

    public T getNormSq() {
        return (T) ((RealFieldElement) ((RealFieldElement) this.f13001x.multiply(this.f13001x)).add((RealFieldElement) this.f13002y.multiply(this.f13002y))).add((RealFieldElement) this.f13003z.multiply(this.f13003z));
    }

    public T getNormInf() {
        T t2 = (T) this.f13001x.abs();
        T t3 = (T) this.f13002y.abs();
        T t4 = (T) this.f13003z.abs();
        if (t2.getReal() <= t3.getReal()) {
            if (t3.getReal() <= t4.getReal()) {
                return t4;
            }
            return t3;
        }
        if (t2.getReal() <= t4.getReal()) {
            return t4;
        }
        return t2;
    }

    public T getAlpha() {
        return (T) this.f13002y.atan2(this.f13001x);
    }

    public T getDelta() {
        return (T) ((RealFieldElement) this.f13003z.divide(getNorm())).asin();
    }

    public FieldVector3D<T> add(FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.add(v2.f13001x), (RealFieldElement) this.f13002y.add(v2.f13002y), (RealFieldElement) this.f13003z.add(v2.f13003z));
    }

    public FieldVector3D<T> add(Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.add(v2.getX()), (RealFieldElement) this.f13002y.add(v2.getY()), (RealFieldElement) this.f13003z.add(v2.getZ()));
    }

    public FieldVector3D<T> add(T factor, FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.getField2().getOne(), this, factor, v2);
    }

    public FieldVector3D<T> add(T factor, Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.add(factor.multiply(v2.getX())), (RealFieldElement) this.f13002y.add(factor.multiply(v2.getY())), (RealFieldElement) this.f13003z.add(factor.multiply(v2.getZ())));
    }

    public FieldVector3D<T> add(double factor, FieldVector3D<T> v2) {
        return new FieldVector3D<>(1.0d, this, factor, v2);
    }

    public FieldVector3D<T> add(double factor, Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.add(factor * v2.getX()), (RealFieldElement) this.f13002y.add(factor * v2.getY()), (RealFieldElement) this.f13003z.add(factor * v2.getZ()));
    }

    public FieldVector3D<T> subtract(FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.subtract(v2.f13001x), (RealFieldElement) this.f13002y.subtract(v2.f13002y), (RealFieldElement) this.f13003z.subtract(v2.f13003z));
    }

    public FieldVector3D<T> subtract(Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.subtract(v2.getX()), (RealFieldElement) this.f13002y.subtract(v2.getY()), (RealFieldElement) this.f13003z.subtract(v2.getZ()));
    }

    public FieldVector3D<T> subtract(T factor, FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.getField2().getOne(), this, (RealFieldElement) factor.negate(), v2);
    }

    public FieldVector3D<T> subtract(T factor, Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.subtract(factor.multiply(v2.getX())), (RealFieldElement) this.f13002y.subtract(factor.multiply(v2.getY())), (RealFieldElement) this.f13003z.subtract(factor.multiply(v2.getZ())));
    }

    public FieldVector3D<T> subtract(double factor, FieldVector3D<T> v2) {
        return new FieldVector3D<>(1.0d, this, -factor, v2);
    }

    public FieldVector3D<T> subtract(double factor, Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.subtract(factor * v2.getX()), (RealFieldElement) this.f13002y.subtract(factor * v2.getY()), (RealFieldElement) this.f13003z.subtract(factor * v2.getZ()));
    }

    public FieldVector3D<T> normalize() throws MathArithmeticException {
        RealFieldElement norm = getNorm();
        if (norm.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR, new Object[0]);
        }
        return scalarMultiply((FieldVector3D<T>) norm.reciprocal());
    }

    public FieldVector3D<T> orthogonal() throws MathArithmeticException {
        double threshold = 0.6d * getNorm().getReal();
        if (threshold == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        if (FastMath.abs(this.f13001x.getReal()) <= threshold) {
            RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f13002y.multiply(this.f13002y)).add((RealFieldElement) this.f13003z.multiply(this.f13003z))).sqrt()).reciprocal();
            return new FieldVector3D<>((RealFieldElement) realFieldElement.getField2().getZero(), (RealFieldElement) realFieldElement.multiply(this.f13003z), (RealFieldElement) ((RealFieldElement) realFieldElement.multiply(this.f13002y)).negate());
        }
        if (FastMath.abs(this.f13002y.getReal()) <= threshold) {
            RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f13001x.multiply(this.f13001x)).add((RealFieldElement) this.f13003z.multiply(this.f13003z))).sqrt()).reciprocal();
            return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) realFieldElement2.multiply(this.f13003z)).negate(), (RealFieldElement) realFieldElement2.getField2().getZero(), (RealFieldElement) realFieldElement2.multiply(this.f13001x));
        }
        RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f13001x.multiply(this.f13001x)).add((RealFieldElement) this.f13002y.multiply(this.f13002y))).sqrt()).reciprocal();
        return new FieldVector3D<>((RealFieldElement) realFieldElement3.multiply(this.f13002y), (RealFieldElement) ((RealFieldElement) realFieldElement3.multiply(this.f13001x)).negate(), (RealFieldElement) realFieldElement3.getField2().getZero());
    }

    public static <T extends RealFieldElement<T>> T angle(FieldVector3D<T> v1, FieldVector3D<T> v2) throws MathArithmeticException {
        RealFieldElement realFieldElement = (RealFieldElement) v1.getNorm().multiply(v2.getNorm());
        if (realFieldElement.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        RealFieldElement realFieldElementDotProduct = dotProduct(v1, v2);
        double threshold = realFieldElement.getReal() * 0.9999d;
        if (realFieldElementDotProduct.getReal() < (-threshold) || realFieldElementDotProduct.getReal() > threshold) {
            FieldVector3D<T> v3 = crossProduct(v1, v2);
            if (realFieldElementDotProduct.getReal() >= 0.0d) {
                return (T) ((RealFieldElement) v3.getNorm().divide(realFieldElement)).asin();
            }
            return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) v3.getNorm().divide(realFieldElement)).asin()).subtract(3.141592653589793d)).negate();
        }
        return (T) ((RealFieldElement) realFieldElementDotProduct.divide(realFieldElement)).acos();
    }

    public static <T extends RealFieldElement<T>> T angle(FieldVector3D<T> v1, Vector3D v2) throws MathArithmeticException {
        RealFieldElement realFieldElement = (RealFieldElement) v1.getNorm().multiply(v2.getNorm());
        if (realFieldElement.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM, new Object[0]);
        }
        RealFieldElement realFieldElementDotProduct = dotProduct(v1, v2);
        double threshold = realFieldElement.getReal() * 0.9999d;
        if (realFieldElementDotProduct.getReal() < (-threshold) || realFieldElementDotProduct.getReal() > threshold) {
            FieldVector3D<T> v3 = crossProduct(v1, v2);
            if (realFieldElementDotProduct.getReal() >= 0.0d) {
                return (T) ((RealFieldElement) v3.getNorm().divide(realFieldElement)).asin();
            }
            return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) v3.getNorm().divide(realFieldElement)).asin()).subtract(3.141592653589793d)).negate();
        }
        return (T) ((RealFieldElement) realFieldElementDotProduct.divide(realFieldElement)).acos();
    }

    public static <T extends RealFieldElement<T>> T angle(Vector3D vector3D, FieldVector3D<T> fieldVector3D) throws MathArithmeticException {
        return (T) angle(fieldVector3D, vector3D);
    }

    public FieldVector3D<T> negate() {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.negate(), (RealFieldElement) this.f13002y.negate(), (RealFieldElement) this.f13003z.negate());
    }

    public FieldVector3D<T> scalarMultiply(T a2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.multiply(a2), (RealFieldElement) this.f13002y.multiply(a2), (RealFieldElement) this.f13003z.multiply(a2));
    }

    public FieldVector3D<T> scalarMultiply(double a2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.multiply(a2), (RealFieldElement) this.f13002y.multiply(a2), (RealFieldElement) this.f13003z.multiply(a2));
    }

    public boolean isNaN() {
        return Double.isNaN(this.f13001x.getReal()) || Double.isNaN(this.f13002y.getReal()) || Double.isNaN(this.f13003z.getReal());
    }

    public boolean isInfinite() {
        return !isNaN() && (Double.isInfinite(this.f13001x.getReal()) || Double.isInfinite(this.f13002y.getReal()) || Double.isInfinite(this.f13003z.getReal()));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof FieldVector3D) {
            FieldVector3D<T> rhs = (FieldVector3D) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.f13001x.equals(rhs.f13001x) && this.f13002y.equals(rhs.f13002y) && this.f13003z.equals(rhs.f13003z);
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return 409;
        }
        return 311 * ((107 * this.f13001x.hashCode()) + (83 * this.f13002y.hashCode()) + this.f13003z.hashCode());
    }

    public T dotProduct(FieldVector3D<T> v2) {
        return (T) this.f13001x.linearCombination(this.f13001x, v2.f13001x, this.f13002y, v2.f13002y, this.f13003z, v2.f13003z);
    }

    public T dotProduct(Vector3D v2) {
        return (T) this.f13001x.linearCombination(v2.getX(), this.f13001x, v2.getY(), this.f13002y, v2.getZ(), this.f13003z);
    }

    public FieldVector3D<T> crossProduct(FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.linearCombination(this.f13002y, v2.f13003z, this.f13003z.negate(), v2.f13002y), (RealFieldElement) this.f13002y.linearCombination(this.f13003z, v2.f13001x, this.f13001x.negate(), v2.f13003z), (RealFieldElement) this.f13003z.linearCombination(this.f13001x, v2.f13002y, this.f13002y.negate(), v2.f13001x));
    }

    public FieldVector3D<T> crossProduct(Vector3D v2) {
        return new FieldVector3D<>((RealFieldElement) this.f13001x.linearCombination(v2.getZ(), this.f13002y, -v2.getY(), this.f13003z), (RealFieldElement) this.f13002y.linearCombination(v2.getX(), this.f13003z, -v2.getZ(), this.f13001x), (RealFieldElement) this.f13003z.linearCombination(v2.getY(), this.f13001x, -v2.getX(), this.f13002y));
    }

    public T distance1(FieldVector3D<T> v2) {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) v2.f13001x.subtract(this.f13001x)).abs();
        RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) v2.f13002y.subtract(this.f13002y)).abs();
        return (T) ((RealFieldElement) realFieldElement.add(realFieldElement2)).add((RealFieldElement) ((RealFieldElement) v2.f13003z.subtract(this.f13003z)).abs());
    }

    public T distance1(Vector3D v2) {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) this.f13001x.subtract(v2.getX())).abs();
        RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) this.f13002y.subtract(v2.getY())).abs();
        return (T) ((RealFieldElement) realFieldElement.add(realFieldElement2)).add((RealFieldElement) ((RealFieldElement) this.f13003z.subtract(v2.getZ())).abs());
    }

    public T distance(FieldVector3D<T> v2) {
        RealFieldElement realFieldElement = (RealFieldElement) v2.f13001x.subtract(this.f13001x);
        RealFieldElement realFieldElement2 = (RealFieldElement) v2.f13002y.subtract(this.f13002y);
        RealFieldElement realFieldElement3 = (RealFieldElement) v2.f13003z.subtract(this.f13003z);
        return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(realFieldElement)).add((RealFieldElement) realFieldElement2.multiply(realFieldElement2))).add((RealFieldElement) realFieldElement3.multiply(realFieldElement3))).sqrt();
    }

    public T distance(Vector3D v2) {
        RealFieldElement realFieldElement = (RealFieldElement) this.f13001x.subtract(v2.getX());
        RealFieldElement realFieldElement2 = (RealFieldElement) this.f13002y.subtract(v2.getY());
        RealFieldElement realFieldElement3 = (RealFieldElement) this.f13003z.subtract(v2.getZ());
        return (T) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(realFieldElement)).add((RealFieldElement) realFieldElement2.multiply(realFieldElement2))).add((RealFieldElement) realFieldElement3.multiply(realFieldElement3))).sqrt();
    }

    public T distanceInf(FieldVector3D<T> v2) {
        T t2 = (T) ((RealFieldElement) v2.f13001x.subtract(this.f13001x)).abs();
        T t3 = (T) ((RealFieldElement) v2.f13002y.subtract(this.f13002y)).abs();
        T t4 = (T) ((RealFieldElement) v2.f13003z.subtract(this.f13003z)).abs();
        if (t2.getReal() <= t3.getReal()) {
            if (t3.getReal() <= t4.getReal()) {
                return t4;
            }
            return t3;
        }
        if (t2.getReal() <= t4.getReal()) {
            return t4;
        }
        return t2;
    }

    public T distanceInf(Vector3D v2) {
        T t2 = (T) ((RealFieldElement) this.f13001x.subtract(v2.getX())).abs();
        T t3 = (T) ((RealFieldElement) this.f13002y.subtract(v2.getY())).abs();
        T t4 = (T) ((RealFieldElement) this.f13003z.subtract(v2.getZ())).abs();
        if (t2.getReal() <= t3.getReal()) {
            if (t3.getReal() <= t4.getReal()) {
                return t4;
            }
            return t3;
        }
        if (t2.getReal() <= t4.getReal()) {
            return t4;
        }
        return t2;
    }

    public T distanceSq(FieldVector3D<T> v2) {
        RealFieldElement realFieldElement = (RealFieldElement) v2.f13001x.subtract(this.f13001x);
        RealFieldElement realFieldElement2 = (RealFieldElement) v2.f13002y.subtract(this.f13002y);
        RealFieldElement realFieldElement3 = (RealFieldElement) v2.f13003z.subtract(this.f13003z);
        return (T) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(realFieldElement)).add((RealFieldElement) realFieldElement2.multiply(realFieldElement2))).add((RealFieldElement) realFieldElement3.multiply(realFieldElement3));
    }

    public T distanceSq(Vector3D v2) {
        RealFieldElement realFieldElement = (RealFieldElement) this.f13001x.subtract(v2.getX());
        RealFieldElement realFieldElement2 = (RealFieldElement) this.f13002y.subtract(v2.getY());
        RealFieldElement realFieldElement3 = (RealFieldElement) this.f13003z.subtract(v2.getZ());
        return (T) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(realFieldElement)).add((RealFieldElement) realFieldElement2.multiply(realFieldElement2))).add((RealFieldElement) realFieldElement3.multiply(realFieldElement3));
    }

    public static <T extends RealFieldElement<T>> T dotProduct(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2) {
        return (T) fieldVector3D.dotProduct(fieldVector3D2);
    }

    public static <T extends RealFieldElement<T>> T dotProduct(FieldVector3D<T> fieldVector3D, Vector3D vector3D) {
        return (T) fieldVector3D.dotProduct(vector3D);
    }

    public static <T extends RealFieldElement<T>> T dotProduct(Vector3D vector3D, FieldVector3D<T> fieldVector3D) {
        return (T) fieldVector3D.dotProduct(vector3D);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(FieldVector3D<T> v1, FieldVector3D<T> v2) {
        return v1.crossProduct(v2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(FieldVector3D<T> v1, Vector3D v2) {
        return v1.crossProduct(v2);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> crossProduct(Vector3D v1, FieldVector3D<T> v2) {
        return new FieldVector3D<>((RealFieldElement) ((FieldVector3D) v2).f13001x.linearCombination(v1.getY(), ((FieldVector3D) v2).f13003z, -v1.getZ(), ((FieldVector3D) v2).f13002y), (RealFieldElement) ((FieldVector3D) v2).f13002y.linearCombination(v1.getZ(), ((FieldVector3D) v2).f13001x, -v1.getX(), ((FieldVector3D) v2).f13003z), (RealFieldElement) ((FieldVector3D) v2).f13003z.linearCombination(v1.getX(), ((FieldVector3D) v2).f13002y, -v1.getY(), ((FieldVector3D) v2).f13001x));
    }

    public static <T extends RealFieldElement<T>> T distance1(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2) {
        return (T) fieldVector3D.distance1(fieldVector3D2);
    }

    public static <T extends RealFieldElement<T>> T distance1(FieldVector3D<T> fieldVector3D, Vector3D vector3D) {
        return (T) fieldVector3D.distance1(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distance1(Vector3D vector3D, FieldVector3D<T> fieldVector3D) {
        return (T) fieldVector3D.distance1(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distance(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2) {
        return (T) fieldVector3D.distance(fieldVector3D2);
    }

    public static <T extends RealFieldElement<T>> T distance(FieldVector3D<T> fieldVector3D, Vector3D vector3D) {
        return (T) fieldVector3D.distance(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distance(Vector3D vector3D, FieldVector3D<T> fieldVector3D) {
        return (T) fieldVector3D.distance(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2) {
        return (T) fieldVector3D.distanceInf(fieldVector3D2);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(FieldVector3D<T> fieldVector3D, Vector3D vector3D) {
        return (T) fieldVector3D.distanceInf(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distanceInf(Vector3D vector3D, FieldVector3D<T> fieldVector3D) {
        return (T) fieldVector3D.distanceInf(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(FieldVector3D<T> fieldVector3D, FieldVector3D<T> fieldVector3D2) {
        return (T) fieldVector3D.distanceSq(fieldVector3D2);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(FieldVector3D<T> fieldVector3D, Vector3D vector3D) {
        return (T) fieldVector3D.distanceSq(vector3D);
    }

    public static <T extends RealFieldElement<T>> T distanceSq(Vector3D vector3D, FieldVector3D<T> fieldVector3D) {
        return (T) fieldVector3D.distanceSq(vector3D);
    }

    public String toString() {
        return Vector3DFormat.getInstance().format(toVector3D());
    }

    public String toString(NumberFormat format) {
        return new Vector3DFormat(format).format(toVector3D());
    }
}
