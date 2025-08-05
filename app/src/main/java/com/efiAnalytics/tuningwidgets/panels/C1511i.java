package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/i.class */
class C1511i implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1509g f10460a;

    C1511i(C1509g c1509g) {
        this.f10460a = c1509g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10460a.close();
        this.f10460a.f10457p.dispose();
    }
}
