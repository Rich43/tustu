package com.efiAnalytics.apps.ts.dashboard;

import java.io.Serializable;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/w.class */
class C1424w implements G.aN, Serializable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Gauge f9557a;

    C1424w(Gauge gauge) {
        this.f9557a = gauge;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        this.f9557a.invalidatePainter();
    }
}
