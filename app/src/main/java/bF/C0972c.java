package bF;

import G.C0094c;
import G.cZ;
import G.dh;
import c.InterfaceC1386e;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bF.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/c.class */
public class C0972c implements C {

    /* renamed from: a, reason: collision with root package name */
    double[] f6839a;

    /* renamed from: b, reason: collision with root package name */
    double[] f6840b;

    /* renamed from: c, reason: collision with root package name */
    private cZ f6838c = new C0094c("");

    /* renamed from: d, reason: collision with root package name */
    private dh f6841d = new G.B(0.0d);

    /* renamed from: e, reason: collision with root package name */
    private dh f6842e = new G.B(Double.MIN_VALUE);

    /* renamed from: f, reason: collision with root package name */
    private dh f6843f = new G.B(Double.MAX_VALUE);

    /* renamed from: g, reason: collision with root package name */
    private dh f6844g = new G.B(1.0d);

    /* renamed from: h, reason: collision with root package name */
    private InterfaceC1386e f6845h = null;

    public C0972c(int i2) {
        this.f6839a = new double[8];
        this.f6840b = new double[8];
        this.f6839a = new double[i2];
        this.f6840b = new double[i2];
    }

    @Override // bF.C
    public int a() {
        return this.f6839a.length;
    }

    @Override // bF.C
    public String b() {
        try {
            return this.f6838c.a();
        } catch (Exception e2) {
            return "";
        }
    }

    @Override // bF.C
    public Double a(int i2) {
        try {
            return Double.valueOf(this.f6839a[i2]);
        } catch (IndexOutOfBoundsException e2) {
            return Double.valueOf(Double.NaN);
        }
    }

    @Override // bF.C
    public void a(int i2, Double d2) {
        this.f6839a[i2] = d2.doubleValue();
    }

    @Override // bF.C
    public int c() {
        return (int) this.f6841d.a();
    }

    @Override // bF.C
    public double d() {
        double d2 = Double.MAX_VALUE;
        for (int i2 = 0; i2 < this.f6839a.length; i2++) {
            try {
                if (this.f6839a[i2] < d2) {
                    d2 = this.f6839a[i2];
                }
            } catch (Exception e2) {
                Logger.getLogger(C0972c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return 0.0d;
            }
        }
        return d2;
    }

    @Override // bF.C
    public double e() {
        double d2 = Double.MIN_VALUE;
        for (int i2 = 0; i2 < this.f6839a.length; i2++) {
            try {
                if (this.f6839a[i2] > d2) {
                    d2 = this.f6839a[i2];
                }
            } catch (Exception e2) {
                Logger.getLogger(C0972c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return 0.0d;
            }
        }
        return d2;
    }

    @Override // bF.C
    public boolean b(int i2) {
        return this.f6840b[i2] == this.f6839a[i2];
    }

    public void f() {
        for (int i2 = 0; i2 < this.f6839a.length; i2++) {
            this.f6840b[i2] = this.f6839a[i2];
        }
    }

    @Override // bF.C
    public double c(int i2) {
        return this.f6840b[i2];
    }

    public void a(dh dhVar) {
        this.f6841d = dhVar;
    }

    public void b(dh dhVar) {
        this.f6842e = dhVar;
    }

    public void c(dh dhVar) {
        this.f6843f = dhVar;
    }

    public void a(cZ cZVar) {
        this.f6838c = cZVar;
    }

    @Override // bF.C
    public double d(int i2) {
        return this.f6844g.a(i2);
    }

    public void d(dh dhVar) {
        this.f6844g = dhVar;
    }

    @Override // bF.C
    public boolean g() {
        if (this.f6845h != null) {
            return this.f6845h.a();
        }
        return true;
    }

    public void a(InterfaceC1386e interfaceC1386e) {
        this.f6845h = interfaceC1386e;
    }

    @Override // bF.C
    public void b(int i2, Double d2) {
        this.f6840b[i2] = d2.doubleValue();
    }
}
