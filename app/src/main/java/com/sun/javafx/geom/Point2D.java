package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Point2D.class */
public class Point2D {

    /* renamed from: x, reason: collision with root package name */
    public float f11907x;

    /* renamed from: y, reason: collision with root package name */
    public float f11908y;

    public Point2D() {
    }

    public Point2D(float x2, float y2) {
        this.f11907x = x2;
        this.f11908y = y2;
    }

    public void setLocation(float x2, float y2) {
        this.f11907x = x2;
        this.f11908y = y2;
    }

    public void setLocation(Point2D p2) {
        setLocation(p2.f11907x, p2.f11908y);
    }

    public static float distanceSq(float x1, float y1, float x2, float y2) {
        float x12 = x1 - x2;
        float y12 = y1 - y2;
        return (x12 * x12) + (y12 * y12);
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        float x12 = x1 - x2;
        float y12 = y1 - y2;
        return (float) Math.sqrt((x12 * x12) + (y12 * y12));
    }

    public float distanceSq(float px, float py) {
        float px2 = px - this.f11907x;
        float py2 = py - this.f11908y;
        return (px2 * px2) + (py2 * py2);
    }

    public float distanceSq(Point2D pt) {
        float px = pt.f11907x - this.f11907x;
        float py = pt.f11908y - this.f11908y;
        return (px * px) + (py * py);
    }

    public float distance(float px, float py) {
        float px2 = px - this.f11907x;
        float py2 = py - this.f11908y;
        return (float) Math.sqrt((px2 * px2) + (py2 * py2));
    }

    public float distance(Point2D pt) {
        float px = pt.f11907x - this.f11907x;
        float py = pt.f11908y - this.f11908y;
        return (float) Math.sqrt((px * px) + (py * py));
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.f11907x);
        return bits ^ (Float.floatToIntBits(this.f11908y) * 31);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Point2D) {
            Point2D p2d = (Point2D) obj;
            return this.f11907x == p2d.f11907x && this.f11908y == p2d.f11908y;
        }
        return false;
    }

    public String toString() {
        return "Point2D[" + this.f11907x + ", " + this.f11908y + "]";
    }
}
