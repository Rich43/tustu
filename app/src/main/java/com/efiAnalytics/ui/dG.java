package com.efiAnalytics.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dG.class */
class dG extends WindowAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dF f11327a;

    dG(dF dFVar) {
        this.f11327a = dFVar;
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.f11327a.k();
    }
}
