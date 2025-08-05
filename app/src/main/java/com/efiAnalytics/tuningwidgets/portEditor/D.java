package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/D.class */
class D implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C f10528a;

    D(C c2) {
        this.f10528a = c2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10528a.a((String) this.f10528a.getSelectedItem());
    }
}
