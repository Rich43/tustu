package bD;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:bD/E.class */
class E extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ r f6613a;

    E(r rVar) {
        this.f6613a = rVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f6613a.a(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f6613a.a(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
            this.f6613a.g();
        }
    }
}
