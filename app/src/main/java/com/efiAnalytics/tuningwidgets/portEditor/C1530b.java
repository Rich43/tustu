package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: com.efiAnalytics.tuningwidgets.portEditor.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/b.class */
class C1530b implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10553a;

    C1530b(OutputPortEditor outputPortEditor) {
        this.f10553a = outputPortEditor;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) throws IllegalArgumentException {
        this.f10553a.a(this.f10553a.f10539b.isSelected());
    }
}
