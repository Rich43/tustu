package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/aw.class */
class aw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10099a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10100b;

    aw(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10100b = aoVar;
        this.f10099a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10100b.f10084g.l()) {
            this.f10100b.f10084g.i(this.f10100b.f10084g.n().e());
        }
    }
}
