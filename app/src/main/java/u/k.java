package u;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/* loaded from: TunerStudioMS.jar:u/k.class */
class k extends MouseInputAdapter {

    /* renamed from: a, reason: collision with root package name */
    Container f13997a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f13998b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    boolean f13999c = false;

    /* renamed from: d, reason: collision with root package name */
    Component f14000d = null;

    /* renamed from: e, reason: collision with root package name */
    C1879a f14001e = null;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ h f14002f;

    public k(h hVar, Container container) {
        this.f14002f = hVar;
        this.f13997a = null;
        this.f13997a = container;
    }

    public void a(Component component) {
        this.f13998b.add(component);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) throws HeadlessException {
        a(mouseEvent);
        this.f13999c = false;
        this.f14000d = null;
    }

    private void a(MouseEvent mouseEvent) throws HeadlessException {
        Point point = mouseEvent.getPoint();
        Point pointConvertPoint = SwingUtilities.convertPoint(this.f13997a, point, this.f13997a);
        int id = mouseEvent.getID();
        if (!this.f13999c) {
            this.f14000d = SwingUtilities.getDeepestComponentAt(this.f14002f.getContentPane(), pointConvertPoint.f12370x, pointConvertPoint.f12371y);
            a(id);
        }
        if (this.f14000d == null || (this.f14000d instanceof j)) {
            return;
        }
        if ((this.f14000d instanceof C1879a) && this.f14001e == null) {
            C1879a c1879a = (C1879a) this.f14000d;
            c1879a.a();
            this.f14001e = c1879a;
        } else if (!(this.f14000d instanceof C1879a) && this.f14001e != null) {
            this.f14001e.b();
            this.f14001e = null;
        } else if (this.f13998b.contains(this.f14000d)) {
            if (mouseEvent.getID() == 500 || mouseEvent.getID() == 504 || mouseEvent.getID() == 505 || this.f13999c) {
                Point pointConvertPoint2 = SwingUtilities.convertPoint(this.f13997a, point, this.f14000d);
                this.f14000d.dispatchEvent(new MouseEvent(this.f14000d, id, mouseEvent.getWhen(), mouseEvent.getModifiers(), pointConvertPoint2.f12370x, pointConvertPoint2.f12371y, mouseEvent.getClickCount(), mouseEvent.isPopupTrigger()));
            }
        }
    }

    private void a(int i2) {
        if (i2 == 501) {
            this.f13999c = true;
        }
    }
}
