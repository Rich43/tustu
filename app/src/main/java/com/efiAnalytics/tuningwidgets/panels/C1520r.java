package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/r.class */
class C1520r implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1516n f10499a;

    C1520r(C1516n c1516n) {
        this.f10499a = c1516n;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f10499a.a((C1528z) this.f10499a.f10486e.getSelectedValue(), false);
    }
}
