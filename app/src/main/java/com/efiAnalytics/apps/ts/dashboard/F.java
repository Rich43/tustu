package com.efiAnalytics.apps.ts.dashboard;

import i.InterfaceC1749i;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/F.class */
class F implements InterfaceC1749i {

    /* renamed from: a, reason: collision with root package name */
    boolean f9279a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f9280b;

    F(C1425x c1425x) {
        this.f9280b = c1425x;
    }

    @Override // i.InterfaceC1749i
    public void a() {
        this.f9280b.f9558aj = null;
        this.f9280b.f9560al = null;
        this.f9279a = this.f9280b.f9580aw;
        this.f9280b.f9580aw = true;
        this.f9280b.repaint();
    }

    @Override // i.InterfaceC1749i
    public void b() {
        this.f9280b.f9558aj = null;
        this.f9280b.f9560al = null;
        this.f9280b.f9580aw = this.f9279a;
        this.f9280b.repaint();
    }
}
