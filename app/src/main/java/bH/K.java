package bH;

import java.util.Arrays;

/* loaded from: TunerStudioMS.jar:bH/K.class */
public class K {

    /* renamed from: a, reason: collision with root package name */
    private float[] f7009a;

    /* renamed from: b, reason: collision with root package name */
    private int f7010b;

    public K() {
        this.f7010b = 0;
        this.f7009a = new float[0];
    }

    public K(int i2) {
        this.f7010b = 0;
        this.f7009a = new float[i2];
    }

    private void b(int i2) {
        if (i2 - this.f7009a.length > 0) {
            this.f7009a = Arrays.copyOf(this.f7009a, this.f7009a.length + 10000);
        }
    }

    public boolean a(float f2) {
        b(this.f7010b + 1);
        float[] fArr = this.f7009a;
        int i2 = this.f7010b;
        this.f7010b = i2 + 1;
        fArr[i2] = f2;
        return true;
    }

    public boolean a(int i2, float f2) {
        b(this.f7010b + 1);
        System.arraycopy(this.f7009a, i2, this.f7009a, i2 + 1, this.f7010b - i2);
        this.f7009a[i2] = f2;
        this.f7010b++;
        return true;
    }

    public float a(int i2) {
        return this.f7009a[i2];
    }

    public void b(int i2, float f2) {
        this.f7009a[i2] = f2;
    }

    public int a() {
        return this.f7010b;
    }

    public void a(int i2, int i3) {
        System.arraycopy(this.f7009a, i3, this.f7009a, i2, this.f7010b - i3);
        int i4 = this.f7010b - (i3 - i2);
        for (int i5 = i4; i5 < this.f7010b; i5++) {
            this.f7009a[i5] = Float.NaN;
        }
        this.f7010b = i4;
    }

    public void b() {
        this.f7009a = new float[0];
        this.f7010b = 0;
    }
}
