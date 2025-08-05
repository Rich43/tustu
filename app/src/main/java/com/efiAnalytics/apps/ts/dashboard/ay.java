package com.efiAnalytics.apps.ts.dashboard;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ay.class */
class ay implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9486a;

    ay(C1391ad c1391ad) {
        this.f9486a = c1391ad;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f9486a.a().setStartAngle(((JSlider) changeEvent.getSource()).getValue());
        this.f9486a.a().repaint();
    }
}
