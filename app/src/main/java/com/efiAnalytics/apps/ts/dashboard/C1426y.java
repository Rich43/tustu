package com.efiAnalytics.apps.ts.dashboard;

import i.InterfaceC1741a;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/y.class */
class C1426y implements InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9666a;

    C1426y(C1425x c1425x) {
        this.f9666a = c1425x;
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f9666a.f9584k == null || !this.f9666a.f9584k.R()) {
            this.f9666a.f9558aj = null;
            this.f9666a.f9560al = null;
            this.f9666a.f9566f.c();
            this.f9666a.f9656aa = System.currentTimeMillis() + C1425x.f9657ab;
            this.f9666a.f9566f.a(this.f9666a.f9656aa);
        }
    }
}
