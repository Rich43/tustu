package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: t.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/v.class */
class C1874v extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1870r f13909a;

    C1874v(C1870r c1870r) {
        this.f13909a = c1870r;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        if (this.f13909a.a(this.f13909a.f13905e, C1818g.b("Component Height"))) {
            this.f13909a.c().k((int) this.f13909a.f13905e.e());
            this.f13909a.f13905e.setForeground(UIManager.getColor("Label.foreground"));
        }
    }
}
