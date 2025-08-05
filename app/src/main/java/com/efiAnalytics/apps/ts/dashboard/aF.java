package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aF.class */
class aF implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9442a;

    aF(C1391ad c1391ad) {
        this.f9442a = c1391ad;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }
}
