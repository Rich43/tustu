package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Vec2d.class */
public class Vec2d {

    /* renamed from: x, reason: collision with root package name */
    public double f11926x;

    /* renamed from: y, reason: collision with root package name */
    public double f11927y;

    public Vec2d() {
    }

    public Vec2d(double x2, double y2) {
        this.f11926x = x2;
        this.f11927y = y2;
    }

    public Vec2d(Vec2d v2) {
        set(v2);
    }

    public Vec2d(Vec2f v2) {
        set(v2);
    }

    public void set(Vec2d v2) {
        this.f11926x = v2.f11926x;
        this.f11927y = v2.f11927y;
    }

    public void set(Vec2f v2) {
        this.f11926x = v2.f11928x;
        this.f11927y = v2.f11929y;
    }

    public void set(double x2, double y2) {
        this.f11926x = x2;
        this.f11927y = y2;
    }

    public static double distanceSq(double x1, double y1, double x2, double y2) {
        double x12 = x1 - x2;
        double y12 = y1 - y2;
        return (x12 * x12) + (y12 * y12);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double x12 = x1 - x2;
        double y12 = y1 - y2;
        return Math.sqrt((x12 * x12) + (y12 * y12));
    }

    public double distanceSq(double vx, double vy) {
        double vx2 = vx - this.f11926x;
        double vy2 = vy - this.f11927y;
        return (vx2 * vx2) + (vy2 * vy2);
    }

    public double distanceSq(Vec2d v2) {
        double vx = v2.f11926x - this.f11926x;
        double vy = v2.f11927y - this.f11927y;
        return (vx * vx) + (vy * vy);
    }

    public double distance(double vx, double vy) {
        double vx2 = vx - this.f11926x;
        double vy2 = vy - this.f11927y;
        return Math.sqrt((vx2 * vx2) + (vy2 * vy2));
    }

    public double distance(Vec2d v2) {
        double vx = v2.f11926x - this.f11926x;
        double vy = v2.f11927y - this.f11927y;
        return Math.sqrt((vx * vx) + (vy * vy));
    }

    public int hashCode() {
        long bits = (31 * ((31 * 7) + Double.doubleToLongBits(this.f11926x))) + Double.doubleToLongBits(this.f11927y);
        return (int) (bits ^ (bits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2d) {
            Vec2d v2 = (Vec2d) obj;
            return this.f11926x == v2.f11926x && this.f11927y == v2.f11927y;
        }
        return false;
    }

    public String toString() {
        return "Vec2d[" + this.f11926x + ", " + this.f11927y + "]";
    }
}
