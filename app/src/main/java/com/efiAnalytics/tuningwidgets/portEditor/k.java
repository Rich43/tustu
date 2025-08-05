package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/k.class */
class k implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10570a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ j f10571b;

    k(j jVar, OutputPortEditor outputPortEditor) {
        this.f10571b = jVar;
        this.f10570a = outputPortEditor;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f10571b.setEnabled(this.f10571b.isEnabled());
    }
}
