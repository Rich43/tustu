package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.be, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/be.class */
class C1567be implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1566bd f10993a;

    C1567be(C1566bd c1566bd) {
        this.f10993a = c1566bd;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10993a.b(this.f10993a.f10986a.getColor());
        this.f10993a.f10991e.setEnabled(true);
    }
}
