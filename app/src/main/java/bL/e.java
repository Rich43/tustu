package bL;

import com.efiAnalytics.ui.C1562b;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bL/e.class */
class e {

    /* renamed from: b, reason: collision with root package name */
    private double f7132b;

    /* renamed from: c, reason: collision with root package name */
    private double f7133c;

    /* renamed from: d, reason: collision with root package name */
    private double f7134d;

    /* renamed from: e, reason: collision with root package name */
    private double f7135e;

    /* renamed from: f, reason: collision with root package name */
    private String f7136f;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f7137a;

    public e(a aVar, String str, double d2, double d3, double d4, double d5) {
        this.f7137a = aVar;
        this.f7132b = Double.MIN_VALUE;
        this.f7133c = 0.0d;
        this.f7134d = C1562b.f10868g;
        this.f7135e = Double.MAX_VALUE;
        this.f7136f = null;
        this.f7136f = str;
        this.f7133c = d2;
        this.f7132b = d3;
        this.f7134d = d4;
        this.f7135e = d5;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof e)) {
            return false;
        }
        e eVar = (e) obj;
        return eVar.d() == d() && eVar.b() == b() && eVar.a() == a();
    }

    public String toString() {
        return C1818g.b(c());
    }

    public double a() {
        return this.f7132b;
    }

    public double b() {
        return this.f7133c;
    }

    public String c() {
        return this.f7136f;
    }

    public double d() {
        return this.f7134d;
    }
}
