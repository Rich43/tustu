package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/af.class */
class af implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10061a;

    af(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10061a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10061a.q();
        this.f10061a.i();
        this.f10061a.o();
    }
}
