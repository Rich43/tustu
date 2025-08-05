package bG;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/* loaded from: TunerStudioMS.jar:bG/n.class */
class n implements MouseListener, MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    double f6964a = 0.0d;

    /* renamed from: b, reason: collision with root package name */
    double f6965b = 0.0d;

    /* renamed from: c, reason: collision with root package name */
    double f6966c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ m f6967d;

    n(m mVar) {
        this.f6967d = mVar;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (this.f6967d.isEnabled()) {
            this.f6964a = this.f6967d.b(mouseEvent.getX(), mouseEvent.getY());
            this.f6966c = this.f6964a;
            this.f6965b = this.f6967d.c();
            int iA = this.f6967d.a(mouseEvent.getX(), mouseEvent.getY());
            if (iA >= 0 && this.f6967d.g()) {
                bH.C.c("Tooth Clicked: " + iA);
                this.f6967d.c(Integer.valueOf(iA));
            }
            this.f6967d.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f6967d.k();
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.f6967d.isEnabled()) {
            this.f6967d.a((this.f6964a - this.f6967d.b(mouseEvent.getX(), mouseEvent.getY())) + this.f6965b);
            this.f6967d.k();
            this.f6967d.repaint();
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
