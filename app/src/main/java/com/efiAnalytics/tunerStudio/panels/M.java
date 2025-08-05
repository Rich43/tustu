package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/M.class */
class M implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9961a;

    M(J j2) {
        this.f9961a = j2;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f9961a.d();
    }
}
