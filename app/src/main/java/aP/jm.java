package aP;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:aP/jm.class */
class jm implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3798a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ jl f3799b;

    jm(jl jlVar, iX iXVar) {
        this.f3799b = jlVar;
        this.f3798a = iXVar;
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
            this.f3799b.cancelCellEditing();
            keyEvent.consume();
        }
    }
}
