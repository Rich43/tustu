package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ag.class */
class C1394ag extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1391ad f9468a;

    C1394ag(C1391ad c1391ad) {
        this.f9468a = c1391ad;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f9468a.a().setUnits(((JTextField) keyEvent.getSource()).getText());
        this.f9468a.a().repaint();
    }
}
