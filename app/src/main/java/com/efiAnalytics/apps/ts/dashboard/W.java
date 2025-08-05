package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/W.class */
class W extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    List f9412a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f9413b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f9414c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    List f9415d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    boolean f9416e = false;

    /* renamed from: f, reason: collision with root package name */
    AbstractC1420s f9417f = null;

    /* renamed from: g, reason: collision with root package name */
    Point f9418g = null;

    /* renamed from: h, reason: collision with root package name */
    final /* synthetic */ C1425x f9419h;

    W(C1425x c1425x) {
        this.f9419h = c1425x;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        boolean z2;
        try {
            if (!this.f9419h.isEnabled()) {
                if (z2) {
                    return;
                } else {
                    return;
                }
            }
            this.f9417f = a(mouseEvent);
            Point mousePosition = this.f9417f.getParent().getMousePosition();
            if (mousePosition == null) {
                if (mouseEvent.getSource() instanceof AbstractC1420s) {
                    this.f9419h.dispatchEvent(mouseEvent);
                    return;
                }
                return;
            }
            if (mouseEvent.getButton() == 1 || mouseEvent.getButton() == 3) {
                boolean z3 = (mouseEvent.getModifiers() & 2) != 2;
                if (!this.f9419h.e(this.f9417f) || !z3) {
                    this.f9419h.a(this.f9417f, z3);
                }
            }
            if (mouseEvent.getButton() == 3) {
            }
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == 1) {
                this.f9419h.t();
                mouseEvent.consume();
            }
            this.f9412a.clear();
            this.f9413b.clear();
            this.f9414c.clear();
            this.f9415d.clear();
            Iterator it = this.f9419h.f9592aF.iterator();
            while (it.hasNext()) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                this.f9412a.add(Integer.valueOf(mousePosition.f12370x - abstractC1420s.getX()));
                this.f9413b.add(Integer.valueOf(mousePosition.f12371y - abstractC1420s.getY()));
                this.f9414c.add(Integer.valueOf(abstractC1420s.getWidth()));
                this.f9415d.add(Integer.valueOf(abstractC1420s.getHeight()));
            }
            this.f9418g = mousePosition;
            this.f9419h.requestFocus();
            this.f9419h.repaint();
            Iterator it2 = this.f9419h.f9592aF.iterator();
            while (it2.hasNext()) {
                AbstractC1420s abstractC1420s2 = (AbstractC1420s) it2.next();
                this.f9416e = this.f9418g.f12370x > (abstractC1420s2.getX() + abstractC1420s2.getWidth()) - this.f9419h.f9569am && this.f9418g.f12370x < abstractC1420s2.getX() + abstractC1420s2.getWidth() && this.f9418g.f12371y > (abstractC1420s2.getY() + abstractC1420s2.getHeight()) - this.f9419h.f9569am && this.f9418g.f12371y < abstractC1420s2.getY() + abstractC1420s2.getHeight();
                if (this.f9416e) {
                    break;
                }
            }
            if (mouseEvent.getSource() instanceof AbstractC1420s) {
                this.f9419h.dispatchEvent(mouseEvent);
            }
        } finally {
            if (mouseEvent.getSource() instanceof AbstractC1420s) {
                this.f9419h.dispatchEvent(mouseEvent);
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (!this.f9419h.L() && (mouseEvent.getSource() instanceof AbstractC1420s)) {
            this.f9419h.dispatchEvent(mouseEvent);
        }
        if (this.f9419h.isEnabled()) {
            AbstractC1420s abstractC1420sA = this.f9417f != null ? this.f9417f : a(mouseEvent);
            Iterator it = this.f9419h.f9592aF.iterator();
            while (it.hasNext()) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) it.next();
                if (this.f9416e) {
                    double width = abstractC1420s.getWidth() / this.f9419h.getWidth();
                    abstractC1420s.setRelativeWidth(width);
                    abstractC1420s.setRelativeHeight(abstractC1420s.getHeight() / this.f9419h.getHeight());
                } else {
                    double x2 = abstractC1420s.getX() / this.f9419h.getWidth();
                    abstractC1420s.setRelativeX(x2);
                    abstractC1420s.setRelativeY(abstractC1420s.getY() / this.f9419h.getHeight());
                }
            }
            this.f9416e = false;
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        if (this.f9419h.isEnabled() && ((AbstractC1420s) mouseEvent.getSource()).hasFocus() && this.f9419h.getCursor().getType() != 0) {
            this.f9419h.setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        Point mousePosition;
        Point mousePosition2;
        if (!this.f9419h.L() && (mouseEvent.getSource() instanceof AbstractC1420s)) {
            this.f9419h.dispatchEvent(mouseEvent);
        }
        if (this.f9419h.isEnabled() && this.f9419h.L()) {
            this.f9417f.invalidate();
            if (!this.f9416e) {
                if (this.f9417f == null || (mousePosition = this.f9417f.getParent().getMousePosition()) == null || this.f9417f == null) {
                    return;
                }
                for (int i2 = 0; i2 < this.f9419h.f9592aF.size(); i2++) {
                    ((AbstractC1420s) this.f9419h.f9592aF.get(i2)).setLocation(mousePosition.f12370x - ((Integer) this.f9412a.get(i2)).intValue(), mousePosition.f12371y - ((Integer) this.f9413b.get(i2)).intValue());
                }
                return;
            }
            if (mouseEvent.getSource() instanceof C1425x) {
                this.f9417f.setSize(mouseEvent.getX() - this.f9417f.getX(), mouseEvent.getY() - this.f9417f.getY());
                return;
            }
            if (this.f9414c == null || this.f9417f == null || this.f9418g == null || (mousePosition2 = this.f9417f.getParent().getMousePosition()) == null) {
                return;
            }
            for (int i3 = 0; i3 < this.f9419h.f9592aF.size(); i3++) {
                ((AbstractC1420s) this.f9419h.f9592aF.get(i3)).setSize(((Integer) this.f9414c.get(i3)).intValue() + (mousePosition2.f12370x - this.f9418g.f12370x), ((Integer) this.f9415d.get(i3)).intValue() - (this.f9418g.f12371y - mousePosition2.f12371y));
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (this.f9419h.isEnabled()) {
            AbstractC1420s abstractC1420sA = this.f9417f != null ? this.f9417f : a(mouseEvent);
            if (abstractC1420sA.hasFocus() && mouseEvent.getX() > abstractC1420sA.getWidth() - this.f9419h.f9569am && mouseEvent.getY() > abstractC1420sA.getHeight() - this.f9419h.f9569am) {
                this.f9419h.setCursor(Cursor.getPredefinedCursor(5));
            } else if (this.f9419h.getCursor().getType() != 0) {
                this.f9419h.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private AbstractC1420s a(MouseEvent mouseEvent) {
        AbstractC1420s abstractC1420s = (AbstractC1420s) mouseEvent.getSource();
        AbstractC1420s abstractC1420sA = this.f9419h.a(abstractC1420s.getX() + mouseEvent.getX(), abstractC1420s.getY() + mouseEvent.getY());
        return abstractC1420sA == null ? abstractC1420s : abstractC1420sA;
    }
}
