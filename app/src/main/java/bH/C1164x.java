package bh;

import G.InterfaceC0112cr;
import G.R;
import G.aH;

/* renamed from: bh.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/x.class */
class C1164x implements InterfaceC0112cr {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1162v f8143a;

    C1164x(C1162v c1162v) {
        this.f8143a = c1162v;
    }

    @Override // G.InterfaceC0112cr
    public boolean a(R r2, aH aHVar) {
        return r2.R() || !this.f8143a.f8136c.contains(aHVar);
    }
}
