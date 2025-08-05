package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/j.class */
class C1437j implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TuneViewComponent f9784a;

    C1437j(TuneViewComponent tuneViewComponent) {
        this.f9784a = tuneViewComponent;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9784a.setEcuConfigurationName(actionEvent.getActionCommand());
    }
}
