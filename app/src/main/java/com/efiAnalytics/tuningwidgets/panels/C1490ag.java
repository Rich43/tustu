package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ag.class */
class C1490ag implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1488ae f10399a;

    C1490ag(C1488ae c1488ae) {
        this.f10399a = c1488ae;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f10399a.b((C1496am) this.f10399a.f10393f.getSelectedValue());
    }
}
