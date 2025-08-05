package aP;

import s.C1818g;

/* renamed from: aP.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/o.class */
class C0459o implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ G.cZ f3830a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ com.efiAnalytics.ui.dF f3831b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0338f f3832c;

    C0459o(C0338f c0338f, G.cZ cZVar, com.efiAnalytics.ui.dF dFVar) {
        this.f3832c = c0338f;
        this.f3830a = cZVar;
        this.f3831b = dFVar;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        String string;
        try {
            string = C1818g.b(this.f3830a.a());
        } catch (V.g e2) {
            string = this.f3830a.toString();
        }
        this.f3831b.setTitle(string);
    }
}
