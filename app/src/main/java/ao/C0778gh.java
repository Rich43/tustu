package ao;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: ao.gh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gh.class */
class C0778gh implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5977a;

    C0778gh(fX fXVar) {
        this.f5977a = fXVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f5977a.j();
    }
}
