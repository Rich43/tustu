package bw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bw/g.class */
class g implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f9177a;

    g(f fVar) {
        this.f9177a = fVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f9177a.b();
    }
}
