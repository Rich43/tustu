package G;

import java.io.IOException;

/* renamed from: G.ar, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ar.class */
class C0058ar implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    int f803a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0054an f804b;

    C0058ar(C0054an c0054an) {
        this.f804b = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) throws IOException {
        if (c0132o.a() != 3) {
            if (this.f804b.b(c0132o)) {
                aB.a().a(this.f804b.f782a.c(), "Updated Quick Runtime Table");
                return;
            } else {
                aB.a().a(this.f804b.f782a.c(), "Write Complete");
                return;
            }
        }
        this.f803a++;
        if (this.f803a < 2 && c0132o.h()) {
            aB.a().a(this.f804b.f782a.c(), "Write Failed, retrying ");
            this.f804b.a(C0130m.a(c0132o.b()));
            return;
        }
        aB.a().a(this.f804b.f782a.c(), "Write Failed, Giving up ");
        aB.a().b(this.f804b.f782a.c(), "Data write Failed for 2 attempts. Some recent changes may be lost.\nLocal data will be refreshed from controller.");
        C0130m c0130mD = C0130m.d(this.f804b.f782a.O(), c0132o.b().o());
        c0130mD.b(new C0065ay(this.f804b));
        this.f804b.a(c0130mD);
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }
}
