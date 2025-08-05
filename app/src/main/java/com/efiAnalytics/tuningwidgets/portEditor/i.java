package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/i.class */
class i implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10564a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ h f10565b;

    i(h hVar, OutputPortEditor outputPortEditor) {
        this.f10565b = hVar;
        this.f10564a = outputPortEditor;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f10565b.a();
    }
}
