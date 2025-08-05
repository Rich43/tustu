package com.efiAnalytics.ui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bG.class */
class bG implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10881a;

    bG(C1582bt c1582bt) {
        this.f10881a = c1582bt;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        double value = ((JSlider) changeEvent.getSource()).getValue() / 100.0d;
        this.f10881a.f11012a.c(value);
        this.f10881a.a("zHeightScale", value + "");
        this.f10881a.f11012a.z();
        this.f10881a.f11012a.repaint();
    }
}
