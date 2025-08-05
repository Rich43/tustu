package a;

import java.io.Serializable;

/* renamed from: a.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:a/b.class */
public class C0202b implements Serializable, Cloneable {

    /* renamed from: a, reason: collision with root package name */
    private double[][] f2233a;

    /* renamed from: b, reason: collision with root package name */
    private int f2234b;

    /* renamed from: c, reason: collision with root package name */
    private int f2235c;

    public C0202b(int i2, int i3) {
        this.f2234b = i2;
        this.f2235c = i3;
        this.f2233a = new double[i2][i3];
    }

    public C0202b(double[][] dArr) {
        this.f2234b = dArr.length;
        this.f2235c = dArr[0].length;
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            if (dArr[i2].length != this.f2235c) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.f2233a = dArr;
    }

    public C0202b(double[][] dArr, int i2, int i3) {
        this.f2233a = dArr;
        this.f2234b = i2;
        this.f2235c = i3;
    }

    public C0202b a() {
        C0202b c0202b = new C0202b(this.f2234b, this.f2235c);
        double[][] dArrB = c0202b.b();
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArrB[i2][i3] = this.f2233a[i2][i3];
            }
        }
        return c0202b;
    }

    public Object clone() {
        return a();
    }

    public double[][] b() {
        return this.f2233a;
    }

    public double[][] c() {
        double[][] dArr = new double[this.f2234b][this.f2235c];
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArr[i2][i3] = this.f2233a[i2][i3];
            }
        }
        return dArr;
    }

    public int d() {
        return this.f2234b;
    }

    public int e() {
        return this.f2235c;
    }

    public double a(int i2, int i3) {
        return this.f2233a[i2][i3];
    }

    public C0202b a(int i2, int i3, int i4, int i5) {
        C0202b c0202b = new C0202b((i3 - i2) + 1, (i5 - i4) + 1);
        double[][] dArrB = c0202b.b();
        for (int i6 = i2; i6 <= i3; i6++) {
            for (int i7 = i4; i7 <= i5; i7++) {
                try {
                    dArrB[i6 - i2][i7 - i4] = this.f2233a[i6][i7];
                } catch (ArrayIndexOutOfBoundsException e2) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return c0202b;
    }

    public C0202b a(int[] iArr, int i2, int i3) {
        C0202b c0202b = new C0202b(iArr.length, (i3 - i2) + 1);
        double[][] dArrB = c0202b.b();
        for (int i4 = 0; i4 < iArr.length; i4++) {
            try {
                for (int i5 = i2; i5 <= i3; i5++) {
                    dArrB[i4][i5 - i2] = this.f2233a[iArr[i4]][i5];
                }
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return c0202b;
    }

    public C0202b f() {
        C0202b c0202b = new C0202b(this.f2235c, this.f2234b);
        double[][] dArrB = c0202b.b();
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArrB[i3][i2] = this.f2233a[i2][i3];
            }
        }
        return c0202b;
    }

    public C0202b a(C0202b c0202b) {
        e(c0202b);
        C0202b c0202b2 = new C0202b(this.f2234b, this.f2235c);
        double[][] dArrB = c0202b2.b();
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArrB[i2][i3] = this.f2233a[i2][i3] + c0202b.f2233a[i2][i3];
            }
        }
        return c0202b2;
    }

    public C0202b b(C0202b c0202b) {
        e(c0202b);
        C0202b c0202b2 = new C0202b(this.f2234b, this.f2235c);
        double[][] dArrB = c0202b2.b();
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArrB[i2][i3] = this.f2233a[i2][i3] - c0202b.f2233a[i2][i3];
            }
        }
        return c0202b2;
    }

    public C0202b a(double d2) {
        C0202b c0202b = new C0202b(this.f2234b, this.f2235c);
        double[][] dArrB = c0202b.b();
        for (int i2 = 0; i2 < this.f2234b; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArrB[i2][i3] = d2 * this.f2233a[i2][i3];
            }
        }
        return c0202b;
    }

    public C0202b c(C0202b c0202b) {
        if (c0202b.f2234b != this.f2235c) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        C0202b c0202b2 = new C0202b(this.f2234b, c0202b.f2235c);
        double[][] dArrB = c0202b2.b();
        double[] dArr = new double[this.f2235c];
        for (int i2 = 0; i2 < c0202b.f2235c; i2++) {
            for (int i3 = 0; i3 < this.f2235c; i3++) {
                dArr[i3] = c0202b.f2233a[i3][i2];
            }
            for (int i4 = 0; i4 < this.f2234b; i4++) {
                double[] dArr2 = this.f2233a[i4];
                double d2 = 0.0d;
                for (int i5 = 0; i5 < this.f2235c; i5++) {
                    d2 += dArr2[i5] * dArr[i5];
                }
                dArrB[i4][i2] = d2;
            }
        }
        return c0202b2;
    }

    public C0202b d(C0202b c0202b) {
        return this.f2234b == this.f2235c ? new C0201a(this).a(c0202b) : new C0203c(this).a(c0202b);
    }

    public C0202b g() {
        return d(b(this.f2234b, this.f2234b));
    }

    public static C0202b b(int i2, int i3) {
        C0202b c0202b = new C0202b(i2, i3);
        double[][] dArrB = c0202b.b();
        int i4 = 0;
        while (i4 < i2) {
            int i5 = 0;
            while (i5 < i3) {
                dArrB[i4][i5] = i4 == i5 ? 1.0d : 0.0d;
                i5++;
            }
            i4++;
        }
        return c0202b;
    }

    private void e(C0202b c0202b) {
        if (c0202b.f2234b != this.f2234b || c0202b.f2235c != this.f2235c) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }
}
