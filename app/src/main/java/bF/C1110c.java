package bf;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: bf.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/c.class */
class C1110c extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1108a f8047a;

    C1110c(C1108a c1108a) {
        this.f8047a = c1108a;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() != 2 || this.f8047a.f8037f == null) {
            return;
        }
        this.f8047a.e(this.f8047a.f8037f);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            int rowForLocation = this.f8047a.f8041j.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
            this.f8047a.f8041j.setSelectionPath(this.f8047a.f8041j.getPathForLocation(mouseEvent.getX(), mouseEvent.getY()));
            if (rowForLocation > -1) {
                this.f8047a.f8041j.setSelectionRow(rowForLocation);
            }
            this.f8047a.a(mouseEvent);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            int rowForLocation = this.f8047a.f8041j.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
            this.f8047a.f8041j.setSelectionPath(this.f8047a.f8041j.getPathForLocation(mouseEvent.getX(), mouseEvent.getY()));
            if (rowForLocation > -1) {
                this.f8047a.f8041j.setSelectionRow(rowForLocation);
            }
            this.f8047a.a(mouseEvent);
        }
    }
}
