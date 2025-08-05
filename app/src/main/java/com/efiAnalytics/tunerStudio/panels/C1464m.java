package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tunerStudio.panels.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/m.class */
class C1464m implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1458g f10124a;

    C1464m(C1458g c1458g) {
        this.f10124a = c1458g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10124a.f()) {
            this.f10124a.g();
            this.f10124a.close();
            this.f10124a.f10116g.dispose();
        }
    }
}
