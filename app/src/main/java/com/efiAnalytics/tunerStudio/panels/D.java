package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eB;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/D.class */
class D implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C f9936a;

    D(C c2) {
        this.f9936a = c2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f9936a.f9932b = new eB(bV.a(this.f9936a.f9935e.f10137k), "Loading Ignition Log", "Loading Ignition Log File, please wait....", true, false);
        this.f9936a.f9932b.a(this.f9936a.f9934d);
        this.f9936a.f9932b.setVisible(true);
    }
}
