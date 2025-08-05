package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/I.class */
class I implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9355a;

    I(C1425x c1425x) {
        this.f9355a = c1425x;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9355a.f9642aQ = ((JCheckBoxMenuItem) actionEvent.getSource()).getState();
        this.f9355a.f9559ak = null;
        this.f9355a.repaint();
    }
}
