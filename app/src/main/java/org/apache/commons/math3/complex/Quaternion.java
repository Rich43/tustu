package org.apache.commons.math3.complex;

import java.io.Serializable;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/complex/Quaternion.class */
public final class Quaternion implements Serializable {
    public static final Quaternion IDENTITY = new Quaternion(1.0d, 0.0d, 0.0d, 0.0d);
    public static final Quaternion ZERO = new Quaternion(0.0d, 0.0d, 0.0d, 0.0d);

    /* renamed from: I, reason: collision with root package name */
    public static final Quaternion f12982I = new Quaternion(0.0d, 1.0d, 0.0d, 0.0d);

    /* renamed from: J, reason: collision with root package name */
    public static final Quaternion f12983J = new Quaternion(0.0d, 0.0d, 1.0d, 0.0d);

    /* renamed from: K, reason: collision with root package name */
    public static final Quaternion f12984K = new Quaternion(0.0d, 0.0d, 0.0d, 1.0d);
    private static final long serialVersionUID = 20092012;
    private final double q0;
    private final double q1;
    private final double q2;
    private final double q3;

    public Quaternion(double a2, double b2, double c2, double d2) {
        this.q0 = a2;
        this.q1 = b2;
        this.q2 = c2;
        this.q3 = d2;
    }

    public Quaternion(double scalar, double[] v2) throws DimensionMismatchException {
        if (v2.length != 3) {
            throw new DimensionMismatchException(v2.length, 3);
        }
        this.q0 = scalar;
        this.q1 = v2[0];
        this.q2 = v2[1];
        this.q3 = v2[2];
    }

    public Quaternion(double[] v2) {
        this(0.0d, v2);
    }

    public Quaternion getConjugate() {
        return new Quaternion(this.q0, -this.q1, -this.q2, -this.q3);
    }

    public static Quaternion multiply(Quaternion q1, Quaternion q2) {
        double q1a = q1.getQ0();
        double q1b = q1.getQ1();
        double q1c = q1.getQ2();
        double q1d = q1.getQ3();
        double q2a = q2.getQ0();
        double q2b = q2.getQ1();
        double q2c = q2.getQ2();
        double q2d = q2.getQ3();
        double w2 = (((q1a * q2a) - (q1b * q2b)) - (q1c * q2c)) - (q1d * q2d);
        double x2 = (((q1a * q2b) + (q1b * q2a)) + (q1c * q2d)) - (q1d * q2c);
        double y2 = ((q1a * q2c) - (q1b * q2d)) + (q1c * q2a) + (q1d * q2b);
        double z2 = (((q1a * q2d) + (q1b * q2c)) - (q1c * q2b)) + (q1d * q2a);
        return new Quaternion(w2, x2, y2, z2);
    }

    public Quaternion multiply(Quaternion q2) {
        return multiply(this, q2);
    }

    public static Quaternion add(Quaternion q1, Quaternion q2) {
        return new Quaternion(q1.getQ0() + q2.getQ0(), q1.getQ1() + q2.getQ1(), q1.getQ2() + q2.getQ2(), q1.getQ3() + q2.getQ3());
    }

    public Quaternion add(Quaternion q2) {
        return add(this, q2);
    }

    public static Quaternion subtract(Quaternion q1, Quaternion q2) {
        return new Quaternion(q1.getQ0() - q2.getQ0(), q1.getQ1() - q2.getQ1(), q1.getQ2() - q2.getQ2(), q1.getQ3() - q2.getQ3());
    }

    public Quaternion subtract(Quaternion q2) {
        return subtract(this, q2);
    }

    public static double dotProduct(Quaternion q1, Quaternion q2) {
        return (q1.getQ0() * q2.getQ0()) + (q1.getQ1() * q2.getQ1()) + (q1.getQ2() * q2.getQ2()) + (q1.getQ3() * q2.getQ3());
    }

    public double dotProduct(Quaternion q2) {
        return dotProduct(this, q2);
    }

    public double getNorm() {
        return FastMath.sqrt((this.q0 * this.q0) + (this.q1 * this.q1) + (this.q2 * this.q2) + (this.q3 * this.q3));
    }

    public Quaternion normalize() {
        double norm = getNorm();
        if (norm < Precision.SAFE_MIN) {
            throw new ZeroException(LocalizedFormats.NORM, Double.valueOf(norm));
        }
        return new Quaternion(this.q0 / norm, this.q1 / norm, this.q2 / norm, this.q3 / norm);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Quaternion) {
            Quaternion q2 = (Quaternion) other;
            return this.q0 == q2.getQ0() && this.q1 == q2.getQ1() && this.q2 == q2.getQ2() && this.q3 == q2.getQ3();
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        double[] arr$ = {this.q0, this.q1, this.q2, this.q3};
        for (double comp : arr$) {
            int c2 = MathUtils.hash(comp);
            result = (31 * result) + c2;
        }
        return result;
    }

    public boolean equals(Quaternion q2, double eps) {
        return Precision.equals(this.q0, q2.getQ0(), eps) && Precision.equals(this.q1, q2.getQ1(), eps) && Precision.equals(this.q2, q2.getQ2(), eps) && Precision.equals(this.q3, q2.getQ3(), eps);
    }

    public boolean isUnitQuaternion(double eps) {
        return Precision.equals(getNorm(), 1.0d, eps);
    }

    public boolean isPureQuaternion(double eps) {
        return FastMath.abs(getQ0()) <= eps;
    }

    public Quaternion getPositivePolarForm() {
        if (getQ0() < 0.0d) {
            Quaternion unitQ = normalize();
            return new Quaternion(-unitQ.getQ0(), -unitQ.getQ1(), -unitQ.getQ2(), -unitQ.getQ3());
        }
        return normalize();
    }

    public Quaternion getInverse() {
        double squareNorm = (this.q0 * this.q0) + (this.q1 * this.q1) + (this.q2 * this.q2) + (this.q3 * this.q3);
        if (squareNorm < Precision.SAFE_MIN) {
            throw new ZeroException(LocalizedFormats.NORM, Double.valueOf(squareNorm));
        }
        return new Quaternion(this.q0 / squareNorm, (-this.q1) / squareNorm, (-this.q2) / squareNorm, (-this.q3) / squareNorm);
    }

    public double getQ0() {
        return this.q0;
    }

    public double getQ1() {
        return this.q1;
    }

    public double getQ2() {
        return this.q2;
    }

    public double getQ3() {
        return this.q3;
    }

    public double getScalarPart() {
        return getQ0();
    }

    public double[] getVectorPart() {
        return new double[]{getQ1(), getQ2(), getQ3()};
    }

    public Quaternion multiply(double alpha) {
        return new Quaternion(alpha * this.q0, alpha * this.q1, alpha * this.q2, alpha * this.q3);
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder();
        s2.append("[").append(this.q0).append(" ").append(this.q1).append(" ").append(this.q2).append(" ").append(this.q3).append("]");
        return s2.toString();
    }
}
