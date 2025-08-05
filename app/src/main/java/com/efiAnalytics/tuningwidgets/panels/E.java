package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/E.class */
class E implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ A f10249a;

    E(A a2) {
        this.f10249a = a2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10249a.removeAll();
    }
}
