package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/w.class */
class w implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ v f10606a;

    w(v vVar) {
        this.f10606a = vVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10606a.c(((y) this.f10606a.getSelectedItem()).a());
    }
}
