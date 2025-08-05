package com.sun.webkit.graphics;

import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCTransform.class */
public final class WCTransform extends Ref {

    /* renamed from: m, reason: collision with root package name */
    private final double[] f12062m;
    private final boolean is3D;

    public WCTransform(double m11, double m12, double m13, double m14, double m21, double m22, double m23, double m24, double m31, double m32, double m33, double m34, double m41, double m42, double m43, double m44) {
        this.f12062m = new double[16];
        this.f12062m[0] = m11;
        this.f12062m[1] = m21;
        this.f12062m[2] = m31;
        this.f12062m[3] = m41;
        this.f12062m[4] = m12;
        this.f12062m[5] = m22;
        this.f12062m[6] = m32;
        this.f12062m[7] = m42;
        this.f12062m[8] = m13;
        this.f12062m[9] = m23;
        this.f12062m[10] = m33;
        this.f12062m[11] = m43;
        this.f12062m[12] = m14;
        this.f12062m[13] = m24;
        this.f12062m[14] = m34;
        this.f12062m[15] = m44;
        this.is3D = true;
    }

    public WCTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.f12062m = new double[6];
        this.f12062m[0] = m00;
        this.f12062m[1] = m10;
        this.f12062m[2] = m01;
        this.f12062m[3] = m11;
        this.f12062m[4] = m02;
        this.f12062m[5] = m12;
        this.is3D = false;
    }

    public double[] getMatrix() {
        return Arrays.copyOf(this.f12062m, this.f12062m.length);
    }

    public String toString() {
        String val = this.is3D ? "WCTransform:(" + this.f12062m[0] + "," + this.f12062m[1] + "," + this.f12062m[2] + "," + this.f12062m[3] + ")(" + this.f12062m[4] + "," + this.f12062m[5] + "," + this.f12062m[6] + "," + this.f12062m[7] + ")(" + this.f12062m[8] + "," + this.f12062m[9] + "," + this.f12062m[10] + "," + this.f12062m[11] + ")(" + this.f12062m[12] + "," + this.f12062m[13] + "," + this.f12062m[14] + "," + this.f12062m[15] + ")" : "WCTransform:(" + this.f12062m[0] + "," + this.f12062m[1] + "," + this.f12062m[2] + ")(" + this.f12062m[3] + "," + this.f12062m[4] + "," + this.f12062m[5] + ")";
        return val;
    }
}
