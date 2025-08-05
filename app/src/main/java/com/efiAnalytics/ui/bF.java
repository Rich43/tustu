package com.efiAnalytics.ui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bF.class */
class bF implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10880a;

    bF(C1582bt c1582bt) {
        this.f10880a = c1582bt;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f10880a.f11012a.d(((JSlider) changeEvent.getSource()).getValue());
        this.f10880a.f11012a.repaint();
        this.f10880a.f11023q = false;
    }
}
