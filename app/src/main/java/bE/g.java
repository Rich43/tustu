package bE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bE/g.class */
class g implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6718a;

    g(e eVar) {
        this.f6718a = eVar;
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
            this.f6718a.e();
        }
    }
}
