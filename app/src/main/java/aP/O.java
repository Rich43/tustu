package aP;

import G.InterfaceC0124g;

/* loaded from: TunerStudioMS.jar:aP/O.class */
class O implements InterfaceC0124g {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ N f2770a;

    O(N n2) {
        this.f2770a = n2;
    }

    @Override // G.InterfaceC0124g
    public void b(String str, int i2) {
    }

    @Override // G.InterfaceC0124g
    public void a(String str, boolean z2) {
        System.out.println("sent msq data to controller in " + ((System.currentTimeMillis() - this.f2770a.f2769h) / 1000) + "s.");
        this.f2770a.a((int) (System.currentTimeMillis() - this.f2770a.f2769h));
        this.f2770a.f2767f = true;
    }

    @Override // G.InterfaceC0124g
    public void a(String str, int i2) {
    }
}
