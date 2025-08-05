package bH;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bH/Y.class */
class Y implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ X f7031a;

    Y(X x2) {
        this.f7031a = x2;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f7031a.b();
        }
    }
}
