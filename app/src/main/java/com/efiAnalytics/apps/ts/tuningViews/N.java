package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/N.class */
class N implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9733a;

    N(J j2) {
        this.f9733a = j2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9733a.a(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
