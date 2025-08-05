package a;

import b.C0954a;
import java.io.Serializable;

/* renamed from: a.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:a/c.class */
public class C0203c implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private double[][] f2236a;

    /* renamed from: b, reason: collision with root package name */
    private int f2237b;

    /* renamed from: c, reason: collision with root package name */
    private int f2238c;

    /* renamed from: d, reason: collision with root package name */
    private double[] f2239d;

    public C0203c(C0202b c0202b) {
        this.f2236a = c0202b.c();
        this.f2237b = c0202b.d();
        this.f2238c = c0202b.e();
        this.f2239d = new double[this.f2238c];
        for (int i2 = 0; i2 < this.f2238c; i2++) {
            double dA = 0.0d;
            for (int i3 = i2; i3 < this.f2237b; i3++) {
                dA = C0954a.a(dA, this.f2236a[i3][i2]);
            }
            if (dA != 0.0d) {
                dA = this.f2236a[i2][i2] < 0.0d ? -dA : dA;
                for (int i4 = i2; i4 < this.f2237b; i4++) {
                    double[] dArr = this.f2236a[i4];
                    int i5 = i2;
                    dArr[i5] = dArr[i5] / dA;
                }
                double[] dArr2 = this.f2236a[i2];
                int i6 = i2;
                dArr2[i6] = dArr2[i6] + 1.0d;
                for (int i7 = i2 + 1; i7 < this.f2238c; i7++) {
                    double d2 = 0.0d;
                    for (int i8 = i2; i8 < this.f2237b; i8++) {
                        d2 += this.f2236a[i8][i2] * this.f2236a[i8][i7];
                    }
                    double d3 = (-d2) / this.f2236a[i2][i2];
                    for (int i9 = i2; i9 < this.f2237b; i9++) {
                        double[] dArr3 = this.f2236a[i9];
                        int i10 = i7;
                        dArr3[i10] = dArr3[i10] + (d3 * this.f2236a[i9][i2]);
                    }
                }
            }
            this.f2239d[i2] = -dA;
        }
    }

    public boolean a() {
        for (int i2 = 0; i2 < this.f2238c; i2++) {
            if (this.f2239d[i2] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public C0202b a(C0202b c0202b) {
        if (c0202b.d() != this.f2237b) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!a()) {
            throw new RuntimeException("Matrix is rank deficient.");
        }
        int iE = c0202b.e();
        double[][] dArrC = c0202b.c();
        for (int i2 = 0; i2 < this.f2238c; i2++) {
            for (int i3 = 0; i3 < iE; i3++) {
                double d2 = 0.0d;
                for (int i4 = i2; i4 < this.f2237b; i4++) {
                    d2 += this.f2236a[i4][i2] * dArrC[i4][i3];
                }
                double d3 = (-d2) / this.f2236a[i2][i2];
                for (int i5 = i2; i5 < this.f2237b; i5++) {
                    double[] dArr = dArrC[i5];
                    int i6 = i3;
                    dArr[i6] = dArr[i6] + (d3 * this.f2236a[i5][i2]);
                }
            }
        }
        for (int i7 = this.f2238c - 1; i7 >= 0; i7--) {
            for (int i8 = 0; i8 < iE; i8++) {
                double[] dArr2 = dArrC[i7];
                int i9 = i8;
                dArr2[i9] = dArr2[i9] / this.f2239d[i7];
            }
            for (int i10 = 0; i10 < i7; i10++) {
                for (int i11 = 0; i11 < iE; i11++) {
                    double[] dArr3 = dArrC[i10];
                    int i12 = i11;
                    dArr3[i12] = dArr3[i12] - (dArrC[i7][i11] * this.f2236a[i10][i7]);
                }
            }
        }
        return new C0202b(dArrC, this.f2238c, iE).a(0, this.f2238c - 1, 0, iE - 1);
    }
}
