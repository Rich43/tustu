package aS;

import G.C0113cs;
import G.InterfaceC0109co;
import G.bT;

/* loaded from: TunerStudioMS.jar:aS/j.class */
class j implements bT, InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    int f3923a = 65535;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ g f3924b;

    j(g gVar) {
        this.f3924b = gVar;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if ((str.equals(this.f3924b.f3913a) && d2 < this.f3923a) || (str.equals("controllerSettingsLoaded") && d2 != 0.0d)) {
            a(0.0d);
        }
        this.f3923a = (int) d2;
    }

    private void a(double d2) {
        C0113cs.a().a("powerCycleRequired", d2);
        this.f3924b.f3915c = d2;
    }

    @Override // G.bT
    public void a() {
    }

    @Override // G.bT
    public void a(boolean z2) {
        if (z2) {
            a(0.0d);
        }
    }
}
