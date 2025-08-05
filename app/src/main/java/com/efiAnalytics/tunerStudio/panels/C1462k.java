package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: com.efiAnalytics.tunerStudio.panels.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/k.class */
class C1462k implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1458g f10122a;

    C1462k(C1458g c1458g) {
        this.f10122a = c1458g;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f10122a.b(((C1465n) itemEvent.getItem()).a());
            this.f10122a.f10111b.h();
        }
    }
}
