package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/u.class */
class C1873u extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1870r f13908a;

    C1873u(C1870r c1870r) {
        this.f13908a = c1870r;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f13908a.a(this.f13908a.f13904d, C1818g.b("Component Width"))) {
            this.f13908a.c().j((int) this.f13908a.f13904d.e());
            this.f13908a.f13904d.setForeground(UIManager.getColor("Label.foreground"));
        }
    }
}
