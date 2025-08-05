package ao;

import bH.InterfaceC0993a;

/* loaded from: TunerStudioMS.jar:ao/eU.class */
class eU implements InterfaceC0993a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5599a;

    eU(C0737eu c0737eu) {
        this.f5599a = c0737eu;
    }

    @Override // bH.InterfaceC0993a
    public boolean a(String str) {
        return (str == null || !str.toLowerCase().contains(h.g.f12249h.toLowerCase()) || str.toLowerCase().contains("difference") || str.toLowerCase().contains("deviation")) ? false : true;
    }
}
