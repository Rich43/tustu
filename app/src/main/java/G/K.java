package G;

/* loaded from: TunerStudioMS.jar:G/K.class */
class K implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f456a;

    K(J j2) {
        this.f456a = j2;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        synchronized (this.f456a.f405p) {
            if (c0132o.a() == 1) {
                if (this.f456a.x()) {
                    aB.a().a(c0132o.f(), "Burned Page " + c0132o.b().o());
                } else {
                    this.f456a.f427L = -2;
                    aB.a().a(c0132o.f(), "Burned All Data");
                    this.f456a.a(c0132o.f(), true);
                }
            } else if (c0132o.a() != 2) {
                aB.a().b(c0132o.f(), "Burn Page " + c0132o.b().o() + " failed.");
            }
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }
}
