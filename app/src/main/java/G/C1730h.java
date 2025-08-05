package g;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: g.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/h.class */
class C1730h extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1729g f12210a;

    C1730h(C1729g c1729g) {
        this.f12210a = c1729g;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == 27) {
            this.f12210a.b();
        }
        if (keyEvent.getKeyChar() == '\n') {
            this.f12210a.d();
        }
    }
}
