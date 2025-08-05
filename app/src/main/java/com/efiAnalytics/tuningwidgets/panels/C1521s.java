package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/s.class */
class C1521s implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10500a;

    C1521s(C1516n c1516n) {
        this.f10500a = c1516n;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f10500a.a((C1528z) this.f10500a.f10488g.getSelectedValue(), true);
    }
}
