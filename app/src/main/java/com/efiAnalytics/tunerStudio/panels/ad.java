package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ad.class */
class ad implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10059a;

    ad(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10059a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10059a.f10038ae = ((Integer) this.f10059a.f9990n.getItemAt(this.f10059a.f9990n.getSelectedIndex())).intValue();
        this.f10059a.i();
    }
}
