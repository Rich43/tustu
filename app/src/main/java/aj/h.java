package aj;

import G.C0132o;
import G.InterfaceC0131n;
import bH.C;

/* loaded from: TunerStudioMS.jar:aj/h.class */
class h implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f4561a;

    h(d dVar) {
        this.f4561a = dVar;
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws NumberFormatException {
        if (c0132o.a() == 1) {
            this.f4561a.a(c0132o.e());
        } else {
            C.b("Failed to get Trigger Log Data, message:" + c0132o.c());
        }
    }
}
