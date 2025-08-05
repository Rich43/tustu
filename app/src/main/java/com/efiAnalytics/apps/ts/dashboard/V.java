package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/V.class */
class V implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9411a;

    V(C1425x c1425x) {
        this.f9411a = c1425x;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9411a.b(actionEvent.getActionCommand());
    }
}
