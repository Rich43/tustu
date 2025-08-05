package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/aq.class */
class aq implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10087a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10088b;

    aq(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10088b = aoVar;
        this.f10087a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10088b.f10084g.t();
    }
}
