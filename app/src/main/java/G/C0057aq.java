package G;

import java.io.IOException;

/* renamed from: G.aq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/aq.class */
class C0057aq implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    int f801a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0054an f802b;

    C0057aq(C0054an c0054an) {
        this.f802b = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws IOException {
        if (c0132o.a() == 3) {
            this.f801a++;
            if (this.f801a < 2) {
                aB.a().a(this.f802b.f782a.c(), "Write Failed, retrying ");
                C0130m c0130mA = C0130m.a(c0132o.b());
                c0130mA.b(this.f802b.f782a.p().a(c0130mA.o(), c0130mA.q(), c0130mA.r()));
                this.f802b.a(c0130mA);
            } else {
                this.f802b.f782a.C().a(true);
                aB.a().a(this.f802b.f782a.c(), "Write Failed, Giving up ");
                aB.a().b(this.f802b.f782a.c(), "Data write Failed for 2 attempts. Some recent changes may be lost.\nLocal data will be refreshed from controller.");
                this.f802b.f782a.C().a(true);
                C0130m c0130mD = C0130m.d(this.f802b.f782a.O(), c0132o.b().o());
                c0130mD.b(new C0065ay(this.f802b));
                this.f802b.a(c0130mD);
            }
        } else if (this.f802b.f782a.O().x(c0132o.b().o())) {
            aB.a().a(this.f802b.f782a.c(), "Wrote " + c0132o.b().p().length + " bytes");
        }
        aB.a().e();
    }

    @Override // G.InterfaceC0131n
    public void e() {
        aB.a().d();
    }
}
