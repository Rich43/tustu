package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/as.class */
class as implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10091a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10092b;

    as(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10092b = aoVar;
        this.f10091a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10092b.f10084g.a(0.0d);
    }
}
