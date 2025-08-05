package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bK.class */
class bK implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10885a;

    bK(C1582bt c1582bt) {
        this.f10885a = c1582bt;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10885a.a("followMode", "" + this.f10885a.f11019h.isSelected());
    }
}
