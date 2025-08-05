package G;

import java.io.IOException;

/* renamed from: G.ap, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ap.class */
class C0056ap implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0054an f800a;

    C0056ap(C0054an c0054an) {
        this.f800a = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws IOException {
        aB.a().e();
        new C0064ax(this.f800a, c0132o).start();
        this.f800a.a(C0130m.a(this.f800a.f782a.O()));
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        aB.a().a("Reading Controller Settings", d2);
    }

    @Override // G.InterfaceC0131n
    public void e() {
        aB.a().d();
        aB.a().b();
    }
}
