package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/s.class */
class C1871s extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1870r f13906a;

    C1871s(C1870r c1870r) {
        this.f13906a = c1870r;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f13906a.a(this.f13906a.f13902b, C1818g.b("Component X"))) {
            this.f13906a.c().h((int) this.f13906a.f13902b.e());
            this.f13906a.f13902b.setForeground(UIManager.getColor("Label.foreground"));
        }
    }
}
