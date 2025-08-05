package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.InterfaceC0124g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/d.class */
class d implements InterfaceC0124g {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BurnButtonTv f9852a;

    d(BurnButtonTv burnButtonTv) {
        this.f9852a = burnButtonTv;
    }

    @Override // G.InterfaceC0124g
    public void a(String str, int i2) {
        this.f9852a.enableBurn(true);
    }

    @Override // G.InterfaceC0124g
    public void b(String str, int i2) {
    }

    @Override // G.InterfaceC0124g
    public void a(String str, boolean z2) {
        this.f9852a.enableBurn(!z2);
    }
}
