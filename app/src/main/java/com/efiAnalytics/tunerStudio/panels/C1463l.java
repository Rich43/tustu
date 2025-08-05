package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tunerStudio.panels.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/l.class */
class C1463l implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1458g f10123a;

    C1463l(C1458g c1458g) {
        this.f10123a = c1458g;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10123a.close();
        this.f10123a.f10116g.dispose();
    }
}
