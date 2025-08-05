package com.efiAnalytics.apps.ts.dashboard;

import G.di;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ar.class */
class ar extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9479a;

    ar(C1391ad c1391ad) {
        this.f9479a = c1391ad;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            this.f9479a.a().setLowCritical(di.a(C1419r.a(this.f9479a.a()), jTextField.getText()));
            this.f9479a.a().repaint();
        } catch (Exception e2) {
            bH.C.c("Invalid expression: " + jTextField.getText());
        }
    }
}
