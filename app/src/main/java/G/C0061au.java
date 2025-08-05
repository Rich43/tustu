package G;

import bH.C0995c;

/* renamed from: G.au, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/au.class */
class C0061au implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0054an f807a;

    C0061au(C0054an c0054an) {
        this.f807a = c0054an;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        C0130m c0130mB = c0132o.b();
        if (c0132o.a() != 1) {
            aB.a().b(this.f807a.f782a.c(), "Communication with controller " + this.f807a.f782a.c() + " lost after write. Changes may have been lost.");
            this.f807a.f795o = true;
            return;
        }
        int iO = c0132o.b().o();
        if (this.f807a.a(iO) || this.f807a.f782a.C().c(iO)) {
            return;
        }
        if (C0995c.b(this.f807a.f782a.C().a(this.f807a.f782a.c(), c0130mB.o()), c0132o.e())) {
            bH.C.c("Validated Page:" + c0132o.b().o());
            return;
        }
        bH.C.b("Page validation failed on " + this.f807a.f782a.c() + " page:" + c0130mB.o() + "  Doing nothing.");
        bH.C.c("Bytes from Controller:\n" + C0995c.a(c0132o.e(), 16));
        bH.C.c("Bytes In offline Data:\n" + C0995c.a(this.f807a.f782a.h().b(c0130mB.o()), 16));
        aB.a().b(this.f807a.f782a.c(), "Page validation failed on " + this.f807a.f782a.c() + " page: " + c0130mB.o() + "\nReverting back to last bytes successfully saved to Controller.");
        try {
            this.f807a.f791h = true;
            this.f807a.f782a.h().a(c0130mB.o(), 0, c0132o.e(), false);
            this.f807a.f791h = false;
            this.f807a.f782a.h().g();
        } catch (V.g e2) {
            bH.C.c("error setting page bytes");
            e2.printStackTrace();
        }
    }
}
