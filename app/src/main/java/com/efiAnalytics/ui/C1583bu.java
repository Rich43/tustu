package com.efiAnalytics.ui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: com.efiAnalytics.ui.bu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bu.class */
class C1583bu implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f11034a;

    C1583bu(C1582bt c1582bt) {
        this.f11034a = c1582bt;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f11034a.f11012a.c(((JSlider) changeEvent.getSource()).getValue());
        this.f11034a.f11012a.repaint();
        this.f11034a.f11023q = false;
    }
}
