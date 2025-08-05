package org.apache.commons.math3.geometry.spherical.twod;

import com.sun.glass.events.WindowEvent;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/twod/S2Point.class */
public class S2Point implements Point<Sphere2D> {
    public static final S2Point PLUS_I = new S2Point(0.0d, 1.5707963267948966d, Vector3D.PLUS_I);
    public static final S2Point PLUS_J = new S2Point(1.5707963267948966d, 1.5707963267948966d, Vector3D.PLUS_J);
    public static final S2Point PLUS_K = new S2Point(0.0d, 0.0d, Vector3D.PLUS_K);
    public static final S2Point MINUS_I = new S2Point(3.141592653589793d, 1.5707963267948966d, Vector3D.MINUS_I);
    public static final S2Point MINUS_J = new S2Point(4.71238898038469d, 1.5707963267948966d, Vector3D.MINUS_J);
    public static final S2Point MINUS_K = new S2Point(0.0d, 3.141592653589793d, Vector3D.MINUS_K);
    public static final S2Point NaN = new S2Point(Double.NaN, Double.NaN, Vector3D.NaN);
    private static final long serialVersionUID = 20131218;
    private final double theta;
    private final double phi;
    private final Vector3D vector;

    public S2Point(double theta, double phi) throws OutOfRangeException {
        this(theta, phi, vector(theta, phi));
    }

    public S2Point(Vector3D vector) throws MathArithmeticException {
        this(FastMath.atan2(vector.getY(), vector.getX()), Vector3D.angle(Vector3D.PLUS_K, vector), vector.normalize());
    }

    private S2Point(double theta, double phi, Vector3D vector) {
        this.theta = theta;
        this.phi = phi;
        this.vector = vector;
    }

    private static Vector3D vector(double theta, double phi) throws OutOfRangeException {
        if (phi < 0.0d || phi > 3.141592653589793d) {
            throw new OutOfRangeException(Double.valueOf(phi), 0, Double.valueOf(3.141592653589793d));
        }
        double cosTheta = FastMath.cos(theta);
        double sinTheta = FastMath.sin(theta);
        double cosPhi = FastMath.cos(phi);
        double sinPhi = FastMath.sin(phi);
        return new Vector3D(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);
    }

    public double getTheta() {
        return this.theta;
    }

    public double getPhi() {
        return this.phi;
    }

    public Vector3D getVector() {
        return this.vector;
    }

    @Override // org.apache.commons.math3.geometry.Point
    public Space getSpace() {
        return Sphere2D.getInstance();
    }

    @Override // org.apache.commons.math3.geometry.Point
    public boolean isNaN() {
        return Double.isNaN(this.theta) || Double.isNaN(this.phi);
    }

    public S2Point negate() {
        return new S2Point(-this.theta, 3.141592653589793d - this.phi, this.vector.negate());
    }

    @Override // org.apache.commons.math3.geometry.Point
    public double distance(Point<Sphere2D> point) {
        return distance(this, (S2Point) point);
    }

    public static double distance(S2Point p1, S2Point p2) {
        return Vector3D.angle(p1.vector, p2.vector);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof S2Point) {
            S2Point rhs = (S2Point) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.theta == rhs.theta && this.phi == rhs.phi;
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return WindowEvent.FOCUS_GAINED;
        }
        return 134 * ((37 * MathUtils.hash(this.theta)) + MathUtils.hash(this.phi));
    }
}
