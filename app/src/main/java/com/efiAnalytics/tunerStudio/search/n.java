package com.efiAnalytics.tunerStudio.search;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/n.class */
class n implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f10201a;

    n(l lVar) {
        this.f10201a = lVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 40) {
            this.f10201a.f10197c.c();
            return;
        }
        if (keyEvent.getKeyCode() == 38) {
            this.f10201a.f10197c.d();
        } else if (keyEvent.getKeyCode() == 10) {
            this.f10201a.f10197c.f();
        } else {
            this.f10201a.b().a();
        }
    }
}
