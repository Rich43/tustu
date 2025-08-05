package G;

/* renamed from: G.ay, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ay.class */
class C0065ay implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0054an f820a;

    C0065ay(C0054an c0054an) {
        this.f820a = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (c0132o.a() == 1) {
            try {
                this.f820a.f791h = true;
                this.f820a.f782a.h().a(c0132o.b().o(), 0, c0132o.e());
                this.f820a.f791h = false;
            } catch (V.g e2) {
                bH.C.a("Failed on read back for lost data");
            }
        }
    }
}
