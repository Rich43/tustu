package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/j.class */
class j implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TuneSettingsPanel f9866a;

    j(TuneSettingsPanel tuneSettingsPanel) {
        this.f9866a = tuneSettingsPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JButton jButton = (JButton) actionEvent.getSource();
        this.f9866a.showSelectPopup(jButton.getX(), jButton.getY() + jButton.getHeight());
    }
}
