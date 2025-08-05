package bt;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bt.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/am.class */
class C1304am extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8887a;

    C1304am(C1303al c1303al) {
        this.f8887a = c1303al;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f8887a.b(mouseEvent.getX(), mouseEvent.getY());
        } else if (this.f8887a.f8870y) {
            this.f8887a.c(false);
        }
    }
}
