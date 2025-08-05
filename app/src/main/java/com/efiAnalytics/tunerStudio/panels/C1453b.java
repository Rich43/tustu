package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: com.efiAnalytics.tunerStudio.panels.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/b.class */
class C1453b extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1452a f10104a;

    C1453b(C1452a c1452a) {
        this.f10104a = c1452a;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (this.f10104a.f10046a.getText().length() > 6 || " /\\'\"+-*/_)(&^%$#@!~`;<>,.:;?".indexOf(keyEvent.getKeyChar()) >= 0) {
            keyEvent.consume();
        }
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f10104a.f10046a.getText().length() > 0) {
            this.f10104a.f10053j.a(this.f10104a.f10046a.getText());
            this.f10104a.getParent().repaint();
        }
    }
}
