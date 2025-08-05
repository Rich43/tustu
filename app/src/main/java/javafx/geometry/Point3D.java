package javafx.geometry;

import javafx.beans.NamedArg;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/geometry/Point3D.class */
public class Point3D {
    public static final Point3D ZERO = new Point3D(0.0d, 0.0d, 0.0d);

    /* renamed from: x, reason: collision with root package name */
    private double f12638x;

    /* renamed from: y, reason: collision with root package name */
    private double f12639y;

    /* renamed from: z, reason: collision with root package name */
    private double f12640z;
    private int hash = 0;

    public final double getX() {
        return this.f12638x;
    }

    public final double getY() {
        return this.f12639y;
    }

    public final double getZ() {
        return this.f12640z;
    }

    public Point3D(@NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("z") double z2) {
        this.f12638x = x2;
        this.f12639y = y2;
        this.f12640z = z2;
    }

    public double distance(double x1, double y1, double z1) {
        double a2 = getX() - x1;
        double b2 = getY() - y1;
        double c2 = getZ() - z1;
        return Math.sqrt((a2 * a2) + (b2 * b2) + (c2 * c2));
    }

    public double distance(Point3D point) {
        return distance(point.getX(), point.getY(), point.getZ());
    }

    public Point3D add(double x2, double y2, double z2) {
        return new Point3D(getX() + x2, getY() + y2, getZ() + z2);
    }

    public Point3D add(Point3D point) {
        return add(point.getX(), point.getY(), point.getZ());
    }

    public Point3D subtract(double x2, double y2, double z2) {
        return new Point3D(getX() - x2, getY() - y2, getZ() - z2);
    }

    public Point3D subtract(Point3D point) {
        return subtract(point.getX(), point.getY(), point.getZ());
    }

    public Point3D multiply(double factor) {
        return new Point3D(getX() * factor, getY() * factor, getZ() * factor);
    }

    public Point3D normalize() {
        double mag = magnitude();
        if (mag == 0.0d) {
            return new Point3D(0.0d, 0.0d, 0.0d);
        }
        return new Point3D(getX() / mag, getY() / mag, getZ() / mag);
    }

    public Point3D midpoint(double x2, double y2, double z2) {
        return new Point3D(x2 + ((getX() - x2) / 2.0d), y2 + ((getY() - y2) / 2.0d), z2 + ((getZ() - z2) / 2.0d));
    }

    public Point3D midpoint(Point3D point) {
        return midpoint(point.getX(), point.getY(), point.getZ());
    }

    public double angle(double x2, double y2, double z2) {
        double ax2 = getX();
        double ay2 = getY();
        double az2 = getZ();
        double delta = (((ax2 * x2) + (ay2 * y2)) + (az2 * z2)) / Math.sqrt((((ax2 * ax2) + (ay2 * ay2)) + (az2 * az2)) * (((x2 * x2) + (y2 * y2)) + (z2 * z2)));
        if (delta > 1.0d) {
            return 0.0d;
        }
        if (delta < -1.0d) {
            return 180.0d;
        }
        return Math.toDegrees(Math.acos(delta));
    }

    public double angle(Point3D point) {
        return angle(point.getX(), point.getY(), point.getZ());
    }

    public double angle(Point3D p1, Point3D p2) {
        double x2 = getX();
        double y2 = getY();
        double z2 = getZ();
        double ax2 = p1.getX() - x2;
        double ay2 = p1.getY() - y2;
        double az2 = p1.getZ() - z2;
        double bx2 = p2.getX() - x2;
        double by2 = p2.getY() - y2;
        double bz2 = p2.getZ() - z2;
        double delta = (((ax2 * bx2) + (ay2 * by2)) + (az2 * bz2)) / Math.sqrt((((ax2 * ax2) + (ay2 * ay2)) + (az2 * az2)) * (((bx2 * bx2) + (by2 * by2)) + (bz2 * bz2)));
        if (delta > 1.0d) {
            return 0.0d;
        }
        if (delta < -1.0d) {
            return 180.0d;
        }
        return Math.toDegrees(Math.acos(delta));
    }

    public double magnitude() {
        double x2 = getX();
        double y2 = getY();
        double z2 = getZ();
        return Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
    }

    public double dotProduct(double x2, double y2, double z2) {
        return (getX() * x2) + (getY() * y2) + (getZ() * z2);
    }

    public double dotProduct(Point3D vector) {
        return dotProduct(vector.getX(), vector.getY(), vector.getZ());
    }

    public Point3D crossProduct(double x2, double y2, double z2) {
        double ax2 = getX();
        double ay2 = getY();
        double az2 = getZ();
        return new Point3D((ay2 * z2) - (az2 * y2), (az2 * x2) - (ax2 * z2), (ax2 * y2) - (ay2 * x2));
    }

    public Point3D crossProduct(Point3D vector) {
        return crossProduct(vector.getX(), vector.getY(), vector.getZ());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Point3D) {
            Point3D other = (Point3D) obj;
            return getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * 7) + Double.doubleToLongBits(getX());
            long bits2 = (31 * ((31 * bits) + Double.doubleToLongBits(getY()))) + Double.doubleToLongBits(getZ());
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Point3D [x = " + getX() + ", y = " + getY() + ", z = " + getZ() + "]";
    }
}
