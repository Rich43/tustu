package com.efiAnalytics.tuningwidgets.portEditor;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/m.class */
class m implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10575a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ l f10576b;

    m(l lVar, OutputPortEditor outputPortEditor) {
        this.f10576b = lVar;
        this.f10575a = outputPortEditor;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f10576b.c();
    }
}
