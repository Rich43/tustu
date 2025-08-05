package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/af.class */
class C1489af implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1488ae f10398a;

    C1489af(C1488ae c1488ae) {
        this.f10398a = c1488ae;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f10398a.b((C1496am) this.f10398a.f10391d.getSelectedValue());
    }
}
