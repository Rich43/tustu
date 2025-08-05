package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ar.class */
class ar implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10089a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10090b;

    ar(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10090b = aoVar;
        this.f10089a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10090b.f10084g.b(0.0d);
    }
}
