package ao;

import W.InterfaceC0190p;

/* renamed from: ao.fh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fh.class */
class C0751fh implements InterfaceC0190p {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0750fg f5805a;

    C0751fh(C0750fg c0750fg) {
        this.f5805a = c0750fg;
    }

    @Override // W.InterfaceC0190p
    public boolean a() {
        return h.i.j();
    }

    @Override // W.InterfaceC0190p
    public String b() {
        return a() ? h.i.f12275v : h.i.a("delimiter", h.i.f12275v);
    }
}
