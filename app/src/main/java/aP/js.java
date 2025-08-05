package aP;

import ay.C0926c;
import ay.InterfaceC0939p;

/* loaded from: TunerStudioMS.jar:aP/js.class */
class js implements InterfaceC0939p {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ jp f3806a;

    js(jp jpVar) {
        this.f3806a = jpVar;
    }

    @Override // ay.InterfaceC0939p
    public void c(C0926c c0926c) {
        if (c0926c.a().equals("Dash Configuration Server")) {
            this.f3806a.b(c0926c);
        }
    }

    @Override // ay.InterfaceC0939p
    public void d(C0926c c0926c) {
        if (c0926c.a().equals("Dash Configuration Server")) {
            this.f3806a.a(c0926c);
        }
    }
}
