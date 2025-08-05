package br;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* renamed from: br.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/m.class */
class C1249m implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1245i f8479a;

    C1249m(C1245i c1245i) {
        this.f8479a = c1245i;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 27) {
            return false;
        }
        this.f8479a.d();
        return true;
    }
}
