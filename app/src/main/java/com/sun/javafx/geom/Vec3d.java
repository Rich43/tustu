package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Vec3d.class */
public class Vec3d {

    /* renamed from: x, reason: collision with root package name */
    public double f11930x;

    /* renamed from: y, reason: collision with root package name */
    public double f11931y;

    /* renamed from: z, reason: collision with root package name */
    public double f11932z;

    public Vec3d() {
    }

    public Vec3d(double x2, double y2, double z2) {
        this.f11930x = x2;
        this.f11931y = y2;
        this.f11932z = z2;
    }

    public Vec3d(Vec3d v2) {
        set(v2);
    }

    public Vec3d(Vec3f v2) {
        set(v2);
    }

    public void set(Vec3f v2) {
        this.f11930x = v2.f11933x;
        this.f11931y = v2.f11934y;
        this.f11932z = v2.f11935z;
    }

    public void set(Vec3d v2) {
        this.f11930x = v2.f11930x;
        this.f11931y = v2.f11931y;
        this.f11932z = v2.f11932z;
    }

    public void set(double x2, double y2, double z2) {
        this.f11930x = x2;
        this.f11931y = y2;
        this.f11932z = z2;
    }

    public void mul(double scale) {
        this.f11930x *= scale;
        this.f11931y *= scale;
        this.f11932z *= scale;
    }

    public void sub(Vec3f t1, Vec3f t2) {
        this.f11930x = t1.f11933x - t2.f11933x;
        this.f11931y = t1.f11934y - t2.f11934y;
        this.f11932z = t1.f11935z - t2.f11935z;
    }

    public void sub(Vec3d t1, Vec3d t2) {
        this.f11930x = t1.f11930x - t2.f11930x;
        this.f11931y = t1.f11931y - t2.f11931y;
        this.f11932z = t1.f11932z - t2.f11932z;
    }

    public void sub(Vec3d t1) {
        this.f11930x -= t1.f11930x;
        this.f11931y -= t1.f11931y;
        this.f11932z -= t1.f11932z;
    }

    public void add(Vec3d t1, Vec3d t2) {
        this.f11930x = t1.f11930x + t2.f11930x;
        this.f11931y = t1.f11931y + t2.f11931y;
        this.f11932z = t1.f11932z + t2.f11932z;
    }

    public void add(Vec3d t1) {
        this.f11930x += t1.f11930x;
        this.f11931y += t1.f11931y;
        this.f11932z += t1.f11932z;
    }

    public double length() {
        return Math.sqrt((this.f11930x * this.f11930x) + (this.f11931y * this.f11931y) + (this.f11932z * this.f11932z));
    }

    public void normalize() {
        double norm = 1.0d / length();
        this.f11930x *= norm;
        this.f11931y *= norm;
        this.f11932z *= norm;
    }

    public void cross(Vec3d v1, Vec3d v2) {
        double tmpX = (v1.f11931y * v2.f11932z) - (v1.f11932z * v2.f11931y);
        double tmpY = (v2.f11930x * v1.f11932z) - (v2.f11932z * v1.f11930x);
        this.f11932z = (v1.f11930x * v2.f11931y) - (v1.f11931y * v2.f11930x);
        this.f11930x = tmpX;
        this.f11931y = tmpY;
    }

    public double dot(Vec3d v1) {
        return (this.f11930x * v1.f11930x) + (this.f11931y * v1.f11931y) + (this.f11932z * v1.f11932z);
    }

    public int hashCode() {
        long bits = (31 * 7) + Double.doubleToLongBits(this.f11930x);
        long bits2 = (31 * ((31 * bits) + Double.doubleToLongBits(this.f11931y))) + Double.doubleToLongBits(this.f11932z);
        return (int) (bits2 ^ (bits2 >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec3d) {
            Vec3d v2 = (Vec3d) obj;
            return this.f11930x == v2.f11930x && this.f11931y == v2.f11931y && this.f11932z == v2.f11932z;
        }
        return false;
    }

    public String toString() {
        return "Vec3d[" + this.f11930x + ", " + this.f11931y + ", " + this.f11932z + "]";
    }
}
