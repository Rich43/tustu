package com.efiAnalytics.tunerStudio.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/Z.class */
class Z implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10045a;

    Z(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10045a = triggerLoggerPanel;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (this.f10045a.f10006aj == null) {
            return;
        }
        this.f10045a.f10006aj.a(TriggerLoggerPanel.f10016L, this.f10045a.f9992p.getDividerLocation() + "");
    }
}
