package com.efiAnalytics.apps.ts.dashboard;

import G.cX;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/o.class */
class C1416o implements cX {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ DashLabel f9514a;

    C1416o(DashLabel dashLabel) {
        this.f9514a = dashLabel;
    }

    @Override // G.cX
    public String a() {
        String ecuConfigurationName = this.f9514a.getEcuConfigurationName();
        if ((ecuConfigurationName == null || ecuConfigurationName.isEmpty()) && G.T.a().c() != null) {
            ecuConfigurationName = G.T.a().c().c();
        }
        return ecuConfigurationName;
    }
}
