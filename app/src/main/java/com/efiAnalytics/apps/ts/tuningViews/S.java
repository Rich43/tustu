package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/S.class */
class S implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9743a;

    S(J j2) {
        this.f9743a = j2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9743a.a(actionEvent.getActionCommand());
    }
}
