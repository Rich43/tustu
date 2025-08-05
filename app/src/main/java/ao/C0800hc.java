package ao;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: ao.hc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hc.class */
class C0800hc implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f6045a;

    C0800hc(gS gSVar) {
        this.f6045a = gSVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            C0645bi.a().c().requestFocus();
        }
    }
}
