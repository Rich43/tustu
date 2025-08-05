package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/av.class */
class av implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10097a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10098b;

    av(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10098b = aoVar;
        this.f10097a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10098b.f10084g.i(this.f10098b.f10084g.n().a());
    }
}
