package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/A.class */
class A implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1466o f9929a;

    A(C1466o c1466o) {
        this.f9929a = c1466o;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9929a.a(((JCheckBox) actionEvent.getSource()).isSelected());
    }
}
