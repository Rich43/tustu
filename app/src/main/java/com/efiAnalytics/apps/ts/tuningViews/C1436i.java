package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/i.class */
class C1436i implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TuneViewComponent f9783a;

    C1436i(TuneViewComponent tuneViewComponent) {
        this.f9783a = tuneViewComponent;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9783a.showEcuConfigPopup();
    }
}
