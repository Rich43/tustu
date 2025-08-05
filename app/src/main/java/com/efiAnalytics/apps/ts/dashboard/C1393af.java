package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.af, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/af.class */
class C1393af extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9467a;

    C1393af(C1391ad c1391ad) {
        this.f9467a = c1391ad;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f9467a.a().setTitle(((JTextField) keyEvent.getSource()).getText());
        this.f9467a.a().repaint();
    }
}
