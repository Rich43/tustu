package ao;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: ao.ge, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ge.class */
class C0775ge extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5974a;

    C0775ge(fX fXVar) {
        this.f5974a = fXVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f5974a.q();
        }
    }
}
