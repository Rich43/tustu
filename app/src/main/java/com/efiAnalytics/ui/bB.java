package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bB.class */
class bB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10876a;

    bB(C1582bt c1582bt) {
        this.f10876a = c1582bt;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10876a.b(((JCheckBoxMenuItem) actionEvent.getSource()).isSelected());
        this.f10876a.f11012a.z();
        this.f10876a.f11012a.repaint();
    }
}
