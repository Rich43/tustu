package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/aa.class */
class aa implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10056a;

    aa(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10056a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f10056a.q();
        this.f10056a.i();
        this.f10056a.o();
    }
}
