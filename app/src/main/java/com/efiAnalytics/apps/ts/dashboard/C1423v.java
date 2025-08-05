package com.efiAnalytics.apps.ts.dashboard;

import G.cX;
import java.io.Serializable;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/v.class */
class C1423v implements cX, Serializable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Gauge f9556a;

    C1423v(Gauge gauge) {
        this.f9556a = gauge;
    }

    @Override // G.cX
    public String a() {
        String ecuConfigurationName = this.f9556a.getEcuConfigurationName();
        if ((ecuConfigurationName == null || ecuConfigurationName.isEmpty()) && G.T.a().c() != null) {
            ecuConfigurationName = G.T.a().c().c();
        }
        return ecuConfigurationName;
    }
}
