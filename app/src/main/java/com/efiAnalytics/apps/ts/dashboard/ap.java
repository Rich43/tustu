package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ap.class */
class ap implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9477a;

    ap(C1391ad c1391ad) {
        this.f9477a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9477a.f9463a.setGaugePainter(((aE) ((JComboBox) actionEvent.getSource()).getSelectedItem()).a());
        this.f9477a.a().repaint();
    }
}
