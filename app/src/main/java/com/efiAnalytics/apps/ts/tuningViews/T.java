package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneViewGaugeCluster;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/T.class */
class T extends MouseAdapter implements MouseMotionListener {

    /* renamed from: a, reason: collision with root package name */
    List f9744a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f9745b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    boolean f9746c = false;

    /* renamed from: d, reason: collision with root package name */
    TuneViewComponent f9747d = null;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ J f9748e;

    T(J j2) {
        this.f9748e = j2;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        boolean z2;
        try {
            if (!this.f9748e.isEnabled()) {
                if (z2) {
                    return;
                } else {
                    return;
                }
            }
            this.f9747d = a(mouseEvent);
            if (this.f9747d == null) {
                if (mouseEvent.getSource() instanceof TuneViewComponent) {
                    this.f9748e.dispatchEvent(mouseEvent);
                    return;
                }
                return;
            }
            Point mousePosition = this.f9747d.getParent() != null ? this.f9747d.getParent().getMousePosition() : this.f9747d.getMousePosition();
            if (mousePosition == null) {
                if (mouseEvent.getSource() instanceof TuneViewComponent) {
                    this.f9748e.dispatchEvent(mouseEvent);
                    return;
                }
                return;
            }
            if (mouseEvent.getButton() == 1 || mouseEvent.getButton() == 3) {
                boolean z3 = (mouseEvent.getModifiers() & 2) != 2;
                if (!this.f9748e.c(this.f9747d) || !z3) {
                    this.f9748e.a(this.f9747d, z3);
                }
            }
            if (mouseEvent.getButton() == 3 && !(this.f9747d instanceof TuneViewGaugeCluster)) {
                this.f9748e.a(this.f9747d, mousePosition.f12370x - this.f9747d.getX(), mousePosition.f12371y - this.f9747d.getY());
                if (this.f9747d != null) {
                    this.f9747d.requestFocus();
                }
                if (mouseEvent.getSource() instanceof TuneViewComponent) {
                    this.f9748e.dispatchEvent(mouseEvent);
                    return;
                }
                return;
            }
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == 1) {
                this.f9748e.t();
                mouseEvent.consume();
            }
            this.f9744a.clear();
            this.f9745b.clear();
            Iterator it = this.f9748e.f9697K.iterator();
            while (it.hasNext()) {
                TuneViewComponent tuneViewComponent = (TuneViewComponent) it.next();
                this.f9744a.add(Integer.valueOf(mousePosition.f12370x - tuneViewComponent.getX()));
                this.f9745b.add(Integer.valueOf(mousePosition.f12371y - tuneViewComponent.getY()));
            }
            this.f9748e.requestFocus();
            this.f9748e.repaint();
            this.f9746c = this.f9744a.size() == 1 && ((Integer) this.f9744a.get(0)).intValue() > this.f9747d.getWidth() - this.f9748e.f9687C && ((Integer) this.f9745b.get(0)).intValue() > this.f9747d.getHeight() - this.f9748e.f9687C;
            if (mouseEvent.getSource() instanceof TuneViewComponent) {
                this.f9748e.dispatchEvent(mouseEvent);
            }
        } finally {
            if (mouseEvent.getSource() instanceof TuneViewComponent) {
                this.f9748e.dispatchEvent(mouseEvent);
            }
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (!this.f9748e.B() && (mouseEvent.getSource() instanceof TuneViewComponent)) {
            this.f9748e.dispatchEvent(mouseEvent);
        }
        if (this.f9748e.isEnabled()) {
            TuneViewComponent tuneViewComponentA = this.f9747d != null ? this.f9747d : a(mouseEvent);
            if (tuneViewComponentA == null) {
                return;
            }
            if (this.f9746c) {
                tuneViewComponentA.setRelativeWidth(tuneViewComponentA.getWidth() / this.f9748e.getWidth());
                tuneViewComponentA.setRelativeHeight(tuneViewComponentA.getHeight() / this.f9748e.getHeight());
                tuneViewComponentA.doLayout();
                tuneViewComponentA.validate();
            } else {
                tuneViewComponentA.setRelativeX(tuneViewComponentA.getX() / this.f9748e.getWidth());
                tuneViewComponentA.setRelativeY(tuneViewComponentA.getY() / this.f9748e.getHeight());
            }
            this.f9746c = false;
            this.f9747d = null;
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        TuneViewComponent tuneViewComponentA;
        if (this.f9748e.isEnabled() && (tuneViewComponentA = a(mouseEvent)) != null && tuneViewComponentA.hasFocus() && this.f9748e.getCursor().getType() != 0) {
            this.f9748e.setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        Point mousePosition;
        int x2;
        int y2;
        if (!this.f9748e.B() && (mouseEvent.getSource() instanceof TuneViewComponent)) {
            this.f9748e.dispatchEvent(mouseEvent);
        }
        if (this.f9748e.isEnabled() && this.f9748e.B()) {
            if (!this.f9746c) {
                if (this.f9747d == null || (mousePosition = this.f9747d.getParent().getMousePosition()) == null || this.f9747d == null) {
                    return;
                }
                for (int i2 = 0; i2 < this.f9748e.f9697K.size(); i2++) {
                    ((TuneViewComponent) this.f9748e.f9697K.get(i2)).setLocation(mousePosition.f12370x - ((Integer) this.f9744a.get(i2)).intValue(), mousePosition.f12371y - ((Integer) this.f9745b.get(i2)).intValue());
                }
                return;
            }
            TuneViewComponent tuneViewComponentA = a(mouseEvent);
            if (mouseEvent.getSource() instanceof J) {
                x2 = mouseEvent.getX() - this.f9747d.getX();
                y2 = mouseEvent.getY() - this.f9747d.getY();
            } else if (tuneViewComponentA == null || !tuneViewComponentA.equals(this.f9748e.f())) {
                Component component = (Component) mouseEvent.getSource();
                Component componentF = this.f9748e.f();
                x2 = mouseEvent.getX() + (component.getLocation().f12370x - componentF.getLocation().f12370x);
                y2 = mouseEvent.getY() + (component.getLocation().f12371y - componentF.getLocation().f12371y);
            } else {
                x2 = mouseEvent.getX();
                y2 = mouseEvent.getY();
            }
            this.f9747d.setSize(x2, y2);
            this.f9747d.doLayout();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
        if (this.f9748e.isEnabled()) {
            TuneViewComponent tuneViewComponentA = this.f9747d != null ? this.f9747d : a(mouseEvent);
            if (this.f9748e.f9697K.size() == 1 && this.f9748e.f9697K.contains(tuneViewComponentA) && mouseEvent.getX() > tuneViewComponentA.getWidth() - this.f9748e.f9687C && mouseEvent.getY() > tuneViewComponentA.getHeight() - this.f9748e.f9687C) {
                this.f9748e.setCursor(Cursor.getPredefinedCursor(5));
            } else if (this.f9748e.getCursor().getType() != 0) {
                this.f9748e.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private TuneViewComponent a(MouseEvent mouseEvent) {
        Component component;
        Component parent = (Component) mouseEvent.getSource();
        while (true) {
            component = parent;
            if ((component instanceof TuneViewComponent) || component.getParent() == null) {
                break;
            }
            parent = component.getParent();
        }
        return component instanceof TuneViewComponent ? (TuneViewComponent) component : this.f9748e.a(component.getX() + mouseEvent.getX(), component.getY() + mouseEvent.getY());
    }
}
