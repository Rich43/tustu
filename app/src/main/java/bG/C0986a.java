package bG;

import java.util.ArrayList;

/* renamed from: bG.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/a.class */
public class C0986a implements l {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f6908a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    double f6909b = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    private double f6910e = Double.NaN;

    /* renamed from: c, reason: collision with root package name */
    int f6911c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f6912d = 0;

    public C0986a() {
    }

    public C0986a(int i2, int i3) {
        a(i2, i3);
    }

    @Override // bG.l
    public ArrayList a() {
        return this.f6908a;
    }

    public void a(int i2, int i3) {
        double d2;
        double d3 = 360.0d / i2;
        if (Double.isNaN(this.f6910e)) {
            d2 = d3 < 6.0d ? d3 / 2.5d : 3.0d;
        } else {
            d2 = this.f6910e;
        }
        this.f6908a.clear();
        this.f6909b = Double.NaN;
        double d4 = 0.0d;
        while (true) {
            double d5 = d4;
            if (d5 >= i2 - i3) {
                this.f6911c = i2;
                this.f6912d = i3;
                return;
            }
            k kVar = new k();
            kVar.a((d3 * d5) + 0.0d);
            if (Double.isNaN(this.f6909b)) {
                this.f6909b = kVar.a();
            }
            kVar.b(d2);
            this.f6908a.add(kVar);
            d4 = d5 + 1.0d;
        }
    }

    @Override // bG.l
    public double b() {
        return this.f6909b;
    }

    public void a(double d2) {
        this.f6910e = d2;
        if (this.f6911c > 0) {
            a(this.f6911c, this.f6912d);
        }
    }
}
