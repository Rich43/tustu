package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: com.efiAnalytics.tunerStudio.panels.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/c.class */
class C1454c extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1452a f10105a;

    C1454c(C1452a c1452a) {
        this.f10105a = c1452a;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f10105a.f10047b.getText().length() > 0) {
            this.f10105a.f10053j.b(this.f10105a.f10047b.getText());
            this.f10105a.getParent().repaint();
        }
    }
}
