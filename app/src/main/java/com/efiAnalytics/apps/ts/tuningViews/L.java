package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/L.class */
class L implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9731a;

    L(J j2) {
        this.f9731a = j2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9731a.f9722O = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
        this.f9731a.repaint();
    }
}
