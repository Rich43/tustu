package aX;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:aX/d.class */
class d implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ c f4007a;

    d(c cVar) {
        this.f4007a = cVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        this.f4007a.f4002b.setEnabled(!this.f4007a.f4003c.getText().isEmpty());
        if (!this.f4007a.f4003c.getText().isEmpty()) {
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }
}
