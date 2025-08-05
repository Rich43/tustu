package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aD.class */
class aD implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9439a;

    aD(C1391ad c1391ad) {
        this.f9439a = c1391ad;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9439a.f9463a.setFontFamily((String) ((JComboBox) actionEvent.getSource()).getSelectedItem());
        this.f9439a.a().repaint();
    }
}
