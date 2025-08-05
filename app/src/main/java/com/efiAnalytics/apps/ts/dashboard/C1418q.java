package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/q.class */
class C1418q implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ DashTuningPanel f9516a;

    C1418q(DashTuningPanel dashTuningPanel) {
        this.f9516a = dashTuningPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9516a.menuClicked(actionEvent.getActionCommand());
    }
}
