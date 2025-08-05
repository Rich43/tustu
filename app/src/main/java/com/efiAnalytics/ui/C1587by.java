package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: com.efiAnalytics.ui.by, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/by.class */
class C1587by implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f11038a;

    C1587by(C1582bt c1582bt) {
        this.f11038a = c1582bt;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f11038a.g(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
