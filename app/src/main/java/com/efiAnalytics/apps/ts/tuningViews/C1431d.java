package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/d.class */
class C1431d implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1429b f9775a;

    C1431d(C1429b c1429b) {
        this.f9775a = c1429b;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        boolean z2 = itemEvent.getStateChange() == 1;
        this.f9775a.f9771f.setEnabled(z2);
        this.f9775a.f9769d.setEnabled(z2);
        this.f9775a.f9768c.setEnabled(!z2);
    }
}
