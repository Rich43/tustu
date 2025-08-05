package javafx.geometry;

import javafx.beans.NamedArg;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/geometry/Point2D.class */
public class Point2D {
    public static final Point2D ZERO = new Point2D(0.0d, 0.0d);

    /* renamed from: x, reason: collision with root package name */
    private double f12634x;

    /* renamed from: y, reason: collision with root package name */
    private double f12635y;
    private int hash = 0;

    public final double getX() {
        return this.f12634x;
    }

    public final double getY() {
        return this.f12635y;
    }

    public Point2D(@NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2) {
        this.f12634x = x2;
        this.f12635y = y2;
    }

    public double distance(double x1, double y1) {
        double a2 = getX() - x1;
        double b2 = getY() - y1;
        return Math.sqrt((a2 * a2) + (b2 * b2));
    }

    public double distance(Point2D point) {
        return distance(point.getX(), point.getY());
    }

    public Point2D add(double x2, double y2) {
        return new Point2D(getX() + x2, getY() + y2);
    }

    public Point2D add(Point2D point) {
        return add(point.getX(), point.getY());
    }

    public Point2D subtract(double x2, double y2) {
        return new Point2D(getX() - x2, getY() - y2);
    }

    public Point2D multiply(double factor) {
        return new Point2D(getX() * factor, getY() * factor);
    }

    public Point2D subtract(Point2D point) {
        return subtract(point.getX(), point.getY());
    }

    public Point2D normalize() {
        double mag = magnitude();
        if (mag == 0.0d) {
            return new Point2D(0.0d, 0.0d);
        }
        return new Point2D(getX() / mag, getY() / mag);
    }

    public Point2D midpoint(double x2, double y2) {
        return new Point2D(x2 + ((getX() - x2) / 2.0d), y2 + ((getY() - y2) / 2.0d));
    }

    public Point2D midpoint(Point2D point) {
        return midpoint(point.getX(), point.getY());
    }

    public double angle(double x2, double y2) {
        double ax2 = getX();
        double ay2 = getY();
        double delta = ((ax2 * x2) + (ay2 * y2)) / Math.sqrt(((ax2 * ax2) + (ay2 * ay2)) * ((x2 * x2) + (y2 * y2)));
        if (delta > 1.0d) {
            return 0.0d;
        }
        if (delta < -1.0d) {
            return 180.0d;
        }
        return Math.toDegrees(Math.acos(delta));
    }

    public double angle(Point2D point) {
        return angle(point.getX(), point.getY());
    }

    public double angle(Point2D p1, Point2D p2) {
        double x2 = getX();
        double y2 = getY();
        double ax2 = p1.getX() - x2;
        double ay2 = p1.getY() - y2;
        double bx2 = p2.getX() - x2;
        double by2 = p2.getY() - y2;
        double delta = ((ax2 * bx2) + (ay2 * by2)) / Math.sqrt(((ax2 * ax2) + (ay2 * ay2)) * ((bx2 * bx2) + (by2 * by2)));
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
        return Math.sqrt((x2 * x2) + (y2 * y2));
    }

    public double dotProduct(double x2, double y2) {
        return (getX() * x2) + (getY() * y2);
    }

    public double dotProduct(Point2D vector) {
        return dotProduct(vector.getX(), vector.getY());
    }

    public Point3D crossProduct(double x2, double y2) {
        double ax2 = getX();
        double ay2 = getY();
        return new Point3D(0.0d, 0.0d, (ax2 * y2) - (ay2 * x2));
    }

    public Point3D crossProduct(Point2D vector) {
        return crossProduct(vector.getX(), vector.getY());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Point2D) {
            Point2D other = (Point2D) obj;
            return getX() == other.getX() && getY() == other.getY();
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (31 * ((31 * 7) + Double.doubleToLongBits(getX()))) + Double.doubleToLongBits(getY());
            this.hash = (int) (bits ^ (bits >> 32));
        }
        return this.hash;
    }

    public String toString() {
        return "Point2D [x = " + getX() + ", y = " + getY() + "]";
    }
}
