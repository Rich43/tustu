package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/C.class */
class C implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9266a;

    C(C1425x c1425x) {
        this.f9266a = c1425x;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9266a.i(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
