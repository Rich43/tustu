package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:ao/fT.class */
class fT extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5719a;

    fT(C0764fu c0764fu) {
        this.f5719a = c0764fu;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f5719a.a(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        } else {
            new fW(this.f5719a, mouseEvent.getX(), mouseEvent.getY()).start();
        }
    }
}
