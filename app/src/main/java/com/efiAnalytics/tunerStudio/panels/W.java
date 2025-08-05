package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/W.class */
class W implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10042a;

    W(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10042a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10042a.q();
        this.f10042a.i();
        this.f10042a.o();
    }
}
