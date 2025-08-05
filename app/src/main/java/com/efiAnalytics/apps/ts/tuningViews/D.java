package com.efiAnalytics.apps.ts.tuningViews;

import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/D.class */
class D implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C f9678a;

    D(C c2) {
        this.f9678a = c2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f9678a.f9676a == null) {
            this.f9678a.f9676a = this.f9678a.e();
        }
        if (this.f9678a.f9676a == null) {
            return;
        }
        this.f9678a.d();
        this.f9678a.f9676a.doLayout();
        SwingUtilities.invokeLater(new E(this));
    }
}
