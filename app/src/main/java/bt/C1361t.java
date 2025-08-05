package bt;

import com.efiAnalytics.ui.InterfaceC1614cy;
import java.awt.Color;

/* renamed from: bt.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/t.class */
class C1361t extends com.efiAnalytics.apps.ts.dashboard.aH implements InterfaceC1614cy {

    /* renamed from: b, reason: collision with root package name */
    private String f9120b = null;

    /* renamed from: c, reason: collision with root package name */
    private double f9121c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    private double f9122d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    private Color f9123e = Color.RED;

    /* renamed from: f, reason: collision with root package name */
    private String f9124f = null;

    /* renamed from: g, reason: collision with root package name */
    private int f9125g = 0;

    /* renamed from: h, reason: collision with root package name */
    private String f9126h = null;

    /* renamed from: i, reason: collision with root package name */
    private boolean f9127i = true;

    /* renamed from: j, reason: collision with root package name */
    private boolean f9128j = false;

    /* renamed from: k, reason: collision with root package name */
    private int f9129k = 0;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1360s f9130a;

    C1361t(C1360s c1360s) {
        this.f9130a = c1360s;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.aH
    public void a(double d2) {
        if (this.f9127i && (Double.isNaN(this.f9122d) || d2 > this.f9122d)) {
            this.f9122d = bH.H.a(d2);
            this.f9130a.validate();
        }
        if (this.f9128j && (Double.isNaN(this.f9121c) || d2 < this.f9121c)) {
            this.f9121c = d2;
            this.f9130a.validate();
        }
        super.a(d2);
        this.f9129k++;
        if (h() && this.f9129k % 10 == 0) {
            double d3 = Double.MAX_VALUE;
            double d4 = -1.0E7d;
            for (int i2 = 0; i2 < size(); i2++) {
                double dA = a(i2);
                if (dA < d3) {
                    d3 = dA;
                }
                if (dA > d4) {
                    d4 = dA;
                }
            }
            this.f9122d = bH.H.a(d4);
            if (d3 <= 0.0d) {
                this.f9121c = -bH.H.a(Math.abs(d3));
            }
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public void c(int i2) {
        super.b(i2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public int b() {
        return this.f9125g;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public double c() {
        return this.f9121c;
    }

    public void b(double d2) {
        this.f9121c = d2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public double d() {
        return this.f9122d;
    }

    public void c(double d2) {
        this.f9122d = d2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public Color e() {
        return this.f9123e;
    }

    public void a(Color color) {
        this.f9123e = color;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public String f() {
        return this.f9124f;
    }

    public void a(String str) {
        this.f9124f = str;
    }

    public void d(int i2) {
        this.f9125g = i2;
    }

    public void b(String str) {
        this.f9120b = str;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1614cy
    public String g() {
        return this.f9126h;
    }

    public void c(String str) {
        this.f9126h = str;
    }

    public boolean h() {
        return this.f9127i;
    }

    public void a(boolean z2) {
        this.f9127i = z2;
    }

    public void b(boolean z2) {
        this.f9128j = z2;
    }
}
