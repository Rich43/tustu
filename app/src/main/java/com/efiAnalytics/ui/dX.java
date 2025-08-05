package com.efiAnalytics.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dX.class */
class dX implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dS f11388a;

    dX(dS dSVar) {
        this.f11388a = dSVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
        this.f11388a.f();
    }
}
