package com.efiAnalytics.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dW.class */
class dW implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ dS f11387a;

    dW(dS dSVar) {
        this.f11387a = dSVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f11387a.e();
    }
}
