package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ah.class */
class C1395ah extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9469a;

    C1395ah(C1391ad c1391ad) {
        this.f9469a = c1391ad;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) throws NumberFormatException {
        this.f9469a.a().setCurrentOutputChannelValue("", Double.parseDouble(((JTextField) keyEvent.getSource()).getText()));
        this.f9469a.a().repaint();
    }
}
