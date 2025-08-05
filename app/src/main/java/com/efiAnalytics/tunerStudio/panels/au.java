package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/au.class */
class au implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10095a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10096b;

    au(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10096b = aoVar;
        this.f10095a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10096b.f10084g.i(this.f10096b.f10084g.n().b());
    }
}
