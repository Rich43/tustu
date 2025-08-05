package com.efiAnalytics.apps.ts.dashboard;

import d.InterfaceC1712d;
import java.util.TimerTask;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/U.class */
class U extends TimerTask {

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1712d f9409b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9410a;

    U(C1425x c1425x) {
        this.f9410a = c1425x;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        if (this.f9409b != null) {
            this.f9410a.b(this.f9409b);
        }
    }

    public void a(InterfaceC1712d interfaceC1712d) {
        this.f9409b = interfaceC1712d;
    }
}
