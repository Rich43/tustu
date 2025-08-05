package bF;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bF/M.class */
class M implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6822a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ L f6823b;

    M(L l2, D d2) {
        this.f6823b = l2;
        this.f6822a = d2;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        a(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        a(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        a(keyEvent);
    }

    private void a(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 27) {
            this.f6823b.cancelCellEditing();
            keyEvent.consume();
        }
    }
}
