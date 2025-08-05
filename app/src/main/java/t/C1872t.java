package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/t.class */
class C1872t extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1870r f13907a;

    C1872t(C1870r c1870r) {
        this.f13907a = c1870r;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f13907a.a(this.f13907a.f13903c, C1818g.b("Component Y"))) {
            this.f13907a.c().i((int) this.f13907a.f13903c.e());
            this.f13907a.f13903c.setForeground(UIManager.getColor("Label.foreground"));
        }
    }
}
