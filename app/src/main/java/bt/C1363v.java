package bt;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/v.class */
class C1363v extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1360s f9134a;

    C1363v(C1360s c1360s) {
        this.f9134a = c1360s;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.f9134a.a(true);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f9134a.a(false);
    }
}
