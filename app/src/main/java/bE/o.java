package bE;

import com.efiAnalytics.ui.eJ;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:bE/o.class */
class o extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    int f6774a = eJ.a(10);

    /* renamed from: b, reason: collision with root package name */
    Point f6775b = null;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ m f6776c;

    o(m mVar) {
        this.f6776c = mVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        Rectangle rectangleC = this.f6776c.c();
        if (rectangleC.contains(this.f6776c.getWidth() / 2, mouseEvent.getY())) {
            this.f6776c.f6744H = mouseEvent.getY();
            this.f6776c.repaint();
        }
        if (rectangleC.contains(mouseEvent.getX(), this.f6776c.getHeight() / 2)) {
            this.f6776c.f6743G = mouseEvent.getX();
            this.f6776c.repaint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        Rectangle rectangleC = this.f6776c.c();
        if (rectangleC.contains(this.f6776c.getWidth() / 2, mouseEvent.getY())) {
            this.f6776c.f6744H = mouseEvent.getY();
        }
        if (rectangleC.contains(mouseEvent.getX(), this.f6776c.getHeight() / 2)) {
            this.f6776c.f6743G = mouseEvent.getX();
        }
        this.f6776c.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) {
            this.f6776c.f6768w = mouseEvent.getPoint();
        } else if (mouseEvent.getButton() == 3) {
            this.f6775b = mouseEvent.getPoint();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1 && this.f6776c.f6768w != null && this.f6776c.f6768w.getX() < mouseEvent.getX() - this.f6774a && this.f6776c.f6768w.getY() < mouseEvent.getY() - this.f6774a && mouseEvent.getButton() == 1) {
            Iterator it = this.f6776c.f6726d.iterator();
            while (it.hasNext()) {
                p pVar = (p) it.next();
                q qVarA = this.f6776c.a(pVar, this.f6776c.f6768w.f12370x, this.f6776c.f6768w.f12371y);
                q qVarA2 = this.f6776c.a(pVar, mouseEvent.getX(), mouseEvent.getY());
                this.f6776c.a(qVarA.getX(), qVarA2.getX(), qVarA2.getY(), qVarA.getY());
            }
        }
        if (mouseEvent.getButton() != 1) {
            this.f6775b = null;
        }
        this.f6776c.f6768w = null;
        this.f6776c.repaint();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getPreciseWheelRotation() < 0.0d) {
            this.f6776c.a(1.1d);
        } else {
            this.f6776c.a(0.9090909090909091d);
        }
    }
}
