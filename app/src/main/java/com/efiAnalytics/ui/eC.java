package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eC.class */
class eC implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eB f11473a;

    eC(eB eBVar) {
        this.f11473a = eBVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f11473a.a();
        this.f11473a.dispose();
    }
}
