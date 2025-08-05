package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/R.class */
class R implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10696a;

    R(C1705w c1705w) {
        this.f10696a = c1705w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10696a.f11762a.b(this.f10696a.getName() != null ? this.f10696a.getName() : "");
    }
}
