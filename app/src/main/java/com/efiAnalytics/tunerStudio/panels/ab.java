package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ab.class */
class ab implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10057a;

    ab(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10057a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (this.f10057a.f10037ad || this.f10057a.f9976a.j() == null) {
            return;
        }
        this.f10057a.b(this.f10057a.f9976a.j());
    }
}
