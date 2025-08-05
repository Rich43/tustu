package com.efiAnalytics.apps.ts.dashboard;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/az.class */
class az implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9487a;

    az(C1391ad c1391ad) {
        this.f9487a = c1391ad;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f9487a.a().setSweepBeginDegree(((JSlider) changeEvent.getSource()).getValue());
        this.f9487a.a().repaint();
    }
}
