package bD;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bD/M.class */
class M implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ I f6642a;

    M(I i2) {
        this.f6642a = i2;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f6642a.c();
        } else if (keyEvent.getKeyCode() == 27) {
            this.f6642a.d();
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f6642a.f6636e.isSelected()) {
            this.f6642a.b();
        }
    }
}
