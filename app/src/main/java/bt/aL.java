package bt;

import com.efiAnalytics.ui.C1700r;

/* loaded from: TunerStudioMS.jar:bt/aL.class */
class aL implements G.bU {

    /* renamed from: a, reason: collision with root package name */
    int f8761a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8762b;

    public aL(C1303al c1303al, int i2) {
        this.f8762b = c1303al;
        this.f8761a = -1;
        this.f8761a = i2;
    }

    @Override // G.bU
    public void a(int i2) {
        if (this.f8762b.f8879H) {
            return;
        }
        this.f8762b.a(this.f8761a, i2);
        this.f8762b.a(new C1700r[]{new C1700r(this.f8761a, i2)});
    }
}
