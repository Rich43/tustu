package com.efiAnalytics.apps.ts.dashboard;

import G.cX;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aM.class */
class aM implements cX, Serializable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Indicator f9451a;

    aM(Indicator indicator) {
        this.f9451a = indicator;
    }

    @Override // G.cX
    public String a() {
        String ecuConfigurationName = this.f9451a.getEcuConfigurationName();
        if ((ecuConfigurationName == null || ecuConfigurationName.isEmpty()) && G.T.a().c() != null) {
            ecuConfigurationName = G.T.a().c().c();
        }
        return ecuConfigurationName;
    }
}
