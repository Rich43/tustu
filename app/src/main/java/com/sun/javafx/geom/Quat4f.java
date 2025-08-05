package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Quat4f.class */
public class Quat4f {
    static final double EPS2 = 1.0E-30d;

    /* renamed from: x, reason: collision with root package name */
    public float f11909x;

    /* renamed from: y, reason: collision with root package name */
    public float f11910y;

    /* renamed from: z, reason: collision with root package name */
    public float f11911z;

    /* renamed from: w, reason: collision with root package name */
    public float f11912w;

    public Quat4f() {
        this.f11909x = 0.0f;
        this.f11910y = 0.0f;
        this.f11911z = 0.0f;
        this.f11912w = 0.0f;
    }

    public Quat4f(float x2, float y2, float z2, float w2) {
        float mag = (float) (1.0d / Math.sqrt((((x2 * x2) + (y2 * y2)) + (z2 * z2)) + (w2 * w2)));
        this.f11909x = x2 * mag;
        this.f11910y = y2 * mag;
        this.f11911z = z2 * mag;
        this.f11912w = w2 * mag;
    }

    public Quat4f(float[] q2) {
        float mag = (float) (1.0d / Math.sqrt((((q2[0] * q2[0]) + (q2[1] * q2[1])) + (q2[2] * q2[2])) + (q2[3] * q2[3])));
        this.f11909x = q2[0] * mag;
        this.f11910y = q2[1] * mag;
        this.f11911z = q2[2] * mag;
        this.f11912w = q2[3] * mag;
    }

    public Quat4f(Quat4f q1) {
        this.f11909x = q1.f11909x;
        this.f11910y = q1.f11910y;
        this.f11911z = q1.f11911z;
        this.f11912w = q1.f11912w;
    }

    public final void normalize() {
        float norm = (this.f11909x * this.f11909x) + (this.f11910y * this.f11910y) + (this.f11911z * this.f11911z) + (this.f11912w * this.f11912w);
        if (norm > 0.0f) {
            float norm2 = 1.0f / ((float) Math.sqrt(norm));
            this.f11909x *= norm2;
            this.f11910y *= norm2;
            this.f11911z *= norm2;
            this.f11912w *= norm2;
            return;
        }
        this.f11909x = 0.0f;
        this.f11910y = 0.0f;
        this.f11911z = 0.0f;
        this.f11912w = 0.0f;
    }

    public final void set(Matrix3f m1) {
        float ww = 0.25f * (m1.m00 + m1.m11 + m1.m22 + 1.0f);
        if (ww < 0.0f) {
            this.f11912w = 0.0f;
            this.f11909x = 0.0f;
            this.f11910y = 0.0f;
            this.f11911z = 1.0f;
            return;
        }
        if (ww >= EPS2) {
            this.f11912w = (float) Math.sqrt(ww);
            float ww2 = 0.25f / this.f11912w;
            this.f11909x = (m1.m21 - m1.m12) * ww2;
            this.f11910y = (m1.m02 - m1.m20) * ww2;
            this.f11911z = (m1.m10 - m1.m01) * ww2;
            return;
        }
        this.f11912w = 0.0f;
        float ww3 = (-0.5f) * (m1.m11 + m1.m22);
        if (ww3 < 0.0f) {
            this.f11909x = 0.0f;
            this.f11910y = 0.0f;
            this.f11911z = 1.0f;
        } else {
            if (ww3 >= EPS2) {
                this.f11909x = (float) Math.sqrt(ww3);
                float ww4 = 0.5f / this.f11909x;
                this.f11910y = m1.m10 * ww4;
                this.f11911z = m1.m20 * ww4;
                return;
            }
            this.f11909x = 0.0f;
            float ww5 = 0.5f * (1.0f - m1.m22);
            if (ww5 >= EPS2) {
                this.f11910y = (float) Math.sqrt(ww5);
                this.f11911z = m1.m21 / (2.0f * this.f11910y);
            } else {
                this.f11910y = 0.0f;
                this.f11911z = 1.0f;
            }
        }
    }

    public final void set(float[][] m1) {
        float ww = 0.25f * (m1[0][0] + m1[1][1] + m1[2][2] + 1.0f);
        if (ww < 0.0f) {
            this.f11912w = 0.0f;
            this.f11909x = 0.0f;
            this.f11910y = 0.0f;
            this.f11911z = 1.0f;
            return;
        }
        if (ww >= EPS2) {
            this.f11912w = (float) Math.sqrt(ww);
            float ww2 = 0.25f / this.f11912w;
            this.f11909x = (m1[2][1] - m1[1][2]) * ww2;
            this.f11910y = (m1[0][2] - m1[2][0]) * ww2;
            this.f11911z = (m1[1][0] - m1[0][1]) * ww2;
            return;
        }
        this.f11912w = 0.0f;
        float ww3 = (-0.5f) * (m1[1][1] + m1[2][2]);
        if (ww3 < 0.0f) {
            this.f11909x = 0.0f;
            this.f11910y = 0.0f;
            this.f11911z = 1.0f;
        } else {
            if (ww3 >= EPS2) {
                this.f11909x = (float) Math.sqrt(ww3);
                float ww4 = 0.5f / this.f11909x;
                this.f11910y = m1[1][0] * ww4;
                this.f11911z = m1[2][0] * ww4;
                return;
            }
            this.f11909x = 0.0f;
            float ww5 = 0.5f * (1.0f - m1[2][2]);
            if (ww5 >= EPS2) {
                this.f11910y = (float) Math.sqrt(ww5);
                this.f11911z = m1[2][1] / (2.0f * this.f11910y);
            } else {
                this.f11910y = 0.0f;
                this.f11911z = 1.0f;
            }
        }
    }

    public final void scale(float s2) {
        this.f11909x *= s2;
        this.f11910y *= s2;
        this.f11911z *= s2;
        this.f11912w *= s2;
    }

    public String toString() {
        return "Quat4f[" + this.f11909x + ", " + this.f11910y + ", " + this.f11911z + ", " + this.f11912w + "]";
    }
}
