package aO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:aO/v.class */
class v implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2722a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f2723b;

    v(q qVar, k kVar) {
        this.f2723b = qVar;
        this.f2722a = kVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f2723b.a();
            this.f2723b.f2712c.selectAll();
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }
}
