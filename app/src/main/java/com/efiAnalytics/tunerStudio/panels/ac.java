package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ac.class */
class ac implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10058a;

    ac(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10058a = triggerLoggerPanel;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        boolean zIsSelected = this.f10058a.f9991o.isSelected();
        this.f10058a.b("Show Lines", Boolean.toString(zIsSelected));
        this.f10058a.f9982f.b(zIsSelected);
        this.f10058a.f9982f.d();
        this.f10058a.f9982f.repaint();
    }
}
