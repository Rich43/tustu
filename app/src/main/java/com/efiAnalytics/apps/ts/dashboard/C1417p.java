package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/p.class */
class C1417p implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ DashTuningPanel f9515a;

    C1417p(DashTuningPanel dashTuningPanel) {
        this.f9515a = dashTuningPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JButton jButton = (JButton) actionEvent.getSource();
        this.f9515a.showSelectPopup(jButton.getX(), jButton.getY() + jButton.getHeight());
    }
}
