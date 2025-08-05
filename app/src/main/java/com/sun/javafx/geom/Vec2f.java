package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Vec2f.class */
public class Vec2f {

    /* renamed from: x, reason: collision with root package name */
    public float f11928x;

    /* renamed from: y, reason: collision with root package name */
    public float f11929y;

    public Vec2f() {
    }

    public Vec2f(float x2, float y2) {
        this.f11928x = x2;
        this.f11929y = y2;
    }

    public Vec2f(Vec2f v2) {
        this.f11928x = v2.f11928x;
        this.f11929y = v2.f11929y;
    }

    public void set(Vec2f v2) {
        this.f11928x = v2.f11928x;
        this.f11929y = v2.f11929y;
    }

    public void set(float x2, float y2) {
        this.f11928x = x2;
        this.f11929y = y2;
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

    public float distanceSq(float vx, float vy) {
        float vx2 = vx - this.f11928x;
        float vy2 = vy - this.f11929y;
        return (vx2 * vx2) + (vy2 * vy2);
    }

    public float distanceSq(Vec2f v2) {
        float vx = v2.f11928x - this.f11928x;
        float vy = v2.f11929y - this.f11929y;
        return (vx * vx) + (vy * vy);
    }

    public float distance(float vx, float vy) {
        float vx2 = vx - this.f11928x;
        float vy2 = vy - this.f11929y;
        return (float) Math.sqrt((vx2 * vx2) + (vy2 * vy2));
    }

    public float distance(Vec2f v2) {
        float vx = v2.f11928x - this.f11928x;
        float vy = v2.f11929y - this.f11929y;
        return (float) Math.sqrt((vx * vx) + (vy * vy));
    }

    public int hashCode() {
        int bits = (31 * 7) + Float.floatToIntBits(this.f11928x);
        return (31 * bits) + Float.floatToIntBits(this.f11929y);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vec2f) {
            Vec2f v2 = (Vec2f) obj;
            return this.f11928x == v2.f11928x && this.f11929y == v2.f11929y;
        }
        return false;
    }

    public String toString() {
        return "Vec2f[" + this.f11928x + ", " + this.f11929y + "]";
    }
}
