package be;

import G.C0126i;
import G.aI;

/* renamed from: be.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/f.class */
class C1090f implements S {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1087c f7969a;

    C1090f(C1087c c1087c) {
        this.f7969a = c1087c;
    }

    @Override // be.S
    public R a(String str) {
        R r2 = new R();
        if (str.trim().isEmpty()) {
            r2.c();
            r2.a("You must define an expression");
            return r2;
        }
        try {
            C0126i.a(str, (aI) this.f7969a.f7964i);
            r2.b();
            return r2;
        } catch (Exception e2) {
            r2.c();
            if (e2.getMessage() == null || e2.getMessage().length() < 10) {
                r2.a("Invalid Expression");
            } else {
                r2.a(e2.getMessage());
            }
            return r2;
        }
    }
}
