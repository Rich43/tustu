package aP;

import G.C0126i;
import bH.C1007o;
import c.InterfaceC1386e;

/* loaded from: TunerStudioMS.jar:aP/gN.class */
class gN implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    String f3424a;

    /* renamed from: b, reason: collision with root package name */
    G.R f3425b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0308dx f3426c;

    gN(C0308dx c0308dx, String str, G.R r2) {
        this.f3426c = c0308dx;
        this.f3424a = null;
        this.f3425b = null;
        this.f3424a = str;
        this.f3425b = r2;
        if (str == null || str.isEmpty()) {
            return;
        }
        C0126i.a(r2.c(), str, c0308dx.f3276n);
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        try {
            return C1007o.a(this.f3424a, this.f3425b);
        } catch (V.g e2) {
            e2.printStackTrace();
            return true;
        }
    }
}
