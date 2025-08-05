package be;

import G.C0126i;

/* renamed from: be.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/e.class */
class C1089e implements S {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1087c f7968a;

    C1089e(C1087c c1087c) {
        this.f7968a = c1087c;
    }

    @Override // be.S
    public R a(String str) {
        R r2 = new R();
        if (str.length() == 0) {
            r2.a("Channel Name Required");
            r2.c();
            return r2;
        }
        if (!C0126i.c(str)) {
            r2.b();
            return r2;
        }
        r2.a("Channel Name cannot have special characters");
        r2.c();
        return r2;
    }
}
