package bE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bE/i.class */
class i implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6720a;

    i(e eVar) {
        this.f6720a = eVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == '\n') {
            this.f6720a.f();
        }
    }
}
