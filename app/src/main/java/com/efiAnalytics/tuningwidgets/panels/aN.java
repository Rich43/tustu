package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aN.class */
class aN implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aM f10363a;

    aN(aM aMVar) {
        this.f10363a = aMVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f10363a.a(itemEvent.getItem().toString());
    }
}
