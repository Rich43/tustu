package a;

import java.io.Serializable;

/* renamed from: a.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:a/a.class */
public class C0201a implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private double[][] f2228a;

    /* renamed from: b, reason: collision with root package name */
    private int f2229b;

    /* renamed from: c, reason: collision with root package name */
    private int f2230c;

    /* renamed from: d, reason: collision with root package name */
    private int f2231d;

    /* renamed from: e, reason: collision with root package name */
    private int[] f2232e;

    public C0201a(C0202b c0202b) {
        this.f2228a = c0202b.c();
        this.f2229b = c0202b.d();
        this.f2230c = c0202b.e();
        this.f2232e = new int[this.f2229b];
        for (int i2 = 0; i2 < this.f2229b; i2++) {
            this.f2232e[i2] = i2;
        }
        this.f2231d = 1;
        double[] dArr = new double[this.f2229b];
        int i3 = 0;
        while (i3 < this.f2230c) {
            for (int i4 = 0; i4 < this.f2229b; i4++) {
                dArr[i4] = this.f2228a[i4][i3];
            }
            for (int i5 = 0; i5 < this.f2229b; i5++) {
                double[] dArr2 = this.f2228a[i5];
                int iMin = Math.min(i5, i3);
                double d2 = 0.0d;
                for (int i6 = 0; i6 < iMin; i6++) {
                    d2 += dArr2[i6] * dArr[i6];
                }
                int i7 = i5;
                double d3 = dArr[i7] - d2;
                dArr[i7] = d3;
                dArr2[i3] = d3;
            }
            int i8 = i3;
            for (int i9 = i3 + 1; i9 < this.f2229b; i9++) {
                if (Math.abs(dArr[i9]) > Math.abs(dArr[i8])) {
                    i8 = i9;
                }
            }
            if (i8 != i3) {
                for (int i10 = 0; i10 < this.f2230c; i10++) {
                    double d4 = this.f2228a[i8][i10];
                    this.f2228a[i8][i10] = this.f2228a[i3][i10];
                    this.f2228a[i3][i10] = d4;
                }
                int i11 = this.f2232e[i8];
                this.f2232e[i8] = this.f2232e[i3];
                this.f2232e[i3] = i11;
                this.f2231d = -this.f2231d;
            }
            if ((i3 < this.f2229b) & (this.f2228a[i3][i3] != 0.0d)) {
                for (int i12 = i3 + 1; i12 < this.f2229b; i12++) {
                    double[] dArr3 = this.f2228a[i12];
                    int i13 = i3;
                    dArr3[i13] = dArr3[i13] / this.f2228a[i3][i3];
                }
            }
            i3++;
        }
    }

    public boolean a() {
        for (int i2 = 0; i2 < this.f2230c; i2++) {
            if (this.f2228a[i2][i2] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public C0202b a(C0202b c0202b) {
        if (c0202b.d() != this.f2229b) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!a()) {
            throw new RuntimeException("Matrix is singular.");
        }
        int iE = c0202b.e();
        C0202b c0202bA = c0202b.a(this.f2232e, 0, iE - 1);
        double[][] dArrB = c0202bA.b();
        for (int i2 = 0; i2 < this.f2230c; i2++) {
            for (int i3 = i2 + 1; i3 < this.f2230c; i3++) {
                for (int i4 = 0; i4 < iE; i4++) {
                    double[] dArr = dArrB[i3];
                    int i5 = i4;
                    dArr[i5] = dArr[i5] - (dArrB[i2][i4] * this.f2228a[i3][i2]);
                }
            }
        }
        for (int i6 = this.f2230c - 1; i6 >= 0; i6--) {
            for (int i7 = 0; i7 < iE; i7++) {
                double[] dArr2 = dArrB[i6];
                int i8 = i7;
                dArr2[i8] = dArr2[i8] / this.f2228a[i6][i6];
            }
            for (int i9 = 0; i9 < i6; i9++) {
                for (int i10 = 0; i10 < iE; i10++) {
                    double[] dArr3 = dArrB[i9];
                    int i11 = i10;
                    dArr3[i11] = dArr3[i11] - (dArrB[i6][i10] * this.f2228a[i9][i6]);
                }
            }
        }
        return c0202bA;
    }
}
