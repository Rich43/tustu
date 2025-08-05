package ar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: ar.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/k.class */
class C0844k extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0840g f6240a;

    C0844k(C0840g c0840g) {
        this.f6240a = c0840g;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f6240a.a(mouseEvent);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f6240a.a(mouseEvent);
        }
    }
}
