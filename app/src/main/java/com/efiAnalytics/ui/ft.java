package com.efiAnalytics.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ft.class */
class ft extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1688fs f11713a;

    ft(C1688fs c1688fs) {
        this.f11713a = c1688fs;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == 27) {
            this.f11713a.b();
        }
        if (keyEvent.getKeyChar() == '\n') {
            this.f11713a.c();
        }
    }
}
