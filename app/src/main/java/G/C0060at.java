package G;

/* renamed from: G.at, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/at.class */
class C0060at implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0054an f806a;

    C0060at(C0054an c0054an) {
        this.f806a = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (this.f806a.b(c0132o)) {
            aB.a().a(this.f806a.f782a.c(), "Updated Quick Runtime Table");
        } else {
            aB.a().a(this.f806a.f782a.c(), "Write Complete");
        }
        aB.a().e();
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }
}
