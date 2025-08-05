package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Vec3f.class */
public class Vec3f {

    /* renamed from: x, reason: collision with root package name */
    public float f11933x;

    /* renamed from: y, reason: collision with root package name */
    public float f11934y;

    /* renamed from: z, reason: collision with root package name */
    public float f11935z;

    public Vec3f() {
    }

    public Vec3f(float x2, float y2, float z2) {
        this.f11933x = x2;
        this.f11934y = y2;
        this.f11935z = z2;
    }

    public Vec3f(Vec3f v2) {
        this.f11933x = v2.f11933x;
        this.f11934y = v2.f11934y;
        this.f11935z = v2.f11935z;
    }

    public void set(Vec3f v2) {
        this.f11933x = v2.f11933x;
        this.f11934y = v2.f11934y;
        this.f11935z = v2.f11935z;
    }

    public void set(float x2, float y2, float z2) {
        this.f11933x = x2;
        this.f11934y = y2;
        this.f11935z = z2;
    }

    public final void mul(float s2) {
        this.f11933x *= s2;
        this.f11934y *= s2;
        this.f11935z *= s2;
    }

    public void sub(Vec3f t1, Vec3f t2) {
        this.f11933x = t1.f11933x - t2.f11933x;
        this.f11934y = t1.f11934y - t2.f11934y;
        this.f11935z = t1.f11935z - t2.f11935z;
    }

    public void sub(Vec3f t1) {
        this.f11933x -= t1.f11933x;
        this.f11934y -= t1.f11934y;
        this.f11935z -= t1.f11935z;
    }

    public void add(Vec3f t1, Vec3f t2) {
        this.f11933x = t1.f11933x + t2.f11933x;
        this.f11934y = t1.f11934y + t2.f11934y;
        this.f11935z = t1.f11935z + t2.f11935z;
    }

    public void add(Vec3f t1) {
        this.f11933x += t1.f11933x;
        this.f11934y += t1.f11934y;
        this.f11935z += t1.f11935z;
    }

    public float length() {
        return (float) Math.sqrt((this.f11933x * this.f11933x) + (this.f11934y * this.f11934y) + (this.f11935z * this.f11935z));
    }

    public void normalize() {
        float norm = 1.0f / length();
        this.f11933x *= norm;
        this.f11934y *= norm;
        this.f11935z *= norm;
    }

    public void cross(Vec3f v1, Vec3f v2) {
        float tmpX = (v1.f11934y * v2.f11935z) - (v1.f11935z * v2.f11934y);
        float tmpY = (v2.f11933x * v1.f11935z) - (v2.f11935z * v1.f11933x);
        this.f11935z = (v1.f11933x * v2.f11934y) - (v1.f11934y * v2.f11933x);
        this.f11933x = tmpX;
        this.f11934y = tmpY;
    }

    public float dot(Vec3f v1) {
        return (this.f11933x * v1.f11933x) + (this.f11934y * v1.f11934y) + (this.f11935z * v1.f11935z);
    }

    public int hashCode() {
        int bits = (31 * 7) + Float.floatToIntBits(this.f11933x);
        return (31 * ((31 * bits) + Float.floatToIntBits(this.f11934y))) + Float.floatToIntBits(this.f11935z);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec3f) {
            Vec3f v2 = (Vec3f) obj;
            return this.f11933x == v2.f11933x && this.f11934y == v2.f11934y && this.f11935z == v2.f11935z;
        }
        return false;
    }

    public String toString() {
        return "Vec3f[" + this.f11933x + ", " + this.f11934y + ", " + this.f11935z + "]";
    }
}
