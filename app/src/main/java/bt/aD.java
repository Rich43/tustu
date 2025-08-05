package bt;

import bH.InterfaceC0999g;

/* loaded from: TunerStudioMS.jar:bt/aD.class */
class aD implements InterfaceC0999g {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ double[] f8744a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8745b;

    aD(C1303al c1303al, double[] dArr) {
        this.f8745b = c1303al;
        this.f8744a = dArr;
    }

    @Override // bH.InterfaceC0999g
    public boolean a(int i2) {
        return ((double) this.f8745b.f8861p.A().c(i2 < this.f8745b.f8861p.A().i() - 3 ? i2 + 2 : i2)) >= this.f8744a[0] && ((double) this.f8745b.f8861p.A().c(i2 > 1 ? i2 - 2 : 0)) <= this.f8744a[this.f8744a.length - 1] && this.f8745b.f8884M.a(i2);
    }
}
