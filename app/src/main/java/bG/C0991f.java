package bG;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: bG.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/f.class */
class C0991f implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0989d f6936a;

    C0991f(C0989d c0989d) {
        this.f6936a = c0989d;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == '\n') {
            this.f6936a.e();
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }
}
