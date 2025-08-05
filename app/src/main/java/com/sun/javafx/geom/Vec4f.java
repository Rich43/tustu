package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Vec4f.class */
public class Vec4f {

    /* renamed from: x, reason: collision with root package name */
    public float f11940x;

    /* renamed from: y, reason: collision with root package name */
    public float f11941y;

    /* renamed from: z, reason: collision with root package name */
    public float f11942z;

    /* renamed from: w, reason: collision with root package name */
    public float f11943w;

    public Vec4f() {
    }

    public Vec4f(Vec4f v2) {
        this.f11940x = v2.f11940x;
        this.f11941y = v2.f11941y;
        this.f11942z = v2.f11942z;
        this.f11943w = v2.f11943w;
    }

    public Vec4f(float x2, float y2, float z2, float w2) {
        this.f11940x = x2;
        this.f11941y = y2;
        this.f11942z = z2;
        this.f11943w = w2;
    }

    public void set(Vec4f v2) {
        this.f11940x = v2.f11940x;
        this.f11941y = v2.f11941y;
        this.f11942z = v2.f11942z;
        this.f11943w = v2.f11943w;
    }

    public String toString() {
        return "(" + this.f11940x + ", " + this.f11941y + ", " + this.f11942z + ", " + this.f11943w + ")";
    }
}
