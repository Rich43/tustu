package ao;

import bH.InterfaceC0993a;

/* loaded from: TunerStudioMS.jar:ao/eV.class */
class eV implements InterfaceC0993a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5600a;

    eV(C0737eu c0737eu) {
        this.f5600a = c0737eu;
    }

    @Override // bH.InterfaceC0993a
    public boolean a(String str) {
        return (str == null || !str.toLowerCase().contains(h.g.f12250i.toLowerCase()) || str.toLowerCase().contains("difference") || str.toLowerCase().contains("deviation")) ? false : true;
    }
}
