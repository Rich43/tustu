package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:ao/gQ.class */
class gQ extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gP f5944a;

    gQ(gP gPVar) {
        this.f5944a = gPVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f5944a.d(mouseEvent.getX());
        this.f5944a.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        this.f5944a.d(mouseEvent.getX());
        this.f5944a.repaint();
    }
}
