package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ap.class */
class ap implements AdjustmentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10085a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ao f10086b;

    ap(ao aoVar, TriggerLoggerPanel triggerLoggerPanel) {
        this.f10086b = aoVar;
        this.f10085a = triggerLoggerPanel;
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        this.f10086b.f10084g.i();
    }
}
