package bF;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:bF/P.class */
class P extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6834a;

    P(D d2) {
        this.f6834a = d2;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f6834a.a(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f6834a.b(mouseEvent.getX(), mouseEvent.getY());
        }
    }
}
