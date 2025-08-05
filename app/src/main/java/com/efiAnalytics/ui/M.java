package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/M.class */
class M implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10691a;

    M(C1705w c1705w) {
        this.f10691a = c1705w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        boolean z2 = this.f10691a.f11785D;
        this.f10691a.f11785D = true;
        this.f10691a.d();
        this.f10691a.f11785D = z2;
    }
}
