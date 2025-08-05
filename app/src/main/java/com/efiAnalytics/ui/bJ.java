package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bJ.class */
class bJ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10884a;

    bJ(C1582bt c1582bt) {
        this.f10884a = c1582bt;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10884a.d(((JCheckBox) actionEvent.getSource()).isSelected());
    }
}
