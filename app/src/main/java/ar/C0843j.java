package ar;

/* renamed from: ar.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/j.class */
class C0843j implements InterfaceC0838e {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0840g f6239a;

    C0843j(C0840g c0840g) {
        this.f6239a = c0840g;
    }

    @Override // ar.InterfaceC0838e
    public boolean a(String str, String str2) {
        return true;
    }

    @Override // ar.InterfaceC0838e
    public void a(C0836c c0836c) {
        int iA = this.f6239a.a(c0836c.b());
        if (iA >= 0) {
            this.f6239a.setSelectedIndex(iA);
        }
    }

    @Override // ar.InterfaceC0838e
    public void b(C0836c c0836c) {
        int iA = this.f6239a.a(c0836c.b());
        if (iA > 0) {
            this.f6239a.setIconAt(iA, this.f6239a.f6233b);
        }
        this.f6239a.setToolTipTextAt(iA, c0836c.a());
    }

    @Override // ar.InterfaceC0838e
    public void c(C0836c c0836c) {
        int iA = this.f6239a.a(c0836c.b());
        if (iA >= 0) {
            this.f6239a.setIconAt(iA, null);
        }
        this.f6239a.setToolTipTextAt(iA, c0836c.a());
    }

    @Override // ar.InterfaceC0838e
    public void a(String str) {
        int tabCount = this.f6239a.f6235d ? this.f6239a.getTabCount() - 1 : this.f6239a.getTabCount() - 2;
        this.f6239a.insertTab(str, null, this.f6239a.c(), C0839f.a().c(str).a(), tabCount);
        this.f6239a.setSelectedIndex(tabCount);
    }

    @Override // ar.InterfaceC0838e
    public void b(String str) {
        int iA = this.f6239a.a(str);
        if (iA > 0) {
            this.f6239a.remove(iA);
        }
    }

    @Override // ar.InterfaceC0838e
    public void c(String str) {
    }
}
