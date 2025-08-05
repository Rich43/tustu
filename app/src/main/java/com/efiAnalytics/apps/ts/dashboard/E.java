package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.bV;
import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/E.class */
class E implements InterfaceC1387a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9278a;

    E(C1425x c1425x) {
        this.f9278a = c1425x;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1387a
    public void a(File file) {
        try {
            new C1388aa();
            Gauge gaugeA = C1388aa.a(file);
            this.f9278a.b((AbstractC1420s) gaugeA);
            this.f9278a.a((AbstractC1420s) gaugeA, true);
            gaugeA.setValue(gaugeA.min() + (0.3499999940395355d * (gaugeA.max() - gaugeA.min())));
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this.f9278a.f9571ao);
        }
        this.f9278a.validate();
    }
}
