package ao;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/* renamed from: ao.hj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hj.class */
class C0807hj extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0806hi f6088a;

    C0807hj(C0806hi c0806hi) {
        this.f6088a = c0806hi;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f6088a.f6084h) {
            this.f6088a.a(mouseEvent.getX());
            this.f6088a.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f6088a.f6084h) {
            this.f6088a.a(mouseEvent.getX());
            this.f6088a.repaint();
        }
    }
}
