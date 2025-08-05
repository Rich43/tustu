package aP;

import ay.C0924a;
import ay.C0926c;
import ay.InterfaceC0939p;

/* loaded from: TunerStudioMS.jar:aP/cX.class */
class cX implements InterfaceC0939p {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3133a;

    cX(bZ bZVar) {
        this.f3133a = bZVar;
    }

    @Override // ay.InterfaceC0939p
    public void c(C0926c c0926c) {
        if (C0924a.c().a("Log File Server")) {
            return;
        }
        this.f3133a.f3047v.setEnabled(false);
        this.f3133a.f3047v.validate();
    }

    @Override // ay.InterfaceC0939p
    public void d(C0926c c0926c) {
        if (c0926c.a().equals("Log File Server")) {
            this.f3133a.f3047v.setEnabled(true);
            this.f3133a.f3047v.setVisible(true);
            this.f3133a.f3047v.validate();
        }
    }
}
