package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.bf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bf.class */
class C1568bf implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1566bd f10994a;

    C1568bf(C1566bd c1566bd) {
        this.f10994a = c1566bd;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10994a.b(new Color(0, 0, 0, 0));
        this.f10994a.f10991e.setEnabled(true);
    }
}
