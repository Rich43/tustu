package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Matrix3f.class */
public class Matrix3f {
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;

    public Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    public Matrix3f(float[] v2) {
        this.m00 = v2[0];
        this.m01 = v2[1];
        this.m02 = v2[2];
        this.m10 = v2[3];
        this.m11 = v2[4];
        this.m12 = v2[5];
        this.m20 = v2[6];
        this.m21 = v2[7];
        this.m22 = v2[8];
    }

    public Matrix3f(Vec3f[] v2) {
        this.m00 = v2[0].f11933x;
        this.m01 = v2[0].f11934y;
        this.m02 = v2[0].f11935z;
        this.m10 = v2[1].f11933x;
        this.m11 = v2[1].f11933x;
        this.m12 = v2[1].f11933x;
        this.m20 = v2[2].f11933x;
        this.m21 = v2[2].f11933x;
        this.m22 = v2[2].f11933x;
    }

    public Matrix3f(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public Matrix3f() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
    }

    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
    }

    public final void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
    }

    public final void setRow(int row, float[] v2) {
        switch (row) {
            case 0:
                this.m00 = v2[0];
                this.m01 = v2[1];
                this.m02 = v2[2];
                return;
            case 1:
                this.m10 = v2[0];
                this.m11 = v2[1];
                this.m12 = v2[2];
                return;
            case 2:
                this.m20 = v2[0];
                this.m21 = v2[1];
                this.m22 = v2[2];
                return;
            default:
                throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    public final void setRow(int row, Vec3f v2) {
        switch (row) {
            case 0:
                this.m00 = v2.f11933x;
                this.m01 = v2.f11934y;
                this.m02 = v2.f11935z;
                return;
            case 1:
                this.m10 = v2.f11933x;
                this.m11 = v2.f11934y;
                this.m12 = v2.f11935z;
                return;
            case 2:
                this.m20 = v2.f11933x;
                this.m21 = v2.f11934y;
                this.m22 = v2.f11935z;
                return;
            default:
                throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    public final void getRow(int row, Vec3f v2) {
        if (row == 0) {
            v2.f11933x = this.m00;
            v2.f11934y = this.m01;
            v2.f11935z = this.m02;
        } else if (row == 1) {
            v2.f11933x = this.m10;
            v2.f11934y = this.m11;
            v2.f11935z = this.m12;
        } else {
            if (row == 2) {
                v2.f11933x = this.m20;
                v2.f11934y = this.m21;
                v2.f11935z = this.m22;
                return;
            }
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }

    public final void getRow(int row, float[] v2) {
        if (row == 0) {
            v2[0] = this.m00;
            v2[1] = this.m01;
            v2[2] = this.m02;
        } else if (row == 1) {
            v2[0] = this.m10;
            v2[1] = this.m11;
            v2[2] = this.m12;
        } else {
            if (row == 2) {
                v2[0] = this.m20;
                v2[1] = this.m21;
                v2[2] = this.m22;
                return;
            }
            throw new ArrayIndexOutOfBoundsException("Matrix3f");
        }
    }
}
