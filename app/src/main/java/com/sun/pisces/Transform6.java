package com.sun.pisces;

/* loaded from: jfxrt.jar:com/sun/pisces/Transform6.class */
public final class Transform6 {
    public int m00;
    public int m01;
    public int m10;
    public int m11;
    public int m02;
    public int m12;

    private native void initialize();

    public Transform6() {
        this(65536, 0, 0, 65536, 0, 0);
    }

    public Transform6(int m00, int m01, int m10, int m11, int m02, int m12) {
        initialize();
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
    }

    public Transform6(Transform6 t2) {
        this(t2.m00, t2.m01, t2.m10, t2.m11, t2.m02, t2.m12);
    }

    public void postMultiply(Transform6 t2) {
        long _m00 = ((this.m00 * t2.m00) + (this.m01 * t2.m10)) >> 16;
        long _m01 = ((this.m00 * t2.m01) + (this.m01 * t2.m11)) >> 16;
        long _m10 = ((this.m10 * t2.m00) + (this.m11 * t2.m10)) >> 16;
        long _m11 = ((this.m10 * t2.m01) + (this.m11 * t2.m11)) >> 16;
        long _m02 = (((this.m02 << 16) + (this.m00 * t2.m02)) + (this.m01 * t2.m12)) >> 16;
        long _m12 = (((this.m12 << 16) + (this.m10 * t2.m02)) + (this.m11 * t2.m12)) >> 16;
        this.m00 = (int) _m00;
        this.m01 = (int) _m01;
        this.m02 = (int) _m02;
        this.m10 = (int) _m10;
        this.m11 = (int) _m11;
        this.m12 = (int) _m12;
    }

    public Transform6 inverse() {
        float fm00 = this.m00 / 65536.0f;
        float fm01 = this.m01 / 65536.0f;
        float fm02 = this.m02 / 65536.0f;
        float fm10 = this.m10 / 65536.0f;
        float fm11 = this.m11 / 65536.0f;
        float fm12 = this.m12 / 65536.0f;
        float fdet = (fm00 * fm11) - (fm01 * fm10);
        float fa00 = fm11 / fdet;
        float fa01 = (-fm01) / fdet;
        float fa10 = (-fm10) / fdet;
        float fa11 = fm00 / fdet;
        float fa02 = ((fm01 * fm12) - (fm02 * fm11)) / fdet;
        float fa12 = ((fm02 * fm10) - (fm00 * fm12)) / fdet;
        int a00 = (int) (fa00 * 65536.0d);
        int a01 = (int) (fa01 * 65536.0f);
        int a10 = (int) (fa10 * 65536.0f);
        int a11 = (int) (fa11 * 65536.0f);
        int a02 = (int) (fa02 * 65536.0f);
        int a12 = (int) (fa12 * 65536.0f);
        return new Transform6(a00, a01, a10, a11, a02, a12);
    }

    public boolean isIdentity() {
        return this.m00 == 65536 && this.m01 == 0 && this.m10 == 0 && this.m11 == 65536 && this.m02 == 0 && this.m12 == 0;
    }

    public Transform6 setTransform(Transform6 Tx) {
        this.m00 = Tx.m00;
        this.m10 = Tx.m10;
        this.m01 = Tx.m01;
        this.m11 = Tx.m11;
        this.m02 = Tx.m02;
        this.m12 = Tx.m12;
        return this;
    }

    public String toString() {
        return "Transform6[m00=" + (this.m00 / 65536.0d) + ", m01=" + (this.m01 / 65536.0d) + ", m02=" + (this.m02 / 65536.0d) + ", m10=" + (this.m10 / 65536.0d) + ", m11=" + (this.m11 / 65536.0d) + ", m12=" + (this.m12 / 65536.0d) + "]";
    }
}
