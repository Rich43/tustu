package org.apache.commons.math3.geometry.spherical.oned;

import com.sun.glass.events.WindowEvent;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/spherical/oned/S1Point.class */
public class S1Point implements Point<Sphere1D> {
    public static final S1Point NaN = new S1Point(Double.NaN, Vector2D.NaN);
    private static final long serialVersionUID = 20131218;
    private final double alpha;
    private final Vector2D vector;

    public S1Point(double alpha) {
        this(MathUtils.normalizeAngle(alpha, 3.141592653589793d), new Vector2D(FastMath.cos(alpha), FastMath.sin(alpha)));
    }

    private S1Point(double alpha, Vector2D vector) {
        this.alpha = alpha;
        this.vector = vector;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public Vector2D getVector() {
        return this.vector;
    }

    @Override // org.apache.commons.math3.geometry.Point
    public Space getSpace() {
        return Sphere1D.getInstance();
    }

    @Override // org.apache.commons.math3.geometry.Point
    public boolean isNaN() {
        return Double.isNaN(this.alpha);
    }

    @Override // org.apache.commons.math3.geometry.Point
    public double distance(Point<Sphere1D> point) {
        return distance(this, (S1Point) point);
    }

    public static double distance(S1Point p1, S1Point p2) {
        return Vector2D.angle(p1.vector, p2.vector);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof S1Point) {
            S1Point rhs = (S1Point) other;
            if (rhs.isNaN()) {
                return isNaN();
            }
            return this.alpha == rhs.alpha;
        }
        return false;
    }

    public int hashCode() {
        if (isNaN()) {
            return WindowEvent.FOCUS_GAINED;
        }
        return 1759 * MathUtils.hash(this.alpha);
    }
}
