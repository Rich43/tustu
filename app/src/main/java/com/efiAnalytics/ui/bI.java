package com.efiAnalytics.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bI.class */
class bI implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10883a;

    bI(C1582bt c1582bt) {
        this.f10883a = c1582bt;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f10883a.a((String) itemEvent.getItem());
        }
    }
}
