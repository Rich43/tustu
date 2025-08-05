package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/L.class */
class L implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9960a;

    L(J j2) {
        this.f9960a = j2;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f9960a.h();
    }
}
