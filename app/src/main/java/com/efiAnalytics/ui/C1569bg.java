package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.bg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bg.class */
class C1569bg implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1566bd f10995a;

    C1569bg(C1566bd c1566bd) {
        this.f10995a = c1566bd;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10995a.f10986a.setColor(this.f10995a.f10988g);
        this.f10995a.b(this.f10995a.f10988g);
        this.f10995a.f10991e.setEnabled(false);
    }
}
