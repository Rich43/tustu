package com.efiAnalytics.apps.ts.tuningViews;

import c.InterfaceC1385d;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/w.class */
class C1450w implements InterfaceC1385d {

    /* renamed from: a, reason: collision with root package name */
    J f9874a;

    /* renamed from: b, reason: collision with root package name */
    boolean f9875b = true;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1441n f9876c;

    C1450w(C1441n c1441n, J j2) {
        this.f9876c = c1441n;
        this.f9874a = j2;
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return G.T.a().c();
    }

    @Override // c.InterfaceC1385d
    public String a_() {
        return this.f9874a.A();
    }

    @Override // c.InterfaceC1385d
    public void b_(String str) {
        this.f9874a.d(str);
    }

    @Override // c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        this.f9876c.setEnabledAt(this.f9876c.f(this.f9874a.getName()), z2);
        this.f9875b = z2;
    }

    @Override // c.InterfaceC1385d
    public boolean isEnabled() {
        return this.f9875b;
    }
}
