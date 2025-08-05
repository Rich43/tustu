package aP;

import G.C0113cs;
import c.InterfaceC1386e;

/* renamed from: aP.fr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/fr.class */
class C0356fr implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3388a;

    C0356fr(C0308dx c0308dx) {
        this.f3388a = c0308dx;
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        return C0113cs.a().g("dataLogTime") > 0.0d && C0113cs.a().g("dataLoggingActive") == 0.0d;
    }
}
