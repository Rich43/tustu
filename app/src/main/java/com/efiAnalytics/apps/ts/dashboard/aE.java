package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aE.class */
class aE {

    /* renamed from: a, reason: collision with root package name */
    GaugePainter f9440a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1391ad f9441b;

    public aE(C1391ad c1391ad, GaugePainter gaugePainter) {
        this.f9441b = c1391ad;
        this.f9440a = null;
        this.f9440a = gaugePainter;
    }

    public GaugePainter a() {
        return this.f9440a;
    }

    public String toString() {
        return this.f9440a.getName();
    }

    public boolean equals(Object obj) {
        if (obj instanceof aE) {
            return ((aE) obj).a().getName().equals(this.f9440a.getName());
        }
        return false;
    }
}
