package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/j.class */
class C1512j implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1509g f10461a;

    C1512j(C1509g c1509g) {
        this.f10461a = c1509g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10461a.c()) {
            this.f10461a.f();
            this.f10461a.close();
            this.f10461a.f10457p.dispose();
        }
    }
}
