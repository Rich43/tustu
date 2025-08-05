package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/k.class */
class k implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TuneSettingsPanel f9867a;

    k(TuneSettingsPanel tuneSettingsPanel) {
        this.f9867a = tuneSettingsPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9867a.setSettingPanelName(actionEvent.getActionCommand());
        this.f9867a.updateSelectedPanel();
        this.f9867a.f9847h = true;
    }
}
