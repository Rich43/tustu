package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/at.class */
class at implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10093a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10094b;

    at(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10094b = aoVar;
        this.f10093a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10094b.f10084g.l()) {
            this.f10094b.f10084g.i(this.f10094b.f10084g.n().f());
        }
    }
}
