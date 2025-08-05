package bG;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bG/D.class */
class D implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f6907a;

    D(q qVar) {
        this.f6907a = qVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 37:
                this.f6907a.f();
                break;
            case 39:
                this.f6907a.e();
                break;
            case 127:
                this.f6907a.i();
                break;
        }
        if (keyEvent.isControlDown()) {
            switch (keyEvent.getKeyCode()) {
                case 65:
                    this.f6907a.f6974a.j();
                    break;
            }
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }
}
